import java.awt.*;
import java.util.ArrayList;

public class body {
    float vx, vy;
    float x, y;
    float mass;
    int size;

    public body(int x, int y, double vx, double vy, float m){
        this.x=x;
        this.mass=m;
        this.y=y;
        this.vx=(float)vx;
        this.vy=(float)vy;
        size=(int)(5*mass);

    }

    public void update(ArrayList<body> b, Main m){

        float nx=x+vx;
        float ny=y+vy;
        body z=m.doesCollide(this,nx,ny);
        if (z==null){
            x=nx;
            y=ny;
        }else {
            System.out.println("collide");
            float energyGain = 1f;
            float vx1 = vx, vy1 = vy;
            vy = energyGain * (z.vy * z.mass) / mass;
            vx = energyGain * (z.vx * z.mass) / mass;
            z.vy = energyGain * (vy1 * mass) / z.mass;
            z.vx = energyGain * (vx1 * mass) / z.mass;
        }
        size=(int)(5*mass);

        for (int i=0; i<b.size(); i++){
            if (b.get(i)!=this){
                pullTowards(b.get(i));
            }
        }
    }
    public void pullTowards(body b){
        float dx=b.x-x;
        float dy=b.y-y;
        //float dir=(float)(Math.atan(dy/dx));
        float dist=(float)(Math.sqrt(dx*dx+dy*dy));
        float a=(b.mass/(dist*dist))*.1f;
        accel((float)(dx*a),(float)(dy*a));
    }

    public void accel(float x, float y){
        //System.out.println("accelerating "+x+", "+y);
        vx+=x;
        vy+=y;
    }

    public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.fillOval((int)x-size/2,(int)y-size/2,size,size);
    }

    public boolean isSurroundingB(body b){
        if (Math.abs(x-b.x)<size/2&&Math.abs(y-b.y)<size/2){
            return true;
        }
        return false;
    }
}
