package ar.edu.ubp.sia.optimizaciondrones;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController {
    @FXML
    private ComboBox<String> cbSeleccion;

    @FXML
    private ComboBox<String> cbCruza;

    @FXML
    private ComboBox<String> cbMutacion;

    @FXML
    private TextField tfPoblacion;

    @FXML
    private TextField tfGeneraciones;

    @FXML
    private TextField tfPesoDron;

    @FXML
    private TextField tfVolumenDron;

    @FXML
    private TableView<Producto> tablaProductos;

    @FXML
    private TableColumn<Producto, String> colNombre;

    @FXML
    private TableColumn<Producto, Double> colPeso;

    @FXML
    private TableColumn<Producto, Integer> colAncho;

    @FXML
    private TableColumn<Producto, Integer> colAlto;

    @FXML
    private TableColumn<Producto, Integer> colProf;

    @FXML
    private TableColumn<Producto, Integer> colCantidad;

    @FXML
    private TextField tfNombre;

    @FXML
    private TextField tfPeso;

    @FXML
    private TextField tfAlto;

    @FXML
    private TextField tfAncho;

    @FXML
    private TextField tfProfundidad;

    @FXML
    private TextField tfCantidad;

    @FXML
    private Button btnAgregar;

    private ObservableList<Producto> productos;

    @FXML
    private TextArea consoleOutput;

    @FXML
    private AnchorPane chartContainer;

    private LineChart<Number, Number> fitnessChart;
    private XYChart.Series<Number, Number> fitnessSeries;

    @FXML
    public void initialize() {
        cbSeleccion.getItems().addAll("Torneo", "Ruleta", "Ranking");
        cbCruza.getItems().addAll("Un Punto", "Dos Puntos", "Uniforme");
        cbMutacion.getItems().addAll("Simple", "Intercambio", "Inversión");

        cbSeleccion.getSelectionModel().selectFirst();
        cbCruza.getSelectionModel().selectFirst();
        cbMutacion.getSelectionModel().selectFirst();
        tfPoblacion.setText("30");
        tfGeneraciones.setText("50");
        tfVolumenDron.setText("150000");
        tfPesoDron.setText("17");

        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colPeso.setCellValueFactory(cellData -> cellData.getValue().pesoProperty().asObject());
        colAncho.setCellValueFactory(cellData -> cellData.getValue().anchoProperty().asObject());
        colAlto.setCellValueFactory(cellData -> cellData.getValue().altoProperty().asObject());
        colProf.setCellValueFactory(cellData -> cellData.getValue().profundidadProperty().asObject());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());

        productos = FXCollections.observableArrayList();
        tablaProductos.setItems(productos);

        btnAgregar.setOnAction(e -> agregarProducto());

        inicializarGrafico();
    }

    private void agregarProducto() {
        String nombre = tfNombre.getText().trim();
        String pesoStr = tfPeso.getText().trim();
        String anchoStr = tfAncho.getText().trim();
        String altoStr = tfAlto.getText().trim();
        String profStr = tfProfundidad.getText().trim();
        String cantidadStr = tfCantidad.getText().trim();

        if (nombre.equals("DEFAULT")) {
            agregarDefault();
            tfNombre.clear();
            return;
        }

        if (nombre.isEmpty() || pesoStr.isEmpty() || cantidadStr.isEmpty()) {
            mostrarAlerta("Error", "Debe ingresar nombre, peso y cantidad");
            return;
        }

        double peso;
        try {
            peso = Double.parseDouble(pesoStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Peso debe ser un número válido");
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Cantidad debe ser un número válido");
            return;
        }

        int ancho;
        try {
            ancho = Integer.parseInt(anchoStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Ancho debe ser un número válido");
            return;
        }

        int alto;
        try {
            alto = Integer.parseInt(altoStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Alto debe ser un número válido");
            return;
        }

        int profundidad;
        try {
            profundidad = Integer.parseInt(profStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Profundidad debe ser un número válido");
            return;
        }

        productos.add(new Producto(nombre, peso, cantidad, ancho, alto, profundidad));

        tfNombre.clear();
        tfPeso.clear();
        tfCantidad.clear();
        tfAlto.clear();
        tfAncho.clear();
        tfProfundidad.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    protected void onEjecutar() {
        // 1. Armar el pedido desde la tabla
        Map<String, Integer> pedido = new HashMap<>();
        for (Producto p : productos) {
            if (p.getCantidad() > 0) {
                pedido.put(p.getNombre(), p.getCantidad());
            }
        }

        // 2. Armar mapa de productos con volumenes desde la tabla
        Map<String, Double> volumenProductos = new HashMap<>();
        for (Producto p : productos) {
            volumenProductos.put(p.getNombre(), p.getVolumen());
        }

        // 3. Armar mapa de productos con pesos desde la tabla
        Map<String, Double> pesosProductos = new HashMap<>();
        for (Producto p : productos) {
            pesosProductos.put(p.getNombre(), p.getPeso());
        }

        // 3. Leer configuración desde los controles UI
        ConfiguracionAG config = new ConfiguracionAG(
                cbSeleccion.getValue(),
                cbCruza.getValue(),
                cbMutacion.getValue(),
                Integer.parseInt(tfPoblacion.getText()),
                Integer.parseInt(tfGeneraciones.getText()),
                0.8,
                0.1,
                Integer.parseInt(tfPesoDron.getText()),
                Double.parseDouble(tfVolumenDron.getText())
        );

        // 4. Ejecutar algoritmo
        ServicioOptimizacion optimizacion = new ServicioOptimizacion(volumenProductos, pesosProductos, pedido, config);
        optimizacion.ejecutar();
        consoleOutput.setText(optimizacion.outputConsola());

        mostrarFitnessChart(optimizacion.getResultado().getHistorialFitness());

    }

    private void agregarDefault() {
        productos.add(new Producto("Notebook", 2.1, 30, 36, 6, 30));
        productos.add(new Producto("Tablet", 0.6, 60, 26, 4, 20));
        productos.add(new Producto("Parlante Bluetooth", 3.6, 15, 33, 18, 24));
        productos.add(new Producto("Smart TV", 5.0, 8, 124, 10, 80));
        productos.add(new Producto("Smartphone", 0.25, 100, 18, 4, 9));
        productos.add(new Producto("Impresora Láser", 10.0, 10, 45, 31, 40));
        productos.add(new Producto("Ventilador 15''", 6.0, 12, 52, 52, 27));
        productos.add(new Producto("Cámara GoPro", 0.16, 40, 7, 5, 4));
        productos.add(new Producto("Router WiFi", 0.55, 25, 20, 5, 18));
        productos.add(new Producto("Aro luz 18''", 2.0, 10, 50, 6, 50));
    }

    private void inicializarGrafico() {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Generación");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Fitness");

        fitnessChart = new LineChart<>(xAxis, yAxis);
        fitnessChart.setTitle("Evolución del Fitness");
        fitnessChart.setAnimated(false);
        fitnessChart.setLegendVisible(false);
        fitnessChart.setCreateSymbols(false);

        fitnessSeries = new XYChart.Series<>();
        fitnessChart.getData().add(fitnessSeries);

        chartContainer.getChildren().clear();
        chartContainer.getChildren().add(fitnessChart);
    }

    private void mostrarFitnessChart(List<Double> historialFitness) {
        fitnessSeries.getData().clear();
        for (int i = 0; i < historialFitness.size(); i++) {
            fitnessSeries.getData().add(new XYChart.Data<>(i + 1, historialFitness.get(i)));
        }
    }

}