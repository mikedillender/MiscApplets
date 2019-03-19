import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener,Screen {

    //BASIC VARIABLES

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    //Color black=new Color(150, 150,150);

    Player p;
    Object o;
    ArrayList<Object> objects;

    public void init(){//STARTS THE PROGRAM
        this.resize(sWIDTH, sHEIGHT);
        this.addKeyListener(this);
        img=createImage(sWIDTH,sHEIGHT);
        gfx=img.getGraphics();
        p=new Player();
        objects=new ArrayList<>();
        objects.add(new Object(4,0,0,1));
        //objects.add(new Object(11,0,0,.5f));
        //objects.add(new Object(12,0,0,.25f));
        thread=new Thread(this);
        thread.start();
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,sWIDTH,sHEIGHT);//background size

        gfx.setColor(Color.BLACK);
        float deg=p.ox*180/3.14f;
        gfx.drawString("orient = "+p.ox+" ("+deg+")",10,20);
        //RENDER FOREGROUND
        //o.render(gfx,p);
        for (Object o:objects){
            o.render(gfx,p);
            System.out.println("rendering object");
        }
        System.out.println("done render");
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
        if (e.getKeyCode()==KeyEvent.VK_W){
            p.control(0);
        } else if (e.getKeyCode()==KeyEvent.VK_D){
            p.control(1);
        } else if (e.getKeyCode()==KeyEvent.VK_S){
            p.control(2);
        } else if (e.getKeyCode()==KeyEvent.VK_A){
            p.control(3);
        } else if (e.getKeyCode()==KeyEvent.VK_UP){
            p.control(4);
        } else if (e.getKeyCode()==KeyEvent.VK_DOWN){
            p.control(5);
        } else if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            p.control(6);
        } else if (e.getKeyCode()==KeyEvent.VK_LEFT){
            p.control(7);
        }
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }


}