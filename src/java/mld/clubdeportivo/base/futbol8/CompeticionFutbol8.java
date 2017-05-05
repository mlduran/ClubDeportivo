
package mld.clubdeportivo.base.futbol8;

import java.util.ArrayList;
import java.util.Date;
import mld.clubdeportivo.base.Club;
import mld.clubdeportivo.base.Competicion;
import mld.clubdeportivo.base.Equipo;
import mld.clubdeportivo.base.Grupo;
import mld.clubdeportivo.bd.DAOException;
import mld.clubdeportivo.utilidades.Calculos;
import mld.clubdeportivo.utilidades.JornadaTmp;

/**
 *
 * @author Miguel
 */
public final class CompeticionFutbol8 extends Competicion{

    private String clase; // Liga, Copa, Supercopa, etc
    private ArrayList<JornadaFutbol8> jornadas;
    public static String[] clasesCompeticion = {"Liga", "Copa", "Supercopa"};
    private String maximosGoleadores;
    private int recaudacion;

    public CompeticionFutbol8(){}
    
    public CompeticionFutbol8(String tipo, String nombre, Grupo grp){
        
        this.setGrupo(grp);
        this.setNombre(nombre);
        this.setFecha(new Date());
        this.setClase(tipo);
        this.setActiva(true);
        this.setMaximosGoleadores("");
        this.setJornadas(new ArrayList<JornadaFutbol8>());
        this.setRecaudacion(0);
        
    }

    public int getNumeroJornadas() {
        return this.getJornadas().size();
    }

   
    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        boolean ok = false;
        for (String claseComp : clasesCompeticion) 
            if (clase.equals(claseComp)) ok = true;
        
        if (!ok) throw new IllegalArgumentException("La clase de competicion no es valida");
    
