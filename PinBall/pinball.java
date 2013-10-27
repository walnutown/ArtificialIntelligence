import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

enum Direction {
   NorthEast, NorthWest, SouthEast, SouthWest
}

public class pinball {

   public static void main(String[] args) {
      long lStartTime = System.currentTimeMillis();
      
      //Board board = initBoard(new File("pinTest.txt"));
      Board board = initBoard(new File("pinTest.txt"));
      System.out.println("Initial Board:");
      System.out.println(board);
      System.out.println("");
      System.out.println("Eat lights: " + eatMaxLights(board));
      
      long lEndTime = System.currentTimeMillis();
      System.out.println("");
      System.out.println("Execution Time (milliseconds): " + (lEndTime - lStartTime));

   }

   /**
    * Count the maximum number of lights can be eaten through one bump
    * Any two paths with a intersection can be connected through a bump
    * @param dimen is the dimension of the board
    * @param lights is the list of all lights on board
    */
   public static int eatMaxLights(Board board) {
      ArrayList<Path> paths = getPaths(board);
      Collections.sort(paths, new PathComparator());
      for (Path p : paths){
         printPaths(p, null, new Board(board));
         System.out.println("Number of lights on path: " + p.getLightsNum());
      }
      if (paths.size() == 0)
         return 0;
      else if (paths.size() == 1)
         return paths.get(0).getLightsNum();
      // more than two paths on the board here
      int num = paths.get(0).getLightsNum();
      Path pOne = paths.get(0);
      Path pTwo = null;
      for (int i = 0; i < paths.size(); i++) {
         Path p1 = paths.get(i);
         if (p1.getLightsNum() < num)
            break;
         for (int j = i + 1; j < paths.size(); j++) {
            Path p2 = paths.get(j);
            if (p2.getLightsNum() == 0)
               break;
            if (getIntersects(p1, p2).size() > 0) {
               int tmp = countLightsOnPaths(p1, p2);
               if (tmp > num){
                  num = tmp;
                  pOne = p1;
                  pTwo = p2;
               }
               else
                  break;
            }
         }
      }
      
      printPaths(pOne, pTwo, new Board(board));
      System.out.println("Maximum Path:");
       
      return num;
   }
   
   /**
    * Mark paths eating most number of lights on the board
    */
   public static void printPaths(Path p1, Path p2, Board board){
      if (p1 == null)
         return;
      for (Coordinate c : p1.getPath()){
         board.setCoordinateValue(c, 'O');
      }
      if (p2 == null){
         System.out.println("");
         System.out.println(board);
         return;
      }
      for (Coordinate c : p2.getPath()){
         board.setCoordinateValue(c, 'O');
      }  
      System.out.println("");
      System.out.println(board);
   }

   /**
    * Count the number of unique lights on two paths
    */
   public static int countLightsOnPaths(Path p1, Path p2) {
      int num = 0;
      Set<Coordinate> s1 = p1.getLights();
      Set<Coordinate> s2 = p2.getLights();
      Set<Coordinate> intersects = getIntersects(p1, p2);
      for (Coordinate c : s1) {
         if (c.isLight())
            num++;
      }
      for (Coordinate c : s2) {
         if (c.isLight() && !intersects.contains(c))
            num++;
      }
      return num;
   }

   /**
    * Get the intersects of two paths
    */
   public static Set<Coordinate> getIntersects(Path p1, Path p2) {
      Set<Coordinate> intersects = new HashSet<Coordinate>();
      for (Coordinate c : p1.getPath()) {
         if (p2.contains(c))
            intersects.add(c);
      }
      return intersects;
   }

   /**
    * Simulate playing to get the all paths of the board
    */
   public static ArrayList<Path> getPaths(Board board) {
      ArrayList<Path> paths = new ArrayList<Path>();
      for (int i = 0; i < board.getRowNum(); i++) {
         Path path = new Path();
         Coordinate start = board.getCoordinate(0, i);
         path.add(start);
         play(start, board, path, Direction.SouthWest);
         play(start, board, path, Direction.SouthEast);
         paths.add(path);
      }
      return paths;
   }

