import java.util.HashSet;
import java.util.Set;


public class Resolvent {
   private Clause result;
   private Set<Clause> conditions;
   
   public Resolvent(){
      result = new Clause();
      conditions = new HashSet<Clause>();
   }
   
   public Resolvent(Clause result, Clause clause1, Clause clause2){
      this.result = new Clause(result);
      conditions = new HashSet<Clause>();
      conditions.add(clause1);
      conditions.add(clause2);
   }
   
   public Clause getResult(){
      return result;
   }
   
   public Set<Clause> getConditions(){
      return conditions;
   }
   
   public boolean equals(Object obj){
      if (obj.getClass() == this.getClass())
         return false;
      Resolvent res = (Resolvent) obj;
      if (!res.getResult().equals(this.getResult()))
         return false;
      if (!res.getConditions().equals(this.getConditions()))
         return false;
      return true;
   }
}
