/*
 * Edge class
 */
public class Edge {
   double cost;
   int reliable;
   Node end;

   /**
    * @param end is the ending node of the edge
    * @param cost is the cost of from starting node to ending node
    * @param reliable indicates whether the edge is reliable, 1 for reliable, 0
    *        for unreliable
    */
   public Edge(Node n, double c, int r) {
      end = n;
      cost = c;
      reliable = r;
   }
   
   /**
    * Print the end node val.
    */
   public String toString() {
      return end.val;
   }
}