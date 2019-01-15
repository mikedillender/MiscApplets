import javax.imageio.ImageIO;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    private final int size=5;
    private int sWIDTH=1500, sHEIGHT=850;
    private int WIDTH=sWIDTH/size, HEIGHT=sHEIGHT/size;
    boolean blackAndWhite=true;


    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;
    long start=System.nanoTime();

    //COLORS
    Color yes=Color.BLACK;
    Color no=Color.WHITE;
    boolean[][] map;
    int speed=0;
    int smoothspeed=0;
    int movespeed=0;
    float fx,fy;
    float vx=0,vy=0;

    public void init(){//STARTS THE PROGRAM
        this.resize(sWIDTH, sHEIGHT);
        this.addKeyListener(this);
        img=createImage(sWIDTH,sHEIGHT);
        gfx=img.getGraphics();
        map=new boolean[WIDTH][HEIGHT];
        fx=WIDTH/2;
        fy=HEIGHT/2;
        thread=new Thread(this);
        thread.start();
    }

    public void moveF(){
        float vx1=-.5f+(float)Math.random();
        float vy1=-.5f+(float)Math.random();
        if (Math.abs(vx+vx1)<4){
            vx+=vx1;
        }
        if (Math.abs(vy+vy1)<4){
            vy+=vy1;
        }
        //float x=-2+(float)(Math.random()*4);
        //float y=-2+(float)(Math.random()*4);
        if (fx+vx<WIDTH&&fx+vx>0) {
            fx += vx;
        }else {
            vx=0;
        }
        if (fy+vy<HEIGHT&&fy+vy>0) {
            fy += vy;
        }else {
            vy=0;
        }
        //System.out.println("focus at "+fx+","+fy);

    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(Color.white);//background
        gfx.fillRect(0,0,sWIDTH,sHEIGHT);//background size
        paintCoordGrid(gfx);

        //RENDER FOREGROUND


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    boolean colorAdd=false;

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

            //UPDATES

        for (int i=0; i<speed; i++) {
            if (Math.random()<.1) {
                addColor();
            }
            spread();
        }

        chance= .3f+(float) (.4f*.5f*(1+Math.sin((System.nanoTime()-start)/1000000000.0f/3)));



        //smooth();

        repaint();//UPDATES FRAME
        try{ Thread.sleep(50); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }



    public void addColor(){

        int x=(int)(WIDTH*Math.random());
        int y=(int)(HEIGHT*Math.random());
        map[x][y]=!map[x][y];

    }

    float chance=0;

    public void spread(){

        for (int i=0; i<100; i++){
            int x=(int)(Math.random()*WIDTH);
            int y=(int)(Math.random()*HEIGHT);
            for (int d=0; d<4; d++){
                int x1=x+getXInDir(d);
                int y1=y+getYInDir(d);
                if ((map[x][y]&&Math.random()<chance)||(!map[x][y]&&Math.random()>chance)) {
                    if (isValid(x1, y1)) {
                        map[x1][y1] = map[x][y];
                    }
                }
            }
        }
    }

    private boolean isValid(int x, int y){
        if (x>0&&y>0){
            if (x<WIDTH&&y<HEIGHT){
                return true;
            }
        }
        return false;
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
        if (e.getKeyCode()==KeyEvent.VK_DOWN){
            speed--;
        }else if (e.getKeyCode()==KeyEvent.VK_UP) {
            speed++;
        }
    }
    public void keyReleased(KeyEvent e) {

    }
    private float smoothweight=0;
    public void keyTyped(KeyEvent e) { }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID
    public void paintCoordGrid(Graphics gfx1){
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){

                gfx.setColor((map[x][y])?no:yes);
                gfx1.fillRect(x *size,  y*size, size, size);

            }
        }
    }
}