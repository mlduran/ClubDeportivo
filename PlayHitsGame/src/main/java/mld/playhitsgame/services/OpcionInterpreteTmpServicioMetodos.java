/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.List;
import mld.playhitsgame.DAO.OpcionInterpreteDAO;
import mld.playhitsgame.exemplars.OpcionInterpreteTmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpcionInterpreteTmpServicioMetodos implements OpcionInterpreteTmpServicio{
    
    @Autowired
    OpcionInterpreteDAO DAO;

    @Override
    public List<OpcionInterpreteTmp> findByPartidaRonda(Long partida, Long ronda) {
        return DAO.findByPartidaRonda(partida, ronda);
    } 
    
    @Override
    public void deleteByPartida(Long partida) {
        DAO.deleteByPartida(partida);
    } 
 
    @Override
    public OpcionInterpreteTmp saveOpcionInterpreteTmp(OpcionInterpreteTmp opcionInterpreteTmp) {
        return DAO.save(opcionInterpreteTmp);
    }

    @Override
    public void deleteOpcionInterpreteTmp(Long id) {
        DAO.deleteById(id);
    }    
 
}