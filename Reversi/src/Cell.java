/*
 * Cell is the basic element on the board, with a pair of coordinates and value
 */
public class Cell {
   private int x;
   private int y;
   private char value;

   public Cell() {
      this.x = Integer.MAX_VALUE;
      this.y = Integer.MAX_VALUE;
      this.setValue('*');
   }

   public Cell(int x, int y) {
      this.x = x;
      this.y = y;
      this.setValue('*');
   }

   public Cell(int x, int y, char value) {
      this.x = x;
      this.y = y;
      this.setValue(value);
   }

   public Cell(Cell c) {
      this.x = c.getX();
      this.y = c.getY();
      this.setValue(c.getValue());
   }

   public void copy(Cell c) {
      this.x = c.getX();
      this.y = c.getY();
      this.setValue(c.getValue());
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public char getValue() {
      return value;
   }

   public void setValue(char value) {
      this.value = value;
   }

   public String toString() {
      char x = (char) (this.getX() + 1 + '0');
      char y = (char) ('a' + this.getY());
      return y +""+ x ;
   }

}
