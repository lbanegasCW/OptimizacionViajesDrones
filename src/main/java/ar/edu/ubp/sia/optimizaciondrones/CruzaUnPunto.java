package ar.edu.ubp.sia.optimizaciondrones;

import java.util.Random;

public class CruzaUnPunto implements Cruza {

    private final Random random = new Random();

    @Override
    public Cromosoma[] cruzar(Cromosoma padre1, Cromosoma padre2) {
        int longitud = padre1.getLongitud();
        int punto = random.nextInt(longitud - 1) + 1;

        int[] genesPadre1 = padre1.getGenes();
        int[] genesPadre2 = padre2.getGenes();

        int[] hijo1 = new int[longitud];
        int[] hijo2 = new int[longitud];

        // Hijo 1: primeros genes de padre1, luego los genes de padre2
        System.arraycopy(genesPadre1, 0, hijo1, 0, punto);
        System.arraycopy(genesPadre2, punto, hijo1, punto, longitud - punto);

        // Hijo 2: primeros genes de padre2, luego los genes de padre1
        System.arraycopy(genesPadre2, 0, hijo2, 0, punto);
        System.arraycopy(genesPadre1, punto, hijo2, punto, longitud - punto);

        return new Cromosoma[] { new Cromosoma(hijo1), new Cromosoma(hijo2) };
    }

}
