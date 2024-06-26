package mld.clubdeportivo.base.basket;


import mld.clubdeportivo.base.Puntuacion;

/**
 *
 * @author Miguel
 */
public class PuntuacionBasket extends Puntuacion{

    private EquipoBasket equipo;
    private int victorias;
    private int empates;
    private int derrotas;
    private int golesFavor;
    private int golesContra;

    public EquipoBasket getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoBasket equipo) {
        this.equipo = equipo;
    }

    public int getVictorias() {
        return victorias;
    }

    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public int getEmpates() {
        return empates;
    }

    public void setEmpates(int empates) {
        this.empates = empates;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public int getGolesFavor() {
        return golesFavor;
    }

    public void setGolesFavor(int golesFavor) {
        this.golesFavor = golesFavor;
    }

    public int getGolesContra() {
        return golesContra;
    }

    public void setGolesContra(int golesContra) {
        this.golesContra = golesContra;
    }

    public int getDiferenciaGoles(){
        return this.getGolesFavor() - this.getGolesContra();
    }

}
