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
    Color gridColor=new Color(150, 150,150);

    ArrayList<float[]> smoke;
    float direction=0;
    Vec2d valve;


    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        reset(0);
        thread=new Thread(this);
        thread.start();
    }

    public void reset(float direction){
        this.direction=direction;
        smoke=new ArrayList<>();
        valve=new Vec2d(WIDTH/2f,HEIGHT/2f);
    }

    public void addRandom(){
        float o= (float) (direction+(((Math.random()<.5f)?1:-1)*(Math.random()*Math.random()*3.14159/6)));
        //System.out.println("d = "+direction+", o = "+o);
        float v=4;
        float size=(float) (50+(((Math.random()<.5f)?1:-1)*(Math.random()*Math.random()*30)));
        float[] s=new float[]{(float) valve.x, (float) valve.y, (float)(Math.cos(o)*v), (float)(Math.sin(o)*v), 2,size};
        smoke.add(s);
    }

    int timer=10;

    public void move() {
        for (float[] s : smoke) {
            s[4]-=.015f;
            s[0]+=s[2];
            s[1]+=s[3];
            s[3]-=.1f;
            s[5]+=.5f;
        }
        for (int i=0; i<smoke.size(); i++){
            if (smoke.get(i)[4]<0){
                smoke.remove(i);
                i--;
            }
        }
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        //paintCoordGrid(gfx);

        /*timer--;
        if (timer<0){
            timer=10;*/
            addRandom();
        //}
        move();
        //RENDER FOREGROUND
        for (float[] s: smoke){
            float c=.5f+.5f*((1-(s[4]/2f)));
            //System.out.println(c);
            gfx.setColor(new Color(c,c,c));
            gfx.fillOval((int)s[0],(int)s[1],(int)s[5],(int)s[5]);
        }
        gfx.setColor(Color.BLACK);
        gfx.drawString("size : "+smoke.size(), 100,100);

        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

            //UPDATES


            repaint();//UPDATES FRAME
            try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }


    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_UP){
            direction=3.14f/2f;
            reset(direction);
        }if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            direction=0;
            reset(direction);
        }if (e.getKeyCode()==KeyEvent.VK_DOWN){
            direction=(3.14159f/2)*3;
            reset(direction);
        }if (e.getKeyCode()==KeyEvent.VK_LEFT){
            direction=3.14159f;
            reset(direction);
        }
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID
    public void paintCoordGrid(Graphics gfx){
        gfx.setColor(gridColor);
        for (int x=100; x<WIDTH; x+=100){
            gfx.drawString(""+x, x, 20);
            gfx.drawRect(x, 20, 1, HEIGHT);
        }
        for (int y=100; y<HEIGHT; y+=100){
            gfx.drawString(""+y, 20, y);
            gfx.drawRect(20, y, WIDTH, 1);
        }
    }
}