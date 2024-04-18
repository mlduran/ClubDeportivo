
package mld.clubdeportivo.base.basket;

import java.util.ArrayList;
import mld.clubdeportivo.base.Jornada;

/**
 *
 * @author Miguel
 */
public class JornadaBasket extends Jornada{

    private ArrayList<PartidoBasket> partidos;

    public ArrayList<PartidoBasket> getPartidos() {
        return partidos;
    }

    public void setPartidos(ArrayList<PartidoBasket> partidos) {
        this.partidos = partidos;
    }



}
