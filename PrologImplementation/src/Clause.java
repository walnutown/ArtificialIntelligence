import java.util.ArrayList;
import java.util.Collections;

public class Clause {
   private ArrayList<Character> clause;

   public Clause() {
      clause = new ArrayList<Character>();
   }

   public Clause(ArrayList<Character> al) {
      clause = new ArrayList<Character>(al);
   }

   public Clause(Clause c) {
      clause = new ArrayList<Character>(c.getLiterals());
   }

   public void add(Character literal) {
      if (!clause.contains(literal))
         clause.add(literal);
   }

   public void addAll(Clause c) {
      for (Character literal : c.getLiterals()) {
         if (!this.contains(literal))
            this.add(literal);
      }
   }

   public void remove(Character literal) {
      this.getLiterals().remove(literal);
   }

   public ArrayList<Character> getLiterals() {
      return clause;
   }

   public int size() {
      return this.getLiterals().size();
   }

   public Character get(int index) {
      return this.getLiterals().get(index);
   }

   public boolean contains(Character literal) {
      return this.getLiterals().contains(literal);
   }
   
   public void sort(){
      Collections.sort(this.getLiterals(), new LiteralComparator());
   }

   public boolean equals(Object obj) {
      if (obj.getClass() != this.getClass())
         return false;
      Clause c = (Clause) obj;
      if (c.getLiterals().size() != this.getLiterals().size())
         return false;
      for (int i = 0; i < c.size(); i++) {
         if (!c.get(i).equals(this.get(i)))
            return false;
      }
      return true;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      if (this.size() == 0)
         return "Empty";
      for (int i = 0; i < this.size(); i++) {
         Character literal = this.get(i);
         if (Character.isLowerCase(literal)) {
            sb.append("-");
            sb.append(Character.toUpperCase(literal));
         } else {
            sb.append(literal);
         }
         sb.append(" OR ");
      }
      sb.delete(sb.length() - 4, sb.length());
      return sb.toString();
   }
}
