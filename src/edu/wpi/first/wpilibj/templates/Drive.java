/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CANJaguar.ControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author Andrew Peace && Christopher Fry
 */
public class Drive {

    static CANJaguar JaguarRF;
    static CANJaguar JaguarLF;
    static CANJaguar JaguarRR;
    static CANJaguar JaguarLR;
//    private double RF=0;
//    private double LF=0;
//    private double RR=0;
//    private double LR=0;
//    private double cosD=0;
//    private double sinD=0;
    /**
     * Jaguar IDs 2=Bridge 3=Elevator 4=Right Rear 5=Right Front 6=Shooter
     * 7=Intake Back 8=Left Front 9=Left Rear 10=Intake Right 11=Intake Front
     */
    /**
     * New Robot Jaguar IDs 8=Left Front 3=Right Front 7=Left Rear 6=Right Rear
     * 4=Shooter lifter
     */
    public final int JAGUAR_RF_ID = 3;
    public final int JAGUAR_LF_ID = 8;
    public final int JAGUAR_RR_ID = 9;
    public final int JAGUAR_LR_ID = 7;
    public final int DRIVE_ENCODER_CODES = 1024;
    private final byte SYNC_GROUP_DRIVE = 42;
    Joystick joystick;

    public void initialize() {
        try {
            JaguarRF = new CANJaguar(JAGUAR_RF_ID, ControlMode.kVoltage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            JaguarLF = new CANJaguar(JAGUAR_LF_ID, ControlMode.kVoltage);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            JaguarRR = new CANJaguar(JAGUAR_RR_ID, ControlMode.kVoltage);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        try {
            JaguarLR = new CANJaguar(JAGUAR_LR_ID, ControlMode.kVoltage);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
            joystick = new Joystick(1);
    }

    public void drive() {
        try {
            double magnitude = joystick.getMagnitude();
            double direction = joystick.getDirectionRadians();
            double rotation = joystick.getTwist();

            double cosD = Math.cos(direction + Math.PI / 4);
            double sinD = Math.cos(direction - Math.PI / 4);

            double RF = -(cosD * magnitude - rotation);
            double LF = (sinD * magnitude + rotation);
            double RR = -(sinD * magnitude - rotation);
            double LR = (cosD * magnitude + rotation);

            setJaguar(RF, LF, RR, LR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setJaguar(double RF, double LF, double RR, double LR) {
        for (int i = 0; i < 10; i++) {
            try {
                JaguarRF.setX(RF * 12, SYNC_GROUP_DRIVE);
                JaguarLF.setX(LF * 12, SYNC_GROUP_DRIVE);
                JaguarRR.setX(RR * 12, SYNC_GROUP_DRIVE);
                JaguarLR.setX(LR * 12, SYNC_GROUP_DRIVE);
                CANJaguar.updateSyncGroup(SYNC_GROUP_DRIVE);
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void test() {
        for (int i = 0; i < 10; i++) {
            try {
                JaguarRF.setX(12, SYNC_GROUP_DRIVE);
                JaguarLF.setX(12, SYNC_GROUP_DRIVE);
                JaguarRR.setX(0, SYNC_GROUP_DRIVE);
                JaguarLR.setX(0, SYNC_GROUP_DRIVE);
                CANJaguar.updateSyncGroup(SYNC_GROUP_DRIVE);
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
