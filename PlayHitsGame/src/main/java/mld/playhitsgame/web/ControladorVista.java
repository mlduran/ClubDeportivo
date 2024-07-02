/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.exemplars.Partida;
import mld.playhitsgame.exemplars.Respuesta;
import mld.playhitsgame.exemplars.Rol;
import mld.playhitsgame.exemplars.Ronda;
import mld.playhitsgame.exemplars.StatusPartida;
import mld.playhitsgame.exemplars.Tema;
import mld.playhitsgame.exemplars.Usuario;
import mld.playhitsgame.services.CancionServicioMetodos;
import mld.playhitsgame.services.PartidaServicioMetodos;
import mld.playhitsgame.services.RespuestaServicioMetodos;
import mld.playhitsgame.services.RondaServicioMetodos;
import mld.playhitsgame.services.TemaServicioMetodos;
import mld.playhitsgame.services.UsuarioServicioMetodos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
// rol puede ser master o invitado
@SessionAttributes({"usuarioSesion", "partidaSesion", "posiblesinvitados", "rol"})
@Slf4j
public class ControladorVista {
    
     
    @Autowired
    CancionServicioMetodos servCancion;
    @Autowired
    UsuarioServicioMetodos servUsuario;
    @Autowired
    PartidaServicioMetodos servPartida;
    @Autowired
    RondaServicioMetodos servRonda;
    @Autowired
    RespuestaServicioMetodos servRespuesta;
    @Autowired
    TemaServicioMetodos servTema;
    
    @GetMapping("/panel")
    public String panel(Model modelo){    
         
        return "Panel";        
    }
    
    
    private void resultadosPartida(Partida partidaSesion, Model modelo){
        
        HashMap<String,List<Respuesta>> resultadosPartida = new HashMap();
        String nomUsu;
        ArrayList lista;
        
         
        for (Ronda ronda : partidaSesion.getRondas()){            
            for (Respuesta respuesta : ronda.getRespuestas()){
                nomUsu = respuesta.getUsuario().nombre();
                if (!resultadosPartida.containsKey(nomUsu))
                    resultadosPartida.put(nomUsu, new ArrayList());
                lista = (ArrayList) resultadosPartida.get(nomUsu);
                lista.add(respuesta);                
                resultadosPartida.put(nomUsu, lista);
            }
        }
        
        modelo.addAttribute("resultados", resultadosPartida);
        
    }
    
    @GetMapping("/partidaConsulta/{id}")
    public String partidaConsulta(@PathVariable Long id, Model modelo){
        
        Optional<Partida> partida = servPartida.findById(id);
        Partida partidaSesion = partida.get();  
        
        resultadosPartida(partidaSesion, modelo);
        
        modelo.addAttribute("partidaSesion", partidaSesion);        
        
        return "ResultadosPartida";
        
    }
    
    @GetMapping("/partidaMaster")
    public String partidaMaster(Model modelo){
        
        Usuario usu = (Usuario) modelo.getAttribute("usuarioSesion");
        modelo.addAttribute("rol", Rol.master);
        Partida partida = usu.partidaMasterEnCurso();
        
        return partida(modelo, partida);
        
    }
    
    @GetMapping("/partidaInvitado")
    public String partidaInvitado(Model modelo){
        
        Usuario usu = (Usuario) modelo.getAttribute("usuarioSesion");
        modelo.addAttribute("rol", Rol.invitado);
        
        List<Partida> partidas = usu.getPartidasInvitado();
        
        return "SeleccionarPartida";
        
    }
     
    public String partida(Model modelo, Partida partida){
        
        Usuario usu = (Usuario) modelo.getAttribute("usuarioSesion");
             
        modelo.addAttribute("partidaSesion", partida);
                
        return "Partida";      
        
    }
        
