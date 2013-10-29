import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

enum EvaluationFunction {
   PieceNum, PositionWeight
};

enum ScanType {
   Row, Col, Diagonal_TopLeftToRight, Diagonal_TopLeftToBottom
};

/*
 * Board is a matrix displaying the status of game. There're 3 kinds of cells on
 * a board('*' for blank, 'X' for black, 'O' for white)
 */
public class Board {
   private int size;
   private Cell[][] matrix;

   public Board(int s) {
      size = s;
      matrix = new Cell[size][size];
      for (int i = 0; i < size; i++) {
         for (int j = 0; j < size; j++) {
            matrix[i][j] = new Cell(i, j, '*');
         }
      }
   }

   public Board(Board b) {
      size = b.size();
      matrix = new Cell[size][size];
      for (int i = 0; i < size; i++) {
         for (int j = 0; j < size; j++) {
            matrix[i][j] = new Cell(b.getCell(i, j));
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
    * Initialize the board configuration according to input file. Assume that
    * the initial configuration is valid (valid board size and value)
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
            this.getCell(i, j).setValue(line.charAt(j));
         }
         i++;
      }
      in.close();
   }

   /**
    * Get the Cell on board according to coordinates
    * 
    * @return null if cell is not on board
    */
   public Cell getCell(int x, int y) {
      if (x < 0 || x >= size || y < 0 || y >= size)
         return null;
      return matrix[x][y];
   }

   public Cell getCell(Cell c) {
      return getCell(c.getX(), c.getY());
   }

