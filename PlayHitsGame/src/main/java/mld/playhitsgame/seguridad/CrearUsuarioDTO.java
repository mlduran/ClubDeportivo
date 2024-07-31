/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.seguridad;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author miguel
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearUsuarioDTO {
    
    @Email
    @NotBlank
    private String usuario;
    private String alias; 
    @NotBlank
    private String contrasenya;
    private String grupo;
    private String idioma;
    private String preferencias;
    private Set<String> roles;
    
    
}
