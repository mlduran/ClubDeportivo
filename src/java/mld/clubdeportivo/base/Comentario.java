package mld.clubdeportivo.base;

import java.util.Date;
import mld.clubdeportivo.utilidades.StringUtil;

/**
 *
 * @author Miguel
 */

public final class Comentario extends Objeto{

    private Grupo grupo;
    private String club;
    private Date fecha;
    private String comentario;
    private boolean general;


    public Comentario(){}

    public Comentario(Grupo grp, String club, String coment, boolean isgeneral){

        this.setGrupo(grp);
        this.setClub(club);
        this.setFecha(new Date());
        this.setComentario(coment);
        this.setGeneral(isgeneral);

    }
    
    public Comentario(Grupo grp, String club, String coment){
        
        this(grp, club, coment, false);

    }

   
    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = StringUtil.removeCharsEspeciales(comentario);
    }

    public boolean isGeneral() {
        return general;
    }

    public void setGeneral(boolean general) {
        this.general = general;
    }

   
   
}

