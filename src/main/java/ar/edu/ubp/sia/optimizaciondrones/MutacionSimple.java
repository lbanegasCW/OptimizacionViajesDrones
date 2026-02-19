package ar.edu.ubp.sia.optimizaciondrones;

import java.util.Random;

/** Mutación puntual que reemplaza un gen por otro valor aleatorio válido. */
public class MutacionSimple implements Mutacion {

    private final Random random = new Random();

    /** {@inheritDoc} */
    @Override
    public void mutar(Cromosoma cromosoma) {
        int longitud = cromosoma.getLongitud();
        if (longitud == 0) {
            return;
        }

        int posicion = random.nextInt(longitud);
        int nuevoGen;
        do {
            nuevoGen = random.nextInt(longitud);
        } while (nuevoGen == cromosoma.getGene(posicion));

        cromosoma.setGene(posicion, nuevoGen);
    }
}
