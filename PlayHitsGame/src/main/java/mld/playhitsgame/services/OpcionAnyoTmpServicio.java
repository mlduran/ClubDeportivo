/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import mld.playhitsgame.exemplars.OpcionAnyoTmp;

/**
 *
 * @author miguel
 */
public interface OpcionAnyoTmpServicio {

    List<OpcionAnyoTmp> findByPartidaRonda(Long partida, Long ronda);
    
    void deleteByPartida(Long partida);

    OpcionAnyoTmp saveOpcionAnyoTmp(OpcionAnyoTmp opcionAnyoTmp);

    void deleteOpcionAnyoTmp(Long id);

}
