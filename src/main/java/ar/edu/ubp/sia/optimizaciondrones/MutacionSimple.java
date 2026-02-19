package ar.edu.ubp.sia.optimizaciondrones;

import java.util.Random;

public class MutacionSimple implements Mutacion {

    private final Random random = new Random();

    @Override
    public void mutar(Cromosoma cromosoma) {
        int longitud = cromosoma.getGenes().length;
        int posicion = random.nextInt(longitud);

        int valorActual = cromosoma.getGenes()[posicion];
        // Genera un nuevo gen distinto al actual, en rango [0, longitud-1]
        int nuevoValor;
        do {
            nuevoValor = random.nextInt(longitud);
        } while (nuevoValor == valorActual);

        cromosoma.setGene(posicion, nuevoValor);
    }
}
