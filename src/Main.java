import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener {

    private final int sWIDTH=800, sHEIGHT=800;
    int ppt=40;
    private final int WIDTH=sWIDTH/ppt, HEIGHT=sHEIGHT/ppt;
    private Thread thread;
    Graphics gfx;
    Image img;

    float[][] map;
    int vectors=0;
    boolean done=false;
    int movetimer=20;
    ArrayList<Float> ranges;
    int scale=1;
    int maxIterations=3;
    ArrayList<int[]> vects=new ArrayList<>();
    ArrayList<short[]> path;
    short[] st;
    short[] end;
    private ArrayList<ArrayList<short[]>> pausedPaths;
    private ArrayList<short[]> pausedlocs;
    private ArrayList<ArrayList<short[]>> allpaths;


    public void init(){//STARTS THE PROGRAM
        this.resize(sWIDTH, sHEIGHT);
        img=createImage(sWIDTH,sHEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        this.addKeyListener(this);
        reset();

    }

    public void reset(){
        st=new short[]{0,(short)(HEIGHT/2)};
        end=new short[]{(short)(WIDTH-1),(short)((HEIGHT-1)*Math.random())};
        map=new float[WIDTH][HEIGHT];
        maxIterations=1;
        pausedPaths=null;
        pausedlocs=null;
        allpaths=null;
        //maxIterations=(int)(Math.ceil((Math.abs(st[0]-end[0])+Math.abs(st[1]-end[1]))/3));
        createMap();
        path=new ArrayList<>();
    }

    public void createPath(){
        allpaths=new ArrayList<>();
        pausedPaths=new ArrayList<ArrayList<short[]>>();
        pausedlocs=new ArrayList<short[]>();
        //ArrayList<short[]> npath=pathBetween(path,st);
        pathBetween(path,st,(short)0);

        /*if (npath!=null){
            path=npath;
        }*
        //path=pathBetween(path,st,end);
        /*while(path.get(path.size()-1)[0]!=end[0]||path.get(path.size()-1)[1]!=end[1]){
            path(path.get(path.size()-1),end);
        }*/
    }




    public void path(int[] cloc,int[] end){
        int dir=0;
        int xd=0;
        if (cloc[0]>end[0]){
            xd=-1;
        }else if (cloc[0]<end[0]){
            xd=1;
        }
        int yd=0;
        if (cloc[1]>end[1]){
            yd=-1;
        }else if (cloc[1]<end[1]){
            yd=1;
        }
        //path.add(new short[]{cloc[0]+xd,cloc[1]+yd});
    }


    private int getDirToGoal(short[] cloc,short[] end){
        int dir=0;
        int xd=0;

        if (cloc[0]>end[0]){
            xd=3;
        }else if (cloc[0]<end[0]){
            xd=1;
        }
        int yd=0;
        if (cloc[1]>end[1]){
            yd=0;
        }else if (cloc[1]<end[1]){
            yd=2;
        }
        if (Math.abs(cloc[1]-end[1])>Math.abs(cloc[0]-end[0])){
            return yd;
        }return xd;
    }



    public void resumePaths(){
        if (pausedPaths==null){
            createPath();
            return;
        }
        for (int i=0; i<pausedlocs.size(); i++){
            for (int j=i+1; j<pausedlocs.size(); j++){
                if (pausedlocs.get(i)[0]==pausedlocs.get(j)[0]) {
                    if (pausedlocs.get(i)[1] == pausedlocs.get(j)[1]) {
                        if (pausedPaths.get(j).size()>=pausedPaths.get(i).size()) {
                            pausedlocs.remove(j);
                            pausedPaths.remove(j);
                        }else {
                            pausedlocs.remove(i);
                            pausedPaths.remove(i);
                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<short[]>> ppaths= (ArrayList<ArrayList<short[]>>) pausedPaths.clone();
        ArrayList<short[]> plocs= (ArrayList<short[]>) pausedlocs.clone();
        pausedPaths=new ArrayList<ArrayList<short[]>>();
        pausedlocs=new ArrayList<short[]>();
        int maxchecks=50;
        int checks=ppaths.size();
        if (checks>maxchecks){checks=maxchecks;}
        System.out.println("checking closest "+checks+" of "+ppaths.size());
        for (int t=0; t<checks; t++){
            int cdist=1000;
            int cIndex=0;
            for (int i=0; i<ppaths.size(); i++){
                short[] loc=plocs.get(i);
                int dx=loc[0]-end[0];
                int dy=loc[1]-end[1];
                int d=(int)(Math.sqrt(dx*dx+dy*dy));
                if (d<cdist){
                    cdist=d;
                    cIndex=i;
                }
            }
            pathBetween(ppaths.get(cIndex),plocs.get(cIndex),(short)0);
            ppaths.remove(cIndex);
            plocs.remove(cIndex);
        }

    }

    public void solve(){
        createPath();
        long s=System.nanoTime();
        float dt=0;
        while (path.size()<5&&dt<3){
            resumePaths();
            dt=(System.nanoTime()-s)/1000000000f;
        }
        System.out.println("solved in "+dt);
    }

    public ArrayList<short[]> pathBetween(ArrayList<short[]> cpath, short[] cloc, short iteration){
        //System.out.println("going from "+cloc[0]+","+cloc[1]+" to "+end[0]+","+end[1]);
        if (cloc[0]<0||cloc[1]<0||cloc[0]>=map.length||cloc[1]>=map[0].length){return null;}
        if(map[cloc[0]][cloc[1]]==0){return null;}
        for (int i=0;i<cpath.size();i++){if (cpath.get(i)[0]==cloc[0]&&cpath.get(i)[1]==cloc[1]){return null;}}
        if (iteration>maxIterations){
            //System.out.println("too long, "+iteration+">"+maxIterations);
            pausedlocs.add(cloc);
            pausedPaths.add(cpath);
            return null;
        }
        //if (cpath.contains(cloc)){return null;}
        cpath.add(cloc);
        if (cloc[0]==end[0]&&cloc[1]==end[1]){
            if (allpaths.size()==0){
                path=cpath;
            }
            allpaths.add(cpath);
            System.out.println("found a route of l = "+cpath.size());
            return cpath;
        }

        int bestd=getDirToGoal(cloc,end);
        int d=bestd;
        ArrayList<short[]> npath=null;
        for (int dir=0; dir<4; dir++){
            if (dir==1){ d++; }else if (d==2){d-=2;}else if (d==3){d--;}
            if (d<0){d+=4;}else if (d>3){d-=4;}
            short[] newloc=new short[]{(short)(cloc[0]+getXInDir(d)),(short)(cloc[1]+getYInDir(d))};
            //System.out.println("size : "+cpath.size());


            ArrayList<short[]> cnpath= (ArrayList<short[]>) cpath.clone();
            cnpath=pathBetween(cnpath,newloc, (short)(iteration+1));
            if (cnpath!=null){
                return cnpath;
            }
        }
        return npath;


    }




    public void createMap(){
        for (int i=0; i<10; i++){
            addVector();
        }
        ranges=getQuartiles();
        //scalePath();
        ranges=getQuartiles();
        createThreshhold();
        if (map[end[0]][end[1]]==0||map[st[0]][st[1]]==0){createMap();}
    }

    public void scalePath(){
        float weight=1f;
        int midy=map[0].length/2;
        for (int x=0; x<map.length; x+=scale) {
            for (int y = 0; y < map[0].length; y += scale) {
                float rad=(float)Math.pow(1f-(Math.abs(midy-y)/(float)midy),2);
                map[x][y]=(map[x][y]+(weight*getPercentile(rad,ranges)))/(1+weight);

            }
        }
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
                if (map[x][y]<ranges.get(ranges.size()/3)){
                    map[x][y]=.0f;
                }else {
                    map[x][y]=1f;
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
        float period=(float)(5*(Math.sqrt(Math.pow(v[0]-v[2], 2)+Math.pow(v[1]-v[3], 2))/6.28/10));
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
                float value=(float)Math.sin(((xint-v[0])/(v[2]-v[0]))*6.28*5)+1;
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
                }else {
                    gfx.setColor(new Color(255,0,0));
                }
                gfx.fillRect(x * ppt, (y * ppt), ppt, ppt);

            }
        }

        if (pausedlocs!=null){
            gfx.setColor(Color.orange);
           for (int i=0; i<pausedlocs.size();i++){
               int x=pausedlocs.get(i)[0];
               int y=pausedlocs.get(i)[1];
               gfx.fillRect(x * ppt, (y * ppt), ppt, ppt);
           }
        }
        if (allpaths!=null) {
            for (int z = 0; z < allpaths.size(); z++) {
                //gfx.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                gfx.setColor(Color.GREEN);
                for (int i = 0; i < allpaths.get(z).size(); i++) {
                    int x = allpaths.get(z).get(i)[0];
                    int y = allpaths.get(z).get(i)[1];
                    gfx.fillRect(x * ppt, (y * ppt), ppt, ppt);
                }
            }
        }
        gfx.setColor(Color.BLUE);
        for (int i=0; i<path.size(); i++){
            int x=path.get(i)[0];
            int y=path.get(i)[1];
            gfx.fillRect(x * ppt, (y * ppt), ppt, ppt);
        }
        gfx.setColor(Color.RED);
        int x=end[0];
        int y=end[1];
        gfx.fillRect(x * ppt, (y * ppt), ppt, ppt);


        /*gfx.setColor(Color.RED);
        for (int i=0; i<vects.size(); i++){
            gfx.drawLine(vects.get(i)[0],vects.get(i)[1],vects.get(i)[2],vects.get(i)[3]);
        }*/
    }

    public void scaleWithSine(){
        float period=map.length/2;
        float b=(2*3.14f)/period;
        float a=map[0].length/3;
        float vshift=map[0].length/2;
        float weight=.2f;
        for (int x=0; x<map.length; x++){

            float cy=(float)(a*Math.sin(x*b)+vshift);
            for (int y=0; y<map[0].length; y++){
                float dy=1-(float)(Math.abs(y-cy))/map[0].length;
                map[x][y]=((dy*weight)+map[x][y])/(1+weight);
            }
        }
    }

    public void scaleWithBigCos(){
        float period=map.length;
        float b=(2*3.14f)/period;
        float a=-map[0].length/4;
        float vshift=map[0].length/2;
        float weight=.2f;
        for (int x=0; x<map.length; x++){

            float cy=(float)(a*Math.cos(x*b)+vshift);
            for (int y=0; y<map[0].length; y++){

                float dy=1-(float)(((y<cy)?0:Math.abs(y-cy)))/map[0].length;
                map[x][y]=((dy*weight)+map[x][y])/(1+weight);
            }
        }
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


    public short getXInDir(int dir){
        if (dir==1){
            return 1;
        }else if (dir==3){
            return -1;
        }
        return 0;
    }

    public short getYInDir(int dir){
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
        int key = e.getKeyCode();
        if (key==KeyEvent.VK_SPACE){
            resumePaths();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key==KeyEvent.VK_P){
            createPath();
            System.out.println("path found, l = "+path.size());
        }else if (key==KeyEvent.VK_R){
            reset();
        }else if (key==KeyEvent.VK_S){
            reset();
            solve();
        }
        /*
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
        if (key == KeyEvent.VK_S) {
            ranges=getQuartiles();
            scaleWithBigCos();
        }
        if (key == KeyEvent.VK_SPACE) {
            smooth();
        }

        if (key == KeyEvent.VK_D) {
            ranges=getQuartiles();
            scaleDepths();
        }
        if (key == KeyEvent.VK_R) {
            vects=new ArrayList<>();
            vectors=0;
            for (int x=0; x<WIDTH; x++){
                for (int y=0; y<HEIGHT; y++){
                    map[x][y]=0;
                }
            }
        }*/
    }
}