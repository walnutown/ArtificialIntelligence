import java.util.ArrayList;

/*
 * Path implementation
 * */
public class Path {
   double cost;
   Node root;
   private static ArrayList<Node> list;
   public Path(Node r){
      root = r;
      list = new ArrayList<Node>();
      list.add(root);
      cost = 0.0;
   }
   
   public Path(Path p){
      root = new Node(p.root);
      list = new ArrayList<Node>();
      list.add(root);
      for (int i = 1; i < p.getNodeList().size(); i++){
         list.add(p.getNodeList().get(i));
      }
      cost = p.cost;
   }
   
   public void addNode(Node n, double c){
      list.add(n);
      cost += c;
   }
   
   public Node getLastNode(){
      return list.get(list.size()-1);
   }
   
   public boolean contains(Node target){
      for (Node n : list){
         if (target.equals(n))
            return true;
      }
      return false;
   }
   
   public ArrayList<Node> getNodeList(){
      return list;
   }
   
   public String toString(){
      return list.toString();
   }
   
}
