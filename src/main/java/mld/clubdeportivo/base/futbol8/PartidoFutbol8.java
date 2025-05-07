package mld.clubdeportivo.base.futbol8;

import static java.lang.Math.abs;
import static java.lang.String.valueOf;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.CronicaStrings;
import mld.clubdeportivo.base.Partido;
import mld.clubdeportivo.utilidades.Calculos;
import java.util.logging.*;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static mld.clubdeportivo.base.CronicaStrings.chuta;
import static mld.clubdeportivo.base.CronicaStrings.cortaPase;
import static mld.clubdeportivo.base.CronicaStrings.cubre;
import static mld.clubdeportivo.base.CronicaStrings.derriba;
import static mld.clubdeportivo.base.CronicaStrings.falta;
import static mld.clubdeportivo.base.CronicaStrings.finPartido;
import static mld.clubdeportivo.base.CronicaStrings.iniciaAtaque;
import static mld.clubdeportivo.base.CronicaStrings.lesion;
import static mld.clubdeportivo.base.CronicaStrings.lesionPortero;
import static mld.clubdeportivo.base.CronicaStrings.pasa;
import static mld.clubdeportivo.base.CronicaStrings.pasaAtras;
import static mld.clubdeportivo.base.CronicaStrings.paseLargo;
import static mld.clubdeportivo.base.CronicaStrings.paseLargoPerdido;
import static mld.clubdeportivo.base.CronicaStrings.pasePerdido;
import static mld.clubdeportivo.base.CronicaStrings.penalti;
import static mld.clubdeportivo.base.CronicaStrings.pierde;
import static mld.clubdeportivo.base.CronicaStrings.porteroSale;
import static mld.clubdeportivo.base.CronicaStrings.primerAtaque;
import static mld.clubdeportivo.base.CronicaStrings.quedaSolo;
import static mld.clubdeportivo.base.CronicaStrings.quita;
import static mld.clubdeportivo.base.CronicaStrings.regatea;
import static mld.clubdeportivo.base.CronicaStrings.tarjeta;
import static mld.clubdeportivo.base.CronicaStrings.tienePelota;
import static mld.clubdeportivo.base.CronicaStrings.tiroDespejado;
import static mld.clubdeportivo.base.CronicaStrings.tiroRebotado;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.Cabeza;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.Falta;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.Penalti;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.TiroArea;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.TiroLejano;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.TiroLejanoSolo;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.TiroSolo;
import static mld.clubdeportivo.base.futbol8.ClaseTiro.values;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.MORAL_INICIAL;
import static mld.clubdeportivo.base.futbol8.EquipoFutbol8.NUMERO_MINIMO_JUGADORES;
import static mld.clubdeportivo.base.futbol8.EsfuerzoFutbol8.Maximo;
import static mld.clubdeportivo.base.futbol8.EsfuerzoFutbol8.Relajado;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.Agresivos;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.Contraataque;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.Estaticos;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.Presionar;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.Ralentizar;
import static mld.clubdeportivo.base.futbol8.EstrategiaFutbol8.Tirar;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Defensa;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Delantero;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Medio;
import static mld.clubdeportivo.base.futbol8.PosicionJugFutbol8.Portero;
import static mld.clubdeportivo.utilidades.Calculos.obtener;
import static mld.clubdeportivo.utilidades.Calculos.obtenerResultado;
import static mld.clubdeportivo.utilidades.Calculos.valorAleatorio;


public final class PartidoFutbol8 extends Partido{
    
    private static Logger logger = getLogger(PartidoFutbol8.class.getName());
    public static int NUMERO_SIMULACIONES = 100;
    public static int MINUTOS_PARTIDO = 90;

    private String tipo;
    private boolean simulacion;
    private int segundo; // para llevar la cuenta de los minutos del partido
    private ArrayList<JugadorFutbol8> goleadores;
    private ArrayList<AlineacionFutbol8> alineaciones;
    private EstadisticaPartidoFutbol8 estadistica;
    private EquipoFutbol8 eqLocal;
    private EquipoFutbol8 eqVisit;
    private AlineacionFutbol8 aliLocal;
    private AlineacionFutbol8 aliVisit;
    private int golesLocal;
    private int golesVisitante;

    ArrayList<CronicaFutbol8> cronica = new ArrayList<>();
    
    public ArrayList<CronicaFutbol8> getCronica(){
        return this.cronica;
    }
    
    public void setCronica(ArrayList<CronicaFutbol8> cronica){
        this.cronica = cronica;
    }
    
    private EnumMap<CronicaStrings, String> mapeo(){
        
        var mapeo = new EnumMap<CronicaStrings, String>(CronicaStrings.class);
        mapeo.put(primerAtaque, " inicia el primer ataque");
        mapeo.put(tienePelota, " tiene la pelota");
        mapeo.put(cubre, " va a cubrir");
        mapeo.put(pasa, " pasa la pelota a ");
        mapeo.put(pasaAtras, " pasa hacia atras la pelota a ");
        mapeo.put(quita, " le quita la pelota a ");
        mapeo.put(regatea, " regatea a ");
        mapeo.put(falta, " hace una falta a ");
        mapeo.put(derriba, " derriba claramente a ");
        mapeo.put(paseLargo, " hace un pase largo a ");
        mapeo.put(pierde, " no sabe que hacer con la pelota y la pierde");
        mapeo.put(pasePerdido, " hace un pase malo que se pierde por la banda");
        mapeo.put(paseLargoPerdido, " hace un pase largo que se pierde por la banda");
        mapeo.put(iniciaAtaque, " inicia el ataque");
        mapeo.put(finPartido, "El arbitro pita el final del partido");
        mapeo.put(tarjeta, "La entrada ha sido muy dura y el arbitro saca la tarjeta amarilla a ");
        mapeo.put(penalti, " le derriba dentro del area y el arbitro pita penalti y saca la tarjeta amarilla");
        mapeo.put(lesion, " parece haber sufrido un duro golpe, y tiene que ser atendido"); 
        mapeo.put(lesionPortero, " parece haber sufrido un hecho daño, y tiene que ser atendido");
        mapeo.put(quedaSolo, " se queda solo");
        mapeo.put(porteroSale, " sale a tapar a ");
        mapeo.put(cortaPase, " pone la pierna y le corta el balon a ");
        mapeo.put(chuta, " se coloca el balon y chuta ");
        mapeo.put(tiroDespejado, " despeja el balon ");
        mapeo.put(tiroRebotado, " la pelota rebota en ");
        return mapeo;
    }
    private void escribir(String accion){            
        
        this.cronica.add(CronicaFutbol8.escribir(this, accion, "", segundo / 60));        
    }
    
    private void escribir(String accion, String cuadrante, EquipoFutbol8 eq){  
        
        if (eq != null)
            if (eq.equals(eqLocal)) cuadrante = "L" + cuadrante;
            else cuadrante = "V" + cuadrante;
        
        this.cronica.add(CronicaFutbol8.escribir(this, accion, cuadrante, segundo / 60));        
    }
    
    
    public PartidoFutbol8(){
        this.setIsSimulacion(false);
    
    }
    
    public PartidoFutbol8(boolean simulacion){
        this.setIsSimulacion(simulacion);
    
    }
    
    public PartidoFutbol8(String tipo){
        this.tipo = tipo;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }
    
    public void perpararDatos(EquipoFutbol8 eq){
        
        eq.jugadas = 0;
        eq.corners = 0;
        eq.extras = new StringBuilder();
        eq.faltasDir = 0;
        eq.goleadores = new StringBuilder();
        eq.goles = 0;
        eq.lesionados = new StringBuilder();
        eq.penalties = 0;
        eq.posesion = 0;
        eq.tarjetas = new StringBuilder();
        eq.tirArea = 0;
        eq.tirLej = 0;
        var jugs = eq.getAlineacion().jugadores();
        for (var jug : jugs) {
            jug.setCansancioPartido(0);
        }
        
    }
    
