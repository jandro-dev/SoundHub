package com.afrancop.springboot.estudiosonido.models.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.afrancop.springboot.estudiosonido.models.entity.Cliente;
import com.afrancop.springboot.estudiosonido.models.entity.Factura;
import com.afrancop.springboot.estudiosonido.models.entity.Producto;


public interface IClienteService 
{
    public List<Cliente> findAll();
    public Page<Cliente> findAll(Pageable pageable);
    public void save(Cliente cliente);
    public Cliente findOne(Long id);
    public void delete(Long id);
    public List<Producto> findByNombreProducto(String term);
    public List<Producto> findAllProductos();
    public void saveFactura(Factura factura);
    public Producto findProductoById(Long id);
    public Factura findFacturaById(Long id);
    public void deleteFactura(Long id);
} 