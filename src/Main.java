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
    private final int WIDTH=1600, HEIGHT=800;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(31, 30, 43);
    int tilesize=4;
    int stars=0;
    //"CHARACTER"
    Color[][] map=new Color[WIDTH/tilesize][HEIGHT/tilesize];

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        map=new Color[WIDTH/tilesize][HEIGHT/tilesize];
    }

    public void paint(Graphics g){
        //BACKGROUND

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
        if (stars>=0&&stars<20) {
            spawntimer -= 2;
            movetimer -= 2;
            if (movetimer < 0) {
                movetimer = 10;
            }
            if (spawntimer < 0) {
                addStar();
                spawntimer = 20;
            }
        }else if (stars==20){
            save();
            stars++;
        }


        repaint();//UPDATES FRAME
        try{ Thread.sleep(3); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }


    public void addStar(){
        int x=(int)(map.length*Math.random());
        int y=(int)(map[0].length*Math.random());
        float radius=(float)(Math.random()*5+1);
        /*float r=(float)(Math.random());
        float g=(float)(Math.random());
        float b=(float)(Math.random());
        */
        float range=.1f;
        float c=.5f+(float)(Math.random()*.4);
        float r=c-range+(float)(Math.random()*2*range);
        float g=c-range+(float)(Math.random()*2*range);
        float b=c-range+(float)(Math.random()*2*range);
        stars++;



        for (int x1=(int)Math.floor(x-radius); x1<=(int)Math.ceil(x+radius); x1++){
            for (int y1=(int)Math.floor(y-radius); y1<=(int)Math.ceil(y+radius); y1++){
                if (!isValidLoc(x1,y1)){continue;}
                int dx=x-x1,dy=y-y1;
                float rad=(float)(Math.sqrt(dx*dx+dy*dy));
                if (rad<=radius){
                    float p=1-(rad/radius);
                    System.out.println(r+","+g+","+b+","+p);
                    Color ch=new Color(r,g,b,p);
                    if (map[x1][y1]==null) {
                        map[x1][y1] = ch;
                    }else {
                        Color c1=map[x1][y1];
                        float p1=c1.getAlpha();
                        float r1=(r*p+(c1.getRed()*p1/255f))/(p1+p);
                        float g1=(g*p+(c1.getGreen()*p1/255f))/(p1+p);
                        float b1=(b*p+(c1.getBlue()*p1/255f))/(p1+p);
                        float p2=(p1/255f+p);
                        if (p2>1){p2=1;}
                        c1=new Color(r1,g1,b1,p2);
                        map[x1][y1]=c1;
                    }
                }
            }
        }

    }

    //INPUT
    public void keyPressed(KeyEvent e) {  }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) { }



    private boolean isValidLoc(int x, int y){
        if (x>=0&&y>=0){
            if (x<map.length&&y<map[0].length){
                return true;
            }
        }
        return false;
    }

    public void drawwater(Graphics gfx){
        gfx.setColor(background);
        gfx.fillRect(0,0,WIDTH,HEIGHT);
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                if (map[x][y]!=null){
                    gfx.setColor(map[x][y]);
                    gfx.fillRect(x * tilesize, y * tilesize, tilesize, tilesize);
                }

            }
        }
    }


    public void save()
    {
        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(map.length,map.length, BufferedImage.TYPE_INT_RGB);

// Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (map[x][y]!=null) {
                    bufferedImage.setRGB(x, y, map[x][y].getRGB());
                }
            }
        }

        try {
            if (ImageIO.write(bufferedImage, "png", new File("./output_image.png")))
            {
                System.out.println("-- saved");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean isEnd(int x, int y){
        if (y==map[0].length-1){
            if (x>=(map.length/2)-5&&x<(map.length/2)+5)
                return true;
        }
        return false;
    }




}