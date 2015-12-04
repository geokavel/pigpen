package pigpen.players;

import pigpen.*;

public class RandomPlayer extends Player {
	public int[] pick(Board board) {
		int id = PigPen.random(board.size)+1;
		return board.get(id).pick(Pen.TOP);
	}
}