package ar.edu.ubp.sia.optimizaciondrones;

/**
 * Clase que encapsula la configuración del Algoritmo Genético
 *
 */
public class ConfiguracionAG {

    private String tipoSeleccion;
    private String tipoCruza;
    private String tipoMutacion;
    private int tamanoPoblacion;
    private int numeroGeneraciones;
    private double probabilidadCruza;
    private double probabilidadMutacion;
    private int pesoMaximoPorViaje;
    private double capacidadVolumenCaja;


    public ConfiguracionAG(String tipoSeleccion, String tipoCruza, String tipoMutacion,
                           int tamanoPoblacion, int numeroGeneraciones, double probabilidadCruza,
                           double probabilidadMutacion, int pesoMaximoPorViaje, double capacidadVolumenCaja) {
        this.tipoSeleccion = tipoSeleccion;
        this.tipoCruza = tipoCruza;
        this.tipoMutacion = tipoMutacion;
        this.tamanoPoblacion = tamanoPoblacion;
        this.numeroGeneraciones = numeroGeneraciones;
        this.probabilidadCruza = probabilidadCruza;
        this.probabilidadMutacion = probabilidadMutacion;
        this.pesoMaximoPorViaje = pesoMaximoPorViaje;
        this.capacidadVolumenCaja = capacidadVolumenCaja;
    }

    /**
     * Valida los parámetros mínimos para ejecución del algoritmo
     * @return true si configuración es válida
     */
    public boolean esValida() {
        return tamanoPoblacion > 0 &&
                numeroGeneraciones > 0 &&
                probabilidadCruza >= 0 && probabilidadCruza <= 1 &&
                probabilidadMutacion >= 0 && probabilidadMutacion <= 1 &&
                tipoSeleccion != null && !tipoSeleccion.isEmpty() &&
                tipoCruza != null && !tipoCruza.isEmpty() &&
                tipoMutacion != null && !tipoMutacion.isEmpty();
    }

    public String getTipoSeleccion() { return tipoSeleccion; }

    public String getTipoCruza() { return tipoCruza; }

    public String getTipoMutacion() { return tipoMutacion; }

    public int getTamanoPoblacion() { return tamanoPoblacion; }

    public int getNumeroGeneraciones() { return numeroGeneraciones; }

    public double getProbabilidadCruza() { return probabilidadCruza; }

    public double getProbabilidadMutacion() { return probabilidadMutacion; }

    public int getPesoMaximoPorViaje() {
        return pesoMaximoPorViaje;
    }

    public double getCapacidadVolumenCaja() {
        return capacidadVolumenCaja;
    }

    @Override
    public String toString() {
        return String.format("ConfiguracionAG[seleccion=%s, cruza=%s, mutacion=%s, poblacion=%d, generaciones=%d, pCruza=%.2f, pMutacion=%.2f]",
                tipoSeleccion, tipoCruza, tipoMutacion, tamanoPoblacion, numeroGeneraciones, probabilidadCruza, probabilidadMutacion);
    }

}