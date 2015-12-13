
package pigpen.players;

import pigpen.*;

/**
 * Picks the first available fence in the first available Pen
 */ 
public class LinearPlayer extends Player {

	
	@Override
	public int[] pick(Board board, int id, int round) {
		for(int p = 1;p<=board.size;p++) {
			Pen pen = board.get(p);
				if(!pen.closed()) {
					int[] fences = pen.fences();
						for(int i =0;i<fences.length;i++) {
							if(fences[i] == 0) {
								return new int[]{pen.id(),i};
							}
						}
					}
			}
		return new int[]{1,0};
		} 
	}