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
import org.springframework.web.bind.annotation.ResponseBody;

import es.proyectojuegoguerra.springboot.juegospringboot.entities.VehiculoEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.repositories.VehiculoRepository;
import es.proyectojuegoguerra.springboot.juegospringboot.services.CombateService;

@Controller
public class VehiculoController {

    @Autowired
    private VehiculoRepository vehiculoRepo;
    
    //BORRAR SI DA ERROR =========================================================================
    @Autowired
    private CombateService combateService;

    /*@GetMapping("/combate")
    @ResponseBody
    public String iniciarCombate(@RequestParam Long vehiculo1Id, @RequestParam Long vehiculo2Id) {
        return combateService.combate(vehiculo1Id, vehiculo2Id);
    }*/
    
    @GetMapping("/combate")
    public String iniciarCombate(
            @RequestParam Long vehiculo1Id,
            @RequestParam Long vehiculo2Id,
            Model model) {

        String resultado = combateService.combate(vehiculo1Id, vehiculo2Id);

        // DEBE ESTAR CARGADO LA LISTA DE VEHICULOS PARA MOSTRAR LA TABLA
        List<VehiculoEntity> vehiculos = vehiculoRepo.findAll();
        model.addAttribute("vehiculos", vehiculos);

        // Pasa el resultado del combate al modelo para mostrar en la vista
        model.addAttribute("resultadoCombate", resultado);

        return "listadoVehiculo"; // nombre de la plantilla Thymeleaf
    }
    
    //=============================================================================================

    // Mostrar listado de vehículos
    @GetMapping("/listadoVehiculo")
    public String mostrarListado(Model model) {
        List<VehiculoEntity> vehiculos = vehiculoRepo.findAll();
        model.addAttribute("vehiculos", vehiculos);
        return "listadoVehiculo";
    }

    // Mostrar formulario para añadir nuevo vehículo
    @GetMapping("/AnadirVehiculo")
    public String mostrarFormulario() {
        return "AnadirVehiculo";
    }

    // Guardar vehículo desde formulario
    @PostMapping("/anadirVehiculo")
    public String guardarVehiculo(@RequestParam String nombre,
                                 @RequestParam String tipo,
                                 @RequestParam int vida,
                                 @RequestParam int ataque,
                                 @RequestParam int defensa,
                                 Model model) {

        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            model.addAttribute("error", "El nombre es obligatorio.");
            return "AnadirVehiculo";
        }
        
        if (vida < 0) {
            model.addAttribute("error", "La vida no puede ser negativa.");
            return "AnadirVehiculo";
        }

        if (ataque < 1 || ataque > 10) {
            model.addAttribute("error", "El ataque debe ser entre 1 y 10.");
            return "AnadirVehiculo";
        }

        if (defensa < 1 || defensa > 10) {
            model.addAttribute("error", "La defensa debe ser entre 1 y 10.");
            return "AnadirVehiculo";
        }

        if (ataque + defensa > 10) {
            model.addAttribute("error", "La suma de ataque y defensa no puede ser mayor a 10.");
            return "AnadirVehiculo";
        }

        VehiculoEntity nuevo = new VehiculoEntity(nombre, tipo, vida, ataque, defensa);
        vehiculoRepo.save(nuevo);
        return "redirect:/listadoVehiculo";
    }

    // Eliminar vehículo por ID
    @GetMapping("/eliminarVehiculo/{id}")
    public String eliminarVehiculo(@PathVariable Long id) {
        vehiculoRepo.deleteById(id);
        return "redirect:/listadoVehiculo";
    }

    // Mostrar formulario para editar un vehículo existente
    @GetMapping("/editarVehiculo/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        VehiculoEntity vehiculo = vehiculoRepo.findById(id).orElse(null);
        model.addAttribute("vehiculo", vehiculo);
        return "EditarVehiculo"; // Asegúrate de tener este HTML
    }

    // Guardar cambios tras edición
    @PostMapping("/editarVehiculo")
    public String editarVehiculo(@ModelAttribute VehiculoEntity vehiculo) {
        vehiculoRepo.save(vehiculo);
        return "redirect:/listadoVehiculo";
    }
    
    @GetMapping("/vehiculo/{id}")
    public String verDetalleVehiculo(@PathVariable Long id, Model model) {
        VehiculoEntity vehiculo = vehiculoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        model.addAttribute("vehiculo", vehiculo);
        return "vehiculoDetalle";
    }

    @PostMapping("/vehiculo/restaurar/{id}")
    public String restaurarVidaVehiculo(@PathVariable Long id) {
        VehiculoEntity vehiculo = vehiculoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
        vehiculo.setVida(1000);
        vehiculoRepo.save(vehiculo);
        return "redirect:/vehiculo/" + id;
    }
}