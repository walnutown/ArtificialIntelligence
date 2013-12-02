import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class pl {

   public static void main(String[] args) {
      long lStartTime = System.currentTimeMillis();
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
      else
         resolution(kb, queries, new File(output_entail), new File(output_log));

      long lEndTime = System.currentTimeMillis();
      System.out.println("");
      System.out.println("Execution Time (milliseconds): " + (lEndTime - lStartTime));
   }

   /**
    * Forward Chaining
    */
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
            ArrayList<String> new_facts = new ArrayList<String>();
            boolean end = false;
            do {
               new_facts.clear();
               for (Rule rule : tmp_kb.getRules()) {
                  String implication = rule.getImplication();
                  ArrayList<Character> conditions = rule.getConditions();
                  for (int i = 0; i < conditions.size(); i++) {
                     if (!tmp_kb.containsFact(conditions.get(i)))
                        break;
                     if (i == conditions.size() - 1) {
                        if (!tmp_kb.containsFact(implication.charAt(0))) {
                           new_facts.add(implication);
                           if (q.equals(implication.charAt(0)))
                              end = true;
                        }
                     }
                  }
                  if (end == true)
                     break;
               }
               for (String new_fact : new_facts) {
                  outLogs.append(tmp_kb.printFacts());
                  outLogs.append("#");
                  outLogs.append(tmp_kb.getRule(new_fact));
                  outLogs.append(" # ");
                  outLogs.append(new_fact.charAt(0));
                  outLogs.append("\n");
                  tmp_kb.addFact(new_fact.charAt(0));
               }
               if (end == true)
                  break;
            } while (!new_facts.isEmpty());
         }
         if (tmp_kb.containsFact(q)) {
            outEntails.append("YES");
         } else
            outEntails.append("NO");
         outEntails.append("\n");
         outLogs.append("-------------------------------------------------------------");
         outLogs.append("\n");
      }
      // System.out.println(outEntails);
      // System.out.println(outLogs);
      write(output_entail, outEntails.toString());
      write(output_log, outLogs.toString());
   }

   /**
    * Backward Chaining
    */
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
      // System.out.println(outEntails);
      // System.out.println(outLogs);
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
      else if (kb.getRule(q + "0") == null) {
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
               outLogs.append(kb.getRule(key));
               outLogs.append(" # ");
               for (Character condition : kb.getRule(key).getConditions()) {
                  outLogs.append(condition);
                  outLogs.append(", ");
               }
               outLogs.delete(outLogs.length() - 2, outLogs.length());
               outLogs.append("\n");
               boolean entailed = true;
               for (Character condition : kb.getRule(key).getConditions()) {
                  entailed = entailed && bcHelper(kb, condition, outLogs, usedRules);
               }
               entailed_total = entailed_total || entailed;
            }
         }
         return entailed_total;
      }
   }

   /**
    * Resolution
    */
   public static void resolution(KnowledgeBase kb, ArrayList<Character> queries, File output_entail, File output_log) {
      StringBuilder outEntails = new StringBuilder();
      StringBuilder outLogs = new StringBuilder();
      outLogs.append("Resolving clause 1#Resolving clause 2#Added clause");
      outLogs.append("\n");
      for (Character q : queries) {
         if (resolutionHelper(kb, q, outLogs) == true)
            outEntails.append("YES");
         else
            outEntails.append("NO");
         outEntails.append("\n");
         outLogs.append("-------------------------------------------------------------");
         outLogs.append("\n");
      }
      // System.out.println(outEntails);
      // System.out.println(outLogs);
      write(output_entail, outEntails.toString());
      write(output_log, outLogs.toString());
   }

   public static boolean resolutionHelper(KnowledgeBase kb, Character query, StringBuilder outLogs) {
      ArrayList<Clause> clauses = new ArrayList<Clause>(kb.getClauses()); // for output
      Set<Clause> clauses_set = new HashSet<Clause>(kb.getClauses()); // to improve time complexity
      Clause clause = new Clause();
      clause.add(Character.toLowerCase(query));
      clauses.add(clause);
      clauses_set.add(clause);
      ArrayList<Resolvent> new_resolvents = new ArrayList<Resolvent>();
      int count = 1;
      while (true) {
         outLogs.append("ITERATION = ");
         outLogs.append(count);
         outLogs.append("\n");
         for (int i = 0; i < clauses.size(); i++) {
            for (int j = i + 1; j < clauses.size(); j++) {
               Clause c1 = clauses.get(i);
               Clause c2 = clauses.get(j);
               Set<Clause> conditions = new HashSet<Clause>();
               conditions.add(c1);
               conditions.add(c2);
               boolean duplicate = false;
               for (Resolvent res : new_resolvents) {
                  if (res.containsConditions(conditions))
                     duplicate = true;
               }
               // avoid duplicate resolving
               if (duplicate == true)
                  continue;
               ArrayList<Resolvent> resolvents = resolve(c1, c2, outLogs);
               for (Resolvent res : resolvents) {
                  if (res.getResult().size() == 0)
                     return true;
                  else {
                     if (!new_resolvents.contains(res))
                        new_resolvents.add(res);
                  }
               }
            }
         }
         ArrayList<Clause> new_clauses = new ArrayList<Clause>();
         for (Resolvent res : new_resolvents) {
            new_clauses.add(res.getResult());
         }
         // Check whether clauses cl1 is a subset of clauses cl2
         boolean isSubset = true;
         for (Clause c : new_clauses) {
            if (!clauses_set.contains(c)) {
               isSubset = false;
               clauses.add(c);
               clauses_set.add(c);
            }
         }
         if (isSubset == true)
            return false;
         new_resolvents.clear();
         count++;
      }
   }

   /**
    * Resolve two clauses
    * 
    * @return mapping of clauses and resolvent
    */
   public static ArrayList<Resolvent> resolve(Clause c1, Clause c2, StringBuilder outLogs) {
      ArrayList<Resolvent> resolvents = new ArrayList<Resolvent>();
      Clause result = new Clause();
      int resolved = 0;
      // check if the two clauses can be resolved
      for (int i = 0; i < c1.size(); i++) {
         Character literal = c1.get(i);
         Character neg = Character.isLowerCase(literal) ? Character.toUpperCase(literal) : Character.toLowerCase(literal);
         if (c2.contains(neg))
            resolved++;
         else
            result.add(literal);
      }
      // resolved == 0 or resolved >1 should be dropped
      if (resolved == 1) {
         for (int i = 0; i < c2.size(); i++) {
            Character literal = c2.get(i);
            Character neg = Character.isLowerCase(literal) ? Character.toUpperCase(literal) : Character.toLowerCase(literal);
            if (!c1.contains(neg) && !c1.contains(literal)) {
               result.add(literal);
            }
         }
         outLogs.append(c1);
         outLogs.append(" # ");
         outLogs.append(c2);
         outLogs.append(" # ");
         result.sort();
         outLogs.append(result);
         outLogs.append("\n");
         resolvents.add(new Resolvent(result, c1, c2));
      }
      return resolvents;
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
