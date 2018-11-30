import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener {

    private final int sWIDTH=800, sHEIGHT=900;
    int ppt=16;
    private final int WIDTH=sWIDTH/ppt, HEIGHT=sHEIGHT/ppt;
    private Thread thread;
    Graphics gfx;
    Image img;
    Node[] nodes;
    int[][] paths;
    int[] pathUse;
    int[] nodeUse;

    public void init(){//STARTS THE PROGRAM
        this.resize(sWIDTH, sHEIGHT);
        img=createImage(sWIDTH,sHEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
        this.addKeyListener(this);
        nodes=new Node[25];
        setPaths();
        nodeUse=new int[25];
        for (int i=0; i<nodes.length; i++){
            nodes[i]=new Node(i);
        }
        for (int i=0; i<nodes.length; i++){
            nodes[i].addConnections(paths);
        }
        pathUse=new int[paths.length];

    }

    public void paint(Graphics g){
        gfx.setColor(new Color(255,255,255));
        gfx.fillRect(0,0,sWIDTH,sHEIGHT);
        draw(gfx);
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

        repaint();//UPDATES FRAME
        try{ Thread.sleep(20); } //ADDS TIME BETWEEN FRAMES (FPS)
        catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }



    public void draw(Graphics gfx){
        for (int i=0; i<nodes.length; i++){
            int size=10+(10*nodeUse[i]);
            gfx.setColor(new Color(0,0,0));
            gfx.fillRect(100*nodes[i].getPos()[0]-(size/2),100*nodes[i].getPos()[1]-(size/2), size, size);
            gfx.drawString(i+"",100*nodes[i].getPos()[0]+size,100*nodes[i].getPos()[1]);
        }
        gfx.setColor(new Color(0,0,0));

        for (int i=0; i<nodes.length; i++){
            for (int c=0; c<nodes[i].connections.size(); c++){
                gfx.drawLine(100*nodes[i].getPos()[0],100*nodes[i].getPos()[1],100*nodes[nodes[i].getConnections().get(c)[0]].getPos()[0],100*nodes[nodes[i].getConnections().get(c)[0]].getPos()[1]);

            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        getDayRoute();
    }

    public void getDayRoute(){
        nodeUse=new int[25];
        ArrayList<int[]> day=new ArrayList<>();
        int[] s=new int[]{0,10,12,4,1,9,2};
        int rt=0;
        for (int p=1; p<s.length; p++){
            int[] r=getFastestRoute(s[p-1],s[p]);
            day.add(r);
            for (int i=0; i<r.length; i++){
                nodeUse[r[i]]++;
                if (i>0){
                    int path=paths[getRouteBetween(r[i], r[i-1])][2];
                    rt+=paths[path][2];
                    pathUse[path]++;
                }
            }
        }
        System.out.println("total walking distance = "+rt);

    }

    private int getRouteBetween(int x, int y){
        for (int i=0; i<paths.length; i++){
            if ((paths[i][0]==x&&paths[i][1]==y)||(paths[i][0]==y&&paths[i][1]==x)){
                return i;
            }
        }
        return -1;
    }

    public int[] getFastestRoute(int s, int dest){
        int[] route =findRoute(s,dest,new int[0], true);
        //if (route==null){return;}
        System.out.println("");
        System.out.print("Fastest route from "+s+" to "+dest+" is ");
        for (int i=0; i<route.length; i++){
            System.out.print(route[i]+", ");
        }
        return route;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public int[] findRoute(int cnode, int dest, int[] route, boolean isStart){
        //System.out.println("cnode="+cnode);
        for (int i=0; i<route.length; i++){if (cnode==route[i]){return null;}}
        int[] r1=new int[route.length+1];
        for (int i=0; i<route.length; i++){
            r1[i]=route[i];
        }
        r1[route.length]=cnode;
        if (cnode==dest){ return r1; }
        ArrayList<int[]> psbleRoutes=new ArrayList<>();
        for (int i=0; i<paths.length; i++){
            if (paths[i][1]==cnode){
                psbleRoutes.add(findRoute(paths[i][0],dest,r1,false));
            }
            if (paths[i][0]==cnode){
                psbleRoutes.add(findRoute(paths[i][1],dest,r1,false));
            }
        }
        int[] shortest=null;
        for (int i=0; i<psbleRoutes.size(); i++){
            if (psbleRoutes.get(i)!=null){
                if (psbleRoutes.get(i)[psbleRoutes.get(i).length-1]==dest) {
                    if (shortest==null){
                        shortest=psbleRoutes.get(i);
                    }else{
                        if (psbleRoutes.get(i).length<shortest.length){
                            shortest=psbleRoutes.get(i);
                        }
                    }
                }
            }
        }
        return shortest;

    }

    public void setPaths() {
        paths=new int[][]{
                {0,1,1},
                {0,2,1},
                {2,3,1},
                {1,3,1},
                {4,5,1},
                {6,5,1},
                {6,7,1},
                {8,7,1},
                {8,9,1},
                {8,2,1},
                {3,9,1},
                {10,11,1},
                {12,11,1},
                {12,13,1},
                {14,13,1},
                {4,15,1},
                {5,10,1},
                {6,11,1},
                {7,12,1},
                {8,13,1},
                {9,14,1},
                {16,15,1},
                {16,10,1},
                {15,16,1},
                {19,15,1},
                {20,16,1},
                {20,19,1},
                {17,18,1},
                {21,22,1},
                {23,24,1},
                {23,21,1},
                {24,22,1},
                {17,21,1},
                {18,22,1},
                {13,17,1},
                {14,18,1},
        };
    }
}