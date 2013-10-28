package Deque;

public class Counter {
   private int count;
   public Counter(){
      count = 0;
   }
   public Counter(int c){
      count = c;
   }
   public int getValue() {
      return count;
   }
   public void add(){
      count++;
   }
   public void add(int i){
      count += i;
   }
   
   public String toString(){
      return "" + count;
   }
}
