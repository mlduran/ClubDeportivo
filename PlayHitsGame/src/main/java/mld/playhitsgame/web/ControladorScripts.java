/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import mld.playhitsgame.correo.EmailServicioMetodos;
import mld.playhitsgame.correo.Mail;
import mld.playhitsgame.exemplars.Batalla;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Config;
import mld.playhitsgame.exemplars.Dificultad;
import mld.playhitsgame.exemplars.OpcionAnyoTmp;
import mld.playhitsgame.exemplars.OpcionInterpreteTmp;
import mld.playhitsgame.exemplars.OpcionTituloTmp;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.PtsUsuario;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.StatusBatalla;
import static mld.playhitsgame.exemplars.StatusBatalla.Programada;
import mld.playhitsgame.exemplars.StatusPartida;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.exemplars.TipoPartida;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.BatallaServicioMetodos;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.ConfigServicioMetodos;
import mld.playhitsgame.services.EstrellaServicioMetodos;
import mld.playhitsgame.services.OpcionAnyoTmpServicioMetodos;
import mld.playhitsgame.services.OpcionInterpreteTmpServicioMetodos;
import mld.playhitsgame.services.OpcionTituloTmpServicioMetodos;
import mld.playhitsgame.services.PartidaServicioMetodos;
import mld.playhitsgame.services.RespuestaServicioMetodos;
import mld.playhitsgame.services.RondaServicioMetodos;
import mld.playhitsgame.services.TemaServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import mld.playhitsgame.utilidades.Utilidades;
import static mld.playhitsgame.utilidades.Utilidades.asignarCancionesAleatorias;
import static mld.playhitsgame.utilidades.Utilidades.opcionesAnyosCanciones;
import static mld.playhitsgame.utilidades.Utilidades.opcionesInterpretesCanciones;
import static mld.playhitsgame.utilidades.Utilidades.opcionesTitulosCanciones;
import static mld.playhitsgame.utilidades.Utilidades.rangoAnyosCanciones;
import static mld.playhitsgame.utilidades.Utilidades.resultadosBatalla;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author miguel
 */
@Controller
public class ControladorScripts {

    @Value("${custom.mailadmin}")
    private String mailAdmin;
    @Value("${custom.entorno}")
    public String entorno;
    @Value("${custom.server.ip}")
    private String customIp;

    @Autowired
    EmailServicioMetodos servEmail;
    @Autowired
    UsuarioServicioMetodos servUsuario;
    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    BatallaServicioMetodos servBatalla;
    @Autowired
    PartidaServicioMetodos servPartida;
    @Autowired
    RondaServicioMetodos servRonda;
    @Autowired
    RespuestaServicioMetodos servRespuesta;
    @Autowired
    OpcionTituloTmpServicioMetodos servOpTitulo;
    @Autowired
    OpcionInterpreteTmpServicioMetodos servOpInterprete;
    @Autowired
    OpcionAnyoTmpServicioMetodos servOpAnyo;
    @Autowired
    EstrellaServicioMetodos servEstrella;
    @Autowired
    TemaServicioMetodos servTema;
    @Autowired
    ConfigServicioMetodos servConfig;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String tokenValidacion = "a3b2f10c-482d-4d7e-a5ed-2f9b7e8bcfd0";
    private int numMaxEstrellas;
    private int diasHistorico;
    private int diasEliminacion;
    private int diasNocorreo;
    
    private void actualizarValoresConfig(){
        
        Config laConfig = servConfig.getSettings();
        // para que estos valores se refresquen si se cambian en la BD
        // habria que llamar a este metodo
        numMaxEstrellas = laConfig.getNumMaxEstrellas();
        diasHistorico = laConfig.getDiasHistorico();
        diasEliminacion = laConfig.getDiasEliminacion();
        diasNocorreo = laConfig.getDiasNocorreo();
        
    }

    @PostConstruct
    public void init() {
        // Código a ejecutar al arrancar la aplicación
        System.out.println("Aplicación iniciada (PostConstruct ControladorScripts). Ejecutando tareas de inicio...");

        actualizarValoresConfig();
    }

