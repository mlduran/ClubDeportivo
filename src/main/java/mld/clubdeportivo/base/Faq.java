package mld.clubdeportivo.base;

import java.util.Date;
import mld.clubdeportivo.utilidades.StringUtil;

/**
 *
 * @author Miguel
 */

public final class Faq extends Objeto{

    private long idClub;
    private Club club;
    private Date fecha;
    private String pregunta;
    private String respuesta;


    public Faq(){}

    public Faq(Club club, String pregunta){

        this.setClub(club);
        this.setFecha(new Date());
        this.setPregunta(pregunta);
        this.setRespuesta("");

    }
    
  
    public long getIdClub() {
        return idClub;
    }

    public void setIdClub(long idClub) {
        this.idClub = idClub;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

  
   
}

