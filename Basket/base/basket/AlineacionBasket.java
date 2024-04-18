/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.basket;

import java.util.ArrayList;
import mld.clubdeportivo.base.Alineacion;
import mld.clubdeportivo.base.basket.EntrenadorBasket;
import mld.clubdeportivo.base.basket.EsfuerzoBasket;
import mld.clubdeportivo.base.basket.EstrategiaBasket;
import mld.clubdeportivo.base.basket.PartidoBasket;
import mld.clubdeportivo.base.basket.TacticaBasket;
import mld.clubdeportivo.utilidades.Calculos;

/**
 *
 * @author Miguel
 */
public final class AlineacionBasket extends Alineacion {

    private int cuarto;
    private TacticaBasket tactica;
    private JugadorBasket jugador1;
    private String cuadrante1;
    private JugadorBasket jugador2;
    private String cuadrante2;
    private JugadorBasket jugador3;
    private String cuadrante3;
    private JugadorBasket jugador4;
    private String cuadrante4;
    private JugadorBasket jugador5;
    private String cuadrante5;
    private EntrenadorBasket entrenador;
    private int primas;
    private EsfuerzoBasket esfuerzo;
    private EstrategiaBasket estrategia;
    
    public AlineacionBasket(){}

    public AlineacionBasket(EquipoBasket eq, PartidoBasket partido){

        this.setEquipo(eq);
        this.setPartido(partido);
        this.setEntrenador(eq.getEntrenador());
        this.setPrimas(0);
        this.setEsfuerzo(EsfuerzoBasket.Normal);
        this.setEstrategia(EstrategiaBasket.Normal);

    }

    public EsfuerzoBasket getEsfuerzo() {
        return esfuerzo;
    }

    public void setEsfuerzo(EsfuerzoBasket esfuerzoBasket) {
        this.esfuerzo = esfuerzoBasket;
    }

