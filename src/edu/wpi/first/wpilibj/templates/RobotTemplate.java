/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {
    
    Shoot shoot = new Shoot();
    Drive drive = new Drive();
    Autonomous auto = new Autonomous();
    Camera camera = new Camera();
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        drive.initialize();
        System.out.println("[AIMBot] Drive motors initialized");
        shoot.initialize();
        System.out.println("[AIMBot] Shooter motors initialized");
        camera.initialize();
        System.out.println("[AIMBot] Camera initialized");
//        shoot.ShooterDO1 = new DigitalOutput(4, 1);
//        System.out.println("[AIMBot] Initialized Spike #1");
//        shoot.ShooterDO2 = new DigitalOutput(4, 2);
//        System.out.println("[AIMBot] Initialized Spike #2");
        //shoot.initialize();
        //camera.initialize();
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        auto.startAuto();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        drive.drive();
        System.out.println("[Robot Template] About to shoot");
        shoot.startShooting();
    }    
}