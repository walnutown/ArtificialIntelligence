/*
 * Node class, val is the name of node
 */
public class Node {
   String val;
   

   public Node(String s) {
      val = s;   
   }
   
   public Node(Node n){
      val = n.val;  
   }

   /**
    * Overwrite the equals() method in order for Class Node to be used as the
    * Key in HashMap.
    */
   public boolean equals(Object otherObject) {
      if (otherObject == null) {
         return false;
      }

      if (getClass() != otherObject.getClass()) {
         return false;
      }

      Node other = (Node) otherObject;
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