    private void sincronizarJugadores(EquipoFutbol8 eq){
        // Sincroniza los jugadores de alineacion y equipo
        // ya que vienen de sitios diferentes
        
        var lista = eq.getAlineacion().jugadores();
        for (var jug : eq.getJugadores()) {
            var anyadir = true;
            for (var jugAli : eq.getAlineacion().jugadores()) 
                if (jugAli.equals(jug)) {
                    anyadir = false;
                    break;
                }
            if (anyadir) lista.add(jug);
            
        }
        
        eq.setJugadores(lista);
        
    }
    
    
    private void inicializarDatos(){
        
        this.cronica.clear();
        eqLocal = (EquipoFutbol8) this.getEqLocal();
        aliLocal = this.getAlineacionEqLocal();
        eqLocal.setAlineacion(aliLocal);
        sincronizarJugadores(eqLocal);
        perpararDatos(eqLocal);
        eqVisit = (EquipoFutbol8) this.getEqVisitante();
        aliVisit = this.getAlineacionEqVisitante();
        eqVisit.setAlineacion(aliVisit);
        sincronizarJugadores(eqLocal);
        perpararDatos(eqVisit);
        
        var juegaCasa = !this.getJornada().getDescripcion().contains("Final");
        eqLocal.setJuegaEnCasa(juegaCasa); 
        //Debug
        //eqLocal.setJuegaEnCasa(false);  
        
        if (juegaCasa)            
            calcularAsistenciaPublico(); 
                
        completarValoracionesJugadores(eqLocal);                
        completarValoracionesJugadores(eqVisit);   
        
    }
    
    
    private void desarrolloPartido(){
        
        var segundosPartido = 60 * MINUTOS_PARTIDO;     
        
        EquipoFutbol8 eqAtq;        
        EquipoFutbol8 eqDef; 
        
        if (eqLocal.isJuegaEnCasa() || obtener(2)){
             eqAtq = eqLocal;        
             eqDef = eqVisit;        
        }
        else {
             eqAtq = eqVisit;        
             eqDef = eqLocal;   
        }
            
        EquipoFutbol8 eqTmp;
        JugadorFutbol8 jugAtq = null;
        JugadorFutbol8 jugDef;
        boolean regate, robo, pase;
        regate = false;
        /**
         * CUADRICULA DEL EQUIPO ATACANTE
         * 
         *    A  B  C  D  E  F  G
         * 3                      Delantera
         * 2                      Centrocampo
         * 1                      Defensa
         * 
         */

        segundo = 1;
        escribir(eqAtq.getNombre() + mapeo().get(primerAtaque));
        int ralentizar;
        while(segundosPartido > segundo){
            if (eqAtq.estrategia().equals(Ralentizar))
                ralentizar = 2;
            else
                ralentizar = 1;
            robo = false;            
            pase= false;
            jugAtq = jugadorDeAtaque(eqAtq, jugAtq);
            var pos = jugAtq.getCuadrante().charAt(1);
            // Si venimos de un regate no ponemos jugador de defensa
            if (regate){
                jugDef = null;
                regate = false;
            }
            else
                jugDef = jugadorQueDefiende(eqDef, jugAtq);
            if (jugDef != null){                
            
                // El jugador no esta solo, alguien le defiende
                escribir(jugDef.getNombre() + mapeo().get(cubre), jugAtq.getCuadrante(), eqAtq);
                
                // TIRO :
                // si estamos en delantera y la estrategia es chutar 
                // si estamos en delantera y sale un random de 1/4
                if (pos == '3' && 
                        (eqAtq.estrategia().equals(Tirar) ||
                        obtener(4))
                        ){
                    var newJug = hacerPreparacionTiro(jugAtq, jugDef);
                    if (newJug == null){
                        // se sigue con el tiro
                        hacerTiro(jugAtq, eqDef.portero(), TiroArea);
                    }
                    else if (newJug.equals(jugAtq)){
                        // Se ha disputado un corner por ejemplo
                    }
                    else{
                        // el balon a sido despejado o rebotado
                        jugAtq = newJug;
                        if (!jugAtq.getEquipo().equals(eqAtq))
                            robo = true;  
                        else
                            pase = true;
                    }
                         
                }
                
                // REGATE :
                // Si la estrategia es contrataque
                // Si estamos en defensa y sale random de 1/5
                // Si estamos en delantera y sale random de 1/3
                // Si estamos en delantera y sale random de 1/2
                else if (eqAtq.estrategia().equals(Contraataque) ||
                        (pos == '1' && obtener(5)) ||
                        (pos == '2' && obtener(3)) ||
                        (pos == '3' && obtener(2)) 
                        ) {
                    jugDef = hacerRegate(jugAtq, jugDef);                    
                    if (jugDef != null){
                        // Pierde la pelota
                        // Comprobamos si ha sido con falta
                        if (!faltaAlHacerRegate(jugAtq, jugDef)){
                            robo = true;
                            jugAtq = jugDef;
                        }
                    }
                    else
                        regate = true;
                } 
                
                // PASE : no hay ninguna otra posibilidad
                else{
                    jugAtq = hacerPase(jugAtq, jugDef);
                    if (jugAtq != null && jugDef != null && jugAtq.equals(jugDef)) robo = true;
                    else if (jugAtq != null) pase = true;
                }
                segundo = segundo + 20 * ralentizar;
            }
            else{
                // El jugador esta solo, nadie le defiende
                
                // Si estamos en delantera o la estrategia es tirar
                if (pos == '3' || eqAtq.estrategia().equals(Tirar)){
                    // tira
                    if (pos == '1' || pos == '2') 
                        hacerTiro(jugAtq, eqDef.portero(), TiroLejanoSolo); 
                    else{                        
                        escribir(jugAtq.getNombre() + mapeo().get(quedaSolo), jugAtq.getCuadrante(), eqAtq);
                        var porteroSale = eqDef.portero() != null && obtener(4);
                        if (porteroSale)// El portero sale y puede hacer penalti
                            salidaPortero(jugAtq, eqDef.portero());                        
                        else                            
                            hacerTiro(jugAtq, eqDef.portero(), TiroSolo);                        
                    }                 
                        
                }
                // Si no pasamos
                else{
                    jugAtq = hacerPase(jugAtq, jugDef);
                    if (jugAtq != null && jugDef != null && jugAtq.equals(jugDef)) robo = true;
                    else if (jugAtq != null) pase = true;
                }
                segundo = segundo + 20 * ralentizar;
            }
            
            if (pase || regate)
                continue;
            else{
                eqAtq.jugadas++;
                eqTmp = eqAtq;
                eqAtq = eqDef;
                eqDef = eqTmp;
                if (!robo)
                    jugAtq = null;
                escribir("");                
                segundo = segundo + 60 * ralentizar;
                if (segundosPartido > segundo) {                    
                    if (jugAtq == null)
                        escribir(eqAtq.getNombre() + mapeo().get(iniciaAtaque));
                    else 
                        escribir(eqAtq.getNombre() + mapeo().get(iniciaAtaque), jugAtq.getCuadrante(), eqAtq);                        
                }
            }
           
            
        }
        
        escribir(""); 
        escribir(mapeo().get(finPartido)); 
        
    }
 
