package com.afrancop.springboot.estudiosonido.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {
    
    private String url;
    private Page<T> page;
    private int totalPaginas;
    private int elementosPorPagina;
    private int paginaActual;
    private List<PageItem> paginas;
    
    // Constructores
    public PageRender(String url, Page<T> page) {
        this.url = url;
        this.page = page;
        this.paginas = new ArrayList<PageItem>();

        elementosPorPagina = page.getSize();
        totalPaginas = page.getTotalPages();
        paginaActual = page.getNumber() + 1;
    
        int desde, hasta;
        
        if (totalPaginas <= elementosPorPagina) 
        {    
            desde = 1;
            hasta = totalPaginas;
        }
        else
        {
            if (paginaActual <= elementosPorPagina / 2) 
            {
                desde = 1;
                hasta = elementosPorPagina;
            }
            else if (paginaActual >= totalPaginas - elementosPorPagina / 2) 
            {
                desde = totalPaginas - elementosPorPagina + 1;
                hasta = elementosPorPagina;
            }
            else 
            {
                desde = paginaActual - elementosPorPagina / 2;
                hasta = elementosPorPagina;
            }
        }
        
        for (int i = 0; i < hasta; i++) 
        {
            paginas.add(new PageItem(desde + i,paginaActual == desde + i));
        }
    }
    
    // Getters
    public String getUrl() {
        return url;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public int getPaginaActual() {
        return paginaActual;
    }

    public List<PageItem> getPaginas() {
        return paginas;
    }

    // Funciones
    public boolean isFirst()
    {
        return page.isFirst();
    }

    public boolean isLast() 
    {
        return page.isLast();
    }

    public boolean hasNext() 
    {
        return page.hasNext();
    }

    public boolean hasPrevious() 
    {
        return page.hasPrevious();
    }
}
