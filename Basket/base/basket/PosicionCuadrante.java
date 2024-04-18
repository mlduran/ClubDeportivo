package mld.clubdeportivo.base.basket;

/**
 *
 * @author Miguel
 */
public final class PosicionCuadrante {

    private String posicion;
    private boolean activa;

    public PosicionCuadrante(String pos, boolean act){

        this.setPosicion(pos);
        this.setActiva(act);
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

}
