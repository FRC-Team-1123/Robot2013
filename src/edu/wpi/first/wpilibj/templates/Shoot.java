package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CANJaguar.ControlMode;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 * @author Andrew Peace ;o
 */
public class Shoot {

    long timeLastPressedDeploy;
    Joystick joystick2;
    static CANJaguar elevatorJaguar;
    static CANJaguar DiscDeployer;
    static CANJaguar Shooter1;
    static CANJaguar Shooter2;
    boolean discDeployed = false;
    boolean deployerRetracted = false;
    boolean ShooterIsOn = false;
    Drive drive = new Drive();

    public void startShooting() {
        reinitializeJaguars();
        forceDeployerOut();
        turnOnShooterWheel();
        turnOffShooterWheel();
        lowerElevator();
        reverseShooter();
        System.out.println("[Shoot Class] lowerElevator()");
        try {
            raiseElevator();
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        System.out.println("[Shoot Class] raiseElevator()");
        stopElevator();
        deployDiscOut();
    }

    /**
     * TO DO
     */
    public void initialize() {
        joystick2 = new Joystick(2);
         //ShooterDO1 = new DigitalOutput(1);
//        System.out.println("[AIMBot] Digital Output " + ShooterDO1.getChannel());
//        System.out.println("[AIMBot] Initialized Shooter 1");
//       // ShooterDO2 = new DigitalOutput(2);
//        System.out.println("[AIMBot] Digital Output " + ShooterDO2.getChannel());
//        System.out.println("[AIMBot] Initialized Shooter 2");
        try {
            elevatorJaguar = new CANJaguar(5, ControlMode.kVoltage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            DiscDeployer = new CANJaguar(4, ControlMode.kVoltage);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            Shooter1 = new CANJaguar(6, ControlMode.kVoltage);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            Shooter2 = new CANJaguar(2, ControlMode.kVoltage);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

//    public void displayAngle() {
//        System.out.println("The Angle Voltage is " + AngleEncoder.getVoltage());
//        System.out.println("The Average Angle Voltage is " + AngleEncoder.getAverageVoltage());
//    }

    public void turnOnShooterWheel() {
        if (joystick2.getRawButton(6)) {
            try {
                Shooter1.setX(12);
                Shooter2.setX(12);
                ShooterIsOn = true;
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
//            ShooterDO1.set(true);
//            ShooterDO2.set(true);
        }
    }

    public void turnOffShooterWheel() {
        if (joystick2.getRawButton(4)) {
            try {
                Shooter1.setX(0);
                Shooter2.setX(0);
                ShooterIsOn = false;
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
//            ShooterDO1.set(false);
//            ShooterDO2.set(false);
        }
    }

    public void forceDeployerOut() {
        if (joystick2.getRawButton(2)) {
            try {
                while (DiscDeployer.getReverseLimitOK()) {
                    System.out.println("Reverse is ok!");
                    DiscDeployer.setX(-12);
                }
                deployerRetracted = true;
                System.out.println("Taking deployer out");
                stopDeploy();
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void deployDiscOut() {
        if (joystick2.getTrigger() && ShooterIsOn) {
            System.out.println("Trigger is held!");
            try {
                System.out.println("Forward is " + DiscDeployer.getForwardLimitOK() + "");
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                while (DiscDeployer.getForwardLimitOK()) {
                    System.out.println("Forward is ok!");
                    if (Math.abs(DiscDeployer.getOutputCurrent()) > 15) {
                        DiscDeployer.setX(-12);

                    } else {
                        DiscDeployer.setX(12);
                    }
                }
                discDeployed = true;
                deployDiscIn();
                System.out.println("Pushed disc");
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void deployDiscIn() {
        if (discDeployed) {
            try {
                while (DiscDeployer.getReverseLimitOK()) {
                    System.out.println("Reverse is ok!");
                    if (Math.abs(DiscDeployer.getOutputCurrent()) > 15) {
                        stopDeploy();
                    } else {
                        DiscDeployer.setX(-12);
                    }
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
                DiscDeployer.setX(0);
                System.out.println("Stopped deploy");
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }

//    public void shoot() {
//        if (joystick2.getTrigger()) {
//            ShooterDO1.set(true);
//            ShooterDO2.set(true);
//        }
//    }

    /*
     * TO DO
     */
//    public void endDeploy() {
//        if (System.currentTimeMillis() - timeLastPressedDeploy > 2000) {
//            //DeployDO.set(false);
//        }
//    }
    public void lowerElevator() {
        try {
            System.out.println("Forward is " + elevatorJaguar.getForwardLimitOK() + "");
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            if (joystick2.getRawButton(3) && elevatorJaguar.getForwardLimitOK()) {
                try {
                    elevatorJaguar.setX(12);
                    System.out.println("[Shooter] Lowering elevator");
                } catch (CANTimeoutException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }

    public void raiseElevator() throws CANTimeoutException {
        System.out.println("Reverse is " + elevatorJaguar.getReverseLimitOK() + "");
        if (joystick2.getRawButton(5) && elevatorJaguar.getReverseLimitOK()) {
            try {
                elevatorJaguar.setX(-12);
                System.out.println("[Shooter] Raising elevator");
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void stopElevator() {
        if (!joystick2.getRawButton(5) && !joystick2.getRawButton(3)) {
            try {
                elevatorJaguar.setX(0);
                System.out.println("[Shooter] Turned off elevator");
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void reinitializeJaguars() {
        if (joystick2.getRawButton(12)) {
            this.initialize();
            drive.initialize();
        }
    }

    public void reverseShooter() {
        if (joystick2.getRawButton(10)) {
            try {
                Shooter1.setX(-12);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
            try {
                Shooter2.setX(-12);
            } catch (CANTimeoutException ex) {
                ex.printStackTrace();
            }
        }
    }
}
