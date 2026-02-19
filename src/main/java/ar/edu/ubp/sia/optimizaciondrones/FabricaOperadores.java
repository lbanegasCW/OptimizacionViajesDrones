package ar.edu.ubp.sia.optimizaciondrones;

/**
 * Fábrica centralizada para crear operadores genéticos según su nombre de configuración.
 */
public class FabricaOperadores {

    /** Crea estrategia de selección según etiqueta de UI. */
    public static Seleccion crearSeleccion(String tipo) {
        return switch (tipo) {
            case "Torneo" -> new SeleccionTorneo(6);
            case "Ruleta" -> new SeleccionRuleta();
            case "Ranking" -> new SeleccionRanking();
            default -> throw new IllegalArgumentException("Selección inválida: " + tipo);
        };
    }

    /** Crea estrategia de cruza según etiqueta de UI. */
    public static Cruza crearCruza(String tipo) {
        return switch (tipo) {
            case "Un Punto" -> new CruzaUnPunto();
            case "Dos Puntos" -> new CruzaDosPuntos();
            case "Uniforme" -> new CruzaUniforme();
            default -> throw new IllegalArgumentException("Cruza inválida: " + tipo);
        };
    }

    /** Crea estrategia de mutación según etiqueta de UI. */
    public static Mutacion crearMutacion(String tipo) {
        return switch (tipo) {
            case "Simple" -> new MutacionSimple();
            case "Intercambio" -> new MutacionIntercambio();
            case "Inversión" -> new MutacionInversion();
            default -> throw new IllegalArgumentException("Mutación inválida: " + tipo);
        };
    }
}
