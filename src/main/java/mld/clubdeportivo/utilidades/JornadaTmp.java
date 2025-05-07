package mld.clubdeportivo.utilidades;

/**
 *
 * clase para crear jornadas temporales para un calendario de
 * liga
 */
public final class JornadaTmp {

    private int jornada;
    private Object eqLocal;
    private Object eqVisitante;

    public JornadaTmp(int jornada, Object eqLocal, Object eqVisitante){

        this.setJornada(jornada);
        this.setEqLocal(eqLocal);
        this.setEqVisitante(eqVisitante);
    }

    public int getJornada() {
        return jornada;
    }

    public void setJornada(int jornada) {
        this.jornada = jornada;
    }

    public Object getEqLocal() {
        return eqLocal;
    }

    public void setEqLocal(Object eqLocal) {
        this.eqLocal = eqLocal;
    }

    public Object getEqVisitante() {
        return eqVisitante;
    }

    public void setEqVisitante(Object eqVisitante) {
        this.eqVisitante = eqVisitante;
    }

}
