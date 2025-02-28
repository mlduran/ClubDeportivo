package mld.clubdeportivo.base;

import java.util.ArrayList;
import java.util.Date;
import static mld.clubdeportivo.base.ClaseMovimiento.AmpliacionEstadio;
import static mld.clubdeportivo.base.ClaseMovimiento.Blindajes;
import static mld.clubdeportivo.base.ClaseMovimiento.CapitalInicial;
import static mld.clubdeportivo.base.ClaseMovimiento.ConcesionCredito;
import static mld.clubdeportivo.base.ClaseMovimiento.Cursos;
import static mld.clubdeportivo.base.ClaseMovimiento.DevolucionCredito;
import static mld.clubdeportivo.base.ClaseMovimiento.DevolucionHacienda;
import static mld.clubdeportivo.base.ClaseMovimiento.Fichajes;
import static mld.clubdeportivo.base.ClaseMovimiento.Fichas;
import static mld.clubdeportivo.base.ClaseMovimiento.Gestion;
import static mld.clubdeportivo.base.ClaseMovimiento.IBI;
import static mld.clubdeportivo.base.ClaseMovimiento.Indemnizacion;
import static mld.clubdeportivo.base.ClaseMovimiento.IngresoPublicidad;
import static mld.clubdeportivo.base.ClaseMovimiento.IngresoTaquilla;
import static mld.clubdeportivo.base.ClaseMovimiento.InversionBolsa;
import static mld.clubdeportivo.base.ClaseMovimiento.Mantenimiento;
import static mld.clubdeportivo.base.ClaseMovimiento.PagoHacienda;
import static mld.clubdeportivo.base.ClaseMovimiento.PremioCompeticion;
import static mld.clubdeportivo.base.ClaseMovimiento.Primas;
import static mld.clubdeportivo.base.ClaseMovimiento.RegularizacionNegativa;
import static mld.clubdeportivo.base.ClaseMovimiento.RegularizacionPositiva;
import static mld.clubdeportivo.base.ClaseMovimiento.Renovaciones;
import static mld.clubdeportivo.base.ClaseMovimiento.RescateBolsa;
import static mld.clubdeportivo.base.ClaseMovimiento.SaldoActual;
import static mld.clubdeportivo.base.ClaseMovimiento.VentaJugadores;
import static mld.clubdeportivo.base.ClaseMovimiento.premioFairPlay;

/**
 *
 * @author Miguel
 */

public final class Movimiento extends Objeto{

    private Equipo equipo;
    private Date fecha;
    private ClaseMovimiento clase;
    private int valor;
    private String descripcion;
    private int saldo;


    public Movimiento(){}

    public Movimiento(Equipo equipo, ClaseMovimiento clase, int valor,
            String descripcion){

        this.setEquipo(equipo);
        this.setFecha(new Date());
        this.setClase(clase);
        this.setValor(valor);
        this.setDescripcion(descripcion);

    }

    public Movimiento(Equipo equipo, ClaseMovimiento clase, int valor){

        this(equipo, clase, valor, "");
    }
    
    public String getClaseTxtSimple(){
        
        var txt = "";
 
        if (this.getClase() == SaldoActual)
            txt = "Saldo Actual";
        else if (this.getClase() == CapitalInicial)
            txt = "Capital Inicial";
        else if (this.getClase() == IngresoTaquilla)
            txt = "Ingreso Taquilla";
        else if (this.getClase() == IngresoPublicidad)
            txt = "Ingreso Publicidad";
        else if (this.getClase() == Indemnizacion)
            txt = "Indemnizaciones";
        else if (this.getClase() == Blindajes)
            txt = "Blindajes";
        else if (this.getClase() == Renovaciones)
            txt = "Renovaciones";
        else if (this.getClase() == Fichas)
            txt = "Fichas";
        else if (this.getClase() == Mantenimiento)
            txt = "Gastos Mantenimiento";
        else if (this.getClase() == Gestion)
            txt = "Gastos Gestión";
        else if (this.getClase() == Fichajes)
            txt = "Fichajes";
        else if (this.getClase() == VentaJugadores)
            txt = "Venta Jugadores";
        else if (this.getClase() == InversionBolsa)
            txt = "Inversiones en Bolsa";
        else if (this.getClase() == RescateBolsa)
            txt = "Venta en Bolsa";
        else if (this.getClase() == ConcesionCredito)
            txt = "Concesión Credito";
        else if (this.getClase() == DevolucionCredito)
            txt = "Devolución Credito";
        else if (this.getClase() == AmpliacionEstadio)
            txt = "Ampliación en Estadio/Pavellon";
        else if (this.getClase() == Primas)
            txt = "Primas";
        else if (this.getClase() == Cursos)
            txt = "Cursos";
        else if (this.getClase() == premioFairPlay)
            txt = "Premio al Fair Play";
        else if (this.getClase() == RegularizacionPositiva)
            txt = "Regularización";
        else if (this.getClase() == RegularizacionNegativa)
            txt = "Regularización";
        else if (this.getClase() == PagoHacienda)
            txt = "Pago Hacienda";
        else if (this.getClase() == DevolucionHacienda)
            txt = "Devolucion Hacienda";
        else if (this.getClase() == PremioCompeticion)
            txt = "Premio Competición";
        else if (this.getClase() == IBI)
            txt = "Impueto Bienes Inmuebles (IBI)";
        
        return txt;
        
    }
    

    public String getClaseTxt(){

        var txt = getClaseTxtSimple();  

        if (this.getDescripcion() != null && !this.getDescripcion().equals(""))
            txt = txt.concat(" (" + this.getDescripcion() + ")" );

        return txt;
    }

    public boolean isPositivo(){

        var result = false;

        if (
                this.getClase() == IngresoTaquilla ||
                this.getClase() == IngresoPublicidad ||
                this.getClase() == VentaJugadores ||
                this.getClase() == RescateBolsa ||
                this.getClase() == ConcesionCredito ||
                this.getClase() == CapitalInicial ||
                this.getClase() == premioFairPlay ||
                this.getClase() == RegularizacionPositiva ||
                this.getClase() == DevolucionHacienda ||
                this.getClase() == PremioCompeticion 
                )            
                
            result = true;

        return result;
        
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public ClaseMovimiento getClase() {
        return clase;
    }

    public void setClase(ClaseMovimiento clase) {
        this.clase = clase;
    }

   
    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
    
    public static ArrayList<Movimiento> obtenerExtracto(
            Equipo eq, ArrayList<Movimiento> movs){
        
        var lista = new ArrayList<Movimiento>();
        
        if (!movs.isEmpty()){
            var saldo = eq.getPresupuesto();
            var movIni = new Movimiento(eq, SaldoActual, 0);
            movIni.setSaldo(saldo);
            lista.add(movIni); 

            for (var mov : movs) {
                if (!mov.isPositivo())
                    mov.setValor(- mov.getValor());
                saldo = saldo - mov.getValor();
                lista.add(mov);
                mov.setSaldo(saldo);
            }
        }      
        
        return lista;    
        
    }



   
}

