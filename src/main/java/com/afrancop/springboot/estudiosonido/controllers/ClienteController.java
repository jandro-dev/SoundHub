package com.afrancop.springboot.estudiosonido.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.afrancop.springboot.estudiosonido.models.dao.IFacturaDao;
import com.afrancop.springboot.estudiosonido.models.entity.Cliente;
import com.afrancop.springboot.estudiosonido.models.service.IClienteService;
import com.afrancop.springboot.estudiosonido.models.service.IUploadFileService;
import com.afrancop.springboot.estudiosonido.util.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
    
    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String,Object> model, RedirectAttributes flash)
    {
        Cliente cliente = clienteService.findOne(id);
        
        if (cliente == null) 
        {
            flash.addFlashAttribute("error", "El cliente no existe en la BD");    
            return "redirect:/clientes";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Detalles del cliente: " + cliente.getNombre());

        return "ver";
    }

    @GetMapping(value = "uploads/{filename:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String filename)
    {
        Resource recurso = null;

        try 
        {
            recurso = uploadFileService.load(filename);    
        } 
        catch (MalformedURLException e) 
        {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
            .body(recurso);
    }


    @RequestMapping(value = "/clientes", method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model)
    {
        Pageable pageRequest = PageRequest.of(page, 6);

        Page<Cliente> clientes = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/clientes", clientes);

        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "clientes";
    }

    @RequestMapping(value = "form") // Por defecto es un GET
    public String crear(Map<String, Object> model)
    {
        Cliente cliente = new Cliente();
        model.put("titulo", "Crear Cliente");
        model.put("cliente", cliente);
        return "form";
    }

    @RequestMapping(value = "form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status,
        RedirectAttributes flash, @RequestParam("file") MultipartFile foto)
    {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Crear Cliente");
            return "form";
        }

        if (!foto.isEmpty()) 
        {
            if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null && cliente.getFoto().length() > 0) 
            {
                uploadFileService.delete(cliente.getFoto());
            }
        }
        String uniqueFilename = null;

        try 
        {
            uniqueFilename = uploadFileService.copy(foto);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

        flash.addFlashAttribute("info", "Has subido correctamente: '" + uniqueFilename + "'");

        cliente.setFoto(uniqueFilename);

        String mensajeFlash = (cliente.getId() != null)? "Cliente editado con exito" : "Cliente creado con exito";

        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:clientes";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) 
    {
        Cliente cliente = new Cliente();

        if (id > 0) 
        {
            cliente = clienteService.findOne(id);    

            if (cliente == null) 
            {
                flash.addFlashAttribute("error","El ID del cliente no existe en la BD");
                return "redirect:/clientes";
            }
        }
        else
        {
            flash.addFlashAttribute("error","El ID del cliente no puede ser 0");
            return "redirect:/clientes";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Editar cliente");

        return "form";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash)
    {
        if (id > 0 ) 
        {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);   
            flash.addFlashAttribute("success", "Cliente eliminado con exito");

            if(uploadFileService.delete(cliente.getFoto()))
            {
                flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " ha sido eliminada");
            }
        }
        return "redirect:/clientes";
    }

}
