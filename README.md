# Optimización de Viajes de Drones

Aplicación JavaFX que optimiza la distribución de productos en viajes de dron usando un **Algoritmo Genético (AG)** con restricciones de **peso** y **volumen** por viaje.

## ¿Qué hace el sistema?

El sistema toma:
- Un pedido (productos + cantidades).
- Parámetros del algoritmo genético (selección, cruza, mutación, población, generaciones).
- Restricciones operativas del dron (peso máximo y volumen máximo por viaje).

Y devuelve:
- Plan de viajes optimizado.
- Métrica de aptitud (fitness).
- Evolución del fitness por generación (gráfico).
- Resumen detallado en consola con estadísticas de eficiencia.

## Arquitectura funcional

### 1) Capa de interfaz (JavaFX)
- `MainApplication` inicia la app y carga `main-view.fxml`.
- `MainController` administra los controles del formulario, la tabla de productos, la ejecución y la visualización de resultados.

### 2) Capa de servicio
- `ServicioOptimizacion` recibe los datos del formulario, construye el algoritmo con los operadores seleccionados, ejecuta la optimización y genera el texto de salida.

### 3) Núcleo del algoritmo genético
- `AlgoritmoGenetico`:
  - Inicializa población de cromosomas.
  - Evalúa fitness por aprovechamiento de peso/volumen y cantidad de viajes.
  - Evoluciona por generaciones con selección, cruza y mutación.
  - Decodifica el mejor cromosoma en `ViajeOptimo`.
- `FabricaOperadores` instancia dinámicamente operadores según lo elegido en UI.

### 4) Modelo
- `Producto`: entidad de entrada (peso, dimensiones, cantidad).
- `Cromosoma`: secuencia genética de productos a despachar.
- `ViajeOptimo`: agrupación de productos por viaje con métricas de carga.
- `ResultadoOptimizacion`: encapsula viajes, fitness e indicadores finales.
- `ConfiguracionAG`: parámetros de ejecución del AG.

## Operadores del AG soportados

### Selección
- Torneo
- Ruleta
- Ranking

### Cruza
- Un punto
- Dos puntos
- Uniforme

### Mutación
- Simple
- Intercambio
- Inversión

## Flujo de ejecución

1. El usuario agrega productos a la tabla.
2. Define configuración del AG y límites del dron.
3. Presiona **Ejecutar Optimización**.
4. El controlador arma mapas de pedido/peso/volumen.
5. Se crea `ConfiguracionAG` y se ejecuta `ServicioOptimizacion`.
6. Se muestran:
   - detalle del pedido,
   - viajes mínimos teóricos,
   - configuración usada,
   - resultados y estadísticas,
   - gráfico de evolución de fitness.

## ¿Cómo correr el proyecto?

### Requisitos
- JDK 17
- Maven (o usar el wrapper incluido `./mvnw`)

### Ejecutar en desarrollo
```bash
./mvnw clean javafx:run
```

### Compilar
```bash
./mvnw clean compile
```

### Ejecutar tests
```bash
./mvnw test
```

## Caso **DEFAULT** que inicializa todo

Hay un atajo en la UI para cargar automáticamente un escenario completo de productos.

### Cómo usarlo
1. En el campo **Nombre** del formulario de producto, escribir exactamente: `DEFAULT`.
2. Presionar **Agregar Producto**.
3. El sistema inserta un set predefinido de 10 productos con pesos, dimensiones y cantidades.

## Screenshot
<img width="1000" height="829" alt="image" src="https://github.com/user-attachments/assets/dd394cca-277f-4c46-8819-63e9973a32e3" />

### Dataset cargado por DEFAULT
- Notebook — 2.1 kg — cant. 30 — 36×6×30
- Tablet — 0.6 kg — cant. 60 — 26×4×20
- Parlante Bluetooth — 3.6 kg — cant. 15 — 33×18×24
- Smart TV — 5.0 kg — cant. 8 — 124×10×80
- Smartphone — 0.25 kg — cant. 100 — 18×4×9
- Impresora Láser — 10.0 kg — cant. 10 — 45×31×40
- Ventilador 15'' — 6.0 kg — cant. 12 — 52×52×27
- Cámara GoPro — 0.16 kg — cant. 40 — 7×5×4
- Router WiFi — 0.55 kg — cant. 25 — 20×5×18
- Aro luz 18'' — 2.0 kg — cant. 10 — 50×6×50

## Valores iniciales de configuración en la UI

Al abrir la app, se cargan por defecto:
- Selección: Torneo
- Cruza: Un Punto
- Mutación: Simple
- Población: 30
- Generaciones: 50
- Volumen máximo dron: 150000 cm³
- Peso máximo dron: 17 kg

## Notas
- El fitness combina aprovechamiento de capacidad y penalización por mayor cantidad de viajes.
- La población inicial se genera con una mezcla reproducible de genes (`seed = 42` en el mezclado inicial).
