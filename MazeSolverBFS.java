

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class MazeSolverBFS {
    static int[][] maze;
    static int maxRow;
    static int maxColumn;
    static int[] startingNode;
    static int[] finishNode;
    static int finishNodeNum;
    static int[][] visited;
    static int[] cell_openness_list;
    static int stepsCounter;
    private static Queue<int[]> queue = new LinkedList<>();

    public static void main(String[] args){

        // fetch maze

        String mazedat = readFromFile("maze.dat");
        String[] mazeInfo = mazedat.split(":");
        String[] rowColInfo = mazeInfo[0].split(",");
        maxRow = Integer.parseInt(rowColInfo[0]);
        maxColumn = Integer.parseInt(rowColInfo[1]);

        startingNode = getNodeFromNum(Integer.parseInt(mazeInfo[1]));
        finishNode = getNodeFromNum(Integer.parseInt(mazeInfo[2]));
        finishNodeNum = Integer.parseInt(mazeInfo[2]);

        // 2d array used for visited as it is created and modified in constant time.
        HashMap<int[], int[]> visited = new HashMap<>(); //<currentNode, previousNode>

        cell_openness_list = new int[maxColumn * maxRow];
        for (int i = 0; i < mazeInfo[3].length(); i++){
            cell_openness_list[i] = Character.getNumericValue(mazeInfo[3].charAt(i));
        }

        int stepsSolution = 0; //count for num of steps in solution
        int stepsSearch = 0;

        long startTime = System.nanoTime();
        traverse(visited, startingNode, maze, stepsSearch);
        String path = getPath(visited, startingNode, finishNode, stepsSolution);
        long endTime = System.nanoTime();

        System.out.println(path);

        double duration = (double) (endTime - startTime)/1000000;
        
        System.out.println( "Number of steps in solution: " + (stepsSolution));
        
        System.out.println( "Number of steps during search: " + stepsSearch);

        System.out.println( "Time taken for search: " + duration + " milliseconds.");

        // if(maxColumn < 11 && maxRow < 11){
        //     //printSolution(pathLL);
        // }
        // //printSolution(pathLL);


    }

    private static void traverse(HashMap<int[], int[]> visited, int[] startingNode, int[][] maze, int count){
        queue.add(startingNode);
        int[] current = startingNode;
        while(!queue.isEmpty()){
            count++;
            int[] previous = current;
            current = queue.poll();
            visited.put(current, previous);

            queue.addAll(getNeighbours(maze, startingNode, visited));
        }
    }

    private static String getPath(HashMap<int[], int[]> visited, int[] startingNode, int[] targetNode, int count){
        String path = "";
        int[] currentNode = targetNode;
        while(currentNode != startingNode){
            count++;
            path += getNumFromNode(currentNode) + ",";
        }
        return path;
    }

    private static ArrayList<int[]> getNeighbours(int[][] maze, int[] startingNode, HashMap<int[], int[]> map){
        ArrayList<int[]> neighbours = new ArrayList<>();
        if(maze[startingNode[0]][startingNode[1]] == 3){ //both open
            int[] n1 = {startingNode[0]+1, startingNode[1]};
            int[] n2 = {startingNode[0], startingNode[1]+1};
            //visited check
            if(!map.get(n1).equals(null)){
                neighbours.add(n1);
            }
            if(!map.get(n2).equals(null)){
                neighbours.add(n2);
            }
        }
        else if(maze[startingNode[0]][startingNode[1]] == 2){ //down only open
            int[] n = {startingNode[0]+1, startingNode[1]};
            if(!map.get(n).equals(null)){
                neighbours.add(n);
            }
        }
        else if(maze[startingNode[0]][startingNode[1]] == 1){ //right only open
            int[] n = {startingNode[0], startingNode[1]+1};
            if(!map.get(n).equals(null)){
                neighbours.add(n);
            }
        }

        if(startingNode[0]-1 >= 0){ //check left
            if((maze[startingNode[0]-1][startingNode[1]] == 1 || maze[startingNode[0]-1][startingNode[1]] == 3)){
                int[] n = {startingNode[0]-1, startingNode[1]};
                if(!map.get(n).equals(null)){
                    neighbours.add(n);
                }
            }
        }

        if(startingNode[1]-1 >= 0){ //check up
            if((maze[startingNode[0]][startingNode[1]-1] == 2 || maze[startingNode[0]][startingNode[1]-1] == 3)){
                int[] n = {startingNode[0], startingNode[1]-1};
                if(!map.get(n).equals(null)){
                    neighbours.add(n);
                }
            }
        }
        return neighbours;
    }


    // returns the nodes co-ordinate index in the maze using its ID
    // note the ID is not 0 indexed.
    private static int[] getNodeFromNum(int num){
        int row = (int) (Math.floor(num)-1) / maxRow;
        int col = (num - 1) % maxRow; 
        int[] node = {row, col};
        return node;
    }

    // returns the Nodes ID, where n_0, m_0 = 1, n_0, m_1 = 2,...
    // note it is not 0 indexed.
    private static int getNumFromNode(int[] node){
        return (maxColumn) * node[0] + node[1] + 1;
    }

    public static int[][] generateMaze(String cell_openess_list){
        int[][] maze = new int[maxRow][maxColumn];

        return maze;
    }

    public static String readFromFile(String filename){
        try {
            FileInputStream fis = new FileInputStream("maze.dat");
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            mazeState m = (mazeState)ois.readObject();
            //System.out.println(m);
            ois.close();

            return m.getMazeState();

        } catch (IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return "";
    }
}
