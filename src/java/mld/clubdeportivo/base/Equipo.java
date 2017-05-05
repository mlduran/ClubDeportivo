package mld.clubdeportivo.base;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import mld.clubdeportivo.utilidades.Calculos;
import mld.clubdeportivo.utilidades.StringUtil;

public abstract class Equipo extends Seccion
        implements Comparable<Seccion> {
    
    
    public static final int PRESUPUESTO_INICIAL = 20000;
    public static final int CANTIDA_RELATIVA = 500;
    public static final int CANTIDAD_BASE = 500;
    public static final int SALARIO_BASE = 50;
    public static final int CAPACIDAD_ESPECTADORES_INICIAL = 100;
    public static final int MAX_INV_PUBLICIDAD = 20;
    
    //public static final int CADUCIDAD_CONTRATOS = 40;

    private int primaMaxima = 5000;
    
    private boolean automatico;
    private int moral;
    private int campo;
    private String nombreCampo;
    private int publicidad;
    private int eqTecnico;
    private int ojeadores;
    private boolean ampliarCampo;
    private boolean ampliarPublicidad;
    private boolean ampliarEqTecnico;
    private boolean ampliarOjeadores;
    private int jugadoresOjeados;
    private int presupuesto;
    private int credito;
    private int bolsa;
    private Date fechaBolsa;
    private boolean entrenamiento;
    private int simulaciones;
    private boolean modoAuto;
    private ArrayList<Jugador> jugadores;
    private Entrenador entrenador;
    private int totalIngresos;
    private int totalGastos;
    private long idClub;
    
    
    private boolean juegaEnCasa = false;

    protected Equipo(){};
    
    
    public boolean isAutomatico() {
        return automatico;
    }

    public void setAutomatico(boolean automatico) {
        this.automatico = automatico;
    }
    
    public static int numeroEquiposNoAutomaticos(ArrayList<Equipo> eqs){
        
        int num = 0;
        
        for (Equipo eq : eqs)
            if(!eq.isAutomatico()) num++;

        return num;
        
    }

    public int getMoral() {
        return moral;
    }

    public void setMoral(int moral) {
        this.moral = moral;
    }

    public int getCampo() {
        return campo;
    }

    public void setCampo(int campo) {
        this.campo = campo;
    }

    public int getPublicidad() {
        return publicidad;
    }

    public void setPublicidad(int publicidad) {
        if (this.isAutomatico() || publicidad < 0) publicidad = 0;   
        if (publicidad > MAX_INV_PUBLICIDAD) publicidad = MAX_INV_PUBLICIDAD; 
        
        this.publicidad = publicidad;
    }

    public int getEqTecnico() {
        return eqTecnico;
    }

    public void setEqTecnico(int eqTecnico) {
        if (this.isAutomatico() || eqTecnico < 0) return;
        this.eqTecnico = eqTecnico;
    }

    public int getOjeadores() {        
        return ojeadores;
    }

    public boolean isAmpliarCampo() {
        return ampliarCampo;
    }

    public void setAmpliarCampo(boolean ampliarCampo) {
        this.ampliarCampo = ampliarCampo;
    }

    public boolean isAmpliarPublicidad() {
        return ampliarPublicidad;
    }

    public void setAmpliarPublicidad(boolean ampliarPublicidad) {
        this.ampliarPublicidad = ampliarPublicidad;
    }
    
    public int getIngresosMediosPublicidad(){
        
        return this.getPublicidad() * CANTIDAD_BASE / 2;
        
    }
    
    public int ingresoPublicidad(int porcentajeHacienda){
        
        if (this.getPublicidad() == 0) return 0;
        
        int ingreso = this.getPublicidad() * Calculos.valorAleatorio(CANTIDAD_BASE);
        
        int hacienda = ingreso * porcentajeHacienda / 100;
        
        this.setPresupuesto(this.getPresupuesto() + ingreso - hacienda);
        
        return ingreso;        
    }
    
    

    public boolean isAmpliarEqTecnico() {
        return ampliarEqTecnico;
    }

    public void setAmpliarEqTecnico(boolean ampliarEqTecnico) {
        this.ampliarEqTecnico = ampliarEqTecnico;
    }

    public boolean isAmpliarOjeadores() {
        return ampliarOjeadores;
    }

    public void setAmpliarOjeadores(boolean ampliarOjeadores) {
        this.ampliarOjeadores = ampliarOjeadores;
    }
    
    public void reiniciarInversiones(){
        this.setAmpliarCampo(false);
        this.setAmpliarEqTecnico(false);
        this.setAmpliarOjeadores(false);
        this.setAmpliarPublicidad(false);
    }


    public void setOjeadores(int ojeadores) {
        if (ojeadores < 0) return;
        this.ojeadores = ojeadores;
    }

    public int getJugadoresOjeados() {
        return jugadoresOjeados;
    }

    public void setJugadoresOjeados(int jugadoresOjeados) {
        if (jugadoresOjeados < 0) return;
        this.jugadoresOjeados = jugadoresOjeados;
    }

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }

    public int getCredito() {
        return credito;
    }
    
    public boolean getPuedePedirCredito(){
        return this.getCredito() == 0;
    }
    
    public boolean getPuedeDevolverCredito(){
        return this.getPresupuesto() >= 0;
    }
    
    public int getValorMaximoCredito(){
        // de momento 5000
        return (CANTIDAD_BASE * 10) + (this.getPublicidad() * CANTIDAD_BASE);
    }
     
    public void pedirCredito(int cantidad){
        
        if (cantidad <= 0 || !this.getPuedePedirCredito()) return;
        if (cantidad > this.getValorMaximoCredito())
            throw new UnsupportedOperationException("Superas tu maximo permitido de endeudamiento que es " + this.getValorMaximoCredito());
        this.setCredito(this.getCredito() + cantidad);
        this.setPresupuesto(this.getPresupuesto() + cantidad);  
        
    }
    
    public int devolverCredito(int cantidad){
        if (cantidad > this.getPresupuesto()) cantidad = this.getPresupuesto();
        if (this.getCredito() - cantidad < 0) cantidad = this.getCredito();
        
        this.setCredito(this.getCredito() - cantidad);
        this.setPresupuesto(this.getPresupuesto() - cantidad); 
        
        return cantidad;
        
    }

    public void setCredito(int credito) {
        this.credito = credito;
    }
    
    
    public void aplicarInteresCredito(int interes){
        if (this.getCredito() == 0) return;
        double cantidad = this.getCredito() * interes / 100;
        this.setCredito(this.getCredito() + (int)cantidad);
    }

    public int getBolsa() {
        return bolsa;
    }

    public void setBolsa(int bolsa) {
        this.bolsa = bolsa;
    }
    
    public void invertirBolsa(int cantidad){
        
        if (cantidad <= 0 ) return;
        if (cantidad > this.getPresupuesto())
            throw new UnsupportedOperationException("No tienes suficiente presupuesto");
        this.setBolsa(this.getBolsa() + cantidad);
        if (this.getFechaBolsa() == null)
            this.setFechaBolsa(new Date());
        this.setPresupuesto(this.getPresupuesto() - cantidad);        
    }
    
    public int rescatarBolsa(){
        
        if(this.getBolsa() == 0) return 0;
        int cantidad = (int) (this.getBolsa() - (this.getBolsa() * 0.1));
        this.setPresupuesto(this.getPresupuesto() + cantidad);
        this.setBolsa(0); 
        this.setFechaBolsa(null);
        return cantidad;
    }
    
    public void aplicarFluctuacion(int fluctuacion){
        
        if (this.getBolsa() == 0) return;
        double cantidad = this.getBolsa() * fluctuacion / 100;
        
        this.setBolsa(this.getBolsa() + (int) cantidad);
    }
    
    
    public boolean isEntrenamiento() {
        return entrenamiento;
    }

    public void setEntrenamiento(boolean entrenamiento) {
        this.entrenamiento = entrenamiento;
    }
    
    public boolean isPuedeEntrenar() {
        return (!this.isEntrenamiento() && this.getEqTecnico() > 0);
    }

    public int getSimulaciones() {
        return simulaciones;
    }

    public void setSimulaciones(int simulaciones) {
        this.simulaciones = simulaciones;
    }

    public boolean isModoAuto() {
        return modoAuto;
    }

    public void setModoAuto(boolean modoAuto) {
        this.modoAuto = modoAuto;
    }
    
    public int getPrimaMaxima(){
        return primaMaxima;
    }

    public int getValorPxoximaAmpliacionCampo(){
        int cantidad =  this.getCampo() * 5 + CANTIDA_RELATIVA;
        
        if (this.getCampo() >= 1500)
            cantidad =  this.getCampo() * 10 + CANTIDA_RELATIVA;
        
        if (this.getCampo() >= 2000)
            cantidad =  this.getCampo() * this.getCampo()/100 + CANTIDA_RELATIVA;
        
              
        if (this.getCampo() >= CANTIDA_RELATIVA)
            cantidad = cantidad * this.getCampo() / CANTIDA_RELATIVA;
        
        return cantidad;
    }
    
    public int getValorPxoximaAmpliacionPublicidad(){
        return this.getPublicidad() * CANTIDA_RELATIVA + CANTIDA_RELATIVA;
    }
    
    public int getValorPxoximaAmpliacionEqTecnico(){
        return this.getEqTecnico() * CANTIDA_RELATIVA + CANTIDA_RELATIVA;
    }
    
    public int getValorPxoximaAmpliacionOjeadores(){
        return this.getOjeadores() * CANTIDA_RELATIVA + CANTIDA_RELATIVA;
    }
      
    public int ampliarCampo(){

        if (this.getPresupuesto() < this.getValorPxoximaAmpliacionCampo())
            throw new UnsupportedOperationException("No tiene suficiente presupuesto");
        if (this.isAmpliarCampo())
            throw new UnsupportedOperationException("Solo se puede ampliar una vez cada jornada");

        int precio = this.getValorPxoximaAmpliacionCampo();
        this.setPresupuesto(this.getPresupuesto() - precio);
        this.setCampo(this.getCampo() + CANTIDA_RELATIVA / 5);
        this.setAmpliarCampo(true);
        
        return precio;

    }
    
    public boolean isPublicidadMaxima(){
        
        return this.getPublicidad() == MAX_INV_PUBLICIDAD;
    }

    public int ampliarPublicidad(){

        if (this.getPresupuesto() < this.getValorPxoximaAmpliacionPublicidad() )
            throw new UnsupportedOperationException("No tiene suficiente presupuesto");
        if (this.isAmpliarPublicidad())
            throw new UnsupportedOperationException("Solo se puede ampliar una vez cada jornada");
        if (this.isPublicidadMaxima())
            throw new UnsupportedOperationException("No es posible ampliar mas el numero de contratos");
        
        int precio = this.getValorPxoximaAmpliacionPublicidad();
        this.setPublicidad(this.getPublicidad() + 1);
        this.setPresupuesto(this.getPresupuesto() - precio);
        this.setAmpliarPublicidad(true);
        
        return precio;
    }

    public int ampliarEqTecnico(){

        if (this.getPresupuesto() < this.getValorPxoximaAmpliacionEqTecnico() )
            throw new UnsupportedOperationException("No tiene suficiente presupuesto");
        if (this.isAmpliarEqTecnico())
            throw new UnsupportedOperationException("Solo se puede ampliar una vez cada jornada");
        
        int precio = this.getValorPxoximaAmpliacionEqTecnico();
        this.setEqTecnico(this.getEqTecnico() + 1);
        this.setPresupuesto(this.getPresupuesto() - precio);
        this.setAmpliarEqTecnico(true);
        
        return precio;
    }

    public int ampliarOjeadores() {

        if (this.getPresupuesto() < this.getValorPxoximaAmpliacionOjeadores() )
            throw new UnsupportedOperationException("No tiene suficiente presupuesto");
        if (this.isAmpliarOjeadores())
            throw new UnsupportedOperationException("Solo se puede ampliar una vez cada jornada");

        int precio = this.getValorPxoximaAmpliacionOjeadores();
        this.setOjeadores(this.getOjeadores() + 1);         
        this.setPresupuesto(this.getPresupuesto() - precio);
        this.setAmpliarOjeadores(true);
        
        return precio;
    }
    
    public abstract ArrayList getJugadores();    
    
    public abstract Entrenador getEntrenador();
    
    public int getCosteJugadores(){
        
        int coste = 0; 
        for (Jugador jug : (ArrayList<Jugador>) this.getJugadores()) 
            coste = coste + jug.getFicha();
        return coste;        
    }   
    
    public int getCosteEntrenador(){

        int coste = this.getEntrenador().getFicha();
        return coste;
    }
    
    public int getCosteEquipoTecnico(){
        // Un 20% por cada tecnico

        int coste = SALARIO_BASE + (int) (this.getEntrenador().getFicha() * 0.10);
        coste = coste + (this.getEqTecnico() * 10 - 10);
        coste = this.getEqTecnico() * coste ;
        
        return coste;
    }
    
    public int getCosteOjeadores(){
        // Un 20% por cada tecnico

        int coste = SALARIO_BASE + (this.getOjeadores() * 10 - 10);
        coste = this.getOjeadores() * coste ;
        
        return coste;
    }
    
   
    
    public int getCosteTotal(){
        return this.getCosteFichas() +
                this.getCosteMedioGestion() +
                this.getCosteMedioMantenimiento();
    }
    
    
    public int getCosteFichas(){
        return this.getCosteJugadores() +
                this.getCosteEntrenador() +
                this.getCosteEquipoTecnico() +
                this.getCosteOjeadores();
    }
    
    public int getBalanceAproximado(){
        
        return this.getIngresosMediosCampo() +
                this.getIngresosMediosPublicidad() -
                this.getCosteTotal();
        
    }
        
    public int pagarFichas(){
        int cantidad = this.getCosteFichas();
        this.setPresupuesto(this.getPresupuesto() - cantidad);
        return cantidad;
    }
    
    
    public int getIngresosMediosCampo(){
        // a un precio de 15 y un 45% de capacidad y divido por 2 
        // ya que solo hay la mitad de jornadas en casa
        return ((int) (this.getCampo() * 0.45) * 15 /2)  ;
        
    }
    
       
    public int getCosteMedioMantenimiento(){
        
        return this.getCampo() * 5 / 10;
        
    }
    
    public int getCosteMedioGestion(){
        
        return this.getPublicidad() * CANTIDAD_BASE / 10;
        
    }
    
    public int pagarMantenimientoEstadio(){
        
        int cantidad = 0;
        int min = (this.getCampo() * 5) * 25 / 100; 
        int max = (this.getCampo() * 5) * 75 / 100;
        
        if (Calculos.obtener(10)) {
            cantidad = Calculos.valorAleatorio(min, max);
            this.setPresupuesto(this.getPresupuesto() - cantidad);
        }
        
        return cantidad;        
    }
    
    public int pagarGestion(){
        
        int cantidad = 0;
        
        if (Calculos.obtener(10)) {
            cantidad = Calculos.valorAleatorio(this.getPublicidad() * CANTIDAD_BASE);
            this.setPresupuesto(this.getPresupuesto() - cantidad);
        }       
        
        return cantidad;        
    }
    
    public int perderContratosPublicitarios(int numero){
        
        
        if (this.isAutomatico()) return 0;   
        
        if (this.getPublicidad() - numero < 0) numero = this.getPublicidad();
        
        this.setPublicidad(this.getPublicidad() - numero);
        
        return numero;

        
    }
    
     public int ganarContratosPublicitarios(int numero){
        // como maximo se obtienen 20 contratos 
        
        if (this.isAutomatico()) return 0; 
        
        int actuales = this.getPublicidad();
        
        this.setPublicidad(actuales + numero);
        
        if (this.getPublicidad() > 20) {
            this.setPublicidad(20);
            numero =  20 - actuales;
        }
        
        return numero;

        
    }
    
   
    public boolean equals(Equipo obj) {

        return obj.getId() == this.getId();
    }

    public int compareTo(Equipo o) {

         if (o == null)
            throw new NullPointerException("Referencia nula");

        return this.getClub().compareTo(o.getClub());

    }

    public boolean isJuegaEnCasa() {
        return juegaEnCasa;
    }

    public void setJuegaEnCasa(boolean juegaEnCasa) {
        this.juegaEnCasa = juegaEnCasa;
    }

    public Date getFechaBolsa() {
        return fechaBolsa;
    }

    public void setFechaBolsa(Date fechaBolsa) {
        this.fechaBolsa = fechaBolsa;
    }
    
    public String getFechaBolsaTxt() {
        String txt = "";
        if (this.getFechaBolsa() != null){
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            txt = sdf.format(this.getFechaBolsa());
        }
            
        return txt;
    }

    public int getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(int totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public int getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(int totalGastos) {
        this.totalGastos = totalGastos;
    }
    
    public int getSaldoCuentas(){
        return this.getTotalIngresos() - this.getTotalGastos();
    }
    
     public int getDescuadreCuentas(){
        return this.getSaldoCuentas() - this.getPresupuesto();
    }
    
    public boolean isSaldoCuentasOK(){
        return this.getSaldoCuentas() == this.getPresupuesto();
    }

    public String getNombreCampo() {
        
        if (nombreCampo == null)
            return "";
        
        nombreCampo = StringUtil.truncate(nombreCampo, 50);
        return nombreCampo;
    }

    public void setNombreCampo(String nombreCampo) {
        if (nombreCampo == null)
            this.nombreCampo = "";
        else 
            this.nombreCampo = StringUtil.removeCharsEspeciales(nombreCampo);
    }

    public long getIdClub() {
        return idClub;
    }

    public void setIdClub(long idClub) {
        this.idClub = idClub;
    }

      
    private static class PresupuestoComparator implements Comparator<Equipo> {

        public int compare(Equipo o1, Equipo o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");

            return Integer.valueOf(o1.getPresupuesto()).compareTo(
                    Integer.valueOf(o2.getPresupuesto()));
        }
    }

    public static Comparator<Equipo> PresupuestoComparator(){

        return new PresupuestoComparator();

    }



}
