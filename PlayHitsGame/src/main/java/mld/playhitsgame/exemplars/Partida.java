/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author miguel
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "partidas")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private boolean publica;
    private String nombre;
    private int fase;    

    @Column(name = "fecha", nullable = false)
    @CreationTimestamp
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private StatusPartida status;

    @Enumerated(EnumType.STRING)
    private TipoPartida tipo;

    @Enumerated(EnumType.STRING)
    private Dificultad dificultad;
    
    @ManyToOne
    @JoinColumn(name = "batalla_id", nullable = true)
    private Batalla batalla;

    @ManyToOne// poner LAZY para no cargar hasta hacer un get
    private Usuario master;

    @ManyToMany(mappedBy = "partidasInvitado", fetch = FetchType.EAGER)
    private List<Usuario> invitados;

    @OneToMany(mappedBy = "partida", fetch = FetchType.EAGER) // poner LAZY para no cargar hasta hacer un get 
    private List<Ronda> rondas;

    private int rondaActual;
    private String tema; // por ejemplo Descripciones genericas 
    @Min(1950)
    private int anyoInicial;
    @Min(1950)
    private int anyoFinal;
    private int nCanciones;
    private String grupo;
    private String ganador;

    private boolean activarPlay;
    private boolean sinOfuscar;
    private boolean sonidos;
    
    public String getDescripcionLog(){
        
        return String.valueOf(this.getId() + " " +
                this.getTipo().name() + " " + this.getFecha().toString());
        
    }
    
    public String getCuentaAtras(){
        
        LocalDateTime actual = LocalDateTime.now();
        LocalDateTime fechaFin = this.getFecha().plusHours(24);
        Duration duration = Duration.between(actual, fechaFin);
        
        if (actual.isBefore(fechaFin)) {
            // Convertir la diferencia a horas y minutos
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;

            return hours + "h " + minutes + "m";
        } else {
            return "";
        }
    }

    public boolean isTipoGrupo() {

        return this.getTipo().equals(TipoPartida.grupo);

    }

    public boolean isTipoPersonal() {

        return this.getTipo().equals(TipoPartida.personal);

    }
    
    public boolean isTipoBatalla() {

        return this.getTipo().equals(TipoPartida.batalla);

    }

    public boolean isTerminada() {

        return this.status == StatusPartida.Terminada;

    }
    
    public boolean isEnCurso() {

        return this.status == StatusPartida.EnCurso;

    }

    public String fechaFormateada() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
        return this.getFecha().format(formatter);

    }

    public List<Respuesta> respuestasUsuario(Usuario usuario) {

        ArrayList<Respuesta> respuestas = new ArrayList();
        for (Ronda ronda : this.getRondas()) {
            for (Respuesta resp : ronda.getRespuestas()) {
                if (Objects.equals(usuario, resp.getUsuario())) {
                    respuestas.add(resp);
                }
            }
        }

        return respuestas;
    }

    public String getUsuariosTxT() {

        String txt = "";

        boolean primero = true;

        for (Usuario usu : this.usuariosPartida()) {
            if (primero) {
                primero = false;
            } else {
                txt = txt.concat(", ");
            }
            txt = txt.concat(usu.getNombre());
        }
        return txt;
    }

    public List<Descripcion> getDescripcion() {

        ArrayList<Descripcion> txt = new ArrayList();
        Descripcion des;
        if (this.getGrupo() != null && !this.getGrupo().isEmpty()) {
            des = new Descripcion();
            des.setEtiqueta("general.grup");
            des.setValor(this.getGrupo());
            txt.add(des);
        }
        if (this.getTema() != null && !this.getTema().isEmpty()) {
            des = new Descripcion();
            des.setEtiqueta("general.tema");
            des.setValor(this.getTema());
            txt.add(des);
        }
        if (this.nCanciones > 0) {
            des = new Descripcion();
            des.setEtiqueta("general.canciones");
            des.setValor(String.valueOf(this.nCanciones));
            txt.add(des);
        }

        des = new Descripcion();
        des.setEtiqueta("general.anyoini");
        des.setValor(String.valueOf(String.valueOf(this.getAnyoInicial())));
        txt.add(des);
        des = new Descripcion();
        des.setEtiqueta("general.anyofin");
        des.setValor(String.valueOf(String.valueOf(this.getAnyoFinal())));
        txt.add(des);

        return txt;
    }

    public Ronda rondaActiva() {

        return this.rondas.get(this.rondaActual - 1);
    }

    public Ronda ultimaRonda() {

        Ronda ultima = null;
        List<Ronda> lasRondas = this.getRondas();
        if (lasRondas != null && !lasRondas.isEmpty()) {
            ultima = lasRondas.get(lasRondas.size() - 1);
        }

        return ultima;
    }

    public List<Usuario> usuariosPartida() {

        ArrayList<Usuario> lista = new ArrayList();

        lista.add(this.getMaster());
        if (!this.getInvitados().isEmpty()) {
            lista.addAll(this.getInvitados());
        }

        return lista;
    }

    public void pasarSiguienteRonda() {
        this.setRondaActual(this.getRondaActual() + 1);
    }

    public boolean hayMasRondas() {
        return this.getRondaActual() < this.getRondas().size();
    }

    public int ptsUsuario(Usuario usuario) {

        int pts = 0;

        for (Ronda ronda : this.getRondas()) {
            for (Respuesta resp : ronda.getRespuestas()) {
                if (Objects.equals(resp.getUsuario().getId(), usuario.getId())) {
                    pts = pts + resp.getPuntos();
                }
            }
        }
        return pts;
    }

    public int getPtsUsuarioPartidaPersonal() throws Exception {

        int pts = 0;

        if (!this.isTipoPersonal()) {
            throw new Exception("Este metodo solo es para partidas personales sino utilizar ptsUsuario(Usuario usuario) ");
        }

        for (Ronda ronda : this.getRondas()) {
            for (Respuesta resp : ronda.getRespuestas()) {
                pts = pts + resp.getPuntos();
            }
        }
        return pts;
    }

    public void asignarGanador() {

        String ganadorPartida = "";
        int ptsGanador = 0;

        for (Usuario usuario : this.usuariosPartida()) {
            int ptsUsu = ptsUsuario(usuario);
            if (ptsUsu > ptsGanador) {
                ganadorPartida = usuario.getNombre();
                ptsGanador = ptsUsu;
            }
        }
        this.setGanador(ganadorPartida);
    }

    public List<Cancion> canciones() {

        ArrayList<Cancion> lista = new ArrayList();

        for (Ronda ronda : this.getRondas()) {
            lista.add(ronda.getCancion());
        }

        return lista;

    }

    public boolean isEntreno() {

        return this.getDificultad().equals(Dificultad.Entreno);

    }    
    
    
}
