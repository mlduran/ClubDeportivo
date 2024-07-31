/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.web;

import mld.playhitsgame.seguridad.CrearUsuarioDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
import static mld.playhitsgame.utilidades.Utilidades.*;
import mld.playhitsgame.exemplars.Genero;
import mld.playhitsgame.exemplars.Idioma;
import mld.playhitsgame.exemplars.OpcionInterpreteTmp;
import mld.playhitsgame.exemplars.OpcionTituloTmp;
import mld.playhitsgame.seguridad.Roles;
import mld.playhitsgame.seguridad.UsuarioRol;
import mld.playhitsgame.services.OpcionInterpreteTmpServicioMetodos;
import mld.playhitsgame.services.OpcionTituloTmpServicioMetodos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
// rol puede ser master o invitado
// utilizamos los ids para usuario y partida de sesion, para no cargar tantos datos de
// persistencia en sesion y no usar tanta memoria
@SessionAttributes({"id_usuarioSesion", "id_partidaSesion", "posiblesinvitados", "rol"})
@Slf4j
public class ControladorVista {   
    
    @Value("${custom.server.websocket}")
    private String serverWebsocket;
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
    @Autowired
    OpcionTituloTmpServicioMetodos servOpTitulo; 
    @Autowired
    OpcionInterpreteTmpServicioMetodos servOpInterprete;  
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private Usuario usuarioModelo(Model modelo){
        
        Long id_usu = (Long) modelo.getAttribute("id_usuarioSesion");        
        Usuario usuarioSesion = null;
        try{
            usuarioSesion = servUsuario.findById(id_usu).get();
        }catch(Exception ex){}
        
        return usuarioSesion;        
    }
    
    private void informarUsuarioModelo(Model modelo, Usuario usuario){        
        
        modelo.addAttribute("usuarioSesion", usuario);        
    }
    
    private Partida partidaModelo(Model modelo){
        
        Long id_part = (Long) modelo.getAttribute("id_partidaSesion");
        Partida partidaSesion = null;
        try{
            partidaSesion = servPartida.findById(id_part).get();
        }catch(Exception ex){}
        
        return partidaSesion;        
    }
    
    private void informarPartidaModelo(Model modelo, Partida partida){        
        
        modelo.addAttribute("partidaSesion", partida);        
    }  
    
    @GetMapping("/")
    public String inicio(Model modelo){
        return "Inicio";
    }    
  
    @GetMapping("/logout")
    public String logout(Model modelo){
        modelo.addAttribute("id_usuarioSesion", ""); 
        modelo.addAttribute("id_partidaSesion", "");         
        return "Inicio";
    }    
    
    @PostMapping("/login") 
    public String login(@ModelAttribute("elUsuario") String elUsuario, 
            @ModelAttribute("laContrasenya") String laContrasenya, Model modelo){        
        
        //String passEncrip = passwordEncoder.encode(laContrasenya);       
        //Optional<Usuario> usuLogin = servUsuario.usuarioLogin(elUsuario, passEncrip);
        
        Optional<Usuario> usuLogin =servUsuario.findByUsuario(elUsuario);         
        
        if (usuLogin.isEmpty()){
            modelo.addAttribute("error", "Usuario o password incorrectos");
            return "Inicio";            
        }else{
            Usuario usuarioSesion = usuLogin.get();
            
            boolean ok = passwordEncoder.matches(laContrasenya, usuarioSesion.getContrasenya());         
            
            if (!ok){
                modelo.addAttribute("error", "Usuario o password incorrectos");
                return "Inicio";    
            }
            
            usuarioSesion.getPartidasInvitado();
            usuarioSesion.getPartidasMaster();      
            modelo.addAttribute("id_usuarioSesion", usuarioSesion.getId());
            informarUsuarioModelo(modelo, usuarioSesion);
            return "Panel";
        }
    }
        
    @GetMapping("/panel")
    public String panel(Model modelo){  
        
        Usuario usu = usuarioModelo(modelo);
        if (usu == null)
            return "redirect:/";
        informarUsuarioModelo(modelo, usu);
        
        return "Panel";        
    }    
   
