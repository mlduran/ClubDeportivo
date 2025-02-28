/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.futbol8;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import java.util.ArrayList;
import mld.clubdeportivo.base.Alineacion;
import static mld.clubdeportivo.base.futbol8.EsfuerzoFutbol8.Normal;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.Normal;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Portero;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.valueOf;
import mld.clubdeportivo.utilidades.Calculos;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;

/**
 *
 * @author Miguel
 */
public final class AlineacionFutbol8 extends Alineacion {

    private TacticaFutbol8 tactica;
    private JugadorFutbol8 portero;
    private String cuadrantep;
    private JugadorFutbol8 jugador1;
    private String cuadrante1;
    private JugadorFutbol8 jugador2;
    private String cuadrante2;
    private JugadorFutbol8 jugador3;
    private String cuadrante3;
    private JugadorFutbol8 jugador4;
    private String cuadrante4;
    private JugadorFutbol8 jugador5;
    private String cuadrante5;
    private JugadorFutbol8 jugador6;
    private String cuadrante6;
    private JugadorFutbol8 jugador7;
    private String cuadrante7;
    private EntrenadorFutbol8 entrenador;
    private int primas;
    private EsfuerzoFutbol8 esfuerzo;
    private EstrategiaFutbol8 estrategia;
    
    public AlineacionFutbol8(){}

    public AlineacionFutbol8(EquipoFutbol8 eq, PartidoFutbol8 partido){

        this.setEquipo(eq);
        this.setPartido(partido);
        this.setEntrenador(eq.getEntrenador());
        this.setPrimas(0);
        this.setEsfuerzo(EsfuerzoFutbol8.Normal);
        this.setEstrategia(EstrategiaFutbol8.Normal);

    }

    public EsfuerzoFutbol8 getEsfuerzo() {
        return esfuerzo;
    }

    public void setEsfuerzo(EsfuerzoFutbol8 esfuerzoFutbol8) {
        this.esfuerzo = esfuerzoFutbol8;
    }