    public EstadisticaPartidoFutbol8 jugarPartido(){
        
        logger.log(INFO, "Inicio Partido");
        
        inicializarDatos();
      
        this.getEstadistica().setAlineacionLocal(aliLocal.alineacionParaEstadistica());
        this.getEstadistica().setAlineacionVisitante(aliVisit.alineacionParaEstadistica());
        
        this.getEstadistica().setGolesLocal(0);
        this.getEstadistica().setGolesVisitante(0);
        
        // si no hay un minimo de 4 jugadores no se disputa el 
        // partido
        var numJugOK = verificarNumJugadores();
        
        if (!this.isSimulacion()) 
            quitarTarjetas();
        
        goleadores = new ArrayList<>(); 
        
        if (numJugOK) desarrolloPartido();
                
        eqLocal.extras.append(
                asignarCansancioFinalJugadores(eqLocal, this.isSimulacion()));
        eqVisit.extras.append(
                asignarCansancioFinalJugadores(eqVisit, this.isSimulacion()));
    
        // pruebas penalties
        // eqLocal.goles = 2;
        // eqVisit.goles = 2;
        
        if (numJugOK){     
            this.setGolesLocal(eqLocal.goles);
            this.setGolesVisitante(eqVisit.goles);
            calcularPosesion();
            this.getEstadistica().setPosesionLocal(eqLocal.posesion);
            this.getEstadistica().setPosesionVisitante(eqVisit.posesion);
            this.getEstadistica().setJugadasLocal(eqLocal.jugadas);
            this.getEstadistica().setJugadasVisitante(eqVisit.jugadas);
            this.getEstadistica().setGolesLocal(eqLocal.goles);
            this.getEstadistica().setGolesVisitante(eqVisit.goles);
            this.getEstadistica().setTirosLejanosLocal(eqLocal.tirLej);
            this.getEstadistica().setTirosLejanosVisitante(eqVisit.tirLej);
            this.getEstadistica().setTirosPuertaLocal(eqLocal.tirArea);
            this.getEstadistica().setTirosPuertaVisitante(eqVisit.tirArea);
            this.getEstadistica().setMoralLocal(eqLocal.getMoral());
            this.getEstadistica().setMoralVisitante(eqVisit.getMoral());
            this.getEstadistica().setGoleadoresLocal(eqLocal.goleadores.toString());
            this.getEstadistica().setGoleadoresVisitante(eqVisit.goleadores.toString());
            this.getEstadistica().setCornersLocal(eqLocal.corners);
            this.getEstadistica().setCornersVisitante(eqVisit.corners);
            this.getEstadistica().setFaltasDirectasLocal(eqLocal.faltasDir);
            this.getEstadistica().setFaltasDirectasVisitante(eqVisit.faltasDir);
            this.getEstadistica().setPenaltiesLocal(eqLocal.penalties);
            this.getEstadistica().setPenaltiesVisitante(eqVisit.penalties);
            this.getEstadistica().setTarjetasLocal(eqLocal.tarjetas.toString());
            this.getEstadistica().setTarjetasVisitante(eqVisit.tarjetas.toString());
            this.getEstadistica().setLesionadosLocal(eqLocal.lesionados.toString());
            this.getEstadistica().setLesionadosVisitante(eqVisit.lesionados.toString());
            this.getEstadistica().setExtrasEqLocal(eqLocal.extras.toString());
            this.getEstadistica().setExtrasEqVisitante(eqVisit.extras.toString());
        }
        else{
            this.setEspectadores(0);
        }
            
        
        if (!this.isSimulacion())           
            asignarMoral();
        
        logger.log(INFO, "Fin Partido");
        
        return  this.getEstadistica();
 
    }
    
    
    /* ACCIONES DEL PARTIDO */
    
    private JugadorFutbol8 jugadorDeAtaque(EquipoFutbol8 eq, JugadorFutbol8 jugAtq){
        
        if (jugAtq == null){
            jugAtq = jugadorInicioJugada(eq);
            jugAtq.getEquipo().posesion++;
            escribir(jugAtq.getNombre() + mapeo().get(tienePelota), jugAtq.getCuadrante(), eq);
        }
        
        return jugAtq;        
    }
    
    private boolean faltaAlHacerRegate(JugadorFutbol8 jugAtq, JugadorFutbol8 jugDef){
        // Si el jugador de defensa le quita la pelota miramos 
        // si ha habido alguna falta
        
        var esFalta = false;
        var esLesion = false;
        var posFalta = 7;
        var posTarjeta = 2;
        var posPenalti = 20;
        var posLesion = 5;
        
        if (jugDef.getEstrategia().equals(Agresivos)){
            posFalta = 3;
            posTarjeta = 1;
            posPenalti = 12;
            posLesion = 2;            
        }
        
        var eqDef = jugDef.getEquipo();
         
        if (jugAtq.getCuadrante().charAt(1) == '3' &&
                obtener(posPenalti)){
            // Ha sido penalti
            escribir(jugDef.getNombre() + mapeo().get(falta) + jugAtq.getNombre()); 
            sacarTarjetaAmarilla(jugDef);
            escribir(jugDef.getNombre() + mapeo().get(penalti));
            lanzarPenalti(jugAtq.getEquipo(), eqDef.portero());
        }              
        else if (obtener(posFalta)){
            // Ha sido falta
            escribir(jugDef.getNombre() + mapeo().get(falta) + jugAtq.getNombre()); 
            if (obtener(posLesion)){
                lesionar(jugAtq);
                escribir(jugAtq.getNombre() + mapeo().get(lesion));
                esLesion = true;
            }
            if (esLesion || obtener(posTarjeta)){
                // ha sido tarjeta
                sacarTarjetaAmarilla(jugDef);
                escribir(mapeo().get(tarjeta) + jugDef.getNombre());
            }            
            lanzarFaltaDirecta(jugAtq.getEquipo(), eqDef.portero());       
        }
        
        
        return esFalta;
        
    }
    
    

    private JugadorFutbol8 hacerRegate(JugadorFutbol8 jugAtq, JugadorFutbol8 jugDef){
        // Si devuelve null es que el jugador sale del regate
        // si devuelve un jugador es el que le ha quitado la pelota
        
        JugadorFutbol8 jug = null;

        sumarCansancio(jugDef);
        sumarCansancio(jugAtq);
        var roba = obtenerResultado(jugDef.getRobo(), jugAtq.getRegate());
        if (roba){
            escribir(jugDef.getRoboInf() + mapeo().get(quita) + jugAtq.getRegateInf(), jugDef.getCuadrante(), jugDef.getEquipo());  
            jug = jugDef;
        }
        else{
            escribir(jugAtq.getRegateInf() +  mapeo().get(regatea) + jugDef.getRoboInf(), jugAtq.getCuadrante(), jugAtq.getEquipo());            
            jugAtq.getEquipo().posesion++;
        }

        return jug;
    }
    
    private void salidaPortero(JugadorFutbol8 jugAtq, JugadorFutbol8 portero){
        // Realiza las acciones si el portero sale a cubrir
        
        escribir(portero.getValoracionInf() +  mapeo().get(porteroSale) + jugAtq.getRegateInf()); 
        
        sumarCansancio(portero);
        
        var atrapa = obtenerResultado(portero.getValoracionPartido(), jugAtq.getRegate());
        
        if (atrapa){
            // Atrapa el balon
            escribir("y atrapa el balon"); 
        }        
        else{
            var op = valorAleatorio(3);
            // pueder ser falta o penalti o tiro
            if (op == 0){
                sacarTarjetaAmarilla(portero);            
                escribir(portero.getNombre() + mapeo().get(penalti));
                lanzarPenalti(jugAtq.getEquipo(), portero);  
            }
            else if (op == 1){
                sacarTarjetaAmarilla(portero);            
                escribir(portero.getNombre() + mapeo().get(falta));
                lanzarFaltaDirecta(jugAtq.getEquipo(), portero);  
            }
            else{
                hacerTiro(jugAtq, portero, TiroSolo); 
            }
        }
    }
    
