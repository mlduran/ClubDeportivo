/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.futbol8;

import java.util.ArrayList;
import mld.clubdeportivo.base.Entrenador;
import mld.clubdeportivo.base.Grupo;
import static mld.clubdeportivo.base.futbol8.TacticaFutbol8.tacticaFutbol8;
import static mld.clubdeportivo.base.futbol8.TacticaFutbol8.tacticasFutbol8;
import mld.clubdeportivo.utilidades.Calculos;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;

/**
 *
 * @author Miguel
 */
public class EntrenadorFutbol8 extends Entrenador {
    

    public static int PRECIO_CURSO = 4000; 
    private ArrayList<TacticaFutbol8> tacticas;
    private TacticaFutbol8 ultimaTacticaUtilizada;
    private int plusTactica; 

    public EntrenadorFutbol8(){};
    
     public EntrenadorFutbol8(EquipoFutbol8 equipo, Grupo grupo, String nombre){

        this.setEquipo(equipo);
        this.setGrupo(grupo);
        this.setNombre(nombre);
        this.setContrato(JORNADAS_CONTRATO);      

    }

    public EntrenadorFutbol8(EquipoFutbol8 equipo, String nombre){

        this(equipo, null, nombre);

    }

    public EntrenadorFutbol8(Grupo grupo, String nombre){

       this(null, grupo, nombre);

    }    
    
    public int getIndemnizacion(){
        return this.getContrato() * this.getFicha();
    }
    
    public void contratar(EntrenadorFutbol8 actual){
        
        var eq = (EquipoFutbol8) actual.getEquipo();
        
        if (actual.getIndemnizacion() > eq.getPresupuesto())
            throw new UnsupportedOperationException("No tienes presupuesto suficiente");
        
        eq.setPresupuesto(eq.getPresupuesto() - actual.getIndemnizacion());
        eq.setEntrenador(this);
        this.setEquipo(eq);
        this.setGrupo(eq.getClub().getGrupo());
        actual.setEquipo(null);
        
    }
   

    public ArrayList<TacticaFutbol8> getTacticas() {
        return tacticas;
    }

   
    public void setTacticas(ArrayList<TacticaFutbol8> tacticas) {
        this.tacticas = tacticas;
    }

    public TacticaFutbol8 getTactica(int numero){

        TacticaFutbol8 tac = null;
        for (var tactica : this.tacticas) {
            if (tactica.getNumero() == numero) tac = tactica;
        }

        return tac;
    }


    public String getTacticasString() {

        var txt = new StringBuilder();
        var primero = true;
        for (var tact : this.getTacticas()) {
            if (tact == null) continue;
            if (primero == false) txt.append(","); else primero = false;            
            txt.append(tact.getNumero());            
        }

        return txt.toString();

    }
    
    public TacticaFutbol8 getTacticaAleatoria(){        
        
        var numTac = valorAleatorio(this.getTacticas().size());         
        return this.getTacticas().get(numTac);        
    }
    
    
    public void anyadirTactica(TacticaFutbol8 tact){
        
        this.getTacticas().add(tact);
        this.setFicha(this.getFicha() + 5 * (this.getTacticas().size()));
        
    }
    
    public void anyadirTacticaNuevoContrato(){
        
        var tacts =
                (ArrayList<TacticaFutbol8>) tacticasFutbol8();
        
        for(;;){
            var isOk = true;
            var numTac = valorAleatorio(tacts.size());
            for (var tacticaFutbol8 : this.getTacticas()) {
                if (numTac == tacticaFutbol8.getNumero()){
                    isOk = false;
                    break;
                }
            }
            if (isOk){            
                anyadirTactica(tacticaFutbol8(numTac));                
                break;
            }
        }        
        
    }

    public void anyadirTacticaCurso(TacticaFutbol8 tact) {
        
        
        var eq = (EquipoFutbol8) this.getEquipo();
        
        if (PRECIO_CURSO > eq.getPresupuesto())
            throw new UnsupportedOperationException("No tienes presupuesto suficiente");
        
        anyadirTactica(tact);
        
        eq.setPresupuesto(eq.getPresupuesto() - PRECIO_CURSO);
        
        
    }

    public TacticaFutbol8 getUltimaTacticaUtilizada() {
        return ultimaTacticaUtilizada;
    }

    public void setUltimaTacticaUtilizada(TacticaFutbol8 ultimaTacticaUtilizada) {
        this.ultimaTacticaUtilizada = ultimaTacticaUtilizada;
    }

    public int getPlusTactica() {
        return plusTactica;
    }

    public void setPlusTactica(int plusTactica) {
        this.plusTactica = plusTactica;
    }
    
    public int obtenerPlusTactica(TacticaFutbol8 tacticaUtilizada){
        /* este metodo se ha de llamar antes de un partido
         * y devuelve un nuevo valor segun si se va utilizando la
         * misma tactica o no 
         * La tabla para asignar y desasignar es : 0, 1, 2, 3, 6, 10
         */        
        
        var ultimaTact = this.getUltimaTacticaUtilizada();
        var nuevoPlus = this.getPlusTactica();
        
        if (ultimaTact != null) {
            if (ultimaTact.equals(tacticaUtilizada))
                nuevoPlus = aumentoPlusTactica();
            else
                nuevoPlus = decrementoPlusTactica();                
        }
        
        return nuevoPlus;
        
    }
    
    private int aumentoPlusTactica(){
        
        var actual = this.getPlusTactica();
        var nuevo = actual;
        if (actual == 0) nuevo = 1;
        else if (actual == 1) nuevo = 2;
        else if (actual == 2) nuevo = 3;
        else if (actual == 3) nuevo = 6;
        else if (actual == 6) nuevo = 10; 
        
        return nuevo;        
    }
    
    private int decrementoPlusTactica(){
        
        var actual = this.getPlusTactica();
        var nuevo = actual;
        if (actual == 1) nuevo = 0;
        else if (actual == 2) nuevo = 1;
        else if (actual == 3) nuevo = 2;
        else if (actual == 6) nuevo = 3;
        else if (actual == 10) nuevo = 6;
        
        return nuevo;
    }
    

}
