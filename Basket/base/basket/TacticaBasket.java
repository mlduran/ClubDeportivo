/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.basket;

import java.util.ArrayList;
import mld.clubdeportivo.base.Objeto;

/**
 *
 * @author Miguel
 */
public final class TacticaBasket extends Objeto {

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private int numero;
    private String[] tactica;
    private String tipo; // Ataque - Defensa

    public TacticaBasket(int numero, String[] tactica, String tipo) {

        this.setNumero(numero);
        this.setTactica(tactica);
        this.setTipo(tipo);
    }

   
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }


    public String[] getTactica() {
        return tactica;
    }

    public boolean isTacticaAtaque() {
        return this.getTipo().equals("Ataque");
    }
    public boolean isTacticaDefensa() {
        return this.getTipo().equals("Defensa");
    }
    public void setTactica(String[] tactica) {
        this.tactica = tactica;
    }

   
    public String[][] convertirStrings(String defensa, String centrocampo,
            String delantera){

        String[][] tac = new String[3][7];

        for (int i = 0; i < defensa.length(); i++){
            tac[0][i] = String.valueOf(defensa.charAt(i));
        }

        for (int i = 0; i < centrocampo.length(); i++){
            tac[1][i] = String.valueOf(centrocampo.charAt(i));
        }

        for (int i = 0; i < delantera.length(); i++){
            tac[2][i] = String.valueOf(delantera.charAt(i));
        }

        return tac;

    }

    
    public String getCuadrante() {
        // Devuelve un string de los cuadrantes correspondientes
        // a la tactica en formato A1,C3,F1

        StringBuilder cuadrante = new StringBuilder();

        String[] letras = {"A","B","C","D","E","F","G"};
        for (int i = 2; i >= 0; i--){
            int cont = 0;
            for (String tac : tacticasAtaqueBasket()) {
                if (!cuadrante.toString().isEmpty()) {
                    cuadrante.append(",");
                }
                if (tac.equals("x")) {
                    cuadrante.append(letras[cont]).append(i + 1);
                }
                cont++;
            }
        }

        return cuadrante.toString();

    }

    public static String getTxtTactica(TacticaBasket tac){
        
        return "(" + tac.getCuadranteDefensa() +
                "-" + tac.getCuadranteCentrocampo() + 
                "-" + tac.getCuadranteDelantera() + ")";
         
    }
    
    public String getTxtTipoTactica(){
        
        int contDef = 0;
        for (PosicionCuadrante pos : this.getCuadranteDefensa()) {
            if (pos.isActiva()) contDef++;
        }
        int contCen = 0;
        for (PosicionCuadrante pos : this.getCuadranteCentrocampo()) {
            if (pos.isActiva()) contCen++;
        }
        int contDel = 0;
        for (PosicionCuadrante pos : this.getCuadranteDelantera()) {
            if (pos.isActiva()) contDel++;
        }
        
        String tipo;
        
        if (contDef < contDel) tipo = "Ofensiva";
        else if (contDef >= contCen + contDel) tipo = "Defensiva";
        else tipo = "Normal";
        
        return tipo;
        
        
    }
    
    
    public String getTxtTactica(){
        
        int contDef = 0;
        for (PosicionCuadrante pos : this.getCuadranteDefensa()) {
            if (pos.isActiva()) contDef++;
        }
        int contCen = 0;
        for (PosicionCuadrante pos : this.getCuadranteCentrocampo()) {
            if (pos.isActiva()) contCen++;
        }
        int contDel = 0;
        for (PosicionCuadrante pos : this.getCuadranteDelantera()) {
            if (pos.isActiva()) contDel++;
        }
        
        return "( " + contDef + " - " + contCen + " - " + contDel + " )";
         
    }
    
    public static String getTxtTacticaAtaque(int numero){
        
        TacticaBasket tac = tacticaBasketAtaque(numero);
        
        return getTxtTactica(tac);
        
    }
  

    public boolean equals(TacticaBasket obj) {

        return  obj.getNumero() == this.getNumero();
    }

    public static ArrayList<TacticaBasket> tacticasAtaqueBasket() {
             
        String[] tacticas = null;

        tacticas[1] = 
                "----x----*" + 
                "---------*" + 
                "---------*" + 
                "x-------x*" + 
                "---------*" + 
                "---------*" + 
                "x-------x";
        tacticas[2] = 
                "---------*" + 
                "--x---x--*" + 
                "---------*" + 
                "----x----*" + 
                "---------*" + 
                "x-------x*" + 
                "---------";       

     ArrayList<TacticaBasket> lista = new ArrayList<TacticaBasket>();
        
     for (int i = 1; i <= tacticas.length; i++) {
         TacticaBasket tac;
         
            tac = new TacticaBasket(i, tacticas[i].split("*"), "Ataque");
         lista.add(tac);
     }

     return lista;
     
 }

    public static TacticaBasket tacticaBasketAtaque(int numero) {

        TacticaBasket tac = null;

        for (TacticaBasket tactica : tacticasAtaqueBasket()) {
            if (tactica.getNumero() == numero) {
                tac = tactica;
                break;
            }
        }

        return tac;

    }


}
