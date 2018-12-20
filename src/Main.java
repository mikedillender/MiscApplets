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
    private final int WIDTH=3000, HEIGHT=2000;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;


    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(0);
    Color goalColor=new Color(255,0,0);

    boolean isRunning=false;
    boolean straightening=false;

    int timeWitho2;
    float[] goal;


    int movesPerRef=3;
    ArrayList<float[]> oldlocs;
    ArrayList<float[]> oldcolors;
    float[] loc;
    float[] color;
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
        addMouseListener(this);
        reset();
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        renderRope(gfx);
        gfx.drawString("o = "+o+", o1 = "+o1+", o2 = "+o2, 100,100);
        gfx.drawString("speed = "+movesPerRef, 100,130);
        gfx.setColor(goalColor);
        gfx.fillRect((int)goal[0],(int)goal[1],10,10);

        //RENDER FOREGROUND


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void renderRope(Graphics g){
        g.setColor(gridColor);
        for (int i=0; i<oldlocs.size(); i++){
            Color col=new Color((int)(oldcolors.get(i)[0]),(int)(oldcolors.get(i)[1]),(int)(oldcolors.get(i)[2]));
            g.setColor(col);

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
            resetO2();
        }
        o=o+o1;
        if (Math.abs(o1+o2)<3.14f*3/32) {
            o1 = o1 + o2;
        }else {
            resetO2();
        }

        float[] color2=new float[3];
        for (int i=0; i<3; i++){
            float change=-10+(float)(20*Math.random());
            if (color[i]+change>0&&color[i]+change<255){
                color2[i]=color[i]+change;
            }else {
                color2[i]=color[i];
            }
        }
        color=color2;

        oldlocs.add(loc);
        oldcolors.add(color);
        //o=dor;

        loc=new float[]{loc[0]+(float)(Math.cos(o)*speed),loc[1]+(float)(Math.sin(o)*speed)};
    }
    public void resetO2(){
        float dor=(float)(Math.atan((goal[1]-loc[1])/(goal[0]-loc[0])));
        if ((goal[0]-loc[0])<0){ dor=dor+3.14f; }

        float ddor=dor-o;//ddor = the change needed to get in line

        if (ddor>3.14){
            ddor=-3.14f+ddor;
        }

        float dddor=ddor-o1;

        o2=(float)(Math.random()*dddor/1);
        //o2=(float)(3.14/16*Math.random())-(3.14f/32);
        float rand=(float)(3.14/16*Math.random())-(3.14f/32);
        o2=(o2+rand)/2f;


        timeWitho2=(int)(Math.random()*15*Math.random())+1;
        //timeWitho2=(int)(Math.random()*(ddor/o2))+1;

    }



    private void finish(){
        long startt=System.currentTimeMillis();
        while (!hasReachedEnd()&&System.currentTimeMillis()<startt+2000){
            move();
            //moveO2ToDor();
        }
    }

    public boolean hasReachedEnd(){ return (Math.abs(loc[0]-goal[0])<20&&Math.abs(loc[1]-goal[1])<20); }



    public void reset(){
        //goal=new float[]{(float)(Math.random()*WIDTH*.66)+(WIDTH/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};
        //loc=new float[]{(float)(Math.random()*WIDTH*.66)+(WIDTH/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};

        //goal=new float[]{(float)-(Math.random()*WIDTH*.1)+(WIDTH/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};
        //loc=new float[]{(float)(Math.random()*WIDTH*.1)+(WIDTH*5/6),(float)(Math.random()*HEIGHT*.66)+(HEIGHT/6)};

        loc=new float[]{(float)(Math.random()*WIDTH*3/4)+(WIDTH/8),(float)-(Math.random()*HEIGHT*.1)+(HEIGHT/6)};
        goal=new float[]{(float)(Math.random()*WIDTH*3/4)+(WIDTH/8),(float)(Math.random()*HEIGHT*.1)+(HEIGHT*5/6)};

        timeWitho2=1;
        o=(float)(Math.random()*3.14);
        o1=(float)(Math.random()*3.14/10);
        o2=(float)(Math.random()*3.14/10);
        oldlocs=new ArrayList<>();
        oldlocs.add(loc);
        oldcolors=new ArrayList<>();
        color =new float[]{
                (float)(Math.random()*255),
                (float)(Math.random()*255),
                (float)(Math.random()*255)
        };
        oldcolors.add(color);

    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME
            //UPDATES
            if (isRunning){
                goal = new float[]{getMousePosition().getLocation().x, getMousePosition().getLocation().y};
                for (int i=0; i<movesPerRef; i++) {
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
            move();
        }
        if (e.getKeyCode()==KeyEvent.VK_R){
            reset();
        }
        if (e.getKeyCode()==KeyEvent.VK_F){
            finish();
        }
        if (e.getKeyCode()==KeyEvent.VK_S){
            straightening=!straightening;
            straighten();
        }
        if (e.getKeyCode()==KeyEvent.VK_G){
            pullDown();
        }
        if (e.getKeyCode()==KeyEvent.VK_P){
            isRunning=!isRunning;
        }
        if (e.getKeyCode()==KeyEvent.VK_LEFT){
            if (movesPerRef>0){
                movesPerRef--;
            }
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
    public void mousePressed(MouseEvent e) {
        goal=new float[]{e.getX(), e.getY()};
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        isRunning=true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isRunning=false;
    }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID

}