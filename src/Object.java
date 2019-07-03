import java.awt.*;
import java.util.ArrayList;

public class Object implements Screen{
    public float x=10,y=10,z=10,size=.4f;

    Color[] c= new Color[6];
    Color c1= new Color(0,0,0);

    public void randomizeColors(){
        for (int i=0; i<c.length;i++){
            c[i]=new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
        }
    }

    public Object(){
        randomizeColors();
    }
    public Object(float x, float y, float z, float size){
        this.x=x;
        this.y=y;
        this.z=z;
        this.size=size;
        randomizeColors();
    }

    public void render(Graphics g, Player p){
        g.setColor(c1);
        int[][] faces=new int[][]{
                {0,1,3,2},
                {1,5,7,3},
                {0,4,5,1},
                {0,2,6,4},
                {4,5,7,6},
                {7,3,2,6},
        };
        int[][] pairs=new int[][]{
            {0,4},
            {1,3},
            {2,5}
        };


        float[][] v=new float[8][3];
        float thetax=p.ox;
        for (int i=0; i<8; i++){
            int l=(i<4)?i:i-4;
            float[] vp=new float[3];
            float x1=(i<4)?x:x+size;
            float y1=(l<2)?y:y+size;
            float z1=(i%2==0)?z:z+size;

            v[i][0]=(float)((z1*Math.tan(thetax)+x1)/(Math.cos(thetax)*(Math.tan(thetax)+1)));
            v[i][1]=y1;
            v[i][2]=(float)((x1*Math.tan(thetax)+z1)/(Math.cos(thetax)*(Math.tan(thetax)+1)));


        }



        int[][] vproj=new int[8][];
        for (int i=0; i<v.length; i++){
            float dx=v[i][0]-p.x;
            float dy=v[i][1]-p.y;
            float dz=v[i][2]-p.z;

            float ox=(float)Math.atan(dz/dx);
            float oy=(float)Math.atan(dy/dx);

            float x1=(ox/p.fovx)*sWIDTH/2+(sWIDTH/2f);
            float y1=(oy/p.fovy)*sHEIGHT/2+(sHEIGHT/2f);

            vproj[i]=new int[]{(int)x1,(int)y1};
            //System.out.println(v.indexOf(pos));
            //System.out.println(pos[0]+","+pos[1]+","+pos[2]);
        }
        int i1=0;
        for (int[] pos : vproj){
            g.fillRect((int)pos[0],(int)pos[1],10,10);
            g.drawString(""+i1,(int)pos[0],(int)pos[1]);
            i1++;
        }
        Polygon[] projfaces= new Polygon[6];


        float[] dists=new float[6];

        boolean[] torender= new boolean[6];
        for (int i=0; i<pairs.length; i++){
            int closest=pairs[i][0];
            float[] dist=new float[2];
            for (int z=0; z<2; z++){
                float[] avgpos=new float[3];
                for (int q=0;q<4;q++){
                    avgpos[0]+=v[faces[pairs[i][z]][q]][0]/4f;
                    avgpos[1]+=v[faces[pairs[i][z]][q]][1]/4f;
                    avgpos[2]+=v[faces[pairs[i][z]][q]][2]/4f;
                }
                float dx=p.x-avgpos[0];
                float dy=p.y-avgpos[1];
                float dz=p.z-avgpos[2];
                dist[z]=(float)(Math.sqrt(dx*dx+dy*dy+dz*dz));
                dists[pairs[i][z]]=dist[z];
            }
            if (dist[1]<dist[0]){
                closest=pairs[i][1];
            }
            torender[closest]=true;

        }
        ArrayList<Integer> order=new ArrayList<>();
        for (int i=0; i<6; i++){
            int largest=-1;
            for (int z=0; z<6; z++){
                if(!order.contains(z)){
                    if (largest==-1){
                        largest=z;
                    }else {
                        if (dists[z]>dists[largest]){
                            largest=z;
                        }
                    }
                }
            }
            order.add(largest);
        }

        for (int i=0; i<6; i++){
            int face=order.get(i);
            //if (!torender[face]){continue;}
            int[] xp=new int[4];
            int[] yp=new int[4];
            for (int l=0; l<4; l++){
                xp[l]=vproj[faces[face][l]][0];
                yp[l]=vproj[faces[face][l]][1];
            }
            Polygon poly= new Polygon(xp,yp,4);
            g.setColor(c[face]);
            g.fillPolygon(poly);

        }

    }



}
