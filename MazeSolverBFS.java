import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

//For BFS, a queue was used to load neighbours of the current node into, following the FIFO structure of a Breadth First Search. (seen in traverse())
//To track the path taken, I implemented a HashMap, holding the current node and it's parent node. This was so that during the getPath() function, which is only accessed
//once the entire tree has been traversed, with a repeated HashMap.get(currentNode), we can 'backtrack' from the finishNode to the startNode. A HashMap was selected
//due to its O(1) access times for any variable inside it, so no time would be wasted searching through a List-type structure every time a neighbour is checked. If I
//had not used a HashMap, the program could have become significantly slower, as most other lists-type structures have a access time of O(n).

public class MazeSolverBFS {
    
    static int maxRow;
    static int maxColumn;
    static Integer finishNode;
    static int finishNodeNum;
    static int[] cell_openness_list;

    public static void main(String[] args){

        // fetch maze

        String mazedat = readFromFile("maze.dat");
        String[] mazeInfo = mazedat.split(":");
        String[] rowColInfo = mazeInfo[0].split(",");
        maxRow = Integer.parseInt(rowColInfo[0]);
        maxColumn = Integer.parseInt(rowColInfo[1]);
        System.out.println("maxRow = " + maxRow);
        System.out.println("maxCol = " + maxColumn);

        int stepsSolution = 0;
        int stepsSearch = 0;

        int startingNode = Integer.parseInt(mazeInfo[1]);
        finishNode = Integer.parseInt(mazeInfo[2]);
        System.out.println("starting node at start of program: " + Integer.parseInt(mazeInfo[1]));
        System.out.println("starting node at start of program: " + startingNode);

        // 2d array used for visited as it is created and modified in constant time.
        HashMap<Integer, Integer> visited = new HashMap<>(); //<currentNode, previousNode>

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
        Queue<Integer> queue = new LinkedList<>();
        

        long startTime = System.nanoTime();
        traverse(visited, startingNode, maze, stepsSearch, queue);
        String path = getPath(visited, startingNode, finishNode, stepsSolution);
        long endTime = System.nanoTime();

        System.out.println(path);

        double duration = (double) (endTime - startTime)/1000000;
        
        System.out.println( "Number of steps in solution: " + (stepsSolution));
        
        System.out.println( "Number of steps during search: " + stepsSearch);

        System.out.println( "Time taken for search: " + duration + " milliseconds.");

        //printMazeNodes(maze, startingNode, finishNode);


        // if(maxColumn < 11 && maxRow < 11){
        //     //printSolution(pathLL);
        // }
        printSolution(getPath(visited, startingNode, finishNode, stepsSolution), startingNode);


    }

