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
        float efficiency=1;
        if (m.mode==4){
            efficiency=.5f;
        }
        float nx=x+vx;
        float ny=y+vy;
        if (m.mode!=1&&m.mode!=0) {
            if ((Math.abs(nx - (m.WIDTH / 2)) > m.WIDTH / 4)) {
                if (m.doesCollideWithWall(this, nx, y)) {
                    vx = -vx*efficiency;
                }
            }
            if ((Math.abs(ny - (m.HEIGHT / 2)) > m.HEIGHT / 4)) {
                if (m.doesCollideWithWall(this, x, ny)) {
                    vy = -vy*efficiency;
                }
            }
            body z = m.doesCollide(this, nx, y);
            if (z == null) {
                x = nx;
            } else {
                float energyGain = efficiency;
                float vx1 = vx, vy1 = vy;
                vx = energyGain * (z.vx * z.mass) / mass;
                z.vx = energyGain * (vx1 * mass) / z.mass;
            }
            z = m.doesCollide(this, x, ny);
            if (z == null) {
                y = ny;
            } else {
                float energyGain = efficiency;
                float vx1 = vx, vy1 = vy;
                vy = energyGain * (z.vy * z.mass) / mass;
                z.vy = energyGain * (vy1 * mass) / z.mass;
            }
        }else {
            x=nx;
            y=ny;
        }
        size=(int)(5*mass);

        if (m.mode!=4) {
            for (int i = 0; i < b.size(); i++) {
                if (b.get(i) != this) {
                    pullTowards(b.get(i));
                }
            }
        }else {
            accelDown(.045f);
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
        //System.out.println("accelerating "+x+", "+y);\
        vx+=x;
        vy+=y;
    }

    public void accelDown(float amt){
        vy+=amt;
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
