package mld.clubdeportivo.base.basket;

import java.util.ArrayList;
import java.util.Comparator;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.base.Jugador;
import mld.clubdeportivo.base.basket.EstrategiaBasket;
import static mld.clubdeportivo.base.Jugador.JORNADAS_CONTRATO;
import static mld.clubdeportivo.base.Jugador.colorFondoCelda;
import mld.clubdeportivo.utilidades.Calculos;

public class JugadorBasket extends Jugador {

    private EquipoBasket equipo;
    private PosicionJugBasket posicion;
    private int valoracion;
    private int estadoFisico;
    private boolean juegaJornada;
    private String cuadrante;
    private int ptsLiga;
    private int ptsCopa;
    private int partidosJugados;

    
    // Todas estas son para el partido no se graban
    private int capacidadRobo;
    private int valoracionPartido; 
    private int cansancioPartido; 
    private EstrategiaBasket estrategia; 
    private int Tiro1;
    private int Tiro2;
    private int Tiro3;
    private int Rebote;
    private int Falta;

    private boolean fueraPosicion;

    public JugadorBasket(){}

    // Para alta de jugador
    public JugadorBasket(Grupo grp, EquipoBasket equipo, String nombre,
            mld.clubdeportivo.base.basket.PosicionJugBasket posicion, int valoracion){

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
        this.setJuegaJornada(false);
        this.setPartidosJugados(0);
        this.setEnSubasta(false);
        this.setPuja(0);

    }
    
    public JugadorBasket(Grupo grp, String nombre,
            mld.clubdeportivo.base.basket.PosicionJugBasket posicion, int valoracion){
        this(grp, null, nombre, posicion, valoracion);
    }

    public EquipoBasket getEquipo() {
        return equipo;
    }
    
    public String getNombreEquipo() {
        String txt= "";
        if (this.getEquipo() != null)
            txt = this.getEquipo().getNombre();
        return txt;
    }

    public void setEquipo(EquipoBasket equipo) {
        this.equipo = equipo;
    }

    public mld.clubdeportivo.base.basket.PosicionJugBasket getPosicion() {
        return posicion;
    }

    public void setPosicion(mld.clubdeportivo.base.basket.PosicionJugBasket posicion) {
        this.posicion = posicion;
    }

    public int getValoracion() {
        return valoracion;
    }
    
    public int getValoracionReal() {
        return this.getValoracion() / 10;
    }
    public String getValoracionInf(){
        int val = this.getValoracionPartido();
        if (val == 0) val = this.getValoracionMedia();
        return this.getNombre() + '(' + val + ')';
    }

    public int getValoracionMedia(){
        
        int val = (int) (this.getValoracionReal() * this.getEstadoFisico() / 100);        
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
        
        return  this.getJornadasLesion() == 0;
        
    }

