package ar.edu.ubp.sia.optimizaciondrones;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Representa los resultados finales de la ejecución del algoritmo genético.
 * Incluye información sobre viajes, estadísticas y parámetros de configuración.
 */
public class ResultadoOptimizacion {

    private List<ViajeOptimo> viajes;
    private double mejorAptitud;
    private int generacionMejor;
    private int generacionesEjecutadas;
    private long tiempoEjecucion;
    private List<Double> historialFitness = new ArrayList<>();
    private ConfiguracionAG configuracion;

    private double pesoTotalTransportado = -1;
    private double volumenTotalTransportado = -1;
    private double eficienciaPesoPromedio = -1;
    private double eficienciaVolumenPromedio = -1;
    private int totalProductosTransportados = -1;

    public ResultadoOptimizacion() {}

    public double getMejorAptitud() {
        return mejorAptitud;
    }

    public void setViajes(List<ViajeOptimo> viajes) {
        this.viajes = viajes;
    }

    public void setConfiguracion(ConfiguracionAG config) {
        this.configuracion = config;
    }

    public void setMejorAptitud(double mejorAptitud) {
        this.mejorAptitud = mejorAptitud;
    }

    public void setGeneracionMejor(int generacionMejor) {
        this.generacionMejor = generacionMejor;
    }

    public void setGeneracionesEjecutadas(int generacionesEjecutadas) {
        this.generacionesEjecutadas = generacionesEjecutadas;
    }

    public void setTiempoEjecucion(long tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public List<Double> getHistorialFitness() {
        return historialFitness;
    }

    public void setHistorialFitness(List<Double> historialFitness) {
        this.historialFitness = historialFitness;
    }

    /**
     * Calcula estadísticas generales a partir de los viajes optimizados.
     */
    public void calcularEstadisticas() {
        if (viajes == null || viajes.isEmpty() || configuracion == null) return;

        double sumaPesos = 0.0;
        double sumaVolumenes = 0.0;
        double sumaEficienciaPeso = 0.0;
        double sumaEficienciaVolumen = 0.0;
        int contadorProductos = 0;

        double pesoMax = configuracion.getPesoMaximoPorViaje();
        double volumenMax = configuracion.getCapacidadVolumenCaja();

        for (ViajeOptimo viaje : viajes) {
            double peso = viaje.getPesoTotal();
            double volumen = viaje.getVolumenTotal();

            sumaPesos += peso;
            sumaVolumenes += volumen;

            sumaEficienciaPeso += viaje.calcularPorcentajeUtilizacion(pesoMax);
            sumaEficienciaVolumen += viaje.calcularPorcentajeUtilizacionVolumen(volumenMax);

            contadorProductos += viaje.contarCantidadTotalDeProductos();
        }

        this.pesoTotalTransportado = sumaPesos;
        this.volumenTotalTransportado = sumaVolumenes;
        this.eficienciaPesoPromedio = sumaEficienciaPeso / viajes.size();
        this.eficienciaVolumenPromedio = sumaEficienciaVolumen / viajes.size();
        this.totalProductosTransportados = contadorProductos;
    }

    /**
     * Devuelve un resumen en forma de String del resultado de la optimización.
     */
    public String obtenerResumen() {
        calcularEstadisticas();

        StringBuilder sb = new StringBuilder();
        sb.append("==== RESUMEN DE OPTIMIZACIÓN ====\n");
        sb.append(String.format("Número de viajes: %d\n", viajes != null ? viajes.size() : 0));
        sb.append(String.format("Mejor fitness: %.4f\n", mejorAptitud));
        sb.append(String.format("Peso total transportado: %.2f kg\n", pesoTotalTransportado));
        sb.append(String.format("Volumen total transportado: %.2f cm³\n", volumenTotalTransportado));
        sb.append(String.format("Eficiencia promedio (peso): %.1f%%\n", eficienciaPesoPromedio));
        sb.append(String.format("Eficiencia promedio (volumen): %.1f%%\n", eficienciaVolumenPromedio));
        sb.append(String.format("Total productos transportados: %d\n", totalProductosTransportados));
        sb.append(String.format("Generación óptima: %d\n", generacionMejor));
        sb.append(String.format("Tiempo ejecución: %.2f segundos\n", tiempoEjecucion / 1000.0));
        return sb.toString();
    }

    // Getters adicionales
    public double getPesoTotalTransportado() {
        if (pesoTotalTransportado < 0) calcularEstadisticas();
        return pesoTotalTransportado;
    }

    public double getVolumenTotalTransportado() {
        if (volumenTotalTransportado < 0) calcularEstadisticas();
        return volumenTotalTransportado;
    }

    public double getEficienciaPesoPromedio() {
        if (eficienciaPesoPromedio < 0) calcularEstadisticas();
        return eficienciaPesoPromedio;
    }

    public double getEficienciaVolumenPromedio() {
        if (eficienciaVolumenPromedio < 0) calcularEstadisticas();
        return eficienciaVolumenPromedio;
    }

    public int getTotalProductosTransportados() {
        if (totalProductosTransportados < 0) calcularEstadisticas();
        return totalProductosTransportados;
    }

    public List<ViajeOptimo> getViajes() {
        return viajes;
    }

    public ConfiguracionAG getConfiguracion() {
        return configuracion;
    }

    public int contarViajesConEficienciaPesoMinima(double umbralPorcentaje) {
        if (viajes == null) return 0;

        return (int) viajes.stream()
                .mapToDouble(v -> v.calcularPorcentajeUtilizacion(configuracion.getPesoMaximoPorViaje()))
                .filter(ef -> ef >= umbralPorcentaje)
                .count();
    }

    public int contarViajesConEficienciaVolumenMinima(double umbralPorcentaje) {
        if (viajes == null) return 0;

        return (int) viajes.stream()
                .mapToDouble(v -> v.calcularPorcentajeUtilizacionVolumen(configuracion.getCapacidadVolumenCaja()))
                .filter(ef -> ef >= umbralPorcentaje)
                .count();
    }

    @Override
    public String toString() {
        return obtenerResumen();
    }
}
