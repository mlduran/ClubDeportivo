/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;

import java.util.ArrayList;
import java.util.Optional;
import mld.playhitsgame.exemplars.Batalla;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author miguel
 */
public interface BatallaDAO extends JpaRepository<Batalla, Long> {

    @Override
    Optional<Batalla> findById(Long id);
    
    @Query(value = "SELECT * FROM batallas WHERE status <> 'Terminada' AND status <> 'Historico' ;", nativeQuery = true)
    ArrayList<Batalla> batallasActuales();
    
    @Query(value = "SELECT * FROM batallas WHERE status = 'Terminada' ;", nativeQuery = true)
    ArrayList<Batalla> batallasFinalizadas();

    @Query(value = "SELECT * FROM batallas WHERE status='Programada'AND publica=true ;", nativeQuery = true)
    ArrayList<Batalla> batallasProgramadasPublicas();

    @Query(value = "SELECT * FROM batallas WHERE status='Inscripcion'AND publica=true ;", nativeQuery = true)
    ArrayList<Batalla> batallasEnInscripcionPublicas();

    @Query(value = "SELECT b.*"
            + "FROM batallas b"
            + "INNER JOIN usuarios u ON b.id_usuario = u.id"
            + "WHERE u.id = :id_usuario;", nativeQuery = true)
    ArrayList<Batalla> batallasUsuario(Long id_usuario);

}
