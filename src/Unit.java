import java.util.ArrayList;

public class Unit {
    float[] as;
    boolean casting=false;
    int[] m;
    int[] h;
    float[] casttime;
    String n;
    float buff;
    int numunits=5;
    int index;
    int casts=0;
    int armor=20;
    int d;
    boolean nv;
    int fc=0;
    //boolean isdead=false;


    public Unit(int i, int in, boolean nv){
        this.nv=nv;
        buff=(nv)?25:40;
        index=in;
        int mh=0;
        switch (i){
            case 0:
                n="Zoe";
                m=new int[]{70,100};
                if (nv){ m=new int[]{40,80};}
                as=new float[]{0,.7f};
                mh=900;
                d=275;
                fc=5;
                break;
            case 1:
                n="poppy";
                m=new int[]{60,100};
                if (nv){ m=new int[]{50,90};}
                mh=1170;
                as=new float[]{0,.55f};
                armor=45;
                d=150;
                fc=30;
                break;
            case 2:
                n="ahri";
                m=new int[]{0,60};
                as=new float[]{0,.75f};
                mh=1080;
                d=500;
                fc=3;
                break;
            case 3:
                n="syndra";
                m=new int[]{0,60};
                as=new float[]{0,.7f};
                mh=1080;
                d=100;
                fc=2;
                if (nv){
                    d=125;
                }

                break;
            case 4:
                n="neeko";
                m=new int[]{75,150};
                if (nv){ m=new int[]{50,120};}
                as=new float[]{0,.65f};
                armor=45;
                d=250;
                mh=1440;
                fc=25;
                break;
            case 5:
                n="soraka";
                m=new int[]{50,125};
                as=new float[]{0,.75f};
                if (nv){ m=new int[]{50,120}; }
                armor=30;
                mh=1260;
                fc=3;
                d=0;
                break;
        }
        h=new int[]{mh,mh};
        as[1]=1f/as[1];
        as[0]=as[1];
    }
    public void update(float d, ArrayList<Unit> us, Main mn){
        if (h[0]<=0){us.remove(this);return;}
        as[0]-=d;
        if (as[0]<=0){
            m[0]+=10;
            as[0]=as[1];
        }
        if (m[0]>=m[1]){
            cast(us, mn);
        }
    }
    public void cast(ArrayList<Unit> us, Main mn){
        m[0]=0;
        as[0]=0;
        casts++;
        if (n.equalsIgnoreCase("poppy")){
            h[0]=h[0]+300;
            if (h[0]>h[1]+300){h[0]=h[1]+300;}
        }
        mn.addDmg(this);
        for (Unit u:us){
            if (u.h[0]<=0){continue;}
            if (n.equalsIgnoreCase("soraka")){
                if (u.h[0]<u.h[1]) {
                    u.h[0] += 500;
                    if (u.h[0] > u.h[1]) {
                        u.h[0] = u.h[1];
                    }
                }
            }
            if (u.index!=index){
                u.m[0]+=Math.round(buff/(us.size()-1));
                if(u.m[0]>=u.m[1]) {
                    u.cast(us, mn);
                }
            }
        }
    }

    public void takeDamage(int dmg){
        int rd=(int)(dmg*(100f/(100+armor)));
        h[0]-=rd;
        //int mr=(int)Math.round(rd*(.08));
        int mr=(int)Math.round(((float)rd/h[1])*m[1]);
        m[0]+=(mr<40)?mr:40;
    }
}
