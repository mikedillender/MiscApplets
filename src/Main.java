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
    Color clickcol=new Color(100,100,200);
    ArrayList<float[]> clicks=new ArrayList<>();



    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        this.addMouseListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();

    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        Color epilepsy=new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        //paintCoordGrid(gfx);
        gfx.setColor(new Color(0,0,0));
        gfx.setFont(gfx.getFont().deriveFont(70f));

        gfx.setColor(epilepsy);
        gfx.drawString("NOTES",WIDTH/3,HEIGHT/10);

        gfx.setFont(gfx.getFont().deriveFont(50f));

        gfx.drawString("the environment is important",WIDTH/5,HEIGHT/10+60);
        //RENDER FOREGROUND
        gfx.setColor(clickcol);
        if (clicks.size()>=2) {
            float[] last = clicks.get(0);
            for (float[] c : clicks) {
                gfx.drawLine((int)last[0],(int)last[1],(int)c[0],(int)c[1]);
                last=c;
            }
        }

        //FINAL
        g.drawImage(img,0,0,this);
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

    }

    @Override
    public void mousePressed(MouseEvent e) {
        clicks.add(new float[]{e.getX(), e.getY()});
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