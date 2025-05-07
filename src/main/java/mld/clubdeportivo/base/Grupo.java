
package mld.clubdeportivo.base;

import static java.lang.String.format;
import java.util.ArrayList;
import java.util.List;
import mld.clubdeportivo.utilidades.*;
import static mld.clubdeportivo.utilidades.StringUtil.isNullOrEmpty;
import static mld.clubdeportivo.utilidades.StringUtil.removeCharsEspeciales;
import static mld.clubdeportivo.utilidades.UtilGenericas.isGrupo;

public final class Grupo extends Objeto implements Comparable<Grupo>{
    
    public static final long CODIGO_GRUPO_QUINIELA = 9999999;
    
    private boolean activo;
    private String nombre;
    private int maxEquipos;
    private boolean completo;
    private boolean privado;
    private int codigo;
    private boolean generico;
    private String ip;
    private List<Club> clubs;

    public Grupo(String nombre, int maxEquipos,
            boolean privado, boolean generico, List<Club> clubs){

        this.setActivo(true);
        this.setCompleto(false);
        this.setGenerico(generico);
        this.setMaxEquipos(maxEquipos);
        this.setNombre(nombre);
        this.setPrivado(privado);
        this.setClubs(clubs);

    }

    public Grupo(String nombre, boolean privado, List<Club> clubs){

        this.setActivo(true);
        this.setCompleto(false);
        this.setGenerico(false);
        this.setMaxEquipos(16);
        this.setNombre(nombre);
        this.setPrivado(privado);
        this.setClubs(clubs);

    }

    public Grupo() {

    }

    public Grupo grupoQuiniela() {

        var grp = new Grupo();
        grp.setActivo(true);
        grp.setCompleto(false);
        grp.setGenerico(true);
        grp.setId(CODIGO_GRUPO_QUINIELA);
        grp.setMaxEquipos(9999999);
        grp.setNombre("GrupoQ");
        grp.setPrivado(false);

        return grp;

    }
    
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {

        if (isNullOrEmpty(nombre))
            throw new NullPointerException("Nombre obligatorio");

        nombre = nombre.toUpperCase();

        if (!isGrupo(nombre))
            throw new IllegalArgumentException("Formato Nombre Incorrecto");

        this.nombre = removeCharsEspeciales(nombre);
    }


    public boolean isCompleto() {

        return completo;
    }

    public void setCompleto(boolean completo) {
        this.completo = completo;
    }

    public boolean isPrivado() {
        return privado;
    }

    public void setPrivado(boolean privado) {
        this.privado = privado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public boolean isGenerico() {
        return generico;
    }

    public void setGenerico(boolean generico) {
        this.generico = generico;
    }

    public int getMaxEquipos() {
        return maxEquipos;
    }

    public void setMaxEquipos(int maxEquipos) {

        if (maxEquipos < 1)
            throw new IllegalArgumentException("Numero equipos incorrecto");

        this.maxEquipos = maxEquipos;
    }

    public List<Club> getClubs() {
        return clubs;
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }


    @Override
    public String toString() {

        var txt = new StringBuilder();

        txt.append("GRUPO\n");
        txt.append(format("- Id: %d \n", this.getId()));
        txt.append(format("- Nombre: %s \n", this.getNombre()));
        txt.append(format("- Max Equipos: %d \n", this.getMaxEquipos()));
        txt.append(format("- Completo: %b \n", this.isCompleto()));
        txt.append(format("- Generico: %b \n", this.isGenerico()));
        txt.append(format("- Privado: %b \n", this.isPrivado()));
        txt.append(format("- Activo: %b \n", this.isActivo()));

        return txt.toString();
    }

    public boolean equals(Grupo obj) {

        return  obj.getId() == this.getId();
    }

    public int compareTo(Grupo o) {

         if (o == null)
            throw new NullPointerException("Referencia nula");

        return this.getNombre().compareTo(o.getNombre());

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

  
    


}
