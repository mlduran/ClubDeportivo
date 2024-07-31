/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.seguridad;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author miguel
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UsuarioServicioMetodos servUsuario;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
        Optional<Usuario> buscarUserdetails = servUsuario.findByUsuario(username);
        
        try {
        if (buscarUserdetails.isEmpty())
            throw new UsernameNotFoundException("El usuario " + username + " no existe");
        }catch (Exception e){
            e.printStackTrace();
        }
        
        Usuario userdetails = buscarUserdetails.get();
        
        Collection<? extends GrantedAuthority> authorities = userdetails.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
                .collect(Collectors.toSet());
        
        return new User(
                userdetails.getUsuario(),
                userdetails.getContrasenya(),
                true,true,true,true,
                authorities
        );    
    }    
}
