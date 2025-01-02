/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;

import mld.playhitsgame.exemplars.Estrella;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author miguel
 */
public interface EstrellaDAO extends JpaRepository<Estrella, Long> {

    @Override
    Estrella save(Estrella estrella);

    @Query("SELECT e FROM Estrella e WHERE e.fecha = (SELECT MIN(e2.fecha) FROM Estrella e2)")
    Estrella fechaMasAntigua();

}
