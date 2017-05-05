/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mld.clubdeportivo.base.futbol8;

import java.util.*;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Equipo;
import mld.clubdeportivo.base.Seccion;
import mld.clubdeportivo.base.futbol8.JugadorFutbol8.PosicionComparator;
import mld.clubdeportivo.utilidades.Calculos;

/**
 *
 * @author Miguel
 */
public final class EquipoFutbol8 extends Equipo {
    
    public static final int NUMERO_MIN_JUGADORES = 8;
    public static final int NUMERO_MAX_JUGADORES = 16;
    public static final int NUMERO_MAX_EQ_TECNICO = 12; // debe ser multiplo de 4
    public static final int MORAL_INICIAL = 100;
    public static final int DIAS_WARNING = 15;
    public static final int DIAS_DESACTIVAR = 20;
    public static final int NUMERO_MINIMO_JUGADORES = 5;
    
    private ArrayList<JugadorFutbol8> jugadores;
    private EntrenadorFutbol8 entrenador;
    private ArrayList<PuntuacionFutbol8> puntuaciones;
    private PuntuacionFutbol8 puntuacion;
    private AlineacionFutbol8 alineacion;
    private int equipacion;
    private EspectativaFutbol8 espectativa;
    private PosicionElegidaFutbol8 posicionJuvenil;
    private JuvenilFutbol8 juvenil;
    
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

    public EquipoFutbol8(){}


    public EquipoFutbol8(Club club, boolean automatico,
            ArrayList<JugadorFutbol8> jugadores){

        this.setActivo(false);
        this.setAutomatico(automatico);
        this.setClub(club);
        this.setMoral(MORAL_INICIAL);
        this.setCampo(CAPACIDAD_ESPECTADORES_INICIAL);
        this.setEqTecnico(0);
        this.setPublicidad(0);
        this.setOjeadores(0);
        this.setJugadoresOjeados(0);
        this.setPresupuesto(PRESUPUESTO_INICIAL);
        this.setCredito(0);
        this.setBolsa(0);
        this.setEntrenamiento(false);
        this.setSimulaciones(0);
        this.setModoAuto(false);        
        this.setJugadores(jugadores);
        this.setPrecioEntradas(PRECIO_INICIAL);
        this.setEquipacion(0);
        this.setEspectativa(EspectativaFutbol8.Normal);
        this.setPosicionJuvenil(PosicionElegidaFutbol8.Cualquiera);

    };

    public ArrayList<String> getTiposEsfuerzo(){

        ArrayList<String> lista = new ArrayList<String>();

        for (EsfuerzoFutbol8 tipo : EsfuerzoFutbol8.values()) {
            lista.add(tipo.name());
        }

        return lista;

    }

    public ArrayList<String> getTiposEstrategia(){

        ArrayList<String> lista = new ArrayList<String>();

        for (EstrategiaFutbol8 tipo : EstrategiaFutbol8.values()) {
            lista.add(tipo.name());
        }

        return lista;

    }
   

    public ArrayList<JugadorFutbol8> getJugadores() {
        Collections.sort(jugadores, new PosicionComparator());
        return jugadores;
    }
    
    public JugadorFutbol8 portero(){
        
        JugadorFutbol8 por = null;
        
        if (this.getAlineacion() != null)
            por = this.getAlineacion().getPortero();
        
        return por;
        
    }
    
    public ArrayList<JugadorFutbol8> getPorterosAliniables() {

        ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();

        for (JugadorFutbol8 jug : this.getJugadores()) {
            if (!jug.getPosicion().equals(PosicionJugFutbol8.Portero)) continue;
            if (!jug.isTarjetaRoja() && jug.getJornadasLesion() == 0)
                lista.add(jug);
        }

        return lista;
    }

    public ArrayList<JugadorFutbol8> getJugadoresAliniables() {

        ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();

        for (JugadorFutbol8 jug : this.getJugadores()) {
            if (jug.getPosicion().equals(PosicionJugFutbol8.Portero)) continue;
            if (!jug.isTarjetaRoja() && jug.getJornadasLesion() == 0)
                lista.add(jug);
        }

        return lista;
    }
    
