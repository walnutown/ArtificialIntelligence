/*
 * Node class, val is the name of node
 */
public class Node {
   String val;
   private Location location;
   public Node(String s) {
      val = s;
      location = new Location();
   }
   
   public Node(String s, double x, double y){
      val = s;
      location = new Location(x, y);
   }
   
   public Node(Node n){
      val = n.val;
      location = new Location(n.location);
   }
   
   public void setLocation(double x, double y){
      location.setX(x);
      location.setY(y);
   }
   
   public void setLocation(Location l){
      location.setX(l.getX());
      location.setY(l.getY());
   }
   
   public Location getLocation(){
      return location;
   }

   /**
    * Overwrite the equals() method in order for Class Node to be used as the
    * Key in HashMap.
    */
   public boolean equals(Object otherNode) {
      if (otherNode == null) {
         return false;
      }

      if (getClass() != otherNode.getClass()) {
         return false;
      }

      Node other = (Node) otherNode;
      if (!val.equals(other.val)) {
         return false;
      }
      return true;
   }

   /**
    * Overwrite the hashCode() method in order for Class Node to be used as the
    * Key in HashMap.
    */
   public int hashCode() {
      int h = 0;
      final int HASH_MULTIPLIER = 29;
      for (int i = 0; i < val.length(); i++) {
         h = val.substring(i, i+1).hashCode() * HASH_MULTIPLIER + h;
      }
      return h;
   }

   /**
    * Print the node val.
    */
   public String toString() {
      return val;
   } 
}