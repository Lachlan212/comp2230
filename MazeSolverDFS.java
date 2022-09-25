

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

public class MazeSolverDFS {

    int[][] maze;
    static int maxRow;
    static int maxColumn;
    static int[] startingNode;
    static int[] finishNode;
    static int finishNodeNum;
    static int[][] visited;
    static int[] cell_openness_list;
    static Stack<Integer> path;
    static int stepCounter;

    public static void main(String[] args){

        // fetch maze
        //String filename = args[0];

        //String mazedata = "5,5:2:12:3123212102302301030211110";
        String mazedata = MazeGenerator.readFromFile("maze.dat");

        String[] mazeInfo = mazedata.split(":");
        String[] rowColInfo = mazeInfo[0].split(",");
        maxRow = Integer.parseInt(rowColInfo[0]);
        maxColumn = Integer.parseInt(rowColInfo[1]);

        startingNode = getNodeFromNum(Integer.parseInt(mazeInfo[1]));
        finishNode = getNodeFromNum(Integer.parseInt(mazeInfo[2]));
        finishNodeNum = Integer.parseInt(mazeInfo[2]);

        // 2d array used for visited as it is created and modified in constant time.
        int[][] visited = new int[maxRow][maxColumn]; // initialises to 0

        cell_openness_list = new int[maxColumn * maxRow];
        for (int i = 0; i < mazeInfo[3].length(); i++){
            cell_openness_list[i] = Character.getNumericValue(mazeInfo[3].charAt(i));
        }


        long startTime = System.nanoTime();
        getPath(visited);
        long endTime = System.nanoTime();

        double duration = (double) (endTime - startTime)/1000000;

        LinkedList<Integer> pathLL = new LinkedList<>(); // efficiency of stack no longer required
        while (path.size() > 0){
            pathLL.add( path.pop());
        }
        System.out.print("Path solution is: (");
        for (int i=0; i < pathLL.size() - 1; i++){
            System.out.print( pathLL.get(i) + "," );
        }
        System.out.println( pathLL.get(pathLL.size() - 1) + ")");
        
        System.out.println( "Number of steps in solution: " + (pathLL.size() - 1));
        
        System.out.println( "Number of steps during search: " + stepCounter);

        System.out.println( "Time taken for search: " + duration + " milliseconds.");

        if(maxColumn < 11 && maxRow < 11){
            printSolution(pathLL);
        }
        //printSolution(pathLL);



    }

    private static void printSolution(LinkedList<Integer> soluPath) {

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
                if (i == startingNode[0] && j == startingNode[1]){
                    System.out.print("S ");
                } else if (i == finishNode[0] && j == finishNode[1]){
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

    private static void getPath(int[][] visited){

        Stack<Integer> stack = new Stack<>(); // stack used for its efficiency - add or removing from stack take constant time. The stack is only added to so this is highly efficient.

        DFS(stack, startingNode, visited);

    }

    private static boolean DFS(Stack<Integer> stack, int[] currentNode, int[][] visited) {

        // visit node
        visited[currentNode[0]][currentNode[1]] = 1;
        stepCounter++;


        // check if node is final node
        if ( currentNode[0] == finishNode[0] && currentNode[1] == finishNode[1]){
            stack.push(getNumFromNode(currentNode));
            
            return true;
        }

        int openness = cell_openness_list[getNumFromNode(currentNode)-1]; // adjust for 0 indexing

        ArrayList<int[]> availableNeighbours = new ArrayList<>();

        // check if neighbouring cells are visited or have a wall in between
        if (openness == 3 || openness == 2){ // check down open
            int[] downNode = {currentNode[0] + 1, currentNode[1]};
            if (visited[downNode[0]][downNode[1]] == 0){ // check down not visited
                availableNeighbours.add(downNode);
            }
        }
        if (openness == 3 || openness == 1) { // check right
            int[] rightNode = {currentNode[0], currentNode[1] + 1};
            if (visited[rightNode[0]][rightNode[1]] == 0){
                availableNeighbours.add(rightNode);
            }
        }

        if((currentNode[0]-1)>=0){ // check up
            int[] upNode = {currentNode[0]-1, currentNode[1]};
            if (cell_openness_list[getNumFromNode(upNode)-1] == 2 || cell_openness_list[getNumFromNode(upNode)-1] == 3) {
                if ( visited[upNode[0]][upNode[1]] == 0){
                    availableNeighbours.add(upNode);
                }
            }
        }

        if((currentNode[1]-1)>=0){ // check left
            int[] leftNode = {currentNode[0], currentNode[1]-1};
            if (cell_openness_list[getNumFromNode(leftNode)-1] == 1 || cell_openness_list[getNumFromNode(leftNode)-1] == 3) {
                if ( visited[leftNode[0]][leftNode[1]] == 0){
                    availableNeighbours.add(leftNode);
                }
            }
        }

        if(availableNeighbours.isEmpty()){
            return false;
        }

        // Sort Neighbours by shortest distance to finish node 
        for (int i = 0; i < availableNeighbours.size()-1; i++){
            double currentDistance = Math.sqrt(Math.pow( (availableNeighbours.get(i)[0] + finishNode[0]), 2) + Math.pow( (availableNeighbours.get(i)[1] + finishNode[1]), 2 ) );
            double nextDistance = Math.sqrt(Math.pow( (availableNeighbours.get(i+1)[0] + finishNode[0]), 2) + Math.pow( (availableNeighbours.get(i+1)[1] + finishNode[1]), 2 ) );

            if ( nextDistance < currentDistance ){ // swap nodes
                Collections.swap(availableNeighbours, i, i+1);

                // reverse loop
                for (int j=i; j>1; j++){ // note nextDistance is stored and can be used to compare against previous values
                    double previousDistance = Math.sqrt(Math.pow( (availableNeighbours.get(j-1)[0] + finishNode[0]), 2) + Math.pow( (availableNeighbours.get(j-1)[1] + finishNode[1]), 2 ) ); 
                    if ( nextDistance < previousDistance){ // swap
                        Collections.swap(availableNeighbours, j, j-1);
                    }

                }
            }
        }

        // recursive DFS step
        for ( int i =0; i < availableNeighbours.size(); i++ ){
            
            boolean cellOnPath = DFS(stack, availableNeighbours.get(i), visited);
            if (cellOnPath){
                stack.push(getNumFromNode(currentNode));
                path = stack;
                return true;
            }
        }
        return false;
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
    
}

