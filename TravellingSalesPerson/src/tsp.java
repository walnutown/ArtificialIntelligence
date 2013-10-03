import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

public class tsp {
   enum WriteType {
      LOG, PATH
   };

   private static boolean isDirected;
   private static final int SIZE = 100;

   /**
    * @param args
    */
   public static void main(String[] args) {
      // TODO Auto-generated method stub

      isDirected = true;
      Node startNode = null;
      String inputFile = "";
      String outputPath = "";
      String outputLog = "";
      int type = 0;
      // handle command line arguments
      try {
         if (args.length != 10)
            throw new Exception();
         type = Integer.parseInt(args[1]);
         startNode = new Node(args[3]);
         inputFile = args[5];
         outputPath = args[7];
         outputLog = args[9];
      }
      catch (Exception e) {
         System.out.println("Invalid Commands");
      }
      Graph graph = ReadList(inputFile, startNode);           // initialize the graph with input
      ArrayList<State> states = new ArrayList<State>();       // log all the states in search

      if (type == 1)
         states = Greedy(graph);
      else if (type == 2)
         states = SLD(graph);
      //else if (type == 3)
        // states = MST();
      WriteList(states, outputPath, WriteType.PATH);
      WriteList(states, outputLog, WriteType.LOG);
   }
   
   /**
    * Solve TSP using greedy algorithm
    * @param graph the initial map of nodes
    * @return list of all states in each search step
    */
   public static ArrayList<State> Greedy(Graph graph) {
      ArrayList<State> states = new ArrayList<State>();
      Set<Node> unvisited = new HashSet<Node>(graph.getNodeList());
      Node startNode = graph.root;
      unvisited.remove(startNode);
      Node currNode = startNode;
      Node lastNode = null;
      State currState = new State(startNode);
      states.add(currState);
      // if is not the last unvisited node, to find the nearest node
      while (unvisited.size() > 0) { 
         PriorityQueue<State> qu = new PriorityQueue<State>(SIZE, new StateComparator());
         ArrayList<Node> adj = graph.getNeighbor(currNode);
         for (Node neighbor : adj) {
            if (neighbor.equals(startNode) || !unvisited.contains(neighbor))
               continue;
            State tmp = new State(currNode);
            tmp.visitNode(neighbor, 0.0, graph.getEdgeCost(currNode, neighbor));
            qu.add(tmp);
         }
         // get the nearest node, update states list
         State min = qu.poll();
         Node nextNode = min.getLastNode();
         if (unvisited.size() == 1)
            lastNode = nextNode;
         unvisited.remove(nextNode);                 // add the nearest node into visited set
         State nextState = new State(currState);    // update current state
         nextState.visitNode(nextNode, currState.getG() + min.getH(), 0.0);
         currState = nextState;
         currNode = nextNode;
         states.add(currState);
      }
      // when comes to the last node, just connect the start node
      State lastState = new State(currState);
      lastState.visitNode(startNode, currState.getG() + graph.getEdgeCost(startNode, lastNode), 0.0);
      states.add(lastState);
      return states;
   }

   public static ArrayList<State> SLD(Graph graph) {
      ArrayList<State> states = new ArrayList<State>();
      PriorityQueue<State> qu = new PriorityQueue<State>(SIZE, new StateComparator());
      Node rootNode = graph.root;
      Node currNode = rootNode;
      State currState = new State(currNode);
      qu.add(currState);
      while (qu.size() > 0){
         currState = qu.poll();
         states.add(currState);
         currNode = currState.getLastNode();
         ArrayList<Node> visited = currState.getPath();
         boolean isLastNode = true;
         for (Node nextNode : graph.getNeighbor(currNode)){
            if (visited.contains(nextNode))
               continue;
            isLastNode = false;
            State nextState = new State(currState);
            double g = currState.getG() + graph.getEdgeCost(currNode, nextNode);
            double h = graph.getEdgeCost(nextNode, rootNode);
            nextState.visitNode(nextNode, g, h);
            qu.add(nextState);
         }
         if (isLastNode)
            break;
      }
      State lastState = new State(currState);
      lastState.visitNode(rootNode, currState.getG() + graph.getEdgeCost(currNode, rootNode), 0.0);
      states.add(lastState);
      return states;
   }

   public static void MST(Graph graph) {

   }

   /*---------------Helper Functions----------------------*/

   /**
    * Get the distance between two nodes
    */
   public static double getDistance(Node p, Node q) {
      Location pl = p.getLocation();
      Location ql = q.getLocation();
      return Math.sqrt(Math.abs(pl.getX() - ql.getX()) * Math.abs(pl.getX() - ql.getX()) + Math.abs(pl.getY() - ql.getY()) * Math.abs(pl.getY() - ql.getY()));
   }

   /**
    * Read the input
    * */
   public static Graph ReadList(String inputFile, Node startNode) {
      Graph graph = new Graph(SIZE, startNode, !isDirected);
      File inFile = new File(inputFile);
      Scanner in = null;
      try {
         in = new Scanner(inFile);
      }
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      // read characters line by line
      while (in.hasNextLine()) {
         String line = in.nextLine();
         Scanner lineScanner = new Scanner(line);
         lineScanner.useDelimiter(",");
         String val = "";
         double x = 0;
         double y = 0;
         int count = 0;
         while (count <= 2 && lineScanner.hasNext()) {
            String s = lineScanner.next();
            if (count == 0) {
               val = s;
            }
            else if (count == 1) {
               x = Double.parseDouble(s);
            }
            else if (count == 2) {
               y = Double.parseDouble(s);
            }
            count++;
         }
         Node curr = new Node(val, x, y);
         // store node and edges into graph
         if (!graph.contains(curr))
            graph.addNode(curr);
         else {
            graph.setNodeLocation(curr);
         }
         for (Node n : graph.getNodeList()) {
            if (n.equals(curr))
               continue;
            graph.addEdge(curr, n, getDistance(curr, n));
         }
      }
      in.close();
      return graph;
   }

   /**
    * Write the path/log output in format
    * */
   public static void WriteList(ArrayList<State> states, String outputFile, WriteType t) {
      PrintWriter out = null;
      try {
         out = new PrintWriter(outputFile);
      }
      catch (Exception e) {
         e.printStackTrace();
      }
      StringBuilder sb = new StringBuilder();
      // output path
      if (t == WriteType.PATH) {
         State finalState = states.get(states.size() - 1);
         for (Node n : finalState.getPath()) {
            sb.append(n);
            sb.append("\n");
         }
         sb.append("Total Tour Cost: ");
         sb.append(finalState.getG());
      }
      // output log
      else {
         for (State state : states) {
            for (Node n : state.getPath()) {
               sb.append(n);
            }
            sb.append(",");
            sb.append(state.getG());
            sb.append(",");
            sb.append(state.getH());
            sb.append(",");
            sb.append(state.getG() + state.getH());
            sb.append("\n");
         }
      }
      out.println(sb.toString());
      System.out.println(sb.toString());
      out.close();
   }
}
