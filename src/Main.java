import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    private final int WIDTH=1900, HEIGHT=900;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color waterc=new Color(100,100,250);
    Color gridColor=new Color(150, 150,150);
    int tilesize=5;

    //"CHARACTER"
    float[][] water;
    boolean[][] map;
    boolean[][] inputMap;

    //CONTROLS
    boolean pressingW;
    boolean pressingA;
    boolean pressingS;
    boolean pressingD;

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        water=new float[WIDTH/tilesize][HEIGHT/tilesize];
        map=new boolean[WIDTH/tilesize][HEIGHT/tilesize];
        int[][] m1=new int[][]{
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,1,1,1,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,1,1,0,1,1,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0},
                {0,0,1,1,0,0,0,1,1,0,0},
                {0,0,0,0,0,0,0,0,0,0,0}
        };
        intToBool(m1);
        invertMap();
        scaleMap(inputMap);
    }

    private void intToBool(int[][] m1){
        inputMap=new boolean[m1.length][m1[0].length];
        for (int x=0; x<inputMap.length; x++){
            for (int y=0; y<inputMap[0].length; y++){
                inputMap[x][y]=m1[x][y]==1;
            }
        }
    }

    private void invertMap(){
        boolean[][] m1=new boolean[inputMap[0].length][inputMap.length];
        for (int x=0; x<inputMap.length; x++){
            for (int y=0; y<inputMap[0].length; y++){
                m1[y][x]=inputMap[x][y];
            }
        }
        inputMap=m1;
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size

        //RENDER FOREGROUND
        drawwater(gfx);


        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    int spawntimer=100;
    int movetimer=100;

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

        //UPDATES
        //UPDATES
        spawntimer-=2;
        movetimer-=2;
        if (movetimer<0){
            move();
            movetimer=10;
        }
        if (spawntimer<0){
            addWater();
            spawntimer=20;
        }
        move();
        addWater();


        repaint();//UPDATES FRAME
        try{ Thread.sleep(3); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    public void addWater(){

        for (int i=0; i<10; i++) {
            if (Math.random()<.06) {
                water[(water.length / 2) - 5 + i][0] = 100;
            }
        }
        //water[(int)(Math.random()*water.length)][0]=new int[]{(int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)};
    }


    //INPUT
    public void keyPressed(KeyEvent e) {  }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) { }

    public void move(){
        for (int x=0; x<water.length; x++){
            for (int y=water[0].length-2; y>-1; y--){
                if (water[x][y]>0){
                    flowWater(x,y);
                }
            }
        }
    }

    private void flowWater(int x, int y){
        for (int dir=0; dir<3; dir++){
            if (water[x][y]<10){ return;}
            int x1=x;
            int y1=y;
            switch (dir){
                case 0:
                    y1+=1;
                    break;
                case 1:
                    x1+=1;
                    break;
                case 2:
                    x1-=1;
                    break;
            }
            if (isValidLoc(x1, y1)){
                if (!map[x1][y1]) {
                    if (dir != 0) {
                        float avg = water[x][y] + water[x1][y1];
                        avg /= 2;
                        water[x][y] = avg;
                        water[x1][y1] = avg;
                    } else {
                        water[x1][y1] += water[x][y];
                        water[x][y] = 0;
                        if (water[x1][y1] > 100) {
                            water[x][y] = water[x1][y1] - 100;
                            water[x1][y1] = 100;
                        }
                    }
                }
            }
        }
    }

    private void scaleMap(boolean[][] m1){
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                map[x][y]=m1[(int)(Math.floor(((double)x/map.length)*m1.length))][(int)(Math.floor(((double)y/map[0].length)*m1[0].length))];
            }
        }
    }

    private boolean isValidLoc(int x, int y){
        if (x>=0&&y>=0){
            if (x<water.length&&y<water[0].length){
                return true;
            }
        }
        return false;
    }

    public void drawwater(Graphics gfx){

        for (int x=0; x<water.length; x++){
            for (int y=0; y<water[0].length; y++){
                if (map[x][y]){
                    gfx.setColor(Color.BLACK);
                    gfx.fillRect(x * tilesize, y * tilesize, tilesize, tilesize);
                }
                if (water[x][y]>10) {
                    gfx.setColor(waterc);
                    gfx.fillRect(x * tilesize, y * tilesize, tilesize, tilesize);
                }
            }
        }
    }


}