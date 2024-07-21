package mld.playhitsgame.exemplars;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
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
@Table(name = "registro")
public final class Registro  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TipoRegistro tipo;
    private String ip;
    private String usuario;
    private Date fecha;

}