    public EstrategiaBasket getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaBasket estrategiaBasket) {
        this.estrategia = estrategiaBasket;
    }

    public int getPrimas() {
        return primas;
    }

    public void setPrimas(int primas) {
        this.primas = primas;
    }
    
    public void setPrima(int prima, boolean terceros) {
        
        if (prima < 0)
            throw new IllegalArgumentException("La prima no puede ser negativa " + prima);
        
        int primaMax = this.getEquipo().getPrimaMaxima();
        if (terceros) prima = prima / 2;
        
        int primaFinal = this.getPrimas() + prima;
        
        if (primaFinal > primaMax)
            throw new IllegalArgumentException("La prima no puede superar la cantidad de " + primaMax);
        
        this.setPrimas(this.getPrimas() + prima);
    }

    public JugadorBasket getJugador1() {
        return jugador1;
    }

    public void setJugador1(JugadorBasket jugador1) {
        this.jugador1 = jugador1;
    }

    public JugadorBasket getJugador2() {
        return jugador2;
    }

    public void setJugador2(JugadorBasket jugador2) {
        this.jugador2 = jugador2;
    }

    public JugadorBasket getJugador3() {
        return jugador3;
    }

    public void setJugador3(JugadorBasket jugador3) {
        this.jugador3 = jugador3;
    }

    public JugadorBasket getJugador4() {
        return jugador4;
    }

    public void setJugador4(JugadorBasket jugador4) {
        this.jugador4 = jugador4;
    }

    public JugadorBasket getJugador5() {
        return jugador5;
    }

    public void setJugador5(JugadorBasket jugador5) {
        this.jugador5 = jugador5;
    }


    public EntrenadorBasket getEntrenador() {
        return entrenador;
    }

    public final void setEntrenador(EntrenadorBasket entrenador) {
        this.entrenador = entrenador;
    }

 
    public String getCuadrante1() {
        return cuadrante1;
    }

    public void setCuadrante1(String cuadrante1) {
        this.cuadrante1 = cuadrante1;
    }

    public String getCuadrante2() {
        return cuadrante2;
    }

    public void setCuadrante2(String cuadrante2) {
        this.cuadrante2 = cuadrante2;
    }

    public String getCuadrante3() {
        return cuadrante3;
    }

    public void setCuadrante3(String cuadrante3) {
        this.cuadrante3 = cuadrante3;
    }

    public String getCuadrante4() {
        return cuadrante4;
    }

    public void setCuadrante4(String cuadrante4) {
        this.cuadrante4 = cuadrante4;
    }

    public String getCuadrante5() {
        return cuadrante5;
    }

    public void setCuadrante5(String cuadrante5) {
        this.cuadrante5 = cuadrante5;
    }


    public TacticaBasket getTactica() {
        return tactica;
    }

    public void setTactica(TacticaBasket tactica) {
        this.tactica = tactica;
    }
    
    
    public ArrayList<JugadorBasket> jugadores(){
        
        ArrayList<JugadorBasket> lista = new ArrayList<JugadorBasket>();  
        
        if (this.getJugador1() != null) {
            this.getJugador1().setCuadrante(this.getCuadrante1());
            lista.add(this.getJugador1());
        }            
        if (this.getJugador2() != null) {
            this.getJugador2().setCuadrante(this.getCuadrante2());
            lista.add(this.getJugador2());
        }
        if (this.getJugador3() != null) {
            this.getJugador3().setCuadrante(this.getCuadrante3());
            lista.add(this.getJugador3());
        }
        if (this.getJugador4() != null) {
            this.getJugador4().setCuadrante(this.getCuadrante4());
            lista.add(this.getJugador4());
        }
        if (this.getJugador5() != null) {
            this.getJugador5().setCuadrante(this.getCuadrante5());
            lista.add(this.getJugador5());
        }
        
        return lista;        
    }
    
    
    private String cuadranteOpuesto(String cuadrante){
        
        String cuadOpuesto = String.valueOf(cuadrante.charAt(0));
        if (cuadrante.contains("1"))
            cuadOpuesto = cuadOpuesto.concat("3");
        else if (cuadrante.contains("2"))
            cuadOpuesto = cuadOpuesto.concat("2");
        else if (cuadrante.contains("3"))
            cuadOpuesto = cuadOpuesto.concat("1");
        
        return cuadOpuesto;
        
    }
    
    public JugadorBasket jugadorContrario(String cuadrante){
        
        JugadorBasket jugC = null;
        
         for (JugadorBasket jug : jugadores()) 
            if (jug.getCuadrante().equals(cuadranteOpuesto(cuadrante))){
                jugC = jug;
                break;            }
        
        
        return jugC;
    }
    
    public JugadorBasket jugadorParaPaseMismaLinea(String cuadrante){
        
        JugadorBasket jugP = null;
        ArrayList<JugadorBasket> posibles = new ArrayList<JugadorBasket>();
        
        char linea = cuadrante.charAt(1);
               
        for (JugadorBasket jug : jugadores())
            if (!jug.getCuadrante().equals(cuadrante) &&
                jug.getCuadrante().charAt(1) == linea)
                posibles.add(jug);           
        
        if (posibles.size() > 0)        
            jugP = posibles.get(Calculos.valorAleatorio(posibles.size()));
                
        return jugP;
    }
    
    public JugadorBasket jugadorParaPase(String cuadrante){
        
        JugadorBasket jugP = null;
        ArrayList<JugadorBasket> posiblesA = new ArrayList<JugadorBasket>();
        ArrayList<JugadorBasket> posiblesB = new ArrayList<JugadorBasket>();
        
        if (cuadrante.charAt(1) == '1')
            cuadrante = cuadrante.charAt(0) + "2";
        else if (cuadrante.charAt(1) == '2')
            cuadrante = cuadrante.charAt(0) + "3";
        else
            return null; // no hay pase en la misma linea
        
        ArrayList<String> cercanos = cuadrantesCercanos(cuadrante);
        ArrayList<String> proximos = cuadrantesProximos(cuadrante);
        
        for (JugadorBasket jug : jugadores()){ 

            if (jug.getCuadrante().equals(cuadrante))
                posiblesA.add(jug);            
            else if (cercanos.contains(jug.getCuadrante()))
                posiblesA.add(jug);            
            else if (proximos.contains(jug.getCuadrante()))
                posiblesB.add(jug);            
        }
        
        if (jugP == null && !posiblesA.isEmpty())
            jugP = posiblesA.get(Calculos.valorAleatorio(posiblesA.size()));
        else if (jugP == null && !posiblesB.isEmpty())
            jugP = posiblesB.get(Calculos.valorAleatorio(posiblesB.size()));
        
        return jugP;
    }
    
    public JugadorBasket jugadorParaPaseAtras(String cuadrante){
        
        JugadorBasket jugP = null;
        ArrayList<JugadorBasket> posiblesA = new ArrayList<JugadorBasket>();
        ArrayList<JugadorBasket> posiblesB = new ArrayList<JugadorBasket>();
        
        if (cuadrante.charAt(1) == '3')
            cuadrante = cuadrante.charAt(0) + "2";
        else if (cuadrante.charAt(1) == '2')
            cuadrante = cuadrante.charAt(0) + "1";
        else
            return null; // no hay hacia atras
        
        ArrayList<String> cercanos = cuadrantesCercanos(cuadrante);
        ArrayList<String> proximos = cuadrantesProximos(cuadrante);
        
        for (JugadorBasket jug : jugadores()){ 

            if (jug.getCuadrante().equals(cuadrante)){
                jugP = jug;
                break;
            }
            else if (cercanos.contains(jug.getCuadrante()))
                posiblesA.add(jug);            
            else if (proximos.contains(jug.getCuadrante()))
                posiblesB.add(jug);            
        }
        
        if (jugP == null && !posiblesA.isEmpty())
            jugP = posiblesA.get(Calculos.valorAleatorio(posiblesA.size()));
        else if (jugP == null && !posiblesB.isEmpty())
            jugP = posiblesB.get(Calculos.valorAleatorio(posiblesB.size()));
        
        return jugP;
    }
    
    public JugadorBasket jugadorParaPaseLargo(String cuadranteOri){
        
        JugadorBasket jugP = null;
        ArrayList<JugadorBasket> posiblesA = new ArrayList<JugadorBasket>();
        ArrayList<JugadorBasket> posiblesB = new ArrayList<JugadorBasket>();

        String cuadrante = cuadranteOri.charAt(0) + "3";
        
        ArrayList<String> cercanos = cuadrantesCercanos(cuadrante);
        ArrayList<String> proximos = cuadrantesProximos(cuadrante);
        
        for (JugadorBasket jug : jugadores()){ 
            if (cercanos.contains(jug.getCuadrante()))
                posiblesA.add(jug);            
            else if (proximos.contains(jug.getCuadrante()))
                posiblesB.add(jug);            
        }  
        
        if (jugP == null && !posiblesA.isEmpty())
            jugP = posiblesA.get(Calculos.valorAleatorio(posiblesA.size()));
        else if (jugP == null && !posiblesB.isEmpty())
            jugP = posiblesB.get(Calculos.valorAleatorio(posiblesB.size()));
        
        // si no encontramos ninguno de estos buscamos a cualquiera 
        // que este en la delantera
        if (jugP == null){
            for (JugadorBasket jug : jugadores()){ 
                if (jug.getCuadrante().equals(cuadranteOri))
                    continue;
                else if (jug.getCuadrante().charAt(1) == '3')
                    posiblesA.add(jug);                
            }
            if (!posiblesA.isEmpty())
            jugP = posiblesA.get(Calculos.valorAleatorio(posiblesA.size()));                
        }
        
        return jugP;
        
        
    }
    
    
    private ArrayList<String> cuadrantesCercanos(String cuadrante){
        
        ArrayList<String> cuadrantes = new ArrayList<String>();
        
        if (cuadrante.contains("A"))
            cuadrantes.add("B".concat(cuadrante.substring(1)));
        
        else if (cuadrante.contains("B")){
            cuadrantes.add("A".concat(cuadrante.substring(1)));
            cuadrantes.add("C".concat(cuadrante.substring(1)));
        }
        else if (cuadrante.contains("C")){
            cuadrantes.add("B".concat(cuadrante.substring(1)));
            cuadrantes.add("D".concat(cuadrante.substring(1)));
        }
        else if (cuadrante.contains("D")){
            cuadrantes.add("C".concat(cuadrante.substring(1)));
            cuadrantes.add("E".concat(cuadrante.substring(1)));
        }
        else if (cuadrante.contains("E")){
            cuadrantes.add("D".concat(cuadrante.substring(1)));
            cuadrantes.add("F".concat(cuadrante.substring(1)));
        }        
        else if (cuadrante.contains("F")){
            cuadrantes.add("E".concat(cuadrante.substring(1)));
            cuadrantes.add("G".concat(cuadrante.substring(1)));
        }
        else if (cuadrante.contains("G"))
            cuadrantes.add("F".concat(cuadrante.substring(1)));
        
        return cuadrantes;
        
    }
    
    public JugadorBasket jugadorContrarioCercano(String cuadrante){

        JugadorBasket jugC = null;
        ArrayList<JugadorBasket> jugs = new ArrayList<JugadorBasket>();
        
        ArrayList<String> cercanos = cuadrantesCercanos(cuadranteOpuesto(cuadrante));
        
        for (JugadorBasket jug : jugadores()) 
            if (cercanos.contains(jug.getCuadrante()))
                jugs.add(jug);
                
        if (jugs.size() > 0) 
            jugC = jugs.get(Calculos.valorAleatorio(jugs.size()));
        
        return jugC;
    }
    
    private ArrayList<String> cuadrantesProximos(String cuadrante){
        // Devuelve el cuadrante siguiente al mas cercano
        ArrayList<String> cuadrantes = new ArrayList<String>();
        
        if (cuadrante.contains("A"))
            cuadrantes.add("C".concat(cuadrante.substring(1)));
        
        else if (cuadrante.contains("B")){
            cuadrantes.add("D".concat(cuadrante.substring(1)));
        }
        else if (cuadrante.contains("C")){
            cuadrantes.add("A".concat(cuadrante.substring(1)));
            cuadrantes.add("E".concat(cuadrante.substring(1)));
        }
        else if (cuadrante.contains("D")){
            cuadrantes.add("B".concat(cuadrante.substring(1)));
            cuadrantes.add("F".concat(cuadrante.substring(1)));
        }
        else if (cuadrante.contains("E")){
            cuadrantes.add("C".concat(cuadrante.substring(1)));
            cuadrantes.add("G".concat(cuadrante.substring(1)));
        }        
        else if (cuadrante.contains("F")){
            cuadrantes.add("D".concat(cuadrante.substring(1)));
        }
        else if (cuadrante.contains("G"))
            cuadrantes.add("E".concat(cuadrante.substring(1)));
        
        return cuadrantes;
        
    }
    
    public JugadorBasket jugadorContrarioProximo(String cuadrante){
        // Devuelve el jugador siguiente al mas cercano
        JugadorBasket jugC = null;
        ArrayList<JugadorBasket> jugs = new ArrayList<JugadorBasket>();
        
        ArrayList<String> cercanos = cuadrantesProximos(cuadranteOpuesto(cuadrante));
        
        for (JugadorBasket jug : jugadores()) 
            if (cercanos.contains(jug.getCuadrante()))
                jugs.add(jug);
                
        if (jugs.size() > 0) 
            jugC = jugs.get(Calculos.valorAleatorio(jugs.size()));
        
        return jugC;
    }
    
    
    public void validarAlineacion(){
        
        ArrayList<JugadorBasket> lista = new ArrayList<JugadorBasket>();  
        
        if (this.getJugador1() != null)
            if (!this.getJugador1().isPuedeJugar()){
                this.setJugador1(null);
                this.setCuadrante1("");
            } else
                lista.add(this.getJugador1());        
              
        if (this.getJugador2() != null)
            if (!this.getJugador2().isPuedeJugar() ||
                lista.contains(this.getJugador2())){
            this.setJugador2(null);
            this.setCuadrante2("");
            }else 
                lista.add(this.getJugador2());
        
        if (this.getJugador3() != null)
            if (!this.getJugador3().isPuedeJugar() ||
                lista.contains(this.getJugador3()) ){
            this.setJugador3(null);
            this.setCuadrante3("");
            }else
                lista.add(this.getJugador3());
        
        if (this.getJugador4() != null)
            if (!this.getJugador4().isPuedeJugar() ||
                lista.contains(this.getJugador4())){
            this.setJugador4(null);
            this.setCuadrante4("");
            }else
                lista.add(this.getJugador4());
        
        if (this.getJugador5() != null)
            if (!this.getJugador5().isPuedeJugar() ||
                lista.contains(this.getJugador5())){
            this.setJugador5(null);
            this.setCuadrante5("");
            } else
                lista.add(this.getJugador5());    
        
    }

    public void limpiarAlineacion(){

        this.setJugador1(null);
        this.setJugador2(null);
        this.setJugador3(null);
        this.setJugador4(null);
        this.setJugador5(null);

        this.setCuadrante1(null);
        this.setCuadrante2(null);
        this.setCuadrante3(null);
        this.setCuadrante4(null);
        this.setCuadrante5(null);

    }

    public boolean setDato(String posicion, JugadorBasket jug){

        // primero comprobamos si el puesto ya esta rellenado si
        // es asi no hacemos nada
        
        boolean asignado= false;
        
        ArrayList<JugadorBasket> jugadores = this.jugadores();
        boolean isAliniado = false;
        for (JugadorBasket jugAlineado : jugadores) {
            if (jugAlineado.equals(jug)) isAliniado = true;
            break;
        }
        if (isAliniado) return asignado;
        
        if (this.getJugador1() == null){
            this.setJugador1(jug);
            this.setCuadrante1(posicion);
            asignado = true;
        }else if (this.getJugador2() == null){
            this.setJugador2(jug);
            this.setCuadrante2(posicion);
            asignado = true;
        }else if (this.getJugador3() == null){
            this.setJugador3(jug);
            this.setCuadrante3(posicion);
            asignado = true;
        }else if (this.getJugador4() == null){
            this.setJugador4(jug);
            this.setCuadrante4(posicion);
            asignado = true;
        }else if (this.getJugador5() == null){
            this.setJugador5(jug);
            this.setCuadrante5(posicion);
            asignado = true;
        }
        
        if (asignado) jug.setJuegaJornada(true);
        
        return asignado;
        
    }
    
   public static String codificarJugador(JugadorBasket jug, String cuadrante){
       
       StringBuilder txt = new StringBuilder();
       
       if (cuadrante == null) cuadrante = "XX";
       
       if (jug != null){
            txt.append(cuadrante).append("_");
            txt.append(jug.getId()).append("_");
            txt.append(jug.getPosicion()).append("_");
            txt.append(jug.getValoracionMedia()).append("_");
            txt.append(jug.getNombre());            
        }
       
       return txt.toString();
       
   }
   
   public static String codificarJugadores(ArrayList<JugadorBasket> jugadores){
       
       StringBuilder txt = new StringBuilder();
       
       boolean primero = true;
       
       for (JugadorBasket jug : jugadores) {
           if (primero) primero = false; 
           else txt.append(";");
           txt.append(codificarJugador(jug, null));
       }
       
       return txt.toString();       
   }

   public String getAlineacionCodificada(){

        StringBuilder txt = new StringBuilder();

       if (this.getJugador1() != null) {
            if (!txt.toString().isEmpty()) txt.append(";");
            txt.append(codificarJugador(this.getJugador1(), this.getCuadrante1()));      
            
        }

       if (this.getJugador2() != null) {
            txt.append(";");
            txt.append(codificarJugador(this.getJugador2(), this.getCuadrante2()));
        }

       if (this.getJugador3() != null) {
            txt.append(";");
            txt.append(codificarJugador(this.getJugador3(), this.getCuadrante3()));
        }

       if (this.getJugador4() != null) {
            txt.append(";");
            txt.append(codificarJugador(this.getJugador4(), this.getCuadrante4()));
        }

       if (this.getJugador5() != null) {
            txt.append(";");
            txt.append(codificarJugador(this.getJugador5(), this.getCuadrante5()));
        }

       
        return txt.toString();
    }
   
   public String alineacionParaEstadistica(){
       
       StringBuilder txt = new StringBuilder();
       JugadorBasket[] jugs = {
           this.getJugador1(),
           this.getJugador2(),
           this.getJugador3(),
           this.getJugador4(),
           this.getJugador5(),

       };
       
           
       boolean primero = true;
       for (JugadorBasket jug : jugs) {
           if (jug == null) continue;
           if (!primero) txt.append("&");
           else primero = false;
           txt.append(jug.getNombre()).append(";");
           txt.append(jug.getPosicion().toString()).append(";");
           txt.append(jug.getValoracionMedia()).append(";");
           txt.append(jug.getValoracionPartido()).append(";");               
           txt.append(jug.getCuadrante());           
       }  
       txt.append("&&");
       txt.append(this.getEntrenador().getNombre());
              
       return txt.toString();
       
   }
   
   public static ArrayList<JugadorBasket> parseAlineacion(String txt){
       
       ArrayList<JugadorBasket> alineacion = new ArrayList<JugadorBasket>();
       
       for (String datos : txt.split("&&")[0].split("&")) {
           String[] dato = datos.split(";");
           if (dato.length == 5){
               JugadorBasket jug = new JugadorBasket();
               jug.setNombre(dato[0]);
               // para compatibilizar
               if (dato[1].equals("Centrocampista")) dato[1] = "Medio";
               jug.setPosicion(PosicionJugBasket.valueOf(dato[1]));
               jug.setValoracion(Integer.parseInt(dato[2]) * 10);
               jug.setValoracionPartido(Integer.parseInt(dato[3]));
               jug.setCuadrante(dato[4]);
               alineacion.add(jug);
           }
       }
              
       return alineacion;
   }
   
   public static String parseEntrenador(String txt){
   
       return txt.split("&&")[1];
   }
   
   public ArrayList<String> posicionesCubiertas(){
       
       ArrayList<String> pos = new  ArrayList<String>();
       if (this.getCuadrante1() != null) pos.add(this.getCuadrante1());
       if (this.getCuadrante2() != null) pos.add(this.getCuadrante2());
       if (this.getCuadrante3() != null) pos.add(this.getCuadrante3());
       if (this.getCuadrante4() != null) pos.add(this.getCuadrante4());
       if (this.getCuadrante5() != null) pos.add(this.getCuadrante5());
       
       return pos;       
       
   }

}
