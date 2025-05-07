
package mld.clubdeportivo.base.quinielas;

import java.util.Date;
import mld.clubdeportivo.base.*;
import static mld.clubdeportivo.base.Deporte.Quiniela;

/**
 *
 * @author Miguel
 */
public class CompeticionQuiniela extends Competicion {


    public CompeticionQuiniela(){}

    public CompeticionQuiniela(String nombre){

        var grp = new Grupo();

        this.setGrupo(grp.grupoQuiniela());
        this.setNombre(nombre);
        this.setTipo(Quiniela);
        this.setActiva(true);
        this.setProximaJornada(0);
        this.setUltimaJornada(0);
        this.setFecha(new Date());

    }
    


}
