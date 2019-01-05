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

    //COLORS
    Color background=new Color(255, 255, 255);
    Color black=new Color(0,0,0);
    long startTime;
    int numQuestions=40;
    int numSections=4;
    int time=35;

    boolean paused=false;
    long pauseTime=0;

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        startTime=System.nanoTime();
        Font currentFont = gfx.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 3F);
        gfx.setFont(newFont);
    }

    public void paint(Graphics g){
        if (paused){return;}

        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        gfx.setColor(black);

        double dt=(System.nanoTime()-startTime)/1000000000.0;
        int min=((int)dt/60);
        int s=(int)(dt-(60*min));
        double percent=dt/(time*60);
        int question=(int)(percent*(numQuestions));
        double sec=percent*numSections;
        int csec=(int)Math.floor(sec);
        int percentCSec=(int)(100*((sec-Math.floor(sec))));
        gfx.drawString("                  Time = "+min+" : "+s, WIDTH/5, HEIGHT/2);
        gfx.drawString("          Should be on question "+question, WIDTH/5, HEIGHT/2+50);
        gfx.drawString("Should be on section "+csec+" with "+percentCSec+"% complete", WIDTH/5, HEIGHT/2+100);

        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME
        repaint();//UPDATES FRAME
        try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }


    public void keyPressed(KeyEvent e) {
        if (!paused){
            pauseTime=System.nanoTime();
        }else {
            startTime=startTime+(System.nanoTime()-pauseTime);
        }
        paused=!paused;
    }
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }
}