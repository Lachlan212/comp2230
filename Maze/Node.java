package Maze;

public class Node {

    private boolean visited;
    private int state; // 0 = closed, 1 = right-only open, 2 = down-only open, 3 = both open

    public Node() {
        visited = false;
        state = 0;
    }

    public void visit() {
        visited = true;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setState(int x) {
        state = x;
    }

    public int getState() {
        return state;
    }

    public void openDown() {
        if (state == 0) {
            state = 2;
        } else {
            state = 3;
        }

    }

    public void openRight() {
        if (state == 0) {
            state = 1;
        } else {
            state = 3;
        }

    }

    public String toString() {
        return state + ", ";
    }

}
