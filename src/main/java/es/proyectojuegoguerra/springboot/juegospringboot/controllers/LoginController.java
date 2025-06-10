package es.proyectojuegoguerra.springboot.juegospringboot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.proyectojuegoguerra.springboot.juegospringboot.entities.GuerreroEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.services.UsuarioService;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

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
    public String guerreroNuevo(@RequestParam("vehiculoId") Long vehiculoId, Model model) {
        model.addAttribute("vehiculoId", vehiculoId);
        model.addAttribute("guerrero", new GuerreroEntity());
        return "anadirGuerrero"; // nombre del html en minúscula y sin extensión
    }
}
