/*
 * Creation date:   17.10.2018
 * Authors:         Cato Bakken, Eirik Hemstad, Jonas B. Jacobsen, Torbjørn B. Lauvvik, Torstein H. Sundfær
 *
 * This class is an application for the RobotKu robot. The robot is non-interactive, and follows a cycle of:
 * 1: Moo
 * 2: Open and close mouth
 * 3: Walk two steps
 * 
 * The robot follows this cycle until the touch sensor is held
 *
 * Dependencies:
 * RemoteBrick.class
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
    private static SampleProvider touchSensor;
    private static float[] touchSample;

    //remote ev3
    private static RemoteBrick ev3;
    private static RemoteBrick ev2;

    // Initialize constants
    private static final String VERSION_NUMBER = "1.0.6";

    // Leg constants
    private static final int WALK_SPEED = 70;
    private static final int STAND_SPEED = 30;
    private static final int STAND_HIP = 30;
	private static final int STAND_KNEE = 110;

    // The leg motors are represented by strings
    // Note that there are one set of legs per EV3 brick
    private static final String LEFT_HIP = "A";
    private static final String LEFT_KNEE = "B";
    private static final String RIGHT_HIP = "C";
    private static final String RIGHT_KNEE = "D";

    // Private methods start

    /**
     * Sets up all the bricks, motors and sensors that the program will use
     */
    private static void setupEV3() {
        System.out.println("Initializing setup.");

        System.out.println("Connecting to remote EV3 bricks.");
        try {
            ev3 = new RemoteBrick("EV3", 4);        // Brick for back legs, second value is number of motors
            ev2 = new RemoteBrick("EV2", 4);        // Brick for front legs

            ev3.setSpeedAll(STAND_SPEED);
		    ev2.setSpeedAll(STAND_SPEED);
        }
        catch (Exception e) {
            System.out.println("Could not connect to other EV3 bricks. Please ensure Bluetooth connection.");
            System.out.println(e.toString());
        }

        // Set the main EV3 brick
        brick = BrickFinder.getDefault();      

        // Initialize sensor ports
        System.out.println("Initializing sensor ports.");

        try {
            // Touch sensor
            p1 = brick.getPort("S1");
            touchSensor = new EV3TouchSensor(p1);
        }
        catch (Exception ex) {
            System.out.println("Could not connect to sensor ports. Exception: " + ex);
        }

        System.out.println("Setup complete.");
    }

    /**
     * Plays a 'moo' sound effect at the specified remote brick
     * @param: rBrick
     */
    private static void playMoo(RemoteBrick rBrick){
        rBrick.moo();
    }

    /**
     * Shuts off the robot
     */
    private static void shutOff(){
        System.out.println("Adieu");
        ev2.closeAll();
        ev3.closeAll();
    }

    /**
     * Makes the application pause for the specified time (milliseconds)
     * @param: duration
     */
    private static void delay(int duration){
        try {
			Thread.sleep(duration);
		}
		catch(Exception e){
			System.out.println("Got exception: " + e);
		}
    }

    /*
     * Makes the robot stand up
     * When raising up the robot, all motors rotate to the standing position
     * The order of the motor routine may be crucial to maintaining balance through the process
     * It is important that the robot is in the standard resting position
     */
    private static void standUp() {
        System.out.println("Starting standing procedure.");

        ev2.setSpeedAll(STAND_SPEED);
        ev3.setSpeedAll(STAND_SPEED);

		ev2.rotateTo(RIGHT_HIP, STAND_HIP);
        ev2.rotateTo(LEFT_HIP, STAND_HIP);
        ev3.rotateTo(RIGHT_HIP, STAND_HIP);
        ev3.rotateTo(LEFT_HIP, STAND_HIP);

        ev2.rotateTo(RIGHT_KNEE, STAND_KNEE);
        ev2.rotateTo(LEFT_KNEE, STAND_KNEE);
        ev3.rotateTo(RIGHT_KNEE, STAND_KNEE);
        ev3.rotateTo(LEFT_KNEE, STAND_KNEE);

        delay(5000);   
        //calibrate();
        System.out.println("Standing procedure completed");
    }    

    /**
     * Makes the robot lie down. The robot rotates all leg motors back to their starting position. 
     * If the robot stood up from the resting position, it should return to this.
     */
    private static void lieDown() {
        System.out.println("Starting liedown procedure.");

        ev2.setSpeedAll(STAND_SPEED);
        ev3.setSpeedAll(STAND_SPEED);

        ev2.rotateTo(LEFT_HIP, 0);
        ev2.rotateTo(RIGHT_HIP, 0);
        ev3.rotateTo(LEFT_HIP, 0);
        ev3.rotateTo(RIGHT_HIP, 0);
        ev2.rotateTo(LEFT_KNEE, 0);
        ev2.rotateTo(RIGHT_KNEE, 0);
        ev3.rotateTo(LEFT_KNEE, 0);
        ev3.rotateTo(RIGHT_KNEE, 0);

        delay(8000);

        System.out.println("Liedown procedure completed.");
    }

    /**
     * Makes the robot walk two steps forward
     * When walking, the robot lifts two of its legs (one front, one back, opposite to each other) at a time, before setting them down
     * This process is a little clunky, and the robot has some issues maintaining balance throughout. The forward speed is also very slow.
     * This may be improved upon by careful observation and testing, but the team ran out of time to optimize this further for v1.0.
     */
    private static void walk() {
        ev2.setSpeedAll(WALK_SPEED);
        ev3.setSpeedAll(WALK_SPEED);

        ev2.rotateTo(LEFT_HIP, 90);
        ev2.rotateTo(LEFT_KNEE, 160, false);
        ev3.rotateTo(RIGHT_KNEE, 40);
        ev3.rotateTo(RIGHT_HIP, 0, false);

        ev3.rotateTo(RIGHT_HIP, STAND_HIP, false);
        ev3.rotateTo(RIGHT_KNEE, STAND_KNEE, false);
        ev2.rotateTo(LEFT_HIP, STAND_HIP, false);
        ev2.rotateTo(LEFT_KNEE, STAND_KNEE, false);

        ev3.rotateTo(LEFT_KNEE, 40);
        ev3.rotateTo(LEFT_HIP, 0, false);
        ev2.rotateTo(RIGHT_HIP, 90);
        ev2.rotateTo(RIGHT_KNEE, 160, false);

        ev3.rotateTo(LEFT_HIP, STAND_HIP, false);
        ev3.rotateTo(LEFT_KNEE, STAND_KNEE, false);
        ev2.rotateTo(RIGHT_HIP, STAND_HIP, false);
        ev2.rotateTo(RIGHT_KNEE, STAND_KNEE, false);
    }

    /**
     * Uses the touch sensor to check for touch. If it does register a touch, return true
     */
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

    // Private methods end

    /**
     * Main method
     * @param: args
     */
    public static void main(String[] args) {
        System.out.println("Robotku version " + VERSION_NUMBER);

        // Setup EV3 components
        setupEV3();     

        // Engage standing sequence
        standUp();
        delay(5000);

        // Set speed for mouth motor
        Motor.A.setSpeed(300);

        // Run while the push sensor is not pushed
        while (!checkForPush()) {
            // Play moo sound
            playMoo(ev2);

            // Open and close the mouth
            Motor.A.rotate(60);
            while(Motor.A.isMoving()){
                //wait
            }
            Motor.A.rotate(-60);
            while(Motor.A.isMoving()){
                //wait
            }

            // Walk two steps
            walk();
        }

        // When walking is finished, lie down
        lieDown();      
        delay(3000);

        // Shut off cow
        shutOff();      
    }
}