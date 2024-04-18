/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mld.clubdeportivo.base.basket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import mld.clubdeportivo.base.Equipo;
import mld.clubdeportivo.base.Seccion;
import mld.clubdeportivo.base.basket.EntrenadorBasket;
import mld.clubdeportivo.base.basket.EspectativaBasket;
import mld.clubdeportivo.base.basket.JuvenilBasket;
import mld.clubdeportivo.base.basket.PosicionElegidaBasket;
import mld.clubdeportivo.base.basket.PuntuacionBasket;
import mld.clubdeportivo.base.basket.AlineacionBasket;
import mld.clubdeportivo.base.basket.EntrenadorBasket;
import mld.clubdeportivo.base.basket.EquipoBasket;
import static mld.clubdeportivo.base.basket.EquipoBasket.NUMERO_MINIMO_JUGADORES;
import mld.clubdeportivo.base.basket.EsfuerzoBasket;
import mld.clubdeportivo.base.basket.EspectativaBasket;
import mld.clubdeportivo.base.basket.EstrategiaBasket;
import mld.clubdeportivo.base.basket.JugadorBasket;
import mld.clubdeportivo.base.basket.JuvenilBasket;
import mld.clubdeportivo.base.basket.PosicionElegidaBasket;
import mld.clubdeportivo.base.basket.PosicionJugBasket;
import mld.clubdeportivo.base.basket.PuntuacionBasket;
import mld.clubdeportivo.base.basket.TacticaBasket;
import mld.clubdeportivo.utilidades.Calculos;

/**
 *
 * @author mlopezd
 */
public final class EquipoBasket extends Equipo {
    
    public static final int NUMERO_MIN_JUGADORES = 5;
    public static final int NUMERO_MAX_JUGADORES = 12;
    public static final int NUMERO_MAX_EQ_TECNICO = 12; // debe ser multiplo de 4
    public static final int MORAL_INICIAL = 100;
    public static final int DIAS_WARNING = 15;
    public static final int DIAS_DESACTIVAR = 20;
    public static final int NUMERO_MINIMO_JUGADORES = 5;
    
    private ArrayList<JugadorBasket> jugadores;
    private EntrenadorBasket entrenador;
    private ArrayList<PuntuacionBasket> puntuaciones;
    private PuntuacionBasket puntuacion;
    private AlineacionBasket alineacion;
    private int equipacion;
    private EspectativaBasket espectativa;
    private PosicionElegidaBasket posicionJuvenil;
    private JuvenilBasket juvenil;
    
    // Esto es para el partido, no esta en BD
    protected StringBuilder goleadores = new StringBuilder(); 
    protected StringBuilder tarjetas = new StringBuilder();
    protected StringBuilder lesionados = new StringBuilder();
    protected StringBuilder extras = new StringBuilder();
    protected int posesion;
    protected int jugadas;
    protected int corners = 0;
    protected int faltasDir = 0;
    protected int penalties = 0;
    protected int goles = 0;
    //TIROS A PUERTA
    protected int tirLej = 0;
    protected int tirArea = 0;
    
    
    public ArrayList<String> getTiposEsfuerzo(){

        ArrayList<String> lista = new ArrayList<String>();

        for (EsfuerzoBasket tipo : EsfuerzoBasket.values()) {
            lista.add(tipo.name());
        }

        return lista;

    }

    public ArrayList<String> getTiposEstrategia(){

        ArrayList<String> lista = new ArrayList<String>();

        for (EstrategiaBasket tipo : EstrategiaBasket.values()) {
            lista.add(tipo.name());
        }

        return lista;

    }
   

    public ArrayList<JugadorBasket> getJugadores() {
        Collections.sort(jugadores, new JugadorBasket.PosicionComparator());
        return jugadores;
    }
    
    

    public ArrayList<JugadorBasket> getJugadoresAliniables() {

        ArrayList<JugadorBasket> lista = new ArrayList<JugadorBasket>();

        for (JugadorBasket jug : this.getJugadores()) {
            if (jug.getJornadasLesion() == 0)
                lista.add(jug);
        }

        return lista;
    }
    
     public ArrayList<JugadorBasket> getJugadoresBanquillo() {

        ArrayList<JugadorBasket> lista = new ArrayList<JugadorBasket>();

        for (JugadorBasket jug : this.getJugadoresAliniables()) {
            if (!jug.isJuegaJornada()) lista.add(jug);
        }
      
        return lista;
    }
    
