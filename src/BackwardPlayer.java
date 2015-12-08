package pigpen.players;

import pigpen.*;

/**
 * Picks the first available fence in the last available Pen
 */
public class BackwardPlayer extends Player {

	public int[] pick(Board board) {
		for(int i = board.size;i>0;i--) {
			Pen p = board.get(i);
			if(!p.closed()) {
				return p.pick(Pen.TOP);
			}
		}
		return new int[] {1,0};
	}
}