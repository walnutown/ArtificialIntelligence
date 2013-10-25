import java.io.File;


public class reversi {
   public static final int BOARD_SIZE = 8;
   public static final char MAX = 'X';
   public static final char MIN = 'O';
   
   public static void main(String[] args) {
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
      
      board.initBoard(new File(inputFile));
      System.out.println(board);
      System.out.println("");
      board.markNextMoves(board.getSuccessors(MAX));
      System.out.println(board);
      //board.nextMove(new Move(2,3, MAX));
      //board.nextMove(new Move(3,5, MAX));
      //System.out.println(board);
      //System.out.println(board.getEvaluation(EvaluationFunction.PieceNum));
      

   }
   
   
   
   public static void minMax(){
      
   }
   
   public static void findMinMax(){
      
   }
   
   public static void alphaBeta(){

   }
   
}
