package mld.clubdeportivo.base.futbol8;

import static java.lang.String.format;
import java.util.ArrayList;
import java.util.Comparator;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.Jugador;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.NUMERO_MAX_JUGADORES;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.Agresivos;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.Control;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.Estaticos;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Defensa;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Delantero;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Medio;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Portero;
import mld.clubdeportivo.utilidades.Calculos;
import static mld.clubdeportivo.utilidades.Calculos.obtener;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;

public final class JugadorFutbol8 extends Jugador {
       
    private EquipoFutbol8 equipo;
    private PosicionJugFutbol8 posicion;
    private int valoracion;
    private int estadoFisico;
    private boolean tarjetaAmarilla;
    private boolean tarjetaRoja;
    private boolean juegaJornada;
    private String cuadrante;
    private int golesLiga;
    private int golesCopa;
    private int partidosJugados;

    
    // Todas estas son para el partido no se graban
    private int capacidadRobo;
    private int valoracionPartido; 
    private int cansancioPartido; 
    private EstrategiaFutbol8 estrategia; 
    private int TiroArea;
    private int TiroSolo;
    private int TiroLejano;
    private int TiroLejanoSolo;
    private int Penalti;
    private int Cabeza;
    private int Falta;
    private int paradasTiroArea;
    private int paradasTiroSolo;
    private int paradasTiroLejano;
    private int paradasTiroLejanoSolo;
    private int paradasPenalti;
    private int paradasCabeza;
    private int paradasFalta;
    private boolean fueraPosicion;

    
    public JugadorFutbol8(){}

    // Para alta de jugador
    public JugadorFutbol8(Grupo grp, EquipoFutbol8 equipo, String nombre,
            PosicionJugFutbol8 posicion, int valoracion){

        this.setGrupo(grp);
        this.setEquipo(equipo);
        this.setNombre(nombre);
        this.setPosicion(posicion);
        this.setValoracion(valoracion * 10);

        this.setFicha(valoracion);
        this.setClausula(valoracion * JORNADAS_CONTRATO);
        this.setBlindado(true);
        this.setJornadasLesion(0);
        this.setTransferible(false);
        this.setContrato(JORNADAS_CONTRATO);

        this.setEstadoFisico(100);
        this.setTarjetaAmarilla(false);
        this.setTarjetaRoja(false);
        this.setJuegaJornada(false);
        this.setGolesLiga(0);
        this.setGolesCopa(0);
        this.setPartidosJugados(0);
        this.setEnSubasta(false);
        this.setPuja(0);

    }
    
    public JugadorFutbol8(Grupo grp, String nombre,
            PosicionJugFutbol8 posicion, int valoracion){
        this(grp, null, nombre, posicion, valoracion);
    }

    public EquipoFutbol8 getEquipo() {
        return equipo;
    }
    
    public String getNombreEquipo() {
        var txt= "";
        if (this.getEquipo() != null)
            txt = this.getEquipo().getNombre();
        return txt;
    }

    public void setEquipo(EquipoFutbol8 equipo) {
        this.equipo = equipo;
    }

    public PosicionJugFutbol8 getPosicion() {
        return posicion;
    }

    public void setPosicion(PosicionJugFutbol8 posicion) {
        this.posicion = posicion;
    }

    public int getValoracion() {
        return valoracion;
    }
    
    public int getValoracionReal() {
        return this.getValoracion() / 10;
    }
    public String getValoracionInf(){
        var val = this.getValoracionPartido();
        if (val == 0) val = this.getValoracionMedia();
        return this.getNombre() + '(' + val + ')';
    }

    public int getValoracionMedia(){
        
        var val = (int) (this.getValoracionReal() * this.getEstadoFisico() / 100);        
        return val;
    }
    
    public int getValoracionPartido(){
        //DEBUG
        //return 100;
        return valoracionPartido;
        
    }
    
    public void setValoracionPartido(int valoracionPartido) {
        this.valoracionPartido = valoracionPartido;
    }