    @PostMapping("/partida")
    public String partida(@ModelAttribute("anyo") int anyo,Model modelo){        
        
        Usuario usu = (Usuario) modelo.getAttribute("usuarioSesion");
        Partida partida = (Partida) modelo.getAttribute("partidaSesion");
        Ronda rondaActiva = partida.rondaActiva();
        
        Respuesta resp = servRespuesta.buscarPorRondaUsuario(rondaActiva.getId(), usu.getId());
                 
        resp.setAnyo(anyo);
        int pts = calcularPtsPorAnyo(anyo, rondaActiva.getCancion());
        resp.setPuntos(pts);
        servRespuesta.updateRespuesta(resp.getId(), resp);
        rondaActiva.setCompletada(true);
        servRonda.updateRonda(rondaActiva.getId(), rondaActiva);
        boolean acabar = true;
        if (partida.hayMasRondas()){
            partida.pasarSiguienteRonda();            
            acabar = false;
        }else{
            partida.setStatus(StatusPartida.Terminada);
            partida.asignarGanador();
        }
        servPartida.updatePartida(partida.getId(), partida);
        
        modelo.addAttribute("partidaSesion", partida);
                
        if (acabar){
            resultadosPartida(partida, modelo);
            return "ResultadosPartida";
        }else 
            return "Partida";        
    }
    
 
    @GetMapping("/altaUsuario")
    public String altaUsuario(Model modelo){
        
        modelo.addAttribute("newusuario", new Usuario());
        return "AltaUsuario";
        
    }
    
    @PostMapping("/altaUsuario")
    public String altaUsuario(@ModelAttribute("newusuario") Usuario usuario, Model modelo){
        
        String resp = "OK";
        
        try{
            servUsuario.saveUsuario(usuario);
        }catch(Exception ex){
            resp = "ERROR " + ex; 
        }   
        
        modelo.addAttribute("result", resp);        
        
        return "AltaUsuario";
        
    }
    
    
    
    @GetMapping("/partida/crear")
    public String crearPartida(Model modelo){     
        
        Calendar fecha = Calendar.getInstance();
        
        Partida newPartida = new Partida();
        newPartida.setAnyoInicial(1950);
        newPartida.setAnyoFinal(fecha.get(Calendar.YEAR) -1);
        modelo.addAttribute("newpartida", newPartida);
        ArrayList<String> temas = new ArrayList();
        temas.add("");
        for (Tema tema : servTema.findAll())
            temas.add(tema.getTema());
        modelo.addAttribute("temas", temas);
        
        return "CrearPartida";
        
    }
    
       
    @PostMapping("/partida/crear")
    public String crearPartida(@ModelAttribute("newpartida") Partida partida, 
            @ModelAttribute("nrondas") Integer nrondas, Model modelo){     
           
        
        Usuario usu = (Usuario) modelo.getAttribute("usuarioSesion");
        Calendar cal= Calendar.getInstance();
        int anyoActual = cal.get(Calendar.YEAR);

        try{
            
            // Validaciones
            if (partida.getAnyoFinal() <= partida.getAnyoInicial())
                throw new Exception("Las Fechas Iniciales y Finales no son correctas");            
            if (partida.getAnyoFinal() > anyoActual)
                throw new Exception("El año final es erroneo");
            if (partida.getAnyoInicial() < 1950)
                throw new Exception("El año inicial es erroneo");
            if (nrondas < 5 || nrondas > 30)
                throw new Exception("Las rondas deben estar entre 5 y 30");
            
            partida.setMaster(usu);
            partida.setRondaActual(1);
            Partida newPartida = servPartida.savePartida(partida);
            usu.getPartidasMaster().add(newPartida);
            servUsuario.updateUsuario(usu.getId(), usu);            
            
            //crear las rondas con nrondas
            partida.setRondas(new ArrayList());
            for (int i = 1; i <= nrondas; i++){
                Ronda obj = new Ronda();
                obj.setNumero(i);
                obj.setPartida(partida); 
                obj.setRespuestas(new ArrayList());
                Ronda ronda = servRonda.saveRonda(obj);
                partida.getRondas().add(ronda);                
                
            }
            servCancion.asignarcancionesAleatorias(partida);
            for (Ronda ronda : partida.getRondas()){                
                servRonda.updateRonda(ronda.getId(), ronda);
            }
            
            partida.setStatus(StatusPartida.AnyadirJugadores);
            servPartida.updatePartida(partida.getId(), partida);
            
        }catch(Exception ex){
            String resp = "ERROR " + ex; 
            modelo.addAttribute("result", resp);  
            return "CrearPartida";
        }          
        // obtener los usuarios posibles por grupo
        ArrayList<Usuario> posiblesInvitados = (ArrayList<Usuario>) usuariosGrupo(usu);

        modelo.addAttribute("posiblesinvitados", posiblesInvitados); 
        modelo.addAttribute("partidaSesion", partida); 
        //asignar usuarios
        return "AnyadirInvitados";
        
    }
    
    
    private List<Usuario> usuariosGrupo(Usuario usu){
        
        
        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) servUsuario.usuariosGrupo(usu.getGrupo()); 
        ArrayList<Usuario> invitados =  new ArrayList(); 
        
