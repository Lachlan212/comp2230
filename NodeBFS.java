public class NodeBFS {
    private int[] location;

    public NodeBFS(int x, int y){
        location = new int[2];
        setLocation(x, y);
    }

    public void setLocation(int x, int y){
        location[0] = x;
        location[1] = y;
    }

    public int[] getLocation(){
        return location;
    }

    public int getX(){
        return location[0];
    }

    public int getY(){
        return location[1];
    }

    public int get(int num){
       return location[num];
    }
}
