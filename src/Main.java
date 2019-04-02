import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    private final int WIDTH=800, HEIGHT=800;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(150, 150,150);
    Color FontColor=new Color(0,0,0);

    int[][] map;
    boolean[] nums;
    boolean gameOver=false;



    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        gfx.setFont(new Font("TimesRoman", Font.PLAIN, 100));
        setMap();
        thread=new Thread(this);
        thread.start();
    }

    public void setMap(){
        nums=new boolean[16];
        map=new int[4][4];
        for (int x=0; x<4; x++){
            for (int y=0; y<4; y++){
                map[x][y]=getRand();
            }
        }

    }
    public int getRand(){
        int num=(int)(Math.random()*15.9);
        while (nums[num]){
            num=(int)(Math.random()*15.9);
        }
        nums[num]=true;
        return num;
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size

        drawBoard(gfx);
        if (gameOver){
            gfx.setColor(new Color(0,0,255));
            gfx.drawString("YOU WON", WIDTH/2,HEIGHT/2);
        }

        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void checkWon(){
        int i=1;
        for (int x=0; x<4; x++) {
            for (int y = 0; y < 4; y++) {
                if (i==16){i=0;}
                if (map[x][y]!=i){
                    gameOver=false;
                    return;
                }
                i++;
            }
        }
        gameOver=true;
    }

    public void drawBoard(Graphics gfx){
        for (int x=0; x<4; x++) {
            for (int y = 0; y < 4; y++) {
                if(map[x][y]!=0) {
                    gfx.setColor(gridColor);
                    gfx.fillRect(y * 200 + 5, x * 200 + 5, 180, 180);
                    gfx.setColor(FontColor);

                    gfx.drawString("" + map[x][y], y * 200 + 50, x * 200 + 150);
                }
            }
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

    public void shift(int dir){
        for (int i=0; i<3; i++) {
            for (int x = 0; x<4; x++){
                for (int y = 0; y<4; y++){
                    move(x,y,dir);
                }
            }
        }
        checkWon();
    }
    public void setWon(){
        int i=1;
        for (int x=0; x<4; x++) {
            for (int y = 0; y < 4; y++) {
                if (i==16){i=0;}
                map[x][y]=i;
                i++;
            }
        }
    }

    public void move(int x, int y, int dir){
        int x1=x+getXinDir(dir);
        int y1=y+getYinDir(dir);
        if (doesPosExist(x1,y1)){
            if (map[x1][y1]==0){
                map[x1][y1]=map[x][y];
                map[x][y]=0;
            }
        }
    }

    public boolean doesPosExist(int x, int y){
        if (x>=0 &&y>=0&&y<4&&x<4) {
            return true;
        }
        return false;
    }

    public int getXinDir(int dir){
        if (dir==1){
            return 1;
        }if (dir==3){
            return -1;
        }
        return 0;
    }

    public int getYinDir(int dir){
        if (dir==0){
            return 1;
        }if (dir==2){
            return -1;
        }
        return 0;
    }

    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_UP){
            shift(3);
        }if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            shift(0);
        }if (e.getKeyCode()==KeyEvent.VK_DOWN){
            shift(1);
        }if (e.getKeyCode()==KeyEvent.VK_LEFT){
            shift(2);
        }if (e.getKeyCode()==KeyEvent.VK_P){
            setWon();
        }
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

}