package mld.clubdeportivo.base;

public abstract class Seccion extends Objeto{

    public static int[] PRECIOS_ENTRADAS = {5, 10, 15, 20, 25};
    
    public static int PRECIO_INICIAL = 5;
    private Deporte deporte;
    private boolean activo;    
    private Club club;
    private int precioEntradas;

    protected Seccion(){

    };

    public Deporte getDeporte() {
        return deporte;
    }

    public void setDeporte(Deporte deporte){
        this.deporte = deporte;
    }
    
    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }


    public Long obtenerIdClub(){

        return this.getClub().getId();
    }

    public String getNombre(){

        String nom;
        Club clb = this.getClub();
        if (clb != null)
            nom = this.getClub().getNombre();
        else
            nom = "ELIMINADO";
        
        return nom;
    }

    public String getNombreCorto(){

        String nom;
        Club clb = this.getClub();
        if (clb != null)
            nom = clb.getNombreCorto();
        else
            nom = "ELIM";
        
        return nom;

    }

    public int getPrecioEntradas() {
        return precioEntradas;
    }

    public void setPrecioEntradas(int precioEntradas) {
        
        boolean ok = false;
        
        for (int p : PRECIOS_ENTRADAS) 
            if (p == precioEntradas) ok = true;
        
        if (!ok) throw new IllegalArgumentException("El precio asignado no es correcto"); 
        
        this.precioEntradas = precioEntradas;
    }


}
