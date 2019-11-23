import com.sun.javafx.geom.Vec2f;
import com.sun.javafx.geom.Vec3f;

import java.util.ArrayList;

public class Node {

    ArrayList<Node> connected=new ArrayList<>();
    Vec2f pos;
    Vec2f vel;
    boolean outside;

    public Node(Vec2f p, Vec2f v, boolean outside){
        this.vel=v;
        this.pos=p;
        this.outside=outside;
    }
    public void update(float delta){
        if (outside){return;}
        pos.x+=vel.x*delta;
        pos.y+=vel.y*delta;
        for (Node n:connected){
            //float di=n.distTo(this);
            vel.x+=(delta*(n.pos.x-this.pos.x)/4);
            vel.y+=(delta*(n.pos.y-this.pos.y)/4);
        }
    }
    public void connectClosest(int num, ArrayList<Node> nodes){
        //connected=new ArrayList<>();
        num=num-connected.size();
        for (int i=0; i<num; i++){
            Node b=null;
            float bd=10000;
            for(Node n: nodes){
                if (n!=this && !connected.contains(n)){
                    float d=n.distTo(this);
                    if (d<bd){
                        bd=d;
                        b=n;
                    }
                }
            }
            if (b!=null){
                connectTo(b);
            }
        }

    }

    public float distTo(Node n){
        return pos.distance(n.pos);
    }

    public void connectTo(Node n){
        if(n==this){return;}
        if (!connected.contains(n)) {
            connected.add(n);
        }
        if (!n.connected.contains(this)) {
            n.connected.add(this);
        }
    }

}
