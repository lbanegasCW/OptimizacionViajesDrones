package ar.edu.ubp.sia.optimizaciondrones;

import java.util.HashMap;
import java.util.Map;

/**
 * Entidad de dominio que representa un viaje optimizado con su carga de productos.
 */
public class ViajeOptimo {

    private final Map<String, Integer> productos;
    private double pesoTotal;
    private double volumenTotal;
    private int numeroViaje;

    /** Crea un viaje vacío sin número asignado. */
    public ViajeOptimo() {
        this.productos = new HashMap<>();
        this.pesoTotal = 0.0;
        this.numeroViaje = 0;
    }

    /**
     * Crea un viaje vacío con un número de identificación.
     *
     * @param numeroViaje identificador del viaje
     */
    public ViajeOptimo(int numeroViaje) {
        this();
        this.numeroViaje = numeroViaje;
    }

    /** @return true cuando el peso total no excede el máximo permitido. */
    public boolean estaDentroDelLimite(double pesoMaximo) {
        return pesoTotal <= pesoMaximo;
    }

    /** @return porcentaje de uso de capacidad de peso. */
    public double calcularPorcentajeUtilizacion(double pesoMaximo) {
        if (pesoMaximo <= 0) {
            return 0.0;
        }
        return (pesoTotal / pesoMaximo) * 100.0;
    }

    /** @return cantidad total de unidades cargadas en el viaje. */
    public int contarCantidadTotalDeProductos() {
        return productos.values().stream().mapToInt(Integer::intValue).sum();
    }

    /** @return true si no hay productos cargados. */
    public boolean estaVacio() {
        return productos.isEmpty() || contarCantidadTotalDeProductos() == 0;
    }

    /** Limpia productos y métricas acumuladas del viaje. */
    public void limpiar() {
        productos.clear();
        pesoTotal = 0.0;
        volumenTotal = 0.0;
    }

    /**
     * Incorpora producto al viaje y acumula su peso.
     */
    public void agregarProducto(String nombre, int cantidad, double pesoUnitario) {
        if (cantidad <= 0 || pesoUnitario < 0) {
            return;
        }
        productos.merge(nombre, cantidad, Integer::sum);
        pesoTotal += cantidad * pesoUnitario;
    }

    /** @return mapa de productos y cantidades. */
    public Map<String, Integer> getProductos() {
        return productos;
    }

    /** @return peso total transportado en kg. */
    public double getPesoTotal() {
        return pesoTotal;
    }

    /** @return número de viaje. */
    public int getNumeroViaje() {
        return numeroViaje;
    }

    /** @param numeroViaje nuevo identificador de viaje. */
    public void setNumeroViaje(int numeroViaje) {
        this.numeroViaje = numeroViaje;
    }

    /** Acumula volumen de la carga. */
    public void agregarVolumen(double volumen) {
        this.volumenTotal += volumen;
    }

    /** @return volumen total transportado. */
    public double getVolumenTotal() {
        return volumenTotal;
    }

    /** @return porcentaje de uso de capacidad de volumen. */
    public double calcularPorcentajeUtilizacionVolumen(double volumenMaximo) {
        if (volumenMaximo <= 0) {
            return 0.0;
        }
        return (volumenTotal / volumenMaximo) * 100.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Viaje #%d - Peso total: %.2f kg\n", numeroViaje, pesoTotal));
        productos.forEach((producto, cantidad) -> {
            if (cantidad > 0) {
                sb.append(String.format("  %s: %d unidad%s\n", producto, cantidad, cantidad > 1 ? "es" : ""));
            }
        });
        return sb.toString();
    }
}
