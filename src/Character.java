import java.awt.event.KeyEvent;

public class Character {
    int x, y;
    double xvel, yvel;
    int height, width;
    boolean[] pressing;
    Main main;

    public Character(Main m){
        this.main=m;
        height=20;
        width=20;
        xvel=0;
        yvel=0;
        x=main.getWidth()/2;
        y=main.getHeight()/2;
        pressing=new boolean[4];
    }

    public void render(){

    }

    public void move(){

    }

    public void updateControls(KeyEvent e, boolean pressed){

    }


}
