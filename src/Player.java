public class Player {
    public float x=0,y=0,z=0, v=.05f;
    public float fovx=3.14149f*1.2f;
    public float fovy=3.14149f*1.2f;
    public float ox=0,oy=0;
    public Player(){
    }

    public void control(int dir){
        if (dir<=3) {

            boolean pos = dir < 2;
            boolean straight = (dir % 2) == 0;
            float dx = (float) (v * Math.cos(ox));
            float dz = (float) (v * Math.sin(ox));
            if (pos) {
                x += (straight) ? dx : dz;
                z += (straight) ? dz : dx;
            } else {
                x -= (straight) ? dx : dz;
                z -= (straight) ? dz : dx;
            }
        }else {
            if (dir==4){
                y+=v;
            }else if(dir==5){
                y-=v;
            }else if(dir==6){
                ox+=v/4.0;
            }else if(dir==7){
                ox-=v/4.0;
            }

        }


    }
}
