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
    Color sprite=new Color(0,0,0);
    Color gridColor=new Color(150, 150,150);

    //"CHARACTER"
    int sx=WIDTH/2;
    int sy=HEIGHT/2;

    //CONTROLS
    boolean pressingW;
    boolean pressingA;
    boolean pressingS;
    boolean pressingD;

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        paintCoordGrid(gfx);

        //RENDER FOREGROUND
        gfx.setColor(sprite);
        gfx.fillRect(sx, sy, 10, 10);


        //FINAL
        g.drawImage(img,0,0,this);
    }


    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

            //UPDATES
            if (pressingW){
                sy--;
            }else if (pressingS){
                sy++;
            }
            if (pressingA){
                sx--;
            }else if (pressingD){
                sx++;
            }

            repaint();//UPDATES FRAME
            try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    public void update(Graphics g){ paint(g); }//REDRAWS FRAME


    //INPUT
    public void keyPressed(KeyEvent e) { updateControls(e, true); }
    public void keyReleased(KeyEvent e) { updateControls(e, false); }
    public void keyTyped(KeyEvent e) { }
    public void updateControls(KeyEvent e, boolean pressed){
        if (e.getKeyCode()==KeyEvent.VK_W){
            pressingW=pressed;
        }else if (e.getKeyCode()==KeyEvent.VK_A){
            pressingA=pressed;
        }else if (e.getKeyCode()==KeyEvent.VK_S){
            pressingS=pressed;
        }else if (e.getKeyCode()==KeyEvent.VK_D){
            pressingD=pressed;
        }
    }

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