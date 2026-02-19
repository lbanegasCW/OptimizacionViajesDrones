package ar.edu.ubp.sia.optimizaciondrones;

import java.util.List;
import java.util.Random;

/**
 * Selección por ruleta (fitness proporcional)
 */
public class SeleccionRuleta implements Seleccion {

    private final Random random = new Random();

    @Override
    public Cromosoma seleccionar(List<Cromosoma> poblacion) {
        double sumaFitness = poblacion.stream()
                .mapToDouble(c -> Math.max(0, c.getFitness()))
                .sum();

        if (sumaFitness == 0) {
            // Selección aleatoria si todos fitness son cero
            return poblacion.get(random.nextInt(poblacion.size()));
        }

        double punto = random.nextDouble() * sumaFitness;
        double acumulado = 0.0;
        for (Cromosoma c : poblacion) {
            acumulado += Math.max(0, c.getFitness());
            if (acumulado >= punto) {
                return c;
            }
        }
        return poblacion.get(poblacion.size() - 1);
    }

}