    public void setValoracion(int valoracion) {
        if (valoracion < 100) valoracion = 100;
        if (valoracion > 1000) valoracion = 1000;
        this.valoracion = valoracion;
    }

    public int getEstadoFisico() {
        return estadoFisico;
    }

    public void setEstadoFisico(int estadoFisico) {
        if (estadoFisico < 10) estadoFisico = 10;
        if (estadoFisico > 100) estadoFisico = 100;
        this.estadoFisico = estadoFisico;
    }

    public boolean isTarjetaAmarilla() {
        return tarjetaAmarilla;
    }

    public void setTarjetaAmarilla(boolean tarjetaAmarilla) {
        this.tarjetaAmarilla = tarjetaAmarilla;
    }

    public boolean isTarjetaRoja() {
        return tarjetaRoja;
    }

    public void setTarjetaRoja(boolean tarjetaRoja) {
        this.tarjetaRoja = tarjetaRoja;
    }

    public boolean isJuegaJornada() {
        return juegaJornada;
    }

    public void setJuegaJornada(boolean juegaJornada, boolean verificar) {
        // Asigna el valor, si verificar es true entonces
        // comprueba que el jugador realmente se puede alinear

        if (juegaJornada == true && verificar == true)
            comprobarJuegaJornada();

        this.juegaJornada = juegaJornada;
    }

    public void setJuegaJornada(boolean juegaJornada) {
        
        this.setJuegaJornada(juegaJornada, false);
    }
    
    public boolean isPuedeJugar(){
        
        return !this.isTarjetaRoja() && this.getJornadasLesion() == 0;
        
    }

    private void comprobarJuegaJornada(){

        if (this.isTarjetaRoja() || this.getJornadasLesion() > 0)
            throw new UnsupportedOperationException("El jugador esta lesionado o con tarjeta roja");

        var portero = false;
        var jugadoresCampo = 0;
        for (var jug : this.getEquipo().getJugadores()) {
            if (!jug.isJuegaJornada() || jug.equals(this)) continue;
            if (jug.getPosicion().equals(Portero))
                portero = true;
            else
                jugadoresCampo++;
        }

        if (this.getPosicion().equals(Portero) && portero )
            throw new UnsupportedOperationException("Ya hay un portero alinado");
        if (!this.getPosicion().equals(Portero) && jugadoresCampo > 6)
            throw new UnsupportedOperationException("Solo se pueen aliniar 7 jugadores de campo");

    }

    public int getGolesLiga() {
        return golesLiga;
    }

    public void setGolesLiga(int golesLiga) {
        this.golesLiga = golesLiga;
    }
    
    public int getGolesCopa() {
        return golesCopa;
    }

    public void setGolesCopa(int golesCopa) {
        this.golesCopa = golesCopa;
    }
    
    public int getGolesTemporada(){
        return this.getGolesLiga() + this.getGolesCopa();
    }

    public String getColorValoracion(){

        return colorFondoCelda(this.getValoracionReal());
    }

    public String getColorValoracionMedia(){

        return colorFondoCelda(this.getValoracionMedia());
    }

    public String getColorValoracionPartido(){

        return colorFondoCelda(this.getValoracionPartido());
    }
    
    public String getColorEstadoFisico(){

        return colorFondoCelda(this.getEstadoFisico());
    }
    
    public String getColorPase(){

        return colorFondoCelda(this.getPase());
    }
    
    public String getColorRobo(){

        return colorFondoCelda(this.getRobo());
    }
    public String getColorRegate(){

        return colorFondoCelda(this.getRegate());
    }
    public String getColorTiro(){

        return colorFondoCelda(this.getTiro());
    }
    
