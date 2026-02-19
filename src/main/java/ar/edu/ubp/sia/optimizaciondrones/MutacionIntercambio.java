package ar.edu.ubp.sia.optimizaciondrones;

import java.util.Random;

public class MutacionIntercambio implements Mutacion {

    private final Random random = new Random();

    @Override
    public void mutar(Cromosoma cromosoma) {
        int longitud = cromosoma.getGenes().length;

        if (longitud < 2) return;

        int pos1 = random.nextInt(longitud);
        int pos2;
        do {
            pos2 = random.nextInt(longitud);
        } while (pos2 == pos1);

        int[] genes = cromosoma.getGenes();
        int temp = genes[pos1];
        cromosoma.setGene(pos1, genes[pos2]);
        cromosoma.setGene(pos2, temp);
    }
}
