/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CANJaguar.ControlMode;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class AIMBot extends IterativeRobot {
    
    long timeLastPressedDeploy;
    Joystick joystick1;
    Joystick joystick2;
    DigitalOutput ShooterDO1;
    DigitalOutput ShooterDO2;
    DigitalOutput DeployDO;
    CANJaguar elevatorJaguar;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
            joystick1 = new Joystick(1);
            joystick2 = new Joystick(2);
            ShooterDO1 = new DigitalOutput(1);
            ShooterDO2 = new DigitalOutput(2);
            DeployDO = new DigitalOutput(3);
        try {
            elevatorJaguar = new CANJaguar(1);
            elevatorJaguar.changeControlMode(ControlMode.kVoltage);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

        /**
         * Shoot
         * If the trigger is being held down, the turn on the shooter wheel
         */
        if (joystick1.getTrigger()) {
            ShooterDO1.set(true);
            ShooterDO2.set(true);
        }
        
        /**
         * Deploy Frisbee
         * If Button 2 is held down, then start the motor
         */
        if (joystick1.getRawButton(2)) {
            DeployDO.set(true);
            timeLastPressedDeploy = System.currentTimeMillis();
        }
        
        /**
         * Turn off Frisbee Deploy
         * If it's been more than 2 seconds then turn it off.
         */
        if (System.currentTimeMillis() - timeLastPressedDeploy > 2000) {
            DeployDO.set(false);
        }
        
        /**
         * Move Escalator Down
         * If the 3 Button is held down, move the escalator down.
         */
        if (joystick1.getRawButton(3)) {
            try {
                elevatorJaguar.setX(-1);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
        
        /**
         * Move Escalator Up
         * If the 5 Button is held down, move the escalator up.
         */
        if (joystick1.getRawButton(5)) {
            try {
                elevatorJaguar.setX(1);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }    
}