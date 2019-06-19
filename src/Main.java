import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener, MouseListener {
    private final int WIDTH=1680, HEIGHT=1200;
    private Thread thread;
    Graphics gfx;
    Image img;
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(150, 150,150);
    ArrayList<float[]> moles;
    int score=0; float rad=20; float timer=1;

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        addMouseListener(this);
        moles=new ArrayList<>();
        thread=new Thread(this);
        thread.start();
    }

    public void addMole(){
        float x=(float)(Math.random()*WIDTH);
        float y=(float)(Math.random()*HEIGHT);
        float time=(int)(Math.random()*4)+1f;
        moles.add(new float[]{x,y,time});
    }

    public void renderMoles(Graphics gfx){
        gfx.setFont(gfx.getFont().deriveFont(25f));
        gfx.setColor(new Color(0,0,0));
        for (float[] m:moles){
            gfx.fillOval((int)m[0]-((int)rad),(int)m[1]-((int)rad),(int)(rad*2),(int)(rad*2));
        }
        gfx.drawString("score: "+score,50,50);
    }
    public void paint(Graphics g){
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        renderMoles(gfx);
        g.drawImage(img,0,0,this);
    }
    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
        for (int i=0; i<moles.size(); i++){
            moles.get(i)[2]-=.015f;
            if (moles.get(i)[2]<=0){
                moles.remove(moles.get(i));
                i--;
            }
        }
        timer-=.015f;
        if (timer<0){
            addMole();
            timer=(float)(Math.random()*5);
        }
    }
    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME
            repaint();//UPDATES FRAME
            try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }
    public void mouseClicked(MouseEvent e) {
        System.out.println("clicked");
        float x=e.getX();
        float y=e.getY();
        for (float[] m: moles){
            float dx=x-m[0];
            float dy=y-m[1];
            float dist=(float)(Math.sqrt((dx*dx)+(dy*dy)));
            System.out.println("dist "+dist);
            if (dist<rad){
                moles.remove(m);
                score++;
                System.out.println("hit");
            }
        }
    }

    //INPUT
    public void keyPressed(KeyEvent e) { }
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }


    @Override
    public void mousePressed(MouseEvent e) {

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