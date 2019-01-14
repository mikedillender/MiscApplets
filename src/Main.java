import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Array;
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
    Color gridColor=new Color(150, 150,150);


    float scale=(WIDTH+HEIGHT)/2f;

    ArrayList<Arm> arms;


    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        arms=new ArrayList<>();
        arms.add(new Arm(.1f,.25f, 0,.4f));
        arms.add(new Arm(.1f,.25f, (float)Math.PI/2f+(float)Math.PI,.15f));
        arms.get(0).addArm(arms.get(1),.0f);
        arms.get(1).addArm(arms.get(0),.0f);
        arms.add(new Arm(arms.get(1),arms.get(0),arms.get(1).length,arms.get(0).length));
        arms.add(new Arm(arms.get(0),arms.get(2),arms.get(0).length*(float)Math.random(),arms.get(2).length*(float)Math.random()));
        img=createImage(WIDTH,HEIGHT);
        arms.get(2).addTorque(arms.get(2).length, 100f, 0);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        renderStructure(gfx);

        //RENDER FOREGROUND


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void renderStructure(Graphics gfx){
        gfx.setColor(Color.BLACK);
        for (int i=0; i<arms.size(); i++){
            arms.get(i).render(gfx,1000);
        }
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


}