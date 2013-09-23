import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/*
 * Find the optimal path betwee two nodes according to the input, use 3 search
 * methods to implement it.
 */
public class search {

   private static Graph graph;
   private static Path logPath;
   private static Path minPath;
   private static double minPathCost;
   private static boolean isReliability;
   private static boolean isDirected;
   private static final int SIZE = 100;

   public static void main(String[] args) {

      logPath = new Path(SIZE);
      minPath = new Path(SIZE);
      minPathCost = Double.MAX_VALUE;
      isReliability = true;
      isDirected = true;

      Node startNode = null;
      Node goalNode = null;
      String inputFile = "";
      String outputPath = "";
      String outputLog = "";
      int type = 0;
      // deal with argumetns
      try {
         if (args.length != 12)
            throw new Exception();
         type = Integer.parseInt(args[1]);
         startNode = new Node(args[3]);
         goalNode = new Node(args[5]);
         inputFile = args[7];
         outputPath = args[9];
         outputLog = args[11];

      }
      catch (Exception e) {
         System.out.println("Invalid Commands");
      }

      graph = new Graph(SIZE, startNode, !isDirected);
      ReadList(inputFile);

      if (type == 1)
         BFS(startNode, goalNode);
      else if (type == 2)
         DFS(goalNode, new Path(startNode, SIZE));
      else if (type == 3)
         UniformSearch(!isReliability, startNode, goalNode);
      else if (type == 4)
         UniformSearch(isReliability, startNode, goalNode);

      WriteList(minPath, outputPath);
      WriteList(logPath, outputLog);

   }

   /**
    * Breadth First Search in tree version
    * */
   public static void BFS(Node startNode, Node goalNode) {
      PathQueue qu = new PathQueue(SIZE);
      Path root = new Path(startNode, SIZE);
      qu.add(root);
      while (!qu.isEmpty()) {
         Path curr = qu.poll();
         Node tail = curr.getLastNode();
         logPath.addNode(tail);
         // backtrack the minPath
         if (tail.equals(goalNode)) {
            if (curr.cost < minPathCost) {
               minPath = new Path(curr);
               minPathCost = curr.cost;
            }
            continue;
         }
         // check adjacent nodes
         Path adj = graph.getNeighbor(tail);
         for (int i = 0; i < adj.size(); i++) {
            Node nextNode = adj.get(i);
            if (!curr.contains(nextNode)) {
               Path next = new Path(curr);
               next.add(nextNode, graph.getEdgeCost(tail, nextNode));
               qu.add(next);
            }
         }
      }
   }

   /**
    * Depth First Search in tree version
    * 
    * @param pathCost total cost from start node to current node
    * */
   public static void DFS(Node goalNode, Path path) {
      Node tail = path.getLastNode();
      logPath.addNode(tail);
      // backtrack the minPath
      if (tail.equals(goalNode)) {
         if (path.cost < minPathCost) {
            minPathCost = path.cost;
            minPath = new Path(path);
         }
         return;
      }
      // check adjacent nodes
      Path adj = graph.getNeighbor(tail);
      for (int i = 0; i < adj.size(); i++) {
         Node nextNode = adj.get(i);
         if (!path.contains(nextNode)) {
            path.add(nextNode, graph.getEdgeCost(tail, nextNode));
            DFS(goalNode, path);
            path.removeLast(graph.getEdgeCost(tail, nextNode));
         }
      }
   }

   /**
    * Uniform Cost Search in graph version
    * 
    * @param isReliability whether the edge has reliability property
    * */
   public static void UniformSearch(boolean isReliability, Node startNode, Node goalNode) {
      PathQueue open = new PathQueue(SIZE);
      PathQueue closed = new PathQueue(SIZE);
      Path root = new Path(startNode, SIZE);
      open.add(root);
      while (!open.isEmpty()) {
         Path curr = open.getMin();
         Node tail = curr.getLastNode();
         logPath.addNode(tail);
         // backtrack the min path
         if (tail.equals(goalNode)) {
            if (curr.cost < minPathCost) {
               minPath = new Path(curr);
               minPathCost = curr.cost;
            }
            return;
         }
         // check adjacent nodes
         Path adj = graph.getNeighbor(tail);
         for (int i = 0; i < adj.size(); i++) {
            Node nextNode = adj.get(i);
            if (!curr.contains(nextNode)) {
               double addCost = graph.getEdgeCost(tail, nextNode);
               if (isReliability) {
                  if (graph.getEdgeReliability(tail, nextNode) == 0)
                     addCost += 0.5;
               }
               double cost = curr.cost + addCost;
               Path o = open.getPathWithTail(nextNode);
               Path c = closed.getPathWithTail(nextNode);
               if (o == null && c == null) {
                  Path next = new Path(curr);
                  next.add(nextNode, addCost);
                  open.add(next);
               }
               else if (o != null && cost < o.cost) {
                  open.remove(o);
                  Path next = new Path(curr);
                  next.add(nextNode, addCost);
                  open.add(next);
               }
               else if (c != null && cost < c.cost) {
                  closed.remove(c);
                  Path next = new Path(curr);
                  next.add(nextNode, addCost);
                  open.add(next);
               }
            }
         }
         closed.add(curr);
      }
   }

   /*---------------Helper Functions----------------------*/

   /**
    * Read the input
    * */
   public static void ReadList(String inputFile) {
      try {
         BufferedReader in = new BufferedReader(new FileReader(inputFile));
         // read characters line by line
         Node p = null;
         Node q = null;
         double cost = 0.0;
         int reliable = 0;
         int count = 0;
         String tmp = "";
         String line = in.readLine();
         while (line != null && line.length() > 0) {
            for (int i=0; i < line.length();i++){
               char curr = line.charAt(i);
               if (curr != ','){
                  tmp += curr;
               }
               else if (curr == ',' ){
                  if (count == 0)
                     p = new Node(tmp);
                  else if (count == 1)
                     q = new Node(tmp);
                  else if (count == 2) {
                     cost = Double.parseDouble(tmp);
                  }
                  count++;
                  tmp = "";
               }
            }
            reliable = Integer.parseInt(tmp);
            count = 0;
            tmp = "";
            // store node and edges into graph
            if (!graph.contains(p))
               graph.addNode(p);
            if (!graph.contains(q))
               graph.addNode(q);
            // Path pa = graph.getNodeList();
            graph.addEdge(p, q, cost, reliable);
            line = in.readLine();
         }
         in.close();
      }
      catch (Exception e) {
         System.out.println("Error reading file.");
      }
   }

   /**
    * Write the output in format
    * */
   public static void WriteList(Path path, String outputFile) {
      try {
         PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
         String sb = "";
         for (int i = 0; i < path.size(); i++) {
            sb += path.get(i) + "\n";
         }
         out.write(sb);
         //System.out.println(sb);
         out.close();
      }
      catch (Exception e) {
         System.out.println("Error writing file.");
      }
   }
}
