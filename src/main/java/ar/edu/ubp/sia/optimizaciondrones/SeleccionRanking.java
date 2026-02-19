package ar.edu.ubp.sia.optimizaciondrones;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Selección por ranking: asigna mayor probabilidad a mejores individuos
 * según su posición ordenada por fitness.
 */
public class SeleccionRanking implements Seleccion {

    private final Random random = new Random();

    /** {@inheritDoc} */
    @Override
    public Cromosoma seleccionar(List<Cromosoma> poblacion) {
        List<Cromosoma> ordenados = new ArrayList<>(poblacion);
        Collections.sort(ordenados);

        int n = ordenados.size();
        int sumaRankings = n * (n + 1) / 2;

        double[] acumuladas = new double[n];
        double acumulado = 0;
        for (int i = 0; i < n; i++) {
            double prob = (double) (n - i) / sumaRankings;
            acumulado += prob;
            acumuladas[i] = acumulado;
        }

        double r = random.nextDouble();
        for (int i = 0; i < n; i++) {
            if (r <= acumuladas[i]) {
                return ordenados.get(i).clonar();
            }
        }

        return ordenados.get(n - 1).clonar();
    }
}
