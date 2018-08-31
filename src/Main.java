import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends Applet implements Runnable, KeyListener {

    private final int WIDTH=1280, HEIGHT=900;
    private Thread thread;
    Graphics gfx;
    Image img;
    Color background=new Color(184, 184, 148);


    public void init(){
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        //CREATE OBJECTS

        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
    }


    public void paint(Graphics g){
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        //RENDER METHODS


        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){
        paint(g);
    }

    public void run() {
        for (;;){


            //UPDATES
            repaint();

            try{
                Thread.sleep(15);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("GAME FAILED TO RUN");
            }
        }
    }

    //@Override
    public void keyTyped(KeyEvent e) {

    }


    public void keyPressed(KeyEvent e) {

    }


    public void keyReleased(KeyEvent e) {

    }
}


