import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    private final int size=5;
    private int sWIDTH=1280, sHEIGHT=900;
    private final int WIDTH=sWIDTH/size, HEIGHT=sHEIGHT/size;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(0, 0,0);
    float[][][] map;
    int speed=1;
    int smoothspeed=1;
    int movespeed=1;
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
        gfx.setColor(background);//background
        gfx.fillRect(0,0,sWIDTH,sHEIGHT);//background size
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
        for (int i=0; i<speed; i++) {
            addColor();
            spread();
        }
        for (int i=0; i<smoothspeed; i++) {
            smooth();
        }
        for (int i=0; i<movespeed; i++) {
            moveF();
        }

        //smooth();


        repaint();//UPDATES FRAME
        try{ Thread.sleep(50); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }




    public void addColor(){
        int range=(HEIGHT/5);

        //int x=(int)(Math.random()*(ex-sx))+sx;
        //int y=(int)(Math.random()*(ey-sy))+sy;
        int x=(int)fx+(int)(Math.random()*(-range+(2*Math.random()*range)));
        int y=(int)fy+(int)(Math.random()*(-range+(2*Math.random()*range)));

        if (!isValid(x,y)){return;}
        //int x=(int)(Math.random()*WIDTH)/2;
        //int y=(int)(Math.random()*HEIGHT)/2;
        int w=(int)(Math.random()*5);
        int h=(int)(Math.random()*5);
        for (int v=0; v<3; v++) {
            map[x][y][v]=(float)(Math.random()*255);
        }
        for (int x1=x; x1<x+w; x1++){
            for (int y1=y; y1<y+h; y1++) {
                if (isValid(x1,y1)) {
                    map[x1][y1] = map[x][y];
                }
            }
        }

    }


    public void spread(){

        for (int i=0; i<100; i++){
            int x=(int)(Math.random()*WIDTH);
            int y=(int)(Math.random()*HEIGHT);
            for (int d=0; d<4; d++){
                int x1=x+getXInDir(d);
                int y1=y+getYInDir(d);
                if (isValid(x1,y1)){
                    if (Math.random()<.01*(getAvgAt(x,y))){
                        //if (getAvgAt(x,y)>getAvgAt(x1,y1)){
                            map[x1][y1]=map[x][y];
                        //}
                    }
                }
            }
        }/*
        for (int x=0; x<WIDTH; x++){
            for (int y=0; y<HEIGHT; y++){
                for (int d=0; d<4; d++){
                    int x1=x+getXInDir(d);
                    int y1=y+getYInDir(d);
                    if (isValid(x1,y1)){
                        if (Math.random()<.001*(getAvgAt(x,y))){
                            if (getAvgAt(x,y)>getAvgAt(x1,y1)){
                                map[x1][y1]=map[x][y];
                            }
                        }
                    }
                }
            }
        }*/
    }

    public float getAvgAt(int x, int y){
        float sum=0;
        for (int v=0; v<3; v++){
            sum+=map[x][y][v];
        }
        return sum/3f;
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
        float w=.3f;
        for (int x=1; x<WIDTH-1; x++){
            for (int y=1; y<HEIGHT-1; y++){
                for (int v=0; v<3; v++) {
                    float avg = map[x][y][v];
                    for (int d = 0; d < 4; d++) {
                        int x1 = x + getXInDir(d);
                        int y1 = y + getYInDir(d);
                        avg += map[x1][y1][v];
                    }
                    map[x][y][v] = ((w*(avg / 5f)+(map[x][y][v]))/(1+w));
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
        if (e.getKeyCode()==KeyEvent.VK_DOWN){
            speed--;
        }else if (e.getKeyCode()==KeyEvent.VK_UP) {
            speed++;
        }else if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            smoothspeed++;
        }else if (e.getKeyCode()==KeyEvent.VK_LEFT) {
            smoothspeed--;
        }else if (e.getKeyCode()==KeyEvent.VK_MINUS){
            movespeed--;
        }else if (e.getKeyCode()==KeyEvent.VK_EQUALS) {
            movespeed++;
        }else if (e.getKeyCode()==KeyEvent.VK_R) {
            System.out.println("reset");
            map=new float[WIDTH][HEIGHT][3];
            for (int x=0; x<WIDTH; x++){
                for (int y=0; y<HEIGHT; y++){
                    for (int v=0; v<3; v++) {
                        map[x][y][v] = 0;
                    }
                }
            }
        }

        System.out.println("speed = "+speed+", smooth = "+smoothspeed+", movespeed = "+movespeed);
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID
    public void paintCoordGrid(Graphics gfx1){
        boolean blackAndWhite=true;
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                    if (blackAndWhite){
                        float avg=255-getAvgAt(x,y);
                        //if (avg!=0) {
                           //gfx1.setColor(new Color(0, 0, 0, (avg / 255f)));
                        //}
                        gfx1.setColor(new Color((int)(avg),(int)(avg),(int)(avg)));
                    }else {
                        gfx1.setColor(new Color((int)(255-map[x][y][0]), (int)(255-map[x][y][1]), (int)(255-map[x][y][2])));
                    }
                    gfx1.fillRect(x *size,  y*size, size, size);

            }
        }
    }
}