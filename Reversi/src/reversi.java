import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class reversi {
   public static final int BOARD_SIZE = 8;
   public static final char MAX = 'X';
   public static final char MIN = 'O';
   private static boolean LogMoves_On = false;
   private static boolean LogBoards_On = false;
   private static boolean AlphaBeta = false;

   public static void main(String[] args) {
      long lStartTime = System.currentTimeMillis();

      Board board = new Board(BOARD_SIZE);
      // read command line
      String inputFile = "";
      String outputPath = "";
      String outputLog = "";
      int cutoffDepth = 1;
      int type = 0;
      try {
         if (args.length != 10)
            throw new Exception();
         type = Integer.parseInt(args[1]);
         cutoffDepth = Integer.parseInt(args[3]);
         inputFile = args[5];
         outputPath = args[7];
         outputLog = args[9];
      }
      catch (Exception e) {
         System.out.println("Invalid Commands");
      }
      // play games using 3 different methods
      board.initBoard(new File(inputFile));
      StringBuilder mLogs = new StringBuilder();
      StringBuilder bLogs = new StringBuilder();
      LogMoves_On = true;   // turn on move log here
      LogBoards_On = true;
      if (type == 1) {
         mLogs.append("Node,Depth,Value\n");
         minMax(new Board(board), MAX, cutoffDepth, EvaluationFunction.PieceNum, null, mLogs, bLogs, 1);
      }
      else if (type == 2) {
         AlphaBeta = true;
         mLogs.append("Node,Depth,Value,Alpha,Beta\n");
         minMax(new Board(board), MAX, cutoffDepth, EvaluationFunction.PieceNum, null, mLogs, bLogs, 1);
      }
      else if (type == 3) {
         mLogs.append("Node,Depth,Value,Alpha,Beta\n");
         int[][] weightMatrix = new int[][] { { 99, -8, 8, 6, 6, 8, -8, 99 }, { -8, -24, -4, -3, -3, -4, -24, -8 }, { 8, -4, 7, 4, 4, 7, -4, 8 }, { 6, -3, 4, 0, 0, 4, -3, 6 }, { 6, -3, 4, 0, 0, 4, -3, 6 }, { 8, -4, 7, 4, 4, 7, -4, 8 }, { -8, -24, -4, -3, -3, -4, -24, -8 }, { 99, -8, 8, 6, 6, 8, -8, 99 } };
         AlphaBeta = true;
         minMax(new Board(board), MAX, cutoffDepth, EvaluationFunction.PositionWeight, weightMatrix, mLogs, bLogs, 1);
      }
      // print logs
      printBoards(bLogs, new File(outputPath));
      printMoves(mLogs, new File(outputLog));

      long lEndTime = System.currentTimeMillis();
      System.out.println("");
      System.out.println("Execution Time (milliseconds): " + (lEndTime - lStartTime));
   }

   /**
    * MinMax decision function
    */
   public static void minMax(Board board, char value, int cutoffDepth, EvaluationFunction ef, int[][] weightMatrix, StringBuilder mLogs, StringBuilder bLogs, int step) {
      Set<Cell> successors = board.getSuccessors(value);
      if (successors.size() == 0) {
         if (board.getSuccessors(board.getReversedValue(value)).size() > 0) {
            if (LogBoards_On)
               logBoards(bLogs, step, value, board, true);
            minMax(board, getReversedValue(value), cutoffDepth, ef, weightMatrix, mLogs, bLogs, step + 1);
         }
         else {
            if (LogBoards_On)
               logBoards(bLogs, step, value, board, false);
            printResults(board);
         }
         return;
      }
      Cell minMaxMove = new Cell();
      Cell curr = new Cell();
      findMinMax(new Board(board), minMaxMove, curr, value, 0, cutoffDepth, ef, weightMatrix, mLogs, bLogs, Integer.MIN_VALUE, Integer.MAX_VALUE);
      LogMoves_On = false;// turn off log moves, only log the first step
      if (LogBoards_On)
         logBoards(bLogs, step, value, board, false);
      board.nextMove(minMaxMove, value);
      minMax(board, getReversedValue(value), cutoffDepth, ef, weightMatrix, mLogs, bLogs, step + 1);
   }

   /**
    * Combine the findMax and findMin here. To find the min/max value in next
    * level
    */
   public static int findMinMax(Board board, Cell minMaxMove, Cell curr, char value, int dep, int cutoffDepth, EvaluationFunction ef, int[][] weightMatrix, StringBuilder mLogs, StringBuilder bLogs, int alpha, int beta) {
      int minMaxVal = value == MAX ? Integer.MIN_VALUE : Integer.MAX_VALUE;
      if (dep == cutoffDepth) {        // if cut, return evaluation of board
         minMaxVal = board.getEvaluation(ef, weightMatrix);
         if (LogMoves_On)
            logMoves(minMaxVal, curr, dep, mLogs, alpha, beta);
         return minMaxVal;
      }
      ArrayList<Cell> successors = new ArrayList<Cell>(board.getSuccessors(value));
      if (successors.size() == 0) {    // if no successors, return evaluation
         minMaxVal = board.getEvaluation(ef, weightMatrix);
         if (LogMoves_On)
            logMoves(minMaxVal, curr, dep, mLogs, alpha, beta);
         return minMaxVal;
      }
      Collections.sort(successors, new CellComparator());
      for (Cell next : successors) {
         if (LogMoves_On && dep < cutoffDepth)
            logMoves(minMaxVal, curr, dep, mLogs, alpha, beta);
         Board bPrev = new Board(board);
         board.nextMove(next, value);
         int minMaxNext = findMinMax(board, minMaxMove, next, board.getReversedValue(value), dep + 1, cutoffDepth, ef, weightMatrix, mLogs, bLogs, alpha, beta);
         board = bPrev;
         if (value == MAX) {
            if (minMaxNext > minMaxVal) {
               minMaxVal = minMaxNext;
               if (dep == 0) {
                  minMaxMove.copy(next);
               }
            }
            if (AlphaBeta) {
               if (minMaxVal >= beta) {
                  if (LogMoves_On)
                     logMoves(minMaxVal, curr, dep, mLogs, minMaxVal, beta);
                  return minMaxVal;
               }
               alpha = Math.max(minMaxVal, alpha);
            }
         }
         else {
            if (minMaxNext < minMaxVal) {
               minMaxVal = minMaxNext;
               if (dep == 0) {
                  minMaxMove.copy(next);
               }
            }
            if (AlphaBeta) {
               if (minMaxVal <= alpha) {
                  if (LogMoves_On)
                     logMoves(minMaxVal, curr, dep, mLogs, alpha, minMaxVal);
                  return minMaxVal;
               }
               beta = Math.min(minMaxVal, beta);
            }
         }
      }
      if (LogMoves_On)
         logMoves(minMaxVal, curr, dep, mLogs, alpha, beta);
      return minMaxVal;
   }

   /*---------------Helper Functions----------------------*/

   /**
    * Get the reversed value of current value
    */
   public static char getReversedValue(char currVal) {
      if (currVal == '*')
         try {
            throw new Exception("The cell is '*', cannot be flipped.");
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      return currVal == MAX ? MIN : MAX;
   }

   public static void printResults(Board board) {
      int black = 0;
      int white = 0;
      for (int i = 0; i < board.size(); i++) {
         for (int j = 0; j < board.size(); j++) {
            if (board.getCell(i, j).getValue() == MAX)
               black++;
            else if (board.getCell(i, j).getValue() == MIN)
               white++;
         }
      }
      if (black > white)
         System.out.println("Black wins " + (black - white));
      else if (black < white)
         System.out.println("Black loses " + (white - black));
      else
         System.out.println("Draw");
   }

   public static void printBoards(StringBuilder bLogs, File outputFile) {
      PrintWriter out = null;
      try {
         out = new PrintWriter(outputFile);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      bLogs.append("Game End");
      out.println(bLogs.toString());
      // System.out.println(bLogs.toString());
      out.close();
   }

   public static void logBoards(StringBuilder bLogs, int step, char value, Board board, boolean pass) {
      bLogs.append("STEP = " + step);
      bLogs.append("\n");
      bLogs.append(value == MAX ? "BLACK" : "WHITE");
      if (pass) {
         bLogs.append(" ");
         bLogs.append("PASS");
      }
      bLogs.append("\n");
      bLogs.append(board);
      bLogs.append("\n");
      bLogs.append("\n");
   }

   public static void printMoves(StringBuilder mLogs, File outputFile) {
      PrintWriter out = null;
      try {
         out = new PrintWriter(outputFile);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      out.print(mLogs.toString());
      // System.out.println(mLogs.toString());
      out.close();
   }

   public static void logMoves(int minMaxVal, Cell m, int dep, StringBuilder mLogs, int alpha, int beta) {
      if (dep == 0)
         mLogs.append("root");
      else {
         mLogs.append(m);
      }
      mLogs.append(" ");
      mLogs.append(dep + 1);
      mLogs.append(" ");
      addToLogMove(mLogs, minMaxVal);
      if (AlphaBeta) {
         mLogs.append(" ");
         addToLogMove(mLogs, alpha);
         mLogs.append(" ");
         addToLogMove(mLogs, beta);

         if (alpha >= beta) {
            mLogs.append(" ");
            mLogs.append("CUT-OFF");
         }
      }
      mLogs.append("\n");
   }

   public static void addToLogMove(StringBuilder mLogs, int num) {
      if (num == Integer.MAX_VALUE)
         mLogs.append("Infinity");
      else if (num == Integer.MIN_VALUE)
         mLogs.append("-Infinity");
      else
         mLogs.append((double) num);
   }
}
