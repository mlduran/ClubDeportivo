/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import java.util.Optional;
import mld.playhitsgame.DAO.ConfigDAO;
import mld.playhitsgame.exemplars.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author miguel
 */

@Service
public class ConfigServicioMetodos implements ConfigServicio{
    
    @Autowired
    ConfigDAO DAO;

    @Override
    public Config getSettings() {
        Optional<Config> settings = DAO.findById(1L);
        return settings.orElseGet(this::createDefaultSettings);
    }

    @Override
    public Config saveSettings(Config newSettings) {
        // En lugar de crear un nuevo registro, actualizamos el existente
        Optional<Config> existingSettings = DAO.findById(1L);
        if (existingSettings.isPresent()) {
            Config settings = existingSettings.get();
            settings.setIpRouter(newSettings.getIpRouter());
            return DAO.save(settings);
        } else {
            // Si el registro no existe, lo creamos
            newSettings.setId(1L); // Fuerza el ID a 1 para mantenerlo único
            return DAO.save(newSettings);
        }
    }

    private Config createDefaultSettings() {
        Config settings = new Config();
        settings.setId(1L);
        settings.setIpRouter("");
        settings.setMantenimiento(false);
        return DAO.save(settings);
    }
    
}
