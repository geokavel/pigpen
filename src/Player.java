package pigpen;

public abstract class Player {
  
  
/**
 * @param board the game board
 * @param id your playerID. Also, your position in the turn order.
 * @return      an int[] of the form [PenID,FenceID]
 */
  public abstract int[] pick(Board board, int id); 
  
  
}