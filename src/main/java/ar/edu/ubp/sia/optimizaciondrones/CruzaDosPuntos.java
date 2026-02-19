package ar.edu.ubp.sia.optimizaciondrones;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CruzaDosPuntos implements Cruza {

    private final Random random = new Random();

    @Override
    public Cromosoma[] cruzar(Cromosoma padre1, Cromosoma padre2) {
        int[] genesPadre1 = padre1.getGenes();
        int[] genesPadre2 = padre2.getGenes();

        int longitud = genesPadre1.length;
        if (longitud <= 2) {
            return new Cromosoma[]{ padre1.clonar(), padre2.clonar() };
        }

        int punto1 = random.nextInt(longitud - 1);
        int punto2 = random.nextInt(longitud - punto1 - 1) + punto1 + 1;

        int[] genesHijo1 = new int[longitud];
        int[] genesHijo2 = new int[longitud];
        Arrays.fill(genesHijo1, -1);
        Arrays.fill(genesHijo2, -1);

        // Copiar segmento central de padre2 en hijo1, y de padre1 en hijo2
        for (int i = punto1; i < punto2; i++) {
            genesHijo1[i] = genesPadre2[i];
            genesHijo2[i] = genesPadre1[i];
        }

        // Contar cantidad de genes en padre1 y padre2 para respetar cantidades en hijos
        Map<Integer, Integer> cantidadPorGenPadre1 = contarGenes(genesPadre1);
        Map<Integer, Integer> cantidadPorGenPadre2 = contarGenes(genesPadre2);

        // Rellenar hijos respetando cantidades
        rellenarConCantidades(genesHijo1, genesPadre1, punto1, punto2, cantidadPorGenPadre2);
        rellenarConCantidades(genesHijo2, genesPadre2, punto1, punto2, cantidadPorGenPadre1);

        return new Cromosoma[]{
                new Cromosoma(genesHijo1),
                new Cromosoma(genesHijo2)
        };
    }

    private Map<Integer, Integer> contarGenes(int[] genes) {
        Map<Integer, Integer> conteo = new HashMap<>();
        for (int gen : genes) {
            conteo.put(gen, conteo.getOrDefault(gen, 0) + 1);
        }
        return conteo;
    }

    private void rellenarConCantidades(int[] hijo, int[] padre, int punto1, int punto2, Map<Integer, Integer> cantidadPorGen) {
        Map<Integer, Integer> conteoActual = new HashMap<>();
        for (int gen : hijo) {
            if (gen != -1) {
                conteoActual.put(gen, conteoActual.getOrDefault(gen, 0) + 1);
            }
        }

        int idx = 0;
        for (int gen : padre) {
            int maxCantidad = cantidadPorGen.getOrDefault(gen, 0);
            int actual = conteoActual.getOrDefault(gen, 0);

            if (actual < maxCantidad) {
                // Avanzar idx hasta una posición libre fuera del segmento copiado
                while (idx < hijo.length && (hijo[idx] != -1 || (idx >= punto1 && idx < punto2))) {
                    idx++;
                }
                if (idx >= hijo.length) {
                    throw new IllegalStateException("No hay posición libre para insertar gen");
                }
                hijo[idx] = gen;
                conteoActual.put(gen, actual + 1);
                idx++;  // avanzar para la próxima inserción
            }
        }
    }

}
