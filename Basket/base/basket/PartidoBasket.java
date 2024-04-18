package mld.clubdeportivo.base.basket;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.CronicaStrings;
import mld.clubdeportivo.base.Partido;
import mld.clubdeportivo.utilidades.Calculos;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public final class PartidoBasket extends Partido{
    
    private static Logger logger = LogManager.getLogger(PartidoBasket.class);
    public static int NUMERO_SIMULACIONES = 100;
    public static int MINUTOS_PARTIDO = 90;

    private String tipo;
    private boolean simulacion;
    private int segundo; // para llevar la cuenta de los minutos del partido
    private ArrayList<JugadorBasket> goleadores;
    private ArrayList<AlineacionBasket> alineaciones;
    private EstadisticaPartidoBasket estadistica;
    private EquipoBasket eqLocal;
    private EquipoBasket eqVisit;
    private AlineacionBasket aliLocal;
    private AlineacionBasket aliVisit;
    private int golesLocal;
    private int golesVisitante;

    ArrayList<CronicaBasket> cronica = new ArrayList<CronicaBasket>();
    
    public ArrayList<CronicaBasket> getCronica(){
        return this.cronica;
    }
    
    public void setCronica(ArrayList<CronicaBasket> cronica){
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
        
        this.cronica.add(CronicaBasket.escribir(this, accion, "", segundo / 60));        
    }
    
    private void escribir(String accion, String cuadrante, EquipoBasket eq){  
        
        if (eq != null)
            if (eq.equals(eqLocal)) cuadrante = "L" + cuadrante;
            else cuadrante = "V" + cuadrante;
        
        this.cronica.add(CronicaBasket.escribir(this, accion, cuadrante, segundo / 60));        
    }
    
    
    public PartidoBasket(){
        this.setIsSimulacion(false);
    
    }
    
    public PartidoBasket(boolean simulacion){
        this.setIsSimulacion(simulacion);
    
    }
    
    public PartidoBasket(String tipo){
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
    
    public void perpararDatos(EquipoBasket eq){
        
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
        ArrayList<JugadorBasket> jugs = eq.getAlineacion().jugadores();
        for (JugadorBasket jug : jugs) {
            jug.setCansancioPartido(0);
        }
        
    }
    
    private void sincronizarJugadores(EquipoBasket eq){
        // Sincroniza los jugadores de alineacion y equipo
        // ya que vienen de sitios diferentes
        
        ArrayList<JugadorBasket> lista = eq.getAlineacion().jugadores();
        
        for (JugadorBasket jug : eq.getJugadores()) {
            boolean anyadir = true;
            for (JugadorBasket jugAli : eq.getAlineacion().jugadores()) 
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
        eqLocal = (EquipoBasket) this.getEqLocal();
        aliLocal = this.getAlineacionEqLocal();
        eqLocal.setAlineacion(aliLocal);
        sincronizarJugadores(eqLocal);
        perpararDatos(eqLocal);
        eqVisit = (EquipoBasket) this.getEqVisitante();
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
        
        EquipoBasket eqAtq;        
        EquipoBasket eqDef; 
        
        if (eqLocal.isJuegaEnCasa() || Calculos.obtener(2)){
             eqAtq = eqLocal;        
             eqDef = eqVisit;        
        }
        else {
             eqAtq = eqVisit;        
             eqDef = eqLocal;   
        }
            
        EquipoBasket eqTmp;
        JugadorBasket jugAtq = null;
        JugadorBasket jugDef;
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
            if (eqAtq.estrategia().equals(EstrategiaBasket.Ralentizar))
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
                        (eqAtq.estrategia().equals(EstrategiaBasket.Tirar) ||
                        Calculos.obtener(4))
                        ){
                    JugadorBasket newJug = hacerPreparacionTiro(jugAtq, jugDef);
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
                else if (eqAtq.estrategia().equals(EstrategiaBasket.Contraataque) ||
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
                if (pos == '3' || eqAtq.estrategia().equals(EstrategiaBasket.Tirar)){
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
 
    public EstadisticaPartidoBasket jugarPartido(){
        
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
        
        goleadores = new ArrayList<JugadorBasket>(); 
        
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
    
    private JugadorBasket jugadorDeAtaque(EquipoBasket eq, JugadorBasket jugAtq){
        
        if (jugAtq == null){
            jugAtq = jugadorInicioJugada(eq);
            jugAtq.getEquipo().posesion++;
            escribir(jugAtq.getNombre() + mapeo().get(CronicaStrings.tienePelota), jugAtq.getCuadrante(), eq);
        }
        
        return jugAtq;        
    }
    
    private boolean faltaAlHacerRegate(JugadorBasket jugAtq, JugadorBasket jugDef){
        // Si el jugador de defensa le quita la pelota miramos 
        // si ha habido alguna falta
        
        boolean esFalta = false;
        boolean esLesion = false;
        
        int posFalta = 7;
        int posTarjeta = 2;
        int posPenalti = 20;
        int posLesion = 5;
        
        if (jugDef.getEstrategia().equals(EstrategiaBasket.Agresivos)){
            posFalta = 3;
            posTarjeta = 1;
            posPenalti = 12;
            posLesion = 2;            
        }
        
        EquipoBasket eqDef = jugDef.getEquipo();
         
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
    
    

    private JugadorBasket hacerRegate(JugadorBasket jugAtq, JugadorBasket jugDef){
        // Si devuelve null es que el jugador sale del regate
        // si devuelve un jugador es el que le ha quitado la pelota
        
        JugadorBasket jug = null;

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
    
    private void salidaPortero(JugadorBasket jugAtq, JugadorBasket portero){
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
    
    private JugadorBasket hacerPase(JugadorBasket jugAtq, JugadorBasket jugDef){
 
        JugadorBasket jugTmp;
        boolean paseOk = true;
        int pase = 5;
        int paseLargo = 20;
        EquipoBasket eq = jugAtq.getEquipo();
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
    
    private JugadorBasket hacerPreparacionTiro(JugadorBasket jugAtq, JugadorBasket jugDef){
        // este metodo tiene en cuenta la accion del defensor
        // si se devuelve null es que se sigue con el tiro
   
        JugadorBasket newJug;
        
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
    
    private boolean hacerTiro(JugadorBasket jugAtq, JugadorBasket portero, ClaseTiro clase){
        
        // Devuelve true si es gol
        
        boolean isGol = false;
        TiroPuertaBasket tiro;
        EquipoBasket eq = jugAtq.getEquipo();
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
        tiro = new TiroPuertaBasket(jugAtq, portero, clase);
        String valInfPortero = "a puerta vacia";
        EquipoBasket eqPortero = null;
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
    
   
    
       
    private void registrarGol(JugadorBasket jug, JugadorBasket portero, TiroPuertaBasket tiro){
        
        EquipoBasket eq = jug.getEquipo();
        
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
    
    
    private JugadorBasket jugadorInicioJugada(EquipoBasket eq){
        
        AlineacionBasket ali = eq.getAlineacion();
        JugadorBasket jug = null;
        
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
    
     private JugadorBasket jugadorReceptorAleatorio(){
        
        
        ArrayList<JugadorBasket> jugs = new ArrayList<JugadorBasket>();
        
        jugs.addAll(aliLocal.jugadoresCampo());
        jugs.addAll(aliVisit.jugadoresCampo());
        
        int x = Calculos.valorAleatorio(jugs.size());
        JugadorBasket jug = jugs.get(x);        
         
        return jug;    
        
    }
    
    private JugadorBasket jugadorQueDefiende(EquipoBasket eq, JugadorBasket jugAtq){
        // 1 linea defensiva
        // 2 Centrocampo
        // 3 Delantera
        
        AlineacionBasket ali = eq.getAlineacion();
        JugadorBasket jug;
        int pos = jugAtq.getCuadrante().charAt(1);
        boolean presion = ali.getEstrategia().equals(EstrategiaBasket.Presionar);
        int extraCansacio = 0;
        int capacidadRobo = 100;
                
        jug = ali.jugadorContrario(jugAtq.getCuadrante());
        
        // Si la estrategia es Estatica el defensor no se desplaza
        if (jug == null && ali.getEstrategia().equals(EstrategiaBasket.Estaticos))
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
    
    private JugadorBasket jugadorReceptorCercano(JugadorBasket jugPase){
        
        // Si el jugador esta en delantera no aplica
        if (jugPase.getCuadrante().charAt(1) == '3') return null;
        
        AlineacionBasket ali = jugPase.getEquipo().getAlineacion();
        JugadorBasket jug = ali.jugadorParaPase(jugPase.getCuadrante());
         
         return jug;    
        
    }
    
        
    private JugadorBasket jugadorReceptorMismaLinea(JugadorBasket jugPase){
        
        AlineacionBasket ali = jugPase.getEquipo().getAlineacion();
        JugadorBasket jug = ali.jugadorParaPaseMismaLinea(jugPase.getCuadrante());
         
         return jug;    
        
    }
    
     private JugadorBasket jugadorReceptorLejano(JugadorBasket jugPase){
        
         // Si el jugador esta en delantera no aplica
         if (jugPase.getCuadrante().charAt(1) == '3') return null;
         
         AlineacionBasket ali = jugPase.getEquipo().getAlineacion();
         JugadorBasket jug = ali.jugadorParaPaseLargo(jugPase.getCuadrante());
         
         return jug;    
        
    }
     
     private JugadorBasket jugadorReceptorTrasero(JugadorBasket jugPase){
        
        // Si el jugador esta en delantera no aplica
        if (jugPase.getCuadrante().charAt(1) == '1') return null;
        
        AlineacionBasket ali = jugPase.getEquipo().getAlineacion();
        JugadorBasket jug = ali.jugadorParaPaseAtras(jugPase.getCuadrante());
         
         return jug;    
        
    }
     
    
           
    private void disputarCorner(EquipoBasket eqAtq, EquipoBasket eqDef){
        
        logger.debug("Calcular Corners");
        
         
        AlineacionBasket aliAtq = eqAtq.getAlineacion();
        AlineacionBasket aliDef = eqDef.getAlineacion();
        
        int numJug;

        if (aliAtq.jugadoresCampo().size() > 0){
                    
            numJug = Calculos.valorAleatorio(aliAtq.jugadoresCampo().size());                        
            JugadorBasket jugAtq = aliAtq.jugadoresCampo().get(numJug);                  
            numJug = Calculos.valorAleatorio(aliDef.jugadoresCampo().size());
            JugadorBasket jugDef = aliDef.jugadoresCampo().get(numJug);
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
    
    
    
    private void lanzarFaltaDirecta(EquipoBasket eqAtq, JugadorBasket portero){

        logger.debug("Calcular Faltas Directas");
        
        int numJug;
        
        
        AlineacionBasket aliAtq = eqAtq.getAlineacion();
        
        ArrayList<JugadorBasket> jugs = new ArrayList<JugadorBasket>();
        
        jugs.addAll(aliAtq.centrocampistas());
        jugs.addAll(aliAtq.delanteros());
        
        if (jugs.size() > 0){
            
            // Va a puerta o no
            if (Calculos.obtener(3)) return;
            
            numJug = Calculos.valorAleatorio(jugs.size());                        
            JugadorBasket jugAtq = jugs.get(numJug);                  
            hacerTiro(jugAtq, portero, ClaseTiro.Falta);
        }    
        
        logger.debug("Calcular Faltas Directas OK");
       
    }
    
    private void lanzarPenalti(EquipoBasket eqAtq, JugadorBasket portero){

        logger.debug("lanzar Penalti");
        
        int numJug;
        
        AlineacionBasket aliAtq = eqAtq.getAlineacion();
        
        ArrayList<JugadorBasket> jugs = new ArrayList<JugadorBasket>();
        
        if (aliAtq.delanteros().size() > 0) jugs = aliAtq.delanteros();
        else if (aliAtq.centrocampistas().size() > 0) jugs = aliAtq.delanteros();
                
        if (jugs.size() > 0){
            numJug = Calculos.valorAleatorio(jugs.size());                        
            JugadorBasket jugAtq = jugs.get(numJug);                  
            hacerTiro(jugAtq, portero, ClaseTiro.Penalti);
        }    
        
       logger.debug("lanzar Penalti OK");
       
    }
    
       
    public EquipoBasket getGanador(){
        
        EquipoBasket eq = null;
        
        if (this.getGolesLocal() > this.getGolesVisitante())
            eq = (EquipoBasket) this.getEqLocal();
        else if (this.getGolesLocal() < this.getGolesVisitante())
            eq = (EquipoBasket) this.getEqVisitante();
        
        return eq;
        
    }
    
     public EquipoBasket getPerdedor(){
        
        EquipoBasket eq = null;
        
        if (this.getGolesLocal() < this.getGolesVisitante())
            eq = (EquipoBasket) this.getEqLocal();
        else if (this.getGolesLocal() > this.getGolesVisitante())
            eq = (EquipoBasket) this.getEqVisitante();
        
        return eq;
        
    }


    public ArrayList<AlineacionBasket> getAlineaciones() {
        return alineaciones;
    }
    
     public AlineacionBasket getAlineacionEqLocal() {
        AlineacionBasket ali = null;
         for (AlineacionBasket unaAli : this.getAlineaciones()) {
             if(unaAli.getEquipo().equals(this.getEqLocal()))
                 ali = unaAli;
         }
         return ali;
    }
     
     public AlineacionBasket getAlineacionEqVisitante() {
        AlineacionBasket ali = null;
         for (AlineacionBasket unaAli : this.getAlineaciones()) {
             if(unaAli.getEquipo().equals(this.getEqVisitante()))
                 ali = unaAli;
         }
         return ali;
    }

    public void setAlineaciones(ArrayList<AlineacionBasket> alineaciones) {
        this.alineaciones = alineaciones;
    }
    
    public void asignarPuntos() {
        
        if (this.isSimulacion()) return;

        logger.debug("Asignar Puntos");
        
        EquipoBasket eqL = (EquipoBasket) this.getEqLocal();
        PuntuacionBasket puntsL = eqL.getPuntuacion();
        EquipoBasket eqV = (EquipoBasket) this.getEqVisitante();
        PuntuacionBasket puntsV = eqV.getPuntuacion();
        
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
        
        EquipoBasket eqL = (EquipoBasket) this.getEqLocal();
        EquipoBasket eqV = (EquipoBasket) this.getEqVisitante();
        
        
        if (this.getGanador() == null && eqL.isJuegaEnCasa()){
            eqV.modificarMoral(eqV.getMoral() + 1 + (EquipoBasket.MORAL_INICIAL - eqV.getMoral()) / 9);
            eqL.modificarMoral(eqL.getMoral() - 1);
        }
        else if (this.getGanador() != null && this.getGanador().equals(eqV) && eqL.isJuegaEnCasa()){
            eqV.modificarMoral(eqV.getMoral() + 3 + (EquipoBasket.MORAL_INICIAL - eqV.getMoral()) / 3);
            eqL.modificarMoral(eqL.getMoral() - 3);
        }
        
        // ademas si los equipos han jugado en Maximo sumamos o 
        // quitamos un punto si ganan o pierden
        if (this.getGanador() != null){
            if (eqL.getAlineacion().getEsfuerzo().equals(EsfuerzoBasket.Maximo))
                if (this.getGanador().equals(eqL)) 
                    eqL.modificarMoral(eqL.getMoral() + 1);
                else
                    eqL.modificarMoral(eqL.getMoral() - 1);
            if (eqV.getAlineacion().getEsfuerzo().equals(EsfuerzoBasket.Maximo))
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
        CompeticionBasket comp = (CompeticionBasket) this.getJornada().getCompeticion();

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
        if (eqLocal.getAlineacion().getEsfuerzo().equals(EsfuerzoBasket.Maximo)){
            max = 1.5;
            if (eqVisit.getAlineacion().getEsfuerzo().equals(EsfuerzoBasket.Maximo))
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
    
    private void completarValoracionesJugadores(EquipoBasket eq) {

        logger.debug("Completar valoraciones");
        
        int minJugarEnCasa = 10;
        
        AlineacionBasket ali = eq.getAlineacion();
        
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
        EsfuerzoBasket esf = ali.getEsfuerzo();   
        
        // calculamos plus antrenador
        EntrenadorBasket entr = eq.getEntrenador();
        int plusTact = entr.obtenerPlusTactica(ali.getTactica());
        if (!this.isSimulacion()){
            entr.setPlusTactica(plusTact);
           entr.setUltimaTacticaUtilizada(ali.getTactica());
        }        
        
        int valFinal;
        
        ArrayList<JugadorBasket> jugs = ali.jugadores();
        
        for (JugadorBasket jug : jugs) {
            if (jug == null) continue;
            valFinal = jug.getValoracionMedia();
            valFinal = valFinal * moral / 100;
            
            int suplementos = campo + plusTact;
            if (esf.equals(EsfuerzoBasket.Maximo))
                suplementos = suplementos + 10;
            else if (esf.equals(EsfuerzoBasket.Relajado))
                suplementos = suplementos - 10;
            
            valFinal = valFinal + (valFinal * suplementos / 100);
            
            jug.setValoracionPartido(valFinal);
            jug.setEstrategia(ali.getEstrategia());
            // Si el jugador esta fuera de su posicion lo marcamos
            jug.setFueraPosicion(false);
            if (!jug.getPosicion().equals(PosicionJugBasket.Portero))
                    if (
                            (jug.getCuadrante().charAt(1) == '1' && 
                            !jug.getPosicion().equals(PosicionJugBasket.Defensa)) ||
                            (jug.getCuadrante().charAt(1) == '2' && 
                            !jug.getPosicion().equals(PosicionJugBasket.Medio)) ||
                            (jug.getCuadrante().charAt(1) == '3' && 
                            !jug.getPosicion().equals(PosicionJugBasket.Delantero))
                            )
                        jug.setFueraPosicion(true);           
        }   
        
        if (esf.equals(EsfuerzoBasket.Maximo))
                eq.extras.append("Esfuerzo +10%" + ";");
            else if (esf.equals(EsfuerzoBasket.Relajado))
                eq.extras.append("Esfuerzo -10%" + ";");
        
        if (plusTact > 0)
                eq.extras.append("Plus Tactica: ").append(plusTact).append(";");           
        
        logger.debug("Completar valoraciones OK");

        
    }
    
    private JugadorBasket porteroEqLocal(){
        
        AlineacionBasket ali = this.getAlineacionEqLocal();
        
        return ali.getPortero();
        
    }
    
    private JugadorBasket porteroEqVisitante(){
        
        AlineacionBasket ali = this.getAlineacionEqVisitante();
        
        return ali.getPortero();
        
    }
    
    
       

    public EstadisticaPartidoBasket getEstadistica() {
        return estadistica;
    }

    public void setEstadistica(EstadisticaPartidoBasket estadistica) {
        this.estadistica = estadistica;
    }

    public ArrayList<JugadorBasket> getGoleadores() {
        return goleadores;
    }
  
    
    private EsfuerzoBasket esfuerzoJugador(JugadorBasket jug){
        
        EsfuerzoBasket esf = null;
        
        if (jug.getEquipo().equals(this.getEqLocal()))
            return this.getAlineacionEqLocal().getEsfuerzo();
        else if (jug.getEquipo().equals(this.getEqVisitante()))
            return this.getAlineacionEqVisitante().getEsfuerzo();
        
        return esf;
        
    }

    private void sumarCansancio(JugadorBasket jug, int extra){
        
        int esf = 5;
        
        if (jug == null) return;
        
        EsfuerzoBasket esfJug = esfuerzoJugador(jug);
        
        if (jug.getPosicion().equals(PosicionJugBasket.Portero))
            esf = 2;        
        else if (esfJug.equals(EsfuerzoBasket.Maximo))
            esf = 10;
        else if (esfJug.equals(EsfuerzoBasket.Relajado))
            esf = 2; 
        
        jug.setCansancioPartido(jug.getCansancioPartido() + esf * extra);
        
    }
    
     private void sumarCansancio(JugadorBasket jug){
        
         sumarCansancio(jug, 1);
    }
    
    private String asignarCansancioFinalJugadores (EquipoBasket eq, 
            boolean esSimul){
        
        logger.debug("Asignar cansancio final");
        
        int cansancioAcum = 0;
        
        // los que juegan
        for (JugadorBasket jug : eq.getAlineacion().jugadores()) {
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
        for (JugadorBasket jug : eq.getJugadores()) {
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
    
    private void sacarTarjetaAmarilla(JugadorBasket jug){
        
       
        if (jug.isTarjetaRoja()) return;
        EquipoBasket eq = jug.getEquipo();
        
        boolean roja = jug.isTarjetaAmarilla();
        
        if (roja)
            sacarTarjetaRoja(jug);
        else{
            eq.tarjetas.append(jug.getNombre()).append(";");
            if (!this.isSimulacion())
                jug.setTarjetaAmarilla(true);
        }

        
    }
    
    private void sacarTarjetaRoja(JugadorBasket jug){
                
        if (jug.isTarjetaRoja()) return;
        
        EquipoBasket eq = jug.getEquipo();
        eq.tarjetas.append(jug.getNombre()).append("(Roja);");
      
        if (!this.isSimulacion()) {
            jug.setTarjetaRoja(true);
            jug.setTarjetaAmarilla(false);
        }
            
    }
    
    private void quitarTarjetas() {
        
        if (this.isSimulacion()) return;
        
        logger.debug("Quitar tajetas");
        
        ArrayList<JugadorBasket> jugs = new ArrayList<JugadorBasket>();
        jugs.addAll(this.getEqLocal().getJugadores());
        jugs.addAll(this.getEqVisitante().getJugadores());
        
        for (JugadorBasket jug : jugs)
            if (jug.isTarjetaRoja()) jug.setTarjetaRoja(false);
        
        logger.debug("Quitar tarjetas OK");
                
    }
    
    private void lesionar(JugadorBasket jug){
        
        logger.debug("Lesionar");
        
        if (jug.getJornadasLesion() > 0) return;
        
        EquipoBasket eq = jug.getEquipo();
         
        int maxJornadasLesion = 6;
        
        eq.lesionados.append(jug.getNombre()).append(";");
                
        int num = Calculos.valorAleatorio(1, maxJornadasLesion);  
        
         if (!this.isSimulacion()) 
             // Sumamos 1 porque al finalizar la jornada se va a 
             // restar una jornada
             jug.setJornadasLesion(num + 1);
        
        logger.debug("Lesionar OK");
        
    }
    
    
    public EquipoBasket hacerTandaPenalties(){
        
        // Deviuelve el equipo que gana la tanda de penalties
        
        logger.debug("Hacer tanda Penalties");
        
        escribir("Y vamos a la tanda de penalties");
        
        EquipoBasket eqGanador = null;
        
        int numPenalties = 5;
        
        ArrayList<JugadorBasket> jugsLocal = aliLocal.delanteros();
        jugsLocal.addAll(aliLocal.centrocampistas());
        jugsLocal.addAll(aliLocal.defensas());
        int local = 0;
        
        ArrayList<JugadorBasket> jugsVisit = aliVisit.delanteros();
        jugsVisit.addAll(aliVisit.centrocampistas());
        jugsVisit.addAll(aliVisit.defensas());
        int visit = 0;

        boolean isGol;
        for (int i = 1; i <= numPenalties; i++){
            if (jugsLocal.size() >= i){
                JugadorBasket jugEqLocal = jugsLocal.get(i - 1);
                isGol = hacerTiro(jugEqLocal, porteroEqVisitante(), ClaseTiro.Penalti);  
                if (isGol) local++;
            }
            if (jugsVisit.size() >= i){
                JugadorBasket jugEqVisit = jugsVisit.get(i - 1);
                isGol = hacerTiro(jugEqVisit, porteroEqLocal(), ClaseTiro.Penalti);  
                if (isGol) visit++;
            }       
        }
        
        // Si hay empate desempatamos
        int i = 6;
        while (local == visit){
            if (jugsLocal.size() >= i){
                JugadorBasket jugEqLocal = jugsLocal.get(i - 1);
                isGol = hacerTiro(jugEqLocal, porteroEqVisitante(), ClaseTiro.Penalti);  
                if (isGol) local++;
            }
            if (jugsVisit.size() >= i){
                JugadorBasket jugEqVisit = jugsVisit.get(i - 1);
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
            eqGanador = (EquipoBasket) this.getEqLocal();
        else if (eqLocal.goles < eqVisit.goles)
            eqGanador = (EquipoBasket) this.getEqVisitante();
        
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
        
        int min = EquipoBasket.NUMERO_MINIMO_JUGADORES;
      
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

   
    private void simularTiros(ArrayList<JugadorBasket> jugadores, JugadorBasket portero){        
        
        TiroPuertaBasket tiro;
        
        for (JugadorBasket jug : jugadores) {
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
                for (JugadorBasket jug : jugadores) {
                    campoTiro = jug.getClass().getDeclaredField(clase.name());
                    campoTiro.setAccessible(true);                    
                    int goles = 0;                     
                    for (int i = 0; i < 100; i++){
                        tiro = new TiroPuertaBasket(jug, portero, clase);       
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

    private void posibleLesionPortero(JugadorBasket portero) {
        
        int posibilidadLesion = 500;
        
        boolean isLesion = Calculos.obtener(posibilidadLesion);
        
        if (isLesion){
            lesionar(portero);
            escribir(portero.getNombre() + mapeo().get(CronicaStrings.lesionPortero));
            
        }
        
    }

   

}
