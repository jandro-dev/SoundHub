package com.afrancop.springboot.estudiosonido.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.afrancop.springboot.estudiosonido.models.dao.IClienteDao;
import com.afrancop.springboot.estudiosonido.models.dao.IFacturaDao;
import com.afrancop.springboot.estudiosonido.models.dao.IProductoDao;
import com.afrancop.springboot.estudiosonido.models.entity.Cliente;
import com.afrancop.springboot.estudiosonido.models.entity.Factura;
import com.afrancop.springboot.estudiosonido.models.entity.Producto;

@Service
public class ClienteServiceImplementado implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Autowired
    private IProductoDao productoDao;

    @Autowired IFacturaDao facturaDao;

    @Override
    @Transactional(readOnly = true) // En las inserciones se ignora esto
    public List<Cliente> findAll() {
        return (List<Cliente>) clienteDao.findAll();
    }

    @Override
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteDao.findAll(pageable);
    }

    @Override
    @Transactional
    public void save(Cliente cliente) {
        clienteDao.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id) {
        return clienteDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        clienteDao.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findByNombreProducto(String term) {
        return productoDao.findByNombre(term);
    }

    @Override
    public List<Producto> findAllProductos() {
        return (List<Producto>) productoDao.findAll();
    }
    
    @Override
    @Transactional
    public void saveFactura(Factura factura) {
        facturaDao.save(factura);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findProductoById(Long id) {
        return productoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Factura findFacturaById(Long id) {
        return facturaDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteFactura(Long id) {
        facturaDao.deleteById(id);
    }

}