   /**
    * Play pinBall with the starting coordinate and direction
    * 
    * @param start is the starting coordinate
    * @param direct is the original direction
    */
   public static void play(Coordinate start, Board board, Path path, Direction direct) {
      Coordinate curr = start;
      Coordinate next = null;
      int rowNum = board.getRowNum();
      int colNum = board.getColNum();
      while (true) {
         // get coordinates of next integral position,
         // if the coordinate is not on board or meet the head of path , break.
         if (direct == Direction.SouthEast) {
            next = board.getCoordinate(curr.getX() + 1, curr.getY() + 1);
            if (next == null || path.meetHead(next))
               break;
         }
         else if (direct == Direction.SouthWest) {
            next = board.getCoordinate(curr.getX() + 1, curr.getY() - 1);
            if (next == null || path.meetHead(next))
               break;
         }
         else if (direct == Direction.NorthEast) {
            next = board.getCoordinate(curr.getX() - 1, curr.getY() + 1);
            if (next == null || path.meetHead(next))
               break;
         }
         else if (direct == Direction.NorthWest) {
            next = board.getCoordinate(curr.getX() - 1, curr.getY() - 1);
            if (next == null || path.meetHead(next))
               break;
         }
         // Bump on four borders: bottom, top, left, right
         if (next.getX() == rowNum - 1 && next.getY() < colNum - 1 && direct == Direction.SouthEast) {
            path.add(next);
            direct = Direction.NorthEast;
         }
         else if (next.getX() == rowNum - 1 && next.getY() > 0 && direct == Direction.SouthWest) {
            path.add(next);
            direct = Direction.NorthWest;
         }
         else if (next.getX() == 0 && next.getY() > 0 && direct == Direction.NorthWest) {
            path.add(next);
            direct = Direction.SouthWest;
         }
         else if (next.getX() == 0 && next.getY() < colNum - 1 && direct == Direction.NorthEast) {
            path.add(next);
            direct = Direction.SouthEast;
         }
         else if (next.getX() > 0 && next.getY() == 0 && direct == Direction.NorthWest) {
            path.add(next);
            direct = Direction.NorthEast;
         }
         else if (next.getX() < rowNum && next.getY() == 0 && direct == Direction.SouthWest) {
            path.add(next);
            direct = Direction.SouthEast;
         }
         else if (next.getX() > 0 && next.getY() == 0 && direct == Direction.SouthEast) {
            path.add(next);
            direct = Direction.SouthWest;
         }
         else if (next.getX() < rowNum - 1 && next.getY() == colNum - 1 && direct == Direction.NorthEast) {
            path.add(next);
            direct = Direction.NorthWest;
         }
         // bump on four corners
         else if (next.getX() == rowNum - 1 && next.getY() == colNum - 1 && direct == Direction.SouthEast) {
            path.add(next);
            break;
         }
         else if (next.getX() == rowNum - 1 && next.getY() == 0 && direct == Direction.SouthWest) {
            path.add(next);
            break;
         }
         else if (next.getX() == 0 && next.getY() == 0 && direct == Direction.NorthWest) {
            path.add(next);
            break;
         }
         else if (next.getX() == 0 && next.getY() == colNum - 1 && direct == Direction.NorthEast) {
            path.add(next);
            break;
         }
         // no bumps, continue playing in original direction
         else if (next.getX() < rowNum - 1 && next.getX() > 0 && next.getY() < rowNum - 1 && next.getY() > 0) {
            path.add(next);
            curr = next;
            continue;
         }
         else
            break;      // invalid bump
         curr = next;
      }

   }

   /**
    * Initialize the board from the input file
    */
   public static Board initBoard(File inFile) {
      Board board = null;
      Scanner in = null;
      try {
         in = new Scanner(inFile);
      }
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      int lineNum = 1;
      Coordinate dimen = new Coordinate();
      while (in.hasNextLine()) {
         Scanner lineScanner = new Scanner(in.nextLine());
         if (lineNum == 1) {
            dimen.setX(lineScanner.nextInt());
            dimen.setY(lineScanner.nextInt());
            board = new Board(dimen.getX(), dimen.getY());
         }
         else {
            Coordinate light = new Coordinate();
            light.setX(lineScanner.nextInt());
            light.setY(lineScanner.nextInt());
            board.markLight(light);
         }
         lineNum++;
      }
      return board;
   }

}