    private JugadorFutbol8 hacerPase(JugadorFutbol8 jugAtq, JugadorFutbol8 jugDef){
 
        JugadorFutbol8 jugTmp;
        var paseOk = true;
        var pase = 5;
        var paseLargo = 20;
        var eq = jugAtq.getEquipo();
        sumarCansancio(jugAtq);
        
        // si el jugador no esta solo calculamos posibilidad
        // de que el defensor corte el pase
        if (jugDef != null && obtenerResultado(jugDef.getRobo(), jugAtq.getPase())){
            escribir(jugDef.getRoboInf() + mapeo().get(cortaPase) + jugAtq.getPaseInf(), jugDef.getCuadrante(), jugDef.getEquipo()); 
            return jugDef;    
        }
        
        jugTmp = jugadorReceptorCercano(jugAtq);
        if (jugTmp != null)
            if (obtenerResultado(jugTmp.getPase(), pase))
                escribir(jugAtq.getNombre() + mapeo().get(pasa) + jugTmp.getNombre(), jugTmp.getCuadrante(), eq);
            else{
                escribir(jugAtq.getPaseInf() + mapeo().get(pasePerdido));  
                paseOk = false;
            }
        else{
            jugTmp = jugadorReceptorLejano(jugAtq);
            if (jugTmp != null){
                if (obtenerResultado(jugTmp.getPase(), paseLargo))
                    escribir(jugAtq.getPaseInf() + mapeo().get(paseLargo) + jugTmp.getNombre(), jugTmp.getCuadrante(), eq);
                else{
                    escribir(jugAtq.getPaseInf() + mapeo().get(paseLargoPerdido));  
                    paseOk = false;
                }      
            }else{
                jugTmp = jugadorReceptorMismaLinea(jugAtq);
                if (jugTmp != null)
                    escribir(jugAtq.getNombre() + mapeo().get(pasa) + jugTmp.getNombre(), jugTmp.getCuadrante(), eq);
                else {
                    // no puede pasar a nadie
                    // Hay un 50% de posibilidades de pasar hacia atras o perder el balon
                    if (obtener(2)){
                        jugTmp = jugadorReceptorTrasero(jugAtq);
                    }
                    if (jugTmp != null)
                        escribir(jugAtq.getNombre() + mapeo().get(pasaAtras) + jugTmp.getNombre(), jugTmp.getCuadrante(), eq);
                    else{
                        escribir(jugAtq.getNombre() + mapeo().get(pierde));
                        paseOk = false;
                    }
                }
            }
        }
        
        if (paseOk)
            jugAtq.getEquipo().posesion++; 
        else
            jugTmp = null;
        
        return jugTmp;
    }
    
    private JugadorFutbol8 hacerPreparacionTiro(JugadorFutbol8 jugAtq, JugadorFutbol8 jugDef){
        // este metodo tiene en cuenta la accion del defensor
        // si se devuelve null es que se sigue con el tiro
   
        JugadorFutbol8 newJug;
        
        if (jugDef == null) 
            return null;
        else{
            if (obtenerResultado(jugDef.getRobo() / 4, jugAtq.getTiro())){
                // El defensa despeja el tiro
                escribir(jugAtq.getTiroInf() + mapeo().get(chuta));
                sumarCansancio(jugDef);
                sumarCansancio(jugAtq);
                escribir(jugDef.getRoboInf() + mapeo().get(tiroDespejado));
                // sale aleatoriamente
                newJug = jugadorReceptorAleatorio();
                escribir(newJug.getNombre() + " se hace con el control del balon", newJug.getCuadrante(), newJug.getEquipo());
                
            }
            else if (obtener(4)){
                // La pelota rebota en el defensa
                escribir(jugAtq.getTiroInf() + mapeo().get(chuta));
                sumarCansancio(jugDef);
                sumarCansancio(jugAtq);
                escribir(mapeo().get(tiroRebotado) + jugDef.getNombre());
                if (obtener(2)){
                    // Va a corner
                    escribir("y la pelota sele por la linea de fondo a corner");
                    disputarCorner(jugAtq.getEquipo(), jugDef.getEquipo());
                    newJug = jugAtq;
                }
                else{
                    // sale aleatoriamente
                    newJug = jugadorReceptorAleatorio();
                    escribir(newJug.getNombre() + " se hace con el control del balon", newJug.getCuadrante(), newJug.getEquipo());
                }
                
            }
            else
               return null;
        }
        
        return newJug;
        
    }
    
    private boolean hacerTiro(JugadorFutbol8 jugAtq, JugadorFutbol8 portero, ClaseTiro clase){
        
        // Devuelve true si es gol
        
        var isGol = false;
        TiroPuertaFutbol8 tiro;
        var eq = jugAtq.getEquipo();
        var txt = "";
        sumarCansancio(jugAtq);
        
        if (clase.equals(TiroArea)){
            
            txt = " pisa el area y chuta";
            eq.tirArea++;
        }
        else if (clase.equals(TiroSolo)){
            txt = " se mete en el area y chuta";
            eq.tirArea++;
        }
        else if (clase.equals(TiroLejano)){
            txt = " no se lo piensa y tira desde lejos";
            eq.tirLej++;
        }
        else if (clase.equals(TiroLejanoSolo)){
            txt = " se queda solo y tira desde lejos";  
            eq.tirLej++;
        }
        else if (clase.equals(Falta)){
            txt = " tira la falta";  
            eq.faltasDir++;
        }
        else if (clase.equals(Penalti)){
            txt = " chuta desde el punto de penalti";  
            eq.penalties++;
        }
        else if (clase.equals(Cabeza)){
            txt = " remata de cabeza";  
            eq.corners++;
        }
        
        escribir(jugAtq.getTiroInf() + txt, jugAtq.getCuadrante(), jugAtq.getEquipo());
        tiro = new TiroPuertaFutbol8(jugAtq, portero, clase);
        var valInfPortero = "a puerta vacia";
        EquipoFutbol8 eqPortero = null;
        if (portero != null){
            eqPortero = portero.getEquipo();
            valInfPortero = portero.getValoracionInf();
        }
       
        tiro.hacerTiro(); 
        if (tiro.isEsCorner()){
            escribir("Y " + valInfPortero + " envia la pelota a corner");
            disputarCorner(jugAtq.getEquipo(), eqPortero);            
        }
        else if (tiro.isEsGol()){
            registrarGol(jugAtq, portero, tiro);
            isGol = true;
            escribir("Y a sido Gooooooooooool, " + valInfPortero + " no puede hacer nada", "GO", eqPortero);
        }
        else if (tiro.isParaPortero())
            escribir("Y paradon de " + valInfPortero, portero.getCuadrante(), eqPortero);
        else if (tiro.isEsFuera())
            escribir("Y la pelota sale fuera de porteria");  
        
        // Si no es gol ni fuera calculamos lesion portero
        if (!tiro.isEsGol() && !tiro.isEsFuera() && portero != null)
            posibleLesionPortero(portero);  
        
        return isGol;
        
    }
    
   
    
       
    private void registrarGol(JugadorFutbol8 jug, JugadorFutbol8 portero, TiroPuertaFutbol8 tiro){
        
        var eq = jug.getEquipo();
        
        eq.goles++;         
                
        eq.goleadores.append(jug.getNombre());
        eq.goleadores.append("(").append(tiro.getTxtClase()).append(")");
        eq.goleadores.append("min. ").append((int)(segundo / 60));
        eq.goleadores.append(";");
      
        if (!this.isSimulacion()){
            if (this.getTipo().equals("Liga")){
                jug.setGolesLiga(jug.getGolesLiga() + 1);
                if (portero != null)
                    portero.setGolesLiga(portero.getGolesLiga() + 1);
            }
            if (this.getTipo().equals("Copa")){
                jug.setGolesCopa(jug.getGolesCopa() + 1);
                if (portero != null)
                    portero.setGolesCopa(portero.getGolesCopa() + 1);
            }
            this.getGoleadores().add(jug);        
        }
    }
    
              
    
