import java.util.ArrayList;

/*
 * State is used to store current information
 * */
public class State {
   Node root;
   private Node current;
   private ArrayList<Node> visited;
   private double g;  // g(n) = path cost
   private double h;  // h(n) = heuristic
     
   public State(Node root){
      this.root = root; 
      this.setCurrent(root);
      visited = new ArrayList<Node>();
      visited.add(root);
      this.setG(0.0);
      this.setH(0.0);
   }
   
   public State(State s){
      this.root = s.root;
      this.setCurrent(s.getCurrent());
      visited = new ArrayList<Node>(s.getPath());
      this.setG(s.getG());
      this.setH(s.getH());
   }
   
   public void setCurrent(Node n){
      current = new Node(n);
   }
   
   public Node getCurrent(){
      return current;
   }
   
   public Node getLastNode(){
      return current;
   }
   
   public void visitNode(Node n, double gcost, double hcost){
      this.setCurrent(n);
      visited.add(n);  // current node is added into the visited list
      this.setG(gcost);
      this.setH(hcost);
   }
   
   public ArrayList<Node> getPath(){
      return visited;
   }
   
   public double getG() {
      return g;
   }

   public void setG(double g) {
      this.g = g;
   }

   public double getH() {
      return h;
   }

   public void setH(double h) {
      this.h = h;
   }
   
   public String toString(){
      return visited.toString();
   }
}
