/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.exemplars;

import mld.playhitsgame.seguridad.Roles;

/**
 *
 * @author miguel
 */
public class FiltroUsuarios {
    
    private boolean activo;
    private Roles rol;
    
        public FiltroUsuarios(){        

        this.activo = true;
        this.rol = null;
    }
   
    

    public String filtroSQL(){
        
        String sql = "";    
        sql = sql.concat("activo = ").concat(this.activoTxt());
        return sql;
        
    }

    /**
     * @return the activo
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * @param activo the activo to set
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * @return the rol
     */
    public Roles getRol() {
        return rol;
    }

    /**
     * @param rol the rol to set
     */
    public void setRol(Roles rol) {
        this.rol = rol;
    }
    
     public String activoTxt() {
        if (this.isActivo())
            return "true";
        else
            return "false";
    }
    
    
}
