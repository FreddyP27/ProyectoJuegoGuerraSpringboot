package es.proyectojuegoguerra.springboot.juegospringboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.proyectojuegoguerra.springboot.juegospringboot.entities.GuerreroEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.entities.VehiculoEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.repositories.GuerreroRepository;
import es.proyectojuegoguerra.springboot.juegospringboot.repositories.VehiculoRepository;

@Controller
public class GuerreroController {

    @Autowired
    private GuerreroRepository guerreroRepo;

    @Autowired
    private VehiculoRepository vehiculoRepo;

    // Mostrar listado de guerreros, opcionalmente filtrado por tipo
    @GetMapping("/listadoGuerrero")
    public String mostrarListado(@RequestParam(name = "tipo", required = false) String tipo, Model model) {
        List<GuerreroEntity> guerreros;

        if (tipo == null || tipo.equals("todos")) {
            guerreros = guerreroRepo.findAll();
        } else {
            guerreros = guerreroRepo.findByTipo(tipo);
        }

        model.addAttribute("guerreros", guerreros);
        model.addAttribute("tipoSeleccionado", tipo != null ? tipo : "todos");

        return "listadoGuerreros";
    }

    // Mostrar formulario para añadir guerrero, recibiendo vehiculoId
    /*@GetMapping("/anadirGuerrero")
    public String mostrarFormulario(@RequestParam("vehiculoId") Long vehiculoId, Model model) {
        model.addAttribute("vehiculoId", vehiculoId);
        model.addAttribute("guerrero", new GuerreroEntity());
        return "formularioAnadirGuerrero"; // Nombre de la vista para el formulario
    }*/

    // Guardar guerrero con relación a vehículo
    @PostMapping("/guardarGuerrero")
    public String guardarGuerrero(@ModelAttribute GuerreroEntity guerrero,
                                  @RequestParam("vehiculoId") Long vehiculoId,
                                  RedirectAttributes redirectAttributes) {
        VehiculoEntity vehiculo = vehiculoRepo.findById(vehiculoId).orElse(null);

        if (vehiculo == null) {
            redirectAttributes.addFlashAttribute("error", "Vehículo no encontrado");
            return "redirect:/listadoVehiculo";
        }

        // Validar ataque y defensa
        int ataque = guerrero.getAtaque();
        int defensa = guerrero.getDefensa();

        if (ataque < 1 || ataque > 10) {
            redirectAttributes.addFlashAttribute("error", "El ataque debe estar entre 1 y 10.");
            return "redirect:/anadirGuerrero";
        }

        if (defensa < 1 || defensa > 10) {
            redirectAttributes.addFlashAttribute("error", "La defensa debe estar entre 1 y 10.");
            return "redirect:/anadirGuerrero";
        }

        if (ataque + defensa > 10) {
            redirectAttributes.addFlashAttribute("error", "La suma de ataque y defensa no puede ser mayor a 10.");
            return "redirect:/anadirGuerrero";
        }

        // Validar compatibilidad guerrero-vehículo
        if (!esCompatible(guerrero.getTipo(), vehiculo.getTipo())) {
            redirectAttributes.addFlashAttribute("error",
                "El guerrero de tipo '" + guerrero.getTipo() + "' no puede embarcar en vehículo tipo '" + vehiculo.getTipo() + "'");
            return "redirect:/anadirGuerrero";
        }

        guerrero.setVehiculo(vehiculo);
        guerreroRepo.save(guerrero);

        redirectAttributes.addFlashAttribute("success", "Guerrero guardado correctamente");
        return "redirect:/listadoVehiculo";
    }
    // Aquí va el método privado justo dentro de la clase
    private boolean esCompatible(String tipoGuerrero, String tipoVehiculo) {
        tipoGuerrero = tipoGuerrero.toLowerCase();
        tipoVehiculo = tipoVehiculo.toLowerCase();

        switch (tipoGuerrero) {
            case "soldado":
                return tipoVehiculo.contains("tanque");
            case "vikingo":
                return tipoVehiculo.contains("vikingo");
            case "alienigena":
            case "alienígena":
                return tipoVehiculo.contains("alienigena");
            default:
                return false;
        }
    }
    
    //Aqui va el metodo para eliminar guerrero
    @GetMapping("/guerrero/eliminar/{id}")
    public String eliminarGuerrero(@PathVariable Long id, @RequestParam("vehiculoId") Long vehiculoId) {
        guerreroRepo.deleteById(id);
        return "redirect:/vehiculo/" + vehiculoId;
    }
}
