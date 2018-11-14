/*
 * Creation date:   17.10.2018
 * Authors:         Cato Bakken, Eirik Hemstad, Jonas B. Jacobsen, Torbjørn B. Lauvvik, Torstein H. Sundfær
*/

import lejos.hardware.motor.Motor;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.port.Port;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3TouchSensor;



public class RobotKu {

    // Initialize EV3 objects
    private static Brick brick;
    private static TextLCD lcd;
    private static Port p1;
    private static Port p2;
    private static Port p3;
    private static SampleProvider touchSensor;
    private static float[] touchSample;
    private static SampleProvider angle;
    private static SampleProvider angle2;
    private static float[] gyroSample;
    private static float[] gyroSample2;
    private static EV3GyroSensor gyroSensor;
    private static EV3GyroSensor gyroSensor2;
    private static EV3UltrasonicSensor ultraSensor;
    public static float[] ultraSample;

    //remote ev3
    private static RemoteBrick ev3;
    private static RemoteBrick ev2;

    // Initialize constants
    private static final String VERSION_NUMBER = "1.0.5";
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

        standUp();      // Engage standing sequence
        delay(5000);
        System.out.println("Prøver å gå");

        //playMoo();      // Play the moo

        Motor.A.setSpeed(300);

        while (!checkForPush()) {
            playMoo();
            Motor.A.rotate(60);
            while(Motor.A.isMoving()){
                //wait
            }
            Motor.A.rotate(-60);
            while(Motor.A.isMoving()){
                //wait
            }
            walk();     // While button is not pushed, walk
        }

        lieDown();      // When walking is finished, lie down

        delay(3000);

        // Slutt programrutine

        shutOff();      // Shut off cow
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
            p2 = brick.getPort("S2");
            p3 = brick.getPort("S3");
            touchSensor = new EV3TouchSensor(p1);

            //gyro
    		gyroSensor = new EV3GyroSensor(p1);
            angle = gyroSensor.getAngleMode();
    		gyroSensor.reset();

            gyroSensor2 = new EV3GyroSensor(p3);
            angle2 = gyroSensor2.getAngleMode();
            gyroSensor2.reset();
            //ultrasonic
            ultraSensor = new EV3UltrasonicSensor(p2);

        }
        catch (Exception ex) {
            System.out.println("Could not connect to sensor ports. Exception: " + ex);
        }

        System.out.println("Setup complete.");
    }

    private static void playMoo(){
        ev2.moo();
    }

    private static void shutOff(){
        System.out.println("Adieu");
        ev2.closeAll();
        ev3.closeAll();
    }

    private static void walk() {
        ev2.setSpeedAll(70);
        ev3.setSpeedAll(70);

        ev2.rotateTo("A", 90);
        ev2.rotateTo("B", 160, false);
        ev3.rotateTo("D", 40);
        ev3.rotateTo("C", 0, false);

        ev3.rotateTo("C", 30, false);
        ev3.rotateTo("D", 110, false);
        ev2.rotateTo("A", 30, false);
        ev2.rotateTo("B", 110, false);

        ev3.rotateTo("B", 40);
        ev3.rotateTo("A", 0, false);
        ev2.rotateTo("C", 90);
        ev2.rotateTo("D", 160, false);

        ev3.rotateTo("A", 30, false);
        ev3.rotateTo("B", 110, false);
        ev2.rotateTo("C", 30, false);
        ev2.rotateTo("D", 110, false);
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

		int hipRotationPoint = 30;
		int kneeRotationPoint = 110;

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

        delay(5000);   
        //calibrate();
        System.out.println("Standing procedure completed");
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

    // Deprecated method

    private static float getAngle(){
        gyroSample = new float[angle.sampleSize()];
        angle.fetchSample(gyroSample, 0);
        return gyroSample[0];
    }
    private static float getAngle2(){
        gyroSample2 = new float[angle2.sampleSize()];
        angle2.fetchSample(gyroSample2, 0);
        return gyroSample2[0];
    }

    private static float getDistance(){
        SampleProvider distance = ultraSensor.getDistanceMode();
        ultraSample = new float[distance.sampleSize()];
        distance.fetchSample(ultraSample, 0);
        return ultraSample[0];
    }

    private static void calibrate(){
        float currAngle = getAngle();
        float currAngle2 = getAngle2();
        float distance = getDistance();
        float UPRIGHT_DISTANCE = 0.14f;
        float UPRIGHT_TOLERANSE = 0.03f;
        int CAL_SPEED = 10;
        while(currAngle>0||currAngle<0||currAngle2>0||currAngle2<0||distance<0.7||distance>0.72){
            ev3.setSpeedAll(CAL_SPEED);
            ev2.setSpeedAll(CAL_SPEED);
            //ev3 er foran ev3 er bak
            if(currAngle>0){
                System.out.println("Calibrating. Lowering back.");
                //øk forbein eller senk bakbein
                ev3.backward(MOTORS_HIP);
                while(currAngle>0){
                    currAngle = getAngle();
                }
                ev3.stop(MOTORS_ALL);

            }
            if(currAngle<0){
                System.out.println("Calibrating. Lowering front.");
                //øk bakbein eller senk forbein
                ev2.backward(MOTORS_HIP);
                while(currAngle<0){
                    currAngle = getAngle();
                }
                ev2.stop(MOTORS_ALL);
            }
            if(currAngle2>0){
                System.out.println("Calibrating. Lowering right.");
                //senk bein høyre
                ev3.backward("C");
                ev2.backward("C");
                while(currAngle2>0){
                    currAngle = getAngle2();
                }
                ev3.stop("C");
                ev2.stop("C");

            }
            if(currAngle2<0){
                System.out.println("Calibrating. Lowering left.");
                //senk bein venstre
                ev3.backward("A");
                ev2.backward("A");
                while(currAngle2<0){
                    currAngle = getAngle2();
                }
                ev3.stop("A");
                ev2.stop("A");
            }
            if(distance>UPRIGHT_DISTANCE){
                //senk alt
                System.out.println("Calibrating. Lowering all.");
                ev3.backward(MOTORS_ALL);
                ev2.backward(MOTORS_ALL);
                while(distance>UPRIGHT_DISTANCE){
                    distance = getDistance();
                    System.out.println(distance);
                }
                ev3.stop(MOTORS_ALL);
                ev2.stop(MOTORS_ALL);
            }
            if(distance<UPRIGHT_DISTANCE-UPRIGHT_TOLERANSE){
                //reis alt
                System.out.println("Calibrating. Raising all.");
                ev3.forward(MOTORS_ALL);
                ev2.forward(MOTORS_ALL);
                while(distance<UPRIGHT_DISTANCE-UPRIGHT_TOLERANSE){
                    distance = getDistance();
                    System.out.println(distance);
                }
                ev3.stop(MOTORS_ALL);
                ev2.stop(MOTORS_ALL);
            }
        }
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
}
