/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.camera.AxisCamera;

/**
 *
 * @author Andrew Peace
 */
public class Camera {
    
    AxisCamera camera;
    
    public void initialize() {
        camera = AxisCamera.getInstance("10.11.23.11");
    }   
}