    private void calcularPosesion(){
        
        logger.log(INFO, "Calcular Posesion");

        var posTotal = eqLocal.posesion + eqVisit.posesion;
        eqLocal.posesion = eqLocal.posesion * 100 / posTotal;
        eqVisit.posesion = eqVisit.posesion * 100 / posTotal;
        
        while (100 - eqLocal.posesion - eqVisit.posesion > 0)
            eqLocal.posesion++;
        
        logger.log(INFO, "Calcular Posesion OK");
       
    }
    
    
    private JugadorFutbol8 jugadorInicioJugada(EquipoFutbol8 eq){
        
        var ali = eq.getAlineacion();
        JugadorFutbol8 jug = null;
        
         if (ali.defensas().size() > 0){
             var numJug = valorAleatorio(ali.defensas().size());                        
             jug = ali.defensas().get(numJug); 
         }
         else if (ali.centrocampistas().size() > 0){
             var numJug = valorAleatorio(ali.centrocampistas().size());                        
             jug = ali.centrocampistas().get(numJug); 
         }
         else if (ali.delanteros().size() > 0){
             var numJug = valorAleatorio(ali.delanteros().size());                        
             jug = ali.delanteros().get(numJug); 
         }
  
         return jug;    
        
    }
    
     private JugadorFutbol8 jugadorReceptorAleatorio(){
        
        
        var jugs = new ArrayList<JugadorFutbol8>();
        
        jugs.addAll(aliLocal.jugadoresCampo());
        jugs.addAll(aliVisit.jugadoresCampo());
        
        var x = valorAleatorio(jugs.size());
        var jug = jugs.get(x);        
         
        return jug;    
        
    }
    
    private JugadorFutbol8 jugadorQueDefiende(EquipoFutbol8 eq, JugadorFutbol8 jugAtq){
        // 1 linea defensiva
        // 2 Centrocampo
        // 3 Delantera
        
        var ali = eq.getAlineacion();
        JugadorFutbol8 jug;
        int pos = jugAtq.getCuadrante().charAt(1);
        var presion = ali.getEstrategia().equals(Presionar);
        var extraCansacio = 0;
        var capacidadRobo = 100;
                
        jug = ali.jugadorContrario(jugAtq.getCuadrante());
        
        // Si la estrategia es Estatica el defensor no se desplaza
        if (jug == null && ali.getEstrategia().equals(Estaticos))
            return null;
        
        if (jug == null && ((pos == '1' && presion)
                || pos == '2' || pos == '3')){
            jug = ali.jugadorContrarioCercano(jugAtq.getCuadrante());
            extraCansacio = 2;
            capacidadRobo = 90;
        }
            
        if (jug == null && ((pos == '2' && presion) || pos == '3')){
            jug = ali.jugadorContrarioProximo(jugAtq.getCuadrante());
            extraCansacio = 4;
            capacidadRobo = 70;
        } 
         
        if (jug != null){
            sumarCansancio(jug, extraCansacio);
            jug.setCapacidadRobo(capacidadRobo);
        }
        
        return jug;    
        
    }
    
    private JugadorFutbol8 jugadorReceptorCercano(JugadorFutbol8 jugPase){
        
        // Si el jugador esta en delantera no aplica
        if (jugPase.getCuadrante().charAt(1) == '3') return null;
        
        var ali = jugPase.getEquipo().getAlineacion();
        var jug = ali.jugadorParaPase(jugPase.getCuadrante());
         
         return jug;    
        
    }
    
        
    private JugadorFutbol8 jugadorReceptorMismaLinea(JugadorFutbol8 jugPase){
        
        var ali = jugPase.getEquipo().getAlineacion();
        var jug = ali.jugadorParaPaseMismaLinea(jugPase.getCuadrante());
         
         return jug;    
        
    }
    
     private JugadorFutbol8 jugadorReceptorLejano(JugadorFutbol8 jugPase){
        
         // Si el jugador esta en delantera no aplica
         if (jugPase.getCuadrante().charAt(1) == '3') return null;
         
         var ali = jugPase.getEquipo().getAlineacion();
         var jug = ali.jugadorParaPaseLargo(jugPase.getCuadrante());
         
         return jug;    
        
    }
     
     private JugadorFutbol8 jugadorReceptorTrasero(JugadorFutbol8 jugPase){
        
        // Si el jugador esta en delantera no aplica
        if (jugPase.getCuadrante().charAt(1) == '1') return null;
        
        var ali = jugPase.getEquipo().getAlineacion();
        var jug = ali.jugadorParaPaseAtras(jugPase.getCuadrante());
         
         return jug;    
        
    }
     
    
           
    private void disputarCorner(EquipoFutbol8 eqAtq, EquipoFutbol8 eqDef){
        
        logger.log(INFO, "Calcular Corners");
        
         
        var aliAtq = eqAtq.getAlineacion();
        var aliDef = eqDef.getAlineacion();
        
        int numJug;

        if (aliAtq.jugadoresCampo().size() > 0){
                    
            numJug = valorAleatorio(aliAtq.jugadoresCampo().size());                        
            var jugAtq = aliAtq.jugadoresCampo().get(numJug);
            numJug = valorAleatorio(aliDef.jugadoresCampo().size());
            var jugDef = aliDef.jugadoresCampo().get(numJug);
            escribir("Se lanza el corner y " + jugAtq.getRegateInf() + " y " + jugDef.getRoboInf() + " saltan a por el balon");
            if (obtenerResultado(jugAtq.getRegate(), jugDef.getRobo())){
                sumarCansancio(jugDef);
                sumarCansancio(jugAtq);
                hacerTiro(jugAtq, aliDef.getPortero(), Cabeza);
            }
            else
                escribir("El " + eqDef.getNombre() + " recupera el balon");
                    
        }     
        
        logger.log(INFO, "Calcular Corners OK");
       
    }
    
    
    
    private void lanzarFaltaDirecta(EquipoFutbol8 eqAtq, JugadorFutbol8 portero){

        logger.log(INFO, "Calcular Faltas Directas");
        
        int numJug;
        
        
        var aliAtq = eqAtq.getAlineacion();
        var jugs = new ArrayList<JugadorFutbol8>();
        
        jugs.addAll(aliAtq.centrocampistas());
        jugs.addAll(aliAtq.delanteros());
        
        if (!jugs.isEmpty()){
            
            // Va a puerta o no
            if (obtener(3)) return;
            
            numJug = valorAleatorio(jugs.size());                        
            var jugAtq = jugs.get(numJug);
            hacerTiro(jugAtq, portero, Falta);
        }    
        
        logger.log(INFO, "Calcular Faltas Directas OK");
       
    }
    
    private void lanzarPenalti(EquipoFutbol8 eqAtq, JugadorFutbol8 portero){

        logger.log(INFO, "lanzar Penalti");
        
        int numJug;
        
        var aliAtq = eqAtq.getAlineacion();
        var jugs = new ArrayList<JugadorFutbol8>();
        
        if (!aliAtq.delanteros().isEmpty()) jugs = aliAtq.delanteros();
        else if (!aliAtq.centrocampistas().isEmpty()) jugs = aliAtq.delanteros();
                
        if (!jugs.isEmpty()){
            numJug = valorAleatorio(jugs.size());                        
            var jugAtq = jugs.get(numJug);
            hacerTiro(jugAtq, portero, Penalti);
        }    
        
       logger.log(INFO, "lanzar Penalti OK");
       
    }
    
       
    public EquipoFutbol8 getGanador(){
        
        EquipoFutbol8 eq = null;
        
        if (this.getGolesLocal() > this.getGolesVisitante())
            eq = (EquipoFutbol8) this.getEqLocal();
        else if (this.getGolesLocal() < this.getGolesVisitante())
            eq = (EquipoFutbol8) this.getEqVisitante();
        
        return eq;
        
    }
    
