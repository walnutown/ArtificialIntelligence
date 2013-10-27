/*
 * Move is an operation on the board, with coordiante and value
 */
public class Move {
   private int x;
   private int y;
   private char value;
   
   public Move(){
      this.setX(Integer.MAX_VALUE);
      this.setY(Integer.MAX_VALUE);
      this.setValue('*');
   }
   
   public Move(int x, int y, char value){
      this.setX(x);
      this.setY(y);
      this.setValue(value);
   }
   
   public void copyMove(Move m){
      this.setX(m.getX());
      this.setY(m.getY());
      this.setValue(m.getValue());
   }

   public int getX() {
      return x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public char getValue() {
      return value;
   }

   public void setValue(char value) {
      this.value = value;
   }
   public String toString(){
      return "(" + this.getX() + "," + this.getY() + ")";
   }
   
}
