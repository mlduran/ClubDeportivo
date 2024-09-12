/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.correo.EmailServicioMetodos;
import mld.playhitsgame.correo.Mail;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.FiltroCanciones;
import mld.playhitsgame.exemplars.FiltroUsuarios;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Rol;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.StatusPartida;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.exemplars.Record;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.PartidaServicioMetodos;
import mld.playhitsgame.services.RespuestaServicioMetodos;
import mld.playhitsgame.services.RondaServicioMetodos;
import mld.playhitsgame.services.TemaServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import static mld.playhitsgame.utilidades.Utilidades.*;
import mld.playhitsgame.exemplars.OpcionAnyoTmp;
import mld.playhitsgame.exemplars.OpcionInterpreteTmp;
import mld.playhitsgame.exemplars.OpcionTituloTmp;
import mld.playhitsgame.exemplars.PtsUsuario;
import mld.playhitsgame.exemplars.TipoPartida;
import mld.playhitsgame.seguridad.Roles;
import mld.playhitsgame.seguridad.UsuarioRol;
import mld.playhitsgame.services.OpcionAnyoTmpServicioMetodos;
import mld.playhitsgame.services.OpcionInterpreteTmpServicioMetodos;
import mld.playhitsgame.services.OpcionTituloTmpServicioMetodos;
import mld.playhitsgame.services.UsuarioRolServicioMetodos;
import mld.playhitsgame.utilidades.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
// rol puede ser master o invitado
// utilizamos los ids para usuario y partida de sesion, para no cargar tantos datos de
// persistencia en sesion y no usar tanta memoria
@SessionAttributes({"id_usuarioSesion", "id_partidaSesion", "posiblesinvitados",
    "rol", "filtroUsuarios", "mensajeRespuesta", "respuestaOK", "todoFallo"})
@Slf4j
public class ControladorVista {

    @Value("${custom.server.websocket}")
    private String serverWebsocket;

    @Value("${custom.server.ip}")
    private String customIp;

    private final int[] numeroRondas = {10, 15, 20, 25, 30};

    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    UsuarioServicioMetodos servUsuario;
    @Autowired
    UsuarioRolServicioMetodos servRolUsuario;
    @Autowired
    PartidaServicioMetodos servPartida;
    @Autowired
    RondaServicioMetodos servRonda;
    @Autowired
    RespuestaServicioMetodos servRespuesta;
    @Autowired
    TemaServicioMetodos servTema;
    @Autowired
    OpcionTituloTmpServicioMetodos servOpTitulo;
    @Autowired
    OpcionInterpreteTmpServicioMetodos servOpInterprete;
    @Autowired
    OpcionAnyoTmpServicioMetodos servOpAnyo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    EmailServicioMetodos servMail;

    private String urlSpotify() {

        String urlLogin = null;

        if (customIp == null || customIp.equals("")) {
            Logger.getLogger(ControladorVista.class.getName()).log(Level.WARNING, null,
                    "No se ha informado la variable customIp");
            return null;
        }

        try {
            URL url = new URL(customIp + "/api/spotify/login");
            BufferedReader urlLectura = new BufferedReader(new InputStreamReader(url.openStream()));
            urlLogin = urlLectura.readLine();
        } catch (MalformedURLException ex) {
            Logger.getLogger(ControladorSpotify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControladorSpotify.class.getName()).log(Level.SEVERE, null, ex);
        }

        return urlLogin;
    }

    private Usuario usuarioModelo(Model modelo) {

        Long id_usu = (Long) modelo.getAttribute("id_usuarioSesion");
        Usuario usuarioSesion = null;
        try {
            usuarioSesion = servUsuario.findById(id_usu).get();
        } catch (Exception ex) {
        }

        return usuarioSesion;
    }

    private void informarUsuarioModelo(Model modelo, Usuario usuario) {

        modelo.addAttribute("usuarioSesion", usuario);
    }

    private Partida partidaModelo(Model modelo) {

        Long id_part = (Long) modelo.getAttribute("id_partidaSesion");
        Partida partidaSesion = null;
        try {
            partidaSesion = servPartida.findById(id_part).get();
        } catch (Exception ex) {
        }

        return partidaSesion;
    }

    private void informarPartidaModelo(Model modelo, Partida partida) {

        modelo.addAttribute("id_partidaSesion", partida.getId());
    }

    @GetMapping("/")
    public String inicio(Model modelo) {
        return "Inicio";
    }

    @GetMapping("/logout")
    public String logout(Model modelo) {
        modelo.addAttribute("id_usuarioSesion", "");
        modelo.addAttribute("id_partidaSesion", "");
        return "Inicio";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("elUsuario") String elUsuario,
            @ModelAttribute("laContrasenya") String laContrasenya, Model modelo) {

        //String passEncrip = passwordEncoder.encode(laContrasenya);       
        //Optional<Usuario> usuLogin = servUsuario.usuarioLogin(elUsuario, passEncrip);
        String usu = elUsuario.toLowerCase();
        Optional<Usuario> usuLogin = servUsuario.findByUsuario(usu);

        if (usuLogin.isEmpty()) {
            modelo.addAttribute("error", "Usuario o password incorrectos");
            return "Inicio";
        } else {
            Usuario usuarioSesion = usuLogin.get();

            boolean ok = passwordEncoder.matches(laContrasenya, usuarioSesion.getContrasenya());

            if (!ok) {
                modelo.addAttribute("error", "Usuario o password incorrectos");
                return "Inicio";
            }

            if (!usuarioSesion.isActivo()) {
                modelo.addAttribute("error", "Debes activar tu cuenta con el enlace que se envió a tu cuenta de correo, comprueba la carpeta de spawn");
                return "Inicio";
            }
            usuarioSesion.getPartidasInvitado();
            usuarioSesion.getPartidasMaster();
            if (usuarioSesion.isAdmin()) {
                modelo.addAttribute("urlSpotify", urlSpotify());
            }
            modelo.addAttribute("id_usuarioSesion", usuarioSesion.getId());
            informarUsuarioModelo(modelo, usuarioSesion);
            return "redirect:/panel";
        }
    }

