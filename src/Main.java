import com.sun.javafx.geom.Vec2d;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    private final int WIDTH=1280, HEIGHT=900;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;


    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(0);
    Color goalColor=new Color(255,0,0);


    int timeWitho2;
    float[] goal;

    ArrayList<float[]> oldlocs;
    float[] loc;
    float speed=7;
    float o=0;//orientation
    float o1=0;//rate of change
    float o2=0;//rate of change of rate of change

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();

        goal=new float[]{(float)(Math.random()*WIDTH*.66)+(WIDTH/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};
        loc=new float[]{(float)(Math.random()*WIDTH*.66)+(WIDTH/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};

        timeWitho2=1;
        o=(float)(Math.random()*3.14);
        o1=(float)(Math.random()*3.14/10);
        o2=(float)(Math.random()*3.14/10);
        oldlocs=new ArrayList<>();
        oldlocs.add(loc);

    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        renderRope(gfx);
        gfx.drawString("o = "+o+", o1 = "+o1+", o2 = "+o2, 100,100);
        gfx.setColor(goalColor);
        gfx.fillRect((int)goal[0],(int)goal[1],10,10);

        //RENDER FOREGROUND


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void renderRope(Graphics g){
        g.setColor(gridColor);
        for (int i=0; i<oldlocs.size(); i++){
            g.fillRect((int)oldlocs.get(i)[0],(int)oldlocs.get(i)[1],5,5);
        }
    }

    public void update(Graphics g){ //REDRAWS FRAME

        paint(g);
    }

    public void move(){
        float dor=(float)(Math.atan((goal[1]-loc[1])/(goal[0]-loc[0])));
        if ((goal[0]-loc[0])<0){ dor=dor+3.14f; }

        //System.out.println("optimal dir = "+Math.toDegrees(dor));

        o=o%6.28f;

        timeWitho2--;
        if (timeWitho2<1){
            resetO2(dor);
        }
        o=o+o1;
        if (Math.abs(o1+o2)<3.14f/8) {
            o1 = o1 + o2;
        }else {
            resetO2(dor);
        }
        oldlocs.add(loc);
        //o=dor;

        loc=new float[]{loc[0]+(float)(Math.cos(o)*speed),loc[1]+(float)(Math.sin(o)*speed)};
    }
    public void resetO2(float dor){
        timeWitho2=(int)(Math.random()*5)+3;
        //float ddor=dor-o;
        //if (ddor>3.14){
            //ddor=-3.14f+ddor;
        //}
        o2=(float)((3.14f/4*Math.random())-(3.14f/8))/5;
        //System.out.println(o+" must use slope "+ddor+" to get to "+dor);
        //o2=(float)(Math.random()*ddor)/10f;
        //timeWitho2=(int)(Math.abs(ddor/o2));
    }

    public void moveO2ToDor(){
        float dor=(float)(Math.atan((goal[1]-loc[1])/(goal[0]-loc[0])));
        if ((goal[0]-loc[0])<0){ dor=dor+3.14f; }

        float ddor=dor-o;//ddor = the change needed to get in line
        if (ddor>3.14){
            ddor=-3.14f+ddor;
        }
        float weight=.1f;
        float no2=(o1+(ddor*weight))/(1+weight);
        o1=no2;
        /*
        if (Math.abs(no2)<3.14f/8) {
            o1 = no2;
        }else {
            o1=no2%(3.14f/8);
            //resetO2(dor);
        }*/
    }

    private void finish(){
        while (!hasReachedEnd()){
            move();
            moveO2ToDor();
        }
    }

    public boolean hasReachedEnd(){ return (Math.abs(loc[0]-goal[0])<20&&Math.abs(loc[1]-goal[1])<20); }



    public void reset(){
        goal=new float[]{(float)(Math.random()*WIDTH*.66)+(WIDTH/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};
        loc=new float[]{(float)(Math.random()*WIDTH*.66)+(WIDTH/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};

        timeWitho2=1;
        o=(float)(Math.random()*3.14);
        o1=(float)(Math.random()*3.14/10);
        o2=(float)(Math.random()*3.14/10);
        oldlocs=new ArrayList<>();
        oldlocs.add(loc);

    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME
            //UPDATES
            repaint();//UPDATES FRAME
            try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }


    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            move();
            moveO2ToDor();
        }
        if (e.getKeyCode()==KeyEvent.VK_R){
            reset();
        }
        if (e.getKeyCode()==KeyEvent.VK_F){
            finish();
        }
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID

}