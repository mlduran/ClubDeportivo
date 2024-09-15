/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import mld.playhitsgame.exemplars.OpcionInterpreteTmp;

/**
 *
 * @author miguel
 */
public interface OpcionInterpreteTmpServicio {

    List<OpcionInterpreteTmp> findByPartidaRonda(Long partida, Long ronda);

    void deleteByPartida(Long partida);

    OpcionInterpreteTmp saveOpcionInterpreteTmp(OpcionInterpreteTmp opcionInterpreteTmp);

    void deleteOpcionInterpreteTmp(Long id);

}