     public EquipoFutbol8 getPerdedor(){
        
        EquipoFutbol8 eq = null;
        
        if (this.getGolesLocal() < this.getGolesVisitante())
            eq = (EquipoFutbol8) this.getEqLocal();
        else if (this.getGolesLocal() > this.getGolesVisitante())
            eq = (EquipoFutbol8) this.getEqVisitante();
        
        return eq;
        
    }


    public ArrayList<AlineacionFutbol8> getAlineaciones() {
        return alineaciones;
    }
    
     public AlineacionFutbol8 getAlineacionEqLocal() {
        AlineacionFutbol8 ali = null;
         for (var unaAli : this.getAlineaciones()) {
             if(unaAli.getEquipo().equals(this.getEqLocal()))
                 ali = unaAli;
         }
         return ali;
    }
     
     public AlineacionFutbol8 getAlineacionEqVisitante() {
        AlineacionFutbol8 ali = null;
         for (var unaAli : this.getAlineaciones()) {
             if(unaAli.getEquipo().equals(this.getEqVisitante()))
                 ali = unaAli;
         }
         return ali;
    }

    public void setAlineaciones(ArrayList<AlineacionFutbol8> alineaciones) {
        this.alineaciones = alineaciones;
    }
    
    public void asignarPuntos() {
        
        if (this.isSimulacion()) return;

        logger.log(INFO, "Asignar Puntos");
        
        var eqL = (EquipoFutbol8) this.getEqLocal();
        var puntsL = eqL.getPuntuacion();
        var eqV = (EquipoFutbol8) this.getEqVisitante();
        var puntsV = eqV.getPuntuacion();
        
        Club club;

        if (this.getGolesLocal() > this.getGolesVisitante()){
            puntsL.setPuntos(puntsL.getPuntos() + 3);
            club = eqL.getClub();
            club.setRanking(club.getRanking() + 3);
            puntsL.setVictorias(puntsL.getVictorias() + 1);
            puntsV.setDerrotas(puntsV.getDerrotas() + 1);
        }
        else if(this.getGolesLocal() < this.getGolesVisitante()){
            puntsV.setPuntos(puntsV.getPuntos() + 3);
            club = eqV.getClub();
            club.setRanking(club.getRanking() + 3);
            puntsV.setVictorias(puntsV.getVictorias() + 1);
            puntsL.setDerrotas(puntsL.getDerrotas() + 1);
        }
        else{
            puntsL.setPuntos(puntsL.getPuntos() + 1);
            puntsV.setPuntos(puntsV.getPuntos() + 1);
            puntsL.setEmpates(puntsL.getEmpates() + 1);
            puntsV.setEmpates(puntsV.getEmpates() + 1);
            club = eqV.getClub();
            club.setRanking(club.getRanking() + 1);
        }

        puntsL.setGolesFavor(puntsL.getGolesFavor() + this.getGolesLocal());
        puntsL.setGolesContra(puntsL.getGolesContra() + this.getGolesVisitante());
        puntsV.setGolesFavor(puntsV.getGolesFavor() + this.getGolesVisitante());
        puntsV.setGolesContra(puntsV.getGolesContra() + this.getGolesLocal());
        
        logger.log(INFO, "Asignar Puntos OK");

    }
    
    public void asignarMoral() {
        
        if (this.isSimulacion()) return;
        
        logger.log(INFO, "Asignar Moral");
        
        var eqL = (EquipoFutbol8) this.getEqLocal();
        var eqV = (EquipoFutbol8) this.getEqVisitante();
        
        
        if (this.getGanador() == null && eqL.isJuegaEnCasa()){
            eqV.modificarMoral(eqV.getMoral() + 1 + (MORAL_INICIAL - eqV.getMoral()) / 9);
            eqL.modificarMoral(eqL.getMoral() - 1);
        }
        else if (this.getGanador() != null && this.getGanador().equals(eqV) && eqL.isJuegaEnCasa()){
            eqV.modificarMoral(eqV.getMoral() + 3 + (MORAL_INICIAL - eqV.getMoral()) / 3);
            eqL.modificarMoral(eqL.getMoral() - 3);
        }
        
        // ademas si los equipos han jugado en Maximo sumamos o 
        // quitamos un punto si ganan o pierden
        if (this.getGanador() != null){
            if (eqL.getAlineacion().getEsfuerzo().equals(Maximo))
                if (this.getGanador().equals(eqL)) 
                    eqL.modificarMoral(eqL.getMoral() + 1);
                else
                    eqL.modificarMoral(eqL.getMoral() - 1);
            if (eqV.getAlineacion().getEsfuerzo().equals(Maximo))
                if (this.getGanador().equals(eqV)) 
                    eqV.modificarMoral(eqV.getMoral() + 1);
                else
                    eqV.modificarMoral(eqV.getMoral() - 1);
        }
        
        logger.log(INFO, "Asignar Moral OK");
        
    }

    private void calcularAsistenciaPublico() {
        
        logger.log(INFO, "Calcular Publico");
        
        int interes, porcentaje;
        var comp = (CompeticionFutbol8) this.getJornada().getCompeticion();
        var precioEntradas = eqLocal.getPrecioEntradas();
        this.setPrecioEntradas(precioEntradas);
        
        if (comp.getClase().equals("Liga")){ 
            
            var puntLocal = eqLocal.getPuntuacion().getPuntos();
            var puntVisit = eqVisit.getPuntuacion().getPuntos();
            var difPuntos = abs(puntLocal - puntVisit);
            
            // si el local es el que va por detras lo compensamos
            if (puntLocal < puntVisit) 
                difPuntos = valorAleatorio(difPuntos);         
            
            interes = 25 - difPuntos;   
            if (interes < 5 ) interes = 5;

        }
        else
            interes = 25;
        
        // Segun el precio de las entradas calculamos el minimo
        var pon = 0;
        if (precioEntradas == 5) pon = 24;
        else if (precioEntradas == 10) pon = 12;
        else if (precioEntradas == 15) pon = 9;
        else if (precioEntradas == 20) pon = 6;
        else if (precioEntradas == 25) pon = 3;
        
        /* En funcion de si el equipo local juega con exfuerzo maximo
         multiplicamos por 2 y si los 2 equipos juegan con esfuerzo maximo 
         por 4 */
        double max = 1;
        if (eqLocal.getAlineacion().getEsfuerzo().equals(Maximo)){
            max = 1.5;
            if (eqVisit.getAlineacion().getEsfuerzo().equals(Maximo))
                max = 4;
        }
        pon = (int) (pon * max);
          
        porcentaje = 0;
        for (var i = 1; i <= 4; i++)
            if (obtenerResultado(pon, 9))
                porcentaje = porcentaje + interes;  
        
        // Si tubiesemos un porcentaje final de cero hacemos un random
        // sobre el interes
        if (porcentaje == 0) porcentaje = valorAleatorio(0, interes);
            
        var asistencia = eqLocal.getCampo() * porcentaje / 100;
        
        this.setEspectadores(asistencia);
        
        logger.log(INFO, "Calcular Publico OK");
                
    }
    