    private static void printMazeNodes(int[][] maze, Integer start, Integer fin){
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

    private static void traverse(HashMap<Integer, Integer> visited, Integer startingNode, int[][] maze, int count, Queue<Integer> queue){
        queue.offer(startingNode);
        int current = startingNode;
        int previous = current;
        visited.put(current, previous);
        while(!queue.isEmpty()){
            count++;
            previous = current;
            current = queue.poll();
            ArrayList<Integer> neighbours = getNeighbours(maze, current, visited);
            for(int i=0; i<neighbours.size(); i++){
                System.out.println("queue 1 = " + queue.toString());
                if(!visited.containsKey(neighbours.get(i))){
                    queue.add(neighbours.get(i));
                    visited.put(neighbours.get(i), current);
                }
            }
        }
    }

    private static String getPath(HashMap<Integer, Integer> visited, Integer startingNode, Integer targetNode, int count){
        String path = "";
        int currentNode = targetNode;
        Stack<Integer> stack = new Stack<>();
        stack.add(currentNode);
        System.out.println("Starting node = " + startingNode);
        System.out.println("targetNode = " + targetNode);
        while(currentNode != startingNode){
            currentNode = visited.get(currentNode);
            count++;
            
            stack.add(currentNode);
        }
        while(!stack.isEmpty()){
            path += stack.pop() + ",";
        }
        return path;
    }

    private static void printList(ArrayList<Integer> list){
        for(int i=0; i<list.size(); i++){
            System.out.print("[" + getNodeFromNum(list.get(i))[0]  + "," + getNodeFromNum(list.get(i))[1] + "]");
        }
        System.out.println();
    }

    private static ArrayList<Integer> getNeighbours(int[][] maze, Integer startingNode, HashMap<Integer, Integer> map){
        ArrayList<Integer> neighbours = new ArrayList<>();
        int[] coords = getNodeFromNum(startingNode);
        if(maze[coords[0]][coords[1]] == 3){ //both open
            
            Integer n1 = startingNode+1;
            Integer n2 = startingNode+maxColumn;
            //visited check
            if(getNodeFromNum(n1)[0] < maxRow && getNodeFromNum(n1)[1] < maxColumn){
                if(!map.containsKey(n1)){
                    neighbours.add(n1);
                }
            }
            if(getNodeFromNum(n2)[0] < maxRow && getNodeFromNum(n2)[1] < maxColumn){
                if(!map.containsKey(n2)){
                    neighbours.add(n2);
                }
            }
            
        }
        else if(maze[coords[0]][coords[1]] == 2){ //down only open
            
            Integer n = startingNode+maxColumn;
            if(getNodeFromNum(n)[0] < maxRow){
                if(!map.containsKey(n)){
                    neighbours.add(n);
                }
            }
            
        }
        else if(maze[coords[0]][coords[1]] == 1){ //right only open
            Integer n = startingNode+1;
            if(getNodeFromNum(n)[0] < maxColumn && getNodeFromNum(n)[1] < maxColumn){
                if(!map.containsKey(n)){
                    neighbours.add(n);
                }
            }
        }

        if(coords[0]-1 >= 0){ //check up
            if((maze[coords[0]-1][coords[1]] == 2 || maze[coords[0]-1][coords[1]] == 3)){
                Integer n = startingNode-maxColumn;
                if(!map.containsKey(n)){
                    neighbours.add(n);
                }
            }
        }

        if(coords[1]-1 >= 0){ //check left
            if((maze[coords[0]][coords[1]-1] == 1 || maze[coords[0]][coords[1]-1] == 3)){
                Integer n = startingNode-1;
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
    private static int[] getNodeFromNum(int num){
        int row = (int) (Math.floor(num)-1) / maxColumn;
        int col = (num-1) % maxColumn; 

        int[] node = {row, col};
        return node;
    }

    // returns the Nodes ID, where n_0, m_0 = 1, n_0, m_1 = 2,...
    // note it is not 0 indexed.
    private static int getNumFromNode(int[] node){
        return (maxColumn) * node[0] + node[1] + 1;
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

    public static void printMaze(int[][] maze, Integer start, Integer finish){
        
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
                if (i == getNodeFromNum(start)[0] && j == getNodeFromNum(start)[1]){
                    System.out.print("S ");
                } else if (i == getNodeFromNum(finish)[0] && j == getNodeFromNum(finish)[1]){
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

    private static void printSolution(String solution, int startingNode){

        LinkedList<Integer> soluPath = new LinkedList<>();
        for(int i=0; i<solution.split(",").length; i++){
            soluPath.add( Integer.parseInt(solution.split(",")[i]));
        }


        // print top bar
        System.out.print("-");
        for(int i = 0; i < maxColumn; i++){
            System.out.print("---");
        }
        System.out.println();

        // iterative step
        for(int i=0; i<maxRow; i++){

            // print left bar
            System.out.print("|");
            

            for(int j=0; j<maxColumn; j++){ // check right loop and fill current space
                int[] currNode = {i, j};

                // check start/finish flags and fill space
                if (i == getNodeFromNum(startingNode)[0] && j == getNodeFromNum(startingNode)[1]){
                    System.out.print("S ");
                } else if (i == getNodeFromNum(finishNode)[0] && j == getNodeFromNum(finishNode)[1]){
                    System.out.print("F ");
                } else if ( soluPath.contains(getNumFromNode(currNode))){
                    System.out.print("* ");
                } else {
                    System.out.print("  ");
                }

                if ( cell_openness_list[getNumFromNode(currNode)-1] == 0 || cell_openness_list[getNumFromNode(currNode)-1] == 2) {
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
                int[] currNode = {i, j};
                System.out.print("|");
                if ( cell_openness_list[getNumFromNode(currNode)-1] == 0 || cell_openness_list[getNumFromNode(currNode)-1] == 1){ // down is closed
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
    }
}


