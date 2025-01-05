/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import mld.playhitsgame.seguridad.UsuarioRol;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mld.playhitsgame.seguridad.Roles;
import mld.playhitsgame.utilidades.Utilidades;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author miguel
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @Email
    @NotBlank
    private String usuario;
    private boolean activo;
    private String alias;
    @NotBlank
    private String contrasenya;
    private String grupo;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    private boolean dobleTouch;
    private int segEspera;
    private int puntos;
    private int estrellas;

    @OneToMany(mappedBy = "usuario")
    private List<Tema> temas;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = UsuarioRol.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<UsuarioRol> roles;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date alta;

    @OneToMany(mappedBy = "master")
    private List<Partida> partidasMaster;

    @OneToMany(mappedBy = "usuario")
    private List<Respuesta> respuestas;

    @ManyToMany
    @JoinTable(
            name = "usuario_partida",
            joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "partida_id", referencedColumnName = "id")
    )
    private List<Partida> partidasInvitado;

    public List<Partida> batallas() {
        
        List<Partida> batallas = new ArrayList<>(this.getPartidasInvitado());
        batallas.addAll(this.getPartidasMaster());        

        // Filtrar solo las partidas del tipo batalla
        return batallas.stream()
                .filter(Partida::isTipoBatalla)
                .collect(Collectors.toList());
    }

    public String getActivoTxt() {

        if (this.activo) {
            return "Si";
        } else {
            return "No";
        }
    }

    public String getRolesTxt() {

        String txt = "";
        boolean primero = true;
        for (UsuarioRol rol : this.getRoles()) {
            if (primero) {
                primero = false;
            } else {
                txt = txt + ",";
            }
            if (rol.getName() != null) {
                txt = txt + rol.getName().name();
            }
        }
        return txt;
    }

    public String selId() {

        return "sel_" + String.valueOf(this.getId());
    }

    public boolean sinGrupo() {
        return this.getGrupo() == null || "".equals(this.getGrupo());
    }

    public boolean isAdmin() {

        boolean adm = false;
        for (UsuarioRol rol : this.roles) {
            if (rol.getName().equals(Roles.ADMIN)) {
                adm = true;
                break;
            }
        }
        return adm;
    }

    public boolean isColaborador() {

        boolean adm = false;
        for (UsuarioRol rol : this.roles) {
            if (rol.getName().equals(Roles.COLABORADOR)) {
                adm = true;
                break;
            }
        }
        return adm;
    }

    public String getNombre() {

        String nombre;

        if (this.getAlias() == null || this.getAlias().isBlank()) {
            String[] x = this.getUsuario().split("@");
            nombre = x[0];
        } else {
            nombre = this.getAlias();
        }

        return nombre;
    }

    public String nombreId() {

        return this.getNombre() + this.getClass().toString();
    }

    public Partida partidaMasterEnCurso() {

        Partida result = null;

        for (Partida elem : this.getPartidasMaster()) {
            if (elem.getTipo() != TipoPartida.grupo) {
                continue;
            }
            if (elem.getStatus() == StatusPartida.EnCurso) {
                result = elem;
                break;
            }
        }
        return result;
    }

    public Partida partidaPersonalEnCurso() {

        Partida result = null;

        for (Partida elem : this.getPartidasMaster()) {
            if (elem.getTipo() != TipoPartida.personal) {
                continue;
            }
            if (elem.getStatus() == StatusPartida.EnCurso) {
                result = elem;
                break;
            }
        }
        return result;
    }

    public boolean sePuedeCrearPartidaMaster() {

        return partidaMasterEnCurso() == null
                && partidaPersonalEnCurso() == null;

    }

    public List<Partida> partidasInvitadoPendientes() {

        List<Partida> result = new ArrayList<>();

        for (Partida elem : this.getPartidasInvitado()) {
            if (elem.getTipo() != TipoPartida.grupo) {
                continue;
            }
            if (elem.getStatus() == StatusPartida.EnCurso) {
                result.add(elem);
            }
        }
        return result;
    }

    public boolean hayPartidasInvitadoPendientes() {
        return !partidasInvitadoPendientes().isEmpty();
    }

    public List<Partida> partidasTerminadasGrupo() {

        List<Partida> result = new ArrayList<>();

        for (Partida elem : this.getPartidasMaster()) {
            if (elem.getTipo() != TipoPartida.grupo) {
                continue;
            }
            if (elem.getStatus() == StatusPartida.Terminada) {
                result.add(elem);
            }
        }

        for (Partida elem : this.getPartidasInvitado()) {
            if (elem.getTipo() != TipoPartida.grupo) {
                continue;
            }
            if (elem.getStatus() == StatusPartida.Terminada) {
                result.add(elem);
            }
        }

        Collections.sort(result, (Partida e1, Partida e2)
                -> e2.getFecha().compareTo(e1.getFecha()));

        return result;
    }

    public boolean hayPartidasTerminadasGrupo() {

        return !partidasTerminadasGrupo().isEmpty();
    }

    private boolean batallaCompletada(Partida partida) {
        return partida.getRondas().stream()
                .flatMap(r -> r.getRespuestas().stream())
                .filter(resp -> resp.getUsuario().equals(this))
                .allMatch(Respuesta::isCompletada);
    }

    private List<Partida> filtrarBatallasEnCurso(Predicate<Partida> condicion) {

        return this.batallas().stream()
                .filter(elem -> elem.getStatus() == StatusPartida.EnCurso)
                .filter(condicion)
                .collect(Collectors.toList());
    }

    public List<Partida> batallasEnCurso() {
        return filtrarBatallasEnCurso(this::batallaCompletada);
    }

    public List<Partida> batallasEnCursoPendientes() {
        return filtrarBatallasEnCurso(elem -> !batallaCompletada(elem));
    }

    public boolean hayBatallasEnCurso() {

        return !batallasEnCurso().isEmpty();
    }

    public boolean hayBatallasEnCursoPendientes() {

        return !batallasEnCursoPendientes().isEmpty();
    }

    public List<Partida> batallasTerminadas() {

        List<Partida> result = new ArrayList<>();

        for (Partida elem : this.getPartidasInvitado()) {
            if (elem.getTipo() != TipoPartida.batalla) {
                continue;
            }
            if (elem.getStatus() == StatusPartida.Terminada) {
                result.add(elem);
            }
        }
        return result;
    }

    public boolean hayBatallasTerminadas() {

        return !batallasTerminadas().isEmpty();
    }

    public List<Partida> partidasTerminadasPersonales() {

        List<Partida> result = new ArrayList<>();

        for (Partida elem : this.getPartidasMaster()) {
            if (elem.getTipo() != TipoPartida.personal) {
                continue;
            }
            if (elem.getStatus() == StatusPartida.Terminada) {
                result.add(elem);
            }
        }

        Collections.sort(result, (Partida e1, Partida e2)
                -> e2.getFecha().compareTo(e1.getFecha()));

        return result;
    }

    public boolean hayPartidasTerminadasPersonales() {

        return !partidasTerminadasPersonales().isEmpty();
    }

    public String getTxtGrupo() {

        if (this.getGrupo() == null || this.getGrupo().isBlank()) {
            return "Sin Informar";
        } else {
            return this.getGrupo();
        }
    }

    public int getPuntosPartida(Partida partida) {

        int pts = Utilidades.calcularPtsUsuario(this, partida, false);

        return pts;
    }

    public boolean isTieneTema() {

        return this.getTemas() != null && !this.getTemas().isEmpty();
    }

}
