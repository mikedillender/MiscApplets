import com.sun.javafx.geom.Vec2f;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener, MouseListener {

    //BASIC VARIABLES
    private final int WIDTH = 1280, HEIGHT = 900;
    private float radius=5;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;
    boolean gravOn=false;
    //COLORS
    Color background = new Color(255, 255, 255);
    //Color gridColor = new Color(150, 150, 150);
    //ArrayList<float[]> clicks = new ArrayList<>();
    float redScale=100;
    float intermolecularConstant=5;


    ArrayList<Vec2f[]> ptcls=new ArrayList<>();


    public void init() {//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        this.addMouseListener(this);
        img = createImage(WIDTH, HEIGHT);
        gfx = img.getGraphics();
        thread = new Thread(this);
        thread.start();

    }

    public void paint(Graphics g) {
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0, 0, WIDTH, HEIGHT);//background size

        for (Vec2f[] p: ptcls){
            float e=getEnergyOf(p)/redScale;
            if (e>1){e=1;}
            gfx.setColor(new Color(e,0,1-e));

            gfx.fillOval((int)(p[0].x-radius),(int)(p[0].y-radius),(int)(radius*2),(int)(radius*2));
        }
        gfx.setColor(new Color(0, 0, 0));
        gfx.setFont(gfx.getFont().deriveFont(30f));
        gfx.drawString(printEnergy(),50,60);
        gfx.drawString("gravity = "+gravOn+", intermolecular constant = "+intermolecularConstant,50,100);
        //FINAL
        g.drawImage(img, 0, 0, this);
    }

    public void update(Graphics g) { //REDRAWS FRAME
        paint(g);
        updateParticles();
        if (gravOn){
            gravitize();
        }
        attract(intermolecularConstant*radius*radius);
        //printEnergy();
    }


    public void run() {
        for (; ; ) {//CALLS UPDATES AND REFRESHES THE GAME

            //UPDATES

            repaint();//UPDATES FRAME
            try {
                Thread.sleep(15);
            } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("GAME FAILED TO RUN");
            }//TELLS USER IF GAME CRASHES AND WHY
        }
    }

    private void attract(float amt){
        for (Vec2f[] p:ptcls){
            float speed=getSpeedOf(p);
            int ind = ptcls.indexOf(p);
            boolean hasCollided=false;
            for (int i = 0; i < ptcls.size(); i++) {
                if (i==ind){continue;}
                Vec2f dv=new Vec2f(ptcls.get(i)[0].x-p[0].x,ptcls.get(i)[0].y-p[0].y);
                float dist=(float)(Math.sqrt(dv.x*dv.x+dv.y*dv.y));
                float accel=(float)(amt/(dist*dist));
                if (dv.x==0){dv.x=.00000001f;}
                float orient=(float)Math.atan(dv.y/dv.x);// ORIENT FROM A TO B
                if (dv.x<0){orient+=3.14f;}

                p[1].x+=(float)(accel*Math.cos(orient));
                p[1].y+=(float)(accel*Math.sin(orient));

            }


        }

    }

    private void addParticle(){
        float x=(float)(Math.random()*WIDTH*.8)+(WIDTH*.1f);
        float y=(float)(Math.random()*HEIGHT*.8)+(HEIGHT*.1f);
        float vx=(float)(Math.random()*3-1.5);
        float vy=(float)(Math.random()*3-1.5);
        for (int i = 0; i < ptcls.size(); i++) {
            Vec2f dv=new Vec2f(ptcls.get(i)[0].x-x,ptcls.get(i)[0].y-y);
            float dist=(float)(Math.sqrt(dv.x*dv.x+dv.y*dv.y));
            if (dist<=radius*2f){
                return;
            }
        }
        ptcls.add(new Vec2f[]{new Vec2f(x,y),new Vec2f(vx,vy)});

    }

    private String printEnergy(){
        float total=0;
        for (Vec2f[] p: ptcls){
            total+=getEnergyOf(p);
        }
        total=(Math.round(total*100))/100f;
        float avg=total/ptcls.size();
        redScale=avg*2;
        avg=(Math.round(avg*100)/100f);
        return "Total Kinetic = "+total+", "+ptcls.size()+" particles, avg energy = "+avg;

    }

    private void updateParticles(){
        for (Vec2f[] p:ptcls){
            Vec2f newp=new Vec2f(p[0].x+p[1].x,p[0].y+p[1].y);
            float speed=getSpeedOf(p);
            int b=hasBumpedWall(newp);
            if (b==-1) {
                int ind = ptcls.indexOf(p);
                boolean hasCollided=false;
                for (int i = 0; i < ptcls.size(); i++) {
                    if (i==ind){continue;}
                    Vec2f dv=new Vec2f(ptcls.get(i)[0].x-newp.x,ptcls.get(i)[0].y-newp.y);
                    float dist=(float)(Math.sqrt(dv.x*dv.x+dv.y*dv.y));

                    if (dist<=radius*2f){ // PERFORM COLLISION
                        if (dv.x==0){dv.x=.00000001f;}
                        float orient=(float)Math.atan(dv.y/dv.x);// ORIENT FROM A TO B
                        if (dv.x<0){orient+=3.14f;}
                        orient+=3.14f;

                        float vel2=getSpeedOf(ptcls.get(i));
                        float totalEnergy=speed*speed+vel2*vel2;

                        float energyDif=getEnergyOf(p)-getEnergyOf(ptcls.get(i));
                        //System.out.println(getEnergyOf(p)+" - "+getEnergyOf(ptcls.get(i))+" = "+energyDif);
                        float s1=(float)Math.sqrt(getEnergyOf(p)-(energyDif/2f));
                        float s2=(float)Math.sqrt(getEnergyOf(ptcls.get(i))+(energyDif/2f));

                        //DO CHANGES
                        p[1].x=(float)(s1*Math.cos(orient));
                        p[1].y=(float)(s1*Math.sin(orient));
                        orient+=3.14f;
                        ptcls.get(i)[1].x=(float)(s2*Math.cos(orient));
                        ptcls.get(i)[1].y=(float)(s2*Math.sin(orient));

                        float totalEnergy1=getEnergyOf(p)+getEnergyOf(ptcls.get(i));
                        //System.out.println("E0 = "+totalEnergy+", E1 = "+totalEnergy1);
                        hasCollided=true;

                    }
                }
                if (!hasCollided){
                    p[0]=newp;
                }
            }else {
                if (b%2==1){
                    p[1].x=-p[1].x;
                }else {
                    p[1].y=-p[1].y;
                }
            }
        }

    }

    private float getEnergyOf(Vec2f[] p){
        float speed=getSpeedOf(p);
        return speed*speed;
    }

    private float getSpeedOf(Vec2f[] p){
        return (float)(Math.sqrt(p[1].x*p[1].x+p[1].y*p[1].y));
    }

    private int hasBumpedWall(Vec2f pos){
        if (pos.x+radius>=WIDTH){
            return 1;
        }else if (pos.x-radius<=0){
            return 3;
        }else if (pos.y+radius>=HEIGHT){
            return 2;
        }else if (pos.y-radius<=0){
            return 0;
        }
        return -1;
    }

    public void changeSpeed(float amt) {
        for (Vec2f[] p : ptcls) {
            float speed=(float)(amt*(Math.sqrt(p[1].x*p[1].x+p[1].y*p[1].y)));
            float orient=(float)Math.atan(p[1].y/p[1].x);
            if (p[1].x<0){orient+=3.14f;}
            p[1].x=(float)(speed*Math.cos(orient));
            p[1].y=(float)(speed*Math.sin(orient));

        }
    }
    private void gravitize(){
        for (Vec2f[] p : ptcls) {
            p[1].y+=.05f;
        }
    }

    //INPUT
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_R) {
            ptcls=new ArrayList<>();
        }else if(e.getKeyCode()==KeyEvent.VK_P){
            for (int i=0; i<50; i++){addParticle();}
        }if (e.getKeyCode()==KeyEvent.VK_DOWN){
            changeSpeed(.9f);
        }else if (e.getKeyCode()==KeyEvent.VK_UP){
            changeSpeed(1.1f);
        }else if (e.getKeyCode()==KeyEvent.VK_RIGHT){
            intermolecularConstant+=1f;
        }else if (e.getKeyCode()==KeyEvent.VK_LEFT){
            intermolecularConstant-=1f;
        }else {
            addParticle();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_G){
            gravOn=!gravOn;
        }
    }

    public void keyTyped(KeyEvent e) {
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}