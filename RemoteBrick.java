import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.*;
import lejos.robotics.*;
import lejos.remote.ev3.*;


public class RemoteBrick {
    public String remoteName;
    public RegulatedMotor[] motors;
    public RemoteRequestEV3 brick;
    public int motorCount;
    public String[] bokstaver = {"A","B","C","D"};

    public RemoteBrick(String name, int motorCount){
        this.motorCount = motorCount;
        this.remoteName = name;
        try {
            System.out.println("Connecting to " + remoteName);
            this.brick = new RemoteRequestEV3(BrickFinder.find(remoteName)[0].getIPAddress());
            //Motorer
            System.out.println("Initializing motors at " + remoteName);
            this.motors = new RegulatedMotor[motorCount];
            for(int i=0;i<motorCount;i++){
                this.motors[i] = brick.createRegulatedMotor(bokstaver[i],'L');
            }

            /*motors = new RegulatedMotor[4];
            motors[0] = brick.createRegulatedMotor("A",'L'); //faktisk viktig at den første er med "" og den andre er med ''.
            motors[1] = brick.createRegulatedMotor("B",'L'); //første parameter er motor-port, andre parameter er typen motor. L er de vi har.
            motors[2] = brick.createRegulatedMotor("C",'L');
            motors[3] = brick.createRegulatedMotor("D",'L');*/
        }
        catch (Exception e){
            System.out.println("Got exception " + e);
        }
    }

    public void setSpeedAll(int speed){
        System.out.println("Setting speed: " + speed);
        for(int i = 0;i < motorCount; i++){
            motors[i].setSpeed(speed);
        }
    }

    public void setSpeed(String motor, int speed){
        for(int i = 0;i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                System.out.println("Setting speed at "+motor+": "+speed);
                motors[i].setSpeed(speed);
            }
        }
    }

    public void forward(String motor){
        for(int i = 0; i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                System.out.println("Motor "+motor+" forwards");
                motors[i].forward();
            }
        }
    }

    public void forward(String[] motor){
        for(int i = 0; i < motorCount; i++){
            for(int j = 0; j < motor.length; j++){
                if(motor[j].equals(bokstaver[i])){
                    System.out.println("Motor " + motor[j] + " forwards");
                    motors[j].forward();
                }
            }
        }
    }

    public void backward(String motor){
        for(int i = 0; i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                System.out.println("Motor "+motor+" backwards");
                motors[i].backward();
            }
        }
    }
    public void backward(String[] motor){
        for(int i = 0; i < motorCount; i++){
            for(int j = 0; j < motor.length; j++){
                if(motor[j].equals(bokstaver[i])){
                    System.out.println("Motor " + motor[j] + " backwards");
                    motors[j].backward();
                }
            }
        }
    }
    public void stop(String motor){
        for(int i = 0; i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                System.out.println("Motor "+motor+" stop");
                motors[i].stop();
            }
        }
    }
    public void stop(String[] motor){
        for(int i = 0; i < motorCount; i++){
            for(int j = 0; j < motor.length; j++){
                if(motor[j].equals(bokstaver[i])){
                    System.out.println("Motor " + motor[j] + " stop");
                    motors[j].stop();
                }
            }
        }
    }
    public void rotate(String motor, int deg){
        for(int i = 0; i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                System.out.println("Motor "+motor+" rotate "+deg+" deg");
                motors[i].rotate(deg);
            }
        }
    }
    public void closeAll(){
        System.out.println("CLosing motors at "+remoteName);
        for(int i = 0; i < motorCount; i++){
            motors[i].close();
        }
    }
    public void remoteMotorTest(){
        System.out.println("Setting speed.");
        for(int i=0;i<motors.length;i++){
            motors[i].setSpeed(900);
        }
        System.out.println("Rotating.");
        for(int i=0;i<motors.length;i++){
            motors[i].forward();
        }
        try {
            Thread.sleep(4000);
        }
        catch(Exception e){

        }
        System.out.println("Closing motors. Goodbye.");
        for(int i=0;i<motors.length;i++){
            motors[i].close();
        }
    }

    //testklient
    public static void main(String[] args){
        RemoteBrick ev2 = new RemoteBrick("EV2", 4);
        ev2.setSpeed("C", 250);
        ev2.forward("C");
        try {
            Thread.sleep(5000);
        }
        catch(Exception e){
            //
        }
        ev2.backward("C");
        try {
            Thread.sleep(5000);
        }
        catch(Exception e){
            //
        }
        ev2.stop("C");
        ev2.closeAll();
    }
}