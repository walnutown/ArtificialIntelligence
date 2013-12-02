import java.util.ArrayList;


public class Rule {
   private String implication;
   private ArrayList<Character> conditions;
   
   public Rule(){
      implication = null;
      conditions = new ArrayList<Character>();
   }
   
   public Rule(Rule r){
      implication = r.getImplication();
      conditions = new ArrayList<Character>(r.getConditions());
   }
   
   public Rule(String implication, ArrayList<Character> conditions){
      this.implication = implication;
      this.conditions = new ArrayList<Character>(conditions);
   }
   
   public String getImplication(){
      return implication;
   }
   
   public ArrayList<Character> getConditions(){
      return conditions;
   }
   
   public boolean equals(Object obj){
      if (obj.getClass() != this.getClass())
         return false;
      Rule r = (Rule) obj;
      if (!r.getImplication().equals(this.getImplication()))
         return false;
      for (int i = 0 ; i < r.getConditions().size(); i++){
         if (!r.getConditions().get(i).equals(this.getConditions().get(i)))
            return false;
      }
      return true;
   }
   
   public String toString(){
      StringBuilder sb = new StringBuilder();
      sb.append(implication.charAt(0));
      sb.append(" :- ");
      for (Character c : conditions){
         sb.append(c);
         sb.append(",");
      }
      sb.deleteCharAt(sb.length()-1);
      return sb.toString();
   }
   
}
