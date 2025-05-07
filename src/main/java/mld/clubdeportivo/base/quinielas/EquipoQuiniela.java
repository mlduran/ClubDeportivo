package mld.clubdeportivo.base.quinielas;

import java.util.ArrayList;
import mld.clubdeportivo.base.Deporte;
import static mld.clubdeportivo.base.Deporte.Quiniela;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.Seccion;

/**
 *
 * @author Miguel
 */
public class EquipoQuiniela extends Seccion {
    
    public static final int DIAS_WARNING = 90;
    public static final int DIAS_DESACTIVAR = 120;

    private Deporte seccion = Quiniela;
    private ArrayList<ApuestaQuiniela> apuestas;
    private ArrayList<PuntuacionQuiniela> puntuaciones;
    private ArrayList<EstadisticaQuiniela> estadisiticas;
    private PuntuacionQuiniela puntuacion;
    private EstadisticaQuiniela estadisitica;
    private String resultadoProvisional;
    private boolean admin;

    public Grupo getGrupo(){

        var grp = new Grupo();

        return grp.grupoQuiniela();

    }

    public ArrayList<ApuestaQuiniela> getApuestas() {
        return apuestas;
    }

    public void setApuestas(ArrayList<ApuestaQuiniela> apuestas) {
        this.apuestas = apuestas;
    }

    public ArrayList<PuntuacionQuiniela> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(ArrayList<PuntuacionQuiniela> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public ArrayList<EstadisticaQuiniela> getEstadisiticas() {
        return estadisiticas;
    }

    public void setEstadisiticas(ArrayList<EstadisticaQuiniela> estadisiticas) {
        this.estadisiticas = estadisiticas;
    }

    public boolean equals(EquipoQuiniela eq) {

        return eq.getId() == this.getId();
    }

    public PuntuacionQuiniela getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacione(PuntuacionQuiniela puntuacion) {
        this.puntuacion = puntuacion;
    }

    public EstadisticaQuiniela getEstadisitica() {
        return estadisitica;
    }

    public void setEstadisitica(EstadisticaQuiniela estadisitica) {
        this.estadisitica = estadisitica;
    }

    public String getResultadoProvisional() {
        return resultadoProvisional;
    }

    public void setResultadoProvisional(String resultadoProvisional) {
        this.resultadoProvisional = resultadoProvisional;
    }


    public boolean isAdmin() {
        return admin;
    }


    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
   


}