    public int getPase(){
        if (this.getPosicion().equals(Portero))
            return 0;
        var accion = this.getValoracionPartido();
        if (accion == 0) accion = this.getValoracionMedia();
        if (this.getPosicion().equals(Defensa))
            accion = (int) (accion * 0.60);
        if (this.getPosicion().equals(Delantero))
            accion = (int) (accion * 0.60);   
        
        var mod = 0;
        
        // Si esta fuera de su posicion pierde un 25% en el pase
        if (this.isFueraPosicion())      
                mod = mod - 25;
        
        var estra = this.getEstrategia();
        if (estra != null && (estra.equals(Estaticos) ||
                estra.equals(Control)))      
                mod = mod + 25;
        
        if (mod > 0)
            accion = (int) (accion + (accion * mod / 100)); 
        else if (mod < 0)
            accion = (int) (accion - (accion * mod /100)); 
        
        return accion;        
    }
    public String getPaseInf(){
        return this.getNombre() + '(' + this.getPase() + ')';
    }
    
    public int getRegate(){
        if (this.getPosicion().equals(Portero))
            return 0;
        var accion = this.getValoracionPartido();
        if (accion == 0) accion = this.getValoracionMedia();
        if (this.getPosicion().equals(Defensa))
            accion = (int) (accion * 0.40);
        if (this.getPosicion().equals(Medio))
            accion = (int) (accion * 0.80);
        if (this.getPosicion().equals(Delantero))
            accion = (int) (accion * 0.80);       
        return accion;           
    }
    public String getRegateInf(){
        return this.getNombre() + '(' + this.getRegate() + ')';
    }
    
    public int getTiro(){
        if (this.getPosicion().equals(Portero))
            return 0;
        var accion = this.getValoracionPartido();
        if (accion == 0) accion = this.getValoracionMedia();
        if (this.getPosicion().equals(Defensa))
            accion = (int) (accion * 0.40);
        if (this.getPosicion().equals(Medio))
            accion = (int) (accion * 0.60); 
        return accion;            
    }
    public String getTiroInf(){
        return this.getNombre() + '(' + this.getTiro() + ')';
    }
    
    public int getRobo(){
        if (this.getPosicion().equals(Portero))
            return 0;
        var accion = this.getValoracionPartido();
        if (accion == 0) accion = this.getValoracionMedia();
        
        if (this.getPosicion().equals(Medio))
            accion = (int) (accion * 0.50);
        if (this.getPosicion().equals(Delantero))
            accion = (int) (accion * 0.30);  
        
        var mod = 0;
        var estra = this.getEstrategia();
        if (estra != null)       
            if (estra.equals(Agresivos) ||
                    estra.equals(Estaticos)) 
                mod = mod + 25;
            else if (estra.equals(Control)) 
                mod = mod - 25;
        
        if (mod > 0)
            accion = (int) (accion + (accion * mod / 100)); 
        else if (mod < 0)
            accion = (int) (accion - (accion * mod /100)); 
        
        // Esto es para reducir la capacidad de robo si el jugador esta lejos 
        if (this.getCapacidadRobo() > 0 && this.getCapacidadRobo() < 100)
            accion = (int) (accion *  this.getCapacidadRobo() / 100);
                
        return accion;         
    }
    public String getRoboInf(){
        return this.getNombre() + '(' + this.getRobo() + ')';
    }
    

    @Override
    public String toString() {

        var txt = new StringBuilder();

        txt.append("JUGADOR\n");
        txt.append(format("- Id: %d \n", this.getId()));
        txt.append(format("- Nombre: %s \n", this.getNombre()));
        txt.append(format("- Posicion: %s \n", this.getPosicion()));
        txt.append(format("- Valoracion: %d \n", this.getValoracion()));
        txt.append(format("- Estado Fisico: %d \n", this.getEstadoFisico()));
        txt.append(format("- Ficha: %d \n", this.getFicha()));
        txt.append(format("- Clausula: %d \n", this.getClausula()));
        txt.append(format("- Tarjeta Amarilla: %b \n", this.isTarjetaAmarilla()));
        txt.append(format("- Tarjeta Roja: %b \n", this.isTarjetaRoja()));
        txt.append(format("- Blindado: %b \n", this.isBlindado()));
        txt.append(format("- Transferible: %b \n", this.isTransferible()));
        txt.append(format("- Jornadas Lesion: %d \n", this.getJornadasLesion()));
        txt.append(format("- Contrato: %d \n", this.getContrato()));
        txt.append(format("- Juega Jornada: %b \n", this.isJuegaJornada()));

        return txt.toString();
    }

