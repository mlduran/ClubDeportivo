/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.clubdeportivo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServletConfig {

    private String diasCumplimentacion;

    public ServletConfig(@Value("${custom.diascumplimentacion}") String valorInicial) {
        this.diasCumplimentacion = valorInicial;
    }

    public String getDiasCumplimentacion() {
        return diasCumplimentacion;
    }

    public void setDiasCumplimentacion(String diasCumplimentacion) {
        this.diasCumplimentacion = diasCumplimentacion;
    }
}

