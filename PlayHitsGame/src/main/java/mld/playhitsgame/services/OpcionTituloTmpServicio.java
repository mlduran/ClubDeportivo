/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import mld.playhitsgame.exemplars.OpcionTituloTmp;



/**
 *
 * @author miguel
 */
public interface OpcionTituloTmpServicio {
    
    List<OpcionTituloTmp> findByPartidaRonda(Long partida, Long ronda);  

    OpcionTituloTmp saveOpcionTituloTmp(OpcionTituloTmp opcionTituloTmp);    
    void deleteOpcionTituloTmp(Long id);
    
}
