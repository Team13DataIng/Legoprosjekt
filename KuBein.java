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
    private final BaseRegulatedMotor hipMotor;
    private final BaseRegulatedMotor kneeMotor;

    // Object variables
    private final int KNEE_POS_STANDING = 200;          // TODO: Change
    private final int HIP_POS_STANDING = 200;           // TODO: Change
    private final int DEFAULT_SPEED = 300;
    private int rotationSpeed;

    // Other variables
    private static final String VERSION_NUMBER = "a_0.3";

    public KuBein(BaseRegulatedMotor hipMotor, BaseRegulatedMotor kneeMotor, int rotationSpeed) {
        this.hipMotor = hipMotor;
        this.kneeMotor = kneeMotor;
        this.rotationSpeed = rotationSpeed;
        hipMotor.setSpeed(this.rotationSpeed);
        kneeMotor.setSpeed(this.rotationSpeed);
    }

    public KuBein(BaseRegulatedMotor hipMotor, BaseRegulatedMotor kneeMotor) {
        this.hipMotor = hipMotor;
        this.kneeMotor = kneeMotor;
        this.rotationSpeed = DEFAULT_SPEED;
        hipMotor.setSpeed(this.rotationSpeed);
        kneeMotor.setSpeed(this.rotationSpeed);
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
        hipMotor.setSpeed(rotationSpeed);
        kneeMotor.setSpeed(rotationSpeed);
    }

    public String getVersionNumber() {
        return VERSION_NUMBER;
    }

    // Public Methods

    public void liftLeg() {
        hipMotor.forward();
        kneeMotor.forward();
    }

    public void lowerLeg() {
        hipMotor.backward();
        kneeMotor.backward();
    }

    public void stopLeg() {
        hipMotor.stop();
        kneeMotor.stop();
    }

    public String toString() {
        return ("KuBein version " + VERSION_NUMBER);
    }
}
