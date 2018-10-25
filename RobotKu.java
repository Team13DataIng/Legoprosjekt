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

public class RobotKu {

    // Initialize EV3 objects
    private static Brick brick;
    private static TextLCD lcd;
    private static Port p1, p2, p3;             // p1: touchSensor | p2: forward ultrasonic | p3: downward ultrasonic
    private static SampleProvider touchSensor;
    private static float[] touchSample;

    //remote ev3
    private static RemoteBrick ev3;
    private static RemoteBrick ev2;

    // Initialize constants
    private static final String VERSION_NUMBER = "a_0.6";
    private static final int LEG_SPEED = 10;
    private static final int STAND_HIP = 800;
	private static final int STAND_KNEE = 2200;
    private static final String[] MOTORS_ALL = {"A","B","C","D"};
    private static final String[] MOTORS_HIP = {"A","0","C","0"};
    private static final String[] MOTORS_KNEE = {"0","B","0","D"};

    public static void main(String[] args) {
        System.out.println("Robotku version " + VERSION_NUMBER);

        System.out.println("Test 1");

        setupEV3();     // Setup EV3 components

        standUp();

        // Start programrutine

        /*
        while (!checkForPush()) {
            System.out.println("Walk cycle start.");
            startWalk(ev2, "L");
            startWalk(ev2, "R");
            startWalk(ev3, "L");
            startWalk(ev3, "R");
        }
        */

        System.out.println("Waiting 8 seconds");

        delay(8000);

        lieDown();

        delay(8000);

        // Slutt programrutine

        shutOff();
    }

    // Methods

    // This method sets up the EV3 components
    private static void setupEV3() {
        System.out.println("Initializing setup.");

        System.out.println("Connecting to remote EV3 bricks.");
        try {
            ev3 = new RemoteBrick("EV3", 4);        //Andre parameter er anntall motorer som skal settes opp.
            ev2 = new RemoteBrick("EV2", 4);

            ev3.setSpeedAll(LEG_SPEED);
		    ev2.setSpeedAll(LEG_SPEED);
        }
        catch (Exception e) {
            System.out.println("Could not connect to other EV3 bricks. Please ensure Bluetooth connection.");
            System.out.println(e.toString());
        }

        brick = BrickFinder.getDefault();      // Set the main EV3 brick

        // TODO: Initialize sensor ports
        System.out.println("Initializing sensor ports.");

        try {
            p1 = brick.getPort("S1");
            p2 = brick.getPort("S2");
            p3 = brick.getPort("S3");
            touchSensor = new EV3TouchSensor(p1);
        }
        catch (Exception ex) {
            System.out.println("Could not connect to sensor ports. Exception: " + ex);
        }
        
        System.out.println("Setup complete.");
    }

    private static void shutOff(){
        System.out.println("Adieu");
        ev2.closeAll();
        ev3.closeAll();
    }

    private static void startWalk(RemoteBrick rBrick, String side) {
        /*
        1: Rotate knee backwards
        2: Rotate hip upwards
        3: Rotate knee forward
        4: Rotate hip downwards
        */

        String hip;
        String knee;

        if (side.equals("L")) {
            hip = "A";
            knee = "B";
        } else if (side.equals("R")) {
            hip = "C";
            knee = "D";
        } else {
            throw new IllegalArgumentException("Invalid side. Please use either 'R' or 'L'.");
        }
        
        rBrick.forward(knee);
        rBrick.forward(hip);
        delay(2000);
        rBrick.backward(knee);
        delay(1000);
        rBrick.backward(hip);
        delay(1000);
        rBrick.stop(knee);
        rBrick.stop(hip);
    }

    private static void stopWalk() {

    }

    

    private static void delay(int duration){
        try {
			Thread.sleep(duration);
		}
		catch(Exception e){
			System.out.println("Got exception: " + e);
		}
    }

    private static void standUpAlt() {
        System.out.println("Starting standing procedure.");

        // Denne rekkefølgen er vesentlig
        ev3.forward(MOTORS_ALL);
        ev2.backward(MOTORS_ALL);

		delay(STAND_HIP);

        ev3.stop(MOTORS_HIP);
        ev2.stop(MOTORS_HIP);

        System.out.println("Hips are done!");
        ev3.setSpeedAll(10);
        ev2.setSpeedAll(30);
		delay(3000);

        ev3.stop(MOTORS_KNEE);
        ev2.stop(MOTORS_KNEE);

        System.out.println("Standing procedure completed.");
    }

    private static void standUpAlt2() {
        System.out.println("Starting standing procedure.");

        // Hofter:      Ca. 40 grader
        // Knær:        Ca. 130 grader

        // Denne rekkefølgen er vesentlig
        ev2.setSpeedAll(30);
        ev3.setSpeedAll(30);
        ev2.forward(MOTORS_HIP);
        ev3.backward(MOTORS_HIP);

        delay(2000);

        ev3.stop(MOTORS_HIP);
        ev2.stop(MOTORS_HIP);

        delay(2000);

        ev2.forward(MOTORS_KNEE);
        ev3.backward(MOTORS_KNEE);

        delay(STAND_KNEE);

        ev2.stop(MOTORS_KNEE);
        ev3.stop(MOTORS_KNEE);

        delay(2000);

        System.out.println("Standing procedure completed.");
    }

    private static void standUp() {
        ev2.setSpeedAll(50);
        ev3.setSpeedAll(50);

        ev2.rotateTo("A", 30);
        ev2.rotateTo("C", 30);
        ev3.rotateTo("A", 30);
        ev3.rotateTo("C", 30);
        ev2.rotateTo("B", 130);
        ev2.rotateTo("D", 130);
        ev3.rotateTo("B", 130);
        ev3.rotateTo("D", 130);

        System.out.println("Venter i 10 sekunder.");

        delay(10000);
    }

    private static void lieDown() {
        ev2.setSpeedAll(50);
        ev3.setSpeedAll(50);

        ev2.rotateTo("A", 0);
        ev2.rotateTo("C", 0);
        ev3.rotateTo("A", 0);
        ev3.rotateTo("C", 0);
        ev2.rotateTo("B", 0);
        ev2.rotateTo("D", 0);
        ev3.rotateTo("B", 0);
        ev3.rotateTo("D", 0);
    }

    private static boolean checkForPush() {
        touchSample = new float[touchSensor.sampleSize()];

        if (touchSample != null && touchSample.length > 0) {
            touchSensor.fetchSample(touchSample, 0);
            if (touchSample[0] > 0) {
                return true;
            }
        }
        return false;
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
