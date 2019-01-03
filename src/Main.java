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
    float[][][] map;
    float[][] stars;



    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        map=new float[128][90][3];
        stars=new float[128][90];
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
            addColor();
            flowColor();

            repaint();//UPDATES FRAME
            try{ Thread.sleep(50); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    private void addColor(){
        int bx=(int)(Math.random()*(map.length-4));
        int by=(int)(Math.random()*(map[0].length-4));
        int size=(int)(Math.random()*3);
        int[] color=new int[]{
                (int)(150*Math.random()),
                (int)(150*Math.random()),
                (int)(200*Math.random())
        };
        for (int x=bx; x<bx+size; x++){
            for (int y=by; y<by+size; y++){
                float amt=(float)(Math.random());
                for (int v=0; v<3; v++){
                    map[x][y][v]=(int)((map[x][y][v]+(amt*color[v]))/(1+amt));
                }
            }
        }
    }

    private void flowColor(){
        for (int x=1; x<map.length-1; x++){
            for (int y=1; y<map[0].length-1; y++){
                for (int v=0; v<3; v++){
                    float avg=map[x][y][v];
                    for (int d=0; d<4; d++){
                        avg+=map[x+getXInDir(d)][y+getYInDir(d)][v];
                    }
                    map[x][y][v]=(avg/5f);

                }
            }
        }
    }

    public int getXInDir(int dir){
        if (dir==1){
            return 1;
        }else if (dir==3){
            return -1;
        }
        return 0;
    }

    public int getYInDir(int dir){
        if (dir==0){
            return -1;
        }else if (dir==2){
            return 1;
        }
        return 0;
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
                int[] c=new int[3];
                for (int v=0; v<3; v++){
                    int val=(int)map[x][y][v];
                    if (val>10){
                        val=10;
                    }
                    c[v]=val;
                }
                gfx.setColor(new Color(c[0],c[1],c[2]));
                gfx.fillRect(tilesize * x, tilesize * y, tilesize, tilesize);
            }
        }
    }
}