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

class RobotKu {

    // Initialize EV3 objects
    private static EV3 ev3;
    private static Brick brick;
    private static TextLCD lcd;
    private static Port p1, p2, p3;             // p1: touchSensor | p2: forward ultrasonic | p3: downward ultrasonic
    private static SampleProvider touchSensor;
    private static float[] touchSample;
    private static KuBein lf, rf, lb, rb;

    // Initialize constants
    private static final String VERSION_NUMBER = "a_0.3";

    private static void main(String[] args) {
        System.out.println("Robotku version " + VERSION_NUMBER);

        System.out.println("Test 1");

        setupEV3();     // Setup EV3 components

        standUp();
    }

    // Methods

    // This method sets up the EV3 components
    private static void setupEV3() {
        try {
            // TODO: Attempt to connect to other EV3 brick
        }
        catch (Exception e) {
            System.out.println("Could not connect to other EV3 brick. Please ensure Bluetooth exception.");
            System.out.println(e.toString());
        }

        brick = BrickFinder.getDefault();      // Set the main EV3 brick

        // TODO: Initialize sensor ports

        p1 = brick.getPort("S1");
        p2 = brick.getPort("S2");
        p3 = brick.getPort("S3");

        touchSensor = new EV3TouchSensor(p1);

        // Setup legs
        // TODO: Change everything
        lf = new KuBein(Motor.A, Motor.B);
        rf = new KuBein(Motor.C, Motor.D);
        lb = new KuBein(Motor.A, Motor.B);
        rb = new KuBein(Motor.C, Motor.D);
    }

    private static void startWalk() {

    }

    private static void stopWalk() {

    }

    private static void lieDown() {

    }

    private static void standUp() {
        lf.liftLeg();
        rf.liftLeg();
        lb.liftLeg();
        rb.liftLeg();
    }

    private static void rotateHead() {

    }

    private static void rotateTorso() {

    }

    private static int scanForward() {
        return 0;
    }

    private static int scanDownward() {
        return 0;
    }
}
