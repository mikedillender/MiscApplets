import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener {

    private final int sWIDTH=800, sHEIGHT=800;
    int ppt=16;
    private final int WIDTH=sWIDTH/ppt, HEIGHT=sHEIGHT/ppt;
    private Thread thread;
    Graphics gfx;
    Image img;

    float[][] map;
    int vectors=0;
    boolean done=false;
    int movetimer=20;
    ArrayList<Float> ranges;


    public void init(){//STARTS THE PROGRAM
        this.resize(sWIDTH, sHEIGHT);
        img=createImage(sWIDTH,sHEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        this.addKeyListener(this);
        map=new float[WIDTH][HEIGHT];

    }

    public void paint(Graphics g){
        draw(gfx);
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME
        if (!done) {
            if (vectors < 10) {
                movetimer -= 1;
                if (movetimer < 0) {
                    //addVector();
                    movetimer = 20;
                }
            } else {
                done=true;

            }
        }
        repaint();//UPDATES FRAME
        try{ Thread.sleep(20); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }



    public void draw(Graphics gfx){
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                gfx.setColor(new Color((int)(map[x][y]*255),(int)(map[x][y]*255),(int)(map[x][y]*255)));
                gfx.fillRect(x*ppt , sHEIGHT-(y*ppt) , ppt, ppt);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}