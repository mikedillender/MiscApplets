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
    Color progressBar=new Color(120,120,120);
    long startTime;
    int numQuestions=40;
    int numSections=4;
    int time=35;

    double[] pacing=new double[]{.61,3.05};

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
        double s=(int)(10*(dt-(60*min)))/10.0;
        double percent=dt/(time*60);
        int question=(int)(percent*numQuestions);
        double sec=percent*numSections;
        int csec=(int)Math.floor(sec)+1;
        double percentCSec=(100*((sec-Math.floor(sec))));

        gfx.drawString("                  Time = "+min+" : "+s, WIDTH/4, HEIGHT/3+50);
        gfx.drawString("Should be on section "+csec+" with "+(int)percentCSec+"% complete", WIDTH/4, HEIGHT/3+100);

        gfx.setColor(progressBar);
        gfx.fillRect(WIDTH/5, HEIGHT/3+200,(int)((dt/(time*60.0))*(WIDTH/5*3)),40);
        gfx.fillRect(WIDTH/5, HEIGHT/3+150,(int)((percentCSec/100.0)*(WIDTH/5*3)),40);

        renderBars(gfx);



        g.drawImage(img,0,0,this);
    }

    public void renderBars(Graphics g){
        g.setColor(black);
        g.drawRect(WIDTH/5, HEIGHT/3+150,WIDTH/5*3,40);
        g.drawRect(WIDTH/5, HEIGHT/3+150,(int)((pacing[0]/8.75)*(WIDTH/5*3)),40);
        g.drawRect(WIDTH/5, HEIGHT/3+150,(int)((pacing[1]/8.75)*(WIDTH/5*3)),40);
        g.drawRect(WIDTH/5, HEIGHT/3+200,WIDTH/5*3,40);
        for (int i=1; i<4; i++){
            g.drawRect(WIDTH/5, HEIGHT/3+200,(int)((i/4.0)*(WIDTH/5*3)),40);
        }
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