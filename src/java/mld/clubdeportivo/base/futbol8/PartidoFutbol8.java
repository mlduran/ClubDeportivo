package mld.clubdeportivo.base.futbol8;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.CronicaStrings;
import mld.clubdeportivo.base.Partido;
import mld.clubdeportivo.utilidades.Calculos;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public final class PartidoFutbol8 extends Partido{
    
    private static Logger logger = LogManager.getLogger(PartidoFutbol8.class);
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

    ArrayList<CronicaFutbol8> cronica = new ArrayList<CronicaFutbol8>();
    
    public ArrayList<CronicaFutbol8> getCronica(){
        return this.cronica;
    }
    
    public void setCronica(ArrayList<CronicaFutbol8> cronica){
        this.cronica = cronica;
    }
    
    private EnumMap<CronicaStrings, String> mapeo(){
        
        EnumMap<CronicaStrings, String> mapeo = new EnumMap<CronicaStrings, String>(CronicaStrings.class);
        mapeo.put(CronicaStrings.primerAtaque, " inicia el primer ataque");
        mapeo.put(CronicaStrings.tienePelota, " tiene la pelota");
        mapeo.put(CronicaStrings.cubre, " va a cubrir");
        mapeo.put(CronicaStrings.pasa, " pasa la pelota a ");
        mapeo.put(CronicaStrings.pasaAtras, " pasa hacia atras la pelota a ");
        mapeo.put(CronicaStrings.quita, " le quita la pelota a ");
        mapeo.put(CronicaStrings.regatea, " regatea a ");
        mapeo.put(CronicaStrings.falta, " hace una falta a ");
        mapeo.put(CronicaStrings.derriba, " derriba claramente a ");
        mapeo.put(CronicaStrings.paseLargo, " hace un pase largo a ");
        mapeo.put(CronicaStrings.pierde, " no sabe que hacer con la pelota y la pierde");
        mapeo.put(CronicaStrings.pasePerdido, " hace un pase malo que se pierde por la banda");
        mapeo.put(CronicaStrings.paseLargoPerdido, " hace un pase largo que se pierde por la banda");
        mapeo.put(CronicaStrings.iniciaAtaque, " inicia el ataque");
        mapeo.put(CronicaStrings.finPartido, "El arbitro pita el final del partido");
        mapeo.put(CronicaStrings.tarjeta, "La entrada ha sido muy dura y el arbitro saca la tarjeta amarilla a ");
        mapeo.put(CronicaStrings.penalti, " le derriba dentro del area y el arbitro pita penalti y saca la tarjeta amarilla");
        mapeo.put(CronicaStrings.lesion, " parece haber sufrido un duro golpe, y tiene que ser atendido"); 
        mapeo.put(CronicaStrings.lesionPortero, " parece haber sufrido un hecho daño, y tiene que ser atendido");
        mapeo.put(CronicaStrings.quedaSolo, " se queda solo");
        mapeo.put(CronicaStrings.porteroSale, " sale a tapar a ");
        mapeo.put(CronicaStrings.cortaPase, " pone la pierna y le corta el balon a ");
        mapeo.put(CronicaStrings.chuta, " se coloca el balon y chuta ");
        mapeo.put(CronicaStrings.tiroDespejado, " despeja el balon ");
        mapeo.put(CronicaStrings.tiroRebotado, " la pelota rebota en ");
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
        ArrayList<JugadorFutbol8> jugs = eq.getAlineacion().jugadores();
        for (JugadorFutbol8 jug : jugs) {
            jug.setCansancioPartido(0);
        }
        
    }
    
    private void sincronizarJugadores(EquipoFutbol8 eq){
        // Sincroniza los jugadores de alineacion y equipo
        // ya que vienen de sitios diferentes
        
        ArrayList<JugadorFutbol8> lista = eq.getAlineacion().jugadores();
        
        for (JugadorFutbol8 jug : eq.getJugadores()) {
            boolean anyadir = true;
            for (JugadorFutbol8 jugAli : eq.getAlineacion().jugadores()) 
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
        
        boolean juegaCasa = !this.getJornada().getDescripcion().contains("Final");
        eqLocal.setJuegaEnCasa(juegaCasa); 
        //Debug
        //eqLocal.setJuegaEnCasa(false);  
        
        if (juegaCasa)            
            calcularAsistenciaPublico(); 
                
        completarValoracionesJugadores(eqLocal);                
        completarValoracionesJugadores(eqVisit);   
        
    }
    
    
    private void desarrolloPartido(){
        
        int segundosPartido = 60 * MINUTOS_PARTIDO;     
        
        EquipoFutbol8 eqAtq;        
        EquipoFutbol8 eqDef; 
        
        if (eqLocal.isJuegaEnCasa() || Calculos.obtener(2)){
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
        escribir(eqAtq.getNombre() + mapeo().get(CronicaStrings.primerAtaque));
        int ralentizar;
        while(segundosPartido > segundo){
            if (eqAtq.estrategia().equals(EstrategiaFutbol8.Ralentizar))
                ralentizar = 2;
            else
                ralentizar = 1;
            robo = false;            
            pase= false;
            jugAtq = jugadorDeAtaque(eqAtq, jugAtq);
            char pos = jugAtq.getCuadrante().charAt(1);
            // Si venimos de un regate no ponemos jugador de defensa
            if (regate){
                jugDef = null;
                regate = false;
            }
            else
                jugDef = jugadorQueDefiende(eqDef, jugAtq);
            if (jugDef != null){                
            
                // El jugador no esta solo, alguien le defiende
                escribir(jugDef.getNombre() + mapeo().get(CronicaStrings.cubre), jugAtq.getCuadrante(), eqAtq);
                
                // TIRO :
                // si estamos en delantera y la estrategia es chutar 
                // si estamos en delantera y sale un random de 1/4
                if (pos == '3' && 
                        (eqAtq.estrategia().equals(EstrategiaFutbol8.Tirar) ||
                        Calculos.obtener(4))
                        ){
                    JugadorFutbol8 newJug = hacerPreparacionTiro(jugAtq, jugDef);
                    if (newJug == null){
                        // se sigue con el tiro
                        hacerTiro(jugAtq, eqDef.portero(), ClaseTiro.TiroArea);
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
                else if (eqAtq.estrategia().equals(EstrategiaFutbol8.Contraataque) ||
                        (pos == '1' && Calculos.obtener(5)) ||
                        (pos == '2' && Calculos.obtener(3)) ||
                        (pos == '3' && Calculos.obtener(2)) 
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
                if (pos == '3' || eqAtq.estrategia().equals(EstrategiaFutbol8.Tirar)){
                    // tira
                    if (pos == '1' || pos == '2') 
                        hacerTiro(jugAtq, eqDef.portero(), ClaseTiro.TiroLejanoSolo); 
                    else{                        
                        escribir(jugAtq.getNombre() + mapeo().get(CronicaStrings.quedaSolo), jugAtq.getCuadrante(), eqAtq);
                        boolean porteroSale = eqDef.portero() != null && Calculos.obtener(4);
                        if (porteroSale)// El portero sale y puede hacer penalti
                            salidaPortero(jugAtq, eqDef.portero());                        
                        else                            
                            hacerTiro(jugAtq, eqDef.portero(), ClaseTiro.TiroSolo);                        
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
                        escribir(eqAtq.getNombre() + mapeo().get(CronicaStrings.iniciaAtaque));
                    else 
                        escribir(eqAtq.getNombre() + mapeo().get(CronicaStrings.iniciaAtaque), jugAtq.getCuadrante(), eqAtq);                        
                }
            }
           
            
        }
        
        escribir(""); 
        escribir( mapeo().get(CronicaStrings.finPartido)); 
        
    }
 
    public EstadisticaPartidoFutbol8 jugarPartido(){
        
        logger.debug("Inicio Partido");
        
        inicializarDatos();
      
        this.getEstadistica().setAlineacionLocal(aliLocal.alineacionParaEstadistica());
        this.getEstadistica().setAlineacionVisitante(aliVisit.alineacionParaEstadistica());
        
        this.getEstadistica().setGolesLocal(0);
        this.getEstadistica().setGolesVisitante(0);
        
        // si no hay un minimo de 4 jugadores no se disputa el 
        // partido
        boolean numJugOK = verificarNumJugadores();
        
        if (!this.isSimulacion()) 
            quitarTarjetas();
        
        goleadores = new ArrayList<JugadorFutbol8>(); 
        
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
        
        logger.debug("Fin Partido");
        
        return  this.getEstadistica();
 
    }
    
    
    /* ACCIONES DEL PARTIDO */
    
    private JugadorFutbol8 jugadorDeAtaque(EquipoFutbol8 eq, JugadorFutbol8 jugAtq){
        
        if (jugAtq == null){
            jugAtq = jugadorInicioJugada(eq);
            jugAtq.getEquipo().posesion++;
            escribir(jugAtq.getNombre() + mapeo().get(CronicaStrings.tienePelota), jugAtq.getCuadrante(), eq);
        }
        
        return jugAtq;        
    }
    
    private boolean faltaAlHacerRegate(JugadorFutbol8 jugAtq, JugadorFutbol8 jugDef){
        // Si el jugador de defensa le quita la pelota miramos 
        // si ha habido alguna falta
        
        boolean esFalta = false;
        boolean esLesion = false;
        
        int posFalta = 7;
        int posTarjeta = 2;
        int posPenalti = 20;
        int posLesion = 5;
        
        if (jugDef.getEstrategia().equals(EstrategiaFutbol8.Agresivos)){
            posFalta = 3;
            posTarjeta = 1;
            posPenalti = 12;
            posLesion = 2;            
        }
        
        EquipoFutbol8 eqDef = jugDef.getEquipo();
         
        if (jugAtq.getCuadrante().charAt(1) == '3' &&
                Calculos.obtener(posPenalti)){
            // Ha sido penalti
            escribir(jugDef.getNombre() + mapeo().get(CronicaStrings.falta) + jugAtq.getNombre()); 
            sacarTarjetaAmarilla(jugDef);
            escribir(jugDef.getNombre() + mapeo().get(CronicaStrings.penalti));
            lanzarPenalti(jugAtq.getEquipo(), eqDef.portero());
        }              
        else if (Calculos.obtener(posFalta)){
            // Ha sido falta
            escribir(jugDef.getNombre() + mapeo().get(CronicaStrings.falta) + jugAtq.getNombre()); 
            if (Calculos.obtener(posLesion)){
                lesionar(jugAtq);
                escribir(jugAtq.getNombre() + mapeo().get(CronicaStrings.lesion));
                esLesion = true;
            }
            if (esLesion || Calculos.obtener(posTarjeta)){
                // ha sido tarjeta
                sacarTarjetaAmarilla(jugDef);
                escribir(mapeo().get(CronicaStrings.tarjeta) + jugDef.getNombre());
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
        boolean roba = Calculos.obtenerResultado(jugDef.getRobo(), jugAtq.getRegate());
        if (roba){
            escribir(jugDef.getRoboInf() + mapeo().get(CronicaStrings.quita) + jugAtq.getRegateInf(), jugDef.getCuadrante(), jugDef.getEquipo());  
            jug = jugDef;
        }
        else{
            escribir(jugAtq.getRegateInf() +  mapeo().get(CronicaStrings.regatea) + jugDef.getRoboInf(), jugAtq.getCuadrante(), jugAtq.getEquipo());            
            jugAtq.getEquipo().posesion++;
        }

        return jug;
    }
    
    private void salidaPortero(JugadorFutbol8 jugAtq, JugadorFutbol8 portero){
        // Realiza las acciones si el portero sale a cubrir
        
        escribir(portero.getValoracionInf() +  mapeo().get(CronicaStrings.porteroSale) + jugAtq.getRegateInf()); 
        
        sumarCansancio(portero);
        
        boolean atrapa = Calculos.obtenerResultado(portero.getValoracionPartido(), jugAtq.getRegate());
        
        if (atrapa){
            // Atrapa el balon
            escribir("y atrapa el balon"); 
        }        
        else{
            int op = Calculos.valorAleatorio(3);
            // pueder ser falta o penalti o tiro
            if (op == 0){
                sacarTarjetaAmarilla(portero);            
                escribir(portero.getNombre() + mapeo().get(CronicaStrings.penalti));
                lanzarPenalti(jugAtq.getEquipo(), portero);  
            }
            else if (op == 1){
                sacarTarjetaAmarilla(portero);            
                escribir(portero.getNombre() + mapeo().get(CronicaStrings.falta));
                lanzarFaltaDirecta(jugAtq.getEquipo(), portero);  
            }
            else{
                hacerTiro(jugAtq, portero, ClaseTiro.TiroSolo); 
            }
        }
    }
    
    private JugadorFutbol8 hacerPase(JugadorFutbol8 jugAtq, JugadorFutbol8 jugDef){
 
        JugadorFutbol8 jugTmp;
        boolean paseOk = true;
        int pase = 5;
        int paseLargo = 20;
        EquipoFutbol8 eq = jugAtq.getEquipo();
        sumarCansancio(jugAtq);
        
        // si el jugador no esta solo calculamos posibilidad
        // de que el defensor corte el pase
        if (jugDef != null && Calculos.obtenerResultado(jugDef.getRobo(), jugAtq.getPase())){
            escribir(jugDef.getRoboInf() + mapeo().get(CronicaStrings.cortaPase) + jugAtq.getPaseInf(), jugDef.getCuadrante(), jugDef.getEquipo()); 
            return jugDef;    
        }
        
        jugTmp = jugadorReceptorCercano(jugAtq);
        if (jugTmp != null)
            if (Calculos.obtenerResultado(jugTmp.getPase(), pase))
                escribir(jugAtq.getNombre() + mapeo().get(CronicaStrings.pasa) + jugTmp.getNombre(), jugTmp.getCuadrante(), eq);
            else{
                escribir(jugAtq.getPaseInf() + mapeo().get(CronicaStrings.pasePerdido));  
                paseOk = false;
            }
        else{
            jugTmp = jugadorReceptorLejano(jugAtq);
            if (jugTmp != null){
                if (Calculos.obtenerResultado(jugTmp.getPase(), paseLargo))
                    escribir(jugAtq.getPaseInf() + mapeo().get(CronicaStrings.paseLargo) + jugTmp.getNombre(), jugTmp.getCuadrante(), eq);
                else{
                    escribir(jugAtq.getPaseInf() + mapeo().get(CronicaStrings.paseLargoPerdido));  
                    paseOk = false;
                }      
            }else{
                jugTmp = jugadorReceptorMismaLinea(jugAtq);
                if (jugTmp != null)
                    escribir(jugAtq.getNombre() + mapeo().get(CronicaStrings.pasa) + jugTmp.getNombre(), jugTmp.getCuadrante(), eq);
                else {
                    // no puede pasar a nadie
                    // Hay un 50% de posibilidades de pasar hacia atras o perder el balon
                    if (Calculos.obtener(2)){
                        jugTmp = jugadorReceptorTrasero(jugAtq);
                    }
                    if (jugTmp != null)
                        escribir(jugAtq.getNombre() + mapeo().get(CronicaStrings.pasaAtras) + jugTmp.getNombre(), jugTmp.getCuadrante(), eq);
                    else{
                        escribir(jugAtq.getNombre() + mapeo().get(CronicaStrings.pierde));
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
            if (Calculos.obtenerResultado(jugDef.getRobo() / 4, jugAtq.getTiro())){
                // El defensa despeja el tiro
                escribir(jugAtq.getTiroInf() + mapeo().get(CronicaStrings.chuta));
                sumarCansancio(jugDef);
                sumarCansancio(jugAtq);
                escribir(jugDef.getRoboInf() + mapeo().get(CronicaStrings.tiroDespejado));
                // sale aleatoriamente
                newJug = jugadorReceptorAleatorio();
                escribir(newJug.getNombre() + " se hace con el control del balon", newJug.getCuadrante(), newJug.getEquipo());
                
            }
            else if (Calculos.obtener(4)){
                // La pelota rebota en el defensa
                escribir(jugAtq.getTiroInf() + mapeo().get(CronicaStrings.chuta));
                sumarCansancio(jugDef);
                sumarCansancio(jugAtq);
                escribir(mapeo().get(CronicaStrings.tiroRebotado) + jugDef.getNombre());
                if (Calculos.obtener(2)){
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
        
        boolean isGol = false;
        TiroPuertaFutbol8 tiro;
        EquipoFutbol8 eq = jugAtq.getEquipo();
        String txt = "";
        sumarCansancio(jugAtq);
        
        if (clase.equals(ClaseTiro.TiroArea)){
            
            txt = " pisa el area y chuta";
            eq.tirArea++;
        }
        else if (clase.equals(ClaseTiro.TiroSolo)){
            txt = " se mete en el area y chuta";
            eq.tirArea++;
        }
        else if (clase.equals(ClaseTiro.TiroLejano)){
            txt = " no se lo piensa y tira desde lejos";
            eq.tirLej++;
        }
        else if (clase.equals(ClaseTiro.TiroLejanoSolo)){
            txt = " se queda solo y tira desde lejos";  
            eq.tirLej++;
        }
        else if (clase.equals(ClaseTiro.Falta)){
            txt = " tira la falta";  
            eq.faltasDir++;
        }
        else if (clase.equals(ClaseTiro.Penalti)){
            txt = " chuta desde el punto de penalti";  
            eq.penalties++;
        }
        else if (clase.equals(ClaseTiro.Cabeza)){
            txt = " remata de cabeza";  
            eq.corners++;
        }
        
        escribir(jugAtq.getTiroInf() + txt, jugAtq.getCuadrante(), jugAtq.getEquipo());
        tiro = new TiroPuertaFutbol8(jugAtq, portero, clase);
        String valInfPortero = "a puerta vacia";
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
        
        EquipoFutbol8 eq = jug.getEquipo();
        
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
        
        logger.debug("Calcular Posesion");

        int posTotal = eqLocal.posesion + eqVisit.posesion;
        eqLocal.posesion = eqLocal.posesion * 100 / posTotal;
        eqVisit.posesion = eqVisit.posesion * 100 / posTotal;
        
        while (100 - eqLocal.posesion - eqVisit.posesion > 0)
            eqLocal.posesion++;
        
        logger.debug("Calcular Posesion OK");
       
    }
    
    
    private JugadorFutbol8 jugadorInicioJugada(EquipoFutbol8 eq){
        
        AlineacionFutbol8 ali = eq.getAlineacion();
        JugadorFutbol8 jug = null;
        
         if (ali.defensas().size() > 0){
             int numJug = Calculos.valorAleatorio(ali.defensas().size());                        
             jug = ali.defensas().get(numJug); 
         }
         else if (ali.centrocampistas().size() > 0){
             int numJug = Calculos.valorAleatorio(ali.centrocampistas().size());                        
             jug = ali.centrocampistas().get(numJug); 
         }
         else if (ali.delanteros().size() > 0){
             int numJug = Calculos.valorAleatorio(ali.delanteros().size());                        
             jug = ali.delanteros().get(numJug); 
         }
  
         return jug;    
        
    }
    
     private JugadorFutbol8 jugadorReceptorAleatorio(){
        
        
        ArrayList<JugadorFutbol8> jugs = new ArrayList<JugadorFutbol8>();
        
        jugs.addAll(aliLocal.jugadoresCampo());
        jugs.addAll(aliVisit.jugadoresCampo());
        
        int x = Calculos.valorAleatorio(jugs.size());
        JugadorFutbol8 jug = jugs.get(x);        
         
        return jug;    
        
    }
    
    private JugadorFutbol8 jugadorQueDefiende(EquipoFutbol8 eq, JugadorFutbol8 jugAtq){
        // 1 linea defensiva
        // 2 Centrocampo
        // 3 Delantera
        
        AlineacionFutbol8 ali = eq.getAlineacion();
        JugadorFutbol8 jug;
        int pos = jugAtq.getCuadrante().charAt(1);
        boolean presion = ali.getEstrategia().equals(EstrategiaFutbol8.Presionar);
        int extraCansacio = 0;
        int capacidadRobo = 100;
                
        jug = ali.jugadorContrario(jugAtq.getCuadrante());
        
        // Si la estrategia es Estatica el defensor no se desplaza
        if (jug == null && ali.getEstrategia().equals(EstrategiaFutbol8.Estaticos))
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
        
        AlineacionFutbol8 ali = jugPase.getEquipo().getAlineacion();
        JugadorFutbol8 jug = ali.jugadorParaPase(jugPase.getCuadrante());
         
         return jug;    
        
    }
    
        
    private JugadorFutbol8 jugadorReceptorMismaLinea(JugadorFutbol8 jugPase){
        
        AlineacionFutbol8 ali = jugPase.getEquipo().getAlineacion();
        JugadorFutbol8 jug = ali.jugadorParaPaseMismaLinea(jugPase.getCuadrante());
         
         return jug;    
        
    }
    
     private JugadorFutbol8 jugadorReceptorLejano(JugadorFutbol8 jugPase){
        
         // Si el jugador esta en delantera no aplica
         if (jugPase.getCuadrante().charAt(1) == '3') return null;
         
         AlineacionFutbol8 ali = jugPase.getEquipo().getAlineacion();
         JugadorFutbol8 jug = ali.jugadorParaPaseLargo(jugPase.getCuadrante());
         
         return jug;    
        
    }
     
     private JugadorFutbol8 jugadorReceptorTrasero(JugadorFutbol8 jugPase){
        
        // Si el jugador esta en delantera no aplica
        if (jugPase.getCuadrante().charAt(1) == '1') return null;
        
        AlineacionFutbol8 ali = jugPase.getEquipo().getAlineacion();
        JugadorFutbol8 jug = ali.jugadorParaPaseAtras(jugPase.getCuadrante());
         
         return jug;    
        
    }
     
    
           
    private void disputarCorner(EquipoFutbol8 eqAtq, EquipoFutbol8 eqDef){
        
        logger.debug("Calcular Corners");
        
         
        AlineacionFutbol8 aliAtq = eqAtq.getAlineacion();
        AlineacionFutbol8 aliDef = eqDef.getAlineacion();
        
        int numJug;

        if (aliAtq.jugadoresCampo().size() > 0){
                    
            numJug = Calculos.valorAleatorio(aliAtq.jugadoresCampo().size());                        
            JugadorFutbol8 jugAtq = aliAtq.jugadoresCampo().get(numJug);                  
            numJug = Calculos.valorAleatorio(aliDef.jugadoresCampo().size());
            JugadorFutbol8 jugDef = aliDef.jugadoresCampo().get(numJug);
            escribir("Se lanza el corner y " + jugAtq.getRegateInf() + " y " + jugDef.getRoboInf() + " saltan a por el balon");
            if (Calculos.obtenerResultado(jugAtq.getRegate(), jugDef.getRobo())){
                sumarCansancio(jugDef);
                sumarCansancio(jugAtq);
                hacerTiro(jugAtq, aliDef.getPortero(), ClaseTiro.Cabeza);
            }
            else
                escribir("El " + eqDef.getNombre() + " recupera el balon");
                    
        }     
        
        logger.debug("Calcular Corners OK");
       
    }
    
    
    
    private void lanzarFaltaDirecta(EquipoFutbol8 eqAtq, JugadorFutbol8 portero){

        logger.debug("Calcular Faltas Directas");
        
        int numJug;
        
        
        AlineacionFutbol8 aliAtq = eqAtq.getAlineacion();
        
        ArrayList<JugadorFutbol8> jugs = new ArrayList<JugadorFutbol8>();
        
        jugs.addAll(aliAtq.centrocampistas());
        jugs.addAll(aliAtq.delanteros());
        
        if (jugs.size() > 0){
            
            // Va a puerta o no
            if (Calculos.obtener(3)) return;
            
            numJug = Calculos.valorAleatorio(jugs.size());                        
            JugadorFutbol8 jugAtq = jugs.get(numJug);                  
            hacerTiro(jugAtq, portero, ClaseTiro.Falta);
        }    
        
        logger.debug("Calcular Faltas Directas OK");
       
    }
    
    private void lanzarPenalti(EquipoFutbol8 eqAtq, JugadorFutbol8 portero){

        logger.debug("lanzar Penalti");
        
        int numJug;
        
        AlineacionFutbol8 aliAtq = eqAtq.getAlineacion();
        
        ArrayList<JugadorFutbol8> jugs = new ArrayList<JugadorFutbol8>();
        
        if (aliAtq.delanteros().size() > 0) jugs = aliAtq.delanteros();
        else if (aliAtq.centrocampistas().size() > 0) jugs = aliAtq.delanteros();
                
        if (jugs.size() > 0){
            numJug = Calculos.valorAleatorio(jugs.size());                        
            JugadorFutbol8 jugAtq = jugs.get(numJug);                  
            hacerTiro(jugAtq, portero, ClaseTiro.Penalti);
        }    
        
       logger.debug("lanzar Penalti OK");
       
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
         for (AlineacionFutbol8 unaAli : this.getAlineaciones()) {
             if(unaAli.getEquipo().equals(this.getEqLocal()))
                 ali = unaAli;
         }
         return ali;
    }
     
     public AlineacionFutbol8 getAlineacionEqVisitante() {
        AlineacionFutbol8 ali = null;
         for (AlineacionFutbol8 unaAli : this.getAlineaciones()) {
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

        logger.debug("Asignar Puntos");
        
        EquipoFutbol8 eqL = (EquipoFutbol8) this.getEqLocal();
        PuntuacionFutbol8 puntsL = eqL.getPuntuacion();
        EquipoFutbol8 eqV = (EquipoFutbol8) this.getEqVisitante();
        PuntuacionFutbol8 puntsV = eqV.getPuntuacion();
        
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
        
        logger.debug("Asignar Puntos OK");

    }
    
    public void asignarMoral() {
        
        if (this.isSimulacion()) return;
        
        logger.debug("Asignar Moral");
        
        EquipoFutbol8 eqL = (EquipoFutbol8) this.getEqLocal();
        EquipoFutbol8 eqV = (EquipoFutbol8) this.getEqVisitante();
        
        
        if (this.getGanador() == null && eqL.isJuegaEnCasa()){
            eqV.modificarMoral(eqV.getMoral() + 1 + (EquipoFutbol8.MORAL_INICIAL - eqV.getMoral()) / 9);
            eqL.modificarMoral(eqL.getMoral() - 1);
        }
        else if (this.getGanador() != null && this.getGanador().equals(eqV) && eqL.isJuegaEnCasa()){
            eqV.modificarMoral(eqV.getMoral() + 3 + (EquipoFutbol8.MORAL_INICIAL - eqV.getMoral()) / 3);
            eqL.modificarMoral(eqL.getMoral() - 3);
        }
        
        // ademas si los equipos han jugado en Maximo sumamos o 
        // quitamos un punto si ganan o pierden
        if (this.getGanador() != null){
            if (eqL.getAlineacion().getEsfuerzo().equals(EsfuerzoFutbol8.Maximo))
                if (this.getGanador().equals(eqL)) 
                    eqL.modificarMoral(eqL.getMoral() + 1);
                else
                    eqL.modificarMoral(eqL.getMoral() - 1);
            if (eqV.getAlineacion().getEsfuerzo().equals(EsfuerzoFutbol8.Maximo))
                if (this.getGanador().equals(eqV)) 
                    eqV.modificarMoral(eqV.getMoral() + 1);
                else
                    eqV.modificarMoral(eqV.getMoral() - 1);
        }
        
        logger.debug("Asignar Moral OK");
        
    }

    private void calcularAsistenciaPublico() {
        
        logger.debug("Calcular Publico");
        
        int interes, porcentaje;
        CompeticionFutbol8 comp = (CompeticionFutbol8) this.getJornada().getCompeticion();

        int precioEntradas = eqLocal.getPrecioEntradas();
        this.setPrecioEntradas(precioEntradas);
        
        if (comp.getClase().equals("Liga")){ 
            
            int puntLocal = eqLocal.getPuntuacion().getPuntos();
            int puntVisit = eqVisit.getPuntuacion().getPuntos();
            
            int difPuntos = Math.abs(puntLocal - puntVisit);   
            
            // si el local es el que va por detras lo compensamos
            if (puntLocal < puntVisit) 
                difPuntos = Calculos.valorAleatorio(difPuntos);         
            
            interes = 25 - difPuntos;   
            if (interes < 5 ) interes = 5;

        }
        else
            interes = 25;
        
        // Segun el precio de las entradas calculamos el minimo
        int pon = 0;
        if (precioEntradas == 5) pon = 24;
        else if (precioEntradas == 10) pon = 12;
        else if (precioEntradas == 15) pon = 9;
        else if (precioEntradas == 20) pon = 6;
        else if (precioEntradas == 25) pon = 3;
        
        /* En funcion de si el equipo local juega con exfuerzo maximo
         multiplicamos por 2 y si los 2 equipos juegan con esfuerzo maximo 
         por 4 */
        double max = 1;
        if (eqLocal.getAlineacion().getEsfuerzo().equals(EsfuerzoFutbol8.Maximo)){
            max = 1.5;
            if (eqVisit.getAlineacion().getEsfuerzo().equals(EsfuerzoFutbol8.Maximo))
                max = 4;
        }
        pon = (int) (pon * max);
          
        porcentaje = 0;
        for (int i = 1; i <= 4; i++)
            if (Calculos.obtenerResultado(pon, 9))
                porcentaje = porcentaje + interes;  
        
        // Si tubiesemos un porcentaje final de cero hacemos un random
        // sobre el interes
        if (porcentaje == 0) porcentaje = Calculos.valorAleatorio(0, interes);
            
        int asistencia = eqLocal.getCampo() * porcentaje / 100;
        
        this.setEspectadores(asistencia);
        
        logger.debug("Calcular Publico OK");
                
    }
    
    private void completarValoracionesJugadores(EquipoFutbol8 eq) {

        logger.debug("Completar valoraciones");
        
        int minJugarEnCasa = 10;
        
        AlineacionFutbol8 ali = eq.getAlineacion();
        
        int moral = eq.getMoral();
        
        // moral segun primas
        int primas = ali.getPrimas(); 
        int valMoral = 0;
        if (primas > 0)
            valMoral = Calculos.valorAleatorio(primas / 100);
               
        if (valMoral > 0){
            moral = moral + valMoral;
            eq.extras.append("Moral +").append(valMoral).append(";");
        }
        
        // factor campo
        int campo = 0;
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
        EsfuerzoFutbol8 esf = ali.getEsfuerzo();   
        
        // calculamos plus antrenador
        EntrenadorFutbol8 entr = eq.getEntrenador();
        int plusTact = entr.obtenerPlusTactica(ali.getTactica());
        if (!this.isSimulacion()){
            entr.setPlusTactica(plusTact);
           entr.setUltimaTacticaUtilizada(ali.getTactica());
        }        
        
        int valFinal;
        
        ArrayList<JugadorFutbol8> jugs = ali.jugadores();
        
        for (JugadorFutbol8 jug : jugs) {
            if (jug == null) continue;
            valFinal = jug.getValoracionMedia();
            valFinal = valFinal * moral / 100;
            
            int suplementos = campo + plusTact;
            if (esf.equals(EsfuerzoFutbol8.Maximo))
                suplementos = suplementos + 10;
            else if (esf.equals(EsfuerzoFutbol8.Relajado))
                suplementos = suplementos - 10;
            
            valFinal = valFinal + (valFinal * suplementos / 100);
            
            jug.setValoracionPartido(valFinal);
            jug.setEstrategia(ali.getEstrategia());
            // Si el jugador esta fuera de su posicion lo marcamos
            jug.setFueraPosicion(false);
            if (!jug.getPosicion().equals(PosicionJugFutbol8.Portero))
                    if (
                            (jug.getCuadrante().charAt(1) == '1' && 
                            !jug.getPosicion().equals(PosicionJugFutbol8.Defensa)) ||
                            (jug.getCuadrante().charAt(1) == '2' && 
                            !jug.getPosicion().equals(PosicionJugFutbol8.Medio)) ||
                            (jug.getCuadrante().charAt(1) == '3' && 
                            !jug.getPosicion().equals(PosicionJugFutbol8.Delantero))
                            )
                        jug.setFueraPosicion(true);           
        }   
        
        if (esf.equals(EsfuerzoFutbol8.Maximo))
                eq.extras.append("Esfuerzo +10%" + ";");
            else if (esf.equals(EsfuerzoFutbol8.Relajado))
                eq.extras.append("Esfuerzo -10%" + ";");
        
        if (plusTact > 0)
                eq.extras.append("Plus Tactica: ").append(plusTact).append(";");           
        
        logger.debug("Completar valoraciones OK");

        
    }
    
    private JugadorFutbol8 porteroEqLocal(){
        
        AlineacionFutbol8 ali = this.getAlineacionEqLocal();
        
        return ali.getPortero();
        
    }
    
    private JugadorFutbol8 porteroEqVisitante(){
        
        AlineacionFutbol8 ali = this.getAlineacionEqVisitante();
        
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
        
        int esf = 5;
        
        if (jug == null) return;
        
        EsfuerzoFutbol8 esfJug = esfuerzoJugador(jug);
        
        if (jug.getPosicion().equals(PosicionJugFutbol8.Portero))
            esf = 2;        
        else if (esfJug.equals(EsfuerzoFutbol8.Maximo))
            esf = 10;
        else if (esfJug.equals(EsfuerzoFutbol8.Relajado))
            esf = 2; 
        
        jug.setCansancioPartido(jug.getCansancioPartido() + esf * extra);
        
    }
    
     private void sumarCansancio(JugadorFutbol8 jug){
        
         sumarCansancio(jug, 1);
    }
    
    private String asignarCansancioFinalJugadores (EquipoFutbol8 eq, 
            boolean esSimul){
        
        logger.debug("Asignar cansancio final");
        
        int cansancioAcum = 0;
        
        // los que juegan
        for (JugadorFutbol8 jug : eq.getAlineacion().jugadores()) {
            int cansancio = jug.getCansancioPartido();                
            cansancio = cansancio / 10;
            if (jug.getEstadoFisico() - cansancio < 10)
                cansancio = jug.getEstadoFisico() - 10; 
            if (!esSimul){
                jug.setEstadoFisico(jug.getEstadoFisico() - cansancio);
                jug.setPartidosJugados(jug.getPartidosJugados() + 1);
            }
            cansancioAcum = cansancioAcum + cansancio;
            
        } 
        for (JugadorFutbol8 jug : eq.getJugadores()) {
            if (!esSimul && !jug.isJuegaJornada()){
                jug.setEstadoFisico(jug.getEstadoFisico() + 5 + (100 - jug.getEstadoFisico()) / 5);            
                    if (jug.getEstadoFisico() > 100)
                        jug.setEstadoFisico(100);
            }
        }    
        
        String txt = "Cansancio Global ".concat(String.valueOf(cansancioAcum));        
        
        logger.debug("Asignar cansancio final OK");
        
        return txt;
        
    }
    
    private void sacarTarjetaAmarilla(JugadorFutbol8 jug){
        
       
        if (jug.isTarjetaRoja()) return;
        EquipoFutbol8 eq = jug.getEquipo();
        
        boolean roja = jug.isTarjetaAmarilla();
        
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
        
        EquipoFutbol8 eq = jug.getEquipo();
        eq.tarjetas.append(jug.getNombre()).append("(Roja);");
      
        if (!this.isSimulacion()) {
            jug.setTarjetaRoja(true);
            jug.setTarjetaAmarilla(false);
        }
            
    }
    
    private void quitarTarjetas() {
        
        if (this.isSimulacion()) return;
        
        logger.debug("Quitar tajetas");
        
        ArrayList<JugadorFutbol8> jugs = new ArrayList<JugadorFutbol8>();
        jugs.addAll(this.getEqLocal().getJugadores());
        jugs.addAll(this.getEqVisitante().getJugadores());
        
        for (JugadorFutbol8 jug : jugs)
            if (jug.isTarjetaRoja()) jug.setTarjetaRoja(false);
        
        logger.debug("Quitar tarjetas OK");
                
    }
    
    private void lesionar(JugadorFutbol8 jug){
        
        logger.debug("Lesionar");
        
        if (jug.getJornadasLesion() > 0) return;
        
        EquipoFutbol8 eq = jug.getEquipo();
         
        int maxJornadasLesion = 6;
        
        eq.lesionados.append(jug.getNombre()).append(";");
                
        int num = Calculos.valorAleatorio(1, maxJornadasLesion);  
        
         if (!this.isSimulacion()) 
             // Sumamos 1 porque al finalizar la jornada se va a 
             // restar una jornada
             jug.setJornadasLesion(num + 1);
        
        logger.debug("Lesionar OK");
        
    }
    
    
    public EquipoFutbol8 hacerTandaPenalties(){
        
        // Deviuelve el equipo que gana la tanda de penalties
        
        logger.debug("Hacer tanda Penalties");
        
        escribir("Y vamos a la tanda de penalties");
        
        EquipoFutbol8 eqGanador = null;
        
        int numPenalties = 5;
        
        ArrayList<JugadorFutbol8> jugsLocal = aliLocal.delanteros();
        jugsLocal.addAll(aliLocal.centrocampistas());
        jugsLocal.addAll(aliLocal.defensas());
        int local = 0;
        
        ArrayList<JugadorFutbol8> jugsVisit = aliVisit.delanteros();
        jugsVisit.addAll(aliVisit.centrocampistas());
        jugsVisit.addAll(aliVisit.defensas());
        int visit = 0;

        boolean isGol;
        for (int i = 1; i <= numPenalties; i++){
            if (jugsLocal.size() >= i){
                JugadorFutbol8 jugEqLocal = jugsLocal.get(i - 1);
                isGol = hacerTiro(jugEqLocal, porteroEqVisitante(), ClaseTiro.Penalti);  
                if (isGol) local++;
            }
            if (jugsVisit.size() >= i){
                JugadorFutbol8 jugEqVisit = jugsVisit.get(i - 1);
                isGol = hacerTiro(jugEqVisit, porteroEqLocal(), ClaseTiro.Penalti);  
                if (isGol) visit++;
            }       
        }
        
        // Si hay empate desempatamos
        int i = 6;
        while (local == visit){
            if (jugsLocal.size() >= i){
                JugadorFutbol8 jugEqLocal = jugsLocal.get(i - 1);
                isGol = hacerTiro(jugEqLocal, porteroEqVisitante(), ClaseTiro.Penalti);  
                if (isGol) local++;
            }
            if (jugsVisit.size() >= i){
                JugadorFutbol8 jugEqVisit = jugsVisit.get(i - 1);
                isGol = hacerTiro(jugEqVisit, porteroEqLocal(), ClaseTiro.Penalti);
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
        
        logger.debug("Hacer tanda Penalties OK");
        
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
        
        boolean ok = true;
        
        int min = EquipoFutbol8.NUMERO_MINIMO_JUGADORES;
      
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
        
        for (JugadorFutbol8 jug : jugadores) {
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
        for (ClaseTiro clase : ClaseTiro.values()){
            Field campoTiro, campoParada = null;
            int chutesPorteria = 0;
            int paradas = 0;
            try {
                if (portero != null){
                    campoParada = portero.getClass().getDeclaredField("paradas" + clase.name());
                    campoParada.setAccessible(true);
                }
                for (JugadorFutbol8 jug : jugadores) {
                    campoTiro = jug.getClass().getDeclaredField(clase.name());
                    campoTiro.setAccessible(true);                    
                    int goles = 0;                     
                    for (int i = 0; i < 100; i++){
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
            catch (IllegalArgumentException ex) {} 
            catch (IllegalAccessException ex) {}
            catch (NoSuchFieldException ex) {}
            catch (SecurityException ex) {}               
        }
                
    }
    
    
    public void simulacionTiros(){
        
        inicializarDatos();
        simularTiros(this.getAlineacionEqLocal().jugadoresCampo(), this.getAlineacionEqVisitante().getPortero());
        simularTiros(this.getAlineacionEqVisitante().jugadoresCampo(), this.getAlineacionEqLocal().getPortero());
  
    }

    private void posibleLesionPortero(JugadorFutbol8 portero) {
        
        int posibilidadLesion = 500;
        
        boolean isLesion = Calculos.obtener(posibilidadLesion);
        
        if (isLesion){
            lesionar(portero);
            escribir(portero.getNombre() + mapeo().get(CronicaStrings.lesionPortero));
            
        }
        
    }

   

}
