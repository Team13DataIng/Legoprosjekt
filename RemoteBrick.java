/*
Denne klassen lagde vi og den brukes for å koble til og styre motorer på en annen EV3.  Men før denne vil funke må det gjøres noen greier.
1) Passe på at de har ulike navn i instillingene. Vi kaller våre EV2, EV3 OG EV4.
2) Parre dem gjennom bluetooth (med de nye navnene)
3) Gå i den tingen som heter PAN og sett den ene som host og de andre som bluetooth-et eller annet.
Den som er host er den dere burde laste opp koden til.

I koden styrer dere en motor slik:
    RemoteBrick ev2 = new RemoteBrick;
    ev2.setSpeedAll(900);
    ev2.forward("A");
    ev2.closeAll()          //OBS OBS, denne er ganske viktig, for motorene på remote-ev3 stopper ikke når koden stopper, men når de blir bedt om det.
*/
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.remote.ev3.RemoteRequestEV3;
import lejos.hardware.Sound;
import lejos.hardware.Audio;
import java.io.File;

public class RemoteBrick {
    public String remoteName;
    public RegulatedMotor[] motors;
    public RemoteRequestEV3 brick;
    public int motorCount;
    public String[] bokstaver = {"A","B","C","D"};
    //lyd
    public Audio audio;
    public File file = new File("moo0.wav");

    public RemoteBrick(String name, int motorCount){
        this.motorCount = motorCount;
        this.remoteName = name;

        //kobler til Remote EV3
        try {
            System.out.println("Connecting to "+remoteName);
            this.brick = new RemoteRequestEV3(BrickFinder.find(remoteName)[0].getIPAddress());
            //Initialiserer motorer og legger dem til i en array: motors.
            System.out.println("Initializing motors at "+remoteName);
            this.motors = new RegulatedMotor[motorCount];
            for(int i=0;i<motorCount;i++){
                this.motors[i] = brick.createRegulatedMotor(bokstaver[i], 'L'); //L er typen motor
            }
            //lyd
            audio = this.brick.getAudio();
            audio.setVolume(100);

        }

        catch (Exception e){
            System.out.println("Got exception " + e);
        }
    }
    //setter farten for alle motorene
    public void setSpeedAll(int speed){
        System.out.println(remoteName + ": Setting speed for all motors: " + speed);
        for(int i = 0; i < motorCount; i++) {
            motors[i].setSpeed(speed);
        }
    }
    //setter farten for en enkelt motor
    public void setSpeed(String motor, int speed){
        for(int i = 0;i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                System.out.println(remoteName + ": Setting speed at " + motor + ": " + speed);
                motors[i].setSpeed(speed);
            }
        }
    }
    //Får én motor til å kjøre forover
    public void forward(String motor){
        for(int i = 0; i < motorCount; i++) {
            if(motor.equals(bokstaver[i])) {
                motors[i].forward();
            }
        }
    }
    //får en array med motorer til å kjøre forover. Her må bokstaven samsvare med array, så om du vil at A og C skal kjøre må du sende arrayen {"A","0","C","0"}.
    public void forward(String[] motor){
        for(int i = 0; i < motorCount; i++){
            for(int j = 0; j < motor.length; j++){
                if(motor[j].equals(bokstaver[i])){
                    motors[j].forward();
                }
            }
        }
    }

    public void backward(String motor){
        for(int i = 0; i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                motors[i].backward();
            }
        }
    }

    public void backward(String[] motor){
        for(int i = 0; i < motorCount; i++) {
            for(int j = 0; j < motor.length; j++) {
                if(motor[j].equals(bokstaver[i])) {
                    motors[j].backward();
                }
            }
        }
    }

    public void rotateTo(String motor, int degrees) {
        for(int i = 0; i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                motors[i].rotateTo(degrees, true);
            }
        }
    }

    public void rotateTo(String motor, int degrees, boolean wait) {
        for(int i = 0; i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                motors[i].rotateTo(degrees, wait);
            }
        }
    }

    public void stop(String motor){
        for(int i = 0; i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                motors[i].stop();
            }
        }
    }

    public void stop(String[] motor){
        for(int i = 0; i < motorCount; i++){
            for(int j = 0; j < motor.length; j++){
                if(motor[j].equals(bokstaver[i])){
                    motors[j].stop();
                }
            }
        }
    }

    public void rotate(String motor, int deg){
        for(int i = 0; i < motorCount; i++){
            if(motor.equals(bokstaver[i])){
                motors[i].rotate(deg);
            }
        }
    }
    //Sier til remote EV3 at man er ferdig med å bruke motorene.
    public void closeAll(){
        System.out.println("Closing motors at " + remoteName);
        for(int i = 0; i < motorCount; i++){
            motors[i].close();
        }
    }
    //Spiller av en moo-lyd
    public void moo() {
        System.out.println("Spiller lyd: "+file);
        //duration er tiden det tar å spille av lyden
        int dur = Sound.playSample(file);
        if(dur<0){
            System.out.println("En feil har skjedd ved avspilling av lyd: "+file);
        }
        try {
            Thread.sleep(dur+10);
        }
        catch(Exception e){

        }
    }
    public void remoteMotorTest(){
        System.out.println("Setting speed.");
        for(int i=0;i<motors.length;i++){
            motors[i].setSpeed(900);
        }
        System.out.println("Forward.");
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
