package mld.clubdeportivo.base;

import java.util.ArrayList;
import java.util.Date;

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
        
        String txt = "";
 
        if (this.getClase() == ClaseMovimiento.SaldoActual)
            txt = "Saldo Actual";
        else if (this.getClase() == ClaseMovimiento.CapitalInicial)
            txt = "Capital Inicial";
        else if (this.getClase() == ClaseMovimiento.IngresoTaquilla)
            txt = "Ingreso Taquilla";
        else if (this.getClase() == ClaseMovimiento.IngresoPublicidad)
            txt = "Ingreso Publicidad";
        else if (this.getClase() == ClaseMovimiento.Indemnizacion)
            txt = "Indemnizaciones";
        else if (this.getClase() == ClaseMovimiento.Blindajes)
            txt = "Blindajes";
        else if (this.getClase() == ClaseMovimiento.Renovaciones)
            txt = "Renovaciones";
        else if (this.getClase() == ClaseMovimiento.Fichas)
            txt = "Fichas";
        else if (this.getClase() == ClaseMovimiento.Mantenimiento)
            txt = "Gastos Mantenimiento";
        else if (this.getClase() == ClaseMovimiento.Gestion)
            txt = "Gastos Gestión";
        else if (this.getClase() == ClaseMovimiento.Fichajes)
            txt = "Fichajes";
        else if (this.getClase() == ClaseMovimiento.VentaJugadores)
            txt = "Venta Jugadores";
        else if (this.getClase() == ClaseMovimiento.InversionBolsa)
            txt = "Inversiones en Bolsa";
        else if (this.getClase() == ClaseMovimiento.RescateBolsa)
            txt = "Venta en Bolsa";
        else if (this.getClase() == ClaseMovimiento.ConcesionCredito)
            txt = "Concesión Credito";
        else if (this.getClase() == ClaseMovimiento.DevolucionCredito)
            txt = "Devolución Credito";
        else if (this.getClase() == ClaseMovimiento.AmpliacionEstadio)
            txt = "Ampliación en Estadio/Pavellon";
        else if (this.getClase() == ClaseMovimiento.Primas)
            txt = "Primas";
        else if (this.getClase() == ClaseMovimiento.Cursos)
            txt = "Cursos";
        else if (this.getClase() == ClaseMovimiento.premioFairPlay)
            txt = "Premio al Fair Play";
        else if (this.getClase() == ClaseMovimiento.RegularizacionPositiva)
            txt = "Regularización";
        else if (this.getClase() == ClaseMovimiento.RegularizacionNegativa)
            txt = "Regularización";
        else if (this.getClase() == ClaseMovimiento.PagoHacienda)
            txt = "Pago Hacienda";
        else if (this.getClase() == ClaseMovimiento.DevolucionHacienda)
            txt = "Devolucion Hacienda";
        else if (this.getClase() == ClaseMovimiento.PremioCompeticion)
            txt = "Premio Competición";
        else if (this.getClase() == ClaseMovimiento.IBI)
            txt = "Impueto Bienes Inmuebles (IBI)";
        
        return txt;
        
    }
    

    public String getClaseTxt(){

        String txt = getClaseTxtSimple();  

        if (this.getDescripcion() != null && !this.getDescripcion().equals(""))
            txt = txt.concat(" (" + this.getDescripcion() + ")" );

        return txt;
    }

    public boolean isPositivo(){

        boolean result = false;

        if (
                this.getClase() == ClaseMovimiento.IngresoTaquilla ||
                this.getClase() == ClaseMovimiento.IngresoPublicidad ||
                this.getClase() == ClaseMovimiento.VentaJugadores ||
                this.getClase() == ClaseMovimiento.RescateBolsa ||
                this.getClase() == ClaseMovimiento.ConcesionCredito ||
                this.getClase() == ClaseMovimiento.CapitalInicial ||
                this.getClase() == ClaseMovimiento.premioFairPlay ||
                this.getClase() == ClaseMovimiento.RegularizacionPositiva ||
                this.getClase() == ClaseMovimiento.DevolucionHacienda ||
                this.getClase() == ClaseMovimiento.PremioCompeticion 
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
        
        ArrayList<Movimiento> lista = new ArrayList<Movimiento>();
        
        if (!movs.isEmpty()){
            int saldo = eq.getPresupuesto();
            Movimiento movIni = new Movimiento(eq, ClaseMovimiento.SaldoActual, 0);
            movIni.setSaldo(saldo);
            lista.add(movIni); 

            for (Movimiento mov : movs) {
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

