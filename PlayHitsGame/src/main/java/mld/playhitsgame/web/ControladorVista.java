/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.correo.EmailServicioMetodos;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Config;
import mld.playhitsgame.exemplars.Dificultad;
import mld.playhitsgame.exemplars.FiltroCanciones;
import mld.playhitsgame.exemplars.FiltroTemas;
import mld.playhitsgame.exemplars.FiltroUsuarios;
import mld.playhitsgame.exemplars.Genero;
import mld.playhitsgame.exemplars.Idioma;
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
import mld.playhitsgame.exemplars.Puntuacion;
import mld.playhitsgame.exemplars.PuntuacionTMP;
import mld.playhitsgame.exemplars.Registro;
import mld.playhitsgame.exemplars.TipoPartida;
import mld.playhitsgame.exemplars.TipoRegistro;
import mld.playhitsgame.seguridad.Roles;
import mld.playhitsgame.seguridad.UsuarioRol;
import mld.playhitsgame.services.ConfigServicioMetodos;
import mld.playhitsgame.services.OpcionAnyoTmpServicioMetodos;
import mld.playhitsgame.services.OpcionInterpreteTmpServicioMetodos;
import mld.playhitsgame.services.OpcionTituloTmpServicioMetodos;
import mld.playhitsgame.services.PuntuacionServicioMetodos;
import mld.playhitsgame.services.RegistroServicioMetodos;
import mld.playhitsgame.services.UsuarioRolServicioMetodos;
import mld.playhitsgame.utilidades.Utilidades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
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
    "rol", "filtroUsuarios", "mensajeRespuesta", "respuestaOK", "todoFallo", "esRecord",
    "soundOK", "soundErrTitulo", "soundErrInterp", "soundErrAnyo", "soundEliminado",
    "partidaInvitado", "cancionInvitado", "spotifyimagenTmp", "locale"})
@Slf4j
public class ControladorVista {

    @Value("${custom.server.websocket}")
    private String serverWebsocket;

    @Value("${custom.server.ip}")
    private String customIp;

    @Value("${custom.invitados}")
    private String invitadosON;

    @Value("${custom.mailadmin}")
    private String mailAdmin;

    private final int[] NUMERO_RONDAS = {10, 15, 20, 25, 30};
    private final int SEG_PARA_INICIO_RESPUESTA = 15;
    private final int REG_POR_PAG = 100;
    public int numMaxEstrellas;

    private String urlLoginSpotify;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    EmailServicioMetodos servEmail;

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
    PuntuacionServicioMetodos sevrPuntuacion;
    @Autowired
    OpcionTituloTmpServicioMetodos servOpTitulo;
    @Autowired
    OpcionInterpreteTmpServicioMetodos servOpInterprete;
    @Autowired
    OpcionAnyoTmpServicioMetodos servOpAnyo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ConfigServicioMetodos servConfig;
    @Autowired
    RegistroServicioMetodos servRegistro;

    @PostConstruct
    public void init() {
        // Código a ejecutar al arrancar la aplicación
        System.out.println("Aplicación iniciada (PostConstruct). Ejecutando tareas de inicio...");

        String ipRouterConfigurada = servConfig.getSettings().getIpRouter();
        System.out.print("IP Router Configurada : " + ipRouterConfigurada);

        String ipRouterActual = Utilidades.ejecutarComando("curl ifconfig.me");
        System.out.print("IP Router Actual : " + ipRouterActual);

        Config laConfig = servConfig.getSettings();
        if (!ipRouterConfigurada.equals(ipRouterActual)) {
            laConfig.setIpRouter(ipRouterActual);
            servConfig.saveSettings(laConfig);
            Utilidades.enviarMail(servEmail, mailAdmin, "", "Cambio de IP Router",
                    "Se modifica la IP de " + ipRouterConfigurada + " a " + ipRouterActual, "Correo");

        }

        // para que este valor se refresque si se cambia en la BD
        // habria que reiniciar la APP
        numMaxEstrellas = laConfig.getNumMaxEstrellas();

    }

    private Locale idioma(Model modelo) {

        Locale idioma = (Locale) modelo.getAttribute("locale");

        if (idioma == null) {
            idioma = Locale.getDefault();
        }

        return idioma;
    }

    private void asignarIdiomaUsuario(Model modelo, Usuario usuario) {

        Locale localeUsu = null;

        if (usuario.getIdioma() != null) {
            if (usuario.getIdioma().equals(Idioma.English)) {
                localeUsu = new Locale("en");
            } else if (usuario.getIdioma().equals(Idioma.Spanish)) {
                localeUsu = new Locale("es");
            }
        }
        if (localeUsu != null) {
            modelo.addAttribute("locale", localeUsu);
        }

    }

    private String mensaje(Model modelo, String idMensaje) {

        return messageSource.getMessage(idMensaje, null, idioma(modelo));
    }

    private String mensaje(Model modelo, String idMensaje, String arg) {

        Object[] lista = new Object[]{arg};
        return messageSource.getMessage(idMensaje, lista, idioma(modelo));
    }

