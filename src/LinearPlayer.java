
package pigpen.players;

import pigpen.*;

public class LinearPlayer extends Player {

	
	@Override
	public int[] pick(Board board) {
		for(int id = 1;id<=board.size;id++) {
			Pen pen = board.get(id);
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