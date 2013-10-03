import java.util.Comparator;

public class StateComparator implements Comparator<State>{

   @Override
   public int compare(State s1, State s2) {
      // TODO Auto-generated method stub
   
      if (s1.getG() + s1.getH() < s2.getG() + s2.getH()){
         return -1;
      }
      else if (s1.getG() + s1.getH() > s2.getG() + s2.getH()){
         return 1;
      } 
      return 0;
   }  
}