     public ArrayList<JugadorFutbol8> getJugadoresBanquillo() {

        ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();

        for (JugadorFutbol8 jug : this.getJugadoresAliniables()) {
            if (!jug.isJuegaJornada()) lista.add(jug);
        }
        for (JugadorFutbol8 jug : this.getPorterosAliniables()) {
            if (!jug.isJuegaJornada()) lista.add(jug);
        }

        return lista;
    }
    
     public ArrayList<JugadorFutbol8> getJugadoresAliniables(PosicionJugFutbol8 pos) {

        ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();

        for (JugadorFutbol8 jug : this.getJugadoresAliniables()) {
            if (jug.getPosicion() == pos) lista.add(jug);
        }

        return lista;
    }
     
     public ArrayList<JugadorFutbol8> getJugadoresAliniablesNoAliniados(PosicionJugFutbol8 pos) {

        ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();

        for (JugadorFutbol8 jug : this.getJugadoresAliniables(pos)) {
            if (!jug.isJuegaJornada()) lista.add(jug);
        }

        return lista;
    }
    
    public ArrayList<JugadorFutbol8> getJugadoresEntrenables() {

        ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();

        for (JugadorFutbol8 jug : this.getJugadores()) {
            if (jug.getJornadasLesion() == 0)
                lista.add(jug);
        }

        return lista;
    }
    
    public ArrayList<JugadorFutbol8> getJugadoresEntrenables(PosicionJugFutbol8 pos) {

        ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();

        for (JugadorFutbol8 jug : this.getJugadores()) {
            if (jug.getJornadasLesion() == 0 && jug.getPosicion().equals(pos))
                lista.add(jug);
        }

        return lista;
    }
    

   
    
     public ArrayList<JugadorFutbol8> getJugadoresSancionados() {

        ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();

        for (JugadorFutbol8 jug : this.getJugadores()) {
            if (jug.isTarjetaRoja())
                lista.add(jug);
        }

        return lista;
    }
     
      public ArrayList<JugadorFutbol8> getJugadoresLesionados() {

        ArrayList<JugadorFutbol8> lista = new ArrayList<JugadorFutbol8>();

        for (JugadorFutbol8 jug : this.getJugadores()) {
            if (jug.getJornadasLesion() > 0)
                lista.add(jug);
        }

        return lista;
    }

    public JugadorFutbol8 getJugador(Long id){

        JugadorFutbol8 jug = null;
        if (id != null){
            if (this.getJugadores() != null) {
                for (JugadorFutbol8 jugador : this.getJugadores()) {
                    if (jugador.getId() == id.longValue()) {
                        jug = jugador;
                        break;
                    }
                }
            }
        }

        return jug;
    }

    public void setJugadores(ArrayList<JugadorFutbol8> jugadores) {
        this.jugadores = jugadores;
    }

    public EntrenadorFutbol8 getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(EntrenadorFutbol8 entrenador) {
        this.entrenador = entrenador;
    }

