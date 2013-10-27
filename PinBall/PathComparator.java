import java.util.Comparator;

/*
 * Implement the Comparator interface to sort paths according to number of lights 
 * Ascending order
 */
public class PathComparator implements Comparator<Path>{

   @Override
   public int compare(Path p1, Path p2) {
      if (p1.getLightsNum() < p2.getLightsNum())
         return 1;
      else if (p1.getLightsNum() > p2.getLightsNum())
         return -1; 
      return 0;
   }

}
