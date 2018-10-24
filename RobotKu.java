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
    private static final String VERSION_NUMBER = "a_0.5";
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
        //delay før alle motorer skrus av
        delay(8000);

        shutOff();
    }

    // Methods

    // This method sets up the EV3 components
    private static void setupEV3() {
        try {
            ev3 = new RemoteBrick("EV3", 4);        //Andre parameter er anntall motorer som skal settes opp.
            ev2 = new RemoteBrick("EV2", 4);

            ev3.setSpeedAll(LEG_SPEED);
		    ev2.setSpeedAll(LEG_SPEED);
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
    }
    private static void shutOff(){
        System.out.println("Adieu");
        ev2.closeAll();
        ev3.closeAll();
    }
    private static void startWalk() {

    }

    private static void stopWalk() {

    }

    private static void lieDown() {

    }
    private static void delay(int duration){
        try {
			Thread.sleep(duration);
		}
		catch(Exception e){
			System.out.println("Got exeption: " + e);
		}
    }
    private static void standUp() {
        // Denne rekkefølgen er vesentlig
        ev3.forward(MOTORS_ALL);
        ev2.backward(MOTORS_ALL);

		delay(STAND_HIP);

        ev3.stop(MOTORS_HIP);
        ev2.stop(MOTORS_HIP);

		delay(STAND_KNEE - STAND_HIP);

        ev3.stop(MOTORS_KNEE);
        ev2.stop(MOTORS_KNEE);
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
