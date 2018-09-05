
import java.awt.*;
import java.util.ArrayList;

public class Snake {

    float time;
    double v=40;
    double pxDistBetweenSprites=10;
    ArrayList<float[]> previousAngles=new ArrayList<>();
    boolean isTurning=false;
    float timeSinceTurnStart;
    float turnMagnitude=1;
    int BLOCKSIZE;
    float[][] spritePositions;

    public Snake(float x, float y, int WIDTH, int HEIGHT){
        spritePositions=new float[10][];
        for (int i=0; i<10; i++){
            spritePositions[i]=new float[]{x, y};
        }
        previousAngles.add(new float[]{0,0});
    }

    public void update(float delta){
        time+=delta;
        timeSinceTurnStart+=delta;
        if (timeSinceTurnStart>2){
            turnMagnitude=(float)(Math.random()*4)-2;
            timeSinceTurnStart=0;
        }
        float newAngle=previousAngles.get(0)[0]+delta*turnMagnitude;
        previousAngles.add(0, new float[]{newAngle, time});
        System.out.println("angle= "+(int)previousAngles.get(0)[0]);
        for (int i=0; i<10; i++) {
            float cangle=getAngleTSecondsAgo((float) ((double) pxDistBetweenSprites / v) * i, i);
            System.out.println("for sprite "+i+" Angle is "+cangle);
            if (-1 !=cangle) {
                spritePositions[i][0] = spritePositions[i][0] + (float) (delta*(v * Math.cos(cangle)));
                spritePositions[i][1] = spritePositions[i][1] + (float) (delta*(v * Math.sin(cangle)));
            }
        }
    }

    private float getAngleTSecondsAgo(float t, int y){
        float desiredTime=time-t;
        float angle=0;
        float angleCloseness=time;
        float angleTime=0;
        for (int i=0; i<previousAngles.size(); i++){
            float closeness=(float)Math.abs(desiredTime-previousAngles.get(i)[1]);
            if (closeness<angleCloseness){
                angleCloseness=closeness;
                angleTime=previousAngles.get(i)[1];
                angle=previousAngles.get(i)[0];
            }
        }
        if (angleCloseness>(v/pxDistBetweenSprites)){
            return -1;
        }
        return angle;
    }

    public void render(Graphics g) {
        for (int i=0; i<10; i++){
            g.drawRect((int)spritePositions[i][0], (int)spritePositions[i][1],  20, 20);
        }
    }

}
