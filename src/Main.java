import javax.imageio.ImageIO;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Applet implements Runnable {

    private final int WIDTH=1200, HEIGHT=900;

    private Thread thread;
    Graphics gfx;
    Image img;

    float[][] map;
    int vectors=0;
    boolean done=false;
    int movetimer=20;


    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        map=new float[WIDTH][HEIGHT];
    }

    public void paint(Graphics g){
        drawwater(gfx);
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME
        if (!done) {
            if (vectors < 10) {
                movetimer -= 1;
                if (movetimer < 0) {
                    addVector();
                    movetimer = 20;
                }
            } else {
                done=true;
                getQuartiles();
                createThreshhold();
                storeImg();
                System.out.println("30th percentile = ");
            }
        }
        repaint();//UPDATES FRAME
        try{ Thread.sleep(20); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    public void createThreshhold(){
        //float threshold=.5f;
        float[][] map1=map;

        float mid=getAvgInRange(0, 1);
        float q1=getAvgInRange(0, mid);
        float q3=getAvgInRange(mid, 1);
        float q1tomid=getAvgInRange(q1, mid);
        float midtoq3=getAvgInRange(mid, q3);
        float q4=getAvgInRange(q3, 1);
        float q0=getAvgInRange(0, q1);

        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                if (map[x][y]<q0){
                    map[x][y]=0;
                }else if(map[x][y]<q1){
                    map[x][y]=.1f;
                }else if (map[x][y]<q1tomid){
                    map[x][y]=.2f;
                }else if (map[x][y]<mid){
                    map[x][y]=.4f;
                }else if (map[x][y]<midtoq3){
                    map[x][y]=.6f;
                }else if (map[x][y]<q3){
                    map[x][y]=.8f;
                }else if (map[x][y]<q4){
                    map[x][y]=.9f;
                }else {
                    map[x][y]=1.0f;
                }
            }
        }
    }

    private float getPercentile(double percent, ArrayList<Float> quart){
        return (quart.get((int)(percent*(quart.size()-1))));
    }

    private ArrayList<Float> getQuartiles(){
        ArrayList<Float> ranges=new ArrayList<>();
        ranges.add(0f);
        ranges.add(1f);
        ranges=expandQuart(ranges);
        ranges=expandQuart(ranges);
        ranges=expandQuart(ranges);
        ranges=expandQuart(ranges);
        ranges=expandQuart(ranges);
        ranges=expandQuart(ranges);
        for (int i=0; i<ranges.size(); i++){
            System.out.println(i+" - "+ranges.get(i));
        }
        return ranges;
    }

    private ArrayList<Float> expandQuart(ArrayList<Float> quart){
        ArrayList<Float> nlist= new ArrayList<>();
        for (int i=0; i<quart.size()-1; i++){
            nlist.add(quart.get(i));
            nlist.add(getAvgInRange(quart.get(i),quart.get(i+1)));
        }
        nlist.add(quart.get(quart.size()-1));

        return nlist;

    }

    public float getAvgInRange(float low, float high){
        int numTiles=0;
        float sum=0;
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                if (map[x][y]<high&&map[x][y]>low){
                    numTiles++;
                    sum+=map[x][y];
                }
            }
        }
        float avg=sum/(float)numTiles;
        return avg;
    }

    public void addVector(){
        int[] v=new int[]{(int)(Math.random()*WIDTH),(int)(Math.random()*HEIGHT),(int)(Math.random()*WIDTH),(int)(Math.random()*HEIGHT)};
        if (v[1]==v[3]||v[0]==v[2]){return;}
        float slope=(float) (v[1]-v[0])/(float) (v[0]-v[2]);
        float b=v[1]-(slope*v[0]);
        float pslope=-(1/slope);
        float weight=1/((float)vectors+1);
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                float pb1=y-(pslope*x);
                float xint=(b-pb1)/(pslope-slope);
                float value=(float)Math.sin(((xint-v[0])/(v[2]-v[0]))*6.28*5)+1;
                value/=2;
                map[x][y] = (map[x][y] * (1 - weight)) + (value * weight);
            }
        }
        vectors++;
    }

    public void storeImg(){
        // Initialize Color[][] however you were already doing so.
        Color[][] image=new Color[map.length][map[0].length];

        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                if (map[x][y] == 0) {
                    image[x][y]=new Color(100, 00, 00);
                } else {
                    image[x][y]=new Color((int) (map[x][y] * 255) / 2, (int) (map[x][y] * 255), (int) (map[x][y] * 255) / 2);
                }
            }
        }

        // Initialize BufferedImage, assuming Color[][] is already properly populated.
        BufferedImage bufferedImage = new BufferedImage(image.length, image[0].length,
                BufferedImage.TYPE_INT_RGB);

        // Set each pixel of the BufferedImage to the color from the Color[][].
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[x].length; y++) {
                bufferedImage.setRGB(x, y, image[x][y].getRGB());
            }
        }
        File outputfile = new File("C:\\Users\\Mike\\Downloads\\image.jpg");
        try {
            ImageIO.write(bufferedImage, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void drawwater(Graphics gfx){
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                gfx.setColor(new Color((int)(map[x][y]*255),(int)(map[x][y]*255),(int)(map[x][y]*255)));
                if (done) {
                    if (map[x][y] == 0) {
                        gfx.setColor(new Color(100, 100, 200));
                    } else {
                        gfx.setColor(new Color((int) (map[x][y] * 255) / 2, (int) (map[x][y] * 255), (int) (map[x][y] * 255) / 2));
                    }
                }
                gfx.fillRect(x , y , 1,1);
            }
        }
    }
}