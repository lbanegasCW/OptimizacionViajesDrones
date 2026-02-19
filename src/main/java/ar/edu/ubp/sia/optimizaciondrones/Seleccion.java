package ar.edu.ubp.sia.optimizaciondrones;

import java.util.List;

/**
 * Interface para operador de selección
 */
public interface Seleccion {
    Cromosoma seleccionar(List<Cromosoma> poblacion);
}
