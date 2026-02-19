package ar.edu.ubp.sia.optimizaciondrones;

import java.util.*;

/**
 * Clase principal que ejecuta el Algoritmo Genético para optimización de viajes de drones,
 * considerando peso y volumen máximo por viaje.
 */
public class AlgoritmoGenetico {

    private final Map<String, Double> pesosProductos;
    private final Map<String, Double> volumenProductos; // volumen por producto
    private final Map<String, Integer> pedido;           // pedido: producto -> cantidad
    private final ConfiguracionAG config;

    private final Seleccion operadorSeleccion;
    private final Cruza operadorCruza;
    private final Mutacion operadorMutacion;

    private List<Cromosoma> poblacion;
    private final int longitudCromosoma;

    private final Map<String, Integer> productoIdMap;
    private final Map<Integer, String> idProductoMap;

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

        // La longitud del cromosoma es la suma total de unidades a enviar
        this.longitudCromosoma = pedido.values().stream().mapToInt(Integer::intValue).sum();

        this.poblacion = new ArrayList<>();

        // Construir mapas producto <-> id
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
     * Crea un arreglo de genes con todos los productos según la cantidad pedida, mezclado aleatoriamente.
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

        // Barajar genes para diversidad inicial
        Random rnd = new Random(42);
        for (int i = genes.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int temp = genes[i];
            genes[i] = genes[j];
            genes[j] = temp;
        }

        return genes;
    }

    /**
     * Inicializa la población con cromosomas aleatorios válidos (genes secuenciales mezclados).
     */
    private void inicializarPoblacion() {
        poblacion.clear();
        for (int i = 0; i < config.getTamanoPoblacion(); i++) {
            int[] genes = crearGenesIniciales();
            Cromosoma cromosoma = new Cromosoma(genes);
            poblacion.add(cromosoma);
        }
    }

    /**
     * Evalúa la población asignando valores de aptitud a cada cromosoma.
     */
    private void evaluarPoblacion() {
        for (Cromosoma cromosoma : poblacion) {
            double fitness = evaluarFitness(cromosoma);
            cromosoma.setFitness(fitness);
        }
    }

    /**
     * Evalúa el fitness de un cromosoma según el uso total de peso y volumen en viajes válidos.
     */
    private double evaluarFitness(Cromosoma cromosoma) {
        List<ViajeOptimo> viajes = decodificarCromosomaAViajes(cromosoma);

        // Contar productos en viajes
        Map<String, Integer> productosEnViajes = new HashMap<>();
        for (ViajeOptimo viaje : viajes) {
            for (Map.Entry<String, Integer> entry : viaje.getProductos().entrySet()) {
                productosEnViajes.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }

        // Penalizar si faltan productos del pedido
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

        // Penalización para menos viajes (mejor si menos viajes)
        double penalizacionViajes = Math.pow(1.0 / viajes.size(), 2);
        // Bonus para mejor aprovechamiento de peso/volumen
        double bonusAprovechamiento = Math.pow(aprovechamientoPromedio, 3);

        double pesoAprovechamiento = 0.7;
        double pesoViajes = 0.3;

        double fitness = (bonusAprovechamiento * pesoAprovechamiento) + (penalizacionViajes * pesoViajes);

        return fitness;
    }

    /**
     * Decodifica el cromosoma secuencial en viajes optimizando peso y volumen.
     */
    private List<ViajeOptimo> decodificarCromosomaAViajes(Cromosoma cromosoma) {
        List<ViajeOptimo> viajes = new ArrayList<>();
        int numeroViaje = 1;
        ViajeOptimo viajeActual = new ViajeOptimo(numeroViaje);

        double pesoActual = 0.0;
        double volumenActual = 0.0;

        int productosAgregados = 0;  // contador total agregado

        for (int gen : cromosoma.getGenes()) {
            String producto = idProductoMap.get(gen);
            if (producto == null) {
                System.out.println("Producto con id " + gen + " no encontrado en idProductoMap");
                continue;
            }

            double pesoUnitario = pesosProductos.get(producto);
            double volumenUnitario = volumenProductos.get(producto);

            // Verifica si cabe en el viaje actual, si no, crea uno nuevo
            if (pesoActual + pesoUnitario > config.getPesoMaximoPorViaje() ||
                    volumenActual + volumenUnitario > config.getCapacidadVolumenCaja()) {
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

            productosAgregados++;
        }

        if (!viajeActual.estaVacio()) {
            viajes.add(viajeActual);
        }

        System.out.println("Total productos esperados (genes): " + cromosoma.getLongitud());
        System.out.println("Total productos agregados a viajes: " + productosAgregados);

        return viajes;
    }

    /**
     * Ejecuta el algoritmo genético principal.
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

            // Guardamos el mejor cromosoma de la generación anterior (elitismo)
            Collections.sort(poblacion);
            Cromosoma mejor = poblacion.get(0);
            nuevaPoblacion.add(mejor.clonar());

            while (nuevaPoblacion.size() < config.getTamanoPoblacion()) {
                Cromosoma padre1 = operadorSeleccion.seleccionar(poblacion);
                Cromosoma padre2 = operadorSeleccion.seleccionar(poblacion);

                Cromosoma[] hijos = (Math.random() < config.getProbabilidadCruza())
                        ? operadorCruza.cruzar(padre1, padre2)
                        : new Cromosoma[]{padre1.clonar(), padre2.clonar()};

                if (Math.random() < config.getProbabilidadMutacion()) operadorMutacion.mutar(hijos[0]);
                if (Math.random() < config.getProbabilidadMutacion() && nuevaPoblacion.size() + 1 < config.getTamanoPoblacion()) operadorMutacion.mutar(hijos[1]);

                nuevaPoblacion.add(hijos[0]);
                if (nuevaPoblacion.size() < config.getTamanoPoblacion()) {
                    nuevaPoblacion.add(hijos[1]);
                }
            }

            poblacion = nuevaPoblacion;
            evaluarPoblacion();

            // Actualizamos mejor resultado si corresponde
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
