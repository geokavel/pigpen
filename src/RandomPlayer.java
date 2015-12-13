package pigpen.players;

import pigpen.*;


/** 
 * Picks the first available fence in a random Pen 
 */
public class RandomPlayer extends Player {
	public int[] pick(Board board, int id, int round) {
		int pen = PigPen.random(board.size)+1;
		return board.get(pen).pick(Pen.TOP);
	}
}