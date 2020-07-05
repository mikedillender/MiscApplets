import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener {

    //BASIC VARIABLES
    private final int WIDTH=1280, HEIGHT=900;

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(150, 150,150);

    ArrayList<Unit> us;
    ArrayList<Unit> us1;
    float time=0;
    int td=0;
    int tdps=0;
    int td1=0;
    int td1ps=0;
    float[] dmgt=new float[]{.2f,.2f};
    float[] dmgrange=new float[]{100,300};
    ArrayList<int[]> tds;
    ArrayList<int[]> tdpss;
    int[] avgtd=new int[]{0,0};
    int[] avgtdps=new int[]{0,0};

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        gfx.setFont(gfx.getFont().deriveFont(20f));
        us=new ArrayList<>();
        us1=new ArrayList<>();
        tds=new ArrayList<>();
        tdpss=new ArrayList<>();
        for (int i=0; i<6; i++){
            us.add(new Unit(i,i,false));
            us1.add(new Unit(i,i,true));
        }
        thread=new Thread(this);
        thread.start();
    }

    public void reset(){
        time=0;
        dmgt=new float[]{.2f,.2f};
        tds.add(new int[]{td,td1});
        tdpss.add(new int[]{tdps,td1ps});
        td=0;
        td1=0;
        tdps=0;
        td1ps=0;
        us=new ArrayList<>();
        us1=new ArrayList<>();
        for (int i=0; i<6; i++){
            us.add(new Unit(i,i,false));
            us1.add(new Unit(i,i,true));
        }
        avgtd=new int[]{0,0};
        avgtdps=new int[]{0,0};
        for (int[] s: tds){
            avgtd[0]+=s[0];
            avgtd[1]+=s[1];
        }
        for (int[] s: tdpss){
            avgtdps[0]+=s[0];
            avgtdps[1]+=s[1];
        }
        avgtd[0]=(int)((float)avgtd[0]/tds.size());
        avgtd[1]=(int)((float)avgtd[1]/tds.size());
        avgtdps[0]=(int)((float)avgtdps[0]/tdpss.size());
        avgtdps[1]=(int)((float)avgtdps[1]/tdpss.size());

    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size
        //paintCoordGrid(gfx);

        //RENDER FOREGROUND
        for (int i=0; i<us.size(); i++){
            gfx.setColor(Color.BLACK);
            gfx.fillRect(50, 80+(80*i),200,50);
            gfx.drawString(us.get(i).n+" | Casts : "+us.get(i).casts +" (C/s = "+((Math.round(us.get(i).casts/time*100))/100f),50, 70+(80*i));
            gfx.setColor(Color.BLUE);
            gfx.fillRect(50, 105+(80*i),(int)(200*(us.get(i).m[0]/(float)us.get(i).m[1])),25);
            gfx.setColor(Color.RED);
            gfx.fillRect(50, 80+(80*i),(int)(200*(us.get(i).h[0]/(float)us.get(i).h[1])),25);

        }
        for (int i=0; i<us1.size(); i++){

            gfx.setColor(Color.BLACK);
            gfx.fillRect(350, 80+(80*i),200,50);
            gfx.drawString(us1.get(i).n+" | Casts : "+us1.get(i).casts +" (C/s = "+((Math.round(us1.get(i).casts/time*100))/100f),350, 70+(80*i));
            gfx.setColor(Color.BLUE);
            gfx.fillRect(350, 105+(80*i),(int)(200*(us1.get(i).m[0]/(float)us1.get(i).m[1])),25);
            gfx.setColor(Color.RED);
            gfx.fillRect(350, 80+(80*i),(int)(200*(us1.get(i).h[0]/(float)us1.get(i).h[1])),25);

        }
        gfx.setColor(Color.black);
        gfx.drawString("Time Elapsed : "+(Math.round(time*10)/10f),100, 600);
        gfx.drawString("Old Dmg = "+td+" | New Dmg = "+td1,300, 600);
        gfx.drawString("(AVERAGE) Old Dmg = "+avgtd[0]+" | New Dmg = "+avgtd[1],100, 650);
        gfx.drawString("(AVERAGE) Old Dmg/s = "+avgtdps[0]+" | New Dmg/s = "+avgtdps[1],100, 700);
        gfx.drawString("Trials : "+tds.size(),100, 750);
        gfx.drawString("Blue Bar - Mana | Red Bar - Health",100, 800);
        gfx.drawString("Current Patch                          New Patch",70, 35);
        /*
        float syndrarat=us1.get(i).casts/(float)us[3].casts;
        gfx.drawString("Time Elapsed : "+time,200, 600);
        gfx.drawString("New Syndra Dmg / Old Syndra dmg (Total)",400, 600);
        gfx.drawString("1 Star = "+(syndrarat*(85/75f)),400, 630);
        gfx.drawString("2 Star = "+(syndrarat*(125/100f)),400, 660);
        gfx.drawString("3 Star = "+(syndrarat*(225/175f)),400, 690);
        */

        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }

    public void trials(int t){
        while (tds.size()<t+100){
            runUpdates(.2f);
        }
    }
    public void runUpdates(float deltatime){

        time+=deltatime;
        if (time>30){deltatime*=2;}
        dmgt[0]-=deltatime;
        if (dmgt[0]<0){
            dmgt[0]=dmgt[1];
            //hurtrandom();
        }
        //UPDATES
        for (int i=0; i< us.size();i++) {
            if (us.get(i).h[0] < 0) { us.remove(i);i--;continue; }
            us.get(i).update(deltatime, us, this);
        }
        for (int i=0; i< us1.size();i++){
            if (us1.get(i).h[0]<0){us1.remove(i);i--;continue;}
            us1.get(i).update(deltatime, us1, this);
        }
        if (us.size()==0&&tdps==0||time>40){
            tdps=(int)((float)td/time);
        }
        if (us1.size()==0&&td1ps==0||time>40){
            td1ps=(int)((float)td1/time);
        }
        if (us.size()==0&&us1.size()==0||time>40){
            reset();
        }
    }
    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

            float deltatime=.01f;
            runUpdates(deltatime);

            repaint();//UPDATES FRAME
            try{ Thread.sleep(5); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }


    public void hurtrandom(){
        int d=(int)(dmgrange[0]+(Math.sqrt(Math.random())*(dmgrange[1]-dmgrange[0])));
        if (us.size()>0) {
            getTarget(us).takeDamage(d);
        }
        if (us1.size()>0) {
            getTarget(us1).takeDamage(d);
        }
    }
    public Unit getTarget(ArrayList<Unit> l){
        int sum=0;
        for (Unit u: l){
            sum+=u.fc;
        }
        int n=(int)Math.floor(Math.random()*sum);
        sum=0;
        for (Unit u: l){
            if (sum+u.fc>n){u.fc+=(int)(10/(time)); return u;}
            sum+=u.fc;
        }
        return l.get(0);
    }

    //INPUT
    public void keyPressed(KeyEvent e) {
        trials(tds.size());
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

    public void addDmg(Unit u){
        int dmg=u.d;
        if (u.n.equalsIgnoreCase("syndra")){
            dmg=dmg*3*u.casts;
        }
        if (us.contains(u)){
            td+=dmg;
        }else if(us1.contains(u)){
            td1+=dmg;
        }
    }
}