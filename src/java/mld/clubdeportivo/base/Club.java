
package mld.clubdeportivo.base;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import mld.clubdeportivo.utilidades.CalendarFunctions;
import mld.clubdeportivo.utilidades.Seguridad;
import mld.clubdeportivo.utilidades.StringUtil;
import mld.clubdeportivo.utilidades.UtilGenericas;

public final class Club extends Objeto implements Comparable<Club>{

    private boolean activo;
    private int ranking;
    private String nombre;
    private String usuario;
    private String password;
    private String mail;
    private Date fundacion;
    private Date ultimoAcceso;
    private Grupo grupo;
    private long idGrupo;
    private ArrayList<Deporte> deportes;
    private int titulosLigaFutbol8;
    private int titulosCopaFutbol8;
    private int titulosQuiniela;
    

    public Club(Grupo grupo, String nombre, String usuario, String contrasenya,
            String mail ) {

        this.setNombre(nombre);
        this.setUsuario(usuario);
        this.setPassword(contrasenya);
        this.setMail(mail);
        this.setFundacion(new Date());
        this.setUltimoAcceso(new Date());
        this.setActivo(true);
        this.setRanking(0);
        this.setGrupo(grupo);
        this.setDeportes(new ArrayList());

    }

    public Club(Grupo grupo, String nombre, String usuario, String contrasenya) {

        this(grupo, nombre, usuario, contrasenya, "");

    }

    public Club() {

    }

   
    public String getNombre() {
        return nombre;
    }

    public String getNombreCorto() {
        return nombre.substring(0, 5);
    }

    public void setNombre(String nombre) {

        if (StringUtil.isNullOrEmpty(nombre))
            throw new NullPointerException("Nombre obligatorio");

        if (!UtilGenericas.isClub(nombre))
            throw new IllegalArgumentException("Formato Nombre Incorrecto");

        this.nombre = StringUtil.removeCharsEspeciales(nombre);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {

        if (StringUtil.isNullOrEmpty(usuario))
            throw new NullPointerException("Usuario obligatorio");

        if (!UtilGenericas.isUsername(usuario))
            throw new IllegalArgumentException("Formato Usuario Incorrecto");

        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        if (StringUtil.isNullOrEmpty(password))
            throw new NullPointerException("Contraseña obligatoria");

        if (Seguridad.isSHA1Digest(password) &&
                this.getId() != UNSAVED_VALUE) {
            this.password = password;
        }
        else {
            if (!UtilGenericas.isPassword(password))
                throw new IllegalArgumentException("El password a asignar "
                        + "al usuario no tiene formato válido");

            this.password = Seguridad.SHA1Digest(password);
        }

    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {

        if (!mail.equals("") && !UtilGenericas.isMail(mail))
            throw new IllegalArgumentException("Mail no valido");

        this.mail = mail;
    }

    public Date getFundacion() {
        return fundacion;
    }

    public void setFundacion(Date fundacion) {
        this.fundacion = fundacion;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        if (ranking < 0) ranking = 0;
        this.ranking = ranking;
    }

    public Date getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Date ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public boolean isFutbol8(){

        return this.getDeportes().contains(Deporte.Futbol8);

    }

    public boolean isBasket(){

        return this.getDeportes().contains(Deporte.Basket);

    }

    public boolean isQuiniela(){

        return this.getDeportes().contains(Deporte.Quiniela);

    }

    public ArrayList<Deporte> getDeportes() {
       
            return deportes;
    }

    public void setDeportes(ArrayList<Deporte> deportes) {
        this.deportes = deportes;
    }
    
    public int getDiasSinAcceder(){
        
         int numDiasSinAcceder;
         Date hoy = new Date();
         GregorianCalendar calHoy = new GregorianCalendar();
         calHoy.setTime(hoy);
         GregorianCalendar calUtimAcc = new GregorianCalendar();      
         
         calUtimAcc.setTime(this.getUltimoAcceso());
         numDiasSinAcceder = CalendarFunctions.restarFechas(calUtimAcc, calHoy);
         
         return numDiasSinAcceder;
         
    }
    
   
    @Override
    public String toString() {

        StringBuilder txt = new StringBuilder();

        txt.append("CLUB\n");
        txt.append(String.format("- Id: %d \n", this.getId()));
        txt.append(String.format("- Nombre: %s \n", this.getNombre()));
        txt.append(String.format("- Ranking: %d \n", this.getRanking()));
        txt.append(String.format("- Usuario: %s \n", this.getUsuario()));
        txt.append(String.format("- Contraseña: %s \n", this.getPassword()));
        txt.append(String.format("- Mail: %s \n", this.getMail()));
        DateFormat ff = DateFormat.getDateInstance(DateFormat.SHORT);
        txt.append(String.format("- Fecha Fundación: %s \n",
                ff.format(this.getFundacion())));
        txt.append(String.format("- Fecha Ultimo Acceso: %s \n",
                ff.format(this.getUltimoAcceso())));
        txt.append(String.format("- Activo: %b \n", this.isActivo()));

        return txt.toString();
    }

    public boolean equals(Club obj) {

        return  obj.getId() == this.getId();
    }

    public int compareTo(Club o) {

         if (o == null)
            throw new NullPointerException("Referencia nula");

        return this.getNombre().compareToIgnoreCase(o.getNombre());

    }

    public int getTitulosLigaFutbol8() {
        return titulosLigaFutbol8;
    }

    public void setTitulosLigaFutbol8(int titulosLigaFutbol8) {
        this.titulosLigaFutbol8 = titulosLigaFutbol8;
    }

    public int getTitulosCopaFutbol8() {
        return titulosCopaFutbol8;
    }

    public void setTitulosCopaFutbol8(int titulosCopaFutbol8) {
        this.titulosCopaFutbol8 = titulosCopaFutbol8;
    }

    public int getTitulosQuiniela() {
        return titulosQuiniela;
    }

    public void setTitulosQuiniela(int titulosQuiniela) {
        this.titulosQuiniela = titulosQuiniela;
    }

    public long getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(long idGrupo) {
        this.idGrupo = idGrupo;
    }

  

    private static class RankingComparator implements Comparator<Club> {

        public int compare(Club o1, Club o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");

            return Integer.valueOf(o1.getRanking()).compareTo(
                    Integer.valueOf(o2.getRanking()));
        }
    }

    public static Comparator<Club> getRankingComparator(){

        return new RankingComparator();

    }
       
    private static class FundacionComparator implements Comparator<Club> {

        public int compare(Club o1, Club o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");

            return o1.getFundacion().compareTo(o2.getFundacion());
        }
    }

    public static Comparator<Club> FundacionComparator(){

        return new FundacionComparator();

    }

}
