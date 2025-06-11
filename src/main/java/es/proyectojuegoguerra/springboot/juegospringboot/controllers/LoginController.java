package es.proyectojuegoguerra.springboot.juegospringboot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.proyectojuegoguerra.springboot.juegospringboot.entities.GuerreroEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.entities.VehiculoEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.repositories.VehiculoRepository;
import es.proyectojuegoguerra.springboot.juegospringboot.services.UsuarioService;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private VehiculoRepository vehiculoRepo;

    @GetMapping("/login")
    public String mostrarFormulario() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String usuario,
                               @RequestParam String contrasena,
                               Model model) {
    	System.out.println("🚨 ENTRÓ AL MÉTODO POST /login");

        System.out.println("Login recibido: " + usuario);
        System.out.println("Contraseña recibido: "+ contrasena);
        boolean valido = usuarioService.validarUsuario(usuario, contrasena);
        System.out.println("¿Usuario válido? " + valido);
        if(valido) {
            return "redirect:/listadoVehiculo";
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            return "login";
        }
    }

    /*@GetMapping("/anadirGuerrero")
    public String guerreroNuevo() {
        return "AnadirGuerrero";
    }*/
    @GetMapping("/anadirGuerrero")
    public String mostrarFormulario(
        @RequestParam(name = "vehiculoId", required = false) Long vehiculoId,
        Model model) {

        model.addAttribute("guerrero", new GuerreroEntity());

        if (vehiculoId != null) {
            model.addAttribute("vehiculoId", vehiculoId);
            // Obtener el vehículo del repositorio
            VehiculoEntity vehiculo = vehiculoRepo.findById(vehiculoId).orElse(null);
            model.addAttribute("vehiculo", vehiculo); // <-- PASAMOS el vehículo

        } else {
            List<VehiculoEntity> vehiculos = vehiculoRepo.findAll();
            model.addAttribute("vehiculos", vehiculos);
        }

        return "anadirGuerrero";
    }
}