    @GetMapping("/partidaMaster")
    public String partidaMaster(Model modelo){
        
        Usuario usu = usuarioModelo(modelo);
        if (usu == null)
            return "redirect:/";
        modelo.addAttribute("rol", Rol.master);
        Partida partida = usu.partidaMasterEnCurso();
        
        return partida(modelo, partida);        
    }
    
    @GetMapping("/partidaInvitado/{id}")
    public String partidaInvitado(Model modelo, @PathVariable Long id){
        
        Usuario usu = usuarioModelo(modelo);
        if (usu == null)
            return "redirect:/";
        modelo.addAttribute("rol", Rol.invitado);
        
        Partida partida = null;
        for (Partida p : usu.getPartidasInvitado()){
            if (p.getId().equals(id))
                partida = p;
        }
        
        return partida(modelo, partida);        
    }
     
    public String partida(Model modelo, Partida partida){
        
        Usuario usu = usuarioModelo(modelo); 
        if (usu == null)
            return "redirect:/";
        informarPartidaModelo(modelo, partida);
        if (partida == null)
            return "redirect:/panel";
        informarUsuarioModelo(modelo, usu);
        Ronda rondaActual = partida.getRondas().get(partida.getRondaActual() -1 );
        List<OpcionTituloTmp> opcTitulos = 
                servOpTitulo.findByPartidaRonda(partida.getId(),rondaActual.getId() );
        List<OpcionInterpreteTmp> opcInterpretes = 
                servOpInterprete.findByPartidaRonda(partida.getId(),rondaActual.getId() );        
        modelo.addAttribute("serverWebsocket", this.serverWebsocket);
        modelo.addAttribute("opcTitulos", opcTitulos);
        modelo.addAttribute("opcInterpretes", opcInterpretes);
        modelo.addAttribute("id_partidaSesion", partida.getId());
        modelo.addAttribute("respuestas", partida.respuestasUsuario(usu));
        return "Partida";
    }
    
    @GetMapping("/partida")
    public String partida(Model modelo){ 
        
        Partida partida = partidaModelo(modelo);
        return partida(modelo, partida);
    } 
    
    @GetMapping("/altaUsuario")
    public String altaUsuario(Model modelo){
        
        modelo.addAttribute("newusuario", new Usuario());
        return "AltaUsuario";        
    }
    
     @PostMapping("/altaUsuario")
    public String altaUsuario(@ModelAttribute("newusuario") Usuario usuario, Model modelo){
        
        String resp = "OK Se ha creado el usuario ".concat(usuario.getUsuario());
        String passEncrip = passwordEncoder.encode(usuario.getContrasenya());
        
        Set<UsuarioRol> roles = new HashSet();
        UsuarioRol usurol = new UsuarioRol();
        usurol.setName(Roles.USER);        
        roles.add(usurol);
        
        try{
            usuario.setRoles(roles);
            usuario.setActivo(false);
            usuario.setContrasenya(passEncrip); 
            usuario.setPreferencias("");
            servUsuario.save(usuario);
        }catch(Exception ex){
            resp = "ERROR " + ex; 
        }   
        
        modelo.addAttribute("result", resp);        
        return "AltaUsuario";        
    }  
    
