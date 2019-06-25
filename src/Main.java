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
    int width=16;
    int size=(int)(Math.floor(WIDTH/width));

    int sizeindex=4;
    int[] sizes=new int[]{2,4,8,12,16,20,22,24,26,28,30,32};
    boolean[] sizeBinary;
    boolean[] sizeLines;
    float randomization=.05f;

    public void setRules(){
        sizeBinary=new boolean[sizes.length];
        for (int i=0; i<sizes.length; i++) {
            if (i < 3 || i == 4) {
                sizeBinary[i] = true;
            }
        }
        sizeLines=new boolean[sizes.length];
        for (int i=0; i<sizes.length; i++){
            if (i<5){
                //sizeLines[i];
                //sizeBinary[i]=true;
            }
        }
    }

    //GRAPHICS OBJECTS
    private Thread thread;
    Graphics gfx;
    Image img;

    //COLORS
    Color background=new Color(255, 255, 255);
    Color gridColor=new Color(150, 23, 13);
    Color red=new Color(83, 2, 1);
    Color lightred=new Color(209, 157, 160);
    Color green=new Color(1, 34, 1);
    Color lightgreen=new Color(209, 242, 205);
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


    public void create(boolean random,boolean symm, boolean lines){
        if (lines){makeLineMatrix();return;}
        while (!make(random,symm)){}
    }

    public void init(){//STARTS THE PROGRAM
        this.resize(WIDTH, HEIGHT);
        this.addKeyListener(this);
        this.addMouseListener(this);
        setRules();
        //make(false,false);
        //create(true);
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
    public boolean[] getSymRowID(int id, int rn){
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
        for (int x=0; x<rn; x++){
            row[x]=m[x][rn];
        }
        return row;
    }

    private void addLines(){
        for (int i=0; i<width*2-1; i++){
            addLine(i,Math.random()<.5);
        }
    }

    private void makeLineMatrix(){
        int allIDs=(int)(Math.pow(2,(width*2-1)));
        //int startID=(int)(Math.random()*allIDs);
        //int id=startID;
        int id=0;
        //boolean first=true;
        /*
        while (!isCorrect()&&(((startID>=id)&&!first)||(first))){
            addLinesID(id, width*2-1);
            if (id%1000000==0){
                System.out.println(id+"/"+allIDs+" = "+(100*(id/(float)allIDs))+" %");
            }
            id++;
            if (id>=allIDs&&first){
                first=false;
                id=0;
            }
        }*/
        while (!isCorrect()){
            smartLines();
            id++;
            if (id%1000000==0){
                System.out.println(id+"/"+allIDs+" = "+(100*(id/(float)allIDs))+" %");
            }
        }
    }

    private void smartLines(){
        int tr=0;
        int f=1;
        int mt=width/2;
        for (int i=0; i<=width; i++){
            if (tr<=mt){
                if (Math.random()<.5||f>=mt){
                    tr++;
                    addLine(i,true);
                }else {
                    addLine(i,false);
                }
            }else {
                addLine(i,false);
            }
        }
        tr=0;
        f=1;
        for (int i=width; i<width*2-1; i++){
            if (tr<=mt){
                if (Math.random()<.5||f>=mt){
                    tr++;
                    addLine(i,true);
                }else {
                    addLine(i,false);
                }
            }else {
                addLine(i,false);
            }
        }

    }

    private void addLinesID(int id, int tl){
        int r=id;
        //boolean[] row=new boolean[tl];
        for (int i=tl-1; i>=0; i--){
            int pow=(int)(Math.pow(2,i));
            //System.out.println("int id "+id+" y="+(width-1-i)+" i="+i+" pow = "+pow+" r="+r);
            addLine(i,r>=pow);
            if (r>=pow){
                r-=pow;
            }
        }
    }

    private boolean isCorrect(){
        boolean works=true;
        for (int i=0; i<width; i++){
            if (!checkRow(i)){
                works=false;
            }
        }
        return works;
    }

    private void addLine(int p,boolean tru){
        int sx=0,sy=0;
        if (p<=width){
            sx=0;
            sy=p;
        }else {
            sx=p-width;
            sy=0;
        }
        sx++;
        sy++;
        while (sx<width&&sy<width){
            m[sx][sy]=tru;
            sx++;
            sy++;
        }
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

    public boolean make(boolean rand, boolean symm){

        int allIds=(int)(Math.pow(2,width));

        ArrayList<Integer>[] prohib=new ArrayList[width];
        int id=0;
        prohib[0]=new ArrayList<>();
        int[] startids=new int[width];
        int incSize=(int)(Math.ceil(allIds)/(width*width));
        for (int i=1; i<m.length; i++){
            if (prohib[i]==null){prohib[i]=new ArrayList<>();}
            if (symm){
            //    allIds=(int)(Math.pow(2,width-i));
            }
            startids[i]=id;
            System.out.println("row "+i);
            boolean done=false;
            long s=System.nanoTime();
            int lastId=id;
            int iter=0;
            //if (symm){id=0;}
            while ((id<allIds||rand)&&!done) {
                if (rand){
                    m[i]=getRandomRow();
                }else{
                    for (Integer pid:prohib[i]){
                        if (pid-id==0){
                            id++;
                        }
                    }
                    m[i] = (!symm)?getRowID(id):getSymRowID(id,i);
                }

                if ((id%incSize==0&&!rand&&id>1000000)){
                    System.out.println(id+"/"+allIds+" ( "+Math.round(10000f*(id/(float)allIds))/100.0+"% )");
                }else if ((rand&&iter%incSize==0&&iter>1000000)){
                    System.out.println(iter+" iterations");
                    double dt=((System.nanoTime()-s)/1000000000.0);
                    if (dt>4*i){
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
                id++;
                iter++;

            }
            if (!rand) {
                if (id == allIds) {
                    prohib[i]=new ArrayList<>();
                    if (i!=0) {
                        prohib[i - 1].add(lastId);
                        id = startids[i - 1];
                        i = i - 2;
                    }
                }
            }
        }

        return true;
    }

    public void paint(Graphics g){
        //BACKGROUND
        gfx.setColor(background);//background
        gfx.fillRect(0,0,WIDTH,HEIGHT);//background size

        gfx.setFont(gfx.getFont().deriveFont(30f));
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

        gfx.setColor(Color.RED);
        gfx.drawString("Difficulty = "+randomization,50,50);
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
        make(rand,false);
    }

    private void randomize(float percent){
        System.out.println("randomizing "+percent);
        for (int x=0; x<width; x++){
            System.out.println("");
            for (int y=0; y<width; y++){
                if (percent>Math.random()){
                    m[x][y]=Math.random()<.5;
                    System.out.print("("+x+","+y+") ");
                }
            }
        }
    }

    public void createRandom(){
        double dice=Math.random();
        ArrayList<Integer> pg=new ArrayList<>();
        pg.add(0);
        float[] c=new float[3];
        c[0]=.5f;
        if (sizeLines[sizeindex]){
            pg.add(1);
            c[1]=.25f;
        }
        if (sizeBinary[sizeindex]){
            pg.add(2);
            c[2]=.25f;
        }
        float d1=(float)(Math.random()*(c[0]+c[1]+c[2]));
        System.out.println("dice "+d1);
        if (d1<c[0]){
            System.out.println("making random");
            create(true,false,false);
        }else if (c[1]!=0&&d1<c[1]+c[0]){
            System.out.println("making lines");
            create(false,false,true);
        }else {
            System.out.println("doing standard");
            create(false,false,false);
        }

    }

    //INPUT
    public void keyPressed(KeyEvent e) {
        //System.out.println("pressed "+e.getKeyCode());
        if (e.getKeyCode()==KeyEvent.VK_SPACE){
            createRandom();
        }else if (e.getKeyCode()==KeyEvent.VK_R){
            randomize(randomization);
        }else if (e.getKeyCode()==KeyEvent.VK_UP){
            if (randomization+.1f<1){
                randomization+=.01f;
            }
        }else if (e.getKeyCode()==KeyEvent.VK_DOWN){
            if (randomization-.01f>=0){
                randomization-=.01f;
            }
        }
        /*if (e.getKeyCode()==KeyEvent.VK_M) {
            create(false,false,false);
        }
        if (e.getKeyCode()==KeyEvent.VK_N) {
           create(true,false,false);
        }
        if (e.getKeyCode()==KeyEvent.VK_S) {
            make(false,true);
        }
        if (e.getKeyCode()==KeyEvent.VK_L) {
            addLines();
        }
        if (e.getKeyCode()==KeyEvent.VK_O) {
            makeLineMatrix();
        }*/
        if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
            if (sizeindex+1<sizes.length) {
                sizeindex++;
                width=sizes[sizeindex];
                System.out.println("size = "+width);
                size=(int)(Math.floor(WIDTH/width));
                m=new boolean[width][width];
            }
        }
        if (e.getKeyCode()==KeyEvent.VK_LEFT) {
            if (sizeindex-1>=0) {
                sizeindex--;
                width=sizes[sizeindex];
                System.out.println("size = "+width);
                size=(int)(Math.floor(WIDTH/width));
                m=new boolean[width][width];
            }

        }
        randomization=(float)(Math.round(randomization*100)/100f);
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