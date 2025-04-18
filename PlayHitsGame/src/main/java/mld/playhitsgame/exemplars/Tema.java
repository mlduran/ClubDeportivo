/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author miguel
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "temas")
public class Tema {

    public static final int MIN_CANCIONES = 25;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String tema;
    @Enumerated(EnumType.STRING)
    private Idioma idioma;
    @Enumerated(EnumType.STRING)
    private Genero genero;
    @ManyToMany(mappedBy = "tematicas")
    private List<Cancion> canciones;
    @ManyToMany(mappedBy = "tematicas")
    private List<CancionTmp> cancionesTmp;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    private String listasSpotify;
    //private Long usuarioRecord; // para eliminar
    @OneToOne
    @JoinColumn(name = "record_usuario_id", nullable = true) 
    private Usuario recordUsuario;
    
    private int puntos;
    private int nPartidas;
    private boolean activo;

    public String getNumCanciones() {

        return String.valueOf(this.getCanciones().size());
    }
    
    public String getNumCancionesTmp() {

        return String.valueOf(this.getCancionesTmp().size());
    }
    

    public String getNumCancionesValidadas() {

        int n = 0;
        for (Cancion cancion : this.getCanciones()) {
            if (!cancion.isRevisar()) {
                n = n + 1;
            }
        }
        return String.valueOf(n);
    }
    
    public String getDescripcion(){
        
        return this.getTema() + " - " + this.getNumCancionesValidadas() +
                " tracks";
    }

    public String getDatos() {

        if (this.getGenero() == null || this.getIdioma() == null) {
            return "";
        } else {
            return this.getGenero() + "-" + this.getIdioma();
        }

    }
}
