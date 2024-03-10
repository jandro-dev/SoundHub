package com.afrancop.springboot.estudiosonido.models.dao.jpa;

import org.springframework.stereotype.Repository;

import com.afrancop.springboot.estudiosonido.models.entity.Factura;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository("facturaDaoJPA")
public class FacturaDaoImplementado implements IFacturaDaoJPA {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveFactura(Factura factura) 
    {
        if (factura.getId() != null && factura.getId() > 0) 
        {
            entityManager.merge(factura);    
        }    
        else
        {
            entityManager.persist(factura);
        }
    }

    @Override
    @Transactional
    public Factura findFactura(Long id) 
    {
        return entityManager.find(Factura.class, id);    
    }

    @Override
    @Transactional
    public void deleteFactura(Long id) 
    {
        entityManager.remove(findFactura(id));    
    }
    
}
