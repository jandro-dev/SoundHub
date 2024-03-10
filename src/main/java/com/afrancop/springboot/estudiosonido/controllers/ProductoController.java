package com.afrancop.springboot.estudiosonido.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.afrancop.springboot.estudiosonido.models.entity.Cliente;
import com.afrancop.springboot.estudiosonido.models.entity.Producto;
import com.afrancop.springboot.estudiosonido.models.service.IClienteService;
import com.afrancop.springboot.estudiosonido.util.paginator.PageRender;

@Controller
@SessionAttributes("producto")
public class ProductoController {
    
    @Autowired
    private IClienteService clienteService;

    @RequestMapping(value = "/productos", method = RequestMethod.GET)
    public String listarProductos(Model model)
    {
        List<Producto> productos = clienteService.findAllProductos();

        model.addAttribute("productos", productos);
        return "productos";
    }

}
