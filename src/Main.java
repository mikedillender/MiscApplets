import com.sun.javafx.geom.Vec2d;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener, MouseListener {

    //BASIC VARIABLES
    private final int WIDTH=1600, HEIGHT=1000;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;


    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(0);
    Color goalColor=new Color(255,0,0);
    Color start=new Color(0,0,255);
    Color cup = new Color(0,255,0);

    boolean isRunning=false;
    boolean straightening=false;

    int timeWitho2;
    float[] goal;

    boolean mouseControlOn=false;

    float totaldist=0;
    int movesPerRef=80;
    ArrayList<float[]> oldlocs;
    float[] loc;
    float[] color;
    float carlength=20;
    float carspeed=3;
    float initorient=0;
    int dist=0;
    float o=0;//orientation
    float o1=0;//angle of wheels

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        gfx.setFont(new Font("Arial",Font.BOLD,18));
        thread.start();
        addMouseListener(this);
        reset(true);
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        renderTrack(gfx);
        renderRope(gfx);
        gfx.drawString("ang of attack = "+initorient+", wheelangle = "+o1, 100,130);
        gfx.drawString("dist = "+dist+", total dist = "+totaldist, 100,170);
        //gfx.drawString("speed = "+movesPerRef, 100,130);
        //RENDER FOREGROUND


        //FINAL
        g.drawImage(img,0,0,this);
    }

    boolean lines=true;
    public void renderRope(Graphics g){
        g.setColor(gridColor);
        for (int i=0; i<oldlocs.size()-1; i++){
            //Color col=new Color((int)(oldcolors.get(i)[0]),(int)(oldcolors.get(i)[1]),(int)(oldcolors.get(i)[2]));
            //g.setColor(col);
            if (lines){
                g.drawLine((int) oldlocs.get(i)[0], (int) oldlocs.get(i)[1], (int) oldlocs.get(i+1)[0], (int) oldlocs.get(i+1)[1]);
            }else {
                g.fillRect((int) oldlocs.get(i)[0], (int) oldlocs.get(i)[1], 5, 5);
            }
        }
    }

    public void renderTrack(Graphics gfx){
        gfx.setColor(start);
        gfx.fillRect(400, HEIGHT/2,10,10);
        gfx.setColor(new Color(255,125,150));
        gfx.drawLine(400,HEIGHT/2,1200,HEIGHT/2);
        gfx.setColor(new Color(120,205,120));
        gfx.drawLine(400,HEIGHT/2-25,1200,HEIGHT/2-25);
        gfx.setColor(goalColor);
        gfx.fillRect((int)goal[0],(int)goal[1],10,10);

    }
    public void update(Graphics g){ //REDRAWS FRAME

        paint(g);
    }

    public void move(){
        //float dor=(float)(Math.atan((goal[1]-loc[1])/(goal[0]-loc[0])));
        //if ((goal[0]-loc[0])<0){ dor=dor+3.14f; }
        //o=o%6.28f;

        o=o+(o1*carspeed/carlength);

        oldlocs.add(loc);
        //o=dor;
        totaldist=totaldist+carspeed;
        loc=new float[]{loc[0]+(float)(Math.cos(o)*carspeed),loc[1]+(float)(Math.sin(o)*carspeed)};
    }



    private void finish(){
        long startt=System.currentTimeMillis();
        while (!hasReachedEnd()&&System.currentTimeMillis()<startt+2000){
            move();
            //moveO2ToDor();
        }
    }

    public boolean hasReachedEnd(){ return (Math.abs(loc[0]-goal[0])<20&&Math.abs(loc[1]-goal[1])<20); }



    public void reset(boolean full){
        //goal=new float[]{(float)(Math.random()*WIDTH*.66)+(WIDTH/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};
        //loc=new float[]{(float)(Math.random()*WIDTH*.66)+(WIDTH/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};

        //goal=new float[]{(float)-(Math.random()*WIDTH*.1)+(WIDTH/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};
        //loc=new float[]{(float)(Math.random()*WIDTH*.1)+(WIDTH*5/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};
        if (full){
            loc=new float[]{400,HEIGHT/2};
            goal=new float[]{1200,(float)HEIGHT/2};
            dist=(int)(goal[0]-loc[0]);
            totaldist=0;
        }else {
            loc=oldlocs.get(0);
            totaldist=0;
        }

        timeWitho2=1;
        o=(float)-(Math.random()*3.14159/4);
        initorient=o;
        o1=(float)(Math.random()*3.14/50);
        if (o>0){o1=-o1;}
        oldlocs=new ArrayList<>();
        oldlocs.add(loc);
        color =new float[]{
                (float)(Math.random()*200),
                (float)(Math.random()*200),
                (float)(Math.random()*200)
        };

    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME
            //UPDATES
            if (isRunning){
                for (int i=0; i<movesPerRef; i++) {
                    if (hasReachedEnd()){
                        System.out.println("ang = "+initorient+" ; wheels = "+o1);
                        reset(true);
                        //isRunning=false;
                    }else {
                        if (totaldist>dist*2){
                            reset(false);
                        }
                    }
                    move();
                }

            if (straightening) {
                straighten();
            }
            //straighten();
            }
            repaint();//UPDATES FRAME
            try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    public void straighten(){
        int r=1;
        for (int i=r; i<oldlocs.size()-r;i++){
            float sumx=0;
            float sumy=0;
            for (int z=i-r; z<=i+r; z++){
                sumx+=oldlocs.get(z)[0];
                sumy+=oldlocs.get(z)[1];
            }
            sumx=sumx/(2*r+1);
            sumy=sumy/(2*r+1);
            oldlocs.set(i, new float[]{sumx,sumy});
        }
    }

    private void pullDown(){
        int r=3;
        float weight=.3f;
        for (int i=oldlocs.size()-1; i>0;i--){
            float sumx=0;
            float sumy=0;
            float miny=0;
            int amt=0;
            for (int z=i-r; z<=i+r; z++){
                if (z<1||z>=oldlocs.size()){continue;}
                amt++;
                sumx+=oldlocs.get(z)[0];
                sumy+=oldlocs.get(z)[1];
                if (oldlocs.get(z)[1]>miny){
                    miny=oldlocs.get(z)[1];
                }
            }
            sumx=sumx/amt;
            sumy=sumy/amt;
            sumy=(sumy+(weight*miny))/(1+weight);
            oldlocs.set(i, new float[]{oldlocs.get(i)[0],sumy});
        }
    }

    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            reset(false);
        }
        if (e.getKeyCode()==KeyEvent.VK_R){
            reset(true);
        }
        if (e.getKeyCode()==KeyEvent.VK_P){
            isRunning=!isRunning;
        }
        if (e.getKeyCode()==KeyEvent.VK_LEFT){
            movesPerRef--;
        }
        if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            movesPerRef++;
        }

    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) { if(mouseControlOn){goal=new float[]{e.getX(), e.getY()};} }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) { if(mouseControlOn){isRunning=true;} }

    @Override
    public void mouseExited(MouseEvent e) { if(mouseControlOn){isRunning=false;} }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID

}