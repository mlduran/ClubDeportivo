/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import mld.playhitsgame.DAO.OpcionTituloDAO;
import mld.playhitsgame.exemplars.OpcionTituloTmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OpcionTituloTmpServicioMetodos implements OpcionTituloTmpServicio {

    @Autowired
    OpcionTituloDAO DAO;

    @Override
    public List<OpcionTituloTmp> findByPartidaRonda(Long partida, Long ronda) {
        return DAO.findByPartidaRonda(partida, ronda);
    }

    @Override
    public void deleteByPartida(Long partida) {
        DAO.deleteByPartida(partida);
    }

    @Override
    public OpcionTituloTmp saveOpcionTituloTmp(OpcionTituloTmp opcionTituloTmp) {
        return DAO.save(opcionTituloTmp);
    }

    @Override
    public void deleteOpcionTituloTmp(Long id) {
        DAO.deleteById(id);
    }

}
