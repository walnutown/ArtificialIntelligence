import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class KnowledgeBase {
   // use arraylsit here in order to keep consistent with sample output
   private ArrayList<Rule> rules;
   private ArrayList<Character> facts;
   private int[] rule_indexes;   // to solve conflicts in key, e.g. A0, A1
   private ArrayList<Clause> clauses;

   public KnowledgeBase() {
      rules = new ArrayList<Rule>();
      facts = new ArrayList<Character>();
      rule_indexes = new int[26];
      clauses = new ArrayList<Clause>(); 
   }
   
   public KnowledgeBase(KnowledgeBase kb){
      rules = new ArrayList<Rule>();
      facts = new ArrayList<Character>();
      rule_indexes = new int[26];
      clauses = new ArrayList<Clause>(); 
      for (Character fact : kb.getFacts()){
         facts.add(fact);
      }
      for (Rule rule : kb.getRules()){
         rules.add(new Rule(rule));
      }
      for (Clause clause : kb.getClauses()){
         clauses.add(new Clause(clause));
      }
   }

   public KnowledgeBase(File in) {
      rules = new ArrayList<Rule>();
      facts = new ArrayList<Character>();
      rule_indexes = new int[26];
      clauses = new ArrayList<Clause>(); 
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
      convertKBToClauses();
   }
   
   
   /**
    * Add a fact to the knowledge base
    */
   public void addFact(Character fact) {
      facts.add(fact);
   }
   
   public boolean containsFact(Character fact){
      return this.getFacts().contains(fact);
   }
   
   public ArrayList<Character> getFacts(){
      return facts;
   }
   
   /**
    * Add a rule to the knowledge base
    */
   public void addRule(Character implication, ArrayList<Character> conditions) {
      String key = implication + "" + rule_indexes[implication - 'A'];
      Rule rule = new Rule(key, conditions);
      this.addRule(rule);
      rule_indexes[implication - 'A']++;
   }
   
   public void addRule(Rule r){
      if (!this.containsRule(r))
         this.getRules().add(r);
   }
   
   public boolean containsRule(Rule r){
      return this.getRules().contains(r);
   }
   
   public ArrayList<Rule> getRules(){
      return rules;
   }
   
   public Rule getRule(String implication){
      ArrayList<Rule> rl = this.getRules();
      for (int i = 0; i < rl.size(); i++){
         if (rl.get(i).getImplication().equals(implication))
            return rl.get(i);
      }
      return null;
   }
    
   /**
    * Convert knowledge base to clauses
    * lowercase denotes negation, e.g. a denotes -A
    */
   public void convertKBToClauses(){
      for (Rule rule : this.getRules()){
         Clause clause = new Clause();
         for (Character condition : rule.getConditions()){
            clause.add(Character.toLowerCase(condition));
         }
         clause.add(rule.getImplication().charAt(0));
         this.addClause(clause);
      }
      for (Character fact : this.getFacts()){
         Clause clause = new Clause();
         clause.add(fact);
         this.addClause(clause);
      } 
   }
   
   /**
    * Add a clause to the knowledge base
    */
   public void addClause(Clause c){
      if (!this.containsClause(c))
         this.getClauses().add(c);
   }
   
   public boolean containsClause(Clause c){
      return this.getClauses().contains(c);
   }
   
   public ArrayList<Clause> getClauses(){
      return clauses;
   }
    
   public int[] getKeyIndexes(){
      return rule_indexes;
   }
   
   /**
    * Print all facts in the knowledge base
    */
   public String printFacts(){
      StringBuilder sb = new StringBuilder();
      for (Character fact : this.getFacts()){
         sb.append(fact);
         sb.append(", ");
      }
      sb.delete(sb.length()-2, sb.length());
      return sb.toString();
   }
    
   public String toString(){
      StringBuilder sb = new StringBuilder();
      for (Rule rule : this.getRules()){
         sb.append(rule);
         sb.append("\n");
      }
      for (Character fact : this.getFacts()){
         sb.append(fact);
         sb.append("\n");
      }
      sb.deleteCharAt(sb.length()-1);
      return sb.toString();
   }

}
