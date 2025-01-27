/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;

import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.projections.ampliada.UsuarioAmpliadaView;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author miguel
 */
public interface UsuarioDAO extends JpaRepository<Usuario, Long> {

    List<UsuarioAmpliadaView> findBy();

    Optional<Usuario> findByUsuario(String usuario);

    @Override
    Optional<Usuario> findById(Long id);

    @Query(value = "SELECT * FROM usuarios WHERE usuario=:elusuario AND contrasenya=:lacontrasenya ;", nativeQuery = true)
    Optional<Usuario> usuarioLogin(String elusuario, String lacontrasenya);

    @Query(value = "SELECT * FROM usuarios WHERE grupo=:grupo ;", nativeQuery = true)
    List<Usuario> usuariosGrupo(String grupo);

    @Query(value = "SELECT u.*, COUNT(e.id) AS estrellas_count "
            + "FROM usuarios u "
            + "LEFT JOIN estrellas e ON u.id = e.usuario_id "
            + "GROUP BY u.id "
            + "HAVING estrellas_count > 0 "
            + "ORDER BY estrellas_count DESC "
            + "LIMIT 50;", nativeQuery = true)
    List<Usuario> usuariosEstrella();
    
    @Query(value = "SELECT * FROM usuarios WHERE no_correos=false AND activo=true ;", nativeQuery = true)
    List<Usuario> usuariosListaCorreoMasiva();

}
