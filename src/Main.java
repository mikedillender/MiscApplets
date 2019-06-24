import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Main extends Applet implements Runnable, KeyListener, MouseListener {

    //BASIC VARIABLES
    private final int WIDTH=1000, HEIGHT=1000;
    int width=20;
    int size=(int)(Math.floor(WIDTH/width));

    int sizeindex=1;
    int[] sizes=new int[]{2,4,8,12,16};

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(150, 23, 13);
    Color red=new Color(83, 2, 1);
    Color lightred=new Color(209, 157, 160);
    Color green=new Color(1, 55, 2);
    Color lightgreen=new Color(168, 209, 165);
    Color grey=new Color(122, 122, 122);
    Color black=new Color(0,0,0);


    boolean[][] m=new boolean[width][width];


    private boolean doesAgree(boolean[] b, boolean[] b1){
        int a=0;
        for (int i=0; i<b.length; i++){
            if (b[i]==b1[i]){
                a++;
            }
        }
        return a==b.length/2;
    }


    public void create(boolean random){
        while (!make(random)){}
    }

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        this.addMouseListener(this);
        //make(false);
        create(true);
        img=createImage(WIDTH,HEIGHT);
        gfx=img.getGraphics();
        thread=new Thread(this);
        thread.start();
    }

    public boolean[] getRandomRow(){
        boolean[] row=new boolean[m.length];
        for (int i=0; i<row.length; i++){
            row[i]=Math.random()<.5;
        }
        return row;
    }

    public boolean[] getRowID(int id){
        int r=id;
        boolean[] row=new boolean[m.length];
        for (int i=row.length-1; i>=0; i--){
            int pow=(int)(Math.pow(2,i));
            //System.out.println("int id "+id+" y="+(width-1-i)+" i="+i+" pow = "+pow+" r="+r);
            row[width-1-i]=r>=pow;
            if (row[width-1-i]){
                r-=pow;
            }
        }
        return row;
    }

    public boolean checkRow(int i){
        boolean works = true;
        for (int n = 0; n < i; n++) {
            if (!doesAgree(m[i], m[n])) {
                works = false;
            }
        }
        return works;
    }

    public boolean make(boolean rand){

        int allIds=(int)(Math.pow(2,width));

        ArrayList<Integer>[] prohib=new ArrayList[width];
        int id=0;

        int[] startids=new int[width];
        for (int i=1; i<m.length; i++){
            if (prohib[i]==null){prohib[i]=new ArrayList<>();}
            startids[i]=id;
            System.out.println("row "+i);
            boolean done=false;
            long s=System.nanoTime();
            int lastId=id;
            int iter=0;
            while ((id<allIds||rand)&&!done) {
                if (rand){
                    iter++;
                    m[i]=getRandomRow();
                }else {
                    id++;
                    for (Integer pid:prohib[i]){
                        if (pid-id==0){
                            id++;
                        }
                    }
                    m[i] = getRowID(id);
                }
                if ((id%1000000==0&&!rand)||(rand&&iter%1000000==0)){
                    System.out.println(id+"/"+allIds+" ( "+Math.round(10000f*(id/(float)allIds))/100.0+"% )");
                    double dt=((System.nanoTime()-s)/1000000000.0);
                    if (dt>5&&rand){
                        return false;
                    }
                }
                boolean works = true;
                for (int n = 0; n < i; n++) {
                    if (!doesAgree(m[i], m[n])) {
                        works = false;
                    }
                }
                if (works){
                    System.out.println("row "+i+" found at "+((rand)?iter:id));
                    startids[i]=id;
                    done=true;
                }
            }
            if (!rand) {
                if (id == allIds) {
                    prohib[i - 1].add(lastId);
                    id = startids[i - 1];
                    i = i - 2;
                }
            }
        }

        return true;
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size

        gfx.setColor(Color.BLACK);
        //RENDER FOREGROUND
        for (int x=0; x<m.length; x++){
            if (checkRow(x)){
                gfx.setColor(lightgreen);
                gfx.fillRect(x*size,0,size,HEIGHT);
                gfx.setColor(green);
            }else {
                gfx.setColor(Color.BLACK);
                /*
                gfx.setColor(lightred);
                gfx.fillRect(x*size,0,size,HEIGHT);
                gfx.setColor(red);*/
            }
            for (int y=0; y<m.length; y++){
                if (!m[x][y]){
                    gfx.fillRect(x*size,y*size,size,size);
                }
            }
        }
        paintCoordGrid(gfx,size);

        //FINAL
        g.drawImage(img,0,0,this);
    }

    public void update(Graphics g){ //REDRAWS FRAME
        paint(g);
    }


    public void run() { for (;;){//CALLS UPDATES AND REFRESHES THE GAME

            //UPDATES


            repaint();//UPDATES FRAME
            try{ Thread.sleep(15); } //ADDS TIME BETWEEN FRAMES (FPS)
            catch (InterruptedException e) { e.printStackTrace();System.out.println("GAME FAILED TO RUN"); }//TELLS USER IF GAME CRASHES AND WHY
    } }

    private void createNew(boolean rand){
        make(rand);
    }

    private void randomize(float percent){
        System.out.println("randomizing "+percent);
        for (int x=0; x<width; x++){
            System.out.println("");
            for (int y=0; y<width; y++){
                if (percent<Math.random()){
                    m[x][y]=Math.random()<.5;
                    System.out.print("("+x+","+y+") ");
                }
            }
        }
    }

    //INPUT
    public void keyPressed(KeyEvent e) {
        System.out.println("pressed "+e.getKeyCode());
        if (e.getKeyCode()==KeyEvent.VK_M) {
            make(false);
        }
        if (e.getKeyCode()==KeyEvent.VK_N) {
            make(true);
        }
        if (e.getKeyCode()<=57&&e.getKeyCode()>=48){
            float p=(e.getKeyCode()-48f)/10f;
            if (p==0){
                p=1;
            }
            randomize(p);
        }
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyTyped(KeyEvent e) { }

    //QUICK METHOD I MADE TO DISPLAY A COORDINATE GRID
    public void paintCoordGrid(Graphics gfx,int size){
        gfx.setColor(grey);
        for (int x=0; x<width; x++){
            gfx.drawString(""+x, x, 20);
            gfx.drawRect(x*size, 20, 1, HEIGHT);
        }
        for (int y=0; y<width; y++){
            gfx.drawString(""+y, 20, y);
            gfx.drawRect(20, y*size, WIDTH, 1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        System.out.print("clicked "+x+", "+y+" (");
        x=(int)(Math.floor(x/(float)size));
        y=(int)(Math.floor(y/(float)size));
        System.out.print(x+", "+y+")");
        System.out.println("");
        if (x<width&&y<width){
            m[x][y]=!m[x][y];
        }

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