    public int getValoracionMediaJugadores(){
        
        int media = 0;
        
        if (!this.getJugadores().isEmpty()){
            int suma = 0; 
            for (JugadorFutbol8 jug : this.getJugadores()) 
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
     
     public EstrategiaFutbol8 estrategia(){
         
         EstrategiaFutbol8 est = null;
         
         if (this.getAlineacion() != null)
             est = this.getAlineacion().getEstrategia();
         
         return est;
         
     }
     
     public boolean isAbandonado(){
        
         return this.getClub().getDiasSinAcceder() > EquipoFutbol8.DIAS_DESACTIVAR ||
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

    public ArrayList<PuntuacionFutbol8> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(ArrayList<PuntuacionFutbol8> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public PuntuacionFutbol8 getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(PuntuacionFutbol8 puntuacion) {
        this.puntuacion = puntuacion;
    }


    public static  ArrayList<EquipoFutbol8> clasificarEquipos(
            ArrayList<EquipoFutbol8> eqs) {
        //Devuelve los equipos por su clasificacion

        Collections.sort(eqs, new ClasificacionComparator());

        return eqs;
    }

    public static  ArrayList<EquipoFutbol8> clasificarEquipos(
            ArrayList<EquipoFutbol8> eqs, int numero) {
        // Devuelve los equipos por su clasificacion, pero
        // solo los primeros indicados en el numero

        if (numero > eqs.size())
            throw new IllegalArgumentException("El numero no puede ser mayor que los elementos");

        Collections.sort(eqs, new ClasificacionComparator());

        ArrayList<EquipoFutbol8> lista = new ArrayList<EquipoFutbol8>();

        for (int i = 0; i < numero; i++)
            lista.add(eqs.get(i));

        return lista;
    }

    public AlineacionFutbol8 getAlineacion() { 
        return alineacion;
    }

    public void setAlineacion(AlineacionFutbol8 alineacion) {
        this.alineacion = alineacion;
    }

    public void completarAlineacion() {

        AlineacionFutbol8 ali = this.getAlineacion();
        if (ali.getTactica() == null)
            hacerAlineacionAutomatica();
        else
            hacerAlineacionAutomatica(ali.getTactica(), false);       

     }
    
    public void hacerAlineacionAutomatica() {

        int num;
        ArrayList<TacticaFutbol8> tacticas = this.getEntrenador().getTacticas();
        num = Calculos.valorAleatorio(tacticas.size());
        TacticaFutbol8 tac = tacticas.get(num);
        
        AlineacionFutbol8 ali = this.getAlineacion();
        
        // Elegimos entre normal y otra estrategia si es un automatico        
        EstrategiaFutbol8 est;
        if (this.isAutomatico()){
            if (Calculos.obtener(2)){
                int numEst = Calculos.valorAleatorio(EstrategiaFutbol8.values().length);
                est = EstrategiaFutbol8.values()[numEst];
            }
            else 
                est = EstrategiaFutbol8.Normal;
            ali.setEstrategia(est);
        }

        hacerAlineacionAutomatica(tac, true);

     }
    public void hacerAlineacionAutomatica(TacticaFutbol8 tac, boolean limpiar) {

        int num;

        AlineacionFutbol8 ali = this.getAlineacion();
      
        if (limpiar){
            ali.limpiarAlineacion();
            for (Iterator<JugadorFutbol8> it = this.getJugadores().iterator(); it.hasNext();) {
                JugadorFutbol8 jug = it.next();
                jug.setJuegaJornada(false);
            }
        }

        ali.setTactica(tac);
        ali.setEquipo(this);
        ali.setEntrenador(this.getEntrenador());
        ArrayList<JugadorFutbol8> porteros =
                this.getPorterosAliniables();
        if (ali.getPortero() == null && !porteros.isEmpty()){
            num = Calculos.valorAleatorio(porteros.size());
            ali.setDato("P0", porteros.get(num));
            ali.setPortero(porteros.get(num));
        }
        
   
        ArrayList<JugadorFutbol8> defensas =
                this.getJugadoresAliniablesNoAliniados(PosicionJugFutbol8.Defensa);
        Collections.shuffle(defensas);
        ArrayList<JugadorFutbol8> centros =
                this.getJugadoresAliniablesNoAliniados(PosicionJugFutbol8.Medio);
        Collections.shuffle(centros);
        ArrayList<JugadorFutbol8> delanteros =
                this.getJugadoresAliniablesNoAliniados(PosicionJugFutbol8.Delantero);
        Collections.shuffle(delanteros);

        ArrayList<PosicionCuadrante> posPendientes =
                new ArrayList<PosicionCuadrante>();

        num = 0;
        ArrayList<PosicionCuadrante> posDef = tac.getCuadranteDefensa();
        for (PosicionCuadrante pos : posDef) {
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

        ArrayList<PosicionCuadrante> posCen = tac.getCuadranteCentrocampo();
        for (PosicionCuadrante pos : posCen) {
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

        ArrayList<PosicionCuadrante> posDel = tac.getCuadranteDelantera();
        for (PosicionCuadrante pos : posDel) {
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

        ArrayList<JugadorFutbol8> jugRestantes = new ArrayList<JugadorFutbol8>();

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
        
        for (JugadorFutbol8 jug : this.getJugadores()) {
            for (JugadorFutbol8 jugAli : this.getAlineacion().jugadores()) {
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

        AlineacionFutbol8 ali = this.getAlineacion();
        
        if (ali.getPortero() != null)
            for (JugadorFutbol8 jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getPortero()))
                        jug.setJuegaJornada(true);  

        if (ali.getJugador1() != null)
            for (JugadorFutbol8 jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador1()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador2() != null)
            for (JugadorFutbol8 jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador2()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador3() != null)
            for (JugadorFutbol8 jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador3()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador4() != null)
            for (JugadorFutbol8 jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador4()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador5() != null)
            for (JugadorFutbol8 jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador5()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador6() != null)
            for (JugadorFutbol8 jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador6()))
                        jug.setJuegaJornada(true);
        
        if (ali.getJugador7() != null)
            for (JugadorFutbol8 jug : this.getJugadoresAliniables()) 
                if (jug.equals(ali.getJugador7()))
                        jug.setJuegaJornada(true); 

   }
   
   public void limpiarJugadoresQueJuegan(){
       
       for (JugadorFutbol8 jug : this.getJugadores()) 
           jug.setJuegaJornada(false);
              
   }
   
    public void tratarCaracteristicasJugadoresenUltimaJornada(ArrayList<JugadorFutbol8> pichichis){
       
       for (JugadorFutbol8 jug : this.getJugadores()) {
           for (JugadorFutbol8 pich : pichichis) {
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
   
    
   public static ArrayList<EquipoFutbol8> equiposNoAuto(ArrayList<EquipoFutbol8> eqs){
        
        ArrayList<EquipoFutbol8> lista = new ArrayList<EquipoFutbol8>();
        for (EquipoFutbol8 eq : eqs) 
            if (!eq.isAutomatico()) lista.add(eq);
        
        return lista;        
    }
    
    public static ArrayList<EquipoFutbol8> equiposAuto(ArrayList<EquipoFutbol8> eqs){
        
        ArrayList<EquipoFutbol8> lista = new ArrayList<EquipoFutbol8>();
        for (EquipoFutbol8 eq : eqs) 
            if (eq.isAutomatico()) lista.add(eq);
        
        return lista;        
    }
    
    public static ArrayList<EquipoFutbol8> equiposNoActivos(ArrayList<EquipoFutbol8> eqs){
        
        ArrayList<EquipoFutbol8> lista = new ArrayList<EquipoFutbol8>();
        for (EquipoFutbol8 eq : eqs) 
            if (!eq.isActivo()) lista.add(eq);
        
        return lista;        
    }
    
    public static ArrayList<EquipoFutbol8> equiposAutoAEliminar(ArrayList<EquipoFutbol8> equipos){
        
        
        ArrayList<EquipoFutbol8> eqsElim = new ArrayList<EquipoFutbol8>();
        ArrayList<EquipoFutbol8> eqsAuto = equiposAuto(equipos);
        
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

    public EspectativaFutbol8 getEspectativa() {
        return espectativa;
    }

    public void setEspectativa(EspectativaFutbol8 espectativa) {
        this.espectativa = espectativa;
    }


    public PosicionElegidaFutbol8 getPosicionJuvenil() {
        return posicionJuvenil;
    }

    public void setPosicionJuvenil(PosicionElegidaFutbol8 posicionJuvenil) {
        this.posicionJuvenil = posicionJuvenil;
    }

    public JuvenilFutbol8 getJuvenil() {
        return juvenil;
    }

    public void setJuvenil(JuvenilFutbol8 juvenil) {
        this.juvenil = juvenil;
    }
    
    private static class ClasificacionComparator
            implements Comparator<EquipoFutbol8> {

        public int compare(EquipoFutbol8 o1, EquipoFutbol8 o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");

            PuntuacionFutbol8 pto1 = o1.getPuntuacion();
            PuntuacionFutbol8 pto2 = o2.getPuntuacion();

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

    public static Comparator<EquipoFutbol8> ClasificacionComparator(){

        return new ClasificacionComparator();

    }

   


}
