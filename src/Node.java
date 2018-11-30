import java.util.ArrayList;

public class Node {
    int[] pos;
    ArrayList<int[]> connections;
    int id;
    public Node(int id){
        connections=new ArrayList<>();
        this.id=id;
        switch (id){
            case 0:
                pos=new int[]{5,1};
                break;
            case 1:
                pos=new int[]{6,1};
                break;
            case 2:
                pos=new int[]{5,2};

                break;
            case 3:
                pos=new int[]{6,2};

                break;
            case 4:
                pos=new int[]{1,3};

                break;
            case 5:
                pos=new int[]{2,3};

                break;
            case 6:
                pos=new int[]{3,3};
                break;
            case 7:
                pos=new int[]{4,3};
                break;
            case 8:
                pos=new int[]{5,3};
                break;
            case 9:
                pos=new int[]{6,3};
                break;
            case 10:
                pos=new int[]{2,4};
                break;
            case 11:
                pos=new int[]{3,4};
                break;
            case 12:
                pos=new int[]{4,4};
                break;
            case 13:
                pos=new int[]{5,4};
                break;
            case 14:
                pos=new int[]{6,4};
                break;
            case 15:
                pos=new int[]{1,5};
                break;
            case 16:
                pos=new int[]{2,5};
                break;
            case 17:
                pos=new int[]{5,6};
                break;
            case 18:
                pos=new int[]{6,6};
                break;
            case 19:
                pos=new int[]{1,6};
                break;
            case 20:
                pos=new int[]{2,6};
                break;
            case 21:
                pos=new int[]{5,7};
                break;
            case 22:
                pos=new int[]{6,7};
                break;
            case 23:
                pos=new int[]{5,8};
                break;
            case 24:
                pos=new int[]{6,8};
                break;
        }
    }

    public void addConnections(int[][] paths){
        connections=new ArrayList<>();
        for (int i=0; i<paths.length; i++){
            if (paths[i][0]==id||paths[i][1]==id){
                connections.add(paths[i]);
            }
        }

    }

    public int[] getPos() {
        return pos;
    }

    public ArrayList<int[]> getConnections() {
        return connections;
    }
}
