/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mld.playhitsgame.DAO;


import java.util.List;
import java.util.Optional;
import mld.playhitsgame.exemplars.CancionTmp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author miguel
 */
//@Transactional(readOnly = true)
public interface CancionTmpDAO extends JpaRepository<CancionTmp, Long>{
    
    @Override
    List<CancionTmp> findAll();
    
    @Override
    Optional<CancionTmp> findById(Long id); 
    
    @Query(value = "SELECT * FROM canciones_tmp WHERE spotifyid=:id ;", nativeQuery = true)
    Optional<CancionTmp> findByIdSpotify(String id);
    
    @Query(value = "SELECT * FROM canciones_tmp WHERE anyo>=:anyoinicial AND anyo<=:anyofinal AND revisar=:revisar AND solo_temas=:solo_temas ;", nativeQuery = true)
    List<CancionTmp> findByFiltroBasico(int anyoinicial, int anyofinal, boolean revisar, boolean solo_temas);
    
    @Query(value = "SELECT canciones_tmp.*, temas.tema FROM canciones_tmp INNER JOIN canciones_tmp_tematicas ON canciones_tmp.id = canciones_tmp_tematicas.canciones_tmp_id INNER JOIN temas ON temas.id = canciones_tmp_tematicas.tematicas_id WHERE tema=:tema AND anyo>=:anyoinicial AND anyo<=:anyofinal AND revisar=:revisar AND solo_temas=:solo_temas ;", nativeQuery = true)
    List<CancionTmp> findByFiltroConTema(String tema, int anyoinicial, int anyofinal, boolean revisar, boolean solo_temas);

    @Query(value = "SELECT * FROM canciones_tmp WHERE revisar=false ;", nativeQuery = true)
    List<CancionTmp> finBySinRevisar();
   
    @Modifying   
    @Transactional
    @Query(value = "DELETE FROM canciones_tmp_tematicas WHERE canciones_tmp_id=:idCancionTmp ;", nativeQuery = true)
    void eliminarRelacionTemas(Long idCancionTmp);   
}