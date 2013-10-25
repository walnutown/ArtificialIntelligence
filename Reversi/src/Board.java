import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

enum EvaluationFunction {
   PieceNum, PositionWeight
};

/*
 * Board is a matrix displaying the status of game There're 3 kinds of cells on
 * a board('*' for blank, 'X' for black, 'O' for white)
 */
public class Board {
   int size;
   private char[][] matrix;
   // create weight matrix
   private int[][] weightMatrix = new int[][] { { 99, -8, 8, 6, 6, 8, -8, 99 }, { -8, -24, -4, -3, -3, -4, -24, -8 }, { 8, -4, 7, 4, 4, 7, -4, 8 }, { 6, -3, 4, 0, 0, 4, -3, 6 }, { 6, -3, 4, 0, 0, 4, -3, 6 }, { 8, -4, 7, 4, 4, 7, -4, 8 }, { -8, -24, -4, -3, -3, -4, -24, -8 }, { 99, -8, 8, 6, 6, 8, -8, 99 } };

   public Board(int s) {
      size = s;
      matrix = new char[size][size];
   }

   /**
    * Set the value of a cell on board
    */
   public void setCell(int x, int y, char val) {
      if (x < 0 || x > size || y < 0 || y > size)
         try {
            throw new Exception("Position is not in the board");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      matrix[x][y] = val;
   }

   /**
    * Initialize the board configuration Assume that the initial configuration
    * is valid (valid board size and value)
    * 
    * @param input storing the initial board configuration
    */
   public void initBoard(File input) {
      Scanner in = null;
      try {
         in = new Scanner(input);
      }
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      // read characters line by line
      int i = 0, j = 0;
      while (in.hasNextLine() && j < size) {
         String line = in.nextLine();
         for (; j < size; j++) {
            this.setCell(i, j, line.charAt(i));
         }
         j++;
      }
      in.close();
   }

   /**
    * Get the value of a cell on board
    * 
    * @return value
    */
   public char getCell(int x, int y) {
      if (x < 0 || x > size || y < 0 || y > size)
         try {
            throw new Exception("Position is not on the board");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      return matrix[x][y];
   }
   
   /**
    * Get all the valid successor moves of current state
    * @return list of valid successors
    */
   public ArrayList<Coordinate> getValidSuccessors() {
      ArrayList<Coordinate> successors = new ArrayList<Coordinate>();
      
      return successors;
   }
   
   /**
    * Flip cells according to the new move
    * @param newMove is a valid successor move of current state
    */
   public void flipCells(Coordinate newMove, char state){
      int x = newMove.getX();
      int y = newMove.getY();
      if (state == '*'){
         try {
            throw new Exception("The cell is blank, cannot be flipped.");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
      // flip cells in the same row
      while ()
      
   }
   
   /**
    * Get the flipped state of current state
    * @param current is current state
    */
   public char getFlippedState(char current){
      if (current == '*')
         try {
            throw new Exception("The cell is blank, cannot be flipped.");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      return current == 'X'? 'O' : 'X';
   }

   /**
    * Get the evaluation of states according to different evaluation functions
    * PieceNum : BlackNum - WhiteNum PositionWeight : BalckWeigth - WhiteWeight
    * 
    * @return evaluation value
    */
   public int getEvaluation(EvaluationFunction f) {
      int eval = 0;
      for (int i = 0; i < size; i++) {
         for (int j = 0; j < size; j++) {
            if (matrix[i][j] == 'X'){
               if (f == EvaluationFunction.PieceNum)
                  eval += 1;
               else if (f == EvaluationFunction.PositionWeight)
                  eval += weightMatrix[i][j];
            }
            else if (matrix[i][j] == 'O'){
               if (f == EvaluationFunction.PieceNum)
                  eval -= 1;
               else if (f == EvaluationFunction.PositionWeight)
                  eval -= weightMatrix[i][j];
            }
         }
      }
      return eval;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < size; i++) {
         for (int j = 0; j < size; j++) {
            sb.append(matrix[i][j]);
         }
         sb.append("\n");
      }
      sb.deleteCharAt(sb.length() - 1);
      return sb.toString();
   }
}
