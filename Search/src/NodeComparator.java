import java.util.Comparator;

/*
 * NodeComparator for PriorityQueue of Node
 * 
 * */
public class NodeComparator implements Comparator<Node>{

   public int compare(Node n1, Node n2) {
      // TODO Auto-generated method stub
   
      if (n1.pathCost < n2.pathCost){
         return -1;
      }
      else if (n1.pathCost > n2.pathCost){
         return 1;
      }
      
      return 0;
   }
   
}
