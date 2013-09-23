import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * Graph Implementation
 * */
public class Graph {
   Node root=null;
   int size;
   boolean isDirected;
   
   private double[][] adjMatrix;
   private int[][] reMatrix;
   private ArrayList<Node> list;
    
   public Graph(int s, Node r, boolean isd){
      root = r;
      size = s;
      isDirected = isd;
      list = new ArrayList<Node>();
      list.add(root);
      adjMatrix = new double[size][size];
      reMatrix = new int[size][size];
   }
   
   public void addNode(Node n){
      list.add(n);
   }
   
   public void addEdge(Node p, Node q, double cost, int reliable){
      int pi = list.indexOf(p);
      int qi = list.indexOf(q);
      
      if (p.equals(root)){
         pi = 0;
      }
      if(q.equals(root)){
         qi = 0;
      }
      
      if (pi < 0 || qi < 0)
         throw new NullPointerException("Node not found");
      adjMatrix[pi][qi] = cost;
      if (!isDirected)
         adjMatrix[qi][pi] = cost;
      
      reMatrix[pi][qi] = reliable;
   }
   
   public Set<Node> getNeighbor(Node n){
      int ni = list.indexOf(n);
      Set<Node> set = new HashSet<Node>();
      if (ni < 0)
         throw new NullPointerException("Node not found");
      for (int j = 0; j < list.size(); j++){
         if (adjMatrix[ni][j] > 0)
            set.add(list.get(j));      
      }   
      return set;   
   }
   
   public double getEdgeCost(Node p, Node q){
      int pi = list.indexOf(p);
      int qi = list.indexOf(q);
      if (pi < 0 || qi < 0)
         throw new NullPointerException("Node not found");
      return adjMatrix[pi][qi];
   }
   
   public int getEdgeReliability(Node p, Node q){
      int pi = list.indexOf(p);
      int qi = list.indexOf(q);
      if (pi < 0 || qi < 0)
         throw new NullPointerException("Node not found");
      return reMatrix[pi][qi];
   }
   
   public ArrayList<Node> getNodeList(){
      return list;
   }  
   
}
