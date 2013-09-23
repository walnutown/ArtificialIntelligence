/*
 * Jdk1.4 doesn't support Generics, we have to implement path queue
 */
public class PathQueue {
   private int index;
   private int size;
   private Path[] list;

   public PathQueue(int s) {
      size = s;
      list = new Path[size];
      index = 0;
   }

   public void add(Path p) {
      list[index] = p;
      index++;
   }

   public Path poll() {
      if (index == 0) {
         return null;
      }
      Path res = list[0];
      for (int i = 0; i < index; i++) {
         list[i] = list[i + 1];
      }
      index--;
      return res;
   }

   public boolean isEmpty() {
      return index == 0;
   }

   public Path getMin() {
      Path min = list[0];
      double minCost = min.cost;
      int minIndex = 0;
      for (int i = 1; i < index; i++) {
         if (list[i].cost < minCost) {
            minCost = list[i].cost;
            minIndex = i;
            min = list[i];
         }
      }
      // move all paths after min forward
      for (int j = minIndex; j < index; j++) {
         list[j] = list[j + 1];
      }
      index--;
      return min;
   }

   public Path getPathWithTail(Node target) {
      for (int i = 0; i < index; i++) {
         Path p = list[i];
         if (target.equals(p.getLastNode()))
            return p;
      }
      return null;
   }
   
   public void remove(Path target){
      Node tail = target.getLastNode();
      int in = 0;
      for (int i = 1; i < index; i++) {
         Path p = list[i];
         if (tail.equals(p.getLastNode()))
            in = i;   
      }
      // move all paths after min forward
      for (int j = in; j < index; j++) {
         list[j] = list[j + 1];
      }
      index--;
   }

}
