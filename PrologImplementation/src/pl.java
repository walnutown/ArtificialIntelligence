import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class pl {

   public static void main(String[] args) {
      // read command line
      String kb_input_file = "";
      String query_input_file = "";
      String output_entail = "";
      String output_log = "";
      int task = 0;
      try {
         if (args.length != 10)
            throw new Exception();
         task = Integer.parseInt(args[1]);
         kb_input_file = args[3];
         query_input_file = args[5];
         output_entail = args[7];
         output_log = args[9];
      } catch (Exception e) {
         System.out.println("Invalid Commands");
      }

      KnowledgeBase kb = new KnowledgeBase(new File(kb_input_file));
      ArrayList<Character> queries = getQueries(new File(query_input_file));
      System.out.println(kb);
      System.out.println(queries);
      if (task == 1)
         forwardChaining(kb, queries, new File(output_entail), new File(output_log));
      else if (task == 2)
         backwardChaining(kb, queries, new File(output_entail), new File(output_log));

   }

   public static void forwardChaining(KnowledgeBase kb, ArrayList<Character> queries, File output_entail, File output_log) {
      StringBuilder outEntails = new StringBuilder();
      StringBuilder outLogs = new StringBuilder();
      outLogs.append("<Known/Deducted facts>#Rules Fires#NewlyEntailedFacts");
      outLogs.append("\n");
      for (Character q : queries) {
         KnowledgeBase tmp_kb = new KnowledgeBase(kb);
         if (tmp_kb.getFacts().contains(q)) {
            outLogs.append(tmp_kb.printFacts());
            outLogs.append("#");
            outLogs.append("N/A");
            outLogs.append(" # ");
            outLogs.append("N/A");
            outLogs.append("\n");
            outLogs.append("-------------------------------------------------------------");
            outLogs.append("\n");
         } else {
            ArrayList<String> newFacts = new ArrayList<String>();
            boolean end = false;
            do {
               newFacts.clear();
               for (Map.Entry<String, ArrayList<Character>> rule : tmp_kb.getRules().entrySet()) {
                  String deducted = rule.getKey();
                  ArrayList<Character> known = rule.getValue();
                  for (int i = 0; i < known.size(); i++) {
                     if (!tmp_kb.getFacts().contains(known.get(i)))
                        break;
                     if (i == known.size() - 1) {
                        if (!tmp_kb.getFacts().contains(deducted.charAt(0))) {
                           newFacts.add(deducted);
                           if (q.equals(deducted.charAt(0)))
                              end = true;
                        }
                     }
                  }
                  if (end == true)
                     break;
               }
               for (String newFact : newFacts) {
                  outLogs.append(tmp_kb.printFacts());
                  outLogs.append("#");
                  outLogs.append(tmp_kb.printRule(newFact));
                  outLogs.append(" # ");
                  outLogs.append(newFact.charAt(0));
                  outLogs.append("\n");
                  tmp_kb.addFact(newFact.charAt(0));
               }
               if (end == true)
                  break;
            } while (!newFacts.isEmpty());
         }
         if (tmp_kb.getFacts().contains(q)) {
            outEntails.append("YES");
         } else
            outEntails.append("NO");
         outEntails.append("\n");
         outLogs.append("-------------------------------------------------------------");
         outLogs.append("\n");
      }
      System.out.println(outEntails);
      System.out.println(outLogs);
      write(output_entail, outEntails.toString());
      write(output_log, outLogs.toString());
   }

   public static void backwardChaining(KnowledgeBase kb, ArrayList<Character> queries, File output_entail, File output_log) {
      StringBuilder outEntails = new StringBuilder();
      StringBuilder outLogs = new StringBuilder();
      outLogs.append("<Queue of Goals>#Relevant Rules/Fact#New Goal Introduced");
      outLogs.append("\n");
      for (Character q : queries) {
         Set<String> usedRules = new HashSet<String>();
         boolean entailed = bcHelper(kb, q, outLogs, usedRules);
         if (entailed == true)
            outEntails.append("YES");
         else
            outEntails.append("NO");
         outEntails.append("\n");
         outLogs.append("-------------------------------------------------------------");
         outLogs.append("\n");
      }
      System.out.println(outEntails);
      System.out.println(outLogs);
      write(output_entail, outEntails.toString());
      write(output_log, outLogs.toString());
   }

   public static boolean bcHelper(KnowledgeBase kb, Character q, StringBuilder outLogs, Set<String> usedRules) {
      // if query is in the facts of kb
      if (kb.getFacts().contains(q)) {
         outLogs.append(q);
         outLogs.append(" # ");
         outLogs.append(q);
         outLogs.append(" # ");
         outLogs.append("N/A");
         outLogs.append("\n");
         return true;
      }  // no related rules about goal
      else if (!kb.getRules().containsKey(q + "0")) {
         outLogs.append(q);
         outLogs.append(" # ");
         outLogs.append("N/A");
         outLogs.append(" # ");
         outLogs.append("N/A");
         outLogs.append("\n");
         return false;
      } else {
         int[] keyIndexes = kb.getKeyIndexes();
         boolean entailed_total = false;
         for (int i = 0; i < keyIndexes[q - 'A']; i++) {
            String key = q + "" + i;
            if (usedRules.contains(key)) {
               outLogs.append(q);
               outLogs.append(" # ");
               outLogs.append("CYCLE DETECTED");
               outLogs.append(" # ");
               outLogs.append("N/A");
               outLogs.append("\n");
               return false;
            } else {
               usedRules.add(key);
               outLogs.append(q);
               outLogs.append(" # ");
               outLogs.append(kb.printRule(key));
               outLogs.append(" # ");
               for (Character ch : kb.getRules().get(key)) {
                  outLogs.append(ch);
                  outLogs.append(", ");
               }
               outLogs.delete(outLogs.length() - 2, outLogs.length());
               outLogs.append("\n");
               boolean entailed = true;
               for (Character ch : kb.getRules().get(key)) {
                  entailed = entailed && bcHelper(kb, ch, outLogs, usedRules);
               }
               entailed_total = entailed_total || entailed;
            }
         }
         return entailed_total;
      }
   }

   public static void addGoal(Character query, KnowledgeBase kb, Queue<String> goals) {
      // there may be multiple rules related to the goal
      int[] keyIndexes = kb.getKeyIndexes();
      for (int i = 0; i <= keyIndexes[query - 'A']; i++) {
         String key = query + "" + i;
         goals.add(key);
      }
   }

   /**
    * Get the input queries
    * 
    * @return arrayList of the queries
    */
   public static ArrayList<Character> getQueries(File f) {
      ArrayList<Character> queries = new ArrayList<Character>();
      try {
         Scanner fs = new Scanner(f);
         while (fs.hasNextLine()) {
            queries.add(fs.nextLine().charAt(0));
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      return queries;
   }

   /**
    * Write String to file
    */
   public static void write(File f, String s) {
      try {
         PrintWriter pw = new PrintWriter(f);
         pw.write(s);
         pw.close();
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

}