     public ArrayList<JugadorBasket> getJugadoresAliniables(PosicionJugBasket pos) {

        ArrayList<JugadorBasket> lista = new ArrayList<JugadorBasket>();

        for (JugadorBasket jug : this.getJugadoresAliniables()) {
            if (jug.getPosicion() == pos) lista.add(jug);
        }

        return lista;
    }
     
     public ArrayList<JugadorBasket> getJugadoresAliniablesNoAliniados(PosicionJugBasket pos) {

        ArrayList<JugadorBasket> lista = new ArrayList<JugadorBasket>();

        for (JugadorBasket jug : this.getJugadoresAliniables(pos)) {
            if (!jug.isJuegaJornada()) lista.add(jug);
        }

        return lista;
    }
    
    public ArrayList<JugadorBasket> getJugadoresEntrenables() {

        ArrayList<JugadorBasket> lista = new ArrayList<JugadorBasket>();

        for (JugadorBasket jug : this.getJugadores()) {
            if (jug.getJornadasLesion() == 0)
                lista.add(jug);
        }

        return lista;
    }
    
    public ArrayList<JugadorBasket> getJugadoresEntrenables(PosicionJugBasket pos) {

        ArrayList<JugadorBasket> lista = new ArrayList<JugadorBasket>();

        for (JugadorBasket jug : this.getJugadores()) {
            if (jug.getJornadasLesion() == 0 && jug.getPosicion().equals(pos))
                lista.add(jug);
        }

        return lista;
    }
    

         
      public ArrayList<JugadorBasket> getJugadoresLesionados() {

        ArrayList<JugadorBasket> lista = new ArrayList<JugadorBasket>();

        for (JugadorBasket jug : this.getJugadores()) {
            if (jug.getJornadasLesion() > 0)
                lista.add(jug);
        }

        return lista;
    }

    public JugadorBasket getJugador(Long id){

        JugadorBasket jug = null;
        if (id != null){
            if (this.getJugadores() != null) {
                for (JugadorBasket jugador : this.getJugadores()) {
                    if (jugador.getId() == id.longValue()) {
                        jug = jugador;
                        break;
                    }
                }
            }
        }

        return jug;
    }

    public void setJugadores(ArrayList<JugadorBasket> jugadores) {
        this.jugadores = jugadores;
    }

    public EntrenadorBasket getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(EntrenadorBasket entrenador) {
        this.entrenador = entrenador;
    }

    public int getValoracionMediaJugadores(){
        
        int media = 0;
        
        if (!this.getJugadores().isEmpty()){
            int suma = 0; 
            for (JugadorBasket jug : this.getJugadores()) 
                suma = suma + jug.getValoracionReal();
            media = suma / this.getJugadores().size();
        }
        return media;        
    }   
    
    public boolean obtenerOjeado(){
        // con 1 tenemos 1%, con 4 un 4%
        // tambien calcula si se pierde algun ojeador
        
        boolean obtener = false;
     
        if (this.getOjeadores() == 0) return obtener;
        
        int base = 50;
        
        obtener = Calculos.obtenerResultado(this.getOjeadores(), base);
        
        if (obtener)
            this.setJugadoresOjeados(this.getJugadoresOjeados() + 1);        
             
        return obtener;
        
        
    }
    
    public boolean perderOjeador(){
        // con 1 tenemos 0.5%, con 4 un 2%
        // tambien calcula si se pierde algun ojeador
        
        boolean pierde = false;
     
        if (this.getOjeadores() == 0) return pierde;
        
        int base = 100;
        
        pierde = Calculos.obtenerResultado(this.getOjeadores(), base);
        
        if (pierde){
            this.setOjeadores(this.getOjeadores() - 1);
            pierde = true;
        }
        
        return pierde;
        
        
    }
    

     public void modificarMoral(int moral) {         
         
         if (this.isAutomatico()) return;
         this.setMoral(moral);
         if (this.getMoral() > 100)
             this.setMoral(100);
         if (this.getMoral() < 10)
             this.setMoral(10);
         
    }
     
     public EstrategiaBasket estrategia(){
         
         EstrategiaBasket est = null;
         
         if (this.getAlineacion() != null)
             est = this.getAlineacion().getEstrategia();
         
         return est;
         
     }
     
