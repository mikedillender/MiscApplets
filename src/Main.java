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
    Color gridColor=new Color(0, 0,0);
    int[][][] map;



    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        map=new int[128][90][3];
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        paintCoordGrid(gfx);

        //RENDER FOREGROUND


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    int addtimer=100;
    int movetimer=20;
    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

            //UPDATES
            addtimer-=10;
            movetimer-=10;
            if (addtimer<0){
                addtimer=0;
                addBlock();
                addBlock();
                addBlock();
                addBlock();
                addBlock();
                addBlock();
                addBlock();
            }
            if (movetimer<0){
                movetimer=10;
                move();
            }


            repaint();//UPDATES FRAME
            try{ Thread.sleep(50); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    public void addBlock(){
        map[(int)(Math.random()*128)][0][0]=(int)(Math.random()*255);
        map[(int)(Math.random()*128)][0][1]=(int)(Math.random()*255);
        map[(int)(Math.random()*128)][0][2]=(int)(Math.random()*255);
    }

    public void move(){
        for (int x=0; x<map.length; x++){
            for (int y=map[0].length-1; y>-1; y--){
                if (map[x][y][0]!=0) {
                    int[] thisTile=map[x][y];
                    if (y<map[0].length-1) {
                        if (map[x][y + 1][0]==0) {
                            map[x][y] = new int[]{0,0,0};
                            map[x][y + 1] = thisTile;
                        }
                    }
                }
            }
        }
    }


    //INPUT
    public void keyPressed(KeyEvent e) {

    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID
    public void paintCoordGrid(Graphics gfx){
        int tilesize=10;

        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                if (map[x][y][0]!=0) {
                    gfx.setColor(new Color(map[x][y][0], map[x][y][1], map[x][y][2]));
                    gfx.fillRect(tilesize * x, tilesize * y, tilesize, tilesize);
                }
            }
        }
    }
}