    private void completarValoracionesJugadores(EquipoFutbol8 eq) {

        logger.log(INFO, "Completar valoraciones");
        
        var minJugarEnCasa = 10;
        var ali = eq.getAlineacion();
        var moral = eq.getMoral();
        // moral segun primas
        var primas = ali.getPrimas();
        var valMoral = 0;
        if (primas > 0)
            valMoral = valorAleatorio(primas / 100);
               
        if (valMoral > 0){
            moral = moral + valMoral;
            eq.extras.append("Moral +").append(valMoral).append(";");
        }
        
        // factor campo
        var campo = 0;
        if (eq.isJuegaEnCasa()){
            campo = minJugarEnCasa + (this.getEspectadores() / 100);
            if (campo > 30) campo = 30;
            eq.extras.append("ESTADIO: ").append(eq.getNombreCampo()).append(";");
            eq.extras.append("Capacidad: ").append(eq.getCampo()).append(";");
            eq.extras.append("Precio: ").append(eq.getPrecioEntradas()).append(";");
            eq.extras.append("Espectadores: ").append(this.getEspectadores()).append(";");
            eq.extras.append("Soporte Moral: +").append(campo).append(";");
        }
        
        // sumamos 10% si esfuerzo es maximo y -10% si es minimo
        var esf = ali.getEsfuerzo();
        // calculamos plus antrenador
        var entr = eq.getEntrenador();
        var plusTact = entr.obtenerPlusTactica(ali.getTactica());
        if (!this.isSimulacion()){
            entr.setPlusTactica(plusTact);
           entr.setUltimaTacticaUtilizada(ali.getTactica());
        }        
        
        int valFinal;
        
        var jugs = ali.jugadores();
        for (var jug : jugs) {
            if (jug == null) continue;
            valFinal = jug.getValoracionMedia();
            valFinal = valFinal * moral / 100;
            
            var suplementos = campo + plusTact;
            if (esf.equals(Maximo))
                suplementos = suplementos + 10;
            else if (esf.equals(Relajado))
                suplementos = suplementos - 10;
            
            valFinal = valFinal + (valFinal * suplementos / 100);
            
            jug.setValoracionPartido(valFinal);
            jug.setEstrategia(ali.getEstrategia());
            // Si el jugador esta fuera de su posicion lo marcamos
            jug.setFueraPosicion(false);
            if (!jug.getPosicion().equals(Portero))
                    if (
                            (jug.getCuadrante().charAt(1) == '1' && 
                            !jug.getPosicion().equals(Defensa)) ||
                            (jug.getCuadrante().charAt(1) == '2' && 
                            !jug.getPosicion().equals(Medio)) ||
                            (jug.getCuadrante().charAt(1) == '3' && 
                            !jug.getPosicion().equals(Delantero))
                            )
                        jug.setFueraPosicion(true);           
        }   
        
        if (esf.equals(Maximo))
                eq.extras.append("Esfuerzo +10%" + ";");
            else if (esf.equals(Relajado))
                eq.extras.append("Esfuerzo -10%" + ";");
        
        if (plusTact > 0)
                eq.extras.append("Plus Tactica: ").append(plusTact).append(";");           
        
        logger.log(INFO, "Completar valoraciones OK");

        
    }
    
    private JugadorFutbol8 porteroEqLocal(){
        
        var ali = this.getAlineacionEqLocal();
        
        return ali.getPortero();
        
    }
    
    private JugadorFutbol8 porteroEqVisitante(){
        
        var ali = this.getAlineacionEqVisitante();
        
        return ali.getPortero();
        
    }
    
    
       

    public EstadisticaPartidoFutbol8 getEstadistica() {
        return estadistica;
    }

    public void setEstadistica(EstadisticaPartidoFutbol8 estadistica) {
        this.estadistica = estadistica;
    }

    public ArrayList<JugadorFutbol8> getGoleadores() {
        return goleadores;
    }
  
    
    private EsfuerzoFutbol8 esfuerzoJugador(JugadorFutbol8 jug){
        
        EsfuerzoFutbol8 esf = null;
        
        if (jug.getEquipo().equals(this.getEqLocal()))
            return this.getAlineacionEqLocal().getEsfuerzo();
        else if (jug.getEquipo().equals(this.getEqVisitante()))
            return this.getAlineacionEqVisitante().getEsfuerzo();
        
        return esf;
        
    }

    private void sumarCansancio(JugadorFutbol8 jug, int extra){
        
        var esf = 5;
        
        if (jug == null) return;
        
        var esfJug = esfuerzoJugador(jug);
        
        if (jug.getPosicion().equals(Portero))
            esf = 2;        
        else if (esfJug.equals(Maximo))
            esf = 10;
        else if (esfJug.equals(Relajado))
            esf = 2; 
        
        jug.setCansancioPartido(jug.getCansancioPartido() + esf * extra);
        
    }
    
     private void sumarCansancio(JugadorFutbol8 jug){
        
         sumarCansancio(jug, 1);
    }
    
    private String asignarCansancioFinalJugadores (EquipoFutbol8 eq, 
            boolean esSimul){
        
        logger.log(INFO, "Asignar cansancio final");
        
        var cansancioAcum = 0;
        // los que juegan
        for (var jug : eq.getAlineacion().jugadores()) {
            var cansancio = jug.getCansancioPartido();                
            cansancio = cansancio / 10;
            if (jug.getEstadoFisico() - cansancio < 10)
                cansancio = jug.getEstadoFisico() - 10; 
            if (!esSimul){
                jug.setEstadoFisico(jug.getEstadoFisico() - cansancio);
                jug.setPartidosJugados(jug.getPartidosJugados() + 1);
            }
            cansancioAcum = cansancioAcum + cansancio;
            
        }
        for (var jug : eq.getJugadores()) {
            if (!esSimul && !jug.isJuegaJornada()){
                jug.setEstadoFisico(jug.getEstadoFisico() + 5 + (100 - jug.getEstadoFisico()) / 5);            
                    if (jug.getEstadoFisico() > 100)
                        jug.setEstadoFisico(100);
            }
        }    
        var txt = "Cansancio Global ".concat(valueOf(cansancioAcum));
        
        logger.log(INFO, "Asignar cansancio final OK");        
        
        return txt;
        
    }
    
    private void sacarTarjetaAmarilla(JugadorFutbol8 jug){
        
       
        if (jug.isTarjetaRoja()) return;
        var eq = jug.getEquipo();
        var roja = jug.isTarjetaAmarilla();
        
        if (roja)
            sacarTarjetaRoja(jug);
        else{
            eq.tarjetas.append(jug.getNombre()).append(";");
            if (!this.isSimulacion())
                jug.setTarjetaAmarilla(true);
        }

        
    }
    
    private void sacarTarjetaRoja(JugadorFutbol8 jug){
                
        if (jug.isTarjetaRoja()) return;
        
        var eq = jug.getEquipo();
        eq.tarjetas.append(jug.getNombre()).append("(Roja);");
      
        if (!this.isSimulacion()) {
            jug.setTarjetaRoja(true);
            jug.setTarjetaAmarilla(false);
        }
            
    }
    
    private void quitarTarjetas() {
        
        if (this.isSimulacion()) return;
        
        logger.log(INFO, "Quitar tajetas");
        
        var jugs = new ArrayList<JugadorFutbol8>();
        jugs.addAll(this.getEqLocal().getJugadores());
        jugs.addAll(this.getEqVisitante().getJugadores());
        
        for (var jug : jugs)
            if (jug.isTarjetaRoja()) jug.setTarjetaRoja(false);
        
        logger.log(INFO, "Quitar tarjetas OK");
                
    }
    
