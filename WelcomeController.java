package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.*;
// AI --->-1
//USER ---->1
// TIE ---->0
public class WelcomeController implements Initializable {

    public ArrayList<TreeNode> FinalNodes=new ArrayList<>();
    String turn="";
    String whichOneToStart="";
    int whichOneHasStarted=0;  //--->1 user
                                //--->0 AI
    boolean AIWOn=false;
    char userColor='w';
    static final int BOARD_SIZE = 8;
    static final int SQUARE_SIZE = 80;
    static final int [][] indexes=new int[8][8];
    static final int SMALL_SQUARE_SIZE = 20;
    static final Color SMALL_SQUARE_COLOR =Color.HOTPINK;
    final int[] isSmallSquareDark = new int[1];

    public static String WINNER="NO ONE";

    static final char[][] color=new char[8][8];
    //__________________> w=white
    //__________________> r=red

    // Track the occupied cells on the board
    public static Set<Cell> occupiedCells;
    @FXML
    private Button firstButton;
    @FXML
    private Button secondButton;

    @FXML
    private Button AIStartsButton;

    @FXML
    private Button youStartButton;
    //Button homeButton = new Button("Home");

    int AIRow=0;
    int AIColumn=0;
    public Stage primaryStage;
    public Parent root;
    int choice=0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public GridPane createGameBoard() {
        GridPane board = new GridPane();
        boolean isDark = false;
        occupiedCells = new HashSet<>();

        for (int row = 0; row < BOARD_SIZE; row++) {

            isDark = !isDark; // Alternate the color of each row
            for (int col = 0; col < BOARD_SIZE; col++) {

                Rectangle square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                square.setFill(isDark ? Color.DARKGRAY : Color.LIGHTGRAY);

                board.add(square, col, row);
                isDark = !isDark; // Alternate the color of each square

                // Add click event handler to each square
                final int rowIndex = row;
                final int colIndex = col;

                if(whichOneToStart.equalsIgnoreCase("AI")) {
                    callAI(board);
                }


                square.setOnMouseClicked(event -> {
                    if(occupiedCells.size()==63){
                        Stage primaryStage=(Stage) ((Node) event.getSource()).getScene().getWindow();
                        showResult(primaryStage);
                    }
                    if(indexes[rowIndex][colIndex]==1){ // IF THE PLACE ALREADY TAKEN
                        System.out.println("Cannot place brick on block: " + rowIndex + ", " + colIndex);
                    }

                    else if (canPlaceBrick(rowIndex, colIndex)) {// CHECK IF THE PLACE IS LEGAL
                        turn="AI";
                        System.out.println("Placed brick on block: " + rowIndex + ", " + colIndex);
                        Rectangle smallSquare = new Rectangle(SMALL_SQUARE_SIZE, SMALL_SQUARE_SIZE, SMALL_SQUARE_COLOR);
                        addSmallSquare(smallSquare,board, rowIndex, colIndex);

                        char tmp;
                        if(isSmallSquareDark[0] ==0){ //if the placed square is white
                            color[rowIndex][colIndex]='w';
                            smallSquare.setFill(Color.LAVENDER);//inverse the square color
                            isSmallSquareDark[0] =1;
                        }
                        else{
                            color[rowIndex][colIndex]='r';
                            smallSquare.setFill(Color.MAROON);
                            isSmallSquareDark[0] =0;
                        }

                        occupiedCells.add(new Cell(rowIndex, colIndex));
                        indexes[rowIndex][colIndex]=1; //RESERVE INDEX IN THE ARRAy

                        if (checkWin(rowIndex, colIndex,color[rowIndex][colIndex])) {
                            showFinalColors();
                            System.out.print("Congratulations!");
                            if(isSmallSquareDark[0]==0) {
                                System.out.println("RED WON");
                                WINNER="RED";
                                Stage primaryStage=(Stage) ((Node) event.getSource()).getScene().getWindow();
                                showResult(primaryStage);

                            }

                            else {
                                WINNER="WHITE";
                                System.out.println("WHITE WON");
                                Stage primaryStage=(Stage) ((Node) event.getSource()).getScene().getWindow();
                                showResult(primaryStage);
                            }

                        }

                    }

                    else {
                        System.out.println("Cannot place brick on block: " + rowIndex + ", " + colIndex);
                    }
                    if(choice!=1){//IF THE USER WANT'S TO PLAY AGAINST ANOTHER USER
                        //System.out.println("hihihih");
                         callAI(board);
                    }



                });
            }
        }



        return board;
    }


