/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.CancionTmp;
import mld.playhitsgame.exemplars.FiltroCanciones;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.CancionTmpServicioMetodos;
import mld.playhitsgame.services.TemaServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import mld.playhitsgame.utilidades.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({"filtroCanciones", "id_usuarioSesion"})
@Slf4j
public class ControladorCancion {

    Integer REG_POR_PAG = 100;

    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    CancionTmpServicioMetodos servCancionTmp;
    @Autowired
    TemaServicioMetodos servTema;
    @Autowired
    UsuarioServicioMetodos servUsuario;

    @Value("${custom.rutaexportfiles}")
    private String rutaexportfiles;

    private boolean usuarioCorrecto(Model modelo) {

        boolean correcto = false;
        Long id_usu = (Long) modelo.getAttribute("id_usuarioSesion");
        Usuario usuarioSesion;
        try {
            usuarioSesion = servUsuario.findById(id_usu).get();
            correcto = usuarioSesion.isAdmin() || usuarioSesion.isColaborador();
        } catch (Exception ex) {
        }
        return correcto;
    }

    @GetMapping("/listaCanciones")
    public String listaCanciones(Model model) {

        if (!usuarioCorrecto(model)) {
            return "redirect:/logout";
        }

        List<Cancion> lista = servCancion.findAll();
        List errores = new ArrayList();

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        TreeMap<Integer, Integer> estadistica = new TreeMap();
        for (int i = 1950; i <= year; i++) {
            estadistica.put(i, 0);
        }

        for (Cancion obj : lista) {
            try {
                estadistica.put(obj.getAnyo(), estadistica.get(obj.getAnyo()) + 1);
            } catch (Exception e) {
                errores.add(String.valueOf(obj.getId()) + "\t" + obj.getTitulo() + "\t"
                        + obj.getInterprete() + "\t" + String.valueOf(obj.getAnyo()));
            }
        }

        model.addAttribute("estadistica", estadistica);
        model.addAttribute("lista", lista);
        model.addAttribute("errores", errores);

        return "ListaCanciones";
    }

