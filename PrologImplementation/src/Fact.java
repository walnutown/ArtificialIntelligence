
public class Fact {
   private Character value;
   
   public Fact(){
      value = null;
   }
   public Fact(Character val){
      value = val;
   }
   
   public Character getVlaue(){
      return value;
   }
   
   public boolean equals(Object obj){
      if (obj.getClass() != this.getClass())
         return false;
      Fact fact = (Fact) obj;
      if (!fact.getVlaue().equals(this.getVlaue()))
         return false;
      return true;
   }
   
   public String toString(){
      return "" + this.getVlaue();
   }
}
