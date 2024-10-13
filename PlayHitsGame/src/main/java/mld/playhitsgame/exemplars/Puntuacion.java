package mld.playhitsgame.exemplars;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Miguel
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "puntuaciones")
public final class Puntuacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Tema tema;
    private int puntos;
    private Long idUsuario;
    private TipoPartida tipoPartida;
    private Dificultad dificultad;
    private int anyoInicial;
    private int anyoFinal;

}
