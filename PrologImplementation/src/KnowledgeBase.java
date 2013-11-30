import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class KnowledgeBase {
   private Map<String, ArrayList<Character>> rules;
   private Set<Character> facts;
   private int[] key_indexes;   // to implement duplicates in key, e.g. A0, A1

   public KnowledgeBase() {
      rules = new HashMap<String, ArrayList<Character>>();
      facts = new TreeSet<Character>();
      key_indexes = new int[26];
   }
   
   public KnowledgeBase(KnowledgeBase kb){
      rules = new HashMap<String, ArrayList<Character>>();
      facts = new TreeSet<Character>();
      key_indexes = kb.getKeyIndexes();
      for (Character fact : kb.getFacts()){
         facts.add(fact);
      }
      for (Map.Entry<String, ArrayList<Character>> rule : kb.getRules().entrySet()){
         rules.put(rule.getKey(), rule.getValue());
      }
   }

   public KnowledgeBase(File in) {
      rules = new HashMap<String, ArrayList<Character>>();
      facts = new TreeSet<Character>();
      key_indexes = new int[26];
      try {
         Scanner fs = new Scanner(in);
         while (fs.hasNextLine()) {
            Scanner ls = new Scanner(fs.nextLine()).useDelimiter(" :- |,");
            Character ch = null;
            ArrayList<Character>  al = new ArrayList<Character>();
            int count = 0;
            while (ls.hasNext()){
               if (count == 0)
                  ch = ls.next().charAt(0);
               else
                  al.add(ls.next().charAt(0));
               count++;
            }
            if (count == 1)
               this.addFact(ch);
            else
               this.addRule(ch, al);
         }   
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   public void addFact(Character fact) {
      facts.add(fact);
   }

   public void addRule(Character deducted, ArrayList<Character> known) {
      String key = deducted + "" + key_indexes[deducted - 'A'];
      rules.put(key, known);
      key_indexes[deducted - 'A']++;
   }
   
   public Map<String, ArrayList<Character>> getRules(){
      return rules;
   }
   
   public Set<Character> getFacts(){
      return facts;
   }
   
   public int[] getKeyIndexes(){
      return key_indexes;
   }
   
   public String printFacts(){
      StringBuilder sb = new StringBuilder();
      for (Character fact : facts){
         sb.append(fact);
         sb.append(", ");
      }
      sb.delete(sb.length()-2, sb.length());
      return sb.toString();
   }
   
   public String printRule(String key){
      StringBuilder sb = new StringBuilder();
      ArrayList<Character> known = this.getRules().get(key);
      sb.append(key.charAt(0));
      sb.append(" :- ");
      for (Character c : known){
         sb.append(c);
         sb.append(",");
      }
      sb.deleteCharAt(sb.length()-1);
      return sb.toString();
   }
    
   public String toString(){
      StringBuilder sb = new StringBuilder();
      for (Map.Entry<String, ArrayList<Character>> en : rules.entrySet()){
         sb.append(en.getKey().charAt(0));
         sb.append(" :- ");
         for (Character ch : en.getValue()){
            sb.append(ch);
            sb.append(",");
         }
         sb.deleteCharAt(sb.length()-1);
         sb.append("\n");
      }
      for (Character ch : facts){
         sb.append(ch);
         sb.append("\n");
      }
      sb.deleteCharAt(sb.length()-1);
      return sb.toString();
   }

}
