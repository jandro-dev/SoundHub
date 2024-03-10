package com.afrancop.springboot.estudiosonido.util.paginator;

public class PageItem {

    private int numero;
    private boolean actual;
    
    // Constructores
    public PageItem(int numero, boolean actual) {
        this.numero = numero;
        this.actual = actual;
    }

    // Getters y Setters
    public int getNumero() {
        return numero;
    }

    public boolean isActual() {
        return actual;
    }

}