    public EstrategiaFutbol8 getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaFutbol8 estrategiaFutbol8) {
        this.estrategia = estrategiaFutbol8;
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
        
        var primaMax = this.getEquipo().getPrimaMaxima();
        if (terceros) prima = prima / 2;
        
        var primaFinal = this.getPrimas() + prima;
        
        if (primaFinal > primaMax)
            throw new IllegalArgumentException("La prima no puede superar la cantidad de " + primaMax);
        
        this.setPrimas(this.getPrimas() + prima);
    }

    public JugadorFutbol8 getPortero() {
        return portero;
    }

    public void setPortero(JugadorFutbol8 portero) {
        this.portero = portero;
    }

    public JugadorFutbol8 getJugador1() {
        return jugador1;
    }

    public void setJugador1(JugadorFutbol8 jugador1) {
        this.jugador1 = jugador1;
    }

    public JugadorFutbol8 getJugador2() {
        return jugador2;
    }

    public void setJugador2(JugadorFutbol8 jugador2) {
        this.jugador2 = jugador2;
    }

    public JugadorFutbol8 getJugador3() {
        return jugador3;
    }

    public void setJugador3(JugadorFutbol8 jugador3) {
        this.jugador3 = jugador3;
    }

    public JugadorFutbol8 getJugador4() {
        return jugador4;
    }

    public void setJugador4(JugadorFutbol8 jugador4) {
        this.jugador4 = jugador4;
    }

    public JugadorFutbol8 getJugador5() {
        return jugador5;
    }

    public void setJugador5(JugadorFutbol8 jugador5) {
        this.jugador5 = jugador5;
    }

    public JugadorFutbol8 getJugador6() {
        return jugador6;
    }

    public void setJugador6(JugadorFutbol8 jugador6) {
        this.jugador6 = jugador6;
    }

    public JugadorFutbol8 getJugador7() {
        return jugador7;
    }

    public void setJugador7(JugadorFutbol8 jugador7) {
        this.jugador7 = jugador7;
    }

    public EntrenadorFutbol8 getEntrenador() {
        return entrenador;
    }

    public final void setEntrenador(EntrenadorFutbol8 entrenador) {
        this.entrenador = entrenador;
    }

    public String getCuadrantep() {
        return cuadrantep;
    }

    public void setCuadrantep(String cuadrantep) {
        this.cuadrantep = cuadrantep;
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

    public String getCuadrante6() {
        return cuadrante6;
    }

    public void setCuadrante6(String cuadrante6) {
        this.cuadrante6 = cuadrante6;
    }

    public String getCuadrante7() {
        return cuadrante7;
    }

    public void setCuadrante7(String cuadrante7) {
        this.cuadrante7 = cuadrante7;
    }

    public TacticaFutbol8 getTactica() {
        return tactica;
    }

    public void setTactica(TacticaFutbol8 tactica) {
        this.tactica = tactica;
    }
    
    public ArrayList<JugadorFutbol8> jugadores(){
        
        var lista = jugadoresCampo(); 
        if (this.getPortero() != null)
            lista.add(this.getPortero());
        
        return lista;
        
    }
    
    public ArrayList<JugadorFutbol8> jugadoresCampo(){
        
        var lista = new ArrayList<JugadorFutbol8>();  
        
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
        if (this.getJugador6() != null) {
            this.getJugador6().setCuadrante(this.getCuadrante6());
            lista.add(this.getJugador6());
        }
        if (this.getJugador7() != null) {
            this.getJugador7().setCuadrante(this.getCuadrante7());
            lista.add(this.getJugador7());
        }
        
        return lista;        
    }
    
    public ArrayList<JugadorFutbol8> defensas(){
        
        var lista = new ArrayList<JugadorFutbol8>();
        for (var jug : jugadoresCampo())             
            if (jug.getCuadrante().contains("1"))
                lista.add(jug);        
        
        return lista;        
    }
    
    public ArrayList<JugadorFutbol8> centrocampistas(){
        
        var lista = new ArrayList<JugadorFutbol8>();
        for (var jug : jugadoresCampo()) 
            if (jug.getCuadrante().contains("2"))
                lista.add(jug);        
        
        return lista;        
    }
    
    public ArrayList<JugadorFutbol8> delanteros(){
        
        var lista = new ArrayList<JugadorFutbol8>();
        for (var jug : jugadoresCampo()) 
            if (jug.getCuadrante().contains("3"))
                lista.add(jug);        
        
        return lista;        
    }
    
    private String cuadranteOpuesto(String cuadrante){
        
        var cuadOpuesto = valueOf(cuadrante.charAt(0));
        if (cuadrante.contains("1"))
            cuadOpuesto = cuadOpuesto.concat("3");
        else if (cuadrante.contains("2"))
            cuadOpuesto = cuadOpuesto.concat("2");
        else if (cuadrante.contains("3"))
            cuadOpuesto = cuadOpuesto.concat("1");
        
        return cuadOpuesto;
        
    }
    
    public JugadorFutbol8 jugadorContrario(String cuadrante){
        
        JugadorFutbol8 jugC = null;
        
         for (var jug : jugadoresCampo()) 
            if (jug.getCuadrante().equals(cuadranteOpuesto(cuadrante))){
                jugC = jug;
                break;            }
        
        
        return jugC;
    }
    
    public JugadorFutbol8 jugadorParaPaseMismaLinea(String cuadrante){
        
        JugadorFutbol8 jugP = null;
        var posibles = new ArrayList<JugadorFutbol8>();
        var linea = cuadrante.charAt(1);
        for (var jug : jugadoresCampo())
            if (!jug.getCuadrante().equals(cuadrante) &&
                jug.getCuadrante().charAt(1) == linea)
                posibles.add(jug);           
        
        if (!posibles.isEmpty())        
            jugP = posibles.get(valorAleatorio(posibles.size()));
                
        return jugP;
    }
    
    public JugadorFutbol8 jugadorParaPase(String cuadrante){
        
        JugadorFutbol8 jugP = null;
        var posiblesA = new ArrayList<JugadorFutbol8>();
        var posiblesB = new ArrayList<JugadorFutbol8>();
        
        switch (cuadrante.charAt(1)) {
            case '1' -> cuadrante = cuadrante.charAt(0) + "2";
            case '2' -> cuadrante = cuadrante.charAt(0) + "3";
            default -> {
                return null; // no hay pase en la misma linea
            }
        }
        
        var cercanos = cuadrantesCercanos(cuadrante);
        var proximos = cuadrantesProximos(cuadrante);
        for (var jug : jugadoresCampo()){ 

            if (jug.getCuadrante().equals(cuadrante))
                posiblesA.add(jug);            
            else if (cercanos.contains(jug.getCuadrante()))
                posiblesA.add(jug);            
            else if (proximos.contains(jug.getCuadrante()))
                posiblesB.add(jug);            
        }
        
        if (jugP == null && !posiblesA.isEmpty())
            jugP = posiblesA.get(valorAleatorio(posiblesA.size()));
        else if (jugP == null && !posiblesB.isEmpty())
            jugP = posiblesB.get(valorAleatorio(posiblesB.size()));
        
        return jugP;
    }
    
    public JugadorFutbol8 jugadorParaPaseAtras(String cuadrante){
        
        JugadorFutbol8 jugP = null;
        var posiblesA = new ArrayList<JugadorFutbol8>();
        var posiblesB = new ArrayList<JugadorFutbol8>();
        
        switch (cuadrante.charAt(1)) {
            case '3' -> cuadrante = cuadrante.charAt(0) + "2";
            case '2' -> cuadrante = cuadrante.charAt(0) + "1";
            default -> {
                return null; // no hay hacia atras
            }
        }
        
        var cercanos = cuadrantesCercanos(cuadrante);
        var proximos = cuadrantesProximos(cuadrante);
        for (var jug : jugadoresCampo()){ 

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
            jugP = posiblesA.get(valorAleatorio(posiblesA.size()));
        else if (jugP == null && !posiblesB.isEmpty())
            jugP = posiblesB.get(valorAleatorio(posiblesB.size()));
        
        return jugP;
    }
    
    public JugadorFutbol8 jugadorParaPaseLargo(String cuadranteOri){
        
        JugadorFutbol8 jugP = null;
        var posiblesA = new ArrayList<JugadorFutbol8>();
        var posiblesB = new ArrayList<JugadorFutbol8>();
        var cuadrante = cuadranteOri.charAt(0) + "3";
        var cercanos = cuadrantesCercanos(cuadrante);
        var proximos = cuadrantesProximos(cuadrante);
        for (var jug : jugadoresCampo()){ 
            if (cercanos.contains(jug.getCuadrante()))
                posiblesA.add(jug);            
            else if (proximos.contains(jug.getCuadrante()))
                posiblesB.add(jug);            
        }  
        
        if (jugP == null && !posiblesA.isEmpty())
            jugP = posiblesA.get(valorAleatorio(posiblesA.size()));
        else if (jugP == null && !posiblesB.isEmpty())
            jugP = posiblesB.get(valorAleatorio(posiblesB.size()));
        
        // si no encontramos ninguno de estos buscamos a cualquiera 
        // que este en la delantera
        if (jugP == null){
            for (var jug : jugadoresCampo()){ 
                if (jug.getCuadrante().equals(cuadranteOri)) {
                } else if (jug.getCuadrante().charAt(1) == '3')
                    posiblesA.add(jug);                
            }
            if (!posiblesA.isEmpty())
            jugP = posiblesA.get(valorAleatorio(posiblesA.size()));                
        }
        
        return jugP;
        
        
    }
    
    
    private ArrayList<String> cuadrantesCercanos(String cuadrante){
        
        var cuadrantes = new ArrayList<String>();
        
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
    
    public JugadorFutbol8 jugadorContrarioCercano(String cuadrante){

        JugadorFutbol8 jugC = null;
        var jugs = new ArrayList<JugadorFutbol8>();
        var cercanos = cuadrantesCercanos(cuadranteOpuesto(cuadrante));
        for (var jug : jugadoresCampo()) 
            if (cercanos.contains(jug.getCuadrante()))
                jugs.add(jug);
                
        if (!jugs.isEmpty()) 
            jugC = jugs.get(valorAleatorio(jugs.size()));
        
        return jugC;
    }
    
    private ArrayList<String> cuadrantesProximos(String cuadrante){
        // Devuelve el cuadrante siguiente al mas cercano
        var cuadrantes = new ArrayList<String>();
        
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
    
    public JugadorFutbol8 jugadorContrarioProximo(String cuadrante){
        // Devuelve el jugador siguiente al mas cercano
        JugadorFutbol8 jugC = null;
        var jugs = new ArrayList<JugadorFutbol8>();
        var cercanos = cuadrantesProximos(cuadranteOpuesto(cuadrante));
        for (var jug : jugadoresCampo()) 
            if (cercanos.contains(jug.getCuadrante()))
                jugs.add(jug);
                
        if (!jugs.isEmpty()) 
            jugC = jugs.get(valorAleatorio(jugs.size()));
        
        return jugC;
    }
    
    
    public void validarAlineacion(){
        
        var lista = new ArrayList<>();
                  
        if (this.getPortero() != null)
            if (!this.getPortero().isPuedeJugar() ||              
                    this.getPortero().getPosicion() != Portero){
                this.setPortero(null);
                this.setCuadrantep("");          
        }
        
        if (this.getJugador1() != null)
            if (!this.getJugador1().isPuedeJugar() ||
                    this.getJugador1().getPosicion() == Portero){
                this.setJugador1(null);
                this.setCuadrante1("");
            } else
                lista.add(this.getJugador1());        
              
        if (this.getJugador2() != null)
            if (!this.getJugador2().isPuedeJugar() ||
                lista.contains(this.getJugador2()) ||
                this.getJugador2().getPosicion() == Portero){
            this.setJugador2(null);
            this.setCuadrante2("");
            }else 
                lista.add(this.getJugador2());
        
        if (this.getJugador3() != null)
            if (!this.getJugador3().isPuedeJugar() ||
                lista.contains(this.getJugador3()) ||
                this.getJugador3().getPosicion() == Portero){
            this.setJugador3(null);
            this.setCuadrante3("");
            }else
                lista.add(this.getJugador3());
        
        if (this.getJugador4() != null)
            if (!this.getJugador4().isPuedeJugar() ||
                lista.contains(this.getJugador4()) ||
                this.getJugador4().getPosicion() == Portero){
            this.setJugador4(null);
            this.setCuadrante4("");
            }else
                lista.add(this.getJugador4());
        
        if (this.getJugador5() != null)
            if (!this.getJugador5().isPuedeJugar() ||
                lista.contains(this.getJugador5()) ||
                this.getJugador5().getPosicion() == Portero){
            this.setJugador5(null);
            this.setCuadrante5("");
            } else
                lista.add(this.getJugador5());
        
        if (this.getJugador6() != null)
            if (!this.getJugador6().isPuedeJugar() ||
                lista.contains(this.getJugador6()) ||
                this.getJugador6().getPosicion() == Portero){
            this.setJugador6(null);
            this.setCuadrante6("");
            }else 
                lista.add(this.getJugador6());
        
        if (this.getJugador7() != null)
            if (!this.getJugador7().isPuedeJugar() ||
                lista.contains(this.getJugador7()) ||
                this.getJugador7().getPosicion() == Portero){
            this.setJugador7(null);
            this.setCuadrante7("");
            } 
        
    }

    public void limpiarAlineacion(){

        this.setPortero(null);
        this.setJugador1(null);
        this.setJugador2(null);
        this.setJugador3(null);
        this.setJugador4(null);
        this.setJugador5(null);
        this.setJugador6(null);
        this.setJugador7(null);

        this.setCuadrantep(null);
        this.setCuadrante1(null);
        this.setCuadrante2(null);
        this.setCuadrante3(null);
        this.setCuadrante4(null);
        this.setCuadrante5(null);
        this.setCuadrante6(null);
        this.setCuadrante7(null);

    }

    public boolean setDato(String posicion, JugadorFutbol8 jug){

        // primero comprobamos si el puesto ya esta rellenado si
        // es asi no hacemos nada
        
        var asignado= false;
        var jugadores = this.jugadores();
        var isAliniado = false;
        for (var jugAlineado : jugadores) {
            if (jugAlineado.equals(jug)) isAliniado = true;
            break;
        }
        if (isAliniado) return asignado;
        
        if (posicion.equals("P0")){
            this.setPortero(jug);
            this.setCuadrantep(posicion);  
            asignado = true;
        }else if (this.getJugador1() == null){
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
        }else if (this.getJugador6() == null){
            this.setJugador6(jug);
            this.setCuadrante6(posicion);
            asignado = true;
        }else if (this.getJugador7() == null){
            this.setJugador7(jug);
            this.setCuadrante7(posicion);
            asignado = true;
        }
        
        if (asignado) jug.setJuegaJornada(true);
        
        return asignado;
        
    }
    
   public static String codificarJugador(JugadorFutbol8 jug, String cuadrante){
       
       var txt = new StringBuilder();
       
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
   
   public static String codificarJugadores(ArrayList<JugadorFutbol8> jugadores){
       
       var txt = new StringBuilder();
        var primero = true;
       for (var jug : jugadores) {
           if (primero) primero = false; 
           else txt.append(";");
           txt.append(codificarJugador(jug, null));
       }
       
       return txt.toString();       
   }

   public String getAlineacionCodificada(){

        var txt = new StringBuilder();

        if (this.getPortero() != null)
            txt.append(codificarJugador(this.getPortero(), this.getCuadrantep()));

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

       if (this.getJugador6() != null) {
            txt.append(";");
            txt.append(codificarJugador(this.getJugador6(), this.getCuadrante6()));
        }

       if (this.getJugador7() != null) {
            txt.append(";");
            txt.append(codificarJugador(this.getJugador7(), this.getCuadrante7()));
        }
        
        return txt.toString();
    }
   
   public String alineacionParaEstadistica(){
       
       var txt = new StringBuilder();
       JugadorFutbol8[] jugs = {
           this.getPortero(),
           this.getJugador1(),
           this.getJugador2(),
           this.getJugador3(),
           this.getJugador4(),
           this.getJugador5(),
           this.getJugador6(),
           this.getJugador7()           
       };
       
       // habria que mirar porque el cuandrante del portero no esta 
       // informado, pero de momento lo pongo a piñon
       if (this.getPortero() != null)
           this.getPortero().setCuadrante("P0");       
       
        var primero = true;
       for (var jug : jugs) {
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
   
   public static ArrayList<JugadorFutbol8> parseAlineacion(String txt){
       
        var alineacion = new ArrayList<JugadorFutbol8>();
       for (var datos : txt.split("&&")[0].split("&")) {
            var dato = datos.split(";");
           if (dato.length == 5){
               var jug = new JugadorFutbol8();
               jug.setNombre(dato[0]);
               // para compatibilizar
               if (dato[1].equals("Centrocampista")) dato[1] = "Medio";
               jug.setPosicion(valueOf(dato[1]));
               jug.setValoracion(parseInt(dato[2]) * 10);
               jug.setValoracionPartido(parseInt(dato[3]));
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
       
        var pos = new  ArrayList<String>();
       if (this.getCuadrante1() != null) pos.add(this.getCuadrante1());
       if (this.getCuadrante2() != null) pos.add(this.getCuadrante2());
       if (this.getCuadrante3() != null) pos.add(this.getCuadrante3());
       if (this.getCuadrante4() != null) pos.add(this.getCuadrante4());
       if (this.getCuadrante5() != null) pos.add(this.getCuadrante5());
       if (this.getCuadrante6() != null) pos.add(this.getCuadrante6());
       if (this.getCuadrante7() != null) pos.add(this.getCuadrante7());
       
       return pos;       
       
   }

}
