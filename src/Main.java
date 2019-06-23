import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener {

    private final int sWIDTH=2200, sHEIGHT=1400;
    int ppt=4;
    private final int WIDTH=sWIDTH/ppt, HEIGHT=sHEIGHT/ppt;
    private Thread thread;
    Graphics gfx;
    Image img;

    float[][] map;
    int vectors=0;
    boolean done=false;
    int movetimer=20;
    ArrayList<Float> ranges;
    int scale=2;
    ArrayList<int[]> vects=new ArrayList<>();

    boolean[][] bm;


    public void init(){//STARTS THE PROGRAM
        this.resize(sWIDTH, sHEIGHT);
        img=createImage(sWIDTH,sHEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        this.addKeyListener(this);
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
                    //addVector();
                    movetimer = 20;
                }
            } else {
                done=true;

                //scaleWithParabola();
                //scaleUpSides();
                //flatten();
                //createThreshhold();
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
        float weight=1f;
        double a=4.0/map[0].length;
        double ry=map[0].length/8;

        for (int x=0; x<map.length; x+=scale){
            int cx=(-map.length/2+x);
            int cy=(int)(ry*((a*cx)*(a*cx)));
            //if (cy<0){cy=0;}else if (cy>map[0].length){cy=map[0].length;}
            //System.out.println("f("+cx+") = "+cy);

            for (int y=0; y<map[0].length; y+=scale){
                float r=1f-(float)((double)Math.abs(y-cy)/map[0].length);
                if (r>1){r=1;}else if (r<0){r=0;}
                if (y>cy){
                    r=.75f;
                }
                map[x][y]=r*(map[x][y]+weight*getPercentile(r, ranges))/(1f+weight);
                for (int x1=x; x1<x+scale; x1++){
                    for (int y1=y; y1<y+scale; y1++){
                        map[x1][y1]=map[x][y];
                    }
                }

            }
        }
    }


    public void createThreshhold(){
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                if (map[x][y]<ranges.get(ranges.size()/3*2)){
                    map[x][y]=0;
                }else {
                    map[x][y]=1f;
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

    private void scaleDepths(){
        scaleWithParabola();
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
        float period=(float)(50*(Math.sqrt(Math.pow(v[0]-v[2], 2)+Math.pow(v[1]-v[3], 2))/6.28));
        System.out.println("period for new vector is "+period);
        vects.add(v);
        float slope=(float) (v[3]-v[1])/(float) (v[2]-v[0]);
        float b=v[1]-(slope*v[0]);
        float pslope=-(1/slope);
        float weight=1/((float)vectors+1);
        for (int x=0; x<map.length; x+=scale){
            for (int y=0; y<map[0].length; y+=scale){
                float pb1=y-(pslope*x);
                float xint=(b-pb1)/(pslope-slope);
                float value=(float)Math.sin(((xint-v[0])/(v[2]-v[0]))*6.28)+1;
                value/=2;
                map[x][y] = (map[x][y] * (1 - weight)) + (value * weight);
                for (int x1=x; x1<x+scale; x1++){
                    for (int y1=y; y1<y+scale; y1++){
                        map[x1][y1]=map[x][y];
                    }
                }
            }
        }
        vectors++;
    }





    public void drawwater(Graphics gfx){
        for (int x=0; x<map.length; x++){
            for (int y=0; y<map[0].length; y++){
                if (map[x][y]>=0) {
                    gfx.setColor(new Color((int) (map[x][y] * 255), (int) (map[x][y] * 255), (int) (map[x][y] * 255)));
                    if(bm!=null){
                        if (!bm[x][y]){
                            gfx.setColor(new Color(255,0,0));
                            //gfx.fillRect(x * ppt, (y * ppt), ppt, ppt);
                        }
                    }
                }else {
                    gfx.setColor(new Color(255,0,0));
                }
                gfx.fillRect(x * ppt, (y * ppt), ppt, ppt);

            }
        }

        /*for (int[] p: far){
            gfx.setColor(new Color(255,0,0));
            gfx.fillRect(p[0] * ppt, (p[1] * ppt), ppt, ppt);
        }*/

        /*gfx.setColor(Color.RED);
        for (int i=0; i<vects.size(); i++){
            gfx.drawLine(vects.get(i)[0],vects.get(i)[1],vects.get(i)[2],vects.get(i)[3]);
        }*/
    }

    public void smooth(){
        for (int x=1; x<WIDTH-1; x++){
            for (int y=1; y<HEIGHT-1; y++){
                float avg=map[x][y];
                for (int d=0; d<4; d++){
                    int x1=x+getXInDir(d);
                    int y1=y+getYInDir(d);
                    avg+=map[x1][y1];
                }
                map[x][y]=avg/5f;
            }
        }
    }

    public void move(){
        if (bm==null){
            setMap();
        }
        boolean[][] b2=new boolean[bm.length][bm[0].length];
        for (int x=0; x<WIDTH; x++){
            for (int y=0; y<HEIGHT; y++){
                if (bm[x][y]){
                    b2[x][y]=true;
                }else {
                    for (int d=0; d<4;d++){
                        int x1=x+getXInDir(d);
                        int y1=y+getYInDir(d);
                        if (x1>=0&&y1>=0&&y1<HEIGHT&&x1<WIDTH){
                            if (bm[x1][y1]){
                                b2[x][y]=true;
                            }
                        }
                    }
                }
            }
        }
        bm=b2;
    }

    public void setMap(){
        bm=new boolean[map.length][map[0].length];
        for (int x=0; x<WIDTH; x++){
            for (int y=0; y<HEIGHT; y++){
                if (map[x][y]==1){
                    bm[x][y]=true;
                }
            }
        }
    }

    public float getPercent(){
        if (bm==null){return 1;}
        int num=0;
        int total=WIDTH*HEIGHT;
        for (int x=0; x<WIDTH; x++){
            for (int y=0; y<HEIGHT; y++) {
                if (!bm[x][y]){
                    num++;
                }
            }
        }
        System.out.println(num+"/"+total+"="+((float)num/total));
        return (float)num/total;
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

    public void addPond(){
        float weight=.5f;
        ranges=getQuartiles();
        int cx=WIDTH/2;
        int cy=HEIGHT/2;
        for (int x=0;x<WIDTH;x++){
            int dx=x-cx;
            for (int y=0; y<HEIGHT; y++){
                int dy=y-cy;
                float rad=(float)Math.sqrt((((float)dx*dx)/((float)cx*cx))+(((float)dy*dy)/((float)cy*cy)));
                if (rad>1){rad=1;}
                map[x][y]=(map[x][y]+(weight*getPercentile(rad,ranges)))/(1+weight);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }


    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key==KeyEvent.VK_P){
            addPond();
        }
        if (key == KeyEvent.VK_V) {
            addVector();
        }
        if (key == KeyEvent.VK_T) {
            ranges=getQuartiles();
            createThreshhold();
        }

        if (key==KeyEvent.VK_C){
            reset();
            for (int i=0; i<5;i++){
                addVector();
            }

            ranges=getQuartiles();
            createThreshhold();


        }

        if (key == KeyEvent.VK_SPACE) {
            smooth();
        }
        if (key == KeyEvent.VK_M) {
            while (getPercent()>.01f){
                move();
            }
        }

        if (key == KeyEvent.VK_D) {
            ranges=getQuartiles();
            scaleDepths();
        }
        if (key == KeyEvent.VK_R) {
            reset();
        }
    }
    public void reset(){
        vects=new ArrayList<>();
        vectors=0;
        bm=null;
        for (int x=0; x<WIDTH; x++){
            for (int y=0; y<HEIGHT; y++){
                map[x][y]=0;
            }
        }
    }
}