    public void hacerPuja(EquipoFutbol8 eq, int cantidad) {
  
        if (eq.getPresupuesto() < cantidad)
            throw new UnsupportedOperationException("No tienes suficiente presupuesto");
        if (eq.getJugadores().size() >= NUMERO_MAX_JUGADORES)
             throw new UnsupportedOperationException("Ya tienes el maximo de jugadores");
        if (cantidad >= this.getClausula() && cantidad > this.getPuja()){
            this.setEquipoPuja(eq.getId());
            this.setPuja(cantidad);
        }
    }

    public int hacerCompra(EquipoFutbol8 eq) {
        // devuelve lo que ha costado
        
        if (eq.getJugadores().size() >= NUMERO_MAX_JUGADORES)
             throw new UnsupportedOperationException("Ya tienes el maximo de jugadores");
        
        var eqVenta = this.getEquipo();
        int min, max;
        var precio = this.getClausula();
        var val =  this.getValoracionReal();
        if (eq.getPresupuesto() < precio)
            throw new IllegalArgumentException("No tienes suficiente presupuesto");
        
        var intermediarios = 0;
        if (!this.isTransferible()){
            min = precio * 25 / 100;
            max = precio * 75 / 100;
            intermediarios = valorAleatorio(min, max);  
        }
        
        eq.setPresupuesto(eq.getPresupuesto() - precio - intermediarios);
        eqVenta.setPresupuesto(eqVenta.getPresupuesto() + precio);
        
        min = val * 25 / 100;
        max = val * 75 / 100;
        var subida = valorAleatorio(min, max); 
        
        this.setFicha(this.getFicha() + subida);
        this.setClausula(this.getClausula() + subida * JORNADAS_CONTRATO);
        this.setContrato(JORNADAS_CONTRATO);
        this.setJuegaJornada(false);
        this.setTransferible(false);
        this.setEquipo(eq);
        
        return precio + intermediarios;
        
    }
    
    public int despedir() {
        // cuesta valor aleatorio de jornadas de contrato restante * ficha
  
        var eq = this.getEquipo();
        var jornadasAPagar = valorAleatorio(this.getContrato());
        var coste = this.getFicha() * jornadasAPagar;
        eq.setPresupuesto(eq.getPresupuesto() - coste);

        return coste;
        
    }
    
    public int blindar(){
        // cuesta un 5% del valor de la clausula
        
        
        if (this.isBlindado()){
            throw new UnsupportedOperationException("El jugador " + this.getNombre() + " ya esta blindado");
        }
        
        if (this.isEnSubasta()){
            throw new UnsupportedOperationException("El jugador " + this.getNombre() + " esta en subasta");
        }
        
        var eq = this.getEquipo();
        var coste = (int) (this.getContrato() * this.getClausula() * 0.04);
        
        if (eq.getPresupuesto() < coste){
            throw new UnsupportedOperationException("No tienes la cantidad de " + coste + " para blindar el jugador");
        }
        
        eq.setPresupuesto(eq.getPresupuesto() - coste);
        
        this.setBlindado(true);
        this.setTransferible(false);
        
        return coste;        
        
    }
    
    public void ponerTransferible(){
        
        if (this.isEnSubasta()){
            throw new UnsupportedOperationException("El jugador " + this.getNombre() + " esta en subasta");
        }
        
        this.setTransferible(true);
        this.setBlindado(false);
        
    }
    
