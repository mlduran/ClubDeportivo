/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.Date;
import mld.playhitsgame.DAO.RegistroDAO;
import mld.playhitsgame.exemplars.Registro;
import mld.playhitsgame.exemplars.TipoRegistro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RegistroServicioMetodos implements RegistroServicio {

    @Autowired
    RegistroDAO DAO;   

    @Override
    public Registro save(Registro registro) {
        return DAO.save(registro);
    }
    
    public Registro registrar(TipoRegistro tipo, String ip){
        return registrar(tipo, ip, "");
    }
    
    public Registro registrar(TipoRegistro tipo, String ip, String usuario){
        
        Date currentDate = new Date();
        
        Registro newRegistro = new Registro();
        newRegistro.setFecha(currentDate);
        newRegistro.setIp(ip);
        newRegistro.setTipo(tipo);
        newRegistro.setUsuario(usuario);
        newRegistro.setPais(newRegistro.getPaisServicio(ip));
        return save(newRegistro);
        
    }
    
    public Page<Registro> obtenerRegistrosPaginados(int pagina, int tamano) {
        return DAO.findAll(PageRequest.of(pagina, 
                tamano, Sort.by("fecha").descending()));
    }

}
