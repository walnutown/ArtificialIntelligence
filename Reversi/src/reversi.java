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
   private int[][] weightMatrix = new int[][] { { 99, -8, 8, 6, 6, 8, -8, 99 }, { -8, -24, -4, -3, -3, -4, -24, -8 }, { 8, -4, 7, 4, 4, 7, -4, 8 }, { 6, -3, 4, 0, 0, 4, -3, 6 }, { 6, -3, 4, 0, 0, 4, -3, 6 }, { 8, -4, 7, 4, 4, 7, -4, 8 }, { -8, -24, -4, -3, -3, -4, -24, -8 }, { 99, -8, 8, 6, 6, 8, -8, 99 } };

   public static void main(String[] args) {
      long lStartTime = System.currentTimeMillis();

      Board board = new Board(BOARD_SIZE);
      ArrayList<Cell> moves = new ArrayList<Cell>();

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

      board.initBoard(new File(inputFile));
      // System.out.println(board);
      // System.out.println("");
      StringBuilder mLogs = new StringBuilder();
      mLogs.append("Node,Depth,Value\n");
      LogMoves_On = true;   // turn on log here
      minMax(moves, new Board(board), MAX, cutoffDepth, EvaluationFunction.PieceNum, null, mLogs);
      printGame(moves, new Board(board), new File(outputPath));
      printLogs(mLogs, new File(outputLog));

      // board.markNextMoves(board.getSuccessors(MAX));
      // System.out.println(board);
      // board.nextMove(new Move(2,3, MAX));
      // board.nextMove(new Move(3,5, MAX));
      // System.out.println(board);
      // System.out.println(board.getEvaluation(EvaluationFunction.PieceNum));
      long lEndTime = System.currentTimeMillis();
      System.out.println("");
      System.out.println("Execution Time (milliseconds): " + (lEndTime - lStartTime));
   }

   public static void minMax(ArrayList<Cell> moves, Board board, char value, int cutoffDepth, EvaluationFunction ef, int[][] weightMatrix, StringBuilder mLogs) {
      Set<Cell> successors = board.getSuccessors(value);
      if (successors.size() == 0) {
         if (board.getSuccessors(board.getReversedValue(value)).size() > 0)
            minMax(moves, board, getReversedValue(value), cutoffDepth, ef, null, mLogs);
         return;
      }
      Cell minMaxMove = new Cell();
      Cell curr = new Cell();
      findMinMax(new Board(board), minMaxMove, curr, value, 0, cutoffDepth, ef, null, mLogs);
      LogMoves_On = false;  // turn off log, we only need to log the first step
      moves.add(minMaxMove);
      board.nextMove(minMaxMove, value);
      minMax(moves, board, getReversedValue(value), cutoffDepth, ef, null, mLogs);
   }

   /**
    * Combine the findMax and findMin here. To find the min/max value in next
    * level
    */
   public static int findMinMax(Board board, Cell minMaxMove, Cell curr, char value, int dep, int cutoffDepth, EvaluationFunction ef, int[][] weightMatrix, StringBuilder mLogs) {
      int minMaxVal = value == MAX ? Integer.MIN_VALUE : Integer.MAX_VALUE;
      if (dep == cutoffDepth) {        // if cut, return evaluation of board
         minMaxVal = board.getEvaluation(ef, weightMatrix);
         if (LogMoves_On)
            logMoves(minMaxVal, curr, dep, mLogs);
         return minMaxVal;
      }
      ArrayList<Cell> successors = new ArrayList<Cell>(board.getSuccessors(value));                                                                                     
      if (successors.size() == 0) {    // if no successors, return evaluation
         minMaxVal = board.getEvaluation(ef, weightMatrix);
         if (LogMoves_On)
            logMoves(minMaxVal, curr, dep, mLogs);
         return minMaxVal;
      }
      Collections.sort(successors, new CellComparator());
      for (Cell next : successors) {
         if (LogMoves_On && dep < cutoffDepth)
            logMoves(minMaxVal, curr, dep, mLogs);
         Board bPrev = new Board(board);
         board.nextMove(next, value);
         int minMaxNext = findMinMax(board, minMaxMove, next, board.getReversedValue(value), dep + 1, cutoffDepth, ef, weightMatrix, mLogs);
         board = bPrev;
         if (value == MAX) {
            if (minMaxNext > minMaxVal) {
               minMaxVal = minMaxNext;
               if (dep == 0) {
                  minMaxMove.copy(next);
               }
            }
         }
         else {
            if (minMaxNext < minMaxVal) {
               minMaxVal = minMaxNext;
               if (dep == 0) {
                  minMaxMove.copy(next);
               }
            }
         }
      }
      if (LogMoves_On)
         logMoves(minMaxVal, curr, dep, mLogs);
      return minMaxVal;
   }

   public static void alphaBeta() {

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
      return currVal == 'X' ? 'O' : 'X';
   }

   public static void printGame(ArrayList<Cell> moves, Board board, File outputFile) {
      PrintWriter out = null;
      try {
         out = new PrintWriter(outputFile);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      StringBuilder sb = new StringBuilder();
      int step = 1;
      char value = MAX;
      for (int i = -1; i < moves.size(); i++) {
         
         sb.append("STEP = " + step);
         sb.append("\n");
         sb.append(value == MAX ? "BLACK" : "WHITE");
         sb.append("\n");
         if (i >= 0){
            Cell m = moves.get(i);
            board.nextMove(m, getReversedValue(value));
         }
         sb.append(board);
         sb.append("\n");
         sb.append("\n");
         step++;
         value = getReversedValue(value);
      }
      sb.append("Game End");
      out.println(sb.toString());
      System.out.println(sb.toString());
      out.close();
   }

   public static void printLogs(StringBuilder mLogs, File outputFile) {
      PrintWriter out = null;
      try {
         out = new PrintWriter(outputFile);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      out.println(mLogs.toString());
      System.out.println(mLogs.toString());
      out.close();
   }

   public static void logMoves(int minMaxVal, Cell m, int dep, StringBuilder mLogs) {
      if (dep == 0)
         mLogs.append("root");
      else {
         mLogs.append(m);
      }
      mLogs.append(" ");
      mLogs.append(dep + 1);
      mLogs.append(" ");
      if (minMaxVal == Integer.MAX_VALUE)
         mLogs.append("Infinity");
      else if (minMaxVal == Integer.MIN_VALUE)
         mLogs.append("-Infinity");
      else
         mLogs.append((double) minMaxVal);
      mLogs.append("\n");
   }

}
