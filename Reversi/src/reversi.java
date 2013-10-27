import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class reversi {
   public static final int BOARD_SIZE = 8;
   public static final char MAX = 'X';
   public static final char MIN = 'O';
   private static boolean LogMoves_On = false;

   public static void main(String[] args) {
      Board board = new Board(BOARD_SIZE);
      ArrayList<Move> moves = new ArrayList<Move>();

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
      LogMoves_On = true;   // allow log here
      minMax(moves, new Board(board), MAX, cutoffDepth, EvaluationFunction.PieceNum, mLogs);
      printGame(moves, new Board(board), new File(outputPath));
      printLogs(mLogs, new File(outputLog));

      // board.markNextMoves(board.getSuccessors(MAX));
      // System.out.println(board);
      // board.nextMove(new Move(2,3, MAX));
      // board.nextMove(new Move(3,5, MAX));
      // System.out.println(board);
      // System.out.println(board.getEvaluation(EvaluationFunction.PieceNum));

   }

   public static void minMax(ArrayList<Move> moves, Board board, char value, int cutoffDepth, EvaluationFunction ef, StringBuilder mLogs) {
      ArrayList<Move> successors = board.getSuccessors(value);
      if (successors.size() == 0) {
         if (board.getSuccessors(board.getReversedValue(value)).size() != 0)
            minMax(moves, board, board.getReversedValue(value), cutoffDepth, ef, mLogs);
         return;
      }
      Move minMaxMove = new Move();
      Move curr = new Move();
      findMinMax(new Board(board), minMaxMove, curr, value, 0, cutoffDepth, ef, mLogs);
      LogMoves_On = false;  // forbid log except first step
      moves.add(minMaxMove);
      board.nextMove(minMaxMove);
      minMax(moves, board, board.getReversedValue(value), cutoffDepth, ef, mLogs);
   }

   public static int findMinMax(Board board, Move minMaxMove, Move curr, char value, int dep, int cutoffDepth, EvaluationFunction ef, StringBuilder mLogs) {
      if (dep == cutoffDepth+2){
         return board.getEvaluation(ef);
      }
      ArrayList<Move> successors = board.getSuccessors(value);
      if (successors.size() == 0) {
         return board.getEvaluation(ef);
      }
      int minMaxVal = value == MAX ? Integer.MIN_VALUE : Integer.MAX_VALUE;
      Collections.sort(successors, new MoveComparator());
      for (Move next : successors) {
         if (LogMoves_On && dep < cutoffDepth)
            logMoves(minMaxVal, curr, dep, mLogs);
         Board bPrev = new Board(board);
         board.nextMove(next);
         int minMaxNext = findMinMax(board, minMaxMove, next, board.getReversedValue(value), dep + 1, cutoffDepth, ef, mLogs);
         board = bPrev;
         if (value == MAX) {
            minMaxVal = Math.max(minMaxVal, minMaxNext);
            if (dep == 0) {
               if (minMaxNext > minMaxVal)
                  minMaxMove.copyMove(next);
               else if (minMaxNext == minMaxVal) {
                  if (next.getX() < minMaxMove.getX())
                     minMaxMove.copyMove(next);
                  else if (next.getX() == minMaxMove.getX()) {
                     if (next.getY() < minMaxMove.getY())
                        minMaxMove.copyMove(next);
                  }
               }
            }
         }
         else {
            minMaxVal = Math.min(minMaxVal, minMaxNext);
            if (dep == 0) {
               if (minMaxNext < minMaxVal)
                  minMaxMove.copyMove(next);
               else if (minMaxNext == minMaxVal) {
                  if (next.getX() < minMaxMove.getX())
                     minMaxMove.copyMove(next);
                  else if (next.getX() == minMaxMove.getX()) {
                     if (next.getY() < minMaxMove.getY())
                        minMaxMove.copyMove(next);
                  }
               }
            }
         }
      }
      if (LogMoves_On && dep == cutoffDepth)
         logMoves(minMaxVal, curr, dep, mLogs);
      return minMaxVal;
   }

   public static void alphaBeta() {

   }

   /*---------------Helper Functions----------------------*/

   public static void printGame(ArrayList<Move> moves, Board board, File outputFile) {
      PrintWriter out = null;
      try {
         out = new PrintWriter(outputFile);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      StringBuilder sb = new StringBuilder();
      int step = 1;
      for (int i = 0; i < moves.size(); i++) {
         Move m = moves.get(i);
         sb.append("STEP = " + step);
         sb.append("\n");
         sb.append(m.getValue() == MAX ? "BLACK" : "WHITE");
         sb.append("\n");
         sb.append(board);
         board.nextMove(m);
         sb.append("\n");
         sb.append("\n");
         step++;
      }
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

   public static void logMoves(int minMaxVal, Move m, int dep, StringBuilder mLogs) {
      char x = (char) (m.getX() + 1 + '0');
      char y = (char) ('a' + m.getY());
      if (m.getValue() == '*')
         mLogs.append("root");
      else {
         mLogs.append(y);
         mLogs.append(x);
      }
      mLogs.append(" ");
      mLogs.append(dep + 1);
      mLogs.append(" ");
      if (minMaxVal == Integer.MAX_VALUE)
         mLogs.append("Infinity");
      else if (minMaxVal == Integer.MIN_VALUE)
         mLogs.append("-Infinity");
      else
         mLogs.append((double)minMaxVal);
      mLogs.append("\n");
   }

}
