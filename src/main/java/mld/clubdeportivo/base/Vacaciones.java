package mld.clubdeportivo.base;

public abstract class Vacaciones extends Objeto{


    private boolean activo;    

    protected Vacaciones(){

    };

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

 


}