    private void lesionar(JugadorFutbol8 jug){
        
        logger.log(INFO, "Lesionar");
        
        if (jug.getJornadasLesion() > 0) return;
        
        var eq = jug.getEquipo();
        var maxJornadasLesion = 6;
        
        eq.lesionados.append(jug.getNombre()).append(";");
                
        var num = valorAleatorio(1, maxJornadasLesion);  
        
         if (!this.isSimulacion()) 
             // Sumamos 1 porque al finalizar la jornada se va a 
             // restar una jornada
             jug.setJornadasLesion(num + 1);
        
        logger.log(INFO, "Lesionar OK");
        
    }
    
    
    public EquipoFutbol8 hacerTandaPenalties(){
        
        // Deviuelve el equipo que gana la tanda de penalties
        
        logger.log(INFO, "Hacer tanda Penalties");
        
        escribir("Y vamos a la tanda de penalties");
        
        EquipoFutbol8 eqGanador = null;
        
        var numPenalties = 5;
        var jugsLocal = aliLocal.delanteros();
        jugsLocal.addAll(aliLocal.centrocampistas());
        jugsLocal.addAll(aliLocal.defensas());
        var local = 0;
        var jugsVisit = aliVisit.delanteros();
        jugsVisit.addAll(aliVisit.centrocampistas());
        jugsVisit.addAll(aliVisit.defensas());
        var visit = 0;

        boolean isGol;
        for (var i = 1; i <= numPenalties; i++){
            if (jugsLocal.size() >= i){
                var jugEqLocal = jugsLocal.get(i - 1);
                isGol = hacerTiro(jugEqLocal, porteroEqVisitante(), Penalti);  
                if (isGol) local++;
            }
            if (jugsVisit.size() >= i){
                var jugEqVisit = jugsVisit.get(i - 1);
                isGol = hacerTiro(jugEqVisit, porteroEqLocal(), Penalti);  
                if (isGol) visit++;
            }       
        }
        
        // Si hay empate desempatamos
        var i = 6;
        while (local == visit){
            if (jugsLocal.size() >= i){
                var jugEqLocal = jugsLocal.get(i - 1);
                isGol = hacerTiro(jugEqLocal, porteroEqVisitante(), Penalti);  
                if (isGol) local++;
            }
            if (jugsVisit.size() >= i){
                var jugEqVisit = jugsVisit.get(i - 1);
                isGol = hacerTiro(jugEqVisit, porteroEqLocal(), Penalti);
                if (isGol) visit++;
            }   
            i++;
            if (i == 7) i = 1;
        }
        
        this.setGolesLocal(eqLocal.goles);
        this.setGolesVisitante(eqVisit.goles);
        this.getEstadistica().setGoleadoresLocal(eqLocal.goleadores.toString());
        this.getEstadistica().setGoleadoresVisitante(eqVisit.goleadores.toString());
        
        if (eqLocal.goles > eqVisit.goles)
            eqGanador = (EquipoFutbol8) this.getEqLocal();
        else if (eqLocal.goles < eqVisit.goles)
            eqGanador = (EquipoFutbol8) this.getEqVisitante();
        
        escribir("Finalmente el " + eqGanador.getNombre() + " gana la tanda de penalties por " +
                 local + " - " + visit);
        
        logger.log(INFO, "Hacer tanda Penalties OK");
        
        return eqGanador;
        
        
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isSimulacion() {
        return simulacion;
    }

    public void setIsSimulacion(boolean simulacion) {
        this.simulacion = simulacion;
    }

    private boolean verificarNumJugadores() {
        
        var ok = true;
        var min = NUMERO_MINIMO_JUGADORES;
      
        if (aliLocal.jugadores().size() < min && aliVisit.jugadores().size() > min - 1){
            this.getEstadistica().setGolesLocal(0);
            this.getEstadistica().setGolesVisitante(3); 
            this.getEstadistica().setExtrasEqLocal("No disputa partido por no tener suficientes jugadores");
            this.getEstadistica().setExtrasEqVisitante("");
            ok = false;
        }
        else if (aliLocal.jugadores().size() > min - 1 && aliVisit.jugadores().size() < min){  
            this.getEstadistica().setGolesLocal(3);
            this.getEstadistica().setGolesVisitante(0); 
            this.getEstadistica().setExtrasEqLocal("");
            this.getEstadistica().setExtrasEqVisitante("No disputa partido por no tener suficientes jugadores");
            ok = false;
        }
        else if (aliLocal.jugadores().size() < min && aliVisit.jugadores().size() < min){  
            this.getEstadistica().setGolesLocal(0);
            this.getEstadistica().setGolesVisitante(0); 
            this.getEstadistica().setExtrasEqLocal("No disputa partido por no tener suficientes jugadores");
            this.getEstadistica().setExtrasEqVisitante("No disputa partido por no tener suficientes jugadores");
            ok = false;
        }
        
        if (!ok){
            this.getEstadistica().setAlineacionLocal(aliLocal.alineacionParaEstadistica());
            this.getEstadistica().setAlineacionVisitante(aliVisit.alineacionParaEstadistica());
            this.setGolesLocal(this.getEstadistica().getGolesLocal());
            this.setGolesVisitante(this.getEstadistica().getGolesVisitante());
            this.getEstadistica().setMoralLocal(this.getEqLocal().getMoral());
            this.getEstadistica().setMoralVisitante(this.getEqVisitante().getMoral());
            this.getEstadistica().setGoleadoresLocal("");
            this.getEstadistica().setGoleadoresVisitante("");
            this.getEstadistica().setCornersLocal(0);
            this.getEstadistica().setCornersVisitante(0);
            this.getEstadistica().setFaltasDirectasLocal(0);
            this.getEstadistica().setFaltasDirectasVisitante(0);
            this.getEstadistica().setPenaltiesLocal(0);
            this.getEstadistica().setPenaltiesVisitante(0);
            this.getEstadistica().setTarjetasLocal("");
            this.getEstadistica().setTarjetasVisitante("");
            this.getEstadistica().setLesionadosLocal("");
            this.getEstadistica().setLesionadosVisitante("");
        }        
        
        return ok;
        
    }

   
    private void simularTiros(ArrayList<JugadorFutbol8> jugadores, JugadorFutbol8 portero){        
        
        TiroPuertaFutbol8 tiro;
        
        for (var jug : jugadores) {
            jug.setTiroArea(0);
            jug.setTiroSolo(0);
            jug.setTiroLejano(0);
            jug.setTiroLejanoSolo(0);
            jug.setPenalti(0);
            jug.setCabeza(0);
            jug.setFalta(0);
            jug.setParadasTiroArea(0);
            jug.setParadasTiroSolo(0);
            jug.setParadasTiroLejano(0);
            jug.setParadasTiroLejanoSolo(0);
            jug.setParadasPenalti(0);
            jug.setParadasCabeza(0);
            jug.setParadasFalta(0);
        }
        for (var clase : values()){
            Field campoTiro, campoParada = null;
            var chutesPorteria = 0;
            var paradas = 0;
            try {
                if (portero != null){
                    campoParada = portero.getClass().getDeclaredField("paradas" + clase.name());
                    campoParada.setAccessible(true);
                }
                for (var jug : jugadores) {
                    campoTiro = jug.getClass().getDeclaredField(clase.name());
                    campoTiro.setAccessible(true);                    
                    var goles = 0;
                    for (var i = 0; i < 100; i++){
                        tiro = new TiroPuertaFutbol8(jug, portero, clase);       
                        tiro.hacerTiro(); 
                        if (!tiro.isEsFuera()){
                            if (tiro.isEsGol())
                                goles++;
                            else 
                                paradas++;
                            chutesPorteria++;                            
                        }
                    }                    
                    campoTiro.set(jug, goles);    
                }
                if (portero != null){
                    if (chutesPorteria > 0)
                        campoParada.set(portero, paradas * 100 / chutesPorteria);
                    else
                        campoParada.set(portero, 0);
                }
            }
            catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {}               
        }
                
    }
    
    
    public void simulacionTiros(){
        
        inicializarDatos();
        simularTiros(this.getAlineacionEqLocal().jugadoresCampo(), this.getAlineacionEqVisitante().getPortero());
        simularTiros(this.getAlineacionEqVisitante().jugadoresCampo(), this.getAlineacionEqLocal().getPortero());
  
    }

    private void posibleLesionPortero(JugadorFutbol8 portero) {
        
        var posibilidadLesion = 500;
        var isLesion = obtener(posibilidadLesion);
        
        if (isLesion){
            lesionar(portero);
            escribir(portero.getNombre() + mapeo().get(lesionPortero));
            
        }
        
    }

   

}
