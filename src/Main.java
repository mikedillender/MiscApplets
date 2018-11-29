import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener {

    private final int sWIDTH=800, sHEIGHT=900;
    int ppt=16;
    private final int WIDTH=sWIDTH/ppt, HEIGHT=sHEIGHT/ppt;
    private Thread thread;
    Graphics gfx;
    Image img;


    Node[] nodes;

    public void init(){//STARTS THE PROGRAM
        this.resize(sWIDTH, sHEIGHT);
        img=createImage(sWIDTH,sHEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        this.addKeyListener(this);
        nodes=new Node[25];
        for (int i=0; i<nodes.length; i++){
            nodes[i]=new Node(i);
        }
    }

    public void paint(Graphics g){
        draw(gfx);
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

        repaint();//UPDATES FRAME
        try{ Thread.sleep(20); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }



    public void draw(Graphics gfx){
        for (int i=0; i<nodes.length; i++){
            gfx.drawString(i+"",100*nodes[i].getPos()[0],100*nodes[i].getPos()[1]);
            gfx.drawRect(100*nodes[i].getPos()[0],100*nodes[i].getPos()[1], 10,10);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}