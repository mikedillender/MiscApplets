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
    private final int size=10;
    private int sWIDTH=1280, sHEIGHT=900;
    private int WIDTH=sWIDTH/size, HEIGHT=sHEIGHT/size;
    boolean blackAndWhite=false;


    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(0, 0,0);
    float[][][] map;
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
        map=new float[WIDTH][HEIGHT][3];
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
        Color bak=new Color(0x88AE91);
        gfx.setColor(bak);//background
        gfx.fillRect(0,0,sWIDTH,sHEIGHT);//background size
        paintCoordGrid(gfx);

        //RENDER FOREGROUND


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }


    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME


        repaint();//UPDATES FRAME
        try{ Thread.sleep(50); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }



    public void addTree(int x, int y) {
        int w = (int) (Math.ceil(Math.random() * 2));
        int h = (int) (w * Math.random() * 8);
        float[] bark = new float[]{147, 138, 109};

        for (int x1 = x; x1 < x + w; x1++) {
            for (int y1 = y; y1 < y + h + 5; y1++) {
                if (isValid(x1, y1)) {
                    map[x1][y1] = bark;
                }
            }
        }
        for (int y1 = y-2; y1 < y + (.8*h) + 5; y1++) {
            float distmult=(float)Math.sqrt(((float)y1-(y-2))/(y+(.8*h)));
            int l=(int)(Math.random()*distmult*h);
            createTrapez(x,y1, l, 2);
        }
    }


    public float getAvgAt(int x, int y){
        float sum=0;
        for (int v=0; v<3; v++){
            sum+=map[x][y][v];
        }
        return sum/3f;
    }



    public float getAvgAt(int x, int y, float[][][] map){
        float sum=0;
        for (int v=0; v<3; v++){
            sum+=map[x][y][v];
        }
        return sum/3f;
    }


    public void createTrapez(int cx, int cy, int l, int h){
        float[] c=new float[]{(int)(Math.random()*100)+100,(int)(Math.random()*100)+150,(int)(Math.random()*100)+100};
        for (int y=cy-h; y<cy+h; y++) {
            for (int x = cx - (int)(l*((float)(y-(cy-h))/(2*h))); x < cx + (int)(l*((float)y/cy)); x++) {
                if (isValid(x,y)) {
                    map[x][y] = c;
                }
            }
        }

    }

    private void forest(){
        for (int y=0; y<HEIGHT; y++){
            for (int x=0; x<WIDTH; x++){
                if (Math.random()*WIDTH<5){
                    addTree(x,y);
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

    public void smooth(){
        float w=smoothweight;
        float[][][] map2=new float[WIDTH][HEIGHT][3];
        for (int x=1; x<WIDTH-1; x++){
            for (int y=1; y<HEIGHT-1; y++){
                for (int v=0; v<3; v++) {
                    float avg = map[x][y][v];
                    for (int d = 0; d < 4; d++) {
                        int x1 = x + getXInDir(d);
                        int y1 = y + getYInDir(d);
                        avg += map[x1][y1][v];
                    }
                    map2[x][y][v] = ((w*(avg / 5f)+(map[x][y][v]))/(1+w));
                }
            }
        }
        map=map2;
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
        if (e.getKeyCode()==KeyEvent.VK_T){
            addTree((int)(Math.random()*WIDTH),(int)(Math.random()*HEIGHT));
        }
        if (e.getKeyCode()==KeyEvent.VK_F){
            forest();
        }
        if (e.getKeyCode()==KeyEvent.VK_R){
            map=new float[WIDTH][HEIGHT][3];
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
                if (map[x][y][0]!=0) {
                    gfx1.setColor(new Color((int) (map[x][y][0]), (int) (map[x][y][1]), (int) (map[x][y][2])));

                    gfx1.fillRect(x * size, y * size, size, size);
                }
            }
        }
    }
}