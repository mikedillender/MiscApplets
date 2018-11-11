import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    private final int WIDTH=400, HEIGHT=400;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color waterc=new Color(100,100,250);
    Color gridColor=new Color(150, 150,150);
    int tilesize=10;

    boolean[][] map=new boolean[WIDTH/tilesize][HEIGHT/tilesize];



    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        map=new boolean[WIDTH/tilesize][HEIGHT/tilesize];
        addNest(map.length/2,map[0].length/2);
    }

    public void addNest(int x, int y){
        for (int x1=x-5;x1<=x+5;x1++){
            int starty=y+5-(int)(4.0*((Math.abs(x1-x)/5.0)));
            for (int y1=starty;y1<=y+5;y1++){
                map[x1][y1]=true;
            }
        }

    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size

        //RENDER FOREGROUND
        drawMap(gfx);


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME
        repaint();//UPDATES FRAME
        try{ Thread.sleep(3); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }


    public void drawMap(Graphics gfx){
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                if (map[x][y]){
                    gfx.setColor(Color.BLACK);
                    gfx.fillRect(x * tilesize, y * tilesize, tilesize, tilesize);
                }
            }
        }
    }

    //INPUT
    public void keyPressed(KeyEvent e) {  }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) { }


}