import java.util.Comparator;


public class LiteralComparator implements Comparator<Character>{

   @Override
   public int compare(Character l1, Character l2) {
      if (Character.isUpperCase(l1) && Character.isUpperCase(l2) || Character.isLowerCase(l1) && Character.isLowerCase(l2)){
         if (l1 > l2)
            return 1;
         else if(l1 < l2)
            return -1;
         return 0;
      }
      else if (Character.isUpperCase(l1) && Character.isLowerCase(l2)){
         return -1;
      }
      else
         return 1;
   }
   
}