    private String urlSpotify() {

        if (urlLoginSpotify != null) {
            return urlLoginSpotify;
        }

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
            urlLoginSpotify = urlLogin;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ControladorSpotify.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControladorSpotify.class.getName()).log(Level.SEVERE, null, ex);
        }

        return urlLogin;
    }

    private Usuario usuarioModelo(Model modelo) {

        Long id_usu;
        Usuario usuarioSesion = null;
        try {
            id_usu = (Long) modelo.getAttribute("id_usuarioSesion");
            usuarioSesion = servUsuario.findById(id_usu).get();
        } catch (Exception ex) {
        }

        return usuarioSesion;
    }

    private void informarUsuarioModelo(Model modelo, Usuario usuario) {

        modelo.addAttribute("usuarioSesion", usuario);
    }

    private Partida partidaModelo(Model modelo) {

        Partida partidaSesion = null;
        Object id = modelo.getAttribute("id_partidaSesion");
        Long id_part = null;
        if (id != null && id != "") {
            id_part = (Long) id;
        }

        if (id_part != null) {
            try {
                partidaSesion = servPartida.findById(id_part).get();
            } catch (Exception ex) {
            }
        }

        return partidaSesion;
    }

    private void informarPartidaModelo(Model modelo, Partida partida) {

        modelo.addAttribute("id_partidaSesion", partida.getId());
    }

    private String ipCliente(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    @GetMapping("/")
    public String inicio(Model modelo, HttpServletRequest request, Locale locale) {
        servRegistro.registrar(TipoRegistro.Visita, ipCliente(request));
        modelo.addAttribute("invitadosON", invitadosON);
        modelo.addAttribute("locale", locale);
        try {
            Config config = servConfig.getSettings();

            if (config.isMantenimiento()) {
                LocalDateTime fechaMant = config.getFechaMantenimiento();
                LocalDateTime fechaActu = LocalDateTime.now();
                Duration duracion = Duration.between(fechaActu, fechaMant);
                long dias = duracion.toDays();
                long horas = duracion.toHours() % 24;
                long minutos = duracion.toMinutes() % 60;

                String mensaje = mensaje(modelo, "general.mantenimiento");
                if (dias > 0) {
                    mensaje = mensaje.concat(String.valueOf(dias) + " " + mensaje(modelo, "general.dias"));
                }
                if (horas > 0) {
                    mensaje = mensaje.concat(String.valueOf(horas) + " " + mensaje(modelo, "general.horas"));
                }
                if (minutos > 0) {
                    mensaje = mensaje.concat(String.valueOf(minutos) + " " + mensaje(modelo, "general.minutos"));
                }
                modelo.addAttribute("mensajeInicio", mensaje);
            } else if (config.getMensajeInicio_es() != null
                    && !"".equals(config.getMensajeInicio_es())) {
                String mensaje = null;
                if ("es".equals(locale.getLanguage())) {
                    mensaje = config.getMensajeInicio_es();
                }
                if ("en".equals(locale.getLanguage())) {
                    mensaje = config.getMensajeInicio_en();
                }
                modelo.addAttribute("mensajeInicio", mensaje);
            } else {
                String mensaje = mensaje(modelo, "general.ncancionesbd", config.getNCanciones());
                modelo.addAttribute("mensajeInicio", mensaje);
            }

        } catch (Exception ex) {
            Logger.getLogger(ControladorVista.class.getName()).log(
                    Level.WARNING, null, ex);
        }

        return "Inicio";
    }

    @GetMapping("/logout")
    public String logout(Model modelo) {
        modelo.addAttribute("invitadosON", invitadosON);
        modelo.addAttribute("id_usuarioSesion", "");
        modelo.addAttribute("id_partidaSesion", "");
        modelo.addAttribute("rol", "");

        modelo.addAttribute("soundOK", "");
        modelo.addAttribute("soundErrTitulo", "");
        modelo.addAttribute("soundErrInterp", "");
        modelo.addAttribute("soundErrAnyo", "");
        modelo.addAttribute("soundEliminado", "");

        return "redirect:/";
    }

    @GetMapping("/accesoInvitado")
    public String accesoInvitado(Model modelo) {

        modelo.addAttribute("rol", Rol.playhitsgame);
        modelo.addAttribute("spotifyimagenTmp", "");
        return "PanelInvitado";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("elUsuario") String elUsuario,
            @ModelAttribute("laContrasenya") String laContrasenya, Model modelo, HttpServletRequest request) {

        //String passEncrip = passwordEncoder.encode(laContrasenya);       
        //Optional<Usuario> usuLogin = servUsuario.usuarioLogin(elUsuario, passEncrip);
        String usu = elUsuario.toLowerCase();
        Optional<Usuario> usuLogin = servUsuario.findByUsuario(usu);

        if (usuLogin.isEmpty()) {
            modelo.addAttribute("error", mensaje(modelo, "general.usuarioincorrecto"));
            return "Inicio";
        } else {
            Usuario usuarioSesion = usuLogin.get();

            boolean ok = passwordEncoder.matches(laContrasenya, usuarioSesion.getContrasenya());

            if (!ok) {
                modelo.addAttribute("error", mensaje(modelo, "general.usuarioincorrecto"));
                return "Inicio";
            }

            if (!usuarioSesion.isActivo()) {
                modelo.addAttribute("error", mensaje(modelo, "general.activarcuenta"));
                return "Inicio";
            }
            usuarioSesion.getPartidasInvitado();
            usuarioSesion.getPartidasMaster();
            if (usuarioSesion.isAdmin()) {
                modelo.addAttribute("urlSpotify", urlSpotify());
            }
            modelo.addAttribute("id_usuarioSesion", usuarioSesion.getId());
            informarUsuarioModelo(modelo, usuarioSesion);
            if (!usuarioSesion.isAdmin()) {
                servRegistro.registrar(TipoRegistro.Login, ipCliente(request), usuarioSesion.getUsuario());
            }

            asignarIdiomaUsuario(modelo, usuarioSesion);

            return "redirect:/panel";
        }
    }

    @GetMapping("/panel")
    public String panel(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }

        if (usu.isAdmin()) {
            modelo.addAttribute("urlSpotify", urlSpotify());
        }
        informarUsuarioModelo(modelo, usu);

        List<Partida> batallas = servPartida.partidasBatallaCreadas();
        List<Partida> batallasDisponibles = new ArrayList<>();
        List<Partida> batallasInscritas = new ArrayList<>();

        for (Partida partida : batallas) {
            if (partida.getInvitados().contains(usu)) {
                batallasInscritas.add(partida);
            } else {
                batallasDisponibles.add(partida);
            }
        }

        modelo.addAttribute("serverWebsocket", this.serverWebsocket);
        modelo.addAttribute("spotifyimagenTmp", "");
        modelo.addAttribute("batallasDisponibles", batallasDisponibles);
        modelo.addAttribute("batallasInscritas", batallasInscritas);

        return "Panel";
    }

    @GetMapping("/partidaMaster")
    public String partidaMaster(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }
        modelo.addAttribute("rol", Rol.master);
        Partida partida = usu.partidaMasterEnCurso();

        return partidaGrupo(modelo, partida);
    }

    @GetMapping("/partidaInvitado/{id}")
    public String partidaInvitado(Model modelo, @PathVariable Long id) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
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
            return "redirect:/logout";
        }
        informarPartidaModelo(modelo, partida);
        if (partida == null) {
            return "redirect:/panel";
        }
        informarUsuarioModelo(modelo, usu);
        Ronda rondaActual = partida.getRondas().get(partida.getRondaActual() - 1);
        List<OpcionAnyoTmp> opcAnyos
                = servOpAnyo.findByPartidaRonda(partida.getId(), rondaActual.getId());
        List<OpcionTituloTmp> opcTitulos
                = servOpTitulo.findByPartidaRonda(partida.getId(), rondaActual.getId());
        List<OpcionInterpreteTmp> opcInterpretes
                = servOpInterprete.findByPartidaRonda(partida.getId(), rondaActual.getId());
        List<PtsUsuario> ptsUsuarios = new ArrayList();
        for (Usuario usuPartida : partida.usuariosPartida()) {
            PtsUsuario ptsUsuario = new PtsUsuario();
            ptsUsuario.setUsuario(usuPartida);
            ptsUsuario.setPts(usuPartida.getPuntosPartida(partida));
            ptsUsuario.setPuntos(String.valueOf(usuPartida.getPuntosPartida(partida)));
            ptsUsuarios.add(ptsUsuario);
        }
        Collections.sort(ptsUsuarios);

        modelo.addAttribute("serverWebsocket", this.serverWebsocket);
        modelo.addAttribute("opcAnyos", opcAnyos);
        modelo.addAttribute("opcTitulos", opcTitulos);
        modelo.addAttribute("opcInterpretes", opcInterpretes);
        modelo.addAttribute("id_partidaSesion", partida.getId());
        modelo.addAttribute("partidaSesion", partida);
        modelo.addAttribute("respuestas", partida.respuestasUsuario(usu));
        modelo.addAttribute("ptsUsuario", ptsUsuarios);
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
                throw new IndexOutOfBoundsException("general.nomascanciones");
            }

        }
        newRonda.setCancion(cancionSel);
        newRonda.setRespuestas(new ArrayList());
        newRonda.setInicio(LocalTime.now());
        Ronda ronda = servRonda.saveRonda(newRonda);

        // Crear las opciones para las respuestas
        for (OpcionTituloTmp op : opcionesTitulosCanciones(ronda, canciones, !partida.isSinOfuscar())) {
            servOpTitulo.saveOpcionTituloTmp(op);
        }
        for (OpcionInterpreteTmp op : opcionesInterpretesCanciones(ronda, canciones, !partida.isSinOfuscar())) {
            servOpInterprete.saveOpcionInterpreteTmp(op);
        }
        int[] rangoAnyos = rangoAnyosCanciones((ArrayList<Cancion>) canciones);
        for (OpcionAnyoTmp op : opcionesAnyosCanciones(ronda, rangoAnyos[0], rangoAnyos[1])) {
            servOpAnyo.saveOpcionAnyoTmp(op);
        }

        return ronda;
    }

    @GetMapping("/partidaPersonal")
    public String partidaPersonal(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
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
        if (modelo.getAttribute("esRecord") != null) {
            todoFallo = (boolean) modelo.getAttribute("esRecord");
        }

        if ((partida.ultimaRonda() == null || partida.ultimaRonda().isCompletada())
                && !partida.isTerminada() && todoFallo == false) {
            try {
                ultimaRonda = darDeAltaRonda(partida);
            } catch (IndexOutOfBoundsException ex) {
                boolean esRecord = UtilidadesWeb.finalizarPartidaPersonal(partida, usu, this);
                ultimaRonda = partida.ultimaRonda();
                todoFallo = true;
                modelo.addAttribute("esRecord", esRecord);
                modelo.addAttribute("mensajeRespuesta", mensaje(modelo, ex.getMessage())
                        + mensaje(modelo, "general.iniciarotrapartida"));
                modelo.addAttribute("todoFallo", true);
            }
        } else {
            ultimaRonda = partida.ultimaRonda();
        }
        if (ultimaRonda == null) {
            System.out.println("MLD Verificar partida sospechosa " + partida.getId().toString());
            return "redirect:/panel";
        }

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
        if (modelo.getAttribute("esRecord") == null) {
            modelo.addAttribute("esRecord", false);
        }

        long seconds = 0;
        if (ultimaRonda.getInicio() != null) {
            Duration duration = Duration.between(ultimaRonda.getInicio(), LocalTime.now());
            seconds = duration.getSeconds();
        }
        modelo.addAttribute("contador", seconds - SEG_PARA_INICIO_RESPUESTA);

        return "PartidaPersonal";
    }

    @PostMapping("/acabarPartidaPersonal")
    public String acabarPartidaPersonal(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }
        Partida partida = partidaModelo(modelo);
        if (partida == null) {
            return "redirect:/panel";
        }

        UtilidadesWeb.finalizarPartidaPersonal(partida, usu, this);

        return "redirect:/panel";

    }

    @PostMapping("/partidaPersonal")
    public String partidaPersonal(Model modelo,
            @RequestParam("titulo") String titulo,
            @RequestParam("interprete") String interprete,
            @RequestParam("anyo") String anyo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }
        Partida partida = partidaModelo(modelo);
        if (partida == null) {
            return "redirect:/panel";
        }
        ArrayList<String> mensajeRespuesta = new ArrayList();
        boolean respuestaOK = false;
        int fallos = 0;
        boolean hasPerdido = false;
        boolean soundOK = false;
        boolean soundEliminado = false;
        boolean soundErrTitulo = false;
        boolean soundErrInterp = false;
        boolean soundErrAnyo = false;

        try {
            Ronda ronda = partida.ultimaRonda();

            if (ronda.isCompletada()) {
                throw new Exception(mensaje(modelo, "general.rondaincorrecta"));
            }

            Cancion cancion = ronda.getCancion();
            Respuesta resp = new Respuesta();

            int ptsTitulo = 0, ptsInterp = 0;

            resp.setAnyo(Integer.parseInt(anyo));
            int ptsAnyo = Utilidades.calcularPtsPorAnyo(
                    Integer.parseInt(anyo), cancion, partida.getDificultad());
            if (ptsAnyo > 0) {
                resp.setAnyoOk(true);
            } else {
                mensajeRespuesta.add(mensaje(modelo, "general.anyocorrecto")
                        + String.valueOf(cancion.getAnyo()) + " " + mensaje(modelo, "general.turespondiste") + anyo);
                fallos = fallos + 1;
                soundErrAnyo = true;
            }
            Optional<Cancion> canTit = servCancion.findById(Long.valueOf(titulo));
            if (canTit.isPresent()) {
                resp.setTitulo(canTit.get().getTitulo());
                ptsTitulo = Utilidades.calcularPtsPorTitulo(
                        canTit.get().getTitulo(), cancion, partida.getDificultad(), partida.isSinOfuscar());
                if (ptsTitulo > 0) {
                    resp.setTituloOk(true);
                } else {
                    mensajeRespuesta.add(mensaje(modelo, "general.titulocorrecto")
                            + cancion.getTitulo() + " " + mensaje(modelo, "general.turespondiste") + canTit.get().getTitulo());
                    fallos = fallos + 1;
                    soundErrTitulo = true;
                }
            }
            Optional<Cancion> canInt = servCancion.findById(Long.valueOf(interprete));
            if (canInt.isPresent()) {
                resp.setInterprete(canInt.get().getInterprete());
                ptsInterp = Utilidades.calcularPtsPorInterprete(
                        canInt.get().getInterprete(), cancion, partida.getDificultad(), partida.isSinOfuscar());
                if (ptsInterp > 0) {
                    resp.setInterpreteOk(true);
                } else {
                    mensajeRespuesta.add(mensaje(modelo, "general.intercorrecto")
                            + cancion.getInterprete() + " " + mensaje(modelo, "general.turespondiste") + canInt.get().getInterprete());
                    fallos = fallos + 1;
                    soundErrInterp = true;
                }
            }
            if ((partida.getDificultad().equals(Dificultad.Dificil) && fallos > 0)
                    || (partida.getDificultad().equals(Dificultad.Normal) && fallos > 1)
                    || (partida.getDificultad().equals(Dificultad.Facil) && fallos > 2)) {
                mensajeRespuesta.add(mensaje(modelo, "general.seacabo"));
                hasPerdido = true;
                soundEliminado = true;
            }
            if (fallos == 0) {
                mensajeRespuesta.add(mensaje(modelo, "general.todoacertado"));
                respuestaOK = true;
                soundOK = true;
            }

            LocalTime actual = LocalTime.now();
            LocalTime sinComienzo = actual.minusSeconds(SEG_PARA_INICIO_RESPUESTA);

            if (ronda.getInicio() != null && ronda.getInicio().isAfter(sinComienzo)) {
                resp.setInicio(ronda.getInicio());
            } else {
                resp.setInicio(sinComienzo);
            }
            Duration duration = Duration.between(ronda.getInicio(), resp.getInicio());
            Long segundos = duration.getSeconds();
            Long pts = (long) (ptsAnyo + ptsTitulo + ptsInterp);
            if (segundos >= 0 && segundos < pts) {
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
            if (hasPerdido) {
                boolean esRecord = UtilidadesWeb.finalizarPartidaPersonal(partida, usu, this);
                modelo.addAttribute("esRecord", esRecord);
            }

            modelo.addAttribute("spotifyimagenTmp", cancion.getSpotifyimagen());

        } catch (Exception ex) {
            Logger.getLogger(ControladorVista.class.getName()).log(Level.SEVERE, null, ex);
        }
        modelo.addAttribute("mensajeRespuesta", mensajeRespuesta);
        modelo.addAttribute("respuestaOK", respuestaOK);
        modelo.addAttribute("todoFallo", hasPerdido);
        if (partida.isSonidos()) {
            if (soundOK) {
                modelo.addAttribute("soundOK", "Aplausos.mp3");
            } else {
                modelo.addAttribute("soundOK", "");
            }
            if (soundErrTitulo) {
                modelo.addAttribute("soundErrTitulo", "ErrorTitulo.mp3");
            } else {
                modelo.addAttribute("soundErrTitulo", "");
            }
            if (soundErrInterp) {
                modelo.addAttribute("soundErrInterp", "ErrorInterprete.mp3");
            } else {
                modelo.addAttribute("soundErrInterp", "");
            }
            if (soundErrAnyo) {
                modelo.addAttribute("soundErrAnyo", "ErrorAnyo.mp3");
            } else {
                modelo.addAttribute("soundErrAnyo", "");
            }
            if (soundEliminado) {
                modelo.addAttribute("soundEliminado", "Eliminado.mp3");
            } else {
                modelo.addAttribute("soundEliminado", "");
            }
        }

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
            return "redirect:/logout";
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
                err = mensaje(modelo, "general.elusu") + usuario.getUsuario() + " " + mensaje(modelo, "general.yaexiste");
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
                usuario.setSegEspera(5);
                usuario.setDobleTouch(false);
                usuario = servUsuario.save(usuario);
                String token = passwordEncoder.encode(usuario.getUsuario());
                String enlace = customIp + "/validarUsuario?id=" + String.valueOf(usuario.getId())
                        + "&token=" + token;
                boolean ok = Utilidades.enviarMail(servEmail, usuario, mensaje(modelo, "general.altaplay"),
                        enlace, "CorreoAlta");
                if (ok) {
                    resp = mensaje(modelo, "general.usuariocreado").concat(usuario.getUsuario());
                } else {
                    err = mensaje(modelo, "general.mailinvalido");
                }
            } catch (Exception ex) {
                err = "ERROR " + ex.getMessage();
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
                throw new Exception(mensaje(modelo, "general.pwdesigual"));
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
                usuario.setSegEspera(5);
                usuario.setDobleTouch(false);
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

    @PostMapping("/enviarMailMasivo")
    public String enviarMailMasivo(Model modelo, HttpServletRequest req) {

        CompletableFuture.runAsync(() -> {

            String txtMail = req.getParameter("txtMail");
            String enviar = req.getParameter("enviar");
            String prueba = req.getParameter("prueba");

            if (prueba != null) {
                Utilidades.enviarMail(servEmail, mailAdmin, "", mensaje(modelo, "general.avisoplay"),
                        txtMail, "Correo");
            }
            if (enviar != null) {
                List<Usuario> usuarios = servUsuario.findAll();
                int tiempoEspera = 9000; //Para enviar 400 mails por hora
                for (Usuario usu : usuarios) {
                    if (!usu.getUsuario().contains(".")) {
                        continue;
                    }
                    if (usu.isActivo()) {
                        Utilidades.enviarMail(servEmail, usu, mensaje(modelo, "general.avisoplay"),
                                txtMail, "Correo");
                        try {
                            Thread.sleep(tiempoEspera); // Pausa de 1 segundo
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            System.err.println("La espera fue interrumpida: " + e.getMessage());
                        }
                    }
                }
            }
        });

        return "redirect:/administracion";
    }

    @GetMapping("/eliminarUsuario/{id}")
    public String eliminarUsuario(@ModelAttribute("id") Long id, Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }

        try {
            Optional<Usuario> usuDelete = servUsuario.findById(id);
            if (usuDelete.isPresent()) {
                servUsuario.deleteById(id);
            }

        } catch (Exception e) {
            Logger.getLogger(ControladorVista.class.getName()).log(Level.SEVERE, null, e);
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/modificarUsuario")
    public String modificarUsuario(Model modelo
    ) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
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
                if (usuario.getIdioma() != null) {
                    usuSesion.setIdioma(usuario.getIdioma());
                }
                if (usuario.getSegEspera() < 5 || usuario.getSegEspera() > 30) {
                    err = mensaje(modelo, "general.errsegespera");
                } else {
                    usuSesion.setSegEspera(usuario.getSegEspera());
                }
                usuSesion.setDobleTouch(usuario.isDobleTouch());
                servUsuario.update(usuSesion.getId(), usuSesion);
                resp = mensaje(modelo, "general.datosmodificados");
                asignarIdiomaUsuario(modelo, usuario);
            } catch (Exception ex) {
                err = "ERROR " + ex.getMessage();
            }
        }

        modelo.addAttribute("result", resp);
        modelo.addAttribute("error", err);
        return "ModificarUsuario";
    }

    private void anyadirTemas(Model modelo) {

        ArrayList<Tema> temas = new ArrayList();
        Tema temaPlayHitsGame = null;
        Locale elLocale;

        if (modelo.getAttribute("locale") != null) {
            elLocale = (Locale) modelo.getAttribute("locale");
        } else {
            elLocale = Locale.getDefault();
        }

        FiltroTemas filtroTemas = new FiltroTemas(elLocale);
        List<Tema> temasFiltados = filtroTemas.filtrarTemas(servTema.findAll());
        for (Tema tema : temasFiltados) {
            if (!tema.getTema().equals("PlayHitsGame")) {
                temas.add(tema);
            } else {
                temaPlayHitsGame = tema;
            }
        }

        List<Tema> temasOrdenados = temas.stream()
                .sorted(Comparator.comparing(Tema::getTema))
                .collect(Collectors.toList());

        // esto es para que salga primero
        temasOrdenados.add(0, temaPlayHitsGame);

        modelo.addAttribute("temas", temasOrdenados);
    }

    private void crearPartida(Model modelo, Partida newPartida, Usuario usu) {

        Calendar fecha = Calendar.getInstance();
        newPartida.setAnyoInicial(1950);
        newPartida.setAnyoFinal(fecha.get(Calendar.YEAR));
        newPartida.setGrupo(usu.getGrupo());
        modelo.addAttribute("newpartida", newPartida);
        anyadirTemas(modelo);
        modelo.addAttribute("oprondas", NUMERO_RONDAS);
        modelo.addAttribute("nronda", 10);
        informarUsuarioModelo(modelo, usu);

    }

    @GetMapping("/crearPartida")
    public String crearPartida(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
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
            return "redirect:/logout";
        }

        Partida newPartida = new Partida();
        newPartida.setTipo(TipoPartida.personal);
        crearPartida(modelo, newPartida, usu);
        modelo.addAttribute("posiblesinvitados", null);
        return "CrearPartida";
    }

    @GetMapping("/crearPartidaPersonalInvitado")
    public String crearPartidaPersonalInvitado(Model modelo) {

        Calendar fecha = Calendar.getInstance();
        Partida newPartida = new Partida();
        newPartida.setAnyoInicial(1950);
        newPartida.setAnyoFinal(fecha.get(Calendar.YEAR));
        newPartida.setTipo(TipoPartida.personal);
        newPartida.setDificultad(Dificultad.Entreno);
        newPartida.setSinOfuscar(false);
        newPartida.setSonidos(false);

        modelo.addAttribute("newpartida", newPartida);
        anyadirTemas(modelo);

        return "CrearPartidaInvitado";
    }

    @GetMapping("/crearBatalla")
    public String crearBatalla(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }

        Partida newPartida = new Partida();
        crearPartida(modelo, newPartida, usu);

        return "CrearBatalla";
    }

    @PostMapping("/crearBatalla")
    public String crearBatalla(@ModelAttribute("newpartida") Partida partida,
            Model modelo,
            HttpServletRequest req
    ) {
        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }

        Partida newPartida = null;
        int nrondas = Integer.parseInt(req.getParameter("nrondas"));

        try {
            // Validaciones
            if (!usu.isAdmin()) {
                throw new Exception(mensaje(modelo, "general.usuarionopermitido"));
            }

            newPartida = CreaDatosPartida(req, modelo, partida, usu, nrondas);
            // retrasamos 1 dia la fecha para poder añadirse
            LocalDateTime newfecha = LocalDateTime.now();
            newfecha = newfecha.plusHours(24);
            newPartida.setFecha(newfecha);
            newPartida.setPublica(true);
            newPartida.setTipo(TipoPartida.batalla);
            newPartida.setStatus(StatusPartida.EnEspera);
            newPartida.setGrupo("");
            servPartida.updatePartida(newPartida.getId(), newPartida);
            modelo.addAttribute("result", mensaje(modelo, "general.btallacreada"));

        } catch (Exception ex) {
            if (newPartida != null) {
                servPartida.deletePartida(newPartida.getId());
            }
            String resp = "ERROR " + ex.getMessage();
            modelo.addAttribute("result", resp);
        }
        anyadirTemas(modelo);
        informarUsuarioModelo(modelo, usu);
        modelo.addAttribute("oprondas", NUMERO_RONDAS);
        modelo.addAttribute("nronda", nrondas);

        return "CrearBatalla";
    }

    private void registrarNuevaPartidaEnTema(Partida partida) {

        String tema = partida.getTema();
        Optional<Tema> elTema = servTema.findBytema(tema);
        if (elTema.isPresent()) {
            Tema updTema = elTema.get();
            updTema.setNPartidas(updTema.getNPartidas() + 1);
            servTema.update(updTema.getId(), updTema);
        }
    }

    private Partida CreaDatosPartida(HttpServletRequest req, Model modelo,
            Partida partida, Usuario usu, int nrondas) throws Exception {

        Calendar cal = Calendar.getInstance();
        int anyoActual = cal.get(Calendar.YEAR);
        Partida newPartida;

        if (partida.getAnyoFinal() <= partida.getAnyoInicial()) {
            throw new Exception(mensaje(modelo, "general.fechaserr"));
        }
        if (partida.getAnyoFinal() > anyoActual) {
            throw new Exception(mensaje(modelo, "general.fechafinerr"));
        }
        if (partida.getAnyoInicial() < 1950) {
            throw new Exception(mensaje(modelo, "general.fechainierr"));
        }
        if ((partida.getAnyoFinal() - partida.getAnyoInicial()) < 5) {
            throw new Exception(mensaje(modelo, "general.periodoerr"));
        }
        if (nrondas < 10 || nrondas > 30) {
            throw new Exception(mensaje(modelo, "general.rondaserr"));
        }

        List<Cancion> canciones = servCancion.obtenerCanciones(partida);

        if (canciones.size() < nrondas) {
            throw new Exception(mensaje(modelo, "general.cancionesinsuficientes"));
        }

        partida.setInvitados(new ArrayList());
        partida.setNCanciones(canciones.size());
        partida.setMaster(usu);
        partida.setRondaActual(1);
        partida.setGrupo(usu.getGrupo());
        newPartida = servPartida.savePartida(partida);
        //crear las rondas con nrondas
        newPartida.setRondas(new ArrayList());
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
            newPartida.getRondas().add(ronda);

        }
        asignarCancionesAleatorias(newPartida, canciones);
        for (Ronda ronda : partida.getRondas()) {
            servRonda.updateRonda(ronda.getId(), ronda);
        }
        newPartida.setStatus(StatusPartida.EnCurso);
        servPartida.updatePartida(newPartida.getId(), partida);

        int[] rangoAnyos = rangoAnyosCanciones((ArrayList<Cancion>) canciones);
        // Crear las opciones para las respuestas
        for (Ronda ronda : partida.getRondas()) {
            for (OpcionTituloTmp op : opcionesTitulosCanciones(ronda, true)) {
                servOpTitulo.saveOpcionTituloTmp(op);
            }
            for (OpcionInterpreteTmp op : opcionesInterpretesCanciones(ronda, true)) {
                servOpInterprete.saveOpcionInterpreteTmp(op);
            }
            for (OpcionAnyoTmp op
                    : opcionesAnyosCanciones(ronda, rangoAnyos[0], rangoAnyos[1])) {
                servOpAnyo.saveOpcionAnyoTmp(op);
            }
        }
        registrarNuevaPartidaEnTema(newPartida);

        return newPartida;
    }

    private String crearPartidaGrupo(Partida partida,
            Model modelo, HttpServletRequest req, Usuario usu) {

        Partida newPartida = null;
        int nrondas = Integer.parseInt(req.getParameter("nrondas"));

        try {

            // Validaciones
            if (!usu.sePuedeCrearPartidaMaster()) {
                throw new Exception(mensaje(modelo, "general.partidayacreada"));
            }
            newPartida = CreaDatosPartida(req, modelo, partida, usu, nrondas);
            usu.getPartidasMaster().add(newPartida);
            modelo.addAttribute("id_partidaSesion", newPartida);

            ArrayList<Usuario> posiblesInvitados = (ArrayList<Usuario>) modelo.getAttribute("posiblesinvitados");
            if (posiblesInvitados != null) {
                for (Usuario usuarioInv : posiblesInvitados) {

                    String valor = req.getParameter(usuarioInv.nombreId());
                    if ("on".equals(valor)) {

                        Optional<Usuario> usuario = servUsuario.findById(usuarioInv.getId());
                        if (!usuario.isEmpty()) {
                            usuario.get().getPartidasInvitado().add(newPartida);
                            partida.getInvitados().add(usuario.get());
                            servUsuario.update(usuario.get().getId(), usuario.get());
                        }
                    }
                }
            }

        } catch (Exception ex) {
            if (newPartida != null) {
                servPartida.deletePartida(newPartida.getId());
            }
            String resp = "ERROR " + ex.getMessage();
            modelo.addAttribute("result", resp);
            anyadirTemas(modelo);
            informarUsuarioModelo(modelo, usu);
            modelo.addAttribute("oprondas", NUMERO_RONDAS);
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
                throw new Exception(mensaje(modelo, "general.partidayacreadanormal"));
            }
            if (partida.getAnyoFinal() <= partida.getAnyoInicial()) {
                throw new Exception(mensaje(modelo, "general.fechaserr"));
            }
            if (partida.getAnyoFinal() > anyoActual) {
                throw new Exception(mensaje(modelo, "general.fechafinerr"));
            }
            if (partida.getAnyoInicial() < 1950) {
                throw new Exception(mensaje(modelo, "general.fechainierr"));
            }
            if ((partida.getAnyoFinal() - partida.getAnyoInicial()) < 5) {
                throw new Exception(mensaje(modelo, "general.periodoerr"));
            }

            FiltroCanciones filtro = new FiltroCanciones();
            filtro.setAnyoInicial(partida.getAnyoInicial());
            filtro.setAnyoFinal(partida.getAnyoFinal());
            filtro.setTema(partida.getTema());
            List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);
            if (canciones.size() <= 5) {
                throw new Exception(mensaje(modelo, "general.cancionesinsuficientes"));
            }

            partida.setInvitados(new ArrayList());
            partida.setMaster(usu);
            partida.setRondaActual(1);
            partida.setStatus(StatusPartida.EnCurso);
            Partida newPartida = servPartida.savePartida(partida);
            modelo.addAttribute("id_partidaSesion", partida.getId());
            usu.getPartidasMaster().add(newPartida);
            servUsuario.update(usu.getId(), usu);
            registrarNuevaPartidaEnTema(partida);
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
            return "redirect:/logout";
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
        modelo.addAttribute("result", mensaje(modelo, "general.errtipopartida"));
        modelo.addAttribute("spotifyimagenTmp", "");
        return "CrearPartida";

    }

    @PostMapping("/crearPartidaInvitado")
    public String crearPartidaInvitado(@ModelAttribute("newpartida") Partida partida,
            Model modelo,
            HttpServletRequest req) {
        modelo.addAttribute("mensajeRespuesta", "");
        modelo.addAttribute("respuestaOK", true);
        modelo.addAttribute("todoFallo", false);
        FiltroCanciones filtro = new FiltroCanciones();
        filtro.setAnyoInicial(partida.getAnyoInicial());
        filtro.setAnyoFinal(partida.getAnyoFinal());
        filtro.setTema(partida.getTema());
        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);
        if (canciones.size() <= 5) {
            modelo.addAttribute("result",
                    "No hay suficientes canciones para iniciar la partida");
            return "CrearPartidaInvitado";
        }

        partida.setDificultad(Dificultad.Entreno);

        modelo.addAttribute("partidaInvitado", partida);
        modelo.addAttribute("spotifyimagenTmp", "");

        servRegistro.registrar(TipoRegistro.Invitado, ipCliente(req));

        return "redirect:/partidaInvitado";

    }

    private void opcionesInvitado(Model modelo, Partida partida) {

        FiltroCanciones filtro = new FiltroCanciones();
        filtro.setAnyoInicial(partida.getAnyoInicial());
        filtro.setAnyoFinal(partida.getAnyoFinal());
        filtro.setTema(partida.getTema());
        List<Cancion> canciones = servCancion.buscarCancionesPorFiltro(filtro);

        Cancion cancionSel = cancionRandom(canciones);

        // Crear las opciones para las respuestas
        List<Cancion> opcCancionesTitulos = cancionesParaListaOpciones(canciones, cancionSel, 5);
        List<OpcionTituloTmp> opcTitulos = new ArrayList();
        for (Cancion cancion : opcCancionesTitulos) {
            OpcionTituloTmp op = new OpcionTituloTmp();
            op.setCancion(cancion.getId());
            if (partida.isSinOfuscar()) {
                op.setOpTitulo(cancion.getTitulo());
            } else {
                op.setOpTitulo(encriptarString(cancion.getTitulo()));
            }
            opcTitulos.add(op);
        }
        List<Cancion> opcCancionesInterpretes = cancionesParaListaOpciones(canciones, cancionSel, 5);
        List<OpcionInterpreteTmp> opcInterpretes = new ArrayList();
        for (Cancion cancion : opcCancionesInterpretes) {
            OpcionInterpreteTmp op = new OpcionInterpreteTmp();
            op.setCancion(cancion.getId());
            if (partida.isSinOfuscar()) {
                op.setOpInterprete(cancion.getInterprete());
            } else {
                op.setOpInterprete(encriptarString(cancion.getInterprete()));
            }
            opcInterpretes.add(op);
        }
        int[] rangoAnyos = rangoAnyosCanciones((ArrayList<Cancion>) canciones);
        List<String> opcAnyos = opcionesAnyosCancionesInvitado(cancionSel, rangoAnyos[0], rangoAnyos[1]);

        modelo.addAttribute("cancionInvitado", cancionSel);
        modelo.addAttribute("opcTitulos", opcTitulos);
        modelo.addAttribute("opcInterpretes", opcInterpretes);
        modelo.addAttribute("opcAnyos", opcAnyos);

    }

    @GetMapping("/partidaInvitado")
    public String partidaInvitado(Model modelo) {

        Partida partida = (Partida) modelo.getAttribute("partidaInvitado");

        if (partida == null) {
            return "redirect:/logout";
        }

        boolean todoFallo = false;
        if (modelo.getAttribute("todoFallo") != null) {
            todoFallo = (boolean) modelo.getAttribute("todoFallo");
        }
        if (modelo.getAttribute("esRecord") != null) {
            todoFallo = (boolean) modelo.getAttribute("esRecord");
        }

        List<OpcionTituloTmp> opcTitulos;
        List<OpcionInterpreteTmp> opcInterpretes;
        List<OpcionAnyoTmp> opcAnyos;
        if (!todoFallo) {
            opcionesInvitado(modelo, partida);
        } else {
            opcTitulos = new ArrayList();
            opcInterpretes = new ArrayList();
            opcAnyos = new ArrayList();
            modelo.addAttribute("opcTitulos", opcTitulos);
            modelo.addAttribute("opcInterpretes", opcInterpretes);
            modelo.addAttribute("opcAnyos", opcAnyos);
        }
        modelo.addAttribute("partidaInvitado", partida);

        if (modelo.getAttribute("mensajeRespuesta") == null) {
            modelo.addAttribute("mensajeRespuesta", "");
        }
        if (modelo.getAttribute("respuestaOK") == null) {
            modelo.addAttribute("respuestaOK", true);
        }
        if (modelo.getAttribute("todoFallo") == null) {
            modelo.addAttribute("todoFallo", false);
        }
        if (modelo.getAttribute("esRecord") == null) {
            modelo.addAttribute("esRecord", false);
        }

        return "PartidaInvitado";
    }

    @PostMapping("/partidaInvitado")
    public String partidaInvitado(Model modelo,
            @RequestParam("titulo") String titulo,
            @RequestParam("interprete") String interprete,
            @RequestParam("anyo") String anyo) {

        Partida partida = (Partida) modelo.getAttribute("partidaInvitado");
        if (partida == null) {
            return "redirect:/logout";
        }
        ArrayList<String> mensajeRespuesta = new ArrayList();
        boolean respuestaOK = false;
        int fallos = 0;
        boolean hasPerdido = false;
        Cancion cancion;
        boolean soundOK = false;
        boolean soundErrTitulo = false;
        boolean soundErrInterp = false;
        boolean soundErrAnyo = false;

        try {
            try {
                cancion = (Cancion) modelo.getAttribute("cancionInvitado");
            } catch (Exception e) {
                System.out.println("Error al obtener cancion en partida invitado en el post");
                return "error";
            }

            Optional<Cancion> canTit = servCancion.findById(Long.valueOf(titulo));
            if (!cancion.getTitulo().equals(canTit.get().getTitulo())) {
                mensajeRespuesta.add(mensaje(modelo, "general.titulocorrecto")
                        + cancion.getTitulo() + " " + mensaje(modelo, "general.turespondiste") + canTit.get().getTitulo());
                fallos = fallos + 1;
                soundErrTitulo = true;
            }
            Optional<Cancion> canInt = servCancion.findById(Long.valueOf(interprete));
            if (!cancion.getInterprete().equals(canInt.get().getInterprete())) {
                mensajeRespuesta.add(mensaje(modelo, "general.intercorrecto")
                        + cancion.getInterprete() + " " + mensaje(modelo, "general.turespondiste") + canInt.get().getInterprete());
                fallos = fallos + 1;
                soundErrInterp = true;
            }
            if (!anyo.equals(String.valueOf(cancion.getAnyo()))) {
                mensajeRespuesta.add(mensaje(modelo, "general.anyocorrecto")
                        + String.valueOf(cancion.getAnyo()) + " " + mensaje(modelo, "general.turespondiste") + anyo);
                fallos = fallos + 1;
                soundErrAnyo = true;
            }
            if (fallos == 0) {
                mensajeRespuesta.add(mensaje(modelo, "general.todoacertado"));
                respuestaOK = true;
                soundOK = true;
            }
            String img = cancion.getSpotifyimagen();
            modelo.addAttribute("spotifyimagenTmp", img);

        } catch (NumberFormatException ex) {
            Logger.getLogger(ControladorVista.class.getName()).log(Level.SEVERE, null, ex);
        }
        modelo.addAttribute("mensajeRespuesta", mensajeRespuesta);
        modelo.addAttribute("respuestaOK", respuestaOK);
        modelo.addAttribute("todoFallo", hasPerdido);
        if (partida.isSonidos()) {
            if (soundOK) {
                modelo.addAttribute("soundOK", "Aplausos.mp3");
            } else {
                modelo.addAttribute("soundOK", "");
            }
            if (soundErrTitulo) {
                modelo.addAttribute("soundErrTitulo", "ErrorTitulo.mp3");
            } else {
                modelo.addAttribute("soundErrTitulo", "");
            }
            if (soundErrInterp) {
                modelo.addAttribute("soundErrInterp", "ErrorInterprete.mp3");
            } else {
                modelo.addAttribute("soundErrInterp", "");
            }
            if (soundErrAnyo) {
                modelo.addAttribute("soundErrAnyo", "ErrorAnyo.mp3");
            } else {
                modelo.addAttribute("soundErrAnyo", "");
            }
        }

        return "redirect:/partidaInvitado";
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
            return "redirect:/logout";
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
        modelo.addAttribute("partidaSesion", partidaSesion);
        informarPartidaModelo(modelo, partidaSesion);

        return "ResultadosPartida";
    }

    @GetMapping("/partidaConsultaPersonal/{id}")
    public String partidaConsultaPersonal(@PathVariable Long id, Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
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

        if (!usu.sinGrupo()) {
            // Nos eleiminamos a nosotros mismos y ponemos el resto en seleccionado por defecto
            for (Usuario elem : usuarios) {
                if (!Objects.equals(elem.getId(), usu.getId())) {
                    invitados.add(elem);
                }
            }
        }
        return invitados;
    }

    @GetMapping("/administracion")
    public String administracion(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null || !usu.isAdmin()) {
            return "redirect:/logout";
        }

        Config config = servConfig.getSettings();

        LocalDateTime fecha;
        if (config.isMantenimiento()) {
            fecha = config.getFechaMantenimiento();
        } else {
            fecha = LocalDateTime.now();
        }

        // Formatea la fecha y hora en el formato requerido
        String fechaHoraFormateada = fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        modelo.addAttribute("fechaHoraActual", fechaHoraFormateada);

        modelo.addAttribute("config", config);

        return "Administracion";
    }

    @PostMapping("/cambiarMensajeInicio")
    public String cambiarMensajeInicio(Model model, HttpServletRequest req) {

        Config newConfig = servConfig.getSettings();
        newConfig.setMensajeInicio_es(req.getParameter("txtInicio_es"));
        newConfig.setMensajeInicio_en(req.getParameter("txtInicio_en"));
        servConfig.saveSettings(newConfig);

        return "redirect:/administracion";
    }

    @PostMapping("/ponerMantenimiento")
    public String ponerMantenimiento(Model model, HttpServletRequest req) {

        Config newConfig = servConfig.getSettings();
        newConfig.setMantenimiento(true);
        String f = req.getParameter("fechaMantenimiento");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime fecha = LocalDateTime.parse(f, formatter);
        newConfig.setFechaMantenimiento(fecha);
        //newConfig.setFechaMantenimiento();
        servConfig.saveSettings(newConfig);

        return "redirect:/administracion";
    }

    @PostMapping("/quitarMantenimiento")
    public String quitarMantenimiento(Model model, HttpServletRequest req) {

        Config newConfig = servConfig.getSettings();
        newConfig.setMantenimiento(false);
        servConfig.saveSettings(newConfig);

        return "redirect:/administracion";
    }

    @PostMapping("/accionesUsuarios")
    public String accionesUsuarios(Model modelo, HttpServletRequest req) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null || !usu.isAdmin()) {
            return "redirect:/logout";
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
        return "redirect:/usuarios";
    }

    @GetMapping("/validarUsuario")
    public String validarUsuario(Model modelo, HttpServletRequest req) {

        if (req.getParameter("id") == null
                || req.getParameter("token") == null) {
            String url;
            StringBuffer requestURL = req.getRequestURL(); // Obtiene la URL completa hasta el path
            String queryString = req.getQueryString(); // Obtiene la cadena de consulta (query parameters)
            if (queryString == null) {
                url = requestURL.toString();
            } else {
                url = requestURL.append("?").append(queryString).toString();
            }
            System.out.println("MLD Error validacion " + url);
            return "error";
        }

        Optional<Usuario> usu = null;
        String token = null;
        Long id = null;
        try {
            id = Long.valueOf(req.getParameter("id"));
            token = (String) req.getParameter("token");

            usu = servUsuario.findById(id);
        } catch (NumberFormatException numberFormatException) {
        }

        if (usu != null && usu.isPresent()) {
            Usuario usuario = usu.get();
            if (passwordEncoder.matches(usuario.getUsuario(), token)) {
                usuario.setActivo(true);
                servUsuario.update(id, usuario);
                return "redirect:/logout";
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
            modelo.addAttribute("error", mensaje(modelo, "general.usunoexiste"));
            return "PasswordOlvidado";
        } else {
            String token = usuario.get().getContrasenya();
            String enlace = mensaje(modelo, "general.txttokenrepssw") + token;

            Utilidades.enviarMail(servEmail, usuario.get(), mensaje(modelo, "general.recupcontra"),
                    enlace, "Correo");

            modelo.addAttribute("result", mensaje(modelo, "general.codigoenviado"));
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
            modelo.addAttribute("error", mensaje(modelo, "general.usunoexiste"));
            return "PasswordOlvidado";
        }
        Usuario usuCambio = usuarioCambio.get();
        if (!token.equals(usuCambio.getContrasenya())) {
            modelo.addAttribute("error", mensaje(modelo, "general.tokenerr"));
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
                    resp = mensaje(modelo, "general.cambiookpssw");
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
        Rol rol = null;
        if (usu == null) {
            try {
                rol = (Rol) modelo.getAttribute("rol");
            } catch (Exception e) {
            }
            if (rol == null || !rol.equals(Rol.playhitsgame)) {
                return "redirect:/logout";
            }
        }

        List<Usuario> jugadoresEstrella = servUsuario.usuariosEstrella();

        ArrayList<Tema> temas = (ArrayList<Tema>) servTema.findAllPorPartidas();

        ArrayList<Record> records = new ArrayList();

        for (Tema tema : temas) {
            Record newObj = new Record();
            newObj.setTema(tema);
            if (tema.getGenero() == null || tema.getIdioma() == null) {
                newObj.setDescripcion("");
            } else {
                newObj.setDescripcion("(" + tema.getGenero().name() + "-" + tema.getIdioma().name() + ")");
            }
            newObj.setCanciones(tema.getCanciones().size());
            newObj.setPuntos(tema.getPuntos());
            String usuRecord = "PlayHitsGame";
            if (tema.getUsuarioRecord() != null) {
                Optional<Usuario> usuario = servUsuario.findById(tema.getUsuarioRecord());
                if (usuario.isPresent()) {
                    usuRecord = usuario.get().getNombre();
                }
            }
            newObj.setUsuarioRecord(usuRecord);
            records.add(newObj);
        }

        String volver = "/panel";
        if (rol != null) {
            volver = "/accesoInvitado";
        }

        modelo.addAttribute("jugadoresEstrella", jugadoresEstrella);
        modelo.addAttribute("temas", records);
        modelo.addAttribute("volver", volver);

        return "Records";
    }

    @GetMapping("/consultaPuntuacionesTema/{tema_id}")
    public String consultaPuntuacionesTema(@PathVariable Long tema_id, Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }

        List<PuntuacionTMP> pts = new ArrayList();

        // Se muestran los 50 primeros
        List<Puntuacion> obtenerPuntuacionesPersonales = sevrPuntuacion.obtenerPuntuacionesPersonales(tema_id);
        Optional<Tema> tema = servTema.findById(tema_id);

        for (Puntuacion punt : obtenerPuntuacionesPersonales) {
            PuntuacionTMP ptsTMP = new PuntuacionTMP();
            Optional<Usuario> findById = servUsuario.findById(punt.getIdUsuario());
            if (findById.isPresent()) {
                ptsTMP.setUsuario(findById.get().getNombre());
            } else {
                ptsTMP.setUsuario("-");
            }
            ptsTMP.setPuntos(punt.getPuntos());
            pts.add(ptsTMP);
        }

        if (pts.isEmpty()) {
            return "redirect:/records";
        }

        modelo.addAttribute("tema", tema.get());
        modelo.addAttribute("pts", pts);

        return "PuntuacionesTema";
    }

    @GetMapping("/registro")
    public String registro(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null || !usu.isAdmin()) {
            return "redirect:/logout";
        }

        Page<Registro> obtenerRegistrosPaginados = servRegistro.obtenerRegistrosPaginados(page, REG_POR_PAG);
        modelo.addAttribute("registros", obtenerRegistrosPaginados);

        return "Registro";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null || !usu.isAdmin()) {
            return "redirect:/logout";
        }

        FiltroUsuarios filtro;
        if (modelo.getAttribute("filtroUsuarios") == null) {
            filtro = new FiltroUsuarios();
            modelo.addAttribute("filtroUsuarios", filtro);
        } else {
            filtro = (FiltroUsuarios) modelo.getAttribute("filtroUsuarios");
        }

        Page<Usuario> usuarios = servUsuario.findByFiltroBasico(filtro, page, REG_POR_PAG);

        modelo.addAttribute("usuarios", usuarios);

        return "Usuarios";
    }

    @PostMapping("/usuarios")
    public String usuarios(Model modelo, @ModelAttribute("filtroUsuarios") FiltroUsuarios filtro,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null || !usu.isAdmin()) {
            return "redirect:/logout";
        }

        Page<Usuario> usuarios = servUsuario.findByFiltroBasico(filtro, page, REG_POR_PAG);

        modelo.addAttribute("usuarios", usuarios);

        return "Usuarios";
    }

    @GetMapping("/registroLimpiar")
    public String registroLimpiar(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null || !usu.isAdmin()) {
            return "redirect:/logout";
        }

        servRegistro.limpiarRegistro(TipoRegistro.Visita.name());

        return "redirect:/registro";
    }

    @GetMapping("/crearTematica")
    public String crearTematica(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }

        Tema newTema = new Tema();
        newTema.setUsuario(usu);
        newTema.setIdioma(Idioma.International);
        newTema.setGenero(Genero.Generico);

        modelo.addAttribute("tema", newTema);

        return "AltaTema";
    }

    private HashSet<Cancion> cancionesParaTema(Tema tema) {

        List<Tema> temas = servTema.findAll();
        HashSet<Cancion> canciones = new HashSet();
        for (Tema unTema : temas) {
            if (unTema.getGenero().equals(tema.getGenero())
                    && unTema.getIdioma().equals(tema.getIdioma())) {
                canciones.addAll(unTema.getCanciones());
            }
        }

        return canciones;
    }

    @PostMapping("/crearTematica")
    public String crearTematica(@ModelAttribute("newTema") Tema newTema,
            Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }

        String err = null;
        HashSet<Cancion> canciones = null;

        try {
            Optional<Tema> temaBD = servTema.findBytema(newTema.getTema());
            if (temaBD.isPresent()) {
                err = mensaje(modelo, "general.temaexistebd");
            } else {
                canciones = cancionesParaTema(newTema);

                if (canciones.size() < Tema.MIN_CANCIONES) {
                    err = mensaje(modelo, "general.sincancionesparatema");
                }
            }
        } catch (Exception ex) {
            err = ex.getMessage();
        }

        if (err != null) {
            modelo.addAttribute("tema", newTema);
            modelo.addAttribute("error", err);
            return "AltaTema";
        }

        newTema.setUsuario(usu);

        Tema tema = servTema.save(newTema);

        modelo.addAttribute("tema", tema);
        modelo.addAttribute(
                "cancionesDisponibles", canciones);

        return "ModificarCancionesTema";
    }

    @GetMapping("/modificarTematica")
    public String modificarTematica(Model modelo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }

        HashSet<Cancion> canciones = cancionesParaTema(usu.getTemas().getFirst());

        modelo.addAttribute(
                "tema", usu.getTemas().getFirst());

        modelo.addAttribute(
                "cancionesDisponibles", canciones);

        return "ModificarCancionesTema";
    }

    @GetMapping("/anyadirCancionTema/{cancion_id}/{tema_id}")
    public void anyadirCancionTema(Model modelo,
            @PathVariable("cancion_id") Long cancion_id,
            @PathVariable("tema_id") Long tema_id) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return;
        }

        Cancion cancion = servCancion.findById(cancion_id).get();
        Tema tema = servTema.findById(tema_id).get();
        cancion.anyadirTematica(tema);

        servCancion.updateTemasCancion(cancion_id, cancion);
    }

    @GetMapping("/eliminarCancionTema/{cancion_id}/{tema_id}")
    public void eliminarCancionTema(Model modelo,
            @PathVariable("cancion_id") Long cancion_id,
            @PathVariable("tema_id") Long tema_id) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return;
        }

        Cancion cancion = servCancion.findById(cancion_id).get();
        Tema tema = servTema.findById(tema_id).get();
        cancion.eliminarTematica(tema);

        servCancion.updateTemasCancion(cancion_id, cancion);
    }

    @GetMapping("/partidasGrupo")
    public String partidasGrupo(Model modelo,
            @RequestParam(name = "page", defaultValue = "0") int page) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }

        Page<Partida> partidas = servPartida.partidasGrupo(page, REG_POR_PAG);

        modelo.addAttribute("partidas", partidas);

        return "PartidasGrupo";
    }

    @GetMapping("/unirseABatalla/{batalla_id}")
    public String unirseABatalla(Model modelo,
            @PathVariable("batalla_id") Long batalla_id) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "/";
        }

        Optional<Partida> findById = servPartida.findById(batalla_id);
        Partida partida;

        if (findById.isPresent()) {
            partida = findById.get();
            partida.getInvitados().add(usu);
            usu.getPartidasInvitado().add(partida);
            servPartida.updatePartida(partida.getId(), partida);
            servUsuario.update(usu.getId(), usu);
        }

        return "redirect:/panel";
    }

    @GetMapping("/salirDeBatalla/{batalla_id}")
    public String salirDeBatalla(Model modelo,
            @PathVariable("batalla_id") Long batalla_id) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "/";
        }

        Optional<Partida> findById = servPartida.findById(batalla_id);
        Partida partida;

        if (findById.isPresent()) {
            partida = findById.get();
            partida.getInvitados().remove(usu);
            usu.getPartidasInvitado().remove(partida);
            servPartida.updatePartida(partida.getId(), partida);
            servUsuario.update(usu.getId(), usu);
        }

        return "redirect:/panel";
    }

    private Respuesta rondaSinRespuestas(Usuario usu, Partida partida) {

        Respuesta respuesta = null;

        for (Ronda r : partida.getRondas()) {
            for (Respuesta resp : r.getRespuestas()) {
                if (resp.getUsuario().equals(usu)) {
                    if (!resp.isCompletada()) {
                        respuesta = resp;
                    }
                    break;
                }
            }
            if (respuesta != null) {
                break;
            }
        }

        return respuesta;
    }

    private List<Respuesta> rondasRespuestas(Usuario usu, Partida partida) {

        ArrayList<Respuesta> respuestas = new ArrayList();

        for (Ronda r : partida.getRondas()) {
            for (Respuesta resp : r.getRespuestas()) {
                if (resp.getUsuario().equals(usu)) {
                    if (resp.isCompletada()) {
                        respuestas.add(resp);
                    }
                    break;
                }
            }
        }

        return respuestas;
    }

    @GetMapping("/batalla/{id}")
    public String batalla(Model modelo, @PathVariable Long id) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }
        informarUsuarioModelo(modelo, usu);

        Partida partida = partidaModelo(modelo);

        if (partida == null) {
            Optional<Partida> findPartida = servPartida.findById(id);
            if (findPartida.isPresent()) {
                partida = findPartida.get();
            }
        }
        if (partida == null) {
            if (!partida.getMaster().equals(usu)
                    || !partida.getInvitados().contains(usu)) {
                return "redirect:/panel";
            }
        }
        informarPartidaModelo(modelo, partida);

        Respuesta respuestasUltimaRonda = rondaSinRespuestas(usu, partida);
        boolean todoFallo = false;
        if (modelo.getAttribute("todoFallo") != null) {
            todoFallo = (boolean) modelo.getAttribute("todoFallo");
        }
        if (modelo.getAttribute("esRecord") != null) {
            todoFallo = (boolean) modelo.getAttribute("esRecord");
        }

        if (respuestasUltimaRonda == null) {
            // Ya se han completado todas las respuestas redirigir a consulta
            modelo.addAttribute("partidaSesion", partida);
            modelo.addAttribute("pts", partida.ptsUsuario(usu));
            modelo.addAttribute("respuestas",
                    rondasRespuestas(usu, partida));
            return "BatallaConsulta";
        }

        Ronda ultimaRonda = respuestasUltimaRonda.getRonda();

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

        modelo.addAttribute("respuestas",
                rondasRespuestas(usu, partida));

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
        if (modelo.getAttribute("esRecord") == null) {
            modelo.addAttribute("esRecord", false);
        }

        long seconds = 0;
        if (respuestasUltimaRonda.getInicio() != null) {
            Duration duration = Duration.between(respuestasUltimaRonda.getInicio(), LocalTime.now());
            seconds = duration.getSeconds();
        } else {
            respuestasUltimaRonda.setInicio(LocalTime.now());
            servRespuesta.updateRespuesta(respuestasUltimaRonda.getId(), respuestasUltimaRonda);
        }

        modelo.addAttribute("contador", seconds - SEG_PARA_INICIO_RESPUESTA);

        return "Batalla";
    }

    @PostMapping("/batalla")
    public String batalla(Model modelo,
            @RequestParam("titulo") String titulo,
            @RequestParam("interprete") String interprete,
            @RequestParam("anyo") String anyo) {

        Usuario usu = usuarioModelo(modelo);
        if (usu == null) {
            return "redirect:/logout";
        }
        Partida partida = partidaModelo(modelo);
        if (partida == null) {
            return "redirect:/panel";
        }
        ArrayList<String> mensajeRespuesta = new ArrayList();
        boolean respuestaOK = false;
        int fallos = 0;
        boolean soundOK = false;
        boolean soundEliminado = false;
        boolean soundErrTitulo = false;
        boolean soundErrInterp = false;
        boolean soundErrAnyo = false;

        try {

            Respuesta resp = rondaSinRespuestas(usu, partida);
            Cancion cancion = resp.getRonda().getCancion();

            int ptsTitulo = 0, ptsInterp = 0;

            resp.setAnyo(Integer.parseInt(anyo));
            int ptsAnyo = Utilidades.calcularPtsPorAnyo(
                    Integer.parseInt(anyo), cancion, Dificultad.Normal);
            if (ptsAnyo > 0) {
                resp.setAnyoOk(true);
            } else {
                mensajeRespuesta.add(mensaje(modelo, "general.anyocorrecto")
                        + String.valueOf(cancion.getAnyo()) + " " + mensaje(modelo, "general.turespondiste") + anyo);
                fallos = fallos + 1;
                soundErrAnyo = true;
            }
            Optional<Cancion> canTit = servCancion.findById(Long.valueOf(titulo));
            if (canTit.isPresent()) {
                resp.setTitulo(canTit.get().getTitulo());
                ptsTitulo = Utilidades.calcularPtsPorTitulo(
                        canTit.get().getTitulo(), cancion, Dificultad.Normal, false);
                if (ptsTitulo > 0) {
                    resp.setTituloOk(true);
                } else {
                    mensajeRespuesta.add(mensaje(modelo, "general.titulocorrecto")
                            + cancion.getTitulo() + " " + mensaje(modelo, "general.turespondiste") + canTit.get().getTitulo());
                    fallos = fallos + 1;
                    soundErrTitulo = true;
                }
            }
            Optional<Cancion> canInt = servCancion.findById(Long.valueOf(interprete));
            if (canInt.isPresent()) {
                resp.setInterprete(canInt.get().getInterprete());
                ptsInterp = Utilidades.calcularPtsPorInterprete(
                        canInt.get().getInterprete(), cancion, Dificultad.Normal, false);
                if (ptsInterp > 0) {
                    resp.setInterpreteOk(true);
                } else {
                    mensajeRespuesta.add(mensaje(modelo, "general.intercorrecto")
                            + cancion.getInterprete() + " " + mensaje(modelo, "general.turespondiste") + canInt.get().getInterprete());
                    fallos = fallos + 1;
                    soundErrInterp = true;
                }
            }

            if (fallos == 0) {
                mensajeRespuesta.add(mensaje(modelo, "general.todoacertado"));
                respuestaOK = true;
                soundOK = true;
            }

            LocalTime actual = LocalTime.now();
            resp.setFin(actual);
            Duration duration = Duration.between(resp.getInicio(), actual);
            Long segundos = duration.getSeconds();
            Long pts = (long) (ptsAnyo + ptsTitulo + ptsInterp);
            if (segundos >= 0 && segundos < pts) {
                pts = pts - segundos;
            } else {
                pts = 0L;
            }
            int ptsFinales = pts.intValue();

            resp.setPuntos(ptsFinales);
            resp.setCompletada(true);
            servRespuesta.saveRespuesta(resp);

            modelo.addAttribute("spotifyimagenTmp", cancion.getSpotifyimagen());

        } catch (Exception ex) {
            Logger.getLogger(ControladorVista.class.getName()).log(Level.SEVERE, null, ex);
        }
        modelo.addAttribute("mensajeRespuesta", mensajeRespuesta);
        modelo.addAttribute("respuestaOK", respuestaOK);

        if (partida.isSonidos()) {
            if (soundOK) {
                modelo.addAttribute("soundOK", "Aplausos.mp3");
            } else {
                modelo.addAttribute("soundOK", "");
            }
            if (soundErrTitulo) {
                modelo.addAttribute("soundErrTitulo", "ErrorTitulo.mp3");
            } else {
                modelo.addAttribute("soundErrTitulo", "");
            }
            if (soundErrInterp) {
                modelo.addAttribute("soundErrInterp", "ErrorInterprete.mp3");
            } else {
                modelo.addAttribute("soundErrInterp", "");
            }
            if (soundErrAnyo) {
                modelo.addAttribute("soundErrAnyo", "ErrorAnyo.mp3");
            } else {
                modelo.addAttribute("soundErrAnyo", "");
            }
            if (soundEliminado) {
                modelo.addAttribute("soundEliminado", "Eliminado.mp3");
            } else {
                modelo.addAttribute("soundEliminado", "");
            }
        }

        return "redirect:/batalla/" + String.valueOf(partida.getId());
    }

}