   /**
    * Get all the valid successor moves of current state
    * 
    * @return list of valid successors
    */
   public Set<Cell> getSuccessors(char value) {
      boolean hasValue = false;
      char prevVal = '*';
      char reversedVal = this.getReversedValue(value);
      Set<Cell> successors = new HashSet<Cell>();
      // scan rows from both directions: starting from left and starting from
      // right
      for (int i = 0; i < size; i++) {
         int j = 0;
         hasValue = false;
         prevVal = '*';
         while (j < size) {
            if (this.getCell(i, j).getValue() == value)
               hasValue = true;
            else if (this.getCell(i, j).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(i, j)))
                     successors.add(this.getCell(i, j));
               }
               hasValue = false;
            }
            prevVal = this.getCell(i, j).getValue();
            j++;
         }
         hasValue = false;
         prevVal = '*';
         j--;
         while (j >= 0) {
            if (this.getCell(i, j).getValue() == value)
               hasValue = true;
            else if (this.getCell(i, j).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(i, j)))
                     successors.add(this.getCell(i, j));
               }
               hasValue = false;
            }
            prevVal = this.getCell(i, j).getValue();
            j--;
         }
      }
      // scan columns from both directions: starting from top and starting from
      // bottom
      for (int j = 0; j < size; j++) {
         int i = 0;
         hasValue = false;
         prevVal = '*';
         while (i < size) {
            if (this.getCell(i, j).getValue() == value)
               hasValue = true;
            else if (this.getCell(i, j).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(i, j)))
                     successors.add(this.getCell(i, j));
               }
               hasValue = false;
            }
            prevVal = this.getCell(i, j).getValue();
            i++;
         }
         hasValue = false;
         prevVal = '*';
         i--;
         while (i >= 0) {
            if (this.getCell(i, j).getValue() == value)
               hasValue = true;
            else if (this.getCell(i, j).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(i, j)))
                     successors.add(this.getCell(i, j));
               }
               hasValue = false;
            }
            prevVal = this.getCell(i, j).getValue();
            i--;
         }
      }
      // scan two diagonals from both directions
      for (int x = 0; x < size - 2; x++) {
         int y = 0;
         int i = 0;
         hasValue = false;
         prevVal = '*';
         while (x + i < size && y + i < size) {
            if (this.getCell(x + i, y + i).getValue() == value)
               hasValue = true;
            else if (this.getCell(x + i, y + i).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(x + i, y + i)))
                     successors.add(this.getCell(x + i, y + i));
               }
               hasValue = false;
            }
            prevVal = this.getCell(x + i, y + i).getValue();
            i++;
         }
         hasValue = false;
         prevVal = '*';
         i--;
         while (i >= 0) {
            if (this.getCell(x + i, y + i).getValue() == value)
               hasValue = true;
            else if (this.getCell(x + i, y + i).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(x + i, y + i)))
                     successors.add(this.getCell(x + i, y + i));
               }
               hasValue = false;
            }
            prevVal = this.getCell(x + i, y + i).getValue();
            i--;
         }
      }
      for (int y = 0; y < size - 2; y++) {
         int x = 0;
         int i = 0;
         hasValue = false;
         prevVal = '*';
         while (x + i < size && y + i < size) {
            if (this.getCell(x + i, y + i).getValue() == value)
               hasValue = true;
            else if (this.getCell(x + i, y + i).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(x + i, y + i)))
                     successors.add(this.getCell(x + i, y + i));
               }
               hasValue = false;
            }
            prevVal = this.getCell(x + i, y + i).getValue();
            i++;
         }
         hasValue = false;
         prevVal = '*';
         i--;
         while (i >= 0) {
            if (this.getCell(x + i, y + i).getValue() == value)
               hasValue = true;
            else if (this.getCell(x + i, y + i).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(x + i, y + i)))
                     successors.add(this.getCell(x + i, y + i));
               }
               hasValue = false;
            }
            prevVal = this.getCell(x + i, y + i).getValue();
            i--;
         }
      }

      for (int x = 0; x < size - 2; x++) {
         int y = size - 1;
         int i = 0;
         hasValue = false;
         prevVal = '*';
         while (x + i < size && y - i >= 0) {
            if (this.getCell(x + i, y - i).getValue() == value)
               hasValue = true;
            else if (this.getCell(x + i, y - i).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(x + i, y - i)))
                     successors.add(this.getCell(x + i, y - i));
               }
               hasValue = false;
            }
            prevVal = this.getCell(x + i, y - i).getValue();
            i++;
         }
         hasValue = false;
         prevVal = '*';
         i--;
         while (i >= 0) {
            if (this.getCell(x + i, y - i).getValue() == value)
               hasValue = true;
            else if (this.getCell(x + i, y - i).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(x + i, y - i)))
                     successors.add(this.getCell(x + i, y - i));
               }
               hasValue = false;
            }
            prevVal = this.getCell(x + i, y - i).getValue();
            i--;
         }
      }
      for (int y = size - 1; y > 1; y--) {
         int x = 0;
         int i = 0;
         hasValue = false;
         prevVal = '*';
         while (x + i < size && y - i >= 0) {
            if (this.getCell(x + i, y - i).getValue() == value)
               hasValue = true;
            else if (this.getCell(x + i, y - i).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(x + i, y - i)))
                     successors.add(this.getCell(x + i, y - i));
               }
               hasValue = false;
            }
            prevVal = this.getCell(x + i, y - i).getValue();
            i++;
         }
         hasValue = false;
         prevVal = '*';
         i--;
         while (i >= 0) {
            if (this.getCell(x + i, y - i).getValue() == value)
               hasValue = true;
            else if (this.getCell(x + i, y - i).getValue() == '*') {
               if (hasValue == true && prevVal == reversedVal) {
                  if (!successors.contains(this.getCell(x + i, y - i)))
                     successors.add(this.getCell(x + i, y - i));
               }
               hasValue = false;
            }
            prevVal = this.getCell(x + i, y - i).getValue();
            i--;
         }
      }
      return successors;
   }

   /**
    * Mark the next moves on the board
    */
   public void markNextMoves(ArrayList<Cell> successors) {
      for (Cell c : successors) {
         this.getCell(c).setValue('@');
      }
   }

   /**
    * Flip cells according to the next move
    * 
    * @param move is a valid successor move of current state
    */
   public void nextMove(Cell move, char value) {
      // Check if the new move is valid here.
      // <1>the state of new move should be 'X' or 'O'
      // <2>the value of the original position should be '*'
      if (value == '*' || this.getCell(move).getValue() != '*') {
         try {
            throw new Exception("Invalid move, cannot flip cells!");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
      this.getCell(move).setValue(value);   // do next move
      int x = move.getX();
      int y = move.getY();
      // flip cells in the same col
      int i = 1;
      while (x - i >= 0 && this.getCell(x - i, y).getValue() == this.getReversedValue(value))
         i++;
      if (x - i >= 0 && this.getCell(x - i, y).getValue() == value) {
         i--;
         while (i > 0) {
            this.getCell(x - i, y).setValue(value);
            i--;
         }
      }
      i = 1;
      while (x + i < size && this.getCell(x + i, y).getValue() == this.getReversedValue(value))
         i++;
      if (x + i < size && this.getCell(x + i, y).getValue() == value) {
         i--;
         while (i > 0) {
            this.getCell(x + i, y).setValue(value);
            i--;
         }
      }
      // flip cells in the same row
      i = 1;
      while (y - i >= 0 && this.getCell(x, y - i).getValue() == this.getReversedValue(value))
         i++;
      if (y - i >= 0 && this.getCell(x, y - i).getValue() == value) {
         i--;
         while (i > 0) {
            this.getCell(x, y - i).setValue(value);
            i--;
         }
      }
      i = 1;
      while (y + i < size && this.getCell(x, y + i).getValue() == this.getReversedValue(value))
         i++;
      if (y + i < size && this.getCell(x, y + i).getValue() == value) {
         i--;
         while (i > 0) {
            this.getCell(x, y + i).setValue(value);
            i--;
         }
      }
      // flip cells in the diagonal line
      i = 1;
      while (x - i >= 0 && y - i >= 0 && this.getCell(x - i, y - i).getValue() == this.getReversedValue(value))
         i++;
      if (x - i >= 0 && y - i >= 0 && this.getCell(x - i, y - i).getValue() == value) {
         i--;
         while (i > 0) {
            this.getCell(x - i, y - i).setValue(value);
            i--;
         }
      }
      i = 1;
      while (x + i < size && y + i < size && this.getCell(x + i, y + i).getValue() == this.getReversedValue(value))
         i++;
      if (x + i < size && y + i < size && this.getCell(x + i, y + i).getValue() == value) {
         i--;
         while (i > 0) {
            this.getCell(x + i, y + i).setValue(value);
            i--;
         }
      }
      i = 1;
      while (x - i >= 0 && y + i < size && this.getCell(x - i, y + i).getValue() == this.getReversedValue(value))
         i++;
      if (x - i >= 0 && y + i < size && this.getCell(x - i, y + i).getValue() == value) {
         i--;
         while (i > 0) {
            this.getCell(x - i, y + i).setValue(value);
            i--;
         }
      }
      i = 1;
      while (x + i < size && y - i >= 0 && this.getCell(x + i, y - i).getValue() == this.getReversedValue(value))
         i++;
      if (x + i < size && y - i >= 0 && this.getCell(x + i, y - i).getValue() == value) {
         i--;
         while (i > 0) {
            this.getCell(x + i, y - i).setValue(value);
            i--;
         }
      }

   }

   /**
    * Get the reversed value of current value
    */
   public char getReversedValue(char currVal) {
      if (currVal == '*')
         try {
            throw new Exception("The cell is '*', cannot be flipped.");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      return currVal == 'X' ? 'O' : 'X';
   }

   /**
    * Get the evaluation of states according to different evaluation functions
    * PieceNum : BlackNum - WhiteNum; PositionWeight : BalckWeigth - WhiteWeight
    * 
    * @return evaluation value
    */
   public int getEvaluation(EvaluationFunction f, int[][] weightMatrix) {
      if (f == EvaluationFunction.PositionWeight && weightMatrix == null) {
         try {
            throw new Exception("Please assign the weight matrix!");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      }
      int eval = 0;
      for (int i = 0; i < size; i++) {
         for (int j = 0; j < size; j++) {
            if (this.getCell(i, j).getValue() == 'X') {
               if (f == EvaluationFunction.PieceNum)
                  eval += 1;
               else if (f == EvaluationFunction.PositionWeight)
                  eval += weightMatrix[i][j];
            }
            else if (this.getCell(i, j).getValue() == 'O') {
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
            sb.append(this.getCell(i, j).getValue());
         }
         sb.append("\n");
      }
      sb.deleteCharAt(sb.length() - 1);
      return sb.toString();
   }
}