    public void mejorarContrato() {
        // Sube un 10% la ficha y un 20% el valor de la clausula 
     
        if (this.isEnSubasta()){
            throw new UnsupportedOperationException("El jugador " + this.getNombre() + " esta en subasta");
        }
        this.setFicha((int) (this.getFicha() +  this.getFicha() * 0.1));        
        this.setClausula((int) (this.getClausula() +  this.getClausula() * 0.2));

        
    }
    
    public int renovar() {
        // cuesta un 25% de la clausula
  
        var eq = this.getEquipo();
        var coste = (int) (this.getClausula() * 0.25);
        eq.setPresupuesto(eq.getPresupuesto() - coste);
        var subida = valorAleatorio(this.getValoracionReal());
        this.setFicha(this.getFicha() + subida);
        this.setClausula(this.getClausula() + subida * 40);
        
        this.setContrato(JORNADAS_CONTRATO);
        this.setBlindado(false);
        this.setEnSubasta(false);
        this.setTransferible(false);

        return coste;
        
    }
    
    public void subastar() {
        // 
  
        this.setEnSubasta(true);
        this.setTransferible(true);
        this.setBlindado(false);
        
    }
    
    public void bajarValoracion(){
        
        if (obtener(2))
            this.setValoracion(this.getValoracion() - 10);
        if (this.getValoracion() < 100) this.setValoracion(100);
    }

    public String getCuadrante() {
        return cuadrante;
    }

    public void setCuadrante(String cuadrante) {
        this.cuadrante = cuadrante;
    }

    public int getCansancioPartido() {
        return cansancioPartido;
    }

    public void setCansancioPartido(int cansancioPartido) {
        this.cansancioPartido = cansancioPartido;
    }
    
    public boolean isPosicionIncorrecta(){
        
        var incorrecta = false;
        
        if (this.getPosicion().equals(Defensa) &&
            !this.getCuadrante().contains("1") ) incorrecta = true;
        if (this.getPosicion().equals(Medio) &&
            !this.getCuadrante().contains("2") ) incorrecta = true;
        if (this.getPosicion().equals(Delantero) &&
            !this.getCuadrante().contains("3") ) incorrecta = true;
        
        return incorrecta;
    }
    
    public String getTextoPizarra(){
        
        var txt = this.getNombre() + "<br/>(" + this.getValoracionPartido() + ")";
        if (this.isPosicionIncorrecta())
            txt = txt + "(" + this.getPosicion().name().substring(0, 3) + ")";
        
        return txt;
        
    }

