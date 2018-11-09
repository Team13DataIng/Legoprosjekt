/*
 * Creation date:   17.10.2018
 * Authors:         Cato Bakken, Eirik Hemstad, Jonas B. Jacobsen, Torbjørn B. Lauvvik, Torstein H. Sundfær
*/

import lejos.hardware.*;
import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
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
    private static Port p1;
    private static SampleProvider touchSensor;
    private static float[] touchSample;
    private static SampleProvider angle;
    private static float[] gyroSample;
    public static EV3GyroSensor gyroSensor;


    //remote ev3
    private static RemoteBrick ev3;
    private static RemoteBrick ev2;

    // Initialize constants
    private static final String VERSION_NUMBER = "a_0.7";
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
        //playMoo();
        //playMoo();
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

        delay(1000);
        hodeTest();
        //playMoo();
        System.out.println("Waiting 4 seconds");
        calibrate();

        delay(4000);

        lieDown();

        delay(3000);

        // Slutt programrutine

        shutOff();
    }

    // Methods

    // This method sets up the EV3 components
    private static void setupEV3() {
        System.out.println("Initializing setup.");

        System.out.println("Connecting to remote EV3 bricks.");
        try {
            ev3 = new RemoteBrick("EV3", 4); //bakbein       //Andre parameter er anntall motorer som skal settes opp.
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
            //p2 = brick.getPort("S2");
            //p3 = brick.getPort("S3");
            //touchSensor = new EV3TouchSensor(p1);

            //gyro
    		gyroSensor = new EV3GyroSensor(p1);
            angle = gyroSensor.getAngleMode();
    		gyroSensor.reset();
        }
        catch (Exception ex) {
            System.out.println("Could not connect to sensor ports. Exception: " + ex);
        }

        System.out.println("Setup complete.");
    }

    private static void playMoo(){
        ev2.moo();
    }
    private static void hodeTest(){
        Motor.A.setSpeed(300);
        Motor.B.setSpeed(50);
        Motor.B.rotate(100);
        while(Motor.B.isMoving()){
            //wait
        }
        Motor.B.rotate(-100);
        while(Motor.B.isMoving()){

        }
        for(int i = 0;i<15;i++){
            Motor.A.rotate(60);
            while(Motor.A.isMoving()){
                //wait
            }
            Motor.A.rotate(-60);
            while(Motor.A.isMoving()){
                //wait
            }
        }
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
        }
        else if (side.equals("R")) {
            hip = "C";
            knee = "D";
        }
        else {
            throw new IllegalArgumentException("Invalid side. Please use either 'R' or 'L'.");
        }

		rBrick.setSpeedAll(10);

        rBrick.rotateTo(hip, 40);
        delay(1000);
        rBrick.rotateTo(knee, 0);
        delay(2000);


        rBrick.rotateTo(knee, 100);
        rBrick.rotateTo(hip, 10);

        delay(2000);
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

    private static void standUp() {
        System.out.println("Starting standing procedure.");

		int hipRotationPoint = 10;
		int kneeRotationPoint = 100;

        ev2.setSpeedAll(30);
        ev3.setSpeedAll(30);

		ev2.rotateTo("C", hipRotationPoint);
        ev2.rotateTo("A", hipRotationPoint);
        ev3.rotateTo("C", hipRotationPoint);
        ev3.rotateTo("A", hipRotationPoint);

        ev2.rotateTo("D", kneeRotationPoint);
        ev2.rotateTo("B", kneeRotationPoint);
        ev3.rotateTo("D", kneeRotationPoint);
        ev3.rotateTo("B", kneeRotationPoint);

        delay(50);
        calibrate();
        System.out.println("Standing procedure completed");
    }
    private static void calibrate(){
        float currAngle = getAngle();
        int CAL_SPEED = 10;
        /*
            TING SOM MÅ TETSTES HER
            1) ER BACKWARD RIKTIG RETNING?
            2) FUNKER DET?
            3) FART? JUSTER
            4) ALLE LEDD ELLER BARE HOFTE/KNE
        */
        //ev3 er foran ev3 er bak
        if(currAngle>0){
            System.out.println("Calibrating. Lowering back.");
            //øk forbein eller senk bakbein
            ev3.setSpeedAll(CAL_SPEED);
            ev3.backward(MOTORS_ALL);
            while(currAngle>0){
                currAngle = getAngle();
            }
            ev3.stop(MOTORS_ALL);

        }
        else if(currAngle<0){
            System.out.println("Calibrating. Lowering front.");
            //øk bakbein eller senk forbein
            ev2.setSpeedAll(CAL_SPEED);
            ev2.backward(MOTORS_ALL);
            while(currAngle<0){
                currAngle = getAngle();
            }
            ev2.stop(MOTORS_ALL);
        }
    }
    private static float getAngle(){
		gyroSample = new float[angle.sampleSize()];
		angle.fetchSample(gyroSample, 0);
		return gyroSample[0];
    }
    private static void lieDown() {
        System.out.println("Starting liedown procedure.");

        ev2.setSpeedAll(30);
        ev3.setSpeedAll(30);

        ev2.rotateTo("A", 0);
        ev2.rotateTo("C", 0);
        ev3.rotateTo("A", 0);
        ev3.rotateTo("C", 0);
        ev2.rotateTo("B", 0);
        ev2.rotateTo("D", 0);
        ev3.rotateTo("B", 0);
        ev3.rotateTo("D", 0);

        delay(8000);

        System.out.println("Liedown procedure completed.");
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
