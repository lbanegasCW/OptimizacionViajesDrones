package ar.edu.ubp.sia.optimizaciondrones;

import java.util.Random;

/** Implementación de cruza de un punto para cromosomas de igual longitud. */
public class CruzaUnPunto implements Cruza {

    private final Random random = new Random();

    /** {@inheritDoc} */
    @Override
    public Cromosoma[] cruzar(Cromosoma padre1, Cromosoma padre2) {
        int longitud = padre1.getLongitud();
        int punto = random.nextInt(longitud - 1) + 1;

        int[] genesPadre1 = padre1.getGenes();
        int[] genesPadre2 = padre2.getGenes();
        int[] hijo1 = new int[longitud];
        int[] hijo2 = new int[longitud];

        System.arraycopy(genesPadre1, 0, hijo1, 0, punto);
        System.arraycopy(genesPadre2, punto, hijo1, punto, longitud - punto);
        System.arraycopy(genesPadre2, 0, hijo2, 0, punto);
        System.arraycopy(genesPadre1, punto, hijo2, punto, longitud - punto);

        return new Cromosoma[]{new Cromosoma(hijo1), new Cromosoma(hijo2)};
    }
}