        // Nos eleiminamos a nosotros mismos y ponemos el resto en seleccionado por defecto
        for (Usuario elem : usuarios){
            if (!Objects.equals(elem.getId(), usu.getId())){
                   invitados.add(elem);                   
            }                
        }
        
        return invitados;
        
    }   
    
    
    @GetMapping("/partida/anyadirInvitados")
    public String anyadirInvitadosPartida(Model modelo){     
        
        Usuario usu = (Usuario) modelo.getAttribute("usuarioSesion");   
        
        Optional<Partida> partidas = servPartida.partidaUsuarioMaster(usu.getId());
        Partida partida = partidas.get();
        
        if (partida != null){
            modelo.addAttribute("partidaSesion", partida);  
        
            ArrayList<Usuario> posiblesInvitados = (ArrayList<Usuario>) usuariosGrupo(usu);

            if (!posiblesInvitados.isEmpty()){
                modelo.addAttribute("posiblesinvitados", posiblesInvitados);      
                return "AnyadirInvitados";
            }else{
                
                crearRespuestas(partida);
                partida.setStatus(StatusPartida.EnCurso);
                servPartida.updatePartida(partida.getId(), partida);
        
                modelo.addAttribute("partidaSesion", partida);           
                return "Partida";

            }
            
        }else{
            
            modelo.addAttribute("result", "NO SE HA ENCONTRADO PARTIDA");  
            return "Panel";            
        } 
    }
    
    private void crearRespuestas(Partida partida){
        
         for (Ronda ronda : partida.getRondas()){
            ronda.setRespuestas(new ArrayList());
            for (Usuario usuario : partida.usuariosPartida()){ 
                usuario.setRespuestas(new ArrayList());
                Respuesta newResp = new Respuesta();
                newResp.setRonda(ronda);
                newResp.setUsuario(usuario);
                Respuesta resp = servRespuesta.saveRespuesta(newResp);
                ronda.getRespuestas().add(resp);
                usuario.getRespuestas().add(resp);
                servUsuario.updateUsuario(usuario.getId(), usuario);
            }            
            
        }        
        
        
    }
    
    @PostMapping("/partida/anyadirInvitados")
    public String anyadirInvitadosPartida(Model modelo, HttpServletRequest req){     
        
        Partida partida = (Partida) modelo.getAttribute("partidaSesion");
        ArrayList<Usuario> posiblesInvitados = (ArrayList<Usuario>) modelo.getAttribute("posiblesinvitados");
                 
        partida.setInvitados(new ArrayList());
        
        for (Usuario usu : posiblesInvitados){            
            
            String valor = req.getParameter(usu.nombreId());            
            if ("on".equals(valor)){
                
                Optional<Usuario> usuario = servUsuario.findById(usu.getId());
                if (!usuario.isEmpty()){
                
                    usuario.get().getPartidasInvitado().add(partida); 
                    partida.getInvitados().add(usuario.get());            
                    servUsuario.updateUsuario( usuario.get().getId(), usuario.get());                    
                }
            }
        }
        
        //crear las respuestas 
        crearRespuestas(partida);
     
        partida.setStatus(StatusPartida.EnCurso);
        servPartida.updatePartida(partida.getId(), partida);
        
        modelo.addAttribute("partidaSesion", partida);
           
        return "Partida";
        
    }
    
        
     
    @GetMapping("/listaCanciones")
    public String listaCanciones(Model model){       
       
        
        List<Cancion> lista = servCancion.findAll();
        
        TreeMap<Integer,Integer> estadistica = new TreeMap();
       for (int i = 1950; i <= 2023; i++) {
           estadistica.put(i, 0);
       }
        
        
        for (Cancion obj: lista){  
                
                estadistica.put(obj.getAnyo(), estadistica.get(obj.getAnyo()) + 1);
            }
            
   
        model.addAttribute("estadistica", estadistica);
        model.addAttribute("lista", lista);
       
        return "ListaCanciones";
        
    }
    
    public int calcularPtsPorAnyo(int anyo, Cancion cancion){
        
        int anyoCancion = cancion.getAnyo();
        int pts = 0;
        
        int x = Math.abs(anyo - anyoCancion);
        
        if (x == 0)
            pts = 25;
        if (x == 1)
            pts = 15;
        if (x == 2)
            pts = 10;
        if (x > 2)
            pts = 10 - x;
        if (pts < 0)
            pts = 0;

        return pts;
        
    }
    
}
