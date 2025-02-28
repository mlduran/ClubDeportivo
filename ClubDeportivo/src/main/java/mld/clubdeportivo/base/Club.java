
package mld.clubdeportivo.base;

import static java.lang.Integer.valueOf;
import static java.lang.Integer.valueOf;
import static java.lang.String.format;
import java.text.DateFormat;
import static java.text.DateFormat.SHORT;
import static java.text.DateFormat.getDateInstance;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import static mld.clubdeportivo.base.Deporte.Basket;
import static mld.clubdeportivo.base.Deporte.Futbol8;
import static mld.clubdeportivo.base.Deporte.Quiniela;
import mld.clubdeportivo.utilidades.CalendarFunctions;
import static mld.clubdeportivo.utilidades.CalendarFunctions.restarFechas;
import mld.clubdeportivo.utilidades.Seguridad;
import static mld.clubdeportivo.utilidades.Seguridad.SHA1Digest;
import static mld.clubdeportivo.utilidades.Seguridad.isSHA1Digest;
import mld.clubdeportivo.utilidades.StringUtil;
import static mld.clubdeportivo.utilidades.StringUtil.isNullOrEmpty;
import static mld.clubdeportivo.utilidades.StringUtil.removeCharsEspeciales;
import mld.clubdeportivo.utilidades.UtilGenericas;
import static mld.clubdeportivo.utilidades.UtilGenericas.isClub;
import static mld.clubdeportivo.utilidades.UtilGenericas.isMail;
import static mld.clubdeportivo.utilidades.UtilGenericas.isPassword;
import static mld.clubdeportivo.utilidades.UtilGenericas.isUsername;

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
    private boolean auto;
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
        this.setAuto(false);
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

        if (isNullOrEmpty(nombre))
            throw new NullPointerException("Nombre obligatorio");

        if (!isClub(nombre))
            throw new IllegalArgumentException("Formato Nombre Incorrecto");

        this.nombre = removeCharsEspeciales(nombre);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {

        if (isNullOrEmpty(usuario))
            throw new NullPointerException("Usuario obligatorio");

        if (!isUsername(usuario))
            throw new IllegalArgumentException("Formato Usuario Incorrecto");

        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        if (isNullOrEmpty(password))
            throw new NullPointerException("Contraseña obligatoria");

        if (isSHA1Digest(password) &&
                this.getId() != UNSAVED_VALUE) {
            this.password = password;
        }
        else {
            if (!isPassword(password))
                throw new IllegalArgumentException("El password a asignar "
                        + "al usuario no tiene formato válido");

            this.password = SHA1Digest(password);
        }

    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {

        if (!mail.equals("") && !isMail(mail))
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

        return this.getDeportes().contains(Futbol8);

    }

    public boolean isBasket(){

        return this.getDeportes().contains(Basket);

    }

    public boolean isQuiniela(){

        return this.getDeportes().contains(Quiniela);

    }

    public ArrayList<Deporte> getDeportes() {
       
            return deportes;
    }

    public void setDeportes(ArrayList<Deporte> deportes) {
        this.deportes = deportes;
    }
    
    public int getDiasSinAcceder(){
        
         int numDiasSinAcceder;
         var hoy = new Date();
         var calHoy = new GregorianCalendar();
         calHoy.setTime(hoy);
         var calUtimAcc = new GregorianCalendar();      
         
         calUtimAcc.setTime(this.getUltimoAcceso());
         numDiasSinAcceder = restarFechas(calUtimAcc, calHoy);
         
         return numDiasSinAcceder;
         
    }
    
   
    @Override
    public String toString() {

        var txt = new StringBuilder();

        txt.append("CLUB\n");
        txt.append(format("- Id: %d \n", this.getId()));
        txt.append(format("- Nombre: %s \n", this.getNombre()));
        txt.append(format("- Ranking: %d \n", this.getRanking()));
        txt.append(format("- Usuario: %s \n", this.getUsuario()));
        txt.append(format("- Contraseña: %s \n", this.getPassword()));
        txt.append(format("- Mail: %s \n", this.getMail()));
        var ff = getDateInstance(SHORT);
        txt.append(format("- Fecha Fundación: %s \n",
                ff.format(this.getFundacion())));
        txt.append(format("- Fecha Ultimo Acceso: %s \n",
                ff.format(this.getUltimoAcceso())));
        txt.append(format("- Activo: %b \n", this.isActivo()));

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

  
    public boolean isAuto() {
        return auto;
    }

      public void setAuto(boolean auto) {
        this.auto = auto;
    }

  

    private static class RankingComparator implements Comparator<Club> {

        public int compare(Club o1, Club o2) {

            if (o1 == null || o2 == null)
                throw new NullPointerException("Referencia nula");

            return valueOf(o1.getRanking()).compareTo(o2.getRanking());
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
