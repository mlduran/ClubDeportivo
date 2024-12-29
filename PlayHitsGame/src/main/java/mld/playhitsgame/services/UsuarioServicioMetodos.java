/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import mld.playhitsgame.DAO.UsuarioDAO;
import mld.playhitsgame.exemplars.FiltroUsuarios;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.projections.ampliada.UsuarioAmpliadaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

        obj.setIdioma(usuario.getIdioma());
        usuario.setDobleTouch(usuario.isDobleTouch());
        obj.setSegEspera(usuario.getSegEspera());
        obj.setGrupo(usuario.getGrupo());

        obj.setPuntos(usuario.getPuntos());
        obj.setEstrellas(usuario.getEstrellas());
        obj.setActivo(usuario.isActivo());
        obj.setAlta(usuario.getAlta());
        obj.setRoles(usuario.getRoles());

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

    public List<Usuario> findByFiltroBasico(FiltroUsuarios filtroUsuario) {

        List<Usuario> usuarios = DAO.findAll();
        List<Usuario> usuariosFiltro = new ArrayList();
        for (Usuario usu : usuarios) {
            if (usu.isActivo() == filtroUsuario.isActivo()) {
                usuariosFiltro.add(usu);
            }
        }
        return usuariosFiltro;
    }

    public Page<Usuario> findByFiltroBasico(FiltroUsuarios filtroUsuario, int numeroPagina, int tamanioPagina) {
        List<Usuario> usuarios = DAO.findAll(Sort.by(Sort.Direction.DESC, "alta")); // Supongo que `DAO` devuelve una lista completa.

        // Filtra los usuarios según el filtro
        List<Usuario> usuariosFiltro = new ArrayList<>();
        for (Usuario usu : usuarios) {
            if (usu.isActivo() == filtroUsuario.isActivo()) {
                usuariosFiltro.add(usu);
            }
        }

        // Calcula los índices para paginación manual
        int start = numeroPagina * tamanioPagina;
        int end = Math.min(start + tamanioPagina, usuariosFiltro.size());

        // Verifica si los índices están fuera de rango
        List<Usuario> usuariosPaginados = start > end ? new ArrayList<>() : usuariosFiltro.subList(start, end);

        // Retorna un objeto Page creado a partir de los usuarios paginados
        return new PageImpl<>(usuariosPaginados, Pageable.ofSize(tamanioPagina).withPage(numeroPagina), usuariosFiltro.size());

    }

    @Override
    public List<Usuario> usuariosEstrella() {
        return DAO.usuariosEstrella();
    }
}
