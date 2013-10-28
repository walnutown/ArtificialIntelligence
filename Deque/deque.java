package Deque;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

enum Operation {
   pushBack, pushFront, popBack
}

public class deque {

   public static void main(String[] args) {
      ArrayList<Integer> array = initArray(new File("dequeTest.txt"));
      ArrayList<Operation> op = findOperations(array);
      printOperation(op);
   }

   public static ArrayList<Operation> findOperations(ArrayList<Integer> array) {
      ArrayList<Operation> op = new ArrayList<Operation>();
      LinkedList<Integer> ll = new LinkedList<Integer>();
      Counter counter = new Counter(1);
      pushBack(ll, op, counter);
      int index = 0;
      while (counter.getValue() <= array.size()) {
         while (index < array.size() && array.get(index) == ll.peekLast() ) {
            popBack(ll, op);
            index++;
         }
         if (!ll.isEmpty()) {
            if (isAfter(ll.peekLast(), counter.getValue(), array)) {
               pushFront(ll, op, counter);
            }
            else {
               pushBack(ll, op, counter);
            }
         }else{
            pushBack(ll, op, counter);
         }
      }
      // counter == array.size() +1 here  
      while (index < array.size() && array.get(index) == ll.peekLast()) {
         popBack(ll, op);
         index++;
      }
      if (!ll.isEmpty())
         op = null;

      return op;
   }

   public static void pushBack(LinkedList<Integer> ll, ArrayList<Operation> op, Counter counter) {
      ll.add(counter.getValue());
      op.add(Operation.pushBack);
      counter.add();
   }

   public static void pushFront(LinkedList<Integer> ll, ArrayList<Operation> op, Counter counter) {
      ll.addFirst(counter.getValue());
      op.add(Operation.pushFront);
      counter.add();
   }

   public static void popBack(LinkedList<Integer> ll, ArrayList<Operation> op) {
      ll.pollLast();
      op.add(Operation.popBack);
   }

   /**
    * Check if b is after a in the array
    */
   public static boolean isAfter(int a, int b, ArrayList<Integer> array) {
      return array.indexOf(a) < array.indexOf(b);
   }

   /**
    * Read the array from the input file
    */
   public static ArrayList<Integer> initArray(File inFile) {
      Scanner in = null;
      ArrayList<Integer> array = new ArrayList<Integer>();
      try {
         in = new Scanner(inFile);
      }
      catch (FileNotFoundException e) {
         e.printStackTrace();
      }
      while (in.hasNextLine()) {
         Scanner lineScanner = new Scanner(in.nextLine());
         lineScanner.useDelimiter(",");
         while (lineScanner.hasNext()) {
            array.add(lineScanner.nextInt());
         }
      }
      return array;
   }
   /**
    * Print the operations.
    * if there's no valid operations, return "impossible"
    */
   public static void printOperation(ArrayList<Operation> op) {
      StringBuilder sb = new StringBuilder();
      if (op == null)
         sb.append("impossible");
      else {
         for (Operation o : op) {
            sb.append(o);
            sb.append(',');
         }
         sb.deleteCharAt(sb.length() - 1);
      }
      System.out.println(sb.toString());
   }

}
