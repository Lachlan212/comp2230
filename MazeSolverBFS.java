

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MazeSolverBFS {
    
    static int maxRow;
    static int maxColumn;
    static ArrayList<Integer> startingNode;
    static ArrayList<Integer> finishNode;
    static int finishNodeNum;
    static int[][] visited;
    static int[] cell_openness_list;
    static int stepsCounter;

    public static void main(String[] args){

        // fetch maze

        String mazedat = readFromFile("maze.dat");
        String[] mazeInfo = mazedat.split(":");
        String[] rowColInfo = mazeInfo[0].split(",");
        maxRow = Integer.parseInt(rowColInfo[0]);
        maxColumn = Integer.parseInt(rowColInfo[1]);
        System.out.println("maxRow = " + maxRow);
        System.out.println("maxCol = " + maxColumn);

        startingNode = getNodeFromNum(Integer.parseInt(mazeInfo[1]));
        finishNode = getNodeFromNum(Integer.parseInt(mazeInfo[2]));
        finishNodeNum = Integer.parseInt(mazeInfo[2]);
        System.out.println("starting node at start of program: " + Integer.parseInt(mazeInfo[1]));
        System.out.println("starting node at start of program: " + startingNode);

        // 2d array used for visited as it is created and modified in constant time.
        HashMap<ArrayList<Integer>, ArrayList<Integer>> visited = new HashMap<>(); //<currentNode, previousNode>

        cell_openness_list = new int[maxColumn * maxRow];
        for (int i = 0; i < mazeInfo[3].length(); i++){
            cell_openness_list[i] = Character.getNumericValue(mazeInfo[3].charAt(i));
        }
        int[][] maze = new int[maxRow][maxColumn];
        //generateMaze(cell_openness_list, maze);
        int count = 0;
        for(int i=0; i<maze.length; i++){
            for(int j=0; j<maze[i].length; j++){
                maze[i][j] = cell_openness_list[count];
                count++;
            }
        }
        //printMaze(maze, startingNode, finishNode);
        Queue<ArrayList<Integer>> queue = new LinkedList<>();
        int stepsSolution = 0; //count for num of steps in solution
        int stepsSearch = 0;

        long startTime = System.nanoTime();
        traverse(visited, startingNode, maze, stepsSearch, queue);
        String path = getPath(visited, startingNode, finishNode, stepsSolution);
        long endTime = System.nanoTime();

        System.out.println(path);

        double duration = (double) (endTime - startTime)/1000000;
        
        System.out.println( "Number of steps in solution: " + (stepsSolution));
        
        System.out.println( "Number of steps during search: " + stepsSearch);

        System.out.println( "Time taken for search: " + duration + " milliseconds.");

        printMazeNodes(maze, startingNode, finishNode);

        // if(maxColumn < 11 && maxRow < 11){
        //     //printSolution(pathLL);
        // }
        // //printSolution(pathLL);


    }

    private static void printMazeNodes(int[][] maze, ArrayList<Integer> start, ArrayList<Integer> fin){
        for(int i=0; i<maze.length; i++){
            for(int j=0; j<maze[i].length; j++){
                System.out.print("[" + i + "," + j + "]");
            }
            System.out.println("");
        }
        for(int i=0; i<maze.length; i++){
            for(int j=0; j<maze[i].length; j++){
                System.out.print("[" + maze[i][j] + "]");
            }
            System.out.println("");
        }
        printMaze(maze, start, fin);
    }

    private static void traverse(HashMap<ArrayList<Integer>, ArrayList<Integer>> visited, ArrayList<Integer> startingNode, int[][] maze, int count, Queue<ArrayList<Integer>> queue){
        queue.offer(startingNode);
        ArrayList<Integer> current = startingNode;
        ArrayList<Integer> previous = current;
        visited.put(current, previous);
        while(!queue.isEmpty()){
            count++;
            previous = current;
            current = queue.poll();
            visited.put(current, previous);
            ArrayList<ArrayList<Integer>> neighbours = getNeighbours(maze, current, visited);
            printList(neighbours);
            //queue.addAll(neighbours);
            while(!queue.isEmpty()){
                current = queue.poll();
                visited.put(current, previous);
            }
            System.out.println("queue = " + queue.toString());
            neighbours.clear();
            neighbours = getNeighbours(maze, startingNode, visited);
            queue.addAll(neighbours);
            //System.out.println(queue);
        }
    }

    private static String getPath(HashMap<ArrayList<Integer>, ArrayList<Integer>> visited, ArrayList<Integer> startingNode, ArrayList<Integer> targetNode, int count){
        String path = "";
        ArrayList<Integer> currentNode = targetNode;
        Stack<ArrayList<Integer>> stack = new Stack<>();
        stack.add(currentNode);
        System.out.println("Starting node = " + startingNode);
        System.out.println("targetNode = " + targetNode);
        while(currentNode != startingNode){
            currentNode = visited.get(currentNode);
            count++;
            //path += getNumFromNode(currentNode) + ",";
            //path += currentNode + ",";
            stack.add(currentNode);
        }
        while(!stack.isEmpty()){
            //path += getNumFromNode(stack.pop()) + ",";
            path += stack.pop() + ",";
        }
        return path;
    }

    private static void printList(ArrayList<ArrayList<Integer>> list){
        for(int i=0; i<list.size(); i++){
            System.out.print("[" + list.get(i).get(0) + "," + list.get(i).get(1) + "]");
        }
        System.out.println();
    }

    private static ArrayList<ArrayList<Integer>> getNeighbours(int[][] maze, ArrayList<Integer> startingNode, HashMap<ArrayList<Integer>, ArrayList<Integer>> map){
        ArrayList<ArrayList<Integer>> neighbours = new ArrayList<>();
        System.out.println("startingNode = [" + startingNode.get(0) + "," + startingNode.get(1) + "]");
        if(maze[startingNode.get(0)][startingNode.get(1)] == 3){ //both open
            ArrayList<Integer> n1 = new ArrayList<>(); n1.add(startingNode.get(0)+1); n1.add(startingNode.get(1));
            ArrayList<Integer> n2 = new ArrayList<>(); n2.add(startingNode.get(0)); n2.add(startingNode.get(1)+1);
            //visited check
            if(n1.get(0) < 5 && n1.get(1) < 5){
                if(!map.containsKey(n1)){
                    neighbours.add(n1);
                }
            }
            if(n2.get(0) < 5 && n2.get(1) < 5){
                if(!map.containsKey(n2)){
                    neighbours.add(n2);
                }
            }
            
        }
        else if(maze[startingNode.get(0)][startingNode.get(1)] == 2){ //down only open
            
            ArrayList<Integer> n = new ArrayList<>(); n.add(startingNode.get(0)); n.add(startingNode.get(1)+1);
            if(n.get(0) < 5 && n.get(1) < 5){
                if(!map.containsKey(n)){
                    neighbours.add(n);
                }
            }
            
        }
        else if(maze[startingNode.get(0)][startingNode.get(1)] == 1){ //right only open
            ArrayList<Integer> n = new ArrayList<>(); n.add(startingNode.get(0)+1); n.add(startingNode.get(1));
            if(n.get(0) < 5 && n.get(1) < 5){
                if(!map.containsKey(n)){
                    neighbours.add(n);
                }
            }
        }

        if(startingNode.get(0)-1 >= 0){ //check left
            if((maze[startingNode.get(0)-1][startingNode.get(1)] == 1 || maze[startingNode.get(0)-1][startingNode.get(1)] == 3)){
                ArrayList<Integer> n = new ArrayList<>(); n.add(startingNode.get(0)-1); n.add(startingNode.get(1));
                if(!map.containsKey(n)){
                    neighbours.add(n);
                }
            }
        }

        if(startingNode.get(1)-1 >= 0){ //check up
            if((maze[startingNode.get(0)][startingNode.get(1)-1] == 2 || maze[startingNode.get(0)][startingNode.get(1)-1] == 3)){
                ArrayList<Integer> n = new ArrayList<>(); n.add(startingNode.get(0)); n.add(startingNode.get(1)-1);
                //System.out.println("right check = " + n[0] + "," + n[1]);
                if(!map.containsKey(n)){
                    neighbours.add(n);
                }
            }
        }
        return neighbours;
    }


    // returns the nodes co-ordinate index in the maze using its ID
    // note the ID is not 0 indexed.
    private static ArrayList<Integer> getNodeFromNum(int num){
        int row = (int) (Math.floor(num)-1) / maxRow;
        int col = (num-1) % maxRow; //-1 from this resulted in memory crash

        ArrayList<Integer> node = new ArrayList<>();
        node.add(row);
        node.add(col);
        return node;
    }

    // returns the Nodes ID, where n_0, m_0 = 1, n_0, m_1 = 2,...
    // note it is not 0 indexed.
    private static int getNumFromNode(ArrayList<Integer> node){
        return (maxColumn) * node.get(0) + node.get(1) + 1;
    }

    public static void generateMaze(int[] cell_openness_list, int[][] maze){
        System.out.println(cell_openness_list.length);
        int count = 0;
        for(int i=0; i<=maxRow; i++){
            for(int j=0; j<=maxColumn; i++){
                System.out.println("j = " + j);
                System.out.println("i = " + i);
                System.out.println("count = " + count);
                System.out.println("list = " + cell_openness_list.length);
                maze[i][j] = cell_openness_list[count];
                count++;
            }
        }
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

    public static void printMaze(int[][] maze, ArrayList<Integer> start, ArrayList<Integer> finish){
        
        // print top bar
        System.out.print("-");
        for(int i = 0; i < maxColumn; i++){
            System.out.print("---");
        }
        System.out.println();
    
        // iterative step
        for(int i=0; i<maxRow; i++){
            // for(int j=0; j<maxColumn; j++){
            //     System.out.print(maze[i][j]);
            // }
    
            // print left bar
            System.out.print("|");
    
            
    
            for(int j=0; j<maxColumn; j++){ // check right loop and fill current space
    
                // check start/finish flags and fill space
                if (i == start.get(0) && j == start.get(1)){
                    System.out.print("S ");
                } else if (i == finish.get(0) && j == finish.get(1)){
                    System.out.print("F ");
                } else {
                    System.out.print("  ");
                }
    
                if ( maze[i][j] == 0 || maze[i][j] == 2){
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }
            }
    
            System.out.println();
    
            if ( i == maxRow - 1){ // last down's p
                break;
            }
    
            for(int j=0; j<maxColumn; j++){ // check down
                System.out.print("|");
                if ( maze[i][j] == 0 || maze[i][j] == 1){ // down is closed
                    System.out.print("--");
                } else {
                    System.out.print("  ");
                }
            }
            System.out.print("|");
    
            System.out.println("");
        }
    
        // print bottom bar
        System.out.print("-");
        for(int i = 0; i < maxColumn; i++){
            System.out.print("---");
        }
        System.out.println();
    
    
        //System.out.println("\n\n\n");
    }
}


