/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import mld.playhitsgame.exemplars.SearchSpecifications;
import mld.playhitsgame.exemplars.SearchCriteria;
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
    
    @Query(value = "SELECT * FROM canciones WHERE genero=:genero AND pais=:pais AND anyo>=:anyoinicial AND anyo<=:anyofinal AND revisar=:revisar ;", nativeQuery = true)
    List<Cancion> findByFiltroSinTema(String genero, String pais, int anyoinicial, int anyofinal, boolean revisar);
    
    @Query(value = "SELECT * FROM canciones WHERE tema=:tema AND anyo>=:anyoinicial AND anyo<=:anyofinal AND revisar=:revisar ;", nativeQuery = true)
    List<Cancion> findByFiltroConTema(String tema, int anyoinicial, int anyofinal, boolean revisar);

    @Query(value = "SELECT * FROM canciones WHERE revisar=false ;", nativeQuery = true)
    List<Cancion> finBySinRevisar();
   
    
    
    
    
    
}