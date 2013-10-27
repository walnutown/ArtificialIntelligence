/*
 * Coordinate is a pair of integer values, representing location on the board
 */
public class Coordinate {
   private int x;
   private int y;
   private char value;
   private boolean isLight;
   
   public Coordinate(){
      this.setX(0);
      this.setY(0);
      this.setValue('*');
      this.setLight(false);
   }
    
   public Coordinate(int x, int y){
      this.setX(x);
      this.setY(y);
      this.setValue('*');
   }
   
   public Coordinate (Coordinate c){
      this.setX(c.getX());
      this.setY(c.getY());
      this.setValue(c.getValue());
      this.setLight(c.isLight());
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
   
   public String toString(){
      return "["+ this.getX() + ", " + this.getY() + "]";
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (this.getClass() != obj.getClass())
         return false;
      Coordinate c = (Coordinate) obj;
      if (c.getX() == this.getX() && c.getY() == this.getY())
         return true;
      return false;
   }

   public boolean isLight() {
      return isLight;
   }

   public void setLight(boolean isLight) {
      this.isLight = isLight;
      if (isLight == true)
         this.setValue('@');
   }

   public char getValue() {
      return value;
   }

   public void setValue(char value) {
      this.value = value;
   }
   
   
}
