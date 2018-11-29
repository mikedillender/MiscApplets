import java.util.ArrayList;

public class Node {
    int[] pos;
    ArrayList<int[]> connections;
    public Node(int id){
        connections=new ArrayList<>();
        switch (id){
            case 0:
                pos=new int[]{5,1};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 1:
                pos=new int[]{6,1};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 2:
                pos=new int[]{5,2};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 3:
                pos=new int[]{6,2};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 4:
                pos=new int[]{1,3};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 5:
                pos=new int[]{2,3};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 6:
                pos=new int[]{3,3};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 7:
                pos=new int[]{4,3};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 8:
                pos=new int[]{5,3};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 9:
                pos=new int[]{6,3};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 10:
                pos=new int[]{2,4};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 11:
                pos=new int[]{3,4};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 12:
                pos=new int[]{4,4};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 13:
                pos=new int[]{5,4};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 14:
                pos=new int[]{6,4};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 15:
                pos=new int[]{1,5};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 16:
                pos=new int[]{2,5};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 17:
                pos=new int[]{5,6};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 18:
                pos=new int[]{6,6};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 19:
                pos=new int[]{1,6};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 20:
                pos=new int[]{2,6};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 21:
                pos=new int[]{5,7};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 22:
                pos=new int[]{6,7};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 23:
                pos=new int[]{5,8};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
            case 24:
                pos=new int[]{6,8};
                connections.add(new int[]{1,1});
                connections.add(new int[]{2,1});
                break;
        }
    }

    public void addConnections(){
        connections=new ArrayList<>();

    }

    public int[] getPos() {
        return pos;
    }

    public ArrayList<int[]> getConnections() {
        return connections;
    }
}
