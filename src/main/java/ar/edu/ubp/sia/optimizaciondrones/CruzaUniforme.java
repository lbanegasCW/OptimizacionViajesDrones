package ar.edu.ubp.sia.optimizaciondrones;

import java.util.*;

public class CruzaUniforme implements Cruza {

    private final Random random = new Random();

    @Override
    public Cromosoma[] cruzar(Cromosoma padre1, Cromosoma padre2) {
        int longitud = padre1.getGenes().length;
        int[] genesPadre1 = padre1.getGenes();
        int[] genesPadre2 = padre2.getGenes();

        Map<Integer, Integer> conteoTotal = new HashMap<>();
        for (int gen : genesPadre1) {
            conteoTotal.put(gen, conteoTotal.getOrDefault(gen, 0) + 1);
        }

        // Fase 1: asignación uniforme
        List<Integer> poolHijo1 = new ArrayList<>();
        List<Integer> poolHijo2 = new ArrayList<>();

        for (int i = 0; i < longitud; i++) {
            if (random.nextBoolean()) {
                poolHijo1.add(genesPadre1[i]);
                poolHijo2.add(genesPadre2[i]);
            } else {
                poolHijo1.add(genesPadre2[i]);
                poolHijo2.add(genesPadre1[i]);
            }
        }

        // Fase 2: corregir hijos para que tengan las cantidades correctas
        int[] hijo1 = ajustarGenes(poolHijo1, conteoTotal);
        int[] hijo2 = ajustarGenes(poolHijo2, conteoTotal);

        return new Cromosoma[] {
                new Cromosoma(hijo1),
                new Cromosoma(hijo2)
        };
    }

    private int[] ajustarGenes(List<Integer> genesProvisorios, Map<Integer, Integer> conteoEsperado) {
        Map<Integer, Integer> conteoActual = new HashMap<>();
        for (int gen : genesProvisorios) {
            conteoActual.put(gen, conteoActual.getOrDefault(gen, 0) + 1);
        }

        // Eliminar excesos
        List<Integer> corregidos = new ArrayList<>();
        for (int gen : genesProvisorios) {
            int cantidadActual = conteoActual.get(gen);
            int cantidadEsperada = conteoEsperado.getOrDefault(gen, 0);
            if (cantidadEsperada > 0 && cantidadActual <= cantidadEsperada) {
                corregidos.add(gen);
            } else if (cantidadActual > cantidadEsperada) {
                conteoActual.put(gen, cantidadActual - 1);
            }
        }

        // Agregar faltantes
        Map<Integer, Integer> conteoCorregido = new HashMap<>();
        for (int gen : corregidos) {
            conteoCorregido.put(gen, conteoCorregido.getOrDefault(gen, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : conteoEsperado.entrySet()) {
            int gen = entry.getKey();
            int faltan = entry.getValue() - conteoCorregido.getOrDefault(gen, 0);
            for (int i = 0; i < faltan; i++) {
                corregidos.add(gen);
            }
        }

        // Barajar para evitar sesgo
        Collections.shuffle(corregidos);

        // Convertir a array primitivo
        int[] resultado = new int[corregidos.size()];
        for (int i = 0; i < corregidos.size(); i++) {
            resultado[i] = corregidos.get(i);
        }

        return resultado;
    }
}
