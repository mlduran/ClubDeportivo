/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.futbol8;

import java.util.ArrayList;
import mld.clubdeportivo.base.Objeto;

/**
 *
 * @author Miguel
 */
public final class TacticaFutbol8 extends Objeto {

    private int numero;
    private String[][] tactica;

    public TacticaFutbol8(int numero, String[][] tactica) {

        this.setNumero(numero);
        this.setTactica(tactica);
    }

    public TacticaFutbol8(int numero, String defensa, String centrocampo,
            String delantera){

        this.setNumero(numero);
        this.setTactica(defensa, centrocampo, delantera);
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }


    public String[][] getTactica() {
        return tactica;
    }

    public void setTactica(String[][] tactica) {
        this.tactica = tactica;
    }

    public void setTactica(String defensa, String centrocampo,
            String delantera) {
        this.tactica = convertirStrings(defensa, centrocampo, delantera);
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

    public ArrayList<PosicionCuadrante> getCuadranteDefensa(){

        ArrayList<PosicionCuadrante> lista = new ArrayList<PosicionCuadrante>();

        String[] letras = {"A","B","C","D","E","F","G"};
        for (String letra : letras) {
            PosicionCuadrante pc = new PosicionCuadrante(letra + 1,
                    this.getCuadrante().contains(letra + 1));
            lista.add(pc);
        }

        return lista;
    }

    public ArrayList<PosicionCuadrante> getCuadranteCentrocampo(){

        ArrayList<PosicionCuadrante> lista = new ArrayList<PosicionCuadrante>();

        String[] letras = {"A","B","C","D","E","F","G"};
        for (String letra : letras) {
            PosicionCuadrante pc = new PosicionCuadrante(letra + 2,
                    this.getCuadrante().contains(letra + 2));
            lista.add(pc);
        }

        return lista;
    }

     public ArrayList<PosicionCuadrante> getCuadranteDelantera(){

        ArrayList<PosicionCuadrante> lista = new ArrayList<PosicionCuadrante>();

        String[] letras = {"A","B","C","D","E","F","G"};
        for (String letra : letras) {
            PosicionCuadrante pc = new PosicionCuadrante(letra + 3,
                    this.getCuadrante().contains(letra + 3));
            lista.add(pc);
        }

        return lista;
    }

    public String getCuadrante() {
        // Devuelve un string de los cuadrantes correspondientes
        // a la tactica en formato A1,C3,F1

        StringBuilder cuadrante = new StringBuilder();

        // Defensa
        String[] letras = {"A","B","C","D","E","F","G"};
        for (int i = 2; i >= 0; i--){
            int cont = 0;
            for (String tac : this.tactica[i]) {
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

    public static String getTxtTactica(TacticaFutbol8 tac){
        
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
    
    public static String getTxtTactica(int numero){
        
        TacticaFutbol8 tac = tacticaFutbol8(numero);
        
        return getTxtTactica(tac);
        
    }
  

    public boolean equals(TacticaFutbol8 obj) {

        return  obj.getNumero() == this.getNumero();
    }

    public static ArrayList<TacticaFutbol8> tacticasFutbol8() {
     
        int numTac = 21;

        String[] def = new String[numTac + 1],
              cen = new String[numTac + 1],
                del = new String[numTac + 1];

        del[1] = "---x---";
        cen[1] = "-x---x-";
        def[1] = "x-x-x-x";

     del[2] = "---x---";
     cen[2] = "x-----x";
     def[2] = "x-x-x-x";
   
     del[3] = "---x---";
     cen[3] = "--x-x--";
     def[3] = "x-x-x-x";
                
     del[4] = "-------";
     cen[4] = "-x-x-x-";
     def[4] = "x-x-x-x";

     del[5] = "-x---x-";
     cen[5] = "---x---";
     def[5] = "x-x-x-x";
       
     del[6] = "---x---";
     cen[6] = "-x-x-x-";
     def[6] = "x--x--x";
       
     del[7] = "--x-x--";
     cen[7] = "-x---x-";
     def[7] = "x--x--x";
      
     del[8] = "-x-x-x-";
     cen[8] = "---x---";
     def[8] = "-x-x-x-";
        
     del[9] = "x--x--x";
     cen[9] = "---x---";
     def[9] = "x--x--x";
        
     del[10] = "x-----x";
     cen[10] = "--x-x--";
     def[10] = "x--x--x";

     del[11] = "-------";
     cen[11] = "-x---x-";
     def[11] = "x-xxx-x";
        
     del[12] = "-x---x-";
     cen[12] = "x--x--x";
     def[12] = "-x---x-";

     del[13] = "x--x--x";
     cen[13] = "-x---x-";
     def[13] = "--x-x--";

     del[14] = "--x-x--";
     cen[14] = "-x-x-x-";
     def[14] = "--x-x--";
        
     del[15] = "-x---x-";
     cen[15] = "x-x-x-x";
     def[15] = "---x---";
        
     del[16] = "---x---";
     cen[16] = "x-x-x-x";
     def[16] = "-x---x-";
      
     del[17] = "x-x-x-x";
     cen[17] = "-x---x-";
     def[17] = "---x---";
        
     del[18] = "-x---x-";
     cen[18] = "--xxx--";
     def[18] = "x-----x";
        
     del[19] = "-x---x-";
     cen[19] = "x--x--x";
     def[19] = "--x-x--";
     
     del[20] = "-x-x-x-";
     cen[20] = "-x-x-x-";
     def[20] = "---x---";

     del[21] = "---x---";
     cen[21] = "x-xxx-x";
     def[21] = "---x---";
        
     ArrayList<TacticaFutbol8> lista = new ArrayList<TacticaFutbol8>();
        
     for (int i = 1; i <= numTac; i++) {
         TacticaFutbol8 tac = new TacticaFutbol8(i, def[i], cen[i], del[i]);
         lista.add(tac);
     }

     return lista;
     
 }

    public static TacticaFutbol8 tacticaFutbol8(int numero) {

        TacticaFutbol8 tac = null;

        for (TacticaFutbol8 tactica : tacticasFutbol8()) {
            if (tactica.getNumero() == numero) {
                tac = tactica;
                break;
            }
        }

        return tac;

    }


}
