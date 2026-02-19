package ar.edu.ubp.sia.optimizaciondrones;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Producto {
    private final SimpleStringProperty nombre;
    private final SimpleDoubleProperty peso;
    private final SimpleIntegerProperty cantidad;
    private final SimpleIntegerProperty ancho;
    private final SimpleIntegerProperty alto;
    private final SimpleIntegerProperty profundidad;

    public Producto(String nombre, double peso, int cantidad, int ancho, int alto, int profundidad) {
        this.nombre = new SimpleStringProperty(nombre);
        this.peso = new SimpleDoubleProperty(peso);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.ancho = new SimpleIntegerProperty(ancho);
        this.alto = new SimpleIntegerProperty(alto);
        this.profundidad = new SimpleIntegerProperty(profundidad);
    }

    public String getNombre() {
        return nombre.get();
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public double getPeso() {
        return peso.get();
    }

    public SimpleDoubleProperty pesoProperty() {
        return peso;
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public SimpleIntegerProperty cantidadProperty() {
        return cantidad;
    }

    public int getAncho() {
        return ancho.get();
    }

    public SimpleIntegerProperty anchoProperty() {
        return ancho;
    }

    public int getAlto() {
        return alto.get();
    }

    public SimpleIntegerProperty altoProperty() {
        return alto;
    }

    public int getProfundidad() {
        return profundidad.get();
    }

    public SimpleIntegerProperty profundidadProperty() {
        return profundidad;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setPeso(double peso) {
        this.peso.set(peso);
    }


    public double getVolumen() {
        return ancho.get() * alto.get() * profundidad.get();
    }

}