package ar.edu.ubp.sia.optimizaciondrones;

import java.util.Arrays;

/**
 * Modelo de cromosoma: mantiene secuencia de genes y valor de fitness asociado.
 */
public class Cromosoma implements Comparable<Cromosoma> {

    private final int[] genes;
    private double fitness;

    /**
     * Construye un cromosoma a partir de una copia defensiva del arreglo de genes.
     *
     * @param genes secuencia de genes del individuo
     */
    public Cromosoma(int[] genes) {
        this.genes = genes.clone();
        this.fitness = 0;
    }

    /** @return copia defensiva de los genes del cromosoma. */
    public int[] getGenes() {
        return genes.clone();
    }

    /** @return gen en la posición solicitada. */
    public int getGene(int index) {
        return genes[index];
    }

    /**
     * Actualiza un gen y reinicia el fitness para forzar recálculo posterior.
     */
    public void setGene(int index, int valor) {
        genes[index] = valor;
        fitness = 0;
    }

    /** @return longitud del cromosoma. */
    public int getLongitud() {
        return genes.length;
    }

    /** @return valor de fitness actual. */
    public double getFitness() {
        return fitness;
    }

    /** @param fitness nuevo valor de fitness calculado. */
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    /** @return copia del cromosoma sin arrastrar fitness previo. */
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