    public void AITurn(){
        // I MUST FIGURE ALL OPTIONS AVAILABLE FOR AI AFTER THE USER HAS PLAYED:
        // MAKE COPY OF THE CURRENT BOARD OF COLORS:
        char[][] colorsCopy = new char[8][8];
        int[][] indexesCopy=new int[8][8];

        for (int i = 0; i < color.length; i++) {
            System.arraycopy(color[i], 0, colorsCopy[i], 0, color[i].length);
        }//make copy of the original color's board
        for (int i = 0; i < indexes.length; i++) {
            System.arraycopy(indexes[i], 0, indexesCopy[i], 0, indexes[i].length);
        }//make copy of the original integer's board


        //now let's try to make the tree:
        TreeNode root=new TreeNode();
        root.setColors(colorsCopy);
        root.setIndexes(indexesCopy);
        Tree tree=new Tree(root);

        char currentColor='z';

        if(whichOneHasStarted==0) //if the started one is AI, then the AI must be white
          currentColor ='w';
        else if(whichOneHasStarted==1)
            currentColor='r';//if the started one is user, then the AI must be red

        boolean check=false;

        //NOW LET'S FIND LEGAL PLACES FOR THE AI
        //int numberOfAvailablePlaces=0;
        boolean tmp=false; // to check if i can place brick in such square
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(indexes[i][j]==0) { //IF NOTHING PLACED ON THE CELL, CHECK IF WE CAN PLACE BRICK THERE
                    tmp = canPlaceBrick(i, j);
                    if(tmp) {
                        TreeNode newNode=new TreeNode();//create node
                        newNode.setColors(colorsCopy,i,j,currentColor);//set the color for in the place of this brick
                        newNode.setIndexes(indexesCopy,i,j);//set 1 at the place of this brick
                        newNode.setLastRowPlaced(i);
                        newNode.setLastColumnPlaced(j);
                        //add the node to the tree
                        tree.insert(newNode,root);

                        //CHECK IF THIS IS A WINNING MOVE
                       check= checkWin2(i,j,currentColor,newNode.getIndexes(), newNode.getColors());
                       if(check) {//IF THIS AI MOVE IS A WINNING MOVE, DON'T GENERATE IT'S CHILDES
                           newNode.setGenerateChildrens(false);
                           newNode.setValue(-5);//AI HAS WON
                           AIRow=i;
                           AIColumn=j;
                            AIWOn=true;
                           //break;
                       }
                    }//make copy of the original integer's board
                }
            }
            tmp=false;

        }


            if(currentColor=='w')
                userColor='r';
            else if(currentColor=='r')
                userColor='w';
        // NOW I NEED TO GUESS THE NEXT MOVE OF THE USER
        for(int m=0;m<root.getChildren().size();m++){
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                       if(root.getChildren().get(m).isGenerateChildrens()) { // IF THIS MOVE BY THE AI IS NOT WINNING, GENERATE IT'S CHILDES
                           if(root.getChildren().get(m).getIndexes(i,j)==0) { //IF NOTHING PLACED ON THE CELL, CHECK IF WE CAN PLACE BRICK THERE
                               tmp = canPlaceBrick2(i, j,root.getChildren().get(m).getIndexes());
                               if(tmp) {
                                   TreeNode newNode=new TreeNode();//create node
                                   newNode.setColors(root.getChildren().get(m).getColors(),i,j,userColor);//set the color for  the place of this brick
                                   newNode.setIndexes(root.getChildren().get(m).getIndexes(),i,j);//set 1 at the place of this brick
                                   //add the node to the tree
                                   tree.insert(newNode,root.getChildren().get(m));
                                   newNode.setLastRowPlaced(i);
                                   newNode.setLastColumnPlaced(j);

                               }//make copy of the original integer's board
                           }
                       }
                    tmp=false;
                }


            }
        }

        //NOW I SHOULD GIVE VALUES TO THE CHILDES OF CHILDES, BY CHECKING IF THE USER WON!!
        int check2=0;
        for(int i=0;i<root.getChildren().size();i++)
        {
            for (int j=0;j<root.getChildren().get(i).getChildren().size();j++)
            {
                check2=checkWin3(root.getChildren().get(i).getChildren().get(j).getLastRowPlaced(),
                        root.getChildren().get(i).getChildren().get(j).getLastColumnPlaced(),userColor,
                        root.getChildren().get(i).getChildren().get(j).getIndexes(), root.getChildren().get(i).getChildren().get(j).getColors());

                if(check2==1)
                    root.getChildren().get(i).getChildren().get(j).setValue(10); //IF IT'S WINNING MOVE
                else {
                        countSquares(root.getChildren().get(i).getChildren().get(j),root.getChildren().get(i).getChildren().get(j).getLastRowPlaced(),
                            root.getChildren().get(i).getChildren().get(j).getLastColumnPlaced(),userColor,
                            root.getChildren().get(i).getChildren().get(j).getIndexes(), root.getChildren().get(i).getChildren().get(j).getColors());

                        int value=findMaximum(root.getChildren().get(i).getChildren().get(j).getCountH(),
                                root.getChildren().get(i).getChildren().get(j).getCountV(),root.getChildren().get(i).getChildren().get(j).getCountD());
                        root.getChildren().get(i).getChildren().get(j).setValue(value);


                }
            }
        }


        //NOW I NEED TO APPLY THE MINIMAX ALG
        int AIEvaluation=0;
       // if(whichOneHasStarted==0) {//if the AI STARTED THEN IT'S MAXIMIZING
            //AIEvaluation = miniMax(root, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
       // }
       // else if(whichOneHasStarted==1){
            AIEvaluation = miniMax(root, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

       // }
int count=0;
        //NOW I NEED TO SEARCH FOR THE {i,j} FOR THE AI
        for(int i=0;i<root.getChildren().size();i++){
            if(root.getChildren().get(i).getValue()==AIEvaluation){
                count++;
                root.getChildren().get(i).setCount(specialCountSquares(root.getChildren().get(i),root.getChildren().get(i).getLastRowPlaced(),
                        root.getChildren().get(i).getLastColumnPlaced(),currentColor,
                        root.getChildren().get(i).getIndexes(),root.getChildren().get(i).getColors()));

                root.getChildren().get(i).setCount2(specialCountSquares2(root.getChildren().get(i),root.getChildren().get(i).getLastRowPlaced(),
                        root.getChildren().get(i).getLastColumnPlaced(),currentColor,
                        root.getChildren().get(i).getIndexes(),root.getChildren().get(i).getColors()));

            }
        }

        for(int i=0;i<root.getChildren().size();i++){
            for(int j=0;j<8;j++){
                for(int k=0;k<8;k++){
                    System.out.print(root.getChildren().get(i).getColors(j,k));
                }
                System.out.println();
            }
            System.out.println(root.getChildren().get(i).getCount2()+"{"+root.getChildren().get(i).getLastRowPlaced()+","+root.getChildren().get(i).getLastColumnPlaced()+"}");
            System.out.println("Value= "+root.getChildren().get(i).getValue());
            System.out.println("Count1= "+root.getChildren().get(i).getCount());
            System.out.println();
            System.out.println();
        }

        int id=0;
        int max=0;
if(count==root.getChildren().size()) {
    for (int i = 0; i < root.getChildren().size(); i++) {
        if (max < root.getChildren().get(i).getCount()) {
            max = root.getChildren().get(i).getCount();
            id = i;
        }
    }


    int id2 = 0;
    int max2 = 0;

    for (int i = 0; i < root.getChildren().size(); i++) {
        if (max2 < root.getChildren().get(i).getCount2()) {
            max2 = root.getChildren().get(i).getCount2();
            id2 = i;
        }
    }

    if (AIWOn == true) {

    } else if (max >= max2) {
        AIRow = root.getChildren().get(id).getLastRowPlaced();
        AIColumn = root.getChildren().get(id).getLastColumnPlaced();
    } else {
        AIRow = root.getChildren().get(id2).getLastRowPlaced();
        AIColumn = root.getChildren().get(id2).getLastColumnPlaced();
    }
}
else{
    for(int i=0;i<root.getChildren().size();i++){
        if(root.getChildren().get(i).getValue()==AIEvaluation){
            AIRow=root.getChildren().get(i).getLastRowPlaced();
            AIColumn=root.getChildren().get(i).getLastColumnPlaced();
            break;
        }
    }
}

        AIWOn=false;

    }
    public static int findMaximum(int num1, int num2, int num3) {
        int max = num1;

        if (num2 > max) {
            max = num2;
        }

        if (num3 > max) {
            max = num3;
        }

        return max;
    }

    private int miniMax(TreeNode node,int depth,int alpha, int beta,boolean isMaximizingPlayer ){
        if(depth==0||node.getChildren().isEmpty())
            return node.getValue();

        if (isMaximizingPlayer){
                int maxEval=Integer.MIN_VALUE;
                int eval=0;
                for(TreeNode child: node.getChildren()) {
                    eval = miniMax(child, depth - 1, alpha, beta, false);
                    maxEval = Integer.max(maxEval, eval);
                    child.setValue(maxEval);
                    alpha = Integer.max(alpha, eval);
                    if (beta <= alpha)
                        break;
                }
                return maxEval;

        }

        else{
                int minEval=Integer.MAX_VALUE;
                int eval=0;
                for(TreeNode child:node.getChildren()){
                    eval=miniMax(child,depth-1,alpha,beta,true);
                    minEval=Integer.min(minEval,eval);
                    child.setValue(minEval);
                    beta=Integer.min(beta,eval);
                    if(beta<=alpha)
                        break;
                }
                return minEval;
        }
    }

    private boolean canPlaceBrick(int rowIndex, int colIndex) {
        if (colIndex == 0 || colIndex == BOARD_SIZE - 1) {
            // Brick can be placed directly on the left or right wall
            return true;
        }

        Cell leftCell = new Cell(rowIndex, colIndex - 1);
        Cell rightCell = new Cell(rowIndex, colIndex + 1);

        // Brick can be placed to the left or right of another brick
        return occupiedCells.contains(leftCell) || occupiedCells.contains(rightCell);
    }

    private boolean canPlaceBrick2(int rowIndex,int colIndex,int [][] indexes){
        if (colIndex == 0 || colIndex == BOARD_SIZE - 1) {
            // Brick can be placed directly on the left or right wall
            return true;
        }
        if(indexes[rowIndex][colIndex-1]==1||indexes[rowIndex][colIndex+1]==1)
            return true;
        return false;
    }

    private int specialCountSquares2(TreeNode node,int rowIndex, int colIndex,char colorOfSmallSquare,int [][] indexes,char [][] color){
        int targetValue = indexes[rowIndex][colIndex];
        int count=0;

        // Check horizontally
        int left = colIndex - 1;
        while (left >= 0 && indexes[rowIndex][left] == targetValue) {
            if(color[rowIndex][left]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            left--;

        }

        int right = colIndex + 1;
        while (right < BOARD_SIZE && indexes[rowIndex][right] == targetValue) {
            if(color[rowIndex][right]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            right++;
        }



        // Check vertically
        int up = rowIndex - 1;
        while (up >= 0 && indexes[up][colIndex] == targetValue) {
            if(color[up][colIndex]==colorOfSmallSquare) {
                count++;
            }
            else {
                break;
            }
            up--;
        }

        int down = rowIndex + 1;
        while (down < BOARD_SIZE && indexes[down][colIndex] == targetValue) {
            if(color[down][colIndex]==colorOfSmallSquare) {
                count++;
            }
            else {
                break;
            }
            down++;
        }



        // Check diagonally (top-left to bottom-right)

        int topLeftRow = rowIndex - 1;
        int topLeftCol = colIndex - 1;
        while (topLeftRow >= 0 && topLeftCol >= 0 && indexes[topLeftRow][topLeftCol] == targetValue) {
            if(color[topLeftRow][topLeftCol]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            topLeftRow--;
            topLeftCol--;
        }
        int bottomRightRow = rowIndex + 1;
        int bottomRightCol = colIndex + 1;
        while (bottomRightRow < BOARD_SIZE && bottomRightCol < BOARD_SIZE && indexes[bottomRightRow][bottomRightCol] == targetValue) {
            if(color[bottomRightRow][bottomRightCol]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            bottomRightRow++;
            bottomRightCol++;
        }



        // Check diagonally (top-right to bottom-left)

        int topRightRow = rowIndex - 1;
        int topRightCol = colIndex + 1;
        while (topRightRow >= 0 && topRightCol < BOARD_SIZE && indexes[topRightRow][topRightCol] == targetValue) {
            if(color[topRightRow][topRightCol]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            topRightRow--;
            topRightCol++;
        }

        int bottomLeftRow = rowIndex + 1;
        int bottomLeftCol = colIndex - 1;
        while (bottomLeftRow < BOARD_SIZE && bottomLeftCol >= 0 && indexes[bottomLeftRow][bottomLeftCol] == targetValue) {
            if(color[bottomLeftRow][bottomLeftCol]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            bottomLeftRow++;
            bottomLeftCol--;
        }
        System.out.println("COUNT="+count);
        return  count;

    }
    private int specialCountSquares(TreeNode node,int rowIndex, int colIndex,char colorOfSmallSquare,int [][] indexes,char [][] color){
        int targetValue = indexes[rowIndex][colIndex];
            int count=0;

        // Check horizontally
        int left = colIndex - 1;
        while (left >= 0 && indexes[rowIndex][left] == 1) {
            if(color[rowIndex][left]!=colorOfSmallSquare)
                count++;
            else
                break;
            left--;

        }

        int right = colIndex + 1;
        while (right < BOARD_SIZE && indexes[rowIndex][right] == targetValue) {
            if(color[rowIndex][right]!=colorOfSmallSquare)
                count++;
            else
                break;
            right++;
        }



        // Check vertically
        int up = rowIndex - 1;
        while (up >= 0 && indexes[up][colIndex] == targetValue) {
            if(color[up][colIndex]!=colorOfSmallSquare) {
                count++;
            }
            else {
                break;
            }
            up--;
        }

        int down = rowIndex + 1;
        while (down < BOARD_SIZE && indexes[down][colIndex] == targetValue) {
            if(color[down][colIndex]!=colorOfSmallSquare) {
                count++;
            }
            else {
                break;
            }
            down++;
        }



        // Check diagonally (top-left to bottom-right)

        int topLeftRow = rowIndex - 1;
        int topLeftCol = colIndex - 1;
        while (topLeftRow >= 0 && topLeftCol >= 0 && indexes[topLeftRow][topLeftCol] == targetValue) {
            if(color[topLeftRow][topLeftCol]!=colorOfSmallSquare)
                count++;
            else
                break;
            topLeftRow--;
            topLeftCol--;
        }
        int bottomRightRow = rowIndex + 1;
        int bottomRightCol = colIndex + 1;
        while (bottomRightRow < BOARD_SIZE && bottomRightCol < BOARD_SIZE && indexes[bottomRightRow][bottomRightCol] == targetValue) {
            if(color[bottomRightRow][bottomRightCol]!=colorOfSmallSquare)
                count++;
            else
                break;
            bottomRightRow++;
            bottomRightCol++;
        }



        // Check diagonally (top-right to bottom-left)

        int topRightRow = rowIndex - 1;
        int topRightCol = colIndex + 1;
        while (topRightRow >= 0 && topRightCol < BOARD_SIZE && indexes[topRightRow][topRightCol] == targetValue) {
            if(color[topRightRow][topRightCol]!=colorOfSmallSquare)
                count++;
            else
                break;
            topRightRow--;
            topRightCol++;
        }

        int bottomLeftRow = rowIndex + 1;
        int bottomLeftCol = colIndex - 1;
        while (bottomLeftRow < BOARD_SIZE && bottomLeftCol >= 0 && indexes[bottomLeftRow][bottomLeftCol] == targetValue) {
            if(color[bottomLeftRow][bottomLeftCol]!=colorOfSmallSquare)
                count++;
            else
                break;
            bottomLeftRow++;
            bottomLeftCol--;
        }
            return  count;

    }

    private void addSmallSquare(Rectangle smallSquare,GridPane board, int rowIndex, int colIndex) {

        smallSquare.setStroke(Color.BLACK);
        smallSquare.setStrokeWidth(1);
        smallSquare.setArcWidth(5);
        smallSquare.setArcHeight(5);

        // Center align the small square within the block
        int padding = (SQUARE_SIZE - SMALL_SQUARE_SIZE) / 2;
        GridPane.setRowIndex(smallSquare, rowIndex);
        GridPane.setColumnIndex(smallSquare, colIndex);
        GridPane.setMargin(smallSquare, new javafx.geometry.Insets(padding));
        board.getChildren().add(smallSquare);

    }

    private boolean checkWin(int rowIndex, int colIndex,char colorOfSmallSquare) {
        int targetValue = indexes[rowIndex][colIndex];
        int count = 1;

        // Check horizontally
        int left = colIndex - 1;
        while (left >= 0 && indexes[rowIndex][left] == 1) {
            if(color[rowIndex][left]==colorOfSmallSquare)
                count++;
            else
                break;
            left--;

        }

        int right = colIndex + 1;
        while (right < BOARD_SIZE && indexes[rowIndex][right] == 1) {
            if(color[rowIndex][right]==colorOfSmallSquare)
                count++;
            else
                break;
            right++;
        }

        if (count >= 5) {
            return true;
        }

        // Check vertically
        count = 1;
        int up = rowIndex - 1;
        while (up >= 0 && indexes[up][colIndex] == targetValue) {
            if(color[up][colIndex]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            up--;
        }

        int down = rowIndex + 1;
        while (down < BOARD_SIZE && indexes[down][colIndex] == targetValue) {
            if(color[down][colIndex]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            down++;
        }

        if (count >= 5) {
            return true;
        }

        // Check diagonally (top-left to bottom-right)
        count = 1;
        int topLeftRow = rowIndex - 1;
        int topLeftCol = colIndex - 1;
        while (topLeftRow >= 0 && topLeftCol >= 0 && indexes[topLeftRow][topLeftCol] == targetValue) {
            if(color[topLeftRow][topLeftCol]==colorOfSmallSquare)
                count++;
            else
                break;
            topLeftRow--;
            topLeftCol--;
        }
        int bottomRightRow = rowIndex + 1;
        int bottomRightCol = colIndex + 1;
        while (bottomRightRow < BOARD_SIZE && bottomRightCol < BOARD_SIZE && indexes[bottomRightRow][bottomRightCol] == targetValue) {
            if(color[bottomRightRow][bottomRightCol]==colorOfSmallSquare)
                count++;
            else
                break;
            bottomRightRow++;
            bottomRightCol++;
        }

        if (count >= 5) {
            return true;
        }

        // Check diagonally (top-right to bottom-left)
        count = 1;
        int topRightRow = rowIndex - 1;
        int topRightCol = colIndex + 1;
        while (topRightRow >= 0 && topRightCol < BOARD_SIZE && indexes[topRightRow][topRightCol] == targetValue) {
            if(color[topRightRow][topRightCol]==colorOfSmallSquare)
                count++;
            else
                break;
            topRightRow--;
            topRightCol++;
        }

        int bottomLeftRow = rowIndex + 1;
        int bottomLeftCol = colIndex - 1;
        while (bottomLeftRow < BOARD_SIZE && bottomLeftCol >= 0 && indexes[bottomLeftRow][bottomLeftCol] == targetValue) {
            if(color[bottomLeftRow][bottomLeftCol]==colorOfSmallSquare)
                count++;
            else
                break;
            bottomLeftRow++;
            bottomLeftCol--;
        }
        if (count >= 5) {
            return true;
        }

        return false;

    }
    private boolean checkWin2(int rowIndex, int colIndex,char colorOfSmallSquare,int [][] indexes,char [][] color) {
        int targetValue = indexes[rowIndex][colIndex];
        int count = 1;

        // Check horizontally
        int left = colIndex - 1;
        while (left >= 0 && indexes[rowIndex][left] == targetValue) {
            if(color[rowIndex][left]==colorOfSmallSquare)
                count++;
            else
                break;

            left--;

        }

        int right = colIndex + 1;
        while (right < BOARD_SIZE && indexes[rowIndex][right] == targetValue) {
            if(color[rowIndex][right]==colorOfSmallSquare)
                count++;
            else
                break;
            right++;
        }

        if (count >= 5) {
            return true;
        }

        // Check vertically
        count = 1;
        int up = rowIndex - 1;
        while (up >= 0 && indexes[up][colIndex] == targetValue) {
            if(color[up][colIndex]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            up--;
        }

        int down = rowIndex + 1;
        while (down < BOARD_SIZE && indexes[down][colIndex] == targetValue) {
            if(color[down][colIndex]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            down++;
        }

        if (count >= 5) {
            return true;
        }

        // Check diagonally (top-left to bottom-right)
        count = 1;
        int topLeftRow = rowIndex - 1;
        int topLeftCol = colIndex - 1;
        while (topLeftRow >= 0 && topLeftCol >= 0 && indexes[topLeftRow][topLeftCol] == targetValue) {
            if(color[topLeftRow][topLeftCol]==colorOfSmallSquare)
                count++;
            else
                break;
            topLeftRow--;
            topLeftCol--;
        }
        int bottomRightRow = rowIndex + 1;
        int bottomRightCol = colIndex + 1;
        while (bottomRightRow < BOARD_SIZE && bottomRightCol < BOARD_SIZE && indexes[bottomRightRow][bottomRightCol] == targetValue) {
            if(color[bottomRightRow][bottomRightCol]==colorOfSmallSquare)
                count++;
            else
                break;
            bottomRightRow++;
            bottomRightCol++;
        }

        if (count >= 5) {
            return true;
        }

        // Check diagonally (top-right to bottom-left)
        count = 1;
        int topRightRow = rowIndex - 1;
        int topRightCol = colIndex + 1;
        while (topRightRow >= 0 && topRightCol < BOARD_SIZE && indexes[topRightRow][topRightCol] == targetValue) {
            if(color[topRightRow][topRightCol]==colorOfSmallSquare)
                count++;
            else
                break;
            topRightRow--;
            topRightCol++;
        }

        int bottomLeftRow = rowIndex + 1;
        int bottomLeftCol = colIndex - 1;
        while (bottomLeftRow < BOARD_SIZE && bottomLeftCol >= 0 && indexes[bottomLeftRow][bottomLeftCol] == targetValue) {
            if(color[bottomLeftRow][bottomLeftCol]==colorOfSmallSquare)
                count++;
            else
                break;
            bottomLeftRow++;
            bottomLeftCol--;
        }
        if (count >= 5) {
            return true;
        }

        return false;

    }
    private int checkWin3(int rowIndex, int colIndex,char colorOfSmallSquare,int [][] indexes,char [][] color) {
        int targetValue = indexes[rowIndex][colIndex];
        int count = 1;

        // Check horizontally
        int left = colIndex - 1;
        while (left >= 0 && indexes[rowIndex][left] == targetValue) {
            if(color[rowIndex][left]==colorOfSmallSquare)
                count++;
            else
                break;

            left--;

        }

        int right = colIndex + 1;
        while (right < BOARD_SIZE && indexes[rowIndex][right] == targetValue) {
            if(color[rowIndex][right]==colorOfSmallSquare)
                count++;
            else
                break;
            right++;
        }

        if (count >= 5) {
            return 1;
        }

        // Check vertically
        count = 1;
        int up = rowIndex - 1;
        while (up >= 0 && indexes[up][colIndex] == targetValue) {
            if(color[up][colIndex]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            up--;
        }

        int down = rowIndex + 1;
        while (down < BOARD_SIZE && indexes[down][colIndex] == targetValue) {
            if(color[down][colIndex]==colorOfSmallSquare) {
                count++;
            }
            else
                break;
            down++;
        }

        if (count >= 5) {
            return 1;
        }

        // Check diagonally (top-left to bottom-right)
        count = 1;
        int topLeftRow = rowIndex - 1;
        int topLeftCol = colIndex - 1;
        while (topLeftRow >= 0 && topLeftCol >= 0 && indexes[topLeftRow][topLeftCol] == targetValue) {
            if(color[topLeftRow][topLeftCol]==colorOfSmallSquare)
                count++;
            else
                break;
            topLeftRow--;
            topLeftCol--;
        }
        int bottomRightRow = rowIndex + 1;
        int bottomRightCol = colIndex + 1;
        while (bottomRightRow < BOARD_SIZE && bottomRightCol < BOARD_SIZE && indexes[bottomRightRow][bottomRightCol] == targetValue) {
            if(color[bottomRightRow][bottomRightCol]==colorOfSmallSquare)
                count++;
            else
                break;
            bottomRightRow++;
            bottomRightCol++;
        }

        if (count >= 5) {
            return 1;
        }

        // Check diagonally (top-right to bottom-left)
        count = 1;
        int topRightRow = rowIndex - 1;
        int topRightCol = colIndex + 1;
        while (topRightRow >= 0 && topRightCol < BOARD_SIZE && indexes[topRightRow][topRightCol] == targetValue) {
            if(color[topRightRow][topRightCol]==colorOfSmallSquare)
                count++;
            else
                break;
            topRightRow--;
            topRightCol++;
        }

        int bottomLeftRow = rowIndex + 1;
        int bottomLeftCol = colIndex - 1;
        while (bottomLeftRow < BOARD_SIZE && bottomLeftCol >= 0 && indexes[bottomLeftRow][bottomLeftCol] == targetValue) {
            if(color[bottomLeftRow][bottomLeftCol]==colorOfSmallSquare)
                count++;
            else
                break;
            bottomLeftRow++;
            bottomLeftCol--;
        }
        if (count >= 5) {
            return 1;
        }

        return 0;

    }

    private void countSquares(TreeNode node,int rowIndex, int colIndex,char colorOfSmallSquare,int [][] indexes,char [][] color) {
        int targetValue = indexes[rowIndex][colIndex];


        // Check horizontally
        int left = colIndex - 1;
        while (left >= 0 && indexes[rowIndex][left] == 1) {
            if(color[rowIndex][left]==colorOfSmallSquare)
                node.setCountH(node.getCountH()+1);
            else
                break;
            left--;

        }

        int right = colIndex + 1;
        while (right < BOARD_SIZE && indexes[rowIndex][right] == targetValue) {
            if(color[rowIndex][right]==colorOfSmallSquare)
                node.setCountH(node.getCountH()+1);
            else
                break;
            right++;
        }



        // Check vertically
        int up = rowIndex - 1;
        while (up >= 0 && indexes[up][colIndex] == targetValue) {
            if(color[up][colIndex]==colorOfSmallSquare) {
                node.setCountV(node.getCountV()+1);
            }
            else {
                break;
            }
            up--;
        }

        int down = rowIndex + 1;
        while (down < BOARD_SIZE && indexes[down][colIndex] == targetValue) {
            if(color[down][colIndex]==colorOfSmallSquare) {
                node.setCountV(node.getCountV()+1);
            }
            else {
                break;
            }
            down++;
        }



        // Check diagonally (top-left to bottom-right)

        int topLeftRow = rowIndex - 1;
        int topLeftCol = colIndex - 1;
        while (topLeftRow >= 0 && topLeftCol >= 0 && indexes[topLeftRow][topLeftCol] == targetValue) {
            if(color[topLeftRow][topLeftCol]==colorOfSmallSquare)
                node.setCountD(node.getCountD()+1);
            else
                break;
            topLeftRow--;
            topLeftCol--;
        }
        int bottomRightRow = rowIndex + 1;
        int bottomRightCol = colIndex + 1;
        while (bottomRightRow < BOARD_SIZE && bottomRightCol < BOARD_SIZE && indexes[bottomRightRow][bottomRightCol] == targetValue) {
            if(color[bottomRightRow][bottomRightCol]==colorOfSmallSquare)
                node.setCountD(node.getCountD()+1);
            else
                break;
            bottomRightRow++;
            bottomRightCol++;
        }



        // Check diagonally (top-right to bottom-left)

        int topRightRow = rowIndex - 1;
        int topRightCol = colIndex + 1;
        while (topRightRow >= 0 && topRightCol < BOARD_SIZE && indexes[topRightRow][topRightCol] == targetValue) {
            if(color[topRightRow][topRightCol]==colorOfSmallSquare)
                node.setCountD(node.getCountD()+1);
            else
                break;
            topRightRow--;
            topRightCol++;
        }

        int bottomLeftRow = rowIndex + 1;
        int bottomLeftCol = colIndex - 1;
        while (bottomLeftRow < BOARD_SIZE && bottomLeftCol >= 0 && indexes[bottomLeftRow][bottomLeftCol] == targetValue) {
            if(color[bottomLeftRow][bottomLeftCol]==colorOfSmallSquare)
                node.setCountD(node.getCountD()+1);
            else
                break;
            bottomLeftRow++;
            bottomLeftCol--;
        }


    }




    // Helper class to represent a cell on the board
    private static class Cell {
        private int row;
        private int col;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cell cell = (Cell) o;
            return row == cell.row && col == cell.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
    public void showResult(Stage primaryStage){
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 1380, 750);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
}
    public void showFinalColors(){
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(color[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void callAI(GridPane board){

            if(occupiedCells.size()==63){
                showResult(primaryStage);
            }
            if (turn.equalsIgnoreCase("AI")) {
                turn = "user";
                AITurn();
                System.out.println("Placed brick on block: " + AIRow + ", " + AIColumn);
                Rectangle smallSquare = new Rectangle(SMALL_SQUARE_SIZE, SMALL_SQUARE_SIZE, SMALL_SQUARE_COLOR);
                addSmallSquare(smallSquare, board, AIRow, AIColumn);
                if(isSmallSquareDark[0] ==0){ //if the placed square is white
                    color[AIRow][AIColumn]='w';
                    smallSquare.setFill(Color.LAVENDER);//inverse the square color
                    isSmallSquareDark[0] =1;
                }
                else{
                    color[AIRow][AIColumn]='r';
                    smallSquare.setFill(Color.MAROON);
                    isSmallSquareDark[0] =0;
                }
                occupiedCells.add(new Cell(AIRow, AIColumn));
                indexes[AIRow][AIColumn]=1; //RESERVE INDEX IN THE ARRAy


                if (checkWin(AIRow, AIColumn, color[AIRow][AIColumn])) {
                    showFinalColors();
                    System.out.print("Congratulations!");
                    if (isSmallSquareDark[0] == 0) {
                        System.out.println("RED WON");
                        WINNER = "RED";
                          showResult(primaryStage);
                    }
                    else {
                        WINNER = "WHITE";
                        System.out.println("WHITE WON");
                         showResult(primaryStage);
                    }

                }
            }
        }

    @FXML
    void OneVAI(ActionEvent event) throws ClassNotFoundException, IOException  {
        choice=2;
        Parent root=FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Welcome2Screen.fxml")));
        Scene scene=new Scene(root);
        Stage primaryStage=(Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @FXML
    void OneVOne(ActionEvent event) {
        choice=1;

        //StackPane buttonPane = new StackPane();
        //buttonPane.getChildren().add(homeButton);
        //StackPane.setAlignment(homeButton, Pos.TOP_CENTER);
        primaryStage=(Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setTitle("Magnetic Cave");
        GridPane root=new GridPane();
        GridPane board = createGameBoard();
        root.setAlignment(Pos.CENTER);
       // root.add(homeButton,0,0);
        root.add(board,0,1);
        Scene scene = new Scene(root, (BOARD_SIZE * SQUARE_SIZE), (BOARD_SIZE * SQUARE_SIZE)+40);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    void AIStartOnAction(ActionEvent event) {
        whichOneHasStarted=0;
        whichOneToStart="AI";
        turn="AI";
        primaryStage=(Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setTitle("Magnetic Cave");
        GridPane board = createGameBoard();
        Scene scene = new Scene(board, BOARD_SIZE * SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    void YouStartOnAction(ActionEvent event) {
            whichOneHasStarted=1;
            whichOneToStart="User";
            turn="User";
            primaryStage=(Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setTitle("Magnetic Cave");
            GridPane board = createGameBoard();
            Scene scene = new Scene(board, BOARD_SIZE * SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
            primaryStage.setScene(scene);
            primaryStage.show();
    }

}