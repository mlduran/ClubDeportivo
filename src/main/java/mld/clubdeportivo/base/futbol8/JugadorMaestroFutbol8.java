
package mld.clubdeportivo.base.futbol8;

import static java.lang.String.format;
import mld.clubdeportivo.base.JugadorMaestro;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.valueOf;

/**
 *
 * @author Miguel
 */
public final class JugadorMaestroFutbol8 extends JugadorMaestro{

    public static int NUMERO_MAX_JUGADORES = 288; // Es lo que hay en BD 
    private PosicionJugFutbol8 Posicion;


    public JugadorMaestroFutbol8(String nombre, PosicionJugFutbol8 posicion){

        this.setNombre(nombre);
        this.setPosicion(posicion);

    }

    public JugadorMaestroFutbol8(String nombre, String posicion) {

        this(nombre, valueOf(posicion));
    }


    public PosicionJugFutbol8 getPosicion() {
        return Posicion;
    }

    public void setPosicion(PosicionJugFutbol8 Posicion) {
        this.Posicion = Posicion;
    }


    @Override
    public String toString() {

        var txt = new StringBuilder();

        txt.append("JUGADOR MAESTRO\n");
        txt.append(format("- Id: %d \n", this.getId()));
        txt.append(format("- Nombre: %s \n", this.getNombre()));
        txt.append(format("- Posicion: %s \n", this.getPosicion()));

        return txt.toString();
    }

}
