package ar.edu.ubp.sia.optimizaciondrones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Ejecuta la lógica principal del algoritmo genético para optimizar viajes de drones.
 * El fitness combina aprovechamiento de peso/volumen y minimización de cantidad de viajes.
 */
public class AlgoritmoGenetico {

    private final Map<String, Double> pesosProductos;
    private final Map<String, Double> volumenProductos;
    private final Map<String, Integer> pedido;
    private final ConfiguracionAG config;
    private final Seleccion operadorSeleccion;
    private final Cruza operadorCruza;
    private final Mutacion operadorMutacion;
    private final int longitudCromosoma;
    private final Map<String, Integer> productoIdMap;
    private final Map<Integer, String> idProductoMap;

    private List<Cromosoma> poblacion;

    /**
     * Crea una instancia lista para ejecutar evolución sobre un pedido concreto.
     *
     * @param pesosProductos peso unitario por producto
     * @param volumenProductos volumen unitario por producto
     * @param pedido cantidad solicitada por producto
     * @param config parámetros de ejecución del algoritmo
     * @param seleccion estrategia de selección
     * @param cruza estrategia de cruza
     * @param mutacion estrategia de mutación
     */
    public AlgoritmoGenetico(Map<String, Double> pesosProductos,
                             Map<String, Double> volumenProductos,
                             Map<String, Integer> pedido,
                             ConfiguracionAG config,
                             Seleccion seleccion,
                             Cruza cruza,
                             Mutacion mutacion) {
        this.pesosProductos = pesosProductos;
        this.volumenProductos = volumenProductos;
        this.pedido = pedido;
        this.config = config;
        this.operadorSeleccion = seleccion;
        this.operadorCruza = cruza;
        this.operadorMutacion = mutacion;
        this.longitudCromosoma = pedido.values().stream().mapToInt(Integer::intValue).sum();

        this.poblacion = new ArrayList<>();
        this.productoIdMap = new HashMap<>();
        this.idProductoMap = new HashMap<>();

        int id = 0;
        for (String producto : pedido.keySet()) {
            productoIdMap.put(producto, id);
            idProductoMap.put(id, producto);
            id++;
        }
    }

    /**
     * Genera un set de genes inicial según las cantidades del pedido y lo mezcla aleatoriamente.
     */
    private int[] crearGenesIniciales() {
        int[] genes = new int[longitudCromosoma];
        int index = 0;

        for (String producto : pedido.keySet()) {
            int cantidad = pedido.get(producto);
            int idProducto = productoIdMap.get(producto);
            for (int i = 0; i < cantidad; i++) {
                genes[index++] = idProducto;
            }
        }

        Random random = new Random(42);
        for (int i = genes.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = genes[i];
            genes[i] = genes[j];
            genes[j] = temp;
        }

        return genes;
    }

    /** Inicializa la población con cromosomas válidos del tamaño configurado. */
    private void inicializarPoblacion() {
        poblacion.clear();
        for (int i = 0; i < config.getTamanoPoblacion(); i++) {
            poblacion.add(new Cromosoma(crearGenesIniciales()));
        }
    }

    /** Evalúa la aptitud de cada cromosoma de la población actual. */
    private void evaluarPoblacion() {
        for (Cromosoma cromosoma : poblacion) {
            cromosoma.setFitness(evaluarFitness(cromosoma));
        }
    }