    public EstrategiaFutbol8 getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaFutbol8 estrategia) {
        this.estrategia = estrategia;
    }

    public int getTiroArea() {
        return TiroArea;
    }

    public int getTiroSolo() {
        return TiroSolo;
    }

    public int getTiroLejano() {
        return TiroLejano;
    }

    public int getTiroLejanoSolo() {
        return TiroLejanoSolo;
    }

    public int getPenalti() {
        return Penalti;
    }


    public int getCabeza() {
        return Cabeza;
    }

    public int getFalta() {
        return Falta;
    }

    public void setTiroArea(int TiroArea) {
        this.TiroArea = TiroArea;
    }

    public void setTiroSolo(int TiroSolo) {
        this.TiroSolo = TiroSolo;
    }

    public void setTiroLejano(int TiroLejano) {
        this.TiroLejano = TiroLejano;
    }

    public void setTiroLejanoSolo(int TiroLejanoSolo) {
        this.TiroLejanoSolo = TiroLejanoSolo;
    }

    public void setPenalti(int Penalti) {
        this.Penalti = Penalti;
    }

    public void setCabeza(int Cabeza) {
        this.Cabeza = Cabeza;
    }

    public void setFalta(int Falta) {
        this.Falta = Falta;
    }


    public int getParadasTiroArea() {
        return paradasTiroArea;
    }

    public void setParadasTiroArea(int paradasTiroArea) {
        this.paradasTiroArea = paradasTiroArea;
    }

    public int getParadasTiroSolo() {
        return paradasTiroSolo;
    }

    public void setParadasTiroSolo(int paradasTiroSolo) {
        this.paradasTiroSolo = paradasTiroSolo;
    }

    public int getParadasTiroLejano() {
        return paradasTiroLejano;
    }

    public void setParadasTiroLejano(int paradasTiroLejano) {
        this.paradasTiroLejano = paradasTiroLejano;
    }

    public int getParadasTiroLejanoSolo() {
        return paradasTiroLejanoSolo;
    }

    public void setParadasTiroLejanoSolo(int paradasTiroLejanoSolo) {
        this.paradasTiroLejanoSolo = paradasTiroLejanoSolo;
    }

    public int getParadasPenalti() {
        return paradasPenalti;
    }

    public void setParadasPenalti(int paradasPenalti) {
        this.paradasPenalti = paradasPenalti;
    }

    public int getParadasCabeza() {
        return paradasCabeza;
    }

    public void setParadasCabeza(int paradasCabeza) {
        this.paradasCabeza = paradasCabeza;
    }

    public int getParadasFalta() {
        return paradasFalta;
    }

    public void setParadasFalta(int paradasFalta) {
        this.paradasFalta = paradasFalta;
    }

    public int getEquipacion() {
        return this.getEquipo().getEquipacion();
    }


    public int getCapacidadRobo() {
        return capacidadRobo;
    }

    public void setCapacidadRobo(int capacidadRobo) {
        this.capacidadRobo = capacidadRobo;
    }

    public boolean isFueraPosicion() {
        return fueraPosicion;
    }

    public void setFueraPosicion(boolean fueraPosicion) {
        this.fueraPosicion = fueraPosicion;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }
    
    public float getRankingPortero(){
        
        var minPartidos = 15;
        
        float r = 0;
        if (this.getPartidosJugados() > 0)
            r = (float) ((float) (this.getGolesLiga() + this.getGolesCopa()) / (float) this.getPartidosJugados());
        
        // Si no se han jugado los partidos minimos le sumamos la penalizacion
        if (this.getPartidosJugados() < minPartidos)
            r = r + ((minPartidos - this.getPartidosJugados()) / 2);
        
        return r;
    }



    public static class PosicionComparator implements Comparator<JugadorFutbol8> {

        public int compare(JugadorFutbol8 o1, JugadorFutbol8 o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");

            return o1.getPosicion().compareTo(o2.getPosicion());
        }
    }
    
     public static Comparator<JugadorFutbol8> getPosicionComparator(){

        return new PosicionComparator();

    }
      
     
     public static class GolesComparator implements Comparator<JugadorFutbol8> {

         public int compare(JugadorFutbol8 o1, JugadorFutbol8 o2) {

             var result = 1;
             if (o1 == null || o2 == null)
                 throw new NullPointerException("Referencia nula");
             
             if (o1.getGolesTemporada() >= o2.getGolesTemporada()) result = -1;
             
             return result;
         }
     }
     
     public static Comparator<JugadorFutbol8> getGolesComparator(){
         
         return new GolesComparator();
         
     }
     
      public static class RatioPorteroComparator implements Comparator<JugadorFutbol8> {

         public int compare(JugadorFutbol8 o1, JugadorFutbol8 o2) {

             var result = 1;
             if (o1 == null || o2 == null)
                 throw new NullPointerException("Referencia nula");
             
             if (o1.getRankingPortero() <= o2.getRankingPortero()) result = -1;
             
             return result;
         }
     }
     
     public static Comparator<JugadorFutbol8> getRatioPorteroComparator(){
         
         return new RatioPorteroComparator();
         
     }
     
     public static int clausulaMedia(ArrayList<JugadorFutbol8> lista){
        
        var suma = 0;
        var jugs = 0;
        var media = 0;
        for (var jug : lista) {
            if (jug.getEquipo() != null && !jug.getEquipo().isAutomatico()){
                suma = suma + jug.getClausula();
                jugs++;
            }
        }
        
        if (jugs > 0) media = suma / jugs;
        
        return media;
    }
     
        
}
