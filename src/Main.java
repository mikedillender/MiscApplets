import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

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

    ArrayList<body> b;
    int mode=0;


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
            b.get(i).update(b);
        }
        for (body i:b){
            for (body z: b){
                if (z!=i){
                    if (i.isSurroundingB(z)){
                        if (mode==1) {
                            i.mass = (float) (Math.sqrt(Math.pow(i.mass, 2) + Math.pow(z.mass, 2)));
                            i.vx = (i.vx * i.mass + z.vx * z.mass) / (i.mass + z.mass);
                            i.vy = (i.vy * i.mass + z.vy * z.mass) / (i.mass + z.mass);
                            b.remove(z);
                        }else if (mode==2){
                            float energyGain=1.1f;
                            float vx1=i.vx,vy1=i.vy;
                            i.vy=energyGain*(z.vy*z.mass)/i.mass;
                            i.vx=energyGain*(z.vx*z.mass)/i.mass;
                            z.vy=energyGain*(vy1*z.mass)/i.mass;
                            z.vx=energyGain*(vx1*z.mass)/i.mass;
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