    private void comprobarJuegaJornada(){

        if (this.getJornadasLesion() > 0)
            throw new UnsupportedOperationException("El jugador esta lesionado o con tarjeta roja");

        boolean portero = false;
        int jugadoresCampo = 0;

        for (JugadorBasket jug : this.getEquipo().getJugadores()) {
           jugadoresCampo++;
        }

        if (jugadoresCampo > 5)
            throw new UnsupportedOperationException("Solo se pueen aliniar 5 jugadores");

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
       
        int accion = this.getValoracionPartido();
        if (accion == 0) accion = this.getValoracionMedia();      
        
        int mod = 0;
        
        // Si esta fuera de su posicion pierde un 25% en el pase
        if (this.isFueraPosicion())      
                mod = mod - 25;
        
        mld.clubdeportivo.base.basket.EstrategiaBasket estra = this.getEstrategia();
        if (estra != null && (estra.equals(mld.clubdeportivo.base.basket.EstrategiaBasket.Estaticos) ||
                estra.equals(mld.clubdeportivo.base.basket.EstrategiaBasket.Control)))      
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
    
    public int getFinta(){
        int accion = this.getValoracionPartido();
        if (accion == 0) accion = this.getValoracionMedia();
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.Pivot))
            accion = (int) (accion * 0.30);
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.AlaPivot))
            accion = (int) (accion * 0.50);
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.Alero))
            accion = (int) (accion * 0.75);
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.Escolta))
            accion = (int) (accion * 0.90);       
        return accion;           
    }
    public String getFintaInf(){
        return this.getNombre() + '(' + this.getFinta() + ')';
    }
    
    public int getTiro(){
        int accion = this.getValoracionPartido();
        if (accion == 0) accion = this.getValoracionMedia();
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.Pivot))
            accion = (int) (accion * 0.50);
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.AlaPivot))
            accion = (int) (accion * 0.70); 
        return accion;            
    }
    public String getTiroInf(){
        return this.getNombre() + '(' + this.getTiro() + ')';
    }
    
    public int getRebote(){
        int accion = this.getValoracionPartido();
        if (accion == 0) accion = this.getValoracionMedia();
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.Base))
            accion = (int) (accion * 0.30);
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.Escolta))
            accion = (int) (accion * 0.40); 
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.Alero))
            accion = (int) (accion * 0.60); 
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.AlaPivot))
            accion = (int) (accion * 0.85); 
        return accion;            
    }
    public String getReboteInf(){
        return this.getNombre() + '(' + this.getRebote() + ')';
    }
    
    public int getRobo(){

        int accion = this.getValoracionPartido();
        if (accion == 0) accion = this.getValoracionMedia();
        
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.Pivot))
            accion = (int) (accion * 0.50);
        if (this.getPosicion().equals(mld.clubdeportivo.base.basket.PosicionJugBasket.AlaPivot))
            accion = (int) (accion * 0.70);  
        
        int mod = 0;
        
        mld.clubdeportivo.base.basket.EstrategiaBasket estra = this.getEstrategia();
        if (estra != null)       
            if (estra.equals(mld.clubdeportivo.base.basket.EstrategiaBasket.Agresivos) ||
                    estra.equals(mld.clubdeportivo.base.basket.EstrategiaBasket.Estaticos)) 
                mod = mod + 25;
            else if (estra.equals(mld.clubdeportivo.base.basket.EstrategiaBasket.Control)) 
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

        StringBuilder txt = new StringBuilder();

        txt.append("JUGADOR\n");
        txt.append(String.format("- Id: %d \n", this.getId()));
        txt.append(String.format("- Nombre: %s \n", this.getNombre()));
        txt.append(String.format("- Posicion: %s \n", this.getPosicion()));
        txt.append(String.format("- Valoracion: %d \n", this.getValoracion()));
        txt.append(String.format("- Estado Fisico: %d \n", this.getEstadoFisico()));
        txt.append(String.format("- Ficha: %d \n", this.getFicha()));
        txt.append(String.format("- Clausula: %d \n", this.getClausula()));
        txt.append(String.format("- Blindado: %b \n", this.isBlindado()));
        txt.append(String.format("- Transferible: %b \n", this.isTransferible()));
        txt.append(String.format("- Jornadas Lesion: %d \n", this.getJornadasLesion()));
        txt.append(String.format("- Contrato: %d \n", this.getContrato()));
        txt.append(String.format("- Juega Jornada: %b \n", this.isJuegaJornada()));

        return txt.toString();
    }

    public void hacerPuja(EquipoBasket eq, int cantidad) {
  
        if (eq.getPresupuesto() < cantidad)
            throw new UnsupportedOperationException("No tienes suficiente presupuesto");
        if (eq.getJugadores().size() >= EquipoBasket.NUMERO_MAX_JUGADORES)
             throw new UnsupportedOperationException("Ya tienes el maximo de jugadores");
        if (cantidad >= this.getClausula() && cantidad > this.getPuja()){
            this.setEquipoPuja(eq.getId());
            this.setPuja(cantidad);
        }
    }

    public int hacerCompra(EquipoBasket eq) {
        // devuelve lo que ha costado
        
        if (eq.getJugadores().size() >= EquipoBasket.NUMERO_MAX_JUGADORES)
             throw new UnsupportedOperationException("Ya tienes el maximo de jugadores");
        
        EquipoBasket eqVenta = this.getEquipo();
        int min, max;
        int precio = this.getClausula();
        int val =  this.getValoracionReal();
        if (eq.getPresupuesto() < precio)
            throw new IllegalArgumentException("No tienes suficiente presupuesto");
        
        int intermediarios = 0;
        if (!this.isTransferible()){
            min = precio * 25 / 100;
            max = precio * 75 / 100;
            intermediarios = Calculos.valorAleatorio(min, max);  
        }
        
        eq.setPresupuesto(eq.getPresupuesto() - precio - intermediarios);
        eqVenta.setPresupuesto(eqVenta.getPresupuesto() + precio);
        
        min = val * 25 / 100;
        max = val * 75 / 100;
        int subida = Calculos.valorAleatorio(min, max); 
        
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
  
        EquipoBasket eq = this.getEquipo();
        int jornadasAPagar = Calculos.valorAleatorio(this.getContrato());
        int coste = this.getFicha() * jornadasAPagar;
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
        
        EquipoBasket eq = this.getEquipo();
        int coste = (int) (this.getContrato() * this.getClausula() * 0.04);
        
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
  
        EquipoBasket eq = this.getEquipo();
        int coste = (int) (this.getClausula() * 0.25);
        eq.setPresupuesto(eq.getPresupuesto() - coste);
        int subida = Calculos.valorAleatorio(this.getValoracionReal());
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
        
        if (Calculos.obtener(2))
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
        
        boolean incorrecta = false;
        
        // pendiente de implementar
       
        return incorrecta;
    }
    
    public String getTextoPizarra(){
        
        String txt = this.getNombre() + "<br/>(" + this.getValoracionPartido() + ")";
        if (this.isPosicionIncorrecta())
            txt = txt + "(" + this.getPosicion().name().substring(0, 3) + ")";
        
        return txt;
        
    }

    public mld.clubdeportivo.base.basket.EstrategiaBasket getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(mld.clubdeportivo.base.basket.EstrategiaBasket estrategia) {
        this.estrategia = estrategia;
    }


    public int getFalta() {
        return Falta;
    }


    public void setFalta(int Falta) {
        this.Falta = Falta;
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
    
  

    public static class PosicionComparator implements Comparator<JugadorBasket> {

        public int compare(JugadorBasket o1, JugadorBasket o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");

            return o1.getPosicion().compareTo(o2.getPosicion());
        }
    }
    
     public static Comparator<JugadorBasket> getPosicionComparator(){

        return new PosicionComparator();

    }
      
     
     public static class PuntosComparator implements Comparator<JugadorBasket> {

         public int compare(JugadorBasket o1, JugadorBasket o2) {

             int result = 1;
             if (o1 == null || o2 == null)
                 throw new NullPointerException("Referencia nula");
             
             if (o1.getPuntosTemporada() >= o2.getPuntosTemporada()) result = -1;
             
             return result;
         }
     }
     
     public static Comparator<JugadorBasket> getPuntosComparator(){
         
         return new PuntosComparator();
         
     }
     
          
     public static int clausulaMedia(ArrayList<JugadorBasket> lista){
        
        int suma = 0;
        int jugs = 0;
        int media = 0;
        for (JugadorBasket jug : lista) {
            if (jug.getEquipo() != null && !jug.getEquipo().isAutomatico()){
                suma = suma + jug.getClausula();
                jugs++;
            }
        }
        
        if (jugs > 0) media = suma / jugs;
        
        return media;
    }
     
        
}

