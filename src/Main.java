import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    private final int WIDTH=1280, HEIGHT=900;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;
    long timeOfLastPull=0;
    boolean isOn=true;

    long msBetweenPulls=500;
    float pullRange=30;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(0);
    float[] rope=new float[100];

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        timeOfLastPull=System.currentTimeMillis();
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        renderRope(gfx);
        //RENDER FOREGROUND


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void renderRope(Graphics g){
        g.setColor(gridColor);
        int psize=WIDTH/rope.length;
        int y=HEIGHT/2;
        for (int i=0; i<rope.length; i++){
            g.drawRect(psize*i,(int)(y+(psize*rope[i])), psize, psize);
        }
    }

    public void update(Graphics g){ //REDRAWS FRAME
        if (isOn) {
            long time = System.currentTimeMillis();
            long dt = time - timeOfLastPull;
            if (dt > msBetweenPulls) {
                float str = -(pullRange/2f)+(float)(Math.random()*pullRange);
                int loc = (int) (Math.random() * rope.length);
                addPull(loc, str);
                timeOfLastPull = time;
            }
        }
        paint(g);
    }

    private void addPull(int x, float str) {
        System.out.println("pulling "+x+" with a strength of "+str);
        for (int i = 0; i < rope.length; i++) {
            float dist=(float)(Math.abs(x-i));
            float mult=(dist>0)?1/dist:1.2f;
            rope[i]=rope[i]+(str*mult);
        }
    }

    private void normalize(){
        for (int i=1; i<rope.length-1; i++){
            rope[i]=(rope[i]+rope[i-1]+rope[i+1])/3f;
        }
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

            //UPDATES


            repaint();//UPDATES FRAME
            try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }


    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_P){
            isOn=!isOn;
        }
        if (e.getKeyCode()==KeyEvent.VK_N){
            normalize();
        }
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID

}