/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Andrew Peace
 */
public class Autonomous {

    Shoot shoot = new Shoot();
    Drive drive = new Drive();
    boolean discDeployed = false;
    boolean deployerRetracted = false;
    boolean hasStarted = false;
    long wheelStarted = 0;
    long timeLastShot = 0;

    /**
     * Logic
     *
     * 1. Turn on shooter wheel 2. Deploy Disc 3. Wait 2 seconds 4. Deploy 5.
     * Wait 6. Deploy
     */
    public void startAuto() {
//                Timer driveTimer = new Timer();
//        Timer deployTimer = new Timer();
//        driveTimer.start();
//        
//        while (driveTimer.get() < 2000) {
//            try {
//            Drive.JaguarRF.setX(12);
//            Drive.JaguarLF.setX(12);
//            Drive.JaguarRR.setX(12);
//            Drive.JaguarLR.setX(12);
//            } catch (CANTimeoutException e) {
//                e.printStackTrace();
//            }

        try {
            shoot.Shooter1.setX(12);
            shoot.Shooter2.setX(12);
            if (!hasStarted) {
                hasStarted = true;
                wheelStarted = System.currentTimeMillis();
            }
            if ((System.currentTimeMillis() - wheelStarted > 5000) && (System.currentTimeMillis() - timeLastShot > 2000)) {
                deployDiscOut();
                timeLastShot = System.currentTimeMillis();
            }
        } catch (Exception npe) {
            npe.printStackTrace();
        }
    }

    public void deployDiscOut() {
        try {
            System.out.println("Forward is " + shoot.DiscDeployer.getForwardLimitOK() + "");
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            while (shoot.DiscDeployer.getForwardLimitOK()) {
                System.out.println("Forward is ok!");
                shoot.DiscDeployer.setX(12);
            }
            discDeployed = true;
            deployDiscIn();
            System.out.println("Pushed disc");
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }

    }

    public void deployDiscIn() {
        if (discDeployed) {
            try {
                while (shoot.DiscDeployer.getReverseLimitOK()) {
                    System.out.println("Reverse is ok!");
                    shoot.DiscDeployer.setX(-12);
                }
                deployerRetracted = true;
                System.out.println("Taking deployer out");
                stopDeploy();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void stopDeploy() {
        if (deployerRetracted) {
            discDeployed = false;
            deployerRetracted = false;
            try {
                shoot.DiscDeployer.setX(0);
                System.out.println("Stopped deploy");
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }
//        }
//        driveTimer.stop();
//        driveTimer.reset();
//        
//        shoot.ShooterDO1.set(true);
//        shoot.ShooterDO2.set(true);
//        deployTimer.start();
//        
//        while (deployTimer.get() < 2000) {
//           shoot.DeployDO.set(true);
//        }
//        shoot.endDeploy();
}