/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.futbol8;

import java.util.ArrayList;
import mld.clubdeportivo.base.Entrenador;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.utilidades.Calculos;

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
        
        EquipoFutbol8 eq = (EquipoFutbol8) actual.getEquipo();
        
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
        for (TacticaFutbol8 tactica : this.tacticas) {
            if (tactica.getNumero() == numero) tac = tactica;
        }

        return tac;
    }


    public String getTacticasString() {

        StringBuilder txt = new StringBuilder();

        boolean primero = true;
        for (TacticaFutbol8 tact : this.getTacticas()) {
            if (tact == null) continue;
            if (primero == false) txt.append(","); else primero = false;            
            txt.append(tact.getNumero());            
        }

        return txt.toString();

    }
    
    public TacticaFutbol8 getTacticaAleatoria(){        
        
        int numTac = Calculos.valorAleatorio(this.getTacticas().size());         
        return this.getTacticas().get(numTac);        
    }
    
    
    public void anyadirTactica(TacticaFutbol8 tact){
        
        this.getTacticas().add(tact);
        this.setFicha(this.getFicha() + 5 * (this.getTacticas().size()));
        
    }
    
    public void anyadirTacticaNuevoContrato(){
        
        ArrayList<TacticaFutbol8> tacts =
                (ArrayList<TacticaFutbol8>) TacticaFutbol8.tacticasFutbol8();
        
        for(;;){
            boolean isOk = true;
            int numTac = Calculos.valorAleatorio(tacts.size()); 
            for (TacticaFutbol8 tacticaFutbol8 : this.getTacticas()) {
                if (numTac == tacticaFutbol8.getNumero()){
                    isOk = false;
                    break;
                }
            }
            if (isOk){            
                anyadirTactica(TacticaFutbol8.tacticaFutbol8(numTac));                
                break;
            }
        }        
        
    }

    public void anyadirTacticaCurso(TacticaFutbol8 tact) {
        
        
        EquipoFutbol8 eq = (EquipoFutbol8) this.getEquipo();
        
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
        
        TacticaFutbol8 ultimaTact = this.getUltimaTacticaUtilizada();
        
        int nuevoPlus = this.getPlusTactica();
        
        if (ultimaTact != null) {
            if (ultimaTact.equals(tacticaUtilizada))
                nuevoPlus = aumentoPlusTactica();
            else
                nuevoPlus = decrementoPlusTactica();                
        }
        
        return nuevoPlus;
        
    }
    
    private int aumentoPlusTactica(){
        
        int actual = this.getPlusTactica();
        int nuevo = actual;
        if (actual == 0) nuevo = 1;
        else if (actual == 1) nuevo = 2;
        else if (actual == 2) nuevo = 3;
        else if (actual == 3) nuevo = 6;
        else if (actual == 6) nuevo = 10; 
        
        return nuevo;        
    }
    
    private int decrementoPlusTactica(){
        
        int actual = this.getPlusTactica();
        int nuevo = actual;
        if (actual == 1) nuevo = 0;
        else if (actual == 2) nuevo = 1;
        else if (actual == 3) nuevo = 2;
        else if (actual == 6) nuevo = 3;
        else if (actual == 10) nuevo = 6;
        
        return nuevo;
    }
    

}
