//  MazeGenerator
package Maze;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MazeGenerator{

    public static Random rnd = new Random();
    static int maxRow;
    static int maxColumn;
    public static void main(String[] args){

        maxRow = 3;
        maxColumn = 3;
        
        Node[][] maze = new Node[maxRow][maxColumn];
        for(int i=0; i< maxRow ; i++){
            for(int j=0; j<maxColumn; j++){
                maze[i][j] = new Node();
            }
        }

        int[] nextNode = {rnd.nextInt(maxRow), rnd.nextInt(maxColumn)};
        maze[nextNode[0]][nextNode[1]].visit();
        System.out.println(nextNode[0] + " " +  nextNode[1]);
        Stack<int[]> visited = new Stack<>();

        walkthrough(maze, nextNode, visited);
    }

    public static void walkthrough(Node[][] maze, int[] startingNode, Stack<int[]> visited){
        maze[startingNode[0]][startingNode[1]].visit();
        visited.add(startingNode);
        int[] nextNode = pickNeighbour(maze, startingNode);
        
        if(nextNode == null){
            if(visited.isEmpty()){
                return;
            }
            walkthrough(maze, visited.pop(), visited);
        }
        else{
            if(nextNode[0] > startingNode[0]){ //on right
                maze[startingNode[0]][startingNode[1]].openRight();
            }
            else if(nextNode[0] < startingNode[0]){ //next node on left
                maze[nextNode[0]][nextNode[1]].openRight();
            }
            else if(nextNode[1] > startingNode[1]){ //next node down
                maze[startingNode[0]][startingNode[1]].openDown();
            }
            else{ //next node above
                maze[nextNode[0]][nextNode[1]].openDown();
            }
    
            printMaze(maze);
    
            walkthrough(maze, nextNode, visited);
        }

        //if you reach here, maze complete

    }

    public static void printMaze(Node[][] maze){
        for(int i=0; i<maxRow; i++){
            for(int j=0; i<maxColumn; i++){
                System.out.print(maze[i][j]);
            }
            System.out.println("");
        }

        System.out.println("\n\n\n");
    }

    public static int[] pickNeighbour(Node[][] maze, int[] startingNode){

        ArrayList<int[]> unvisited = new ArrayList<>();

        if((startingNode[0]+1)<maxRow){ //check right
            if(!maze[startingNode[0]+1][startingNode[1]].isVisited()){
                int[] x = {startingNode[0]+1, startingNode[1]};
                unvisited.add(x);
            }
        }
        if((startingNode[1]+1)<maxColumn){ //check down
            if(!maze[startingNode[0]][startingNode[1]+1].isVisited()){
                int[] x = {startingNode[0], startingNode[1]+1};
                unvisited.add(x);
            }
        }
        if((startingNode[0]-1)>=0){ //check left
            if(!maze[startingNode[0]-1][startingNode[1]].isVisited()){
                int[] x = {startingNode[0]-1, startingNode[1]};
                unvisited.add(x);
            }
        }
        if((startingNode[1]-1)>=0){ //check up
            if(!maze[startingNode[0]][startingNode[1]-1].isVisited()){
                int[] x = {startingNode[0], startingNode[1]-1};
                unvisited.add(x);
            }
        }
        if(unvisited.isEmpty()){
            return null;
        }
        int next = rnd.nextInt(unvisited.size());

        return unvisited.get(next);
    }

}




// 0 = unvisited
// 