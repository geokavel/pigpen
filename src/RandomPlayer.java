package pigpen.players;

import pigpen.*;


/** 
 * Picks the first available fence in a random Pen 
 */
public class RandomPlayer extends Player {
	public int[] pick(Board board) {
		int id = PigPen.random(board.size)+1;
		return board.get(id).pick(Pen.TOP);
	}
}