package pigpen;

public abstract class Player {
  
  
/**
 * @param board the game board
 * @return      an int[] of the form [PenID,FenceID]
 */
  public abstract int[] pick(Board board, int id); 
  
  
}