package com.afrancop.springboot.estudiosonido.models.dao.jpa;

import org.springframework.data.repository.CrudRepository;

import com.afrancop.springboot.estudiosonido.models.entity.Factura;


public interface IFacturaDaoJPA 
{
    public void saveFactura(Factura factura);
    public Factura findFactura(Long id);
    public void deleteFactura(Long id);
}
