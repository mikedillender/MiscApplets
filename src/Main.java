import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    final int WIDTH=1280, HEIGHT=900;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color sprite=new Color(0,0,0);
    Color gridColor=new Color(150, 150,150);

    ArrayList<body> b;
    int mode=4;

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        b=new ArrayList<>();

        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size

        //RENDER FOREGROUND
        gfx.setColor(sprite);
        for (int i=0; i<b.size(); i++){
            b.get(i).draw(gfx);
        }

        //FINAL
        g.drawImage(img,0,0,this);
    }

    private void addBody(){
        b.add(new body((int)(Math.random()*WIDTH),(int)(Math.random()*HEIGHT), (-1+(2*Math.random()))*.00001,(-1+(2*Math.random()))*.00001, (float)(Math.random()*5)+1));
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
        for (int i=0; i<b.size(); i++){
            b.get(i).update(b, this);
        }
        if (mode==1||mode==2) {
            for (body i : b) {
                for (body z : b) {
                    if (z != i) {
                        if (i.isSurroundingB(z)) {
                            if (mode == 1) {
                                i.mass = (float) (Math.sqrt(Math.pow(i.mass, 2) + Math.pow(z.mass, 2)));
                                i.vx = (i.vx * i.mass + z.vx * z.mass) / (i.mass + z.mass);
                                i.vy = (i.vy * i.mass + z.vy * z.mass) / (i.mass + z.mass);
                                b.remove(z);
                            }
                        }
                    }
                }
            }
        }
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

            //UPDATES


            repaint();//UPDATES FRAME
            try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    public body doesCollide(body b, float nx, float ny){
        for (float o=0; o<6.28f; o+=(3.14f/10f)){
            float px=nx+(float)(Math.cos(o)*(b.size/2f));
            float py=ny+(float)(Math.sin(o)*(b.size/2f));
            for (body b1: this.b){
                if (b1!=b) {
                    if (isPointInBody(px, py, b1)) {
                        return b1;
                    }
                }
            }
        }
        return null;
    }

    public boolean doesCollideWithWall(body b, float nx, float ny){
        for (float o=0; o<6.28f; o+=(3.14f/10f)){
            float px=nx+(float)(Math.cos(o)*(b.size/2f));
            float py=ny+(float)(Math.sin(o)*(b.size/2f));
            if (py>HEIGHT||py<0||px>WIDTH||px<0){
                return true;
            }
        }
        return false;
    }

    public boolean isPointInBody(float x, float y, body b){
        float dx=(float)(Math.abs(x-b.x));
        float dy=(float)(Math.abs(y-b.y));
        float dist=(float)(Math.sqrt(dx*dx+dy*dy));
        if (dist<b.size/2){
            return true;
        }
        return false;
    }




    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_R){
            b=new ArrayList<>();
        }
        if (e.getKeyCode()==KeyEvent.VK_0){
            mode=0;
        }else if (e.getKeyCode()==KeyEvent.VK_1){
            mode=1;
        }else if (e.getKeyCode()==KeyEvent.VK_2){
            mode=2;
        }
        addBody();
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) { }
    public void updateControls(KeyEvent e, boolean pressed){
    }

}