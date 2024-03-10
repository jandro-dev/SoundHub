package com.afrancop.springboot.estudiosonido.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.afrancop.springboot.estudiosonido.models.entity.Factura;


public interface IFacturaDao extends CrudRepository<Factura,Long> { }