    private void ayuda(Model modelo, String nomFich) {

        ArrayList<String> ayuda = Utilidades.leerAyuda("static/ayuda/" + nomFich);
        modelo.addAttribute("ayuda", ayuda);

    }

    @GetMapping("/panel")
    public String panel(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }

        if (usu.isAdmin()) {
            modelo.addAttribute("urlSpotify", urlSpotify());
        }
        informarUsuarioModelo(modelo, usu);

        ayuda(modelo, "panel.txt");

        return "Panel";
    }

    @GetMapping("/partidaMaster")
    public String partidaMaster(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }
        modelo.addAttribute("rol", Rol.master);
        Partida partida = usu.partidaMasterEnCurso();

        return partidaGrupo(modelo, partida);
    }

    @GetMapping("/partidaInvitado/{id}")
    public String partidaInvitado(Model modelo, @PathVariable Long id) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }
        modelo.addAttribute("rol", Rol.invitado);

        Partida partida = null;
        for (Partida p : usu.getPartidasInvitado()) {
            if (p.getId().equals(id)) {
                partida = p;
            }
        }

        return partidaGrupo(modelo, partida);
    }

    public String partidaGrupo(Model modelo, Partida partida) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }
        informarPartidaModelo(modelo, partida);
        if (partida == null) {
            return "redirect:/panel";
        }
        informarUsuarioModelo(modelo, usu);
        Ronda rondaActual = partida.getRondas().get(partida.getRondaActual() - 1);
        List<OpcionTituloTmp> opcTitulos
                = servOpTitulo.findByPartidaRonda(partida.getId(), rondaActual.getId());
        List<OpcionInterpreteTmp> opcInterpretes
                = servOpInterprete.findByPartidaRonda(partida.getId(), rondaActual.getId());
        List<PtsUsuario> ptsUsuarios = new ArrayList();
        for (Usuario usuPartida : partida.usuariosPartida()) {
            PtsUsuario ptsUsuario = new PtsUsuario();
            ptsUsuario.setUsuario(usuPartida);
            ptsUsuario.setPuntos(usuPartida.getTxtPuntosPartida(partida));
            ptsUsuarios.add(ptsUsuario);
        }

        modelo.addAttribute("serverWebsocket", this.serverWebsocket);
        modelo.addAttribute("opcTitulos", opcTitulos);
        modelo.addAttribute("opcInterpretes", opcInterpretes);
        modelo.addAttribute("id_partidaSesion", partida.getId());
        modelo.addAttribute("respuestas", partida.respuestasUsuario(usu));
        modelo.addAttribute("ptsUsuario", ptsUsuarios);
        ayuda(modelo, "partidaGrupo.txt");
        return "PartidaGrupo";
    }

    private Ronda darDeAltaRonda(Partida partida) {

        Ronda newRonda = new Ronda();
        newRonda.setNumero(partida.getRondaActual());
        newRonda.setCompletada(false);
        newRonda.setPartida(partida);
        FiltroCanciones filtro = new FiltroCanciones();
        filtro.setAnyoInicial(partida.getAnyoInicial());
        filtro.setAnyoFinal(partida.getAnyoFinal());
        filtro.setTema(partida.getTema());
        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);
        if (partida.getNCanciones() == 0) {
            partida.setNCanciones(canciones.size());
            servPartida.updatePartida(partida.getId(), partida);
        }
        Cancion cancionSel = null;
        boolean isOK = false;
        while (!isOK) {

            cancionSel = cancionRandom(canciones);
            boolean isExiste = false;
            for (Cancion cancionPart : partida.canciones()) {
                if (cancionPart.equals(cancionSel)) {
                    isExiste = true;
                    break;
                }
            }
            if (!isExiste) {
                isOK = true;
            }
            canciones.remove(cancionSel);
            if (canciones.isEmpty()) // TODO Aqui se deberia de acabar la partida
            {
                throw new IndexOutOfBoundsException("Ya no hay mas canciones");
            }

        }
        newRonda.setCancion(cancionSel);
        newRonda.setRespuestas(new ArrayList());
        newRonda.setInicio(LocalTime.now());
        Ronda ronda = servRonda.saveRonda(newRonda);

        // Crear las opciones para las respuestas
        for (OpcionTituloTmp op : opcionesTitulosCanciones(ronda, canciones)) {
            servOpTitulo.saveOpcionTituloTmp(op);
        }
        for (OpcionInterpreteTmp op : opcionesInterpretesCanciones(ronda, canciones)) {
            servOpInterprete.saveOpcionInterpreteTmp(op);
        }
        for (OpcionAnyoTmp op : opcionesAnyosCanciones(ronda)) {
            servOpAnyo.saveOpcionAnyoTmp(op);
        }

        return ronda;
    }

    @GetMapping("/partidaPersonal")
    public String partidaPersonal(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }
        informarUsuarioModelo(modelo, usu);

        Partida partida = partidaModelo(modelo);

        if (partida == null) {
            Optional<Partida> partidaUsuarioMaster = servPartida.partidaUsuarioMaster(usu.getId());
            if (partidaUsuarioMaster.isPresent() && partidaUsuarioMaster.get().isTipoPersonal()) {
                partida = partidaUsuarioMaster.get();
            }
        }
        if (partida == null) {
            return "redirect:/panel";
        }
        informarPartidaModelo(modelo, partida);

        Ronda ultimaRonda;
        boolean todoFallo = false;
        if (modelo.getAttribute("todoFallo") != null) {
            todoFallo = (boolean) modelo.getAttribute("todoFallo");
        }

        if ((partida.ultimaRonda() == null || partida.ultimaRonda().isCompletada())
                && todoFallo == false) {
            try {
                ultimaRonda = darDeAltaRonda(partida);
            } catch (IndexOutOfBoundsException ex) {
                finalizarPartidaPersonal(partida, usu);
                ultimaRonda = partida.ultimaRonda();
                modelo.getAttribute(ex.getMessage());
            }
        } else {
            ultimaRonda = partida.ultimaRonda();
        }
        ultimaRonda.getRespuestas();

        List<OpcionTituloTmp> opcTitulos;
        List<OpcionInterpreteTmp> opcInterpretes;
        List<OpcionAnyoTmp> opcAnyos;
        if (!todoFallo) {
            opcTitulos = servOpTitulo.findByPartidaRonda(partida.getId(), ultimaRonda.getId());
            opcInterpretes = servOpInterprete.findByPartidaRonda(partida.getId(), ultimaRonda.getId());
            opcAnyos = servOpAnyo.findByPartidaRonda(partida.getId(), ultimaRonda.getId());
        } else {
            opcTitulos = new ArrayList();
            opcInterpretes = new ArrayList();
            opcAnyos = new ArrayList();
        }
        modelo.addAttribute("partidaSesion", partida);
        modelo.addAttribute("pts", partida.ptsUsuario(usu));
        modelo.addAttribute("ronda", ultimaRonda);
        modelo.addAttribute("opcTitulos", opcTitulos);
        modelo.addAttribute("opcInterpretes", opcInterpretes);
        modelo.addAttribute("opcAnyos", opcAnyos);
        if (modelo.getAttribute("mensajeRespuesta") == null) {
            modelo.addAttribute("mensajeRespuesta", "");
        }
        if (modelo.getAttribute("respuestaOK") == null) {
            modelo.addAttribute("respuestaOK", true);
        }
        if (modelo.getAttribute("todoFallo") == null) {
            modelo.addAttribute("todoFallo", false);
        }

        Duration duration = Duration.between(ultimaRonda.getInicio(), LocalTime.now());
        modelo.addAttribute("contador", duration.getSeconds());

        return "PartidaPersonal";
    }

    private void finalizarPartidaPersonal(Partida partida, Usuario usuario) {

        partida.setStatus(StatusPartida.Terminada);
        servPartida.updatePartida(partida.getId(), partida);
        int pts = partida.ptsUsuario(usuario);
        if (partida.getTema() != null && !"".equals(partida.getTema())) {
            Optional<Tema> tema = servTema.findBytema(partida.getTema());
            if (tema.isPresent()) {
                Tema elTema = tema.get();
                if (elTema.getPuntos() < pts) {
                    elTema.setPuntos(pts);
                    elTema.setUsuarioRecord(usuario.getId());
                    servTema.update(elTema.getId(), elTema);
                    usuario.setEstrellas(usuario.getEstrellas() + 1);
                    servUsuario.update(usuario.getId(), usuario);
                }
            }
        }
    }

    @PostMapping("/partidaPersonal")
    public String partidaPersonal(Model modelo,
            @RequestParam("titulo") String titulo,
            @RequestParam("interprete") String interprete,
            @RequestParam("anyo") String anyo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }
        Partida partida = partidaModelo(modelo);
        if (partida == null) {
            return "redirect:/panel";
        }
        ArrayList<String> mensajeRespuesta = new ArrayList();
        boolean respuestaOK = false;
        boolean todoFallo = false;
        try {
            Ronda ronda = partida.ultimaRonda();

            if (ronda.isCompletada()) {
                throw new Exception("La ronda no es correcta");
            }

            Cancion cancion = ronda.getCancion();
            Respuesta resp = new Respuesta();

            int ptsTitulo = 0, ptsInterp = 0;

            resp.setAnyo(Integer.parseInt(anyo));
            int ptsAnyo = Utilidades.calcularPtsPorAnyo(Integer.parseInt(anyo), cancion);
            if (Objects.equals(Integer.valueOf(anyo), cancion.getAnyo())) {
                resp.setAnyoOk(true);
            } else {
                mensajeRespuesta.add("El año correcto era "
                        + String.valueOf(cancion.getAnyo()) + " tu respondiste " + anyo);
            }
            Optional<Cancion> canInt = servCancion.findById(Long.valueOf(interprete));
            if (canInt.isPresent()) {
                resp.setInterprete(canInt.get().getInterprete());
                ptsInterp = Utilidades.calcularPtsPorInterprete(canInt.get().getInterprete(), cancion);
                if (Objects.equals(Long.valueOf(interprete), cancion.getId())) {
                    resp.setInterpreteOk(true);
                } else {
                    mensajeRespuesta.add("El interprete correcto era "
                            + cancion.getInterprete() + " tu respondiste " + canInt.get().getInterprete());
                }
            }
            Optional<Cancion> canTit = servCancion.findById(Long.valueOf(titulo));
            if (canTit.isPresent()) {
                resp.setTitulo(canTit.get().getTitulo());
                ptsTitulo = Utilidades.calcularPtsPorTitulo(canTit.get().getTitulo(), cancion);
                if (Objects.equals(Long.valueOf(titulo), cancion.getId())) {
                    resp.setTituloOk(true);
                } else {
                    mensajeRespuesta.add("El titulo correcto era "
                            + cancion.getTitulo() + " tu respondiste " + canTit.get().getTitulo());
                }
            }
            if (mensajeRespuesta.size() == 3) {
                mensajeRespuesta.add("Lo siento!!!!, se acabo la partida, puedes iniciar otra cuando lo desees");
                todoFallo = true;
            }
            if (mensajeRespuesta.isEmpty()) {
                mensajeRespuesta.add("Fantastico!!!! has acertado todo");
                respuestaOK = true;
            }

            resp.setInicio(LocalTime.now());
            Duration duration = Duration.between(ronda.getInicio(), LocalTime.now());
            Long segundos = duration.getSeconds();
            Long pts = (long) (ptsAnyo + ptsTitulo + ptsInterp);
            if (segundos < pts) {
                pts = pts - segundos;
            } else {
                pts = 0L;
            }
            int ptsFinales = pts.intValue();

            resp.setPuntos(ptsFinales);
            resp.setRonda(ronda);
            resp.setUsuario(usu);
            Respuesta newResp = servRespuesta.saveRespuesta(resp);
            ronda.getRespuestas().add(newResp);

            ronda.setCompletada(true);
            servRonda.updateRonda(ronda.getId(), ronda);

            partida.setRondaActual(partida.getRondaActual() + 1);
            servPartida.updatePartida(partida.getId(), partida);

            // si por ejemplo no se acierta nada, podemos finalizar partida
            if (todoFallo) {
                finalizarPartidaPersonal(partida, usu);
            }

        } catch (Exception ex) {
            Logger.getLogger(ControladorVista.class.getName()).log(Level.SEVERE, null, ex);
        }
        modelo.addAttribute("mensajeRespuesta", mensajeRespuesta);
        modelo.addAttribute("respuestaOK", respuestaOK);
        modelo.addAttribute("todoFallo", todoFallo);
        return "redirect:/partidaPersonal";
    }

    @GetMapping("/altaUsuario")
    public String altaUsuario(Model modelo) {

        modelo.addAttribute("newusuario", new Usuario());
        return "AltaUsuario";
    }

    @GetMapping("/altaUsuarioAdm")
    public String altaUsuarioAdm(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }

        modelo.addAttribute("newusuario", new Usuario());
        return "AltaUsuarioAdm";
    }

    @PostMapping("/altaUsuario")
    public String altaUsuario(@ModelAttribute("newusuario") Usuario usuario,
            @ModelAttribute("pws2") String pws2, Model modelo) {

        String resp = "";
        String err = "";
        boolean passwOK = true;
        boolean usuOK = true;
        try {
            Utilidades.validarPassword(usuario.getContrasenya(), pws2);
        } catch (Exception ex) {
            passwOK = false;
            err = ex.getMessage();
        }

        String usu = usuario.getUsuario().toLowerCase();
        usuario.setUsuario(usu);

        if (passwOK) {
            Optional<Usuario> usuLogin = servUsuario.findByUsuario(usuario.getUsuario());
            if (!usuLogin.isEmpty()) {
                usuOK = false;
                err = "El usuario " + usuario.getUsuario() + " ya existe";
            }
        }

        if (usuOK && passwOK) {

            String passEncrip = passwordEncoder.encode(usuario.getContrasenya());

            Set<UsuarioRol> roles = new HashSet();
            UsuarioRol usurol = new UsuarioRol();
            usurol.setName(Roles.USER);
            roles.add(usurol);

            try {
                usuario.setRoles(roles);
                usuario.setActivo(false);
                usuario.setContrasenya(passEncrip);
                usuario.setPreferencias("");
                servUsuario.save(usuario);
                resp = "Se ha creado el usuario ".concat(usuario.getUsuario());
                String token = passwordEncoder.encode(usuario.getUsuario());
                String enlace = customIp + "/validarUsuario?id=" + String.valueOf(usuario.getId())
                        + "&token=" + token;

                enviarMail(usuario.getUsuario(), "Alta en PlayHitsGame",
                        enlace, "CorreoAlta");
            } catch (Exception ex) {
                err = "ERROR " + ex;
            }
        }

        modelo.addAttribute("result", resp);
        modelo.addAttribute("error", err);
        return "AltaUsuario";
    }

    @PostMapping("/altaUsuarioAdm")
    public String altaUsuarioAdm(@ModelAttribute("newusuario") Usuario usuario,
            @ModelAttribute("pws2") String pws2, Model modelo) {

        String resp = "";
        String err = "";
        boolean passwOK = true;
        boolean usuOK = true;
        try {
            if (!usuario.getContrasenya().equals(pws2)) {
                throw new Exception("Las Contraseñas no son iguales");
            }
        } catch (Exception ex) {
            passwOK = false;
            err = ex.getMessage();
        }

        String usu = usuario.getUsuario().toLowerCase();
        usuario.setUsuario(usu);

        if (passwOK) {
            Optional<Usuario> usuLogin = servUsuario.findByUsuario(usuario.getUsuario());
            if (!usuLogin.isEmpty()) {
                usuOK = false;
                err = "El usuario " + usuario.getUsuario() + " ya existe";
            }
        }

        if (usuOK && passwOK) {

            String passEncrip = passwordEncoder.encode(usuario.getContrasenya());

            Set<UsuarioRol> roles = new HashSet();
            UsuarioRol usurol = new UsuarioRol();
            usurol.setName(Roles.USER);
            roles.add(usurol);

            try {
                usuario.setRoles(roles);
                usuario.setActivo(false);
                usuario.setContrasenya(passEncrip);
                usuario.setPreferencias("");
                usuario.setActivo(true);
                servUsuario.save(usuario);
                resp = "Se ha creado el usuario ".concat(usuario.getUsuario());
            } catch (Exception ex) {
                err = "ERROR " + ex;
            }
        }

        modelo.addAttribute("result", resp);
        modelo.addAttribute("error", err);
        return "AltaUsuarioAdm";
    }

    private void enviarMail(String des, String asunto, String txt, String plantilla) {
        Mail mail = new Mail();
        mail.setAsunto(asunto);
        mail.setDestinatario(des);
        mail.setMensaje(txt);
        mail.setPlantilla(plantilla);
        try {
            servMail.enviarCorreo(mail);
        } catch (MessagingException ex) {
            Logger.getLogger(ControladorVista.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GetMapping("/eliminarUsuario/{id}")
    public String eliminarUsuario(@ModelAttribute("id") Long id, Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }

        try {
            Optional<Usuario> usuDelete = servUsuario.findById(id);
            if (usuDelete.isPresent()) {
                servUsuario.deleteById(id);
            }

        } catch (Exception e) {
            Logger.getLogger(ControladorVista.class.getName()).log(Level.SEVERE, null, e);
        }
        return "redirect:/administracion";
    }

    @GetMapping("/modificarUsuario")
    public String modificarUsuario(Model modelo
    ) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }
        informarUsuarioModelo(modelo, usu);
        return "ModificarUsuario";
    }

    @PostMapping("/modificarUsuario")
    public String modificarUsuario(@ModelAttribute("usuarioSesion") Usuario usuario,
            @ModelAttribute("pws2") String pws2, Model modelo) {

        String resp = "";
        String err = "";

        boolean passwOK = true;
        String newPassw = "";

        if (pws2 != null && !"".equals(pws2)
                && usuario.getContrasenya() != null
                && !"".equals(usuario.getContrasenya())) {
            try {
                Utilidades.validarPassword(usuario.getContrasenya(), pws2);
                newPassw = passwordEncoder.encode(pws2);
            } catch (Exception ex) {
                passwOK = false;
                err = ex.getMessage();
            }
        }
        if (passwOK) {
            try {
                Usuario usuSesion = usuarioModelo(modelo);
                if (!"".equals(newPassw)) {
                    usuSesion.setContrasenya(newPassw);
                }
                usuSesion.setAlias(usuario.getAlias());
                usuSesion.setGrupo(usuario.getGrupo());
                servUsuario.update(usuSesion.getId(), usuSesion);
                resp = "Se han modificado los datos";
            } catch (Exception ex) {
                err = "ERROR " + ex.getMessage();
            }
        }

        modelo.addAttribute("result", resp);
        modelo.addAttribute("error", err);
        return "ModificarUsuario";
    }

    private void anyadirTemas(Model modelo) {

        ArrayList<String> temas = new ArrayList();
        temas.add("");
        for (Tema tema : servTema.findAll()) {
            temas.add(tema.getTema());
        }
        modelo.addAttribute("temas", temas);
    }

    private void crearPartida(Model modelo, Partida newPartida, Usuario usu) {

        Calendar fecha = Calendar.getInstance();
        newPartida.setAnyoInicial(1950);
        newPartida.setAnyoFinal(fecha.get(Calendar.YEAR) - 1);
        newPartida.setGrupo(usu.getGrupo());
        modelo.addAttribute("newpartida", newPartida);
        anyadirTemas(modelo);
        modelo.addAttribute("oprondas", numeroRondas);
        modelo.addAttribute("nronda", 10);
        informarUsuarioModelo(modelo, usu);

    }

    @GetMapping("/crearPartida")
    public String crearPartida(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }

        Partida newPartida = new Partida();
        newPartida.setTipo(TipoPartida.grupo);
        crearPartida(modelo, newPartida, usu);

        ArrayList<Usuario> posiblesInvitados = (ArrayList<Usuario>) usuariosGrupo(usu);

        if (!posiblesInvitados.isEmpty()) {
            modelo.addAttribute("posiblesinvitados", posiblesInvitados);
        } else {
            modelo.addAttribute("posiblesinvitados", null);
        }
        return "CrearPartida";
    }

    @GetMapping("/crearPartidaPersonal")
    public String crearPartidaPersonal(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }

        Partida newPartida = new Partida();
        newPartida.setTipo(TipoPartida.personal);
        crearPartida(modelo, newPartida, usu);
        modelo.addAttribute("posiblesinvitados", null);
        return "CrearPartida";
    }

    private String crearPartidaGrupo(Partida partida,
            Model modelo, HttpServletRequest req, Usuario usu) {

        Calendar cal = Calendar.getInstance();
        int anyoActual = cal.get(Calendar.YEAR);
        int nrondas = Integer.parseInt(req.getParameter("nrondas"));

        try {

            // Validaciones
            if (!usu.sePuedeCrearPartidaMaster()) {
                throw new Exception("Ya tienes una partida Master creada, no se pueden crear mas");
            }
            if (partida.getAnyoFinal() <= partida.getAnyoInicial()) {
                throw new Exception("Las Fechas Iniciales y Finales no son correctas");
            }
            if (partida.getAnyoFinal() > anyoActual) {
                throw new Exception("El año final es erroneo");
            }
            if (partida.getAnyoInicial() < 1950) {
                throw new Exception("El año inicial es erroneo");
            }
            if (nrondas < 10 || nrondas > 30) {
                throw new Exception("Las rondas deben estar entre 10 y 30");
            }

            List<Cancion> canciones = servCancion.obtenerCanciones(partida);

            if (canciones.size() < nrondas) {
                throw new Exception("No hay suficientes canciones, cambia la seleccion");
            }

            partida.setInvitados(new ArrayList());

            ArrayList<Usuario> posiblesInvitados = (ArrayList<Usuario>) modelo.getAttribute("posiblesinvitados");
            if (posiblesInvitados != null) {
                for (Usuario usuarioInv : posiblesInvitados) {

                    String valor = req.getParameter(usuarioInv.nombreId());
                    if ("on".equals(valor)) {

                        Optional<Usuario> usuario = servUsuario.findById(usuarioInv.getId());
                        if (!usuario.isEmpty()) {
                            usuario.get().getPartidasInvitado().add(partida);
                            partida.getInvitados().add(usuario.get());
                        }
                    }
                }
            }

            partida.setNCanciones(canciones.size());
            partida.setMaster(usu);
            partida.setRondaActual(1);
            partida.setGrupo(usu.getGrupo());
            Partida newPartida = servPartida.savePartida(partida);
            modelo.addAttribute("id_partidaSesion", partida.getId());
            usu.getPartidasMaster().add(newPartida);

            for (Usuario usuPartida : partida.getInvitados()) {
                servUsuario.update(usuPartida.getId(), usuPartida);
            }

            //crear las rondas con nrondas
            partida.setRondas(new ArrayList());
            for (int i = 1; i <= nrondas; i++) {
                Ronda newRonda = new Ronda();
                newRonda.setNumero(i);
                newRonda.setPartida(partida);
                newRonda.setRespuestas(new ArrayList());
                Ronda ronda = servRonda.saveRonda(newRonda);
                //Crear las respuestas
                for (Usuario usuario : partida.usuariosPartida()) {
                    usuario.setRespuestas(new ArrayList());
                    Respuesta newResp = new Respuesta();
                    newResp.setRonda(ronda);
                    newResp.setUsuario(usuario);
                    servRespuesta.saveRespuesta(newResp);
                }

                servRonda.updateRonda(ronda.getId(), ronda);
                partida.getRondas().add(ronda);

            }
            asignarCancionesAleatorias(partida, canciones);
            for (Ronda ronda : partida.getRondas()) {
                servRonda.updateRonda(ronda.getId(), ronda);
            }
            partida.setStatus(StatusPartida.EnCurso);
            servPartida.updatePartida(partida.getId(), partida);

            // Crear las opciones para las respuestas
            for (Ronda ronda : partida.getRondas()) {
                for (OpcionTituloTmp op : opcionesTitulosCanciones(ronda)) {
                    servOpTitulo.saveOpcionTituloTmp(op);
                }
                for (OpcionInterpreteTmp op : opcionesInterpretesCanciones(ronda)) {
                    servOpInterprete.saveOpcionInterpreteTmp(op);
                }
            }

        } catch (Exception ex) {
            String resp = "ERROR " + ex.getMessage();
            modelo.addAttribute("result", resp);
            anyadirTemas(modelo);
            informarUsuarioModelo(modelo, usu);
            modelo.addAttribute("oprondas", numeroRondas);
            modelo.addAttribute("nronda", nrondas);
            return "CrearPartida";
        }

        return "redirect:/partidaMaster";

    }

    private String crearPartidaPersonal(Partida partida,
            Model modelo, HttpServletRequest req, Usuario usu) {

        Calendar cal = Calendar.getInstance();
        int anyoActual = cal.get(Calendar.YEAR);

        try {

            // Validaciones
            if (!usu.sePuedeCrearPartidaMaster()) {
                throw new Exception("Ya tienes una partida creada, no se pueden crear mas");
            }
            if (partida.getAnyoFinal() <= partida.getAnyoInicial()) {
                throw new Exception("Las Fechas Iniciales y Finales no son correctas");
            }
            if (partida.getAnyoFinal() > anyoActual) {
                throw new Exception("El año final es erroneo");
            }
            if (partida.getAnyoInicial() < 1950) {
                throw new Exception("El año inicial es erroneo");
            }
            if ((partida.getAnyoFinal() - partida.getAnyoInicial()) < 5) {
                throw new Exception("El peridodo de años, debe ser de al menos 5 años");
            }

            FiltroCanciones filtro = new FiltroCanciones();
            filtro.setAnyoInicial(partida.getAnyoInicial());
            filtro.setAnyoFinal(partida.getAnyoFinal());
            filtro.setTema(partida.getTema());
            List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);
            if (canciones.size() <= 5) {
                throw new Exception("No hay suficientes canciones para iniciar la partida");
            }

            partida.setInvitados(new ArrayList());
            partida.setMaster(usu);
            partida.setRondaActual(1);
            partida.setStatus(StatusPartida.EnCurso);
            Partida newPartida = servPartida.savePartida(partida);
            modelo.addAttribute("id_partidaSesion", partida.getId());
            usu.getPartidasMaster().add(newPartida);
            servUsuario.update(usu.getId(), usu);
        } catch (Exception ex) {
            String resp = "ERROR " + ex.getMessage();
            modelo.addAttribute("result", resp);
            anyadirTemas(modelo);
            informarUsuarioModelo(modelo, usu);
            return "CrearPartida";
        }

        return "redirect:/partidaPersonal";
    }

    @PostMapping("/crearPartida")
    public String crearPartida(@ModelAttribute("newpartida") Partida partida,
            Model modelo,
            HttpServletRequest req
    ) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }
        if (partida.isTipoGrupo()) {
            return crearPartidaGrupo(partida, modelo, req, usu);
        }
        if (partida.isTipoPersonal()) {
            modelo.addAttribute("mensajeRespuesta", "");
            modelo.addAttribute("respuestaOK", true);
            modelo.addAttribute("todoFallo", false);
            return crearPartidaPersonal(partida, modelo, req, usu);
        }
        modelo.addAttribute("result", "Error en el tipo de partida");
        return "CrearPartida";

    }

    private void resultadosPartida(Partida partidaSesion, Model modelo) {

        HashMap<String, List<Respuesta>> resultadosPartida = new HashMap();
        HashMap<String, Integer> totales = new HashMap();
        String nomUsu;
        ArrayList lista;

        for (Ronda ronda : partidaSesion.getRondas()) {
            for (Respuesta respuesta : ronda.getRespuestas()) {
                nomUsu = respuesta.getUsuario().getNombre();
                if (!resultadosPartida.containsKey(nomUsu)) {
                    resultadosPartida.put(nomUsu, new ArrayList());
                }
                lista = (ArrayList) resultadosPartida.get(nomUsu);
                lista.add(respuesta);
                resultadosPartida.put(nomUsu, lista);
            }
        }
        //Crear la suma total
        for (String usu : resultadosPartida.keySet()) {
            int total = 0;
            for (Respuesta resp : resultadosPartida.get(usu)) {
                total = total + resp.getPuntos();
            }
            totales.put(usu, total);
        }

        modelo.addAttribute("ptstotales", totales);
        modelo.addAttribute("resultados", resultadosPartida);
    }

    @GetMapping("/partidaConsultaGrupo/{id}")
    public String partidaConsultaGrupo(@PathVariable Long id, Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }

        Partida partidaSesion = null;
        try {
            Optional<Partida> partida = servPartida.findById(id);
            partidaSesion = partida.get();
        } catch (Exception e) {
        }

        if (partidaSesion == null) {
            return "redirect:/panel";
        }
        resultadosPartida(partidaSesion, modelo);
        informarPartidaModelo(modelo, partidaSesion);

        return "ResultadosPartida";
    }

    @GetMapping("/partidaConsultaPersonal/{id}")
    public String partidaConsultaPersonal(@PathVariable Long id, Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }
        informarUsuarioModelo(modelo, usu);
        Partida partidaSesion = null;
        try {
            Optional<Partida> partida = servPartida.findById(id);
            partidaSesion = partida.get();
        } catch (Exception e) {
        }

        if (partidaSesion == null) {
            return "redirect:/panel";
        }
        informarPartidaModelo(modelo, partidaSesion);
        modelo.addAttribute("partidaSesion", partidaSesion);
        modelo.addAttribute("pts", partidaSesion.ptsUsuario(partidaSesion.getMaster()));

        return "PartidaPersonalConsulta";
    }

    private List<Usuario> usuariosGrupo(Usuario usu) {

        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) servUsuario.usuariosGrupo(usu.getGrupo());
        ArrayList<Usuario> invitados = new ArrayList();

        // Nos eleiminamos a nosotros mismos y ponemos el resto en seleccionado por defecto
        for (Usuario elem : usuarios) {
            if (!Objects.equals(elem.getId(), usu.getId())) {
                invitados.add(elem);
            }
        }
        return invitados;
    }

    @GetMapping("/administracion")
    public String administracion(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }

        FiltroUsuarios filtro;
        if (modelo.getAttribute("filtroUsuarios") == null) {
            filtro = new FiltroUsuarios();
            modelo.addAttribute("filtroUsuarios", filtro);
        } else {
            filtro = (FiltroUsuarios) modelo.getAttribute("filtroUsuarios");
        }

        List<Usuario> usuarios = servUsuario.findByFiltroBasico(filtro);
        modelo.addAttribute("usuarios", usuarios);

        return "Administracion";
    }

    @PostMapping("/administracion")
    public String administracion(Model modelo, @ModelAttribute("filtroUsuarios") FiltroUsuarios filtro) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }

        List<Usuario> usuarios = servUsuario.findByFiltroBasico(filtro);
        modelo.addAttribute("usuarios", usuarios);

        return "Administracion";
    }

    @PostMapping("/accionesUsuarios")
    public String accionesUsuarios(Model modelo, HttpServletRequest req) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }

        String activo = req.getParameter("activo");
        boolean isActivo = false;
        if ("on".equals(activo)) {
            isActivo = true;
        }

        Set<UsuarioRol> roles = new HashSet();
        UsuarioRol usurol;

        String user = req.getParameter("user");
        if ("on".equals(user)) {
            usurol = new UsuarioRol();
            usurol.setName(Roles.USER);
            roles.add(usurol);
        }

        String admin = req.getParameter("admin");
        if ("on".equals(admin)) {
            usurol = new UsuarioRol();
            usurol.setName(Roles.ADMIN);
            roles.add(usurol);
        }

        String colaborador = req.getParameter("colaborador");
        if ("on".equals(colaborador)) {
            usurol = new UsuarioRol();
            usurol.setName(Roles.COLABORADOR);
            roles.add(usurol);
        }

        FiltroUsuarios filtro = (FiltroUsuarios) modelo.getAttribute("filtroUsuarios");
        List<Usuario> usuarios = servUsuario.findByFiltroBasico(filtro);
        for (Usuario usuario : usuarios) {
            if (req.getParameter(usuario.selId()) != null) {
                if ("on".equals(req.getParameter(usuario.selId()))) {
                    for (UsuarioRol usuRol : usuario.getRoles()) {
                        servRolUsuario.deleteById(usuRol.getId());
                    }
                    usuario.setActivo(isActivo);
                    usuario.setRoles(roles);
                    for (UsuarioRol usuRol : usuario.getRoles()) {
                        servRolUsuario.save(usuRol);
                    }
                    servUsuario.update(usuario.getId(), usuario);
                }
            }
        }
        return "redirect:/administracion";
    }

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
                return "redirect:/";
            } else {
                return "error";
            }
        } else {
            return "error";
        }
    }

    @GetMapping("/recuperarContrasenya")
    public String recuperarContrasenya(Model modelo) {

        return "PasswordOlvidado";

    }

    @PostMapping("/codigoRecuperacionPassw")
    public String codigoRecuperacionPassw(@ModelAttribute("mail") String mail, Model modelo) {

        String usu = mail.toLowerCase();
        Optional<Usuario> usuario = servUsuario.findByUsuario(usu);

        if (usuario.isEmpty()) {
            modelo.addAttribute("error", "Este usuario no existe");
            return "PasswordOlvidado";
        } else {
            String token = usuario.get().getContrasenya();
            String enlace = "El codigo de recuperacion para el cambio de contraseña es : " + token;

            enviarMail(usuario.get().getUsuario(), "Recuperación de Contraseña PlayHitsGame",
                    enlace, "Correo");

            modelo.addAttribute("result", "Se ha enviado un codigo de recuperacion a tu cuenta de correo");
            modelo.addAttribute("mail", mail);
        }
        return "PasswordOlvidado";
    }

    @PostMapping("/recuperarContrasenya")
    public String recuperarContrasenya(
            @ModelAttribute("usuario") String usuario,
            @ModelAttribute("token") String token,
            @ModelAttribute("pws1") String pws1,
            @ModelAttribute("pws2") String pws2,
            Model modelo) {

        String usu = null;
        if (usuario != null) {
            usu = usuario.toLowerCase();
        }
        Optional<Usuario> usuarioCambio = servUsuario.findByUsuario(usu);
        if (usuarioCambio.isEmpty()) {
            modelo.addAttribute("error", "Este usuario no existe");
            return "PasswordOlvidado";
        }
        Usuario usuCambio = usuarioCambio.get();
        if (!token.equals(usuCambio.getContrasenya())) {
            modelo.addAttribute("error", "El codigo de recuperación no es correto");
            return "PasswordOlvidado";
        } else {
            boolean passwOK = true;
            String newPassw = "";
            String err = null;
            String resp = null;

            try {
                Utilidades.validarPassword(pws1, pws2);
                newPassw = passwordEncoder.encode(pws2);
            } catch (Exception ex) {
                passwOK = false;
                err = ex.getMessage();
            }

            if (passwOK) {
                try {
                    if (!"".equals(newPassw)) {
                        usuCambio.setContrasenya(newPassw);
                    }
                    servUsuario.update(usuCambio.getId(), usuCambio);
                    resp = "Se ha modificado la contraseña correctamente";
                } catch (Exception ex) {
                    err = "ERROR " + ex.getMessage();
                }
            }
            modelo.addAttribute("error", err);
            modelo.addAttribute("result", resp);
        }

        return "PasswordOlvidado";
    }

    @GetMapping("/records")
    public String records(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/";
        }
        ArrayList<Tema> temas = (ArrayList<Tema>) servTema.findAll();

        ArrayList<Record> records = new ArrayList();

        for (Tema tema : temas) {

            Record newObj = new Record();
            newObj.setTema(tema.getTema());
            newObj.setDescripcion(tema.getDescripcion());
            newObj.setGenero(tema.getGenero());
            newObj.setIdioma(tema.getIdioma());
            newObj.setCanciones(tema.getCanciones().size());
            newObj.setPuntos(tema.getPuntos());
            String usuRecord = "";
            if (tema.getUsuarioRecord() != null) {
                Optional<Usuario> usuario = servUsuario.findById(tema.getUsuarioRecord());
                if (usuario.isPresent()) {
                    usuRecord = usuario.get().getNombre();
                }
            }
            newObj.setUsuarioRecord(usuRecord);
            records.add(newObj);
        }

        modelo.addAttribute("temas", records);

        return "Records";
    }

}