     public boolean isAbandonado(){
        
         return this.getClub().getDiasSinAcceder() > EquipoBasket.DIAS_DESACTIVAR ||
                 this.getJugadores().size() < NUMERO_MINIMO_JUGADORES;
         
     }
    
   
    @Override
    public String toString() {

        StringBuilder txt = new StringBuilder();

        txt.append("EQUIPO\n");
        txt.append(String.format("- Id: %d \n", this.getId()));
        txt.append(String.format("- Activo: %b \n", this.isActivo()));
        txt.append(String.format("- Automatico: %b \n", this.isAutomatico()));
        txt.append(String.format("- Club: %s \n", this.getClub().getNombre()));
        txt.append(String.format("- Moral: %s \n", this.getMoral()));
        txt.append(String.format("- Campo: %s \n", this.getCampo()));
        txt.append(String.format("- Publicidad: %s \n", this.getPublicidad()));
        txt.append(String.format("- Equipo Tecnico: %s \n", this.getEqTecnico()));
        txt.append(String.format("- Ojeadores: %s \n", this.getOjeadores()));
        txt.append(String.format("- Jugadores Ojeados: %s \n", this.getJugadoresOjeados()));
        txt.append(String.format("- Presupuesto: %d \n", this.getPresupuesto()));
        txt.append(String.format("- Credito: %d \n", this.getCredito()));
        txt.append(String.format("- Bolsa: %d \n", this.getBolsa()));
        txt.append(String.format("- Entrenamiento: %b \n", this.isEntrenamiento()));
        txt.append(String.format("- Simulaciones: %s \n", this.getSimulaciones()));
        txt.append(String.format("- Modo Auto: %b \n", this.isModoAuto()));

        return txt.toString();
    }

