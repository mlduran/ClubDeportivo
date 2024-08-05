/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mld.playhitsgame.DAO.UsuarioDAO;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.projections.ampliada.UsuarioAmpliadaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicioMetodos implements UsuarioServicio {

    @Autowired
    UsuarioDAO DAO;

    @Override
    public List<Usuario> findAll() {
        return DAO.findAll();
    }

    @Override
    public List<UsuarioAmpliadaView> findBy() {
        return DAO.findBy();
    }

    @Override
    public Optional<Usuario> findByUsuario(String usuario) {
        return DAO.findByUsuario(usuario);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return DAO.findById(id);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return DAO.save(usuario);
    }

    @Override
    public Usuario update(Long id, Usuario usuario) {
        Usuario obj = DAO.findById(id).get();
        if (Objects.nonNull(usuario.getUsuario()) && !"".equalsIgnoreCase(usuario.getUsuario())) {
            obj.setUsuario(usuario.getUsuario());
        }

        obj.setAlias(usuario.getAlias());

        if (Objects.nonNull(usuario.getContrasenya()) && !"".equalsIgnoreCase(usuario.getContrasenya())) {
            obj.setContrasenya(usuario.getContrasenya());
        }

        if (Objects.nonNull(usuario.getIdioma()) && !"".equalsIgnoreCase(usuario.getIdioma())) {
            obj.setIdioma(usuario.getIdioma());
        }

        obj.setGrupo(usuario.getGrupo());

        if (Objects.nonNull(usuario.getPreferencias()) && !"".equalsIgnoreCase(usuario.getPreferencias())) {
            obj.setPreferencias(usuario.getPreferencias());
        }

        obj.setPuntos(usuario.getPuntos());
        obj.setEstrellas(usuario.getEstrellas());
        obj.setActivo(usuario.isActivo());
        obj.setAlta(usuario.getAlta());

        return DAO.save(obj);
    }

    @Override
    public void deleteById(Long id) {
        DAO.deleteById(id);
    }

    @Override
    public Optional<Usuario> usuarioLogin(String usuario, String contrasenya) {

        return DAO.usuarioLogin(usuario, contrasenya);
    }

    @Override
    public List<Usuario> usuariosGrupo(String grupo) {

        return DAO.usuariosGrupo(grupo);
    }

}
