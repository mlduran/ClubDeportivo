/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.Cancion;
import mld.playhitsgame.projections.ampliada.CancionAmpliadaView;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 *
 * @author miguel
 */
//@Transactional(readOnly = true)
public interface CancionDAO extends JpaRepository<Cancion, Long>{
    
    @Override
    List<Cancion> findAll();
    //List<Cancion> findAll(SearchSpecifications searchSpecifications);
    List<CancionAmpliadaView> findBy();
    
    //@Query("SELECT * FROM cancion WHERE id = :id")
    //Optional<Cancion> findCancionById(long id);
    
    @Override
    Optional<Cancion> findById(Long id); 
    
    @Query(value = "SELECT * FROM canciones WHERE spotifyid=:id ;", nativeQuery = true)
    Optional<Cancion> findByIdSpotify(String id);
    
    @Query(value = "SELECT * FROM canciones WHERE anyo>=:anyoinicial AND anyo<=:anyofinal AND revisar=:revisar ;", nativeQuery = true)
    List<Cancion> findByFiltroBasico(int anyoinicial, int anyofinal, boolean revisar);
    
    @Query(value = "SELECT canciones.*, temas.tema FROM canciones INNER JOIN canciones_tematicas ON canciones.id = canciones_tematicas.canciones_id INNER JOIN temas ON temas.id = canciones_tematicas.tematicas_id WHERE tema=:tema AND anyo>=:anyoinicial AND anyo<=:anyofinal AND revisar=:revisar ;", nativeQuery = true)
    List<Cancion> findByFiltroConTema(String tema, int anyoinicial, int anyofinal, boolean revisar);

    @Query(value = "SELECT * FROM canciones WHERE revisar=false ;", nativeQuery = true)
    List<Cancion> finBySinRevisar();
   
    
    
    
    
    
}