    @PostMapping("/crearUsuario")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody CrearUsuarioDTO crearUsuarioDTO){
        
        Set<UsuarioRol> roles = crearUsuarioDTO.getRoles().stream()
                .map(role -> UsuarioRol.builder()
                        .name(Roles.valueOf(role))
                        .build())
                        .collect(Collectors.toSet());
        
        Usuario usuario = Usuario.builder()
                .usuario(crearUsuarioDTO.getUsuario())
                .contrasenya(passwordEncoder.encode(crearUsuarioDTO.getContrasenya()))
                .alias(crearUsuarioDTO.getAlias())
                .grupo(crearUsuarioDTO.getGrupo())
                .idioma(crearUsuarioDTO.getIdioma())
                .preferencias(crearUsuarioDTO.getPreferencias())
                .roles(roles)
                .build();
        servUsuario.save(usuario);
                
        return ResponseEntity.ok(usuario);
    }
    
    @DeleteMapping("/borrarUsuario")
    public String borrarUsuario(@RequestParam String id){
        
        servUsuario.deleteById(Long.valueOf(id));
        return "Se borra usuario id".concat(id);
        
    }  
    
   

    @GetMapping("/modificarUsuario")
    public String modificarUsuario(Model modelo){  
        
        Usuario usu = usuarioModelo(modelo);
        if (usu == null)
            return "redirect:/";
        informarUsuarioModelo(modelo, usu);        
        return "ModificarUsuario";        
    }
    
    @PostMapping("/modificarUsuario")
    public String modificarUsuario(@ModelAttribute("usuarioSesion") Usuario usuario, Model modelo){
        
        String resp = "OK";
        
        try{
            servUsuario.update(usuario.getId(), usuario);
        }catch(Exception ex){
            resp = "ERROR " + ex; 
        }   
        
        modelo.addAttribute("result", resp);        
        return "ModificarUsuario";        
    }  
    
    private void anyadirTemas(Model modelo){
        
        ArrayList<String> temas = new ArrayList();
        temas.add("");
        for (Tema tema : servTema.findAll())
            temas.add(tema.getTema());
        modelo.addAttribute("temas", temas);        
    }
    
    private void anyadirIdiomas(Model modelo){
        
        ArrayList<String> elems = new ArrayList();
        elems.add("");
        for (Idioma elem :Idioma.values())
            elems.add(elem.toString());
        modelo.addAttribute("idiomas", elems);        
    }
    
    
    @GetMapping("/partida/crear")
    public String crearPartida(Model modelo){     
        
        Calendar fecha = Calendar.getInstance();
        
        Usuario usu = usuarioModelo(modelo);
        if (usu == null)
            return "redirect:/";
        Partida newPartida = new Partida();
        newPartida.setAnyoInicial(1950);
        newPartida.setAnyoFinal(fecha.get(Calendar.YEAR) -1);
        newPartida.setGrupo(usu.getGrupo());
        modelo.addAttribute("newpartida", newPartida); 
        anyadirTemas(modelo);
        anyadirIdiomas(modelo);
        ArrayList<Usuario> posiblesInvitados = (ArrayList<Usuario>) usuariosGrupo(usu);

        if (!posiblesInvitados.isEmpty())
            modelo.addAttribute("posiblesinvitados", posiblesInvitados);      
        else
            modelo.addAttribute("posiblesinvitados", null); 
   
        informarUsuarioModelo(modelo, usu);
        return "CrearPartida";        
    }
    
       
    @PostMapping("/partida/crear")
    public String crearPartida(@ModelAttribute("newpartida") Partida partida, 
            @ModelAttribute("nrondas") Integer nrondas, Model modelo,  HttpServletRequest req){           
        
        Usuario usu = usuarioModelo(modelo);
        if (usu == null)
            return "redirect:/";
        Calendar cal= Calendar.getInstance();
        int anyoActual = cal.get(Calendar.YEAR);

        try{            
            // Validaciones
            if (!usu.sePuedeCrearPartidaMaster())
                throw new Exception("Ya tienes una partida Master creada, no se pueden crear mas");
            if (partida.getAnyoFinal() <= partida.getAnyoInicial())
                throw new Exception("Las Fechas Iniciales y Finales no son correctas");            
            if (partida.getAnyoFinal() > anyoActual)
                throw new Exception("El año final es erroneo");
            if (partida.getAnyoInicial() < 1950)
                throw new Exception("El año inicial es erroneo");
            if (nrondas < 5 || nrondas > 30)
                throw new Exception("Las rondas deben estar entre 5 y 30");
            
            List<Cancion> canciones = servCancion.obtenerCanciones(partida);
            
            if (canciones.size() < nrondas){           
                throw new Exception("No hay suficientes canciones, cambia la seleccion");
            } 
            
            partida.setInvitados(new ArrayList());
        
            ArrayList<Usuario> posiblesInvitados = (ArrayList<Usuario>) modelo.getAttribute("posiblesinvitados");
            if (posiblesInvitados != null){
                for (Usuario usuarioInv : posiblesInvitados){            

                    String valor = req.getParameter(usuarioInv.nombreId());            
                    if ("on".equals(valor)){

                        Optional<Usuario> usuario = servUsuario.findById(usuarioInv.getId());
                        if (!usuario.isEmpty()){                        
                            usuario.get().getPartidasInvitado().add(partida); 
                            partida.getInvitados().add(usuario.get());                                               
                        }
                    }
                }
            }
            
            partida.setMaster(usu);
            partida.setRondaActual(1);
            Partida newPartida = servPartida.savePartida(partida);
            usu.getPartidasMaster().add(newPartida);
            for (Usuario usuPartida: partida.getInvitados())
                servUsuario.update(usuPartida.getId(), usuPartida);            
            
            //crear las rondas con nrondas
            partida.setRondas(new ArrayList());
            for (int i = 1; i <= nrondas; i++){
                Ronda newRonda = new Ronda();
                newRonda.setNumero(i);
                newRonda.setPartida(partida); 
                newRonda.setRespuestas(new ArrayList());
                Ronda ronda = servRonda.saveRonda(newRonda);
                //Crear las respuestas
                for (Usuario usuario : partida.usuariosPartida()){ 
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
            for (Ronda ronda : partida.getRondas()){                
                servRonda.updateRonda(ronda.getId(), ronda);
            }            
            partida.setStatus(StatusPartida.EnCurso);
            servPartida.updatePartida(partida.getId(), partida);
            
            // Crear las opciones para las respuestas
            for (Ronda ronda : partida.getRondas()){
                for (OpcionTituloTmp op : opcionesTitulosCanciones(ronda))
                    servOpTitulo.saveOpcionTituloTmp(op);
                for (OpcionInterpreteTmp op : opcionesInterpretesCanciones(ronda))
                    servOpInterprete.saveOpcionInterpreteTmp(op);  
            }
            
        }catch(Exception ex){
            String resp = "ERROR " + ex.getMessage();
            modelo.addAttribute("result", resp); 
            anyadirTemas(modelo);
            return "CrearPartida";
        }
        modelo.addAttribute("id_partidaSesion", partida.getId()); 
        return "redirect:/partida";        
    }  
    
    private void resultadosPartida(Partida partidaSesion, Model modelo){
        
        HashMap<String,List<Respuesta>> resultadosPartida = new HashMap();
        HashMap<String,Integer> totales = new HashMap();
        String nomUsu;
        ArrayList lista;        
         
        for (Ronda ronda : partidaSesion.getRondas()){            
            for (Respuesta respuesta : ronda.getRespuestas()){
                nomUsu = respuesta.getUsuario().getNombre();
                if (!resultadosPartida.containsKey(nomUsu))
                    resultadosPartida.put(nomUsu, new ArrayList());
                lista = (ArrayList) resultadosPartida.get(nomUsu);
                lista.add(respuesta);                
                resultadosPartida.put(nomUsu, lista);
            }
        }        
        //Crear la suma total
        for (String usu : resultadosPartida.keySet() ){
            int total = 0;
            for (Respuesta resp : resultadosPartida.get(usu))
                total = total + resp.getPuntos();
            totales.put(usu, total);
        }
        
        modelo.addAttribute("ptstotales", totales);
        modelo.addAttribute("resultados", resultadosPartida);        
    }
        
    @GetMapping("/partidaConsulta/{id}")
    public String partidaConsulta(@PathVariable Long id, Model modelo){        
        
        Partida partidaSesion = null;
        try {
            Optional<Partida> partida = servPartida.findById(id);
            partidaSesion = partida.get();
        } catch (Exception e) {
        }
        
        if (partidaSesion == null)
            return "redirect:/";
        resultadosPartida(partidaSesion, modelo);        
        informarPartidaModelo(modelo, partidaSesion);        
        
        return "ResultadosPartida";        
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
}

