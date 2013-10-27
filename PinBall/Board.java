import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * Board is a matrix of Coordinates
 */
public class Board {
   private Coordinate[][] board;
   private Set<Coordinate> lights;
   private int rowNum;
   private int colNum;

   public Board(int rowNum, int colNum) {
      this.rowNum = rowNum;
      this.colNum = colNum;
      lights = new HashSet<Coordinate>();
      board = new Coordinate[rowNum][colNum];
      for (int i = 0; i < rowNum; i++) {
         for (int j = 0; j < colNum; j++) {
            board[i][j] = new Coordinate(i, j);
         }
      }
   }
   
   public Board(Board b){
      this.rowNum = b.getRowNum();
      this.colNum = b.getColNum();
      lights = b.getLights();
      board = new Coordinate[rowNum][colNum];
      for (int i = 0; i < rowNum; i++) {
         for (int j = 0; j < colNum; j++) {
            board[i][j] = new Coordinate(b.getCoordinate(i, j));
         }
      }
   }

   /**
    * Mark a list of lights on the board. If light is not on the board, return
    * false.
    */
   public void markLights(ArrayList<Coordinate> lights) {
      for (Coordinate light : lights) {
         Coordinate n = this.getCoordinate(light);
         if (n == null) {
            try {
               throw new Exception("Light " + light + "is not on the board");
            }
            catch (Exception e) {
               e.printStackTrace();
            }
         }
         n.setLight(true);
         lights.add(n);
      }
   }

   /**
    * Mark a light on the board
    */
   public void markLight(Coordinate light) {
      Coordinate n = this.getCoordinate(light);
      try {
         if (n == null)
            throw new Exception("Light " + light + "is not on the board");
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      n.setLight(true);
      lights.add(n);
   }

   public Coordinate getCoordinate(Coordinate c) {
      return this.getCoordinate(c.getX(), c.getY());
   }

   public Coordinate getCoordinate(int row, int col) {
      if (row < 0 || row >= this.getRowNum() || col < 0 || col >= this.getColNum())
         return null;
      return board[row][col];
   }
   
   public void setCoordianteValue(int row, int col, char val){
      if (row < 0 || row >= this.getRowNum() || col < 0 || col >= this.getColNum()){
         try {
            throw new Exception("[" + row + "," + col + "] is not on the board");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
      this.getCoordinate(row, col).setValue(val);
   }
   
   public void setCoordinateValue(Coordinate c, char val){
      this.setCoordianteValue(c.getX(), c.getY(), val);
   }
   
   public Set<Coordinate> getLights (){
      return lights;
   }

   public int getRowNum() {
      return rowNum;
   }
   public int getColNum() {
      return colNum;
   }
   public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < this.getRowNum(); i++) {
         for (int j = 0; j < this.getColNum(); j++) {
            sb.append(this.getCoordinate(i, j).getValue());
            sb.append(" ");
         }
         sb.append("\n");
      }
      sb.deleteCharAt(sb.length() - 1);
      return sb.toString();
   }
}
