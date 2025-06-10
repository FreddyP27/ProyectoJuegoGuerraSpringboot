package es.proyectojuegoguerra.springboot.juegospringboot.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.proyectojuegoguerra.springboot.juegospringboot.entities.UsuarioEntity;
import es.proyectojuegoguerra.springboot.juegospringboot.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean validarUsuario(String usuario, String contrasena) {
        Optional<UsuarioEntity> usuarioBD = usuarioRepository.findByUsuario(usuario.trim());

        if (usuarioBD.isPresent()) {
            String contrasenaBD = usuarioBD.get().getContrasena().trim();
            System.out.println("Comparando contrasena: " + contrasena + " con BD: " + contrasenaBD);
            return contrasenaBD.equals(contrasena.trim());
        }
        return false;
    }
}
