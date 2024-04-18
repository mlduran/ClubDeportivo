
package mld.clubdeportivo.base.basket;

import mld.clubdeportivo.base.JugadorMaestro;

/**
 *
 * @author Miguel
 */
public final class JugadorMaestroBasket extends JugadorMaestro{

    public static int NUMERO_MAX_JUGADORES = 288; // Es lo que hay en BD 
    private PosicionJugBasket Posicion;


    public JugadorMaestroBasket(String nombre, PosicionJugBasket posicion){

        this.setNombre(nombre);
        this.setPosicion(posicion);

    }

    public JugadorMaestroBasket(String nombre, String posicion) {

        this(nombre, PosicionJugBasket.valueOf(posicion));
    }


    public PosicionJugBasket getPosicion() {
        return Posicion;
    }

    public void setPosicion(PosicionJugBasket Posicion) {
        this.Posicion = Posicion;
    }


    @Override
    public String toString() {

        StringBuilder txt = new StringBuilder();

        txt.append("JUGADOR MAESTRO\n");
        txt.append(String.format("- Id: %d \n", this.getId()));
        txt.append(String.format("- Nombre: %s \n", this.getNombre()));
        txt.append(String.format("- Posicion: %s \n", this.getPosicion()));

        return txt.toString();
    }

}