    private void enviarMail(String asunto, ArrayList<String> txtsMail, List<Usuario> usuarios) {

        if (entorno != null && entorno.equals("Desarrollo")) {            
            Optional<Usuario> findByUsuario = servUsuario.findByUsuario(mailAdmin);
            Usuario usu = findByUsuario.get();
            String token = passwordEncoder.encode(usu.getUsuario());
            String enlace = customIp + "/noRecibirCorreo?id=" + String.valueOf(usu.getId())
                    + "&token=" + token;
            Mail mail = new Mail();
            mail.setAsunto(asunto);
            mail.setDestinatario(usu.getUsuario());
            mail.setNombre("");
            mail.setMensajes(txtsMail);
            mail.setPlantilla("Correo");
            mail.setUrlCancelacionCorreo(enlace);
            mail.setTextoUrlCancelacionCorreo("Quiero dejar de recibir estos correos");
            servEmail.encolarMail(mail);
        } else {
            for (Usuario usu : usuarios) {
                if (!usu.getUsuario().contains(".")) {
                    continue;
                }
                if (usu.isActivo() && !usu.isNoCorreos()) {
                    String token = passwordEncoder.encode(usu.getUsuario());
                    String enlace = customIp + "/noRecibirCorreo?id=" + String.valueOf(usu.getId())
                            + "&token=" + token;
                    Mail mail = new Mail();
                    mail.setAsunto(asunto);
                    mail.setDestinatario(usu.getUsuario());
                    mail.setNombre(usu.getNombre());
                    mail.setMensajes(txtsMail);
                    mail.setPlantilla("Correo");
                    mail.setUrlCancelacionCorreo(enlace);
                    mail.setTextoUrlCancelacionCorreo("Quiero dejar de recibir estos correos");
                    servEmail.encolarMail(mail);
                }
            }
        }

    }

    private void informarNuevaBatalla(Batalla batalla) {

        ArrayList<String> txtsMail = new ArrayList();

        txtsMail.add("Se ha creado una nueva batalla musical y ha comenzado el periodo para inscribirse:");
        txtsMail.add("Batalla " + batalla.getNombre());
        txtsMail.add(batalla.getDescripcionTxT());
        txtsMail.add("Si quieres participar, date prisa, solo tienes unas horas para unirte.");
        List<Usuario> usuarios = servUsuario.usuariosListaCorreoMasiva();

        enviarMail("PLAYHITSGAME NEW MUSICAL WAR " + batalla.getNombre(), txtsMail, usuarios);
    }

    private void informarComienzoBatalla(Batalla batalla) {

        ArrayList<String> txtsMail = new ArrayList();

        txtsMail.add("Ha comenzado la batalla musical a la que te has unido:");
        txtsMail.add("Batalla " + batalla.getNombre());
        txtsMail.add(batalla.getDescripcionTxT());
        txtsMail.add("Puedes acceder para responder tus opciones. Mucha suerte!!!!");
        List<Usuario> usuarios = batalla.getUsuarios();

        enviarMail("PLAYHITSGAME START MUSICAL WAR " + batalla.getNombre(), txtsMail, usuarios);
    }

    private void informarNuevaFaseBatalla(Batalla batalla) {

        ArrayList<String> txtsMail = new ArrayList();

        txtsMail.add("Felicidades!!! has pasado de fase en la batalla musical a la que te uniste:");
        txtsMail.add("Batalla " + batalla.getNombre());
        txtsMail.add(batalla.getDescripcionTxT());
        txtsMail.add("Puedes acceder para responder tus opciones. Mucha suerte!!!!");
        List<Usuario> usuarios = batalla.getUsuarios();

        enviarMail("PLAYHITSGAME NEXT LEVEL MUSICAL WAR " + batalla.getNombre(), txtsMail, usuarios);
    }

