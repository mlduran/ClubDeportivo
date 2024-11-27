package mld.playhitsgame.exemplars;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
@Table(name = "config")
public final class Config  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ipRouter;
    private String usuarioBD;
    private String passwBD;
    private String usuarioMail;
    private String passMail;
    private String mensajeInicio_es;
    private String mensajeInicio_en;
    private boolean mantenimiento;
    private LocalDateTime fechaMantenimiento;

}

