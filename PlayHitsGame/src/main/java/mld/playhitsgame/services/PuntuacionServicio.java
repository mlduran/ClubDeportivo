/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import mld.playhitsgame.exemplars.Puntuacion;

/**
 *
 * @author miguel
 */
public interface PuntuacionServicio {

    List<Puntuacion> obtenerPuntuacionesPersonales(Long tema_id);
    Puntuacion save(Puntuacion puntuacion); 
 
}
