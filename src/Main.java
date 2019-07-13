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
    private final int WIDTH=1280, HEIGHT=900;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(150, 150,150);

    Vec2d s=new Vec2d(75,HEIGHT*.75f);
    Vec2d e=new Vec2d(1000,200);
    ArrayList<Vec2d> rope=new ArrayList<>();
    float L=(float)(Math.sqrt((WIDTH*WIDTH)+((HEIGHT*HEIGHT)/4f)))*.8f;
    float amt=0;
    float sbdy=-20;


    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        this.addMouseListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        setRopeStraight();
        thread=new Thread(this);
        thread.start();
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        //paintCoordGrid(gfx);
        paintRope(gfx);
        //RENDER FOREGROUND


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }


    int timer=0;
    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

            //UPDATES
        timer--;
        if (timer<0){
            timer=10;
            sbdy++;
            //stretchAndSag(amt);
            //sbdy+=20;
            setRope(sbdy);
            //setRopeLength();
        }


            repaint();//UPDATES FRAME
            try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    public void paintRope(Graphics g){
        g.setColor(Color.BLACK);
        if (rope.size()==0){
            g.fillRect((int)s.x,(int)s.y,5,5);
            g.fillRect((int)e.x,(int)e.y,5,5);
        }else {
            for (Vec2d v: rope){
                g.fillRect((int)v.x,(int)v.y,5,5);
            }
            for (int i=1; i<rope.size();i++){
                g.drawLine((int)rope.get(i-1).x,(int)rope.get(i-1).y,(int)rope.get(i).x,(int)rope.get(i).y);
            }
        }

    }


    public void setRope(float bdy){
        if (sbdy>=0){
            bdy=0;
        }
        rope=new ArrayList<>();
        //System.out.println("doing "+bdy);
        float maxdy= (float) Math.abs(e.y-s.y)/2f;
        if (maxdy<bdy){ System.out.println("too large");return;}
        int numPoints=30;
        float dx= (float) (e.x-s.x)/2f;
        Vec2d b=new Vec2d(s.x+dx, s.y-bdy);
        float dy= (float) (s.y-b.y);

        //rope.add(s);
        if (bdy<=0) {
            for (int i = 1; i < numPoints; i++) {
                float x = (((1 - (i / (float) numPoints)) * dx));
                float y2 = (1 - ((x * x) / (dx * dx))) * (dy * dy);
                rope.add(new Vec2d(s.x + dx - x, s.y + (float) (Math.sqrt(Math.abs(y2)))));
            }
            dy = (float) (e.y - b.y);
            for (int i = 0; i < numPoints; i++) {
                float x = ((((i / (float) numPoints)) * dx));
                float y2 = (1 - ((x * x) / (dx * dx))) * (dy * dy);
                rope.add(new Vec2d(s.x + dx + x, e.y + (float) (Math.sqrt(Math.abs(y2)))));
            }
        }

        /*if (sbdy>0){
            int attempts=0;
            float lcsbdy=getCsbdy();
            while (Math.abs(lcsbdy-sbdy)>1&&attempts<100){
                average();
                if (lcsbdy<sbdy){
                    //stretchAndSag(-1);
                }else {
                    //stretchAndSag(1);
                }
                lcsbdy=getCsbdy();
            }
        }*/
        //if (sbdy>0){

        //}
        /*else {
            dx*=2;
            dy= (float) (e.y-s.y);
            numPoints*=2;
            for (int i = 0; i < numPoints; i++) {
                float x = ((((i / (float) numPoints)) * dx));
                float y2 = (1 - ((x * x) / (dx * dx))) * (dy * dy);
                rope.add(new Vec2d(s.x + x, e.y + (float) (Math.sqrt(Math.abs(y2)))));
            }*/

        //}
        //rope.add(e);
        //setRopeStraight();
        int attempts=0;
        float lcsbdy=getCsbdy();
        while (Math.abs(lcsbdy-sbdy)>1&&attempts<100){
            if (lcsbdy<sbdy){
                stretchAndSag(-1);
            }else {
                stretchAndSag(1);
            }
            lcsbdy=getCsbdy();
        }
    }

    public void setRopeStraight(){
        rope=new ArrayList<>();
        int numPoints=30;
        float dx= (float) (e.x-s.x);
        float dy= (float) (e.y-s.y);
        float m=(dy/dx);
        for (int i=0; i<numPoints;i++){
            float x=((float)i/numPoints)*dx;
            float y=m*x;
            rope.add(new Vec2d(s.x+x,s.y+y));
        }


    }


    public float getCsbdy(){
        if (rope==null){return sbdy;}
        if (rope.size()==0||rope.size()<5){return sbdy;}
        int size=rope.size();
        float y= (float) (s.y-rope.get(size/2).y);
        return y;
    }

    public void stretchAndSag(float amt){
        //float amt=3;
        float halfsize=rope.size()/2f;
        for (int i=1; i<rope.size()-1; i++){
            float dist=(float)(1-Math.pow(Math.abs(i-halfsize)/halfsize,1.4))*amt;
            //System.out.println("i"+i+" = "+dist);
            rope.get(i).y+=dist;
        }
    }

    public float getRopeL(){
        float l=0;
        for (int i=1; i<rope.size(); i++){
            float dx= (float) (rope.get(i).x-rope.get(i-1).x);
            float dy= (float) (rope.get(i).y-rope.get(i-1).y);
            float d=(float)(Math.sqrt(dx*dx+dy*dy));
            l=l+d;
        }
        //System.out.println(l);
        return l;
    }

    public void average(){
        if (rope.size()==0){return;}
        ArrayList<Vec2d> newr=new ArrayList<>();
        newr.add(rope.get(0));
        for (int i=1; i<rope.size()-1; i++){
            float y= (float) (rope.get(i).y+rope.get(i-1).y+rope.get(i+1).y)/3f;
            newr.add(new Vec2d(rope.get(i).x,y));
        }
        newr.add(rope.get(rope.size()-1));
        rope=newr;
    }



    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_S) {
            setRopeStraight();
        }else if (e.getKeyCode()==KeyEvent.VK_SPACE){
            average();
        }else if(e.getKeyCode()==KeyEvent.VK_L){
            System.out.println(getRopeL());
        }else if(e.getKeyCode()==KeyEvent.VK_G){
            stretchAndSag(amt);
        }else if(e.getKeyCode()==KeyEvent.VK_UP){
            sbdy-=10;
            amt--;
        }else if(e.getKeyCode()==KeyEvent.VK_DOWN){
            sbdy-=10;
            amt++;
        }
        System.out.println("sbdy = "+sbdy+" = "+getCsbdy());

        //System.out.println(amt);
        //System.out.println("BDY = "+sbdy+", length = "+getRopeL()+" / "+L);
        //System.out.println("");
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

    @Override
    public void mouseClicked(MouseEvent e) {
        //this.e=new Vec2d(e.getX(),e.getY());
        //setRopeLength();
        //setRope(sbdy);
        //while (Math.abs(getRopeL()-L)>50){
        //    setRope((float) (-HEIGHT+(2*Math.random()*HEIGHT)));
        //}
        System.out.println("BDY = "+sbdy+", length = "+getRopeL()+" / "+L);
        System.out.println("");
    }

    public void setRopeLength(){
        setRope(sbdy);
        float lastL=getRopeL();
        int attempts=0;
        while (Math.abs(lastL-L)>50&&attempts<1000){
            attempts++;
            if (sbdy>0&&L<lastL){
                average();
            }else if (lastL<L){
                sbdy-=10;
                setRope(sbdy);
            }else {
                sbdy+=10;
                setRope(sbdy);
            }
            average();
            average();

            System.out.println("BDY = "+sbdy+", length = "+getRopeL()+" / "+L);
            lastL=getRopeL();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}