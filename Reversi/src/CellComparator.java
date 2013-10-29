import java.util.Comparator;

/*
 * Compare Cells according to their coordinates
 */
public class CellComparator implements Comparator<Cell>{
   @Override
   public int compare(Cell c1, Cell c2) {
      if (c1.getX() < c2.getX())
         return -1;
      else if (c1.getX() == c2.getX()) {
         if (c1.getY() < c2.getY())
            return -1;
         else if (c1.getY() > c2.getY())
            return 1;
         else
            return 0;
      }
      else
         return 1;
   }  
}
