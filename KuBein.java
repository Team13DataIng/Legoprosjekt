/*
 * Creation date:   17.10.2018
 * Authors:         Cato Bakken, Eirik Hemstad, Jonas B. Jacobsen, Torbjørn B. Lauvvik, Torstein H. Sundfær
*/

import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.port.*;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.Keys;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.*;
import lejos.hardware.Button;
import java.util.*;

class KuBein {
    private Motor motor;

    // Object variables
    private final int DEFAULT_SPEED;
    private int rotationSpeed;

    // Other variables
    private static final String VERSION_NUMBER = "a_0.1";

    public KuBein(Motor motor, int rotationSpeed) {
        this.motor = motor;
        this.rotationSpeed = rotationSpeed;
        motor.setSpeed(this.rotationSpeed);
    }

    public KuBein(Motor motor) {
        this.motor = motor;
        this.rotationSpeed = DEFAULT_SPEED;
        motor.setSpeed(this.getRotationSpeed);
    }

    // Public setters & getters

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public int getDefaultSpeed() {
        return DEFAULT_SPEED;
    }

    public void setRotationSpeed(int newSpeed) {
        rotationSpeed = newSpeed;
        motor.setSpeed(rotationSpeed);
    }

    public String getVersionNumber() {
        return VERSION_NUMBER;
    }

    // Public Methods

    public void liftLeg() {
        motor.forward();
    }

    public void lowerLeg() {
        motor.backward();
    }

    public void liftLeg(int degrees) {
        motor.rotate(degrees);
    }

    public void lowerLeg(int degrees) {
        motor.rotate(-degrees);
    }

    public void stopLeg() {
        motor.stop();
    }

    public String toString() {
        return ("KuBein version " + VERSION_NUMBER);
    }
}
