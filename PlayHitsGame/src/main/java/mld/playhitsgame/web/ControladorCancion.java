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
import mld.playhitsgame.exemplars.FiltroCanciones;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.CancionServicioMetodos;
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

    @GetMapping("/modificarTema/{id}")
    public String modificarTema(@ModelAttribute("id") Long id, Model modelo) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        Optional<Tema> tema = servTema.findById(id);
        modelo.addAttribute("tema", tema);
        return "ModificarTema";
    }

    @PostMapping("/modificarTema")
    public String modificarCancion(@ModelAttribute("tema") Tema tema, Model modelo) {

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

    private void gestionCanciones(Model modelo, int page) {

        FiltroCanciones filtro;
        if (modelo.getAttribute("filtroCanciones") == null) {
            filtro = new FiltroCanciones();
            modelo.addAttribute("filtroCanciones", filtro);
        } else {
            filtro = (FiltroCanciones) modelo.getAttribute("filtroCanciones");
        }

        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);

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
        gestionCanciones(modelo, page);
        return "GestionCanciones";
    }
    
    
    
    private void corregirTitulos(List<Cancion> canciones){
        
        for (Cancion cancion : canciones){
            String newTitulo = Utilidades.filtrarTitulo(cancion.getTitulo());
            if (!newTitulo.equals(cancion.getTitulo())){
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
        gestionCanciones(modelo, page);
        return "GestionCanciones";
    }

    @GetMapping("/editarCanciones")
    public String editarCanciones(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }
        gestionCanciones(modelo, page);
        return "EditarCanciones";
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
                if (isCambio) {
                    cancion.setRevisar(false);
                    servCancion.updateCancion(cancion.getId(), cancion);
                }
            }
        }
        gestionCanciones(modelo, Integer.parseInt(pageString));

        return "EditarCanciones";
    }

    @GetMapping("/exportarCanciones")
    public String exportarCanciones(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

        gestionCanciones(modelo, page);
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
        gestionCanciones(modelo, page);
        return "GestionCanciones";
    }

    @PostMapping("/cambiosMasivos")
    public String marcarRevision(Model modelo, HttpServletRequest req) {

        if (!usuarioCorrecto(modelo)) {
            return "redirect:/logout";
        }

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

        FiltroCanciones filtro = (FiltroCanciones) modelo.getAttribute("filtroCanciones");
        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);
        
        if (filtro.isDuplicados()) {
            canciones = Utilidades.buscarDuplicados(canciones, false, false);
        }
        
        Pageable pageable = PageRequest.of(page, REG_POR_PAG);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), canciones.size());

        List<Cancion> sublist = canciones.subList(start, end);
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
        temasBD(modelo);
        gestionCanciones(modelo, page);
        return "GestionCanciones";
    }

}