    /**
     * Calcula el fitness de un cromosoma considerando cobertura completa del pedido,
     * eficiencia media de carga y penalización por número de viajes.
     */
    private double evaluarFitness(Cromosoma cromosoma) {
        List<ViajeOptimo> viajes = decodificarCromosomaAViajes(cromosoma);
        Map<String, Integer> productosEnViajes = new HashMap<>();

        for (ViajeOptimo viaje : viajes) {
            for (Map.Entry<String, Integer> entry : viaje.getProductos().entrySet()) {
                productosEnViajes.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }

        for (String producto : pedido.keySet()) {
            int cantidadPedido = pedido.get(producto);
            int cantidadEnviada = productosEnViajes.getOrDefault(producto, 0);
            if (cantidadEnviada < cantidadPedido) {
                return 0.0;
            }
        }

        double sumaAprovechamiento = 0.0;
        for (ViajeOptimo viaje : viajes) {
            double usoPeso = viaje.calcularPorcentajeUtilizacion(config.getPesoMaximoPorViaje());
            double usoVolumen = viaje.calcularPorcentajeUtilizacionVolumen(config.getCapacidadVolumenCaja());
            sumaAprovechamiento += (usoPeso + usoVolumen) / 2.0;
        }

        double aprovechamientoPromedio = sumaAprovechamiento / viajes.size();
        double penalizacionViajes = Math.pow(1.0 / viajes.size(), 2);
        double bonusAprovechamiento = Math.pow(aprovechamientoPromedio, 3);

        return (bonusAprovechamiento * 0.7) + (penalizacionViajes * 0.3);
    }

    /**
     * Convierte el cromosoma en una secuencia de viajes respetando límites de peso y volumen.
     */
    private List<ViajeOptimo> decodificarCromosomaAViajes(Cromosoma cromosoma) {
        List<ViajeOptimo> viajes = new ArrayList<>();
        int numeroViaje = 1;
        ViajeOptimo viajeActual = new ViajeOptimo(numeroViaje);
        double pesoActual = 0.0;
        double volumenActual = 0.0;

        for (int gen : cromosoma.getGenes()) {
            String producto = idProductoMap.get(gen);
            if (producto == null) {
                continue;
            }

            double pesoUnitario = pesosProductos.get(producto);
            double volumenUnitario = volumenProductos.get(producto);

            if (pesoActual + pesoUnitario > config.getPesoMaximoPorViaje()
                    || volumenActual + volumenUnitario > config.getCapacidadVolumenCaja()) {
                if (!viajeActual.estaVacio()) {
                    viajes.add(viajeActual);
                }
                viajeActual = new ViajeOptimo(++numeroViaje);
                pesoActual = 0.0;
                volumenActual = 0.0;
            }

            viajeActual.agregarProducto(producto, 1, pesoUnitario);
            viajeActual.agregarVolumen(volumenUnitario);
            pesoActual += pesoUnitario;
            volumenActual += volumenUnitario;
        }

        if (!viajeActual.estaVacio()) {
            viajes.add(viajeActual);
        }

        return viajes;
    }

    /**
     * Ejecuta el proceso evolutivo completo y devuelve el mejor resultado encontrado.
     *
     * @return resultado con mejor solución, historial de fitness y metadatos de ejecución
     */
    public ResultadoOptimizacion ejecutar() {
        List<Double> historialFitness = new ArrayList<>();
        inicializarPoblacion();
        evaluarPoblacion();

        ResultadoOptimizacion resultado = new ResultadoOptimizacion();
        resultado.setConfiguracion(config);
        resultado.setGeneracionesEjecutadas(config.getNumeroGeneraciones());

        for (int gen = 0; gen < config.getNumeroGeneraciones(); gen++) {
            List<Cromosoma> nuevaPoblacion = new ArrayList<>();

            Collections.sort(poblacion);
            Cromosoma mejor = poblacion.get(0);
            nuevaPoblacion.add(mejor.clonar());

            while (nuevaPoblacion.size() < config.getTamanoPoblacion()) {
                Cromosoma padre1 = operadorSeleccion.seleccionar(poblacion);
                Cromosoma padre2 = operadorSeleccion.seleccionar(poblacion);

                Cromosoma[] hijos = (Math.random() < config.getProbabilidadCruza())
                        ? operadorCruza.cruzar(padre1, padre2)
                        : new Cromosoma[]{padre1.clonar(), padre2.clonar()};

                if (Math.random() < config.getProbabilidadMutacion()) {
                    operadorMutacion.mutar(hijos[0]);
                }
                if (Math.random() < config.getProbabilidadMutacion()
                        && nuevaPoblacion.size() + 1 < config.getTamanoPoblacion()) {
                    operadorMutacion.mutar(hijos[1]);
                }

                nuevaPoblacion.add(hijos[0]);
                if (nuevaPoblacion.size() < config.getTamanoPoblacion()) {
                    nuevaPoblacion.add(hijos[1]);
                }
            }

            poblacion = nuevaPoblacion;
            evaluarPoblacion();

            Collections.sort(poblacion);
            mejor = poblacion.get(0);
            if (mejor.getFitness() > resultado.getMejorAptitud()) {
                resultado.setMejorAptitud(mejor.getFitness());
                resultado.setGeneracionMejor(gen);
                resultado.setViajes(decodificarCromosomaAViajes(mejor));
            }
            historialFitness.add(mejor.getFitness());
        }

        resultado.setHistorialFitness(historialFitness);
        return resultado;
    }
}
