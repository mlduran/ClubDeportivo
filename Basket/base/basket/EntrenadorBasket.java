/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.basket;

import java.util.ArrayList;
import mld.clubdeportivo.base.Entrenador;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.utilidades.Calculos;

/**
 *
 * @author Miguel
 */
public class EntrenadorBasket extends Entrenador {
    

    public static int PRECIO_CURSO = 4000; 
    private ArrayList<TacticaBasket> tacticas;
    private TacticaBasket ultimaTacticaUtilizada;
    private int plusTactica; 

    public EntrenadorBasket(){};
    
     public EntrenadorBasket(EquipoBasket equipo, Grupo grupo, String nombre){

        this.setEquipo(equipo);
        this.setGrupo(grupo);
        this.setNombre(nombre);
        this.setContrato(JORNADAS_CONTRATO);      

    }

    public EntrenadorBasket(EquipoBasket equipo, String nombre){

        this(equipo, null, nombre);

    }

    public EntrenadorBasket(Grupo grupo, String nombre){

       this(null, grupo, nombre);

    }    
    
    public int getIndemnizacion(){
        return this.getContrato() * this.getFicha();
    }
    
    public void contratar(EntrenadorBasket actual){
        
        EquipoBasket eq = (EquipoBasket) actual.getEquipo();
        
        if (actual.getIndemnizacion() > eq.getPresupuesto())
            throw new UnsupportedOperationException("No tienes presupuesto suficiente");
        
        eq.setPresupuesto(eq.getPresupuesto() - actual.getIndemnizacion());
        eq.setEntrenador(this);
        this.setEquipo(eq);
        this.setGrupo(eq.getClub().getGrupo());
        actual.setEquipo(null);
        
    }
   

    public ArrayList<TacticaBasket> getTacticas() {
        return tacticas;
    }

   
    public void setTacticas(ArrayList<TacticaBasket> tacticas) {
        this.tacticas = tacticas;
    }

    public TacticaBasket getTactica(int numero){

        TacticaBasket tac = null;
        for (TacticaBasket tactica : this.tacticas) {
            if (tactica.getNumero() == numero) tac = tactica;
        }

        return tac;
    }


    public String getTacticasString() {

        StringBuilder txt = new StringBuilder();

        boolean primero = true;
        for (TacticaBasket tact : this.getTacticas()) {
            if (tact == null) continue;
            if (primero == false) txt.append(","); else primero = false;            
            txt.append(tact.getNumero());            
        }

        return txt.toString();

    }
    
    public TacticaBasket getTacticaAleatoria(){        
        
        int numTac = Calculos.valorAleatorio(this.getTacticas().size());         
        return this.getTacticas().get(numTac);        
    }
    
    
    public void anyadirTactica(TacticaBasket tact){
        
        this.getTacticas().add(tact);
        this.setFicha(this.getFicha() + 5 * (this.getTacticas().size()));
        
    }
    
    public void anyadirTacticaNuevoContrato(){
        
        ArrayList<TacticaBasket> tacts =
                (ArrayList<TacticaBasket>) TacticaBasket.tacticasBasket();
        
        for(;;){
            boolean isOk = true;
            int numTac = Calculos.valorAleatorio(tacts.size()); 
            for (TacticaBasket tacticaBasket : this.getTacticas()) {
                if (numTac == tacticaBasket.getNumero()){
                    isOk = false;
                    break;
                }
            }
            if (isOk){            
                anyadirTactica(TacticaBasket.tacticaBasket(numTac));                
                break;
            }
        }        
        
    }

    public void anyadirTacticaCurso(TacticaBasket tact) {
        
        
        EquipoBasket eq = (EquipoBasket) this.getEquipo();
        
        if (PRECIO_CURSO > eq.getPresupuesto())
            throw new UnsupportedOperationException("No tienes presupuesto suficiente");
        
        anyadirTactica(tact);
        
        eq.setPresupuesto(eq.getPresupuesto() - PRECIO_CURSO);
        
        
    }

    public TacticaBasket getUltimaTacticaUtilizada() {
        return ultimaTacticaUtilizada;
    }

    public void setUltimaTacticaUtilizada(TacticaBasket ultimaTacticaUtilizada) {
        this.ultimaTacticaUtilizada = ultimaTacticaUtilizada;
    }

    public int getPlusTactica() {
        return plusTactica;
    }

    public void setPlusTactica(int plusTactica) {
        this.plusTactica = plusTactica;
    }
    
    public int obtenerPlusTactica(TacticaBasket tacticaUtilizada){
        /* este metodo se ha de llamar antes de un partido
         * y devuelve un nuevo valor segun si se va utilizando la
         * misma tactica o no 
         * La tabla para asignar y desasignar es : 0, 1, 2, 3, 6, 10
         */        
        
        TacticaBasket ultimaTact = this.getUltimaTacticaUtilizada();
        
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
