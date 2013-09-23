
/*
 * Path implementation
 */
public class Path {
   double cost;
   Node root;
   private int index;
   private int size;
   private Node[] list;

   public Path(int s) {
      cost = 0.0;
      size = s;
      list = new Node[size];
      index = 0;
      root = null;
   }

   public Path(Node r, int s) {
      cost = 0.0;
      root = r;
      size = s;
      list = new Node[size];
      list[0] = root;
      index = 1;
   }

   public Path(Path p) {
      cost = p.cost;
      root = new Node(p.root);
      size = p.size;
      list = new Node[size];
      list[0] = root;
      index = 1;
      while (index < p.index) {
         list[index] = p.getNodeList()[index];
         index++;
      }
   }

   public void add(Node n, double c) {
      if (index == 0)
         root = n;
      list[index] = n;
      index++;
      cost += c;
   }

   public void addNode(Node n) {
      if (index == 0)
         root = n;
      list[index] = n;
      index++;
   }

   public Node getLastNode() {
      return list[index - 1];
   }

   public boolean contains(Node target) {
      for (int i = 0; i < index; i++) {
         if (target.equals(list[i]))
            return true;
      }
      return false;
   }

   public int indexOf(Node target) {
      for (int i = 0; i < index; i++) {
         if (target.equals(list[i]))
            return i;
      }
      return -1;
   }

   public Node[] getNodeList() {
      return list;
   }

//   public String toString() {
//      return Arrays.toString(list);
//   }

   public int size() {
      return index;
   }

   public Node get(int target) {
      for (int i = 0; i < index; i++) {
         if (target == i)
            return list[i];
      }
      return null;
   }

   public void removeLast(double c) {
      if (index == 0)
         return;
      list[index - 1] = null;
      index--;
      cost -= c;
   }

}
