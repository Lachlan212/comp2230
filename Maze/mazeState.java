package Maze;

import java.io.Serializable;

public class mazeState implements Serializable{
    String mazeString;

    public mazeState(Node[][] maze, int[] start, int[] finish, int maxRow, int maxColumn){
        mazeString = getData(maze, start, finish, maxRow, maxColumn);
    }

    public String getData(Node[][] maze, int[] start, int[] finish, int maxRow, int maxColumn){
        String savedInfo =  maxRow + "," + maxColumn + ":" + (maxColumn * start[0] + start[1] + 1) + ":" + (maxColumn * finish[0] + finish[1] + 1) + ":"; //note starts at node 1 and not node 0
        String cell_openness_list = "";
        for(int i=0; i<maxRow; i++){
            for(int j=0; j<maxColumn; j++){
                cell_openness_list += maze[i][j].getState();
            }
        } 
        savedInfo += cell_openness_list;
        return savedInfo;

    }

    public String getMazeState(){
        return mazeString;
    }

    public String toString(){
        return mazeString;
    }
}
