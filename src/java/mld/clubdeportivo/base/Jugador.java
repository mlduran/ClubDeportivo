package mld.clubdeportivo.base;


public abstract class Jugador extends Objeto
        implements Comparable<Jugador>{

    public static final int JORNADAS_CONTRATO = 40;
    public static int NUMERO_MAX_JUGADORES_SUBASTA = 5;
    
    private String nombre;
    private int ficha;
    private int clausula;
    private boolean blindado;
    private int jornadasLesion;
    private boolean transferible;
    private int contrato;
    private Grupo grupo;
    private boolean enSubasta;
    private int puja;
    private long equipoPuja;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String jugador) {
        this.nombre = jugador;
    }

    public int getFicha() {
        return ficha;
    }

    public void setFicha(int ficha) {
        
        if (ficha >= 10)
            this.ficha = ficha;
    }

    public int getClausula() {
        return clausula;
    }

    public void setClausula(int clausula) {
        if (clausula >= 400)
            this.clausula = clausula;
    }

    public boolean isBlindado() {
        return blindado;
    }

    public void setBlindado(boolean blindado) {
        if (!this.isEnSubasta())
            this.blindado = blindado;
    }

    public int getJornadasLesion() {
        return jornadasLesion;
    }

    public void setJornadasLesion(int jornadasLesion) {
        this.jornadasLesion = jornadasLesion;
    }

    public boolean isTransferible() {
        return transferible;
    }

    public void setTransferible(boolean tranferible) {
        if (!this.isEnSubasta())
            this.transferible = tranferible;
    }

    public int getContrato() {
        return contrato;
    }

    public void setContrato(int contrato) {
        this.contrato = contrato;
    }
    
    public String getColorContrato(){

        if (this.getContrato() <= 5)
            return "B40431";
        else
            return "F7F8E0";
    }

    public static String colorFondoCelda(int valor){

        String color = "FFFFFF";

        if (valor < 20) color ="EDFAEA";
        else if(valor < 30) color = "ECF6CE";
        else if(valor < 40) color = "F2F5A9";
        else if(valor < 50) color = "F5DA81";
        else if(valor < 60) color = "F7A6A6";
        else if(valor < 70) color = "D2659F";
        else if(valor < 80) color = "C15151";
        else if(valor < 90) color = "992626";
        else if(valor >= 90) color = "770202";

        return color;
    }

    public boolean equals(Jugador obj) {

        return obj.getId() == this.getId();
    }

    public int compareTo(Jugador o) {

         if (o == null)
            throw new NullPointerException("Referencia nula");

        return this.getNombre().compareTo(o.getNombre());

    }

    public boolean isEnSubasta() {
        return enSubasta;
    }

    public void setEnSubasta(boolean enSubasta) {
        this.enSubasta = enSubasta;
    }
    
    public void bajarSubasta(boolean isLibre){
        this.setClausula(this.getClausula() - (int) (this.getClausula() * 0.1));
        if (isLibre)
            this.setFicha(this.getFicha() - (int) (this.getFicha() * 0.1));
    }

    public int getPuja() {
        return puja;
    }

    public void setPuja(int puja) {
        this.puja = puja;
    }

    public long getEquipoPuja() {
        return equipoPuja;
    }

    public void setEquipoPuja(long equipoPuja) {
        this.equipoPuja = equipoPuja;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

  
   




}
