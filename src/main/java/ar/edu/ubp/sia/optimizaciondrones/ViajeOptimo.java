package ar.edu.ubp.sia.optimizaciondrones;

import java.util.HashMap;
import java.util.Map;

/**
 * Representa un viaje realizado por un dron, incluyendo los productos transportados y el peso total.
 */
public class ViajeOptimo {

    /** Mapa de productos y sus respectivas cantidades. */
    private final Map<String, Integer> productos;

    /** Peso total de todos los productos del viaje. */
    private double pesoTotal;

    /** Volumen total de todos los productos del viaje. */
    private double volumenTotal;

    /** Número identificador del viaje. */
    private int numeroViaje;

    /**
     * Crea un viaje vacío con número por defecto.
     */
    public ViajeOptimo() {
        this.productos = new HashMap<>();
        this.pesoTotal = 0.0;
        this.numeroViaje = 0;
    }

    /**
     * Crea un viaje vacío con un número de identificación especificado.
     *
     * @param numeroViaje el número del viaje
     */
    public ViajeOptimo(int numeroViaje) {
        this();
        this.numeroViaje = numeroViaje;
    }

    /**
     * Verifica si el viaje está dentro del límite de peso permitido.
     *
     * @param pesoMaximo el peso máximo que puede cargar el dron
     * @return true si el peso total es menor o igual al máximo permitido, false en caso contrario
     */
    public boolean estaDentroDelLimite(double pesoMaximo) {
        return pesoTotal <= pesoMaximo;
    }

    /**
     * Calcula el porcentaje de utilización de la capacidad de carga del dron.
     *
     * @param pesoMaximo el peso máximo permitido
     * @return el porcentaje de capacidad utilizada
     */
    public double calcularPorcentajeUtilizacion(double pesoMaximo) {
        if (pesoMaximo <= 0) {
            return 0.0;
        }
        return (pesoTotal / pesoMaximo) * 100.0;
    }

    /**
     * Devuelve la cantidad total de productos cargados en el viaje.
     *
     * @return la suma de todas las unidades de productos
     */
    public int contarCantidadTotalDeProductos() {
        return productos.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Verifica si el viaje está vacío (sin productos o todos en cero).
     *
     * @return true si no hay productos cargados, false en caso contrario
     */
    public boolean estaVacio() {
        return productos.isEmpty() || contarCantidadTotalDeProductos() == 0;
    }

    /**
     * Elimina todos los productos del viaje y reinicia el peso total.
     */
    public void limpiar() {
        productos.clear();
        pesoTotal = 0.0;
    }

    /**
     * Agrega un producto al viaje.
     *
     * @param nombre     el nombre del producto
     * @param cantidad   la cantidad a agregar
     * @param pesoUnitario el peso de una unidad del producto
     */
    public void agregarProducto(String nombre, int cantidad, double pesoUnitario) {
        if (cantidad <= 0 || pesoUnitario < 0) return;

        productos.merge(nombre, cantidad, Integer::sum);
        pesoTotal += cantidad * pesoUnitario;
    }

    // Getters y Setters

    public Map<String, Integer> getProductos() {
        return productos;
    }

    public double getPesoTotal() {
        return pesoTotal;
    }

    public int getNumeroViaje() {
        return numeroViaje;
    }

    public void setNumeroViaje(int numeroViaje) {
        this.numeroViaje = numeroViaje;
    }

    public void agregarVolumen(double volumen) {
        this.volumenTotal += volumen;
    }

    public double getVolumenTotal() {
        return volumenTotal;
    }

    public double calcularPorcentajeUtilizacionVolumen(double volumenMaximo) {
        if (volumenMaximo <= 0) return 0.0;
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
