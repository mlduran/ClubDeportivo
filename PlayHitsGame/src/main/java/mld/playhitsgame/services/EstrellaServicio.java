/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import mld.playhitsgame.exemplars.Estrella;

/**
 *
 * @author miguel
 */
public interface EstrellaServicio {

    Estrella update(Long id, Estrella estrella);
    Estrella save(Estrella estrella);

}