    @GetMapping("/altaCancion")
    public String altaCancion(Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }
        modelo.addAttribute("newcancion", new Cancion());
        return "AltaCancion";
    }

    @PostMapping("/altaCancion")
    public String altaCancion(@ModelAttribute("newcancion") Cancion cancion, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        String resp = "OK";

        try {
            servCancion.saveCancion(cancion);
        } catch (Exception ex) {
            resp = "ERROR " + ex;
        }

        modelo.addAttribute("result", resp);

        return "AltaCancion";
    }

    @GetMapping("/modificarCancion/{id}")
    public String modificarCancion(@ModelAttribute("id") Long id, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        Optional<Cancion> cancion = servCancion.findById(id);
        temasBD(modelo);

        modelo.addAttribute("cancion", cancion);
        return "ModificarCancion";
    }

    @PostMapping("/modificarCancion")
    public String modificarCancion(@ModelAttribute("cancion") Cancion cancion, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        String resp = "OK";

        try {
            servCancion.updateCancion(cancion.getId(), cancion);
        } catch (Exception ex) {
            resp = "ERROR " + ex;
        }

        temasBD(modelo);
        modelo.addAttribute("result", resp);

        return "ModificarCancion";
    }

    @GetMapping("/modificarCancionTmp/{id}")
    public String modificarCancionTmp(@ModelAttribute("id") Long id, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        Optional<CancionTmp> cancion = servCancionTmp.findById(id);
        temasBD(modelo);

        modelo.addAttribute("cancion", cancion);
        return "ModificarCancionTmp";
    }

    @PostMapping("/modificarCancionTmp")
    public String modificarCancionTmp(@ModelAttribute("cancion") CancionTmp cancion, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        String resp = "OK";

        try {
            servCancionTmp.updateCancionTmp(cancion.getId(), cancion);
        } catch (Exception ex) {
            resp = "ERROR " + ex;
        }

        temasBD(modelo);
        modelo.addAttribute("result", resp);

        return "ModificarCancionTmp";
    }

    @GetMapping("/modificarTema/{id}")
    public String modificarTema(@ModelAttribute("id") Long id, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        Optional<Tema> tema = servTema.findById(id);
        modelo.addAttribute("temaUpdate", tema);
        return "ModificarTema";
    }

    @PostMapping("/modificarTema")
    public String modificarCancion(@ModelAttribute("temaUpdate") Tema tema, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        String resp = "OK";

        try {
            servTema.update(tema.getId(), tema);
        } catch (Exception ex) {
            resp = "ERROR " + ex;
        }

        modelo.addAttribute("result", resp);

        return "ModificarTema";
    }

    @GetMapping("/eliminarCancion/{id}")
    public String eliminarCancion(@ModelAttribute("id") Long id, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        servCancion.deleteCancion(id);
        return "redirect:/gestionCanciones";
    }

    @GetMapping("/eliminarCancionTmp/{id}")
    public String eliminarCancionTmp(@ModelAttribute("id") Long id, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        servCancionTmp.deleteCancionTmp(id);
        return "redirect:/gestionCancionesTmp";
    }

    @GetMapping("/eliminarTema/{id}")
    public String eliminarTema(@ModelAttribute("id") Long id, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        Optional<Tema> tema = servTema.findById(id);

        if (tema.isPresent() && tema.get().getCanciones().isEmpty()) {
            servTema.delete(tema.get());
            return "redirect:/gestionTemas";
        }

        ArrayList<Tema> temas = (ArrayList<Tema>) servTema.findAll();
        modelo.addAttribute("temas", temas);
        modelo.addAttribute("result", "No se puede eliminar un tema con canciones");

        return "GestionTemas";

    }

    private void temasBD(Model modelo) {

        ArrayList temas = new ArrayList();
        temas.add("");
        for (Tema tema : servTema.findAll()) {
            temas.add(tema.getTema());
        }
        modelo.addAttribute("temas", temas);

    }

    private void gestionCanciones(Model modelo, int page, boolean isTmp) {

        FiltroCanciones filtro;
        if (modelo.getAttribute("filtroCanciones") == null) {
            filtro = new FiltroCanciones();
            modelo.addAttribute("filtroCanciones", filtro);
        } else {
            filtro = (FiltroCanciones) modelo.getAttribute("filtroCanciones");
        }

        List canciones;
        if (isTmp) {
            canciones = servCancionTmp.buscarCancionesPorFiltro(filtro);
        } else {
            canciones = servCancion.buscarCancionesPorFiltro(filtro);
        }

        if (filtro.isDuplicados()) {
            canciones = Utilidades.buscarDuplicados(canciones, false, false);
        }

        Pageable pageable = PageRequest.of(page, REG_POR_PAG);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), canciones.size());

        List<Cancion> sublist = canciones.subList(start, end);

        modelo.addAttribute("canciones",
                new PageImpl<>(sublist, pageable, canciones.size()));
        modelo.addAttribute("paginaActual", page);
    }

    @GetMapping("/gestionCanciones")
    public String revisionCanciones(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        temasBD(modelo);
        gestionCanciones(modelo, page, false);
        return "GestionCanciones";
    }

    @GetMapping("/gestionCancionesTmp")
    public String revisionCancionesTmp(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        temasBD(modelo);
        gestionCanciones(modelo, page, true);
        return "GestionCancionesTmp";
    }

    private void corregirTitulos(List<Cancion> canciones) {

        for (Cancion cancion : canciones) {
            String newTitulo = Utilidades.filtrarTitulo(cancion.getTitulo());
            if (!newTitulo.equals(cancion.getTitulo())) {
                cancion.setTitulo(newTitulo);
                servCancion.updateCancion(cancion.getId(), cancion);
            }
        }
    }

    @GetMapping("/corregirDuplicados")
    public String corregirDuplicados(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        FiltroCanciones filtro;
        if (modelo.getAttribute("filtroCanciones") == null) {
            filtro = new FiltroCanciones();
            modelo.addAttribute("filtroCanciones", filtro);
        } else {
            filtro = (FiltroCanciones) modelo.getAttribute("filtroCanciones");
        }

        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);
        corregirTitulos(canciones);

        for (Cancion cancion : Utilidades.duplicadosParaActualizar(canciones)) {
            servCancion.updateCancion(cancion.getId(), cancion);
        }
        for (Cancion cancion : Utilidades.duplicadosParaEliminar(canciones)) {
            // puede haber canciones que se esta utilizando
            // estas no se podran eliminar
            try {
                servCancion.deleteCancion(cancion.getId());
            } catch (Exception e) {
                Logger.getLogger(ControladorCancion.class.getName()).log(Level.WARNING, "La Cancion se esta utilizando", cancion);
            }
        }

        temasBD(modelo);
        gestionCanciones(modelo, page, true);
        return "GestionCanciones";
    }

    @GetMapping("/editarCanciones")
    public String editarCanciones(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }
        gestionCanciones(modelo, page, false);
        return "EditarCanciones";
    }

    @GetMapping("/editarCancionesTmp")
    public String editarCancionesTmp(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }
        gestionCanciones(modelo, page, true);
        return "EditarCancionesTmp";
    }

    @PostMapping("/editarCanciones")
    public String editarCanciones(Model modelo, HttpServletRequest req) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        String pageString = req.getParameter("paginaActual");
        int page = Integer.parseInt(pageString);

        FiltroCanciones filtro = (FiltroCanciones) modelo.getAttribute("filtroCanciones");
        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);

        Pageable pageable = PageRequest.of(page, REG_POR_PAG);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), canciones.size());

        List<Cancion> sublist = canciones.subList(start, end);
        for (Cancion cancion : sublist) {
            boolean isCambio = false;
            if (req.getParameter(cancion.selId()) != null) {
                if (!cancion.getTitulo().equals(req.getParameter("titulo_" + cancion.selId()))) {
                    cancion.setTitulo(req.getParameter("titulo_" + cancion.selId()));
                    isCambio = true;
                }
                if (!cancion.getInterprete().equals(req.getParameter("interprete_" + cancion.selId()))) {
                    cancion.setInterprete(req.getParameter("interprete_" + cancion.selId()));
                    isCambio = true;
                }
                int anyo = Integer.parseInt(req.getParameter("anyo_" + cancion.selId()));
                if (cancion.getAnyo() != anyo) {
                    cancion.setAnyo(anyo);
                    isCambio = true;
                }
                if (!cancion.getSpotifyplay().equals(req.getParameter("spotifyplay_" + cancion.selId()))) {
                    cancion.setSpotifyplay(req.getParameter("spotifyplay_" + cancion.selId()));
                    isCambio = true;
                }
                if (isCambio) {
                    cancion.setRevisar(false);
                    servCancion.updateCancion(cancion.getId(), cancion);
                }
            }
        }
        gestionCanciones(modelo, Integer.parseInt(pageString), true);

        return "EditarCanciones";
    }

    @PostMapping("/editarCancionesTmp")
    public String editarCancionesTmp(Model modelo, HttpServletRequest req) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        String pageString = req.getParameter("paginaActual");
        int page = Integer.parseInt(pageString);

        FiltroCanciones filtro = (FiltroCanciones) modelo.getAttribute("filtroCanciones");
        List<CancionTmp> canciones = servCancionTmp.buscarCancionesPorFiltro(filtro);

        Pageable pageable = PageRequest.of(page, REG_POR_PAG);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), canciones.size());

        List<CancionTmp> sublist = canciones.subList(start, end);
        for (CancionTmp cancion : sublist) {
            boolean isCambio = false;
            if (req.getParameter(cancion.selId()) != null) {
                if (!cancion.getTitulo().equals(req.getParameter("titulo_" + cancion.selId()))) {
                    cancion.setTitulo(req.getParameter("titulo_" + cancion.selId()));
                    isCambio = true;
                }
                if (!cancion.getInterprete().equals(req.getParameter("interprete_" + cancion.selId()))) {
                    cancion.setInterprete(req.getParameter("interprete_" + cancion.selId()));
                    isCambio = true;
                }
                if (!cancion.getSpotifyplay().equals(req.getParameter("spotifyplay_" + cancion.selId()))) {
                    cancion.setSpotifyplay(req.getParameter("spotifyplay_" + cancion.selId()));
                    isCambio = true;
                }
                int anyo = Integer.parseInt(req.getParameter("anyo_" + cancion.selId()));
                if (cancion.getAnyo() != anyo) {
                    cancion.setAnyo(anyo);
                    isCambio = true;
                }
                if (isCambio) {
                    cancion.setRevisar(false);
                    servCancionTmp.updateCancionTmp(cancion.getId(), cancion);
                }
            }
        }
        gestionCanciones(modelo, Integer.parseInt(pageString), true);

        return "EditarCancionesTmp";
    }

    @GetMapping("/exportarCanciones")
    public String exportarCanciones(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        gestionCanciones(modelo, page, true);
        temasBD(modelo);
        List<Cancion> canciones = (List<Cancion>) modelo.getAttribute("canciones");
        Utilidades.exportarCanciones(canciones, rutaexportfiles);

        modelo.addAttribute("result", "Se ha exportado el fichero en el directorio " + rutaexportfiles);

        return "GestionCanciones";
    }

    @GetMapping("/gestionTemas")
    public String revisionTemas(Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }
        ArrayList<Tema> temas = (ArrayList<Tema>) servTema.findAll();
        modelo.addAttribute("temas", temas);

        return "GestionTemas";
    }

    @PostMapping("/gestionCanciones")
    public String revisionCanciones(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @ModelAttribute("filtroCanciones") FiltroCanciones filtro) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }
        temasBD(modelo);
        gestionCanciones(modelo, page, false);
        return "GestionCanciones";
    }

    @PostMapping("/gestionCancionesTmp")
    public String revisionCancionesTmp(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @ModelAttribute("filtroCanciones") FiltroCanciones filtro) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }
        temasBD(modelo);
        gestionCanciones(modelo, page, true);
        return "GestionCancionesTmp";
    }

    @PostMapping("/cambiosMasivos")
    public String cambiosMasivos(Model modelo, HttpServletRequest req) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        FiltroCanciones filtro = (FiltroCanciones) modelo.getAttribute("filtroCanciones");
        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);
        if (filtro.isDuplicados()) {
            canciones = Utilidades.buscarDuplicados(canciones, false, false);
        }
        ejecutarCambiosMasivos(modelo, req, canciones, false);
        return "GestionCanciones";
    }

    @PostMapping("/cambiosMasivosTmp")
    public String cambiosMasivosTmp(Model modelo, HttpServletRequest req) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        FiltroCanciones filtro = (FiltroCanciones) modelo.getAttribute("filtroCanciones");
        List<CancionTmp> canciones = servCancionTmp.buscarCancionesPorFiltro(filtro);
        ejecutarCambiosMasivos(modelo, req, canciones, true);
        return "GestionCancionesTmp";
    }

    private void ejecutarCambiosMasivos(Model modelo, HttpServletRequest req,
            List canciones, boolean isTmp) {

        String pageString = req.getParameter("paginaActual");
        int page = Integer.parseInt(pageString);

        String opVerificar = req.getParameter("actualizarVerificar");
        String opAnyadirTema = req.getParameter("anyadirTema");
        String opEliminarTema = req.getParameter("eliminarTema");
        String opEliminarCanciones = req.getParameter("eliminarCanciones");

        String temaModificar = req.getParameter("temaModificar");
        Tema tema = null;
        if (temaModificar != null) {
            Optional<Tema> findBytema = servTema.findBytema(temaModificar);
            if (findBytema.isPresent()) {
                tema = findBytema.get();
            }
        }
        String validar = req.getParameter("validar");
        boolean isValidar = false;
        if ("on".equals(validar)) {
            isValidar = true;
        }

        Pageable pageable = PageRequest.of(page, REG_POR_PAG);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), canciones.size());

        List sublist = canciones.subList(start, end);

        if (isTmp) {
            ejecutarCambiosBDTmp(req, sublist, tema, opVerificar, opAnyadirTema,
                    opEliminarTema, opEliminarCanciones, isValidar);
            gestionCanciones(modelo, page, true);
        } else {
            ejecutarCambiosBD(req, sublist, tema, opVerificar, opAnyadirTema,
                    opEliminarTema, opEliminarCanciones, isValidar);
            gestionCanciones(modelo, page, false);
        }

        temasBD(modelo);

    }

    private void ejecutarCambiosBD(HttpServletRequest req, List<Cancion> sublist, Tema tema,
            String opVerificar, String opAnyadirTema, String opEliminarTema, String opEliminarCanciones, boolean isValidar) {

        for (Cancion cancion : sublist) {
            if (req.getParameter(cancion.selId()) != null) {
                if ("on".equals(req.getParameter(cancion.selId()))) {

                    if (opEliminarCanciones != null && opEliminarCanciones.equals("Eliminar Canciones")) {
                        try {
                            servCancion.deleteCancion(cancion.getId());
                            System.out.println("Se elimina cancion " + String.valueOf(cancion.getId()));
                        } catch (Exception e) {
                            System.out.println("ERROR al eliminar cancion " + String.valueOf(cancion.getId()) + " " + e.getMessage());
                        }
                        continue;
                    }
                    if (opVerificar != null && opVerificar.equals("Actualizar Verificar")) {
                        cancion.setRevisar(isValidar);
                    }
                    if (opAnyadirTema != null && opAnyadirTema.equals("Añadir Tema")) {
                        cancion.anyadirTematica(tema);
                    }
                    if (opEliminarTema != null && opEliminarTema.equals("Eliminar Tema")) {
                        cancion.eliminarTematica(tema);
                    }

                    servCancion.updateCancion(cancion.getId(), cancion);
                }
            }
        }
    }

    private void ejecutarCambiosBDTmp(HttpServletRequest req, List<CancionTmp> sublist, Tema tema,
            String opVerificar, String opAnyadirTema, String opEliminarTema, String opEliminarCanciones, boolean isValidar) {

        for (CancionTmp cancion : sublist) {
            if (req.getParameter(cancion.selId()) != null) {
                if ("on".equals(req.getParameter(cancion.selId()))) {

                    if (opEliminarCanciones != null && opEliminarCanciones.equals("Eliminar Canciones")) {
                        try {
                            servCancionTmp.deleteCancionTmp(cancion.getId());
                            System.out.println("Se elimina cancion " + String.valueOf(cancion.getId()));
                        } catch (Exception e) {
                            System.out.println("ERROR al eliminar cancion " + String.valueOf(cancion.getId()) + " " + e.getMessage());
                        }
                        continue;
                    }
                    if (opVerificar != null && opVerificar.equals("Actualizar Verificar")) {
                        cancion.setRevisar(isValidar);
                    }
                    if (opAnyadirTema != null && opAnyadirTema.equals("Añadir Tema")) {
                        cancion.anyadirTematica(tema);
                    }
                    if (opEliminarTema != null && opEliminarTema.equals("Eliminar Tema")) {
                        cancion.eliminarTematica(tema);
                    }

                    servCancionTmp.updateCancionTmp(cancion.getId(), cancion);
                }
            }
        }

    }

    private void incorporarCancionesBD(Model modelo, int page, boolean eliminar) {

        FiltroCanciones filtro;
        if (modelo.getAttribute("filtroCanciones") == null) {
            filtro = new FiltroCanciones();
            modelo.addAttribute("filtroCanciones", filtro);
        } else {
            filtro = (FiltroCanciones) modelo.getAttribute("filtroCanciones");
        }
        List<CancionTmp> canciones = servCancionTmp.buscarCancionesPorFiltro(filtro);

        Pageable pageable = PageRequest.of(page, REG_POR_PAG);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), canciones.size());

        List<CancionTmp> sublist = canciones.subList(start, end);
        List<Cancion> cancionesBD = servCancion.findAll();

        for (CancionTmp cancionTmp : sublist) {
            String des = cancionTmp.getSpotifyid() + " - " + cancionTmp.getTitulo() + " - " + cancionTmp.getInterprete();

            Cancion cancionExistente;
            Optional<Cancion> cancionBD = servCancion.findByIdSpotify(cancionTmp.getSpotifyid());
            if (cancionBD.isEmpty()) {
                cancionExistente = Utilidades.existeCancion(cancionTmp, cancionesBD);
            } else {
                cancionExistente = cancionBD.get();
            }
            if (cancionExistente != null || cancionTmp.isSoloTemas()) {
                if (cancionExistente != null) {
                    if (cancionExistente.isTieneTemas(cancionTmp.getTematicas())) {
                        System.out.println("Ya tiene los temas " + des);
                    } else {
                        for (Tema tema : cancionTmp.getTematicas()) {
                            cancionExistente.anyadirTematica(tema);
                        }
                        servCancion.updateTemasCancion(cancionExistente.getId(), cancionExistente);
                        System.out.println("Se añaden tematicas a " + des);
                    }
                }
            } else {                
                Cancion cancionNew = new Cancion();
                cancionNew.setAlbum(cancionTmp.getAlbum());
                cancionNew.setAnyo(cancionTmp.getAnyo());
                cancionNew.setInterprete(cancionTmp.getInterprete());
                cancionNew.setRevisar(cancionTmp.isRevisar());
                cancionNew.setSpotifyid(cancionTmp.getSpotifyid());
                cancionNew.setSpotifyimagen(cancionTmp.getSpotifyimagen());
                cancionNew.setSpotifyplay(cancionTmp.getSpotifyplay());
                cancionNew.setTitulo(cancionTmp.getTitulo());
                Cancion cancionBDnew = servCancion.saveCancion(cancionNew);
                cancionBDnew.setTematicas(new ArrayList());
                for (Tema tema :cancionTmp.getTematicas()){
                    cancionBDnew.anyadirTematica(tema);
                }
                servCancion.updateTemasCancion(cancionBDnew.getId(), cancionBDnew);                
                
                System.out.println("Se añade NUEVA CANCION " + des);
            }

            if (eliminar) {
                servCancionTmp.deleteCancionTmp(cancionTmp.getId());
            }

        }

    }

    @GetMapping("/incorporarCancionesTmp")
    public String incorporarCancionesTmp(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        incorporarCancionesBD(modelo, page, false);
        gestionCanciones(modelo, page, true);
        return "GestionCancionesTmp";
    }

    @GetMapping("/incorporarEliminarCancionesTmp")
    public String incorporarEliminarCancionesTmp(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        incorporarCancionesBD(modelo, page, true);
        gestionCanciones(modelo, page, true);
        return "GestionCancionesTmp";
    }

}
