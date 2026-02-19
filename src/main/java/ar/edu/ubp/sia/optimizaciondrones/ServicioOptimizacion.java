package ar.edu.ubp.sia.optimizaciondrones;

import java.text.DecimalFormat;
import java.util.Map;

public class ServicioOptimizacion {

    private final DecimalFormat df = new DecimalFormat("#.##");
    private ResultadoOptimizacion resultado = null;
    private Map<String, Double> volumenProductos;
    private Map<String, Double> pesosProductos;
    private Map<String, Integer> pedido;
    private ConfiguracionAG config;

    public ServicioOptimizacion(Map<String, Double> volumenProductos, Map<String, Double> pesosProductos,
                                Map<String, Integer> pedido, ConfiguracionAG config) {
        this.volumenProductos = volumenProductos;
        this.pesosProductos = pesosProductos;
        this.pedido = pedido;
        this.config = config;
    }

    public void ejecutar() {
        Seleccion seleccion = FabricaOperadores.crearSeleccion(config.getTipoSeleccion());
        Cruza cruza = FabricaOperadores.crearCruza(config.getTipoCruza());
        Mutacion mutacion = FabricaOperadores.crearMutacion(config.getTipoMutacion());

        AlgoritmoGenetico ag = new AlgoritmoGenetico(
                pesosProductos, volumenProductos, pedido, config, seleccion, cruza, mutacion);
        resultado = ag.ejecutar();
    }

    public String outputConsola() {
        StringBuilder sb = new StringBuilder();

        sb.append("PEDIDO DESDE FORMULARIO:\n");
        double pesoTotal = 0;
        double volumenTotal = 0;
        int productosTotal = 0;

        for (Map.Entry<String, Integer> item : pedido.entrySet()) {
            int cantidad = item.getValue();
            double peso = pesosProductos.get(item.getKey()) * cantidad;
            double volumen = volumenProductos.get(item.getKey()) * cantidad;

            pesoTotal += peso;
            volumenTotal += volumen;
            productosTotal += cantidad;

            sb.append("- ").append(item.getKey()).append(": ")
                    .append(cantidad).append(" unidades (")
                    .append(df.format(peso)).append(" kg, ")
                    .append(df.format(volumen)).append(" cm3)\n");
        }

        sb.append("\nTotal: ").append(productosTotal).append(" productos, ")
                .append(df.format(pesoTotal)).append(" kg, ")
                .append(df.format(volumenTotal)).append(" cm3\n");

        sb.append("Viajes mínimos teóricos (peso): ")
                .append((int) Math.ceil(pesoTotal / resultado.getConfiguracion().getPesoMaximoPorViaje())).append("\n");
        sb.append("Viajes mínimos teóricos (volumen): ")
                .append((int) Math.ceil(volumenTotal / resultado.getConfiguracion().getCapacidadVolumenCaja())).append("\n");

        sb.append("\nConfiguración:\n")
                .append("- Selección: ").append(resultado.getConfiguracion().getTipoSeleccion()).append("\n")
                .append("- Cruza: ").append(resultado.getConfiguracion().getTipoCruza()).append("\n")
                .append("- Mutación: ").append(resultado.getConfiguracion().getTipoMutacion()).append("\n")
                .append("- Población: ").append(resultado.getConfiguracion().getTamanoPoblacion()).append("\n")
                .append("- Generaciones: ").append(resultado.getConfiguracion().getNumeroGeneraciones()).append("\n")
                .append("- Máximo peso por viaje: ").append(resultado.getConfiguracion().getPesoMaximoPorViaje()).append(" kg\n")
                .append("- Máximo volumen por viaje: ").append(resultado.getConfiguracion().getCapacidadVolumenCaja()).append(" cm3\n");

        sb.append("\nRESULTADOS:\n")
                .append("- Aptitud alcanzada: ").append(df.format(resultado.getMejorAptitud())).append("\n")
                .append("- Viajes necesarios: ").append(resultado.getViajes().size()).append("\n");

        sb.append("\nDetalle de viajes:\n");
        for (int i = 0; i < resultado.getViajes().size(); i++) {
            ViajeOptimo viaje = resultado.getViajes().get(i);
            double peso = viaje.getPesoTotal();
            double volumen = viaje.getVolumenTotal();
            double porcentajePeso = (peso / resultado.getConfiguracion().getPesoMaximoPorViaje()) * 100;
            double porcentajeVolumen = (volumen / resultado.getConfiguracion().getCapacidadVolumenCaja()) * 100;

            sb.append("Viaje ").append(i + 1).append(":\n");
            sb.append("- Peso: ").append(df.format(peso)).append(" kg (")
                    .append(df.format(porcentajePeso)).append("% capacidad)\n");
            sb.append("- Volumen: ").append(df.format(volumen)).append(" cm3 (")
                    .append(df.format(porcentajeVolumen)).append("% capacidad)\n");
            sb.append("- Productos: ");

            boolean primero = true;
            for (Map.Entry<String, Integer> prod : viaje.getProductos().entrySet()) {
                if (prod.getValue() > 0) {
                    if (!primero) sb.append(", ");
                    sb.append(prod.getKey()).append(" x").append(prod.getValue());
                    primero = false;
                }
            }
            sb.append("\n");
        }

        resultado.calcularEstadisticas();
        sb.append("\nEstadísticas:\n")
                .append("- Peso total transportado: ").append(df.format(resultado.getPesoTotalTransportado())).append(" kg\n")
                .append("- Volumen total transportado: ").append(df.format(resultado.getVolumenTotalTransportado())).append(" cm3\n")
                .append("- Eficiencia promedio (peso): ").append(df.format(resultado.getEficienciaPesoPromedio())).append("%\n")
                .append("- Eficiencia promedio (volumen): ").append(df.format(resultado.getEficienciaVolumenPromedio())).append("%\n")
                .append("- Viajes eficientes (peso >80%): ").append(resultado.contarViajesConEficienciaPesoMinima(80.0)).append("\n")
                .append("- Viajes eficientes (volumen >80%): ").append(resultado.contarViajesConEficienciaVolumenMinima(80.0)).append("\n");

        return sb.toString();
    }

    public ResultadoOptimizacion getResultado() {
        return resultado;
    }

}
