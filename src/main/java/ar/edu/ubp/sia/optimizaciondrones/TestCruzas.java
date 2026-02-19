package ar.edu.ubp.sia.optimizaciondrones;

import java.util.HashSet;
import java.util.Set;

/**
 * Utilidad manual para validar comportamiento básico de operadores de cruza.
 */
public class TestCruzas {

    /** Ejecuta validaciones simples por consola para cada estrategia de cruza. */
    public static void main(String[] args) {
        Cromosoma padre1 = new Cromosoma(new int[]{1, 2, 3, 4, 5, 6, 7, 8});
        Cromosoma padre2 = new Cromosoma(new int[]{8, 7, 6, 5, 4, 3, 2, 1});

        Cruza[] cruzas = {
                new CruzaUnPunto(),
                new CruzaDosPuntos(),
                new CruzaUniforme()
        };

        for (Cruza cruza : cruzas) {
            System.out.println("Probando cruza: " + cruza.getClass().getSimpleName());

            Cromosoma[] hijos = cruza.cruzar(padre1, padre2);
            for (int i = 0; i < hijos.length; i++) {
                int[] genesHijo = hijos[i].getGenes();
                int[] genesPadre1 = padre1.getGenes();
                int[] genesPadre2 = padre2.getGenes();

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
            if (v == valor) {
                return true;
            }
        }
        return false;
    }

    private static boolean mismosGenes(int[] a, int[] b) {
        if (a.length != b.length) {
            return false;
        }

        Set<Integer> setA = new HashSet<>();
        Set<Integer> setB = new HashSet<>();
        for (int v : a) {
            setA.add(v);
        }
        for (int v : b) {
            setB.add(v);
        }
        return setA.equals(setB);
    }
}
