import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Board is a matrix displaying the status of game
 * There're 3 kinds of cells on a board('*' for blank, 'X' for black, 'O' for white)
 */
public class Board {
   int size;
   private char[][] matrix;
   
   public Board(int s){
      size = s;
      matrix = new char[size][size];
   }
   
   /**
    * Set the value of a position on board
    */
   public void setBoard(int row, int col, char val){
      if (row < 1 || row > size || col < 1 || col > size)
         try {
            throw new Exception("Position is not in the board");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      matrix[row-1][col-1] = val; 
   }
   
   /**
    * Initialize the board configuration
    * Assume that the initial configuration is valid (valid board size and value)
    * @param input storing the initial board configuration
    */
   public void initBoard(File input){
      Scanner in = null;
      try {
         in = new Scanner(input);
      }
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      // read characters line by line
      int row = 1;
      while (in.hasNextLine() && row <= 8) {
         String line = in.nextLine();
         for (int i = 0; i < size; i++){
            this.setBoard(row, i+1, line.charAt(i));
         }
         row++;
      }
      in.close();
   }
   
   /**
    * Get the value of a position on board
    * @return value
    */
   public char getBoard(int row, int col){
      if (row < 1 || row > size || col < 1 || col > size)
         try {
            throw new Exception("Position is not on the board");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      return matrix[row-1][col-1];
   }
   
   public String toString(){
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < size; i ++){
         for (int j = 0; j < size; j++){
            sb.append(matrix[i][j]);
         }
         sb.append("\n");
      }
      sb.deleteCharAt(sb.length()-1);
      return sb.toString();
   }
}
