import java.awt.*;
import java.util.ArrayList;

public class Arm {
    float length, orientation, x, y;
    ArrayList<Arm> arms;
    ArrayList<Float> armLocs;
    float torque=0;

    public Arm(float x, float y, float orient, float L){
        arms=new ArrayList<>();
        armLocs=new ArrayList<>();
        this.x=x;
        this.y=y;
        orientation=orient;
        length=L;
    }

    public void addArm(Arm a1, float l1){
        arms.add(a1);
        armLocs.add(l1);
    }
    public Arm(Arm a1, Arm a2, float l1, float l2){
        arms=new ArrayList<>();
        armLocs=new ArrayList<>();
        arms.add(a1);
        arms.add(a2);
        armLocs.add(0f);
        a1.addArm(this,l1);
        a2.addArm(this,l2);
        x=a1.x+(float)(Math.cos(a1.orientation)*l1);
        y=a1.y+(float)(Math.sin(a1.orientation)*l1);
        float x1=a2.x+(float)(Math.cos(a2.orientation)*l2);
        float y1=a2.y+(float)(Math.sin(a2.orientation)*l2);
        length=(float)(Math.sqrt((x1-x)*(x1-x)+(y1-y)*(y1-y)));
        armLocs.add(length);
        orientation=(float)(Math.tan((y1-y)/(x1-x)));
        if (y1-y<0&&x1-x<0){
            orientation+=Math.PI;
        }

    }

    public ArrayList<Arm> getArms() {
        return arms;
    }

    public ArrayList<Float> getArmLocs() {
        return armLocs;
    }

    public void render(Graphics g,float scale){
        int x=(int)(this.x*scale);
        int y=(int)(this.y*scale);
        int x1=x+(int)(length*Math.cos(orientation)*scale);
        int y1=y+(int)(length*Math.sin(orientation)*scale);
        //System.out.println("drawing line from "+x+", "+y+" to "+x1+", "+y1);
        g.drawLine(x,y,x1,y1);
        g.drawString("t = "+torque,(x+x1)/2,(y+y1)/2);

    }
    public void addTorque(float r, float f, int loop){
        torque=r*f;
        spreadTorque(torque, this, loop);
    }

    public void spreadTorque(float t, Arm a1, int loop){
        System.out.println("spreading torque to "+length);
        if (torque==0) {
            this.torque = t;
        }
        if (loop>10){return;}
        for (int i=0; i<arms.size(); i++){
            if (arms.get(i)!=a1){
                Arm a=arms.get(i);
                if(armLocs.get(i)==0){continue;}
                float f=(t/armLocs.get(i))*a.getArmLocs().get(a.arms.indexOf(this));
                System.out.println("spreading torque of "+f+" to "+arms.get(i).length);
                arms.get(i).spreadTorque(f, this,loop+1);
            }
        }
    }
}
