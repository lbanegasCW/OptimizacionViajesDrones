package ar.edu.ubp.sia.optimizaciondrones;

/**
 * Interface para operador de cruza
 */
public interface Cruza {
    Cromosoma[] cruzar(Cromosoma padre1, Cromosoma padre2);
}