        this.clase = clase;
    }

     public ArrayList<JornadaFutbol8> getJornadas() {
        return jornadas;
    }
     
    public JornadaFutbol8 getJornadabyNumero(int numero){
        
        JornadaFutbol8 jor = null;
        
        for (JornadaFutbol8 jornada : this.getJornadas()) {
            if(jornada.getNumero() == numero){ 
                jor = jornada;
                break;
            }
        }
        
        return jor;
    }

    public void setJornadas(ArrayList<JornadaFutbol8> jornadas) {
        this.jornadas = jornadas;
    }

  
    public static CompeticionFutbol8 crearLiga(Grupo grp, ArrayList<EquipoFutbol8> eqs){

        CompeticionFutbol8 comp = new CompeticionFutbol8("Liga", "Liga Futbol8", grp);        

        ArrayList<JornadaTmp> jornadas = Calculos.crearCalendarioLiga((ArrayList) eqs);

        for (int i = 1; i < (eqs.size() * 2) - 1; i++){
            JornadaFutbol8 jornada = new JornadaFutbol8();
            jornada.setCompeticion(comp);
            jornada.setNumero(i);
            jornada.setDescripcion("Jornada " + i);
            jornada.setPartidos(new ArrayList<PartidoFutbol8>());
            comp.getJornadas().add(jornada);
            for (JornadaTmp jornadaTmp : jornadas) {
                if (jornadaTmp.getJornada() == i){
                    PartidoFutbol8 partido = new PartidoFutbol8("Liga");
                    partido.setJornada(jornada);
                    partido.setEqLocal((Equipo) jornadaTmp.getEqLocal());
                    partido.setEqVisitante((Equipo) jornadaTmp.getEqVisitante());
                    partido.setDescripcion(partido.getNombreEqLocal() + " - " + partido.getNombreEqVisitante());
                    jornada.getPartidos().add(partido);
                    ArrayList<AlineacionFutbol8> alineaciones =
                            new ArrayList<AlineacionFutbol8>();
                    alineaciones.add(
                            new AlineacionFutbol8((EquipoFutbol8) jornadaTmp.getEqLocal(), partido));

                    alineaciones.add(
                            new AlineacionFutbol8((EquipoFutbol8) jornadaTmp.getEqVisitante(),partido));
                    partido.setAlineaciones(alineaciones);
                }
            }
        }

        return comp;

    }
   

    public static CompeticionFutbol8 crearCopa(Grupo grp, ArrayList<EquipoFutbol8> eqs) 
            throws DAOException {
        // solo creamos semifinales a partir de 8 equipos
        // si hay menos solo hay una final

        CompeticionFutbol8 comp = new CompeticionFutbol8("Copa", "Copa Futbol8", grp);
        
        JornadaFutbol8 jornada; 

        int numEqs = eqs.size();

        // seleccionamos los que realmente han de competir
        if (numEqs <= 6)
            numEqs = 2;
        else if (numEqs <= 10)
            numEqs = 4;
        else if (numEqs <= 14)
            numEqs = 8;
        else if (numEqs == 16)
            numEqs = 16;
        eqs = EquipoFutbol8.clasificarEquipos(eqs, numEqs);

        String des = "Final";
        if (numEqs == 2){
            jornada = new JornadaFutbol8();
            jornada.setCompeticion(comp);
            jornada.setNumero(1);
            jornada.setDescripcion(des);
            jornada.setPartidos(new ArrayList<PartidoFutbol8>());
            comp.getJornadas().add(jornada);

            PartidoFutbol8 partido = new PartidoFutbol8("Copa");
            partido.setJornada(jornada);
            partido.setEqLocal(eqs.get(0));
            partido.setEqVisitante(eqs.get(1));
            partido.setDescripcion(partido.getNombreEqLocal() + " - " + 
                    partido.getNombreEqVisitante());
            jornada.getPartidos().add(partido);
            asignarAlineaciones(partido);
            
        }
        else
        {
            if (numEqs == 4)
                des = "Semifinales";
            else if (numEqs == 8)
                des = "Cuartos";
            else if (numEqs == 16)
                des = "Octavos";

            jornada = new JornadaFutbol8();
            jornada.setCompeticion(comp);
            jornada.setNumero(1);
            jornada.setDescripcion(des.concat("(ida)"));
            jornada.setPartidos(new ArrayList<PartidoFutbol8>());
            comp.getJornadas().add(jornada);
            for (int i = 1; i <= numEqs / 2; i++) {
                PartidoFutbol8 partido = new PartidoFutbol8("Copa");
                partido.setJornada(jornada);
                partido.setEqLocal(eqs.get(numEqs - i));
                partido.setEqVisitante(eqs.get(i - 1));
                partido.setDescripcion(partido.getNombreEqLocal() + " - " + 
                    partido.getNombreEqVisitante());
                jornada.getPartidos().add(partido);
                asignarAlineaciones(partido);
            }

            jornada = new JornadaFutbol8();
            jornada.setCompeticion(comp);
            jornada.setNumero(2);
            jornada.setDescripcion(des.concat("(vuelta)"));
            jornada.setPartidos(new ArrayList<PartidoFutbol8>());
            comp.getJornadas().add(jornada);
            for (int i = 1; i <= numEqs / 2; i++) {
                PartidoFutbol8 partido = new PartidoFutbol8("Copa");
                partido.setJornada(jornada);
                partido.setEqLocal(eqs.get(i - 1));
                partido.setEqVisitante(eqs.get(numEqs - i));
                partido.setDescripcion(partido.getNombreEqLocal() + " - " + 
                    partido.getNombreEqVisitante());
                jornada.getPartidos().add(partido);
                asignarAlineaciones(partido);
            }

            int numJor = 3;
            int numPartidos = 1;
            while (!des.equals("Final")){               
                if (des.equals("Octavos")){
                    des = "Cuartos";
                    numPartidos = 4;
                }
                else if (des.equals("Cuartos")){
                    des = "Semifinales";
                    numPartidos = 2;
                }
                else if (des.equals("Semifinales")) des = "Final";
                
                if (!des.equals("Final")){
                    jornada = new JornadaFutbol8();
                    jornada.setCompeticion(comp);
                    jornada.setNumero(numJor);
                    jornada.setDescripcion(des.concat("(ida)"));
                    jornada.setPartidos(new ArrayList<PartidoFutbol8>());
                    comp.getJornadas().add(jornada);
                    for (int i = 1; i <= numPartidos; i++) {
                        PartidoFutbol8 partido = new PartidoFutbol8("Copa");
                        partido.setJornada(jornada);
                        jornada.getPartidos().add(partido);
                    }
                    jornada = new JornadaFutbol8();
                    jornada.setCompeticion(comp);
                    jornada.setNumero(numJor + 1);
                    jornada.setDescripcion(des.concat("(vuelta)"));
                    jornada.setPartidos(new ArrayList<PartidoFutbol8>());
                    comp.getJornadas().add(jornada);
                    for (int i = 1; i <= numPartidos; i++) {
                        PartidoFutbol8 partido = new PartidoFutbol8("Copa");
                        partido.setJornada(jornada);
                        jornada.getPartidos().add(partido);
                    }
                }
                else{
                    jornada = new JornadaFutbol8();
                    jornada.setCompeticion(comp);
                    jornada.setNumero(numJor);
                    jornada.setDescripcion(des);
                    jornada.setPartidos(new ArrayList<PartidoFutbol8>());
                    comp.getJornadas().add(jornada);
                    PartidoFutbol8 partido = new PartidoFutbol8("Copa");
                    partido.setJornada(jornada);
                    jornada.getPartidos().add(partido);
                }
                numJor = numJor + 2;
                    
            }

        }

        return comp;

    }
    
        
    public void crearSigienteRondaDeCopa(
            ArrayList<EquipoFutbol8> eqs, int numProxJor){
  
        JornadaFutbol8 proxJor = this.getJornadabyNumero(numProxJor);
        
        if (eqs.size() == 2){
            // es la final
            PartidoFutbol8 partido = proxJor.getPartidos().get(0);
            partido.setEqLocal(eqs.get(0));
            partido.setEqVisitante(eqs.get(1));
            partido.setDescripcion(partido.getNombreEqLocal() + " - " + 
                    partido.getNombreEqVisitante());
            asignarAlineaciones(partido);
        }
        else{
            // ida
            for (int i = 0; i < eqs.size(); i = i + 2){
                PartidoFutbol8 partido = proxJor.getPartidos().get(i / 2);
                partido.setEqLocal(eqs.get(i));
                partido.setEqVisitante(eqs.get(i + 1));
                partido.setDescripcion(partido.getNombreEqLocal() + " - " + 
                        partido.getNombreEqVisitante());
                asignarAlineaciones(partido);
            }            
            // vuelta
            proxJor = this.getJornadabyNumero(numProxJor + 1);
            for (int i = 0; i < eqs.size(); i = i + 2){
                PartidoFutbol8 partido = proxJor.getPartidos().get(i / 2);
                partido.setEqLocal(eqs.get(i + 1));
                partido.setEqVisitante(eqs.get(i));
                partido.setDescripcion(partido.getNombreEqLocal() + " - " + 
                        partido.getNombreEqVisitante());
                asignarAlineaciones(partido);
            }        
        } 
        
    }
    
   
    
    
    private static void asignarAlineaciones(PartidoFutbol8 partido){
        
        if (partido.getEqLocal() != null && partido.getEqVisitante() != null ){
            ArrayList<AlineacionFutbol8> alineaciones =
                    new ArrayList<AlineacionFutbol8>();
            alineaciones.add(
                    new AlineacionFutbol8((EquipoFutbol8) partido.getEqLocal(), partido));
            
            alineaciones.add(
                    new AlineacionFutbol8((EquipoFutbol8) partido.getEqVisitante(),partido));
            partido.setAlineaciones(alineaciones);
        }

    }

    public void finalizarLiga(ArrayList<EquipoFutbol8> eqs)  {
        //  realiza las acciones de finalizar la competicion
        
        this.setActiva(false);
        this.setCampeon(eqs.get(0).getNombre());
        this.setSubcampeon(eqs.get(1).getNombre());
        
        // puntos para ranking
        int puntos = 50 * eqs.size();
        int ptsAct, ptsNew;
        int pos = 1;
        for (EquipoFutbol8 eq : eqs) {
            Club club = eq.getClub();
            ptsAct = club.getRanking();
            ptsNew = puntos / pos;
            club.setRanking(ptsAct / 2 + ptsNew);
            pos++;    
        }
        
    }

    public void finalizarCopa(PartidoFutbol8 partido) {
        
        this.setActiva(false);
        this.setCampeon(partido.getGanador().getNombre());
        this.setSubcampeon(partido.getPerdedor().getNombre());
        EquipoFutbol8 eqWin= partido.getGanador();
        eqWin.setPublicidad(eqWin.getPublicidad() + 1);
        
        // Ranking
        Club club = partido.getGanador().getClub();
        int ptsAct = club.getRanking();
        club.setRanking(ptsAct + 200);
        club = partido.getPerdedor().getClub();
        ptsAct = club.getRanking();
        club.setRanking(ptsAct + 100);
        
        
    }

    public String getMaximosGoleadores() {
        return maximosGoleadores;
    }

    public void setMaximosGoleadores(String maximosGoleadores) {
        this.maximosGoleadores = maximosGoleadores;
    }

    public int getRecaudacion() {
        return recaudacion;
    }

    public void setRecaudacion(int recaudacion) {
        this.recaudacion = recaudacion;
    }
   


}
