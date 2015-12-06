package pigpen.players;

import pigpen.*;

import java.util.*;

/**
 * Picks a Pen with only one fence remaining. 
 * Otherwise picks one with the most fences remaining
 */
public class Finisher extends Player implements Comparator<Pen> {


  public int[] pick(Board board) {
  	return Collections.max(board.getList(),this).pick(Pen.TOP);
  	
  }
  
  
  @Override
  public int compare(Pen p1, Pen p2) {
    //1 remaining is best, 4 remaining is second.
    int r1 = p1.remaining();
    int r2 = p2.remaining();
    if(r1 == 1) r1 = 5;
    if(r2 == 1) r2 = 5;
    return Integer.compare(r1,r2);
  }
    
  
}