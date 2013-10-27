import java.util.HashSet;
import java.util.Set;

/*
 * Path consists a set of coordinates
 * <1> a pinBall finally loops in one path
 * <2> a board may have multiple paths, including the two longest diagonals
 */
public class Path {
   private Set<Coordinate> set;
   private Coordinate head;
   
   public Path(){
      set = new HashSet<Coordinate>();
      head = null;
   }

   public void add(Coordinate c){
      if (set.size() == 0)
         head = c;
      set.add(c);
   }
   
   public int getLightsNum(){
      return this.getLights().size();
   }
   
   public Set<Coordinate> getLights(){
      Set<Coordinate> lights = new HashSet<Coordinate>();
      for (Coordinate c: this.getPath()){
         if (c.isLight())
            lights.add(c);
      }
      return lights;
   }
   
   public void remove(Coordinate c){
      if (!set.contains(c))
         try {
            throw new Exception("Cannot remove! " + c + " is not on Path:" + this.toString());
         }
         catch (Exception e) {
            e.printStackTrace();
         }
      else{
         if (c.equals(head))
            head = null;
         set.remove(c);
      }
   }
   
   public Set<Coordinate> getPath(){
      return set;
   }
   
   public boolean meetHead(Coordinate c){
      if (c.equals(this.getHead()))
         return true;
      return false;
   }
   
   public boolean contains(Coordinate c){
      if (this.getPath().contains(c))
         return true;
      return false;
   }
   
   public String toString(){
      return this.getPath().toString();
   }

   public Coordinate getHead() {
      return head;
   }
}
