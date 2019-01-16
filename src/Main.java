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


    float[] world;
    int points=100;

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        world=new float[points];
        for (int x=0; x<points; x++){
            world[x]=100;
        }
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
    }

    public void change(){
        for (int x=0; x<points; x++){
            float change=(float)(Math.random()*4)-2;
            world[x]+=change;
        }
    }
    public void smooth(){
        for (int x=0; x<points; x++){
            int samples=0;
            float sum=0;
            for (int x1=x-1; x1<=x+1; x1++){
                if (exists(x1)){
                    samples++;
                    sum+=world[x1];
                }if (x1>=points){
                    samples++;
                    sum+=world[0];
                }else if (x1<0){
                    samples++;
                    sum+=world[world.length+x1];
                }

            }
            world[x]=sum/samples;
        }
    }
    private boolean exists(int i){
        if (i>0&&i<points){return true;}
        return false;
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        renderCircle(gfx);
        change();
        smooth();

        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void renderCircle(Graphics g){
        float rad=200;
        int cx=WIDTH/2;
        int cy=HEIGHT/2;
        g.setColor(Color.BLACK);
        ArrayList<int[]> p=new ArrayList<>();
        for (int i=0; i<points; i++){
                float o=((float)i/points)*6.28f;
                float x=cx+(int)(world[i]*Math.cos(o));
                float y=cy+(int)(world[i]*Math.sin(o));
                p.add(new int[]{(int)x,(int)y});
        }
        for (int i=0; i<p.size()-1; i++){
            g.drawLine(p.get(i)[0],p.get(i)[1],p.get(i+1)[0],p.get(i+1)[1]);
        }
        g.drawLine(p.get(0)[0],p.get(0)[1],p.get(p.size()-1)[0],p.get(p.size()-1)[1]);

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

    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

}