    public int compareTo(Seccion o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ArrayList<PuntuacionBasket> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(ArrayList<PuntuacionBasket> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public PuntuacionBasket getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(PuntuacionBasket puntuacion) {
        this.puntuacion = puntuacion;
    }


    public static  ArrayList<EquipoBasket> clasificarEquipos(
            ArrayList<EquipoBasket> eqs) {
        //Devuelve los equipos por su clasificacion

        Collections.sort(eqs, new ClasificacionComparator());

        return eqs;
    }

    public static  ArrayList<EquipoBasket> clasificarEquipos(
            ArrayList<EquipoBasket> eqs, int numero) {
        // Devuelve los equipos por su clasificacion, pero
        // solo los primeros indicados en el numero

        if (numero > eqs.size())
            throw new IllegalArgumentException("El numero no puede ser mayor que los elementos");

        Collections.sort(eqs, new ClasificacionComparator());

        ArrayList<EquipoBasket> lista = new ArrayList<EquipoBasket>();

        for (int i = 0; i < numero; i++)
            lista.add(eqs.get(i));

        return lista;
    }

    public AlineacionBasket getAlineacion() { 
        return alineacion;
    }

    public void setAlineacion(AlineacionBasket alineacion) {
        this.alineacion = alineacion;
    }

    public void completarAlineacion() {

        AlineacionBasket ali = this.getAlineacion();
        if (ali.getTactica() == null)
            hacerAlineacionAutomatica();
        else
            hacerAlineacionAutomatica(ali.getTactica(), false);       

     }
    
    public void hacerAlineacionAutomatica() {

        int num;
        ArrayList<TacticaBasket> tacticas = this.getEntrenador().getTacticas();
        num = Calculos.valorAleatorio(tacticas.size());
        TacticaBasket tac = tacticas.get(num);
        
        AlineacionBasket ali = this.getAlineacion();
        
        // Elegimos entre normal y otra estrategia si es un automatico        
        EstrategiaBasket est;
        if (this.isAutomatico()){
            if (Calculos.obtener(2)){
                int numEst = Calculos.valorAleatorio(EstrategiaBasket.values().length);
                est = EstrategiaBasket.values()[numEst];
            }
            else 
                est = EstrategiaBasket.Normal;
            ali.setEstrategia(est);
        }

        hacerAlineacionAutomatica(tac, true);

     }
    public void hacerAlineacionAutomatica(TacticaBasket tac, boolean limpiar) {

        int num;

        AlineacionBasket ali = this.getAlineacion();
      
        if (limpiar){
            ali.limpiarAlineacion();
            for (Iterator<JugadorBasket> it = this.getJugadores().iterator(); it.hasNext();) {
                JugadorBasket jug = it.next();
                jug.setJuegaJornada(false);
            }
        }

        ali.setTactica(tac);
        ali.setEquipo(this);
        ali.setEntrenador(this.getEntrenador());
   
        ArrayList<JugadorBasket> base =
                this.getJugadoresAliniablesNoAliniados(PosicionJugBasket.Base);
        Collections.shuffle(base);
        ArrayList<JugadorBasket> escolta =
                this.getJugadoresAliniablesNoAliniados(PosicionJugBasket.Escolta);
        Collections.shuffle(escolta);
        ArrayList<JugadorBasket> alero =
                this.getJugadoresAliniablesNoAliniados(PosicionJugBasket.Alero);
        Collections.shuffle(alero);
        ArrayList<JugadorBasket> alaPivot =
                this.getJugadoresAliniablesNoAliniados(PosicionJugBasket.AlaPivot);
        Collections.shuffle(alaPivot);
        ArrayList<JugadorBasket> pivot =
                this.getJugadoresAliniablesNoAliniados(PosicionJugBasket.Pivot);
        Collections.shuffle(pivot);
       
        ArrayList<mld.clubdeportivo.base.basket.PosicionCuadrante> posPendientes =
                new ArrayList<mld.clubdeportivo.base.basket.PosicionCuadrante>();

        num = 0;
        ArrayList<mld.clubdeportivo.base.basket.PosicionCuadrante> posDef = tac.getCuadranteDefensa();
        for (mld.clubdeportivo.base.basket.PosicionCuadrante pos : posDef) {
            if (ali.posicionesCubiertas().contains(pos.getPosicion()))
                num++;
            else if (pos.isActiva())
                if(!defensas.isEmpty()){
                    if (ali.setDato(pos.getPosicion(), defensas.get(0)))
                        defensas.remove(0);
                    num++;}
                else
                    posPendientes.add(pos);
        }

        ArrayList<mld.clubdeportivo.base.basket.PosicionCuadrante> posCen = tac.getCuadranteCentrocampo();
        for (mld.clubdeportivo.base.basket.PosicionCuadrante pos : posCen) {
            if (ali.posicionesCubiertas().contains(pos.getPosicion()))
                num++;
            else if (pos.isActiva())
                if(!centros.isEmpty()){
                    if (ali.setDato(pos.getPosicion(), centros.get(0)))                    
                        centros.remove(0);
                    num++;}
                else
                    posPendientes.add(pos);
        }

        ArrayList<mld.clubdeportivo.base.basket.PosicionCuadrante> posDel = tac.getCuadranteDelantera();
        for (mld.clubdeportivo.base.basket.PosicionCuadrante pos : posDel) {
            if (ali.posicionesCubiertas().contains(pos.getPosicion()))
                num++;
            else if (pos.isActiva())
                if(!delanteros.isEmpty()){
                    if (ali.setDato(pos.getPosicion(), delanteros.get(0)))                    
                        delanteros.remove(0);
                    num++;}
                else
                    posPendientes.add(pos);
        }
        
        /* Si no hemos podido completar los jugadores en su
           posicion lo hacemos en cualquiera */

        ArrayList<JugadorBasket> jugRestantes = new ArrayList<JugadorBasket>();

        jugRestantes.addAll(defensas);
        jugRestantes.addAll(centros);
        jugRestantes.addAll(delanteros);
        Collections.shuffle(jugRestantes);
        for (int i = 7 - num; i > 0; i--){
            if (jugRestantes.isEmpty()) break;
            ali.setDato(posPendientes.get(0).getPosicion(), jugRestantes.get(0));
            posPendientes.remove(0);
            jugRestantes.remove(0);
        }
        
        for (JugadorBasket jug : this.getJugadores()) {
            for (JugadorBasket jugAli : this.getAlineacion().jugadores()) {
                if (jug.equals(jugAli)) {
                    jug.setJuegaJornada(true);
                    jugAli.setJuegaJornada(true);
                }
            }
        }
        

    }
    
   public void marcarJugadoresAliniados() {
       // Este metodo sirve para marcar los jugadores que juegan en
       // el equipo simulado, ya que esa info no se guarda

        AlineacionBasket ali = this.getAlineacion();
        
        if (ali.getPortero() != null)
            for (JugadorBasket jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getPortero()))
                        jug.setJuegaJornada(true);  

        if (ali.getJugador1() != null)
            for (JugadorBasket jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador1()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador2() != null)
            for (JugadorBasket jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador2()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador3() != null)
            for (JugadorBasket jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador3()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador4() != null)
            for (JugadorBasket jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador4()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador5() != null)
            for (JugadorBasket jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador5()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador6() != null)
            for (JugadorBasket jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador6()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador7() != null)
            for (JugadorBasket jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador7()))
                        jug.setJuegaJornada(true); 

   }
   
   public void limpiarJugadoresQueJuegan(){
       
       for (JugadorBasket jug : this.getJugadores()) 
           jug.setJuegaJornada(false);
              
   }
   
    public void tratarCaracteristicasJugadoresenUltimaJornada(ArrayList<JugadorBasket> pichichis){
       
       for (JugadorBasket jug : this.getJugadores()) {
           for (JugadorBasket pich : pichichis) {
               if (pich.getId() == jug.getId()){
                   int newValor = jug.getValoracion() + 50;
                   if (newValor > 1000) newValor = 1000;
                   jug.setValoracion(newValor);
               }
           }   
           jug.setEstadoFisico(100);
           jug.setGolesLiga(0);
           jug.setGolesCopa(0);
           jug.setPartidosJugados(0);
       }
              
   }
   
    
   public static ArrayList<EquipoBasket> equiposNoAuto(ArrayList<EquipoBasket> eqs){
        
        ArrayList<EquipoBasket> lista = new ArrayList<EquipoBasket>();
        for (EquipoBasket eq : eqs) 
            if (!eq.isAutomatico()) lista.add(eq);
        
        return lista;        
    }
    
    public static ArrayList<EquipoBasket> equiposAuto(ArrayList<EquipoBasket> eqs){
        
        ArrayList<EquipoBasket> lista = new ArrayList<EquipoBasket>();
        for (EquipoBasket eq : eqs) 
            if (eq.isAutomatico()) lista.add(eq);
        
        return lista;        
    }
    
    public static ArrayList<EquipoBasket> equiposNoActivos(ArrayList<EquipoBasket> eqs){
        
        ArrayList<EquipoBasket> lista = new ArrayList<EquipoBasket>();
        for (EquipoBasket eq : eqs) 
            if (!eq.isActivo()) lista.add(eq);
        
        return lista;        
    }
    
    public static ArrayList<EquipoBasket> equiposAutoAEliminar(ArrayList<EquipoBasket> equipos){
        
        
        ArrayList<EquipoBasket> eqsElim = new ArrayList<EquipoBasket>();
        ArrayList<EquipoBasket> eqsAuto = equiposAuto(equipos);
        
        int numEqsNuevos = equiposNoActivos(equipos).size();
        int numEqsAuto = eqsAuto.size();  
        int numEqsElim = 0;        
        
        if (numEqsNuevos > 0)
            if (numEqsAuto >= numEqsNuevos)
                numEqsElim = numEqsNuevos;
            else
                numEqsElim = numEqsAuto;
        
        if (numEqsElim > 0){        
            for (int i = 0; i < numEqsElim; i++){
                eqsElim.add(eqsAuto.get(i));
            }
        }        
        
        return eqsElim;
    }


    public int getEquipacion() {
        return equipacion;
    }

    public void setEquipacion(int equipacion) {
        this.equipacion = equipacion;
    }

    public EspectativaBasket getEspectativa() {
        return espectativa;
    }

    public void setEspectativa(EspectativaBasket espectativa) {
        this.espectativa = espectativa;
    }


    public PosicionElegidaBasket getPosicionJuvenil() {
        return posicionJuvenil;
    }

    public void setPosicionJuvenil(PosicionElegidaBasket posicionJuvenil) {
        this.posicionJuvenil = posicionJuvenil;
    }

    public JuvenilBasket getJuvenil() {
        return juvenil;
    }

    public void setJuvenil(JuvenilBasket juvenil) {
        this.juvenil = juvenil;
    }
    
    private static class ClasificacionComparator
            implements Comparator<EquipoBasket> {

        public int compare(EquipoBasket o1, EquipoBasket o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");

            PuntuacionBasket pto1 = o1.getPuntuacion();
            PuntuacionBasket pto2 = o2.getPuntuacion();

            int result = 0;
            if (pto1.getPuntos() > pto2.getPuntos()) result = -1;
            else if (pto1.getPuntos() < pto2.getPuntos()) result = 1;
            else if (pto1.getDiferenciaGoles() > pto2.getDiferenciaGoles()) result = -1;
            else if (pto1.getDiferenciaGoles() < pto2.getDiferenciaGoles()) result = 1;
            else if (pto1.getVictorias() > pto2.getVictorias()) result = -1;
            else if (pto1.getVictorias() < pto2.getVictorias()) result = 1;

            return result;
        }
    }

    public static Comparator<EquipoBasket> ClasificacionComparator(){

        return new ClasificacionComparator();

    }

   
    
}
