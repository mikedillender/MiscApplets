import com.sun.javafx.geom.Vec2f;

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
    float timer=1;
    ArrayList<Node> n;
    ArrayList<ArrayList<Node>> ss;
    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        n=new ArrayList<>();
        ss=new ArrayList<>();
        gfx=img.getGraphics();
        createAll();
        thread=new Thread(this);
        thread.start();
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        //paintCoordGrid(gfx);
        gfx.setColor(Color.BLACK);
        /*for (Node no:n){
            for (Node cn:no.connected){
                gfx.drawLine((int)(no.pos.x),(int)(no.pos.y),(int)(cn.pos.x),(int)(cn.pos.y));
            }
        }*/
        int l1=0;
        for (ArrayList<Node> s:ss){
            l1++;
            int i=0;
            int[] x=new int[s.size()];
            int[] y=new int[s.size()];

            for (Node sn: s){
                x[i]=(int)(sn.pos.x);
                y[i]=(int)(sn.pos.y);
                i++;
            }
            gfx.setColor(new Color((l1*255/ss.size())/2,(l1*255/ss.size())/2,(l1*255/ss.size())));
            gfx.fillPolygon(x,y,s.size());
        }
        //RENDER FOREGROUND


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void createAll(){
        int pw=15;
        int py=10;
        for (int y=0; y<py; y++){
            boolean out=(y==0||y+1==py);
            for (int x=0;x <pw; x++){
                boolean out1= (out) ||( x == 0 )||( x + 1 == pw);
                System.out.println("x = "+x+", y = "+y+" | out = "+out);
                Node nw=new Node(new Vec2f(x*((float)(WIDTH+100f)/(pw-1))-50,y*((float)(HEIGHT+100f)/(py-1))-50),new Vec2f((float)(Math.random()*14-7),(float)(Math.random()*14-7)),out1);
                nw.connectClosest(2,n);
                n.add(nw);
            }
        }
        for (Node n1:n){
            ss.add(findShape(n1));

        }
    }

    public ArrayList<Node> findShape(Node n){
        //ArrayList<A>
        ArrayList<Node> s=new ArrayList<>();
        if (n.connected.size()>1) {
            s.add(n);
            for (Node n1:n.connected){
                s.add(n1);
            }
        }
        return s;
    }

    public void addShape(){
        int size=3+(int)(Math.random()*3);
        ArrayList<Node> s=new ArrayList<>();
        int rx=(int)(0);
        int ry=(int)(Math.random()*HEIGHT);
        for (int i=0; i<size; i++){
            /*Node sn=new Node(new Vec2f(rx-50+(int)(Math.random()*100),ry-50+(int)(Math.random()*100)),new Vec2f((float)(Math.random()*13+7),(float)(Math.random()*18-9)));
            for (int z=0; z<s.size(); z++){
                sn.connectTo(s.get(z));
            }
            s.add(sn);*/
        }
        for (Node sn:s){
            sn.connectClosest(5,n);
            n.add(sn);
        }
        ss.add(s);
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

            //UPDATES
            timer-=.015f;
            if (timer<0){
                timer=.5f;
                //if (n.size()>4) {
                //    ss.add(findShape(n.get((int) (n.size() * Math.random()))));
                //}
                //addShape();
                //Node nw=new Node(new Vec2f(0,(float)(Math.random()*HEIGHT)),new Vec2f((float)(Math.random()*13+7),(float)(Math.random()*18-9)));
                //nw.connectClosest(4,n);
                //n.add(nw);
            }
            for (Node no:n){
                no.update(.015f);
            }

            repaint();//UPDATES FRAME
            try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }


    //INPUT
    public void keyPressed(KeyEvent e) {
        //addShape();
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