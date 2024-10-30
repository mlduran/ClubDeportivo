/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mld.playhitsgame.services;

import mld.playhitsgame.exemplars.Config;

/**
 *
 * @author miguel
 */
public interface ConfigServicio {
    
    public Config getSettings();
    public Config saveSettings(Config newSettings);

}
