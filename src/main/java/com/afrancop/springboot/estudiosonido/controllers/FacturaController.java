package com.afrancop.springboot.estudiosonido.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.afrancop.springboot.estudiosonido.models.dao.IFacturaDao;
import com.afrancop.springboot.estudiosonido.models.dao.jpa.IFacturaDaoJPA;
import com.afrancop.springboot.estudiosonido.models.entity.Cliente;
import com.afrancop.springboot.estudiosonido.models.entity.Factura;
import com.afrancop.springboot.estudiosonido.models.entity.ItemFactura;
import com.afrancop.springboot.estudiosonido.models.entity.Producto;
import com.afrancop.springboot.estudiosonido.models.service.IClienteService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
    
    // Con JPA
    @Autowired
    @Qualifier("facturaDaoJPA")
    private IFacturaDaoJPA facturaDao;
    
    @Autowired
    private IClienteService clienteService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) 
    {
        //Factura factura = clienteService.findFacturaById(id);
        Factura factura = facturaDao.findFactura(id);
        
        if (factura == null) 
        {
            flash.addFlashAttribute("error", "La factura no existe en la base de datos");
            return "redirect:/clientes";    
        }

        model.addAttribute("factura", factura);
        model.addAttribute("titutlo","Factura: ".concat(factura.getDescripcion()));

        return "factura/ver";
    }
    

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model, RedirectAttributes flash) 
    {
        Cliente cliente = clienteService.findOne(clienteId);

        if (cliente == null) 
        {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/clientes";    
        }

        Factura factura = new Factura();
        factura.setCliente(cliente);

        model.put("factura", factura);
        model.put("titulo", "Crear Factura");

        return "factura/form";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = {"application/json"})
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) 
    {
        return clienteService.findByNombreProducto(term);
    }

    @PostMapping("form")
    public String guardar(@Valid Factura factura, BindingResult result, Model model,
            @RequestParam(name = "item_id[]", required = false) Long[] itemId,
            @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad,
            RedirectAttributes flash, SessionStatus status ) 
    {
        if (result.hasErrors()) 
        {
            model.addAttribute("titulo", "Crear Factura");
            return "factura/form";    
        }

        if (itemId == null || itemId.length == 0) 
        {
            model.addAttribute("titulo", "Crear Factura");
            model.addAttribute("error", "Error: La factura No puede no tener lineas");
            return "factura/form";    
        }

        for (int i = 0; i < itemId.length; i++) 
        {
            Producto producto = clienteService.findProductoById(itemId[i]);
            
            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);

            log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
        }
        
        //clienteService.saveFactura(factura);
        facturaDao.saveFactura(factura);
        status.setComplete();

        flash.addFlashAttribute("success","Factura creada con exito");

        return "redirect:/ver/" + factura.getCliente().getId();
    }
        
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) 
    {
        //Factura factura = clienteService.findFacturaById(id);
        Factura factura = facturaDao.findFactura(id);

        if (factura != null) 
        {
            //clienteService.deleteFactura(id);
            facturaDao.deleteFactura(id);
            flash.addFlashAttribute("success","Factura eliminada");
            return "redirect:/ver/" + factura.getCliente().getId();    
        }

        flash.addFlashAttribute("error","La factura no existe en la base de datos o no se pudo eliminar");
        return "redirect:/clientes";
    }
    
}
