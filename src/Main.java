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
    Color gridColor=new Color(0,0,0);

    int[] grades;

    int selected=0;


    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        grades=new int[]{90,90,90};
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        gfx.setColor(gridColor);
        for (int i=0; i<grades.length; i++){
            gfx.drawString(i+" "+grades[i],100,100+20*i);
        }
        double avg=getAvg();
        gfx.drawString("avg="+avg,100,300);
        gfx.drawString("savg="+((avg+86.46)/2.0+5),100,330);

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


    private double getAvg(){
        double quizes=562+grades[2];
        double tquizes=700+100;
        double tests=343+grades[0]+grades[1];
        double ttests=400+200;
        return (40*(quizes/tquizes))+(60*(tests/ttests));
    }

    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_UP){
            if (selected+1<grades.length){
                selected++;
            }
        }
        if (e.getKeyCode()==KeyEvent.VK_DOWN){
            if (selected-1>=0){
                selected--;
            }
        }
        if (e.getKeyCode()==KeyEvent.VK_LEFT){
            grades[selected]-=5;
        }
        if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            grades[selected]+=5;
        }
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
}