import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

enum EvaluationFunction {
   PieceNum, PositionWeight
};

enum ScanType {
   Row, Col, Diagonal_TopLeftToRight, Diagonal_TopLeftToBottom
};

/*
 * Board is a matrix displaying the status of game There're 3 kinds of cells on
 * a board('*' for blank, 'X' for black, 'O' for white)
 */
public class Board {
   private int size;
   private char[][] matrix;
   // create weight matrix
   private int[][] weightMatrix = new int[][] { { 99, -8, 8, 6, 6, 8, -8, 99 }, { -8, -24, -4, -3, -3, -4, -24, -8 }, { 8, -4, 7, 4, 4, 7, -4, 8 }, { 6, -3, 4, 0, 0, 4, -3, 6 }, { 6, -3, 4, 0, 0, 4, -3, 6 }, { 8, -4, 7, 4, 4, 7, -4, 8 }, { -8, -24, -4, -3, -3, -4, -24, -8 }, { 99, -8, 8, 6, 6, 8, -8, 99 } };

   public Board(int s) {
      size = s;
      matrix = new char[size][size];
   }

   public Board(Board b) {
      size = b.size();
      matrix = new char[size][size];
      for (int i = 0; i < size; i++) {
         for (int j = 0; j < size; j++) {
            matrix[i][j] = b.getCell(i, j);
         }
      }
   }

   /**
    * @return size of the board
    */
   public int size() {
      return size;
   }

   /**
    * Set the value of a cell on board
    */
   public void setCell(int x, int y, char val) {
      if (x < 0 || x >= size || y < 0 || y >= size)
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
      while (in.hasNextLine() && i < size) {
         String line = in.nextLine();
         for (j = 0; j < size; j++) {
            this.setCell(i, j, line.charAt(j));
         }
         i++;
      }
      in.close();
   }

   /**
    * Get the value of a cell on board
    * 
    * @return value
    */
   public char getCell(int x, int y) {
      if (x < 0 || x >= size || y < 0 || y >= size)
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
    * 
    * @return list of valid successors
    */
   public ArrayList<Move> getSuccessors(char value) {
      boolean hasValue = false;
      char prev = '*';
      char reversedVal = this.getReversedValue(value);
      ArrayList<Move> successors = new ArrayList<Move>();
      for (int x = 0; x < size; x++) {
         scanRowORCol(successors, x, value, ScanType.Row);
      }
      for (int y = 0; y < size; y++) {
         scanRowORCol(successors, y, value, ScanType.Col);
      }
      for (int x = 0; x < size - 2; x++) {
         scanDiagonal_TopLeft(successors, x, value, ScanType.Diagonal_TopLeftToBottom);
      }
      for (int y = 1; y < size - 2; y++) {
         scanDiagonal_TopLeft(successors, y, value, ScanType.Diagonal_TopLeftToRight);
      }
      // Scan in diagonal direction (starting from top right) to find the
      // successor moves
      for (int x = 0; x < size - 2; x++) {
         int y = size - 1;
         int i = 0;
         hasValue = false;
         prev = '*';
         while (x + i < size && y - i >= 0) {
            if (matrix[x + i][y - i] == value)
               hasValue = true;
            else if (matrix[x + i][y - i] == '*') {
               if (hasValue == true && prev == reversedVal) {
                  successors.add(new Move(x + i, y - i, value));
                  hasValue = false;
               }
            }
            prev = matrix[x + i][y - i];
            i++;
         }
         hasValue = false;
         prev = '*';
         i--;
         while (i > 0) {
            if (matrix[x + i][y - i] == value)
               hasValue = true;
            else if (matrix[x + i][y - i] == '*') {
               if (hasValue == true && prev == reversedVal) {
                  successors.add(new Move(x + i, y - i, value));
                  hasValue = false;
               }
            }
            prev = matrix[x + i][y - i];
            i--;
         }
      }
      for (int y = size - 2; y > 1; y--) {
         int x = 0;
         int i = 0;
         hasValue = false;
         prev = '*';
         while (x + i < size && y - i >= 0) {
            if (matrix[x + i][y - i] == value)
               hasValue = true;
            else if (matrix[x + i][y - i] == '*') {
               if (hasValue == true && prev == reversedVal) {
                  successors.add(new Move(x + i, y - i, value));
                  hasValue = false;
               }
            }
            prev = matrix[x + i][y - i];
            i++;
         }
         hasValue = false;
         prev = '*';
         i--;
         while (i > 0) {
            if (matrix[x + i][y - i] == value)
               hasValue = true;
            else if (matrix[x + i][y - i] == '*') {
               if (hasValue == true && prev == reversedVal) {
                  successors.add(new Move(x + i, y - i, value));
                  hasValue = false;
               }
            }
            prev = matrix[x + i][y - i];
            i--;
         }
      }
      return successors;
   }

   /**
    * Scan in diagonal direction (starting from top left) to find the successor
    * moves
    */
   public void scanDiagonal_TopLeft(ArrayList<Move> successors, int x, char value, ScanType st) {
      char reversedVal = this.getReversedValue(value);
      int y = 0;
      int i = 0;
      boolean hasValue = false;
      char prev = '*';
      while (x + i < size && y + i < size) {
         char curr = this.getScannedValue(x + i, y + i, st);
         if (curr == value)
            hasValue = true;
         else if (curr == '*') {
            if (hasValue == true && prev == reversedVal) {
               this.addToSuccessors(successors, x + i, y + i, value, st);
               hasValue = false;
            }
         }
         prev = curr;
         i++;
      }
      hasValue = false;
      prev = '*';
      i--;
      while (i > 0) {
         char curr = this.getScannedValue(x + i, y + i, st);
         if (curr == value)
            hasValue = true;
         else if (curr == '*') {
            if (hasValue == true && prev == reversedVal) {
               this.addToSuccessors(successors, x + i, y + i, value, st);
               hasValue = false;
            }
         }
         prev = curr;
         i--;
      }
   }

   /**
    * Scan in row or col direction to find the successor moves
    */
   public void scanRowORCol(ArrayList<Move> successors, int x, char value, ScanType st) {
      char reversedVal = this.getReversedValue(value);
      int y = 0;
      boolean hasValue = false;
      char prev = '*';
      while (y < size) {
         char curr = this.getScannedValue(x, y, st);
         if (curr == value)
            hasValue = true;
         else if (curr == '*') {
            if (hasValue == true && prev == reversedVal) {
               this.addToSuccessors(successors, x, y, value, st);
               hasValue = false;
            }
         }
         prev = curr;
         y++;
      }
      hasValue = false;
      prev = '*';
      y--;
      while (y > 0) {
         char curr = this.getScannedValue(x, y, st);
         if (curr == value)
            hasValue = true;
         else if (curr == '*') {
            if (hasValue == true && prev == reversedVal) {
               this.addToSuccessors(successors, x, y, value, st);
               hasValue = false;
            }
         }
         prev = curr;
         y--;
      }
   }

   /**
    * Get cell value according to ScanType and coordinates
    */
   public char getScannedValue(int x, int y, ScanType st) {
      char curr = '*';
      if (st == ScanType.Row || st == ScanType.Diagonal_TopLeftToBottom)
         curr = this.getCell(x, y);
      else if (st == ScanType.Col || st == ScanType.Diagonal_TopLeftToRight)
         curr = this.getCell(y, x);
      return curr;
   }

   /**
    * Add move to successors
    */
   public void addToSuccessors(ArrayList<Move> successors, int x, int y, char value, ScanType st) {
      if (st == ScanType.Row || st == ScanType.Diagonal_TopLeftToBottom){
         successors.add(new Move(x, y, value));
      }
      else if (st == ScanType.Col || st == ScanType.Diagonal_TopLeftToRight)
         successors.add(new Move(y, x, value));
   }

   /**
    * Mark the next moves on the matrix
    */
   public void markNextMoves(ArrayList<Move> successors) {
      for (Move m : successors) {
         matrix[m.getX()][m.getY()] = '@';
      }
   }

   /**
    * Flip cells according to the next move
    * 
    * @param nextMove is a valid successor move of current state
    */
   public void nextMove(Move m) {
      // Check if the new move is valid here.
      // <1>the state of new move should be 'X' or 'O'
      // <2>the value of the original position should be '*'
      if (m.getValue() == '*' || matrix[m.getX()][m.getY()] != '*') {
         try {
            throw new Exception("Invalid move, cannot flip cells!");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
      matrix[m.getX()][m.getY()] = m.getValue();   // do next move
      int x = m.getX();
      int y = m.getY();
      // flip cells in the same col
      int i = 1;
      while (x - i >= 0 && matrix[x - i][y] == this.getReversedValue(m.getValue()))
         i++;
      if (x - i >= 0 && matrix[x - i][y] == m.getValue()) {
         i--;
         while (i > 0) {
            matrix[x - i][y] = m.getValue();
            i--;
         }
      }
      i = 1;
      while (x + i < size && matrix[x + i][y] == this.getReversedValue(m.getValue()))
         i++;
      if (x + i < size && matrix[x + i][y] == m.getValue()) {
         i--;
         while (i > 0) {
            matrix[x + i][y] = m.getValue();
            i--;
         }
      }
      // flip cells in the same row
      i = 1;
      while (y - i >= 0 && matrix[x][y - i] == this.getReversedValue(m.getValue()))
         i++;
      if (y - i >= 0 && matrix[x][y - i] == m.getValue()) {
         i--;
         while (i > 0) {
            matrix[x][y - i] = m.getValue();
            i--;
         }
      }
      i = 1;
      while (y + i < size && matrix[x][y + i] == this.getReversedValue(m.getValue()))
         i++;
      if (y + i < size && matrix[x][y + i] == m.getValue()) {
         i--;
         while (i > 0) {
            matrix[x][y + i] = m.getValue();
            i--;
         }
      }
      // flip cells in the diagonal line
      i = 1;
      while (x - i >= 0 && y - i >= 0 && matrix[x - i][y - i] == this.getReversedValue(m.getValue()))
         i++;
      if (x - i >= 0 && y - i >= 0 && matrix[x - i][y - i] == m.getValue()) {
         i--;
         while (i > 0) {
            matrix[x - i][y - i] = m.getValue();
            i--;
         }
      }
      i = 1;
      while (x + i < size && y + i < size && matrix[x + i][y + i] == this.getReversedValue(m.getValue()))
         i++;
      if (x + i < size && y + i < size && matrix[x + i][y + i] == m.getValue()) {
         i--;
         while (i > 0) {
            matrix[x + i][y + i] = m.getValue();
            i--;
         }
      }
      i = 1;
      while (x - i >= 0 && y + i < size && matrix[x - i][y + i] == this.getReversedValue(m.getValue()))
         i++;
      if (x - i >= 0 && y + i < size && matrix[x - i][y + i] == m.getValue()) {
         i--;
         while (i > 0) {
            matrix[x - i][y + i] = m.getValue();
            i--;
         }
      }
      i = 1;
      while (x + i < size && y - i >= 0 && matrix[x + i][y - i] == this.getReversedValue(m.getValue()))
         i++;
      if (x + i < size && y - i >= 0 && matrix[x + i][y - i] == m.getValue()) {
         i--;
         while (i > 0) {
            matrix[x + i][y - i] = m.getValue();
            i--;
         }
      }

   }

   /**
    * Get the flipped value of current value
    * 
    * @param current is value
    */
   public char getReversedValue(char current) {
      if (current == '*')
         try {
            throw new Exception("The cell is '*', cannot be flipped.");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      return current == 'X' ? 'O' : 'X';
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
            if (matrix[i][j] == 'X') {
               if (f == EvaluationFunction.PieceNum)
                  eval += 1;
               else if (f == EvaluationFunction.PositionWeight)
                  eval += weightMatrix[i][j];
            }
            else if (matrix[i][j] == 'O') {
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
