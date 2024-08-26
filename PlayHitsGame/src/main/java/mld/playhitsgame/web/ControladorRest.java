/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author miguel
 */
@RestController
@Slf4j
public class ControladorRest {

    @Autowired
    UsuarioServicioMetodos servUsuario;
    @Autowired
    private PasswordEncoder passwordEncoder;
     
    @GetMapping("/validarUsuario")
    public String validarUsuario(Model modelo, HttpServletRequest req) {        
        
        Long id = Long.valueOf(req.getParameter("id"));
        String token = (String) req.getParameter("token");

        Optional<Usuario> usu = servUsuario.findById(id);
        if (usu.isPresent()) {
            Usuario usuario = usu.get();
            if (passwordEncoder.matches(usuario.getUsuario(), token)) {
                usuario.setActivo(true);
                servUsuario.update(id, usuario);
                return "Usuario Validado, ya puedes acceder a PlayHitsGame";   
            } else {
                return "error";
            }
        } else {
            return "error";
        }
    }

}
