package ar.edu.ubp.sia.optimizaciondrones;

import java.util.Random;

public class MutacionInversion implements Mutacion {

    private final Random random = new Random();

    @Override
    public void mutar(Cromosoma cromosoma) {
        int longitud = cromosoma.getGenes().length;

        if (longitud < 2) return;

        int inicio = random.nextInt(longitud);
        int fin = random.nextInt(longitud);

        if (inicio > fin) {
            int temp = inicio;
            inicio = fin;
            fin = temp;
        }

        int[] genes = cromosoma.getGenes();

        while (inicio < fin) {
            int temp = genes[inicio];
            cromosoma.setGene(inicio, genes[fin]);
            cromosoma.setGene(fin, temp);
            inicio++;
            fin--;
        }
    }
}

