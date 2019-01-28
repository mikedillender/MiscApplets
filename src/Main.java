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
    Color gridColor=new Color(150, 150,150);



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

        //RENDER FOREGROUND


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

    private Color getRandomSkin(){
        float r=0,g=0,b=0;

        while (!isValidSkin(r,g,b)) {
            float maxDiff = .2f;
            r = random(220, 20);
            g = random(r - 40, 20);
            b = random(g - 40, 20);
        }
        return new Color(r/255f,g/255f,b/255f,1);

    }

    private Color varL(Color c){
        float r=c.getRed(), g=c.getGreen(), b=c.getBlue();
        float p=1-(float)(Math.random()*Math.random()*.75);
        System.out.println("p = "+p);
        System.out.printf((r*p)+", "+(g*p)+","+(b*p));
        return new Color(r*p/255f,g*p/255f,b*p/255f);
    }



    private boolean isValidSkin(float r, float g, float b){
        float avgVal=(r+g+b)/(3);
        if (avgVal>175&&r>g&&g>b){
            return true;
        }
        return false;
    }

    private float random(float around, float maxdiff){
        float d=(float)(Math.random()*2*maxdiff)-maxdiff;
        return around+d;
    }

    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_C) {
            background = getRandomSkin();
        }else if (e.getKeyCode()==KeyEvent.VK_D){
            background=varL(background);
        }
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID

}