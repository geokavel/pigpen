package pigpen;

import java.util.LinkedList;

public abstract class Player {

LinkedList<int[]> moves =  new LinkedList<int[]>();
  
  
/**
 * @param board the game board
 * @param id your playerID. Also, your position in the turn order.
 * @param round which round this is. 
 * @return      an int[] of the form [PenID,FenceID]
 */
  public abstract int[] pick(Board board, int id, int round); 

  
}