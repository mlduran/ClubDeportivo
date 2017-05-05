
package mld.clubdeportivo.base.quinielas;

import java.util.Date;
import mld.clubdeportivo.base.*;

/**
 *
 * @author Miguel
 */
public class CompeticionQuiniela extends Competicion {


    public CompeticionQuiniela(){}

    public CompeticionQuiniela(String nombre){

        Grupo grp = new Grupo();

        this.setGrupo(grp.grupoQuiniela());
        this.setNombre(nombre);
        this.setTipo(Deporte.Quiniela);
        this.setActiva(true);
        this.setProximaJornada(0);
        this.setUltimaJornada(0);
        this.setFecha(new Date());

    }
    


}
