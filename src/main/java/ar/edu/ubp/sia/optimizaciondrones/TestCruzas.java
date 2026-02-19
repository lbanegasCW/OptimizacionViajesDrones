package ar.edu.ubp.sia.optimizaciondrones;

import java.util.HashSet;
import java.util.Set;

public class TestCruzas {

    public static void main(String[] args) {
        // Crear cromosomas padres
        int[] genesPadre1 = new int[10];
        int[] genesPadre2 = new int[10];
        for (int i = 0; i < 10; i++) {
            genesPadre1[i] = i;
            genesPadre2[i] = 9 - i;
        }
        Cromosoma padre1 = new Cromosoma(genesPadre1);
        Cromosoma padre2 = new Cromosoma(genesPadre2);

        // Instanciar cruzas
        Cruza[] cruzas = new Cruza[] {
                new CruzaUnPunto(),
                new CruzaDosPuntos(),
                new CruzaUniforme()  // Ajusta el nombre si tu clase se llama distinto
        };

        for (Cruza cruza : cruzas) {
            System.out.println("Probando cruza: " + cruza.getClass().getSimpleName());
            Cromosoma[] hijos = cruza.cruzar(padre1, padre2);
            for (int i = 0; i < hijos.length; i++) {
                int[] genesHijo = hijos[i].getGenes();

                // Validaciones
                if (contieneValor(genesHijo, -1)) {
                    System.err.println("Error: hijo " + i + " contiene -1 en genes.");
                } else {
                    System.out.println("Hijo " + i + " no contiene -1.");
                }

                if (genesHijo.length != genesPadre1.length) {
                    System.err.println("Error: hijo " + i + " tamaño distinto al padre.");
                }

                if (!mismosGenes(genesHijo, genesPadre1) && !mismosGenes(genesHijo, genesPadre2)) {
                    System.err.println("Error: hijo " + i + " no contiene los genes correctos.");
                } else {
                    System.out.println("Hijo " + i + " contiene los genes correctamente.");
                }
            }
            System.out.println("-----");
        }
    }

    private static boolean contieneValor(int[] array, int valor) {
        for (int v : array) {
            if (v == valor) return true;
        }
        return false;
    }

    private static boolean mismosGenes(int[] a, int[] b) {
        if (a.length != b.length) return false;
        Set<Integer> setA = new HashSet<>();
        Set<Integer> setB = new HashSet<>();
        for (int v : a) setA.add(v);
        for (int v : b) setB.add(v);
        return setA.equals(setB);
    }
}
