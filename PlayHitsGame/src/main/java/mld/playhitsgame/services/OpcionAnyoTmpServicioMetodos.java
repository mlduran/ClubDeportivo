/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import mld.playhitsgame.DAO.OpcionAnyoDAO;
import mld.playhitsgame.exemplars.OpcionAnyoTmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpcionAnyoTmpServicioMetodos implements OpcionAnyoTmpServicio {

    @Autowired
    OpcionAnyoDAO DAO;

    @Override
    public List<OpcionAnyoTmp> findByPartidaRonda(Long partida, Long ronda) {
        return DAO.findByPartidaRonda(partida, ronda);
    }

    @Override
    public List<OpcionAnyoTmp> deleteByPartida(Long partida) {
        return DAO.deleteByPartida(partida);
    }

    @Override
    public OpcionAnyoTmp saveOpcionAnyoTmp(OpcionAnyoTmp opcionAnyoTmp) {
        return DAO.save(opcionAnyoTmp);
    }

    @Override
    public void deleteOpcionAnyoTmp(Long id) {
        DAO.deleteById(id);
    }

}
