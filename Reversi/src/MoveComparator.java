import java.util.Comparator;


public class MoveComparator implements Comparator<Move>{
   @Override
   public int compare(Move m1, Move m2) {
      if (m1.getX() < m2.getX())
         return -1;
      else if (m1.getX() == m2.getX()) {
         if (m1.getY() < m2.getY())
            return -1;
         else if (m1.getY() > m2.getY())
            return 1;
         else
            return 0;
      }
      else
         return 1;
   }  
}
