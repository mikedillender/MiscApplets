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
    double velx;
    double vely;

    //CONTROLS
    boolean pressingW;
    boolean pressingA;
    boolean pressingS;
    boolean pressingD;

    Snake snake=new Snake(WIDTH/2,HEIGHT/2,WIDTH, HEIGHT);

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
        gfx.fillRect(sx, sy, 20, 20);
        snake.render(gfx);

        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

        //UPDATES
        if (pressingW){
            vely-=.2;
        }
        if (pressingA){
            velx-=.1;
        }
        if (pressingD){
            velx+=.1;
        }
        vely+=.1;
        velx*=.97;
        sx+=velx;
        sy+=vely;
        if (sx>WIDTH-20){
            velx=0;
            sx=WIDTH-20;
        }
        if (sx<0){
            velx=0;
            sx=0;
        }
        if (sy>HEIGHT-20){
            sy=HEIGHT-20;
            vely=0;
        }
        if (sy<0){
            vely=0;
            sy=0;
        }
        snake.update(.015f);

        repaint();//UPDATES FRAME
        try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }


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