package mld.clubdeportivo.base;

import java.util.Date;

/**
 *
 * @author Miguel
 */

public final class Noticia extends Objeto{

    private Grupo grupo;
    private Deporte deporte;
    private Date fecha;
    private String noticia;
    private boolean general;


    public Noticia(){}

    public Noticia(Grupo grp, Deporte deporte, String noticia, boolean isgeneral){

        this.setGrupo(grp);
        this.setFecha(new Date());
        this.setNoticia(noticia);
        this.setDeporte(deporte);
        this.setGeneral(isgeneral);

    }
    
    public Noticia(Grupo grp, String noticia){
        
        this(grp, null, noticia, false);

    }
    
    public Noticia(Grupo grp, String noticia, boolean isgeneral){
        
        this(grp, null, noticia, isgeneral);

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

    public String getNoticia() {
        return noticia;
    }

    public void setNoticia(String noticia) {
        this.noticia = noticia;
    }

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte) {
        this.deporte = deporte;
    }

    public boolean isGeneral() {
        return general;
    }

    public void setGeneral(boolean general) {
        this.general = general;
    }

   
   
}

