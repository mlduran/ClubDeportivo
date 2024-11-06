package mld.playhitsgame.exemplars;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mld.playhitsgame.utilidades.Utilidades;

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
    @Enumerated(EnumType.STRING)
    private TipoRegistro tipo;
    private String ip;
    private String usuario;
    private Date fecha;
    private String pais;
    
    
    public String getPaisServicio(String ip){
        
        return Utilidades.getCountryFromIP(ip);
      
    }

}

