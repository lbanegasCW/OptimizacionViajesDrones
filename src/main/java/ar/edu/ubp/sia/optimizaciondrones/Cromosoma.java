package ar.edu.ubp.sia.optimizaciondrones;

import java.util.Arrays;
import java.util.Random;

/**
 * Representa un cromosoma con genes y aptitud (fitness)
 */
public class Cromosoma implements Comparable<Cromosoma> {

    private final int[] genes;
    private double fitness;
    private static final Random random = new Random();

    // genes es una lista secuencial de productos (no cantidades)
    public Cromosoma(int[] genes) {
        this.genes = genes.clone();
        this.fitness = 0;
    }

    public int[] getGenes() {
        return genes.clone();
    }

    public int getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, int valor) {
        genes[index] = valor;
        fitness = 0;
    }

    public int getLongitud() {
        return genes.length;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Cromosoma clonar() {
        return new Cromosoma(genes);
    }

    @Override
    public int compareTo(Cromosoma o) {
        return Double.compare(o.fitness, this.fitness);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cromosoma)) return false;
        Cromosoma otro = (Cromosoma) obj;
        return Arrays.equals(this.genes, otro.genes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }

    @Override
    public String toString() {
        return "Cromosoma{" +
                "fitness=" + fitness +
                ", genes=" + Arrays.toString(genes) +
                '}';
    }
}