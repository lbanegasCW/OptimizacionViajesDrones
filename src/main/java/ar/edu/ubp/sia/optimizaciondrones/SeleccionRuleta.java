package ar.edu.ubp.sia.optimizaciondrones;

import java.util.List;
import java.util.Random;

/** Implementa selección proporcional al fitness (ruleta). */
public class SeleccionRuleta implements Seleccion {
    private final Random random = new Random();

    /** {@inheritDoc} */
    @Override
    public Cromosoma seleccionar(List<Cromosoma> poblacion) {
        double sumaFitness = 0;
        for (Cromosoma c : poblacion) {
            sumaFitness += c.getFitness();
        }

        if (sumaFitness == 0) {
            return poblacion.get(random.nextInt(poblacion.size())).clonar();
        }

        double valor = random.nextDouble() * sumaFitness;
        double acumulado = 0;

        for (Cromosoma c : poblacion) {
            acumulado += c.getFitness();
            if (acumulado >= valor) {
                return c.clonar();
            }
        }

        return poblacion.get(poblacion.size() - 1).clonar();
    }
}