    private void informarFinBatalla(Batalla batalla) {

        ArrayList<String> txtsMail = new ArrayList();

        txtsMail.add("Ha finalizado la batalla musical a la que te uniste:");
        txtsMail.add("Batalla " + batalla.getNombre());
        txtsMail.add(batalla.getDescripcionTxT());
        txtsMail.add("Puedes acceder para visualizar los resultados.");
        String ganador = "----";
        if (batalla.getGanador() != null) {
            ganador = batalla.getGanador().getNombre();
        }
        txtsMail.add("Enhorabuena al ganador " + ganador
                + " que ha añadido una Estrella a su palmares");
        List<Usuario> usuarios = batalla.getUsuariosInscritos();

        enviarMail("PLAYHITSGAME END MUSICAL WAR " + batalla.getNombre(), txtsMail, usuarios);
    }

    private void iniciarBatalla(Batalla batalla) {

        List<Cancion> canciones = servCancion.obtenerCanciones(batalla);

        for (Usuario usu : batalla.getUsuarios()) {
            Partida newPartida = new Partida();

            // segun la fase en la que estemos aumentamos 
            // el numero de rondas hasta un maximo de 30
            int newRondas = (batalla.getNRondas() - 5) + (batalla.getFase() * 5);
            if (newRondas > 25) {
                newRondas = 25;
            }
            newPartida.setFase(batalla.getFase());
            newPartida.setNombre(batalla.getNombre());
            newPartida.setTipo(TipoPartida.batalla);
            newPartida.setTema(batalla.getTema());
            newPartida.setNCanciones(canciones.size());
            newPartida.setAnyoInicial(batalla.getAnyoInicial());
            newPartida.setAnyoFinal(batalla.getAnyoFinal());
            newPartida.setDificultad(Dificultad.Normal);
            newPartida.setFecha(batalla.getFecha());
            newPartida.setMaster(usu);
            newPartida.setPublica(batalla.isPublica());
            newPartida.setRondaActual(1);
            newPartida.setSinOfuscar(batalla.isSinOfuscar());
            newPartida.setSonidos(batalla.isSonidos());
            newPartida.setInvitados(new ArrayList());
            newPartida.setBatalla(batalla);

            newPartida = servPartida.savePartida(newPartida);
            newPartida.setRondas(new ArrayList());
            for (int i = 1; i <= newRondas; i++) {
                Ronda newRonda = new Ronda();
                newRonda.setNumero(i);
                newRonda.setPartida(newPartida);
                newRonda.setRespuestas(new ArrayList());
                Ronda ronda = servRonda.saveRonda(newRonda);

                //Crear las respuestas
                for (Usuario usuario : newPartida.usuariosPartida()) {
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
            for (Ronda ronda : newPartida.getRondas()) {
                servRonda.updateRonda(ronda.getId(), ronda);
            }
            newPartida.setStatus(StatusPartida.EnCurso);
            servPartida.updatePartida(newPartida.getId(), newPartida);
            batalla.getPartidas().add(newPartida);
            int[] rangoAnyos = rangoAnyosCanciones((ArrayList<Cancion>) canciones);
            // Crear las opciones para las respuestas
            for (Ronda ronda : newPartida.getRondas()) {
                for (OpcionTituloTmp op : opcionesTitulosCanciones(ronda, !newPartida.isSinOfuscar())) {
                    servOpTitulo.saveOpcionTituloTmp(op);
                }
                for (OpcionInterpreteTmp op : opcionesInterpretesCanciones(ronda, !newPartida.isSinOfuscar())) {
                    servOpInterprete.saveOpcionInterpreteTmp(op);
                }
                for (OpcionAnyoTmp op : opcionesAnyosCanciones(ronda, rangoAnyos[0], rangoAnyos[1])) {
                    servOpAnyo.saveOpcionAnyoTmp(op);
                }
            }
        }
        batalla.setStatus(StatusBatalla.EnCurso);
        servBatalla.update(batalla.getId(), batalla);

    }

    private void finalizarPartidasBatalla(Batalla batalla) {

        // Forzamos la finalizacion de todas las partidas
        for (Partida partida : batalla.getPartidas()) {
            partida.setStatus(StatusPartida.Terminada);
            servPartida.updatePartida(partida.getId(), partida);
            servOpTitulo.deleteByPartida(partida.getId());
            servOpInterprete.deleteByPartida(partida.getId());
            servOpAnyo.deleteByPartida(partida.getId());
        }
    }

    private void finalizarBatalla(Batalla batalla) {

        finalizarPartidasBatalla(batalla);

        //Damos ESTRELLA al primero
        List<PtsUsuario> resultadosBatalla = Utilidades.resultadosBatalla(batalla, batalla.getFase());
        if (!resultadosBatalla.isEmpty()) {
            PtsUsuario ptsPrimero = Utilidades.resultadosBatalla(batalla, batalla.getFase()).getFirst();
            if (batalla.isPublica()) {
                servEstrella.darEstrella(ptsPrimero.getUsuario(), numMaxEstrellas);
            }
            batalla.setGanador(ptsPrimero.getUsuario());
        }
        batalla.setStatus(StatusBatalla.Terminada);
        servBatalla.update(batalla.getId(), batalla);
    }

    private void cancelarBatalla(Batalla batalla) {

        List<Usuario> usuariosInscritos = batalla.getUsuariosInscritos();
        for (Usuario usu : usuariosInscritos) {
            usu.getBatallasInscritas().remove(batalla);
            servUsuario.update(usu.getId(), usu);
        }
        batalla.setStatus(StatusBatalla.Terminada);
        servBatalla.update(batalla.getId(), batalla);
    }

    private void pasarUsuariosBatallaDeFase(Batalla batalla, int nUsuAClasifiar, int fase) {

        List<PtsUsuario> resultadosBatalla = resultadosBatalla(batalla, fase);

        for (PtsUsuario pts
                : resultadosBatalla.subList(nUsuAClasifiar, batalla.getUsuarios().size())) {
            Usuario usu = pts.getUsuario();
            usu.getBatallas().remove(batalla);
            servUsuario.update(usu.getId(), usu);
            batalla.getUsuarios().remove(usu);
        }

        servBatalla.update(batalla.getId(), batalla);
    }

    private void crearNuevaBatallaPublica() {

        ArrayList<Tema> temas = new ArrayList();
        for (Tema tema : servTema.findAll()) {
            if (tema.isActivo()) {
                temas.add(tema);
            }
        }
        Random random = new Random();
        int indTemaAleatorio = random.nextInt(temas.size());
        Tema temaAleatorio = temas.get(indTemaAleatorio);

        Batalla newBatalla = new Batalla();
        newBatalla.setUsuariosInscritos(new ArrayList());
        newBatalla.setTema(temaAleatorio.getTema());
        Calendar fecha = Calendar.getInstance();
        newBatalla.setAnyoInicial(1950);
        newBatalla.setAnyoFinal(fecha.get(Calendar.YEAR));
        List<Cancion> canciones = servCancion.obtenerCanciones(newBatalla);
        newBatalla.setNCanciones(canciones.size());
        LocalDateTime newfecha = LocalDateTime.now();
        newfecha = newfecha.plusHours(24);
        newBatalla.setFecha(newfecha);
        newBatalla.setPublica(true);
        newBatalla.setStatus(StatusBatalla.Programada);
        newBatalla.setNRondas(10);
        newBatalla.setFase(1);
        newBatalla = servBatalla.save(newBatalla);
        newBatalla.setNombre(temaAleatorio.getTema() + " " + String.valueOf(newBatalla.getId()));
        servBatalla.update(newBatalla.getId(), newBatalla);
    }

    private void tratarBatallas() {

        crearNuevaBatallaPublica();

        // para los cambios de status asumimos que este script
        // se lanzara diariamente a la misma hora
        List<Batalla> batallas = servBatalla.findAll();

        for (Batalla batalla : batallas) {
            switch (batalla.getStatus()) {
                case Inscripcion -> {
                    batalla.setUsuarios(new ArrayList());
                    List<Usuario> usuariosInscritos = batalla.getUsuariosInscritos();
                    // si no hay al menos 2 inscritos cancelamos la batalla
                    if (usuariosInscritos.size() < 2) {
                        cancelarBatalla(batalla);
                    } else {
                        for (Usuario usu : usuariosInscritos) {
                            usu.getBatallas().add(batalla);
                            batalla.getUsuarios().add(usu);
                            servUsuario.update(usu.getId(), usu);
                        }
                        batalla.setFase(1);
                        servBatalla.update(batalla.getId(), batalla);
                        iniciarBatalla(batalla);
                        informarComienzoBatalla(batalla);
                    }
                }
                case EnCurso -> {
                    // Si podemos hacer otra batalla la hacemos
                    int nUsuAClasifiar = 0;
                    int totalUsuarios = batalla.getUsuarios().size();

                    if (totalUsuarios > 1) {
                        // para el caso especial de que totalUsuarios sea 3 hacemos que
                        // lo minimo sea 2
                        if (nUsuAClasifiar == 3) {
                            nUsuAClasifiar = 2;
                        } else {
                            nUsuAClasifiar = totalUsuarios / 2;
                        }
                    }
                    if (nUsuAClasifiar > 1) {
                        pasarUsuariosBatallaDeFase(
                                batalla, nUsuAClasifiar, batalla.getFase());
                        batalla.setFase(batalla.getFase() + 1);
                        finalizarPartidasBatalla(batalla);
                        iniciarBatalla(batalla);
                        informarNuevaFaseBatalla(batalla);
                    } else {
                        finalizarBatalla(batalla);
                        informarFinBatalla(batalla);
                    }
                }
                case Programada -> {
                    batalla.setStatus(StatusBatalla.Inscripcion);
                    LocalDateTime newfecha = LocalDateTime.now();
                    newfecha = newfecha.plusHours(24);
                    batalla.setFecha(newfecha);
                    servBatalla.update(batalla.getId(), batalla);
                    informarNuevaBatalla(batalla);
                }

                default -> {
                }
            }
        }
    }

    private void finalizarPartidasObsoletas() {

        // Se finalizan las partidas que tengan mas de 30 dias
        LocalDateTime fechaHace30Dias = LocalDateTime.now().minusDays(30);
        for (Partida partida : servPartida.partidasActuales()) {
            if (partida.isTipoGrupo() || partida.isTipoPersonal()) {
                if (partida.getFecha().isBefore(fechaHace30Dias)) {
                    String txt = partida.getDescripcionLog();
                    partida.setStatus(StatusPartida.Terminada);
                    servPartida.updatePartida(partida.getId(), partida);
                    System.out.println("Se finaliza partida " + txt);
                }
            }
        }
    }

    private void pasarAHistoricoPartidasYBatallas() {

        // Se pasan a Historico las partidas / Batallas que tengan mas de 90 dias
        System.out.println("Se pasa a historico partidas y batallas anterirores a " + String.valueOf(diasHistorico) + " dias");
        LocalDateTime fechaHace90Dias = LocalDateTime.now().minusDays(diasHistorico);
        for (Partida partida : servPartida.partidasFinalizadas()) {
            if (partida.isTipoGrupo() || partida.isTipoPersonal()) {
                if (partida.getFecha().isBefore(fechaHace90Dias)) {
                    String txt = partida.getDescripcionLog();
                    partida.setStatus(StatusPartida.Historico);
                    servPartida.updatePartida(partida.getId(), partida);
                    System.out.println("Se pasa a Historico partida " + txt);
                }
            }
        }
        for (Batalla batalla : servBatalla.batallasFinalizadas()) {
            if (batalla.getFecha().isBefore(fechaHace90Dias)) {
                String txt = batalla.getDescripcionLog();
                batalla.setStatus(StatusBatalla.Historico);
                servBatalla.update(batalla.getId(), batalla);
                System.out.println("Se pasa a Historico batalla " + txt);
            }
        }
    }
    
    private void eliminarPartidasYBatallas() {

        System.out.println("Se eliminan partidas y batallas anterirores a " + String.valueOf(diasEliminacion) + " dias");
        // Eliminamos partidas / Batallas que tengan mas de 180 dias
        LocalDateTime fechaHace180Dias = LocalDateTime.now().minusDays(diasEliminacion);
        for (Partida partida : servPartida.partidasHistoricas()) {
            if (partida.isTipoGrupo() || partida.isTipoPersonal()) {
                if (partida.getFecha().isBefore(fechaHace180Dias)) {
                    String txt = partida.getDescripcionLog();                    
                    servPartida.deletePartida(partida.getId());
                    System.out.println("Se elimina partida " + txt);
                }
            }
        }
        for (Batalla batalla : servBatalla.batallasHistoricas()) {
            if (batalla.getFecha().isBefore(fechaHace180Dias)) {
                String txt = batalla.getDescripcionLog();                
                servBatalla.deleteBatalla(batalla);
                System.out.println("Se elimina batalla " + txt);
            }
        }
    }
    
    private void eliminarUsuariosNoValidados(){
        
        // Se eliminan usuarios no activados si han pasado 30 dias desde que no a accedido
        LocalDateTime fechaHace30Dias = LocalDateTime.now().minusDays(30);
        Date fecha = Date.from(fechaHace30Dias.atZone(ZoneId.systemDefault()).toInstant());        
                
        for (Usuario usu : servUsuario.usuariosDesactivados()){
            if (usu.getAlta().before(fecha)){
                servUsuario.deleteById(usu.getId());
            }
        }
    }
    
    private void activarOpcionDeNoEnvioDeCorreo(){
        
        // Se activa opcion si han pasado 30 dias desde que no a accedido
        LocalDateTime fechaHace30Dias = LocalDateTime.now().minusDays(diasNocorreo);
        Date fecha = Date.from(fechaHace30Dias.atZone(ZoneId.systemDefault()).toInstant());        
        
        List<Usuario> usuariosListaCorreoMasiva = servUsuario.usuariosListaCorreoMasiva();
        for (Usuario usu : usuariosListaCorreoMasiva){
            if (usu.getUltimoAcceso().before(fecha)){
                usu.setNoCorreos(true);
                servUsuario.update(usu.getId(), usu);
            }
        }
    }
    

    @GetMapping("/lanzarScripts")
    public ResponseEntity<Void> lanzarScripts(Model modelo, HttpServletRequest req
    ) {

        String txtCorreo;

        if (req.getParameter("token") == null) {
            String url;
            StringBuffer requestURL = req.getRequestURL(); // Obtiene la URL completa hasta el path
            String queryString = req.getQueryString(); // Obtiene la cadena de consulta (query parameters)
            if (queryString == null) {
                url = requestURL.toString();
            } else {
                url = requestURL.append("?").append(queryString).toString();
            }
            txtCorreo = "Error lanzamiento scripts " + url;
        } else {
            String token = (String) req.getParameter("token");
            if (token.equals(tokenValidacion)) {
                try {
                    // METODOS A EJECUTAR
                    actualizarValoresConfig();
                    tratarBatallas();
                    pasarAHistoricoPartidasYBatallas();
                    finalizarPartidasObsoletas();
                    activarOpcionDeNoEnvioDeCorreo();
                    eliminarUsuariosNoValidados();
                    eliminarPartidasYBatallas();
                    /////////////////////// FIN
                    txtCorreo = "Lanzamiento de scripts OK";
                } catch (Exception ex) {
                    txtCorreo = "Error en script : " + ex.getMessage();
                }
            } else {
                txtCorreo = "Error lanzamiento scripts. El token de validacion es incorrecto : " + token;
            }
        }

        Mail mail = new Mail();
        mail.setAsunto("Jornada PlayHitsGame");
        mail.setDestinatario(mailAdmin);
        mail.setMensaje(txtCorreo);
        mail.setPlantilla("Correo");
        mail.setNombre("");
        mail.setPrioritario(true);
        servEmail.encolarMail(mail);

        return ResponseEntity.ok().build();

    }

}
