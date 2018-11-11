import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Main extends Applet implements Runnable {

    private final int sWIDTH=800, sHEIGHT=400;
    int ppt=16;
    private final int WIDTH=800/ppt, HEIGHT=800/ppt;
    private Thread thread;
    Graphics gfx;
    Image img;

    float[][] map;
    int vectors=0;
    boolean done=false;
    int movetimer=20;
    ArrayList<Float> ranges;


    public void init(){//STARTS THE PROGRAM
        this.resize(sWIDTH, sHEIGHT);
        img=createImage(sWIDTH,sHEIGHT);
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
                ranges=getQuartiles();
                scaleWithParabola();
                scaleUpSides();
                //flatten();
                createThreshhold();
                //System.out.println("30th percentile = ");
            }
        }
        repaint();//UPDATES FRAME
        try{ Thread.sleep(20); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    public void flatten(){
        float[][] map2=new float[map.length][map[0].length];
        for (int x=0; x<WIDTH; x++){
            for (int y=0; y<HEIGHT/2; y++){
                map2[x][y]=(map[x][y*2]+map[x][y*2+1])/2f;
            }
        }
        map=map2;
    }

    public void scaleWithParabola(){
        float weight=2.5f;
        double a=4.0/map[0].length;
        double ry=map[0].length/4;
        for (int x=0; x<map.length; x++){
            int cx=(-map.length/2+x);
            int cy=(int)(ry*((a*cx)*(a*cx)));
            if (cy<0){cy=0;}else if (cy>map[0].length){cy=map[0].length;}
            System.out.println("f("+cx+") = "+cy);
            for (int y=0; y<map[0].length; y++){
                float r=1f-(float)((double)Math.abs(y-cy)/map[0].length);
                if (r>1){r=1;}else if (r<0){r=0;}
                map[x][y]=(map[x][y]+weight*getPercentile(r, ranges))/(1f+weight);
            }
        }
    }


    public void createThreshhold(){
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                if (map[x][y]<ranges.get(ranges.size()/3)){
                    map[x][y]=.25f;
                    map[x][y]=.01f;
                }else if (map[x][y]<ranges.get(ranges.size()/3*2)){
                    map[x][y]=.5f;
                    map[x][y]=.01f;
                }else {
                    map[x][y]=.75f;
                    //map[x][y]=.01f;
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
            //System.out.println(i+" - "+ranges.get(i));
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

    public void scaleUpSides(){
        float max=(getPercentile(.2, ranges)+getPercentile(.8, ranges))/2f;
        for (int x=0; x<WIDTH; x++){
            for (int y=0; y<HEIGHT/10; y++){
                map[x][HEIGHT-1-y]=((y*map[x][HEIGHT-1-y])+((HEIGHT/10-y)*max))/(HEIGHT/10);
                map[x][y]=((y*map[x][y])+((HEIGHT/10-y)*max))/(HEIGHT/10);
            }
        }
        for (int x=0; x<WIDTH/10; x++){
            for (int y=0; y<HEIGHT; y++){
                map[WIDTH-1-x][y]=((x*map[WIDTH-x-1][y])+((WIDTH/10-x)*max))/(WIDTH/10);
                map[x][y]=((x*map[x][y])+((WIDTH/10-x)*max))/(WIDTH/10);
            }
        }

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
                gfx.fillRect(x*ppt , sHEIGHT-(y*ppt) , ppt, ppt);
            }
        }
    }
}