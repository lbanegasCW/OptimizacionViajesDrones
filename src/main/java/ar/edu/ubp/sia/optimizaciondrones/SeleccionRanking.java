package ar.edu.ubp.sia.optimizaciondrones;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Selección por ranking
 */
public class SeleccionRanking implements Seleccion {

    private final Random random = new Random();

    @Override
    public Cromosoma seleccionar(List<Cromosoma> poblacion) {
        int n = poblacion.size();

        // Clonamos la lista y la ordenamos por fitness ascendente
        List<Cromosoma> ordenados = poblacion.stream()
                .sorted(Comparator.comparingDouble(Cromosoma::getFitness))
                .toList();

        // Cálculo de la suma de rankings (triangular)
        double sumaRankings = n * (n + 1) / 2.0;

        // Generamos probabilidades acumuladas
        double[] acumuladas = new double[n];
        acumuladas[0] = 1.0 / sumaRankings;

        for (int i = 1; i < n; i++) {
            acumuladas[i] = acumuladas[i - 1] + (i + 1) / sumaRankings;
        }

        // Selección por ruleta
        double r = random.nextDouble();
        for (int i = 0; i < n; i++) {
            if (r <= acumuladas[i]) {
                return ordenados.get(i).clonar();
            }
        }

        return ordenados.get(n - 1).clonar(); // fallback por seguridad
    }
}
