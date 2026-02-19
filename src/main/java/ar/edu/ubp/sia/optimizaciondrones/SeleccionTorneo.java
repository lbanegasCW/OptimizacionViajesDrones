package ar.edu.ubp.sia.optimizaciondrones;

import java.util.List;
import java.util.Random;

/**
 * Selección por torneo
 */
public class SeleccionTorneo implements Seleccion {

    private final int tamanioTorneo;
    private final Random random = new Random();

    public SeleccionTorneo(int tamanioTorneo) {
        this.tamanioTorneo = tamanioTorneo;
    }

    @Override
    public Cromosoma seleccionar(List<Cromosoma> poblacion) {
        Cromosoma mejor = null;
        for (int i = 0; i < tamanioTorneo; i++) {
            Cromosoma candidato = poblacion.get(random.nextInt(poblacion.size()));
            if (mejor == null || candidato.getFitness() > mejor.getFitness()) {
                mejor = candidato;
            }
        }
        return mejor;
    }

}