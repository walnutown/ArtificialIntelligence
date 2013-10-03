/**
 * @author taohu
 *
 */
public class Location {
   private double x;
   private double y;

   public Location(){
      this.setX(0);
      this.setY(0);
   }
   
   public Location(double x, double y) {
      this.setX(x);
      this.setY(y);
   }
   
   public Location(Location l){
      this.setX(l.getX());
      this.setY(l.getY());
   }
   
   public boolean equals(Object otherLocation){
      if (otherLocation == null){
         return false;
      }
      if (otherLocation.getClass() != this.getClass()){
         return false;
      }
      Location other = (Location) otherLocation;
      if (this.getX() != other.getX() || this.getY() != this.getY()){
         return false;
      }
      return true;
   }

   public double getX() {
      return x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return y;
   }

   public void setY(double y) {
      this.y = y;
   }
   
   public String toString(){
      return "[ "+ this.getX() + ", " + this.getY() + "]";
   }
   
}
