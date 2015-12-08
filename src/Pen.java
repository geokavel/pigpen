package pigpen;

/**
 * A Pen composed of four fences. Every Pen shares some or all fences with other Pens.
 */

public class Pen  {
    /**
     * Square Mode Top Fence
     */
    public static final int TOP = 0;
     /**
      * Square Mode Right Fence
      */
    public static final int RIGHT = 1;
     /**
      * Square Mode Bottom Fence
      */
    public static final int BOTTOM = 2;
     /**
      * Square Mode Left Fence
      */
    public static final int LEFT = 3;
     /**
      * Hexagon Mode North-East Fence
      */
    public static final int NE = 0;
     /**
      * Hexagon Mode East Fence
      */
    public static final int E = 1;
     /**
      * Hexagon Mode South-East Fence
      */
    public static final int SE = 2;
     /**
      * Hexagon Mode South-West Fence
      */
    public static final int SW = 3;
     /**
      * Hexagon Mode West Fence
      */
    public static final int W = 4;
     /**
      * Hexagon Mode North-West Fence
      */
    public static final int NW = 5;
    
	Board board;
	int[] fences;
	int winner;
	int id;
	public final int row,col;
	int sides; 
	
	Pen(Board board,int id, int sides) {
		this.board = board;
	    this.id = id;
	    if(sides == 4) {
	    row = (id-1)/board.cols;
	    col = (id-1)%board.cols;
	    }
	    else {
	    	int r=0,sum=0,old_sum=0;
	    	while(sum<id) {
	    		old_sum = sum;
	    		sum+=board.rows-Math.abs(r-board.rows/2);
	    		r++;
	    	}
	    	row = r-1;
	    	col = id-1 - old_sum;
	    }
	    this.sides = sides;
	    fences = new int[sides];
	}
	
	/**
	 * Generates a return value for Player.pick() 
	 * @param fence the ID of the fence: TOP, RIGHT, BOTTOM, or LEFT (Square Mode)
	 * or NE, E, SE, SW, W, NW (Hexagon Mode)
	 * @return      a two element array in the form [PenID, FenceID]
	 */
	public int[] pick(int fence) {
		if(fence >= 0 && fence <sides) 
			return new int[]{id,fence};
		return new int[]{id,0};
	}
	/**
	 * Checks if the all the Pen's fences have been filled in.
	 */
	public boolean closed() {
		return winner>0;
	}
	
	

	

	
	/**
	 * Gets the Pen neighboring the given fence
	 * @return a neighboring Pen, or Pen with ID -1 if there is no neighbor sharing
	 * the given fence
	 */
	public Pen n(int fence) {
		if(sides == 4) {
		switch(fence) {
			case TOP : return board.getPenAt(row-1,col);
			case RIGHT : return board.getPenAt(row,col+1);
			case BOTTOM : return board.getPenAt(row+1,col);
			case LEFT : return board.getPenAt(row,col-1);
			default : return board.get(-1);
		}
		}
		switch(fence) {
			case NE : int c = row>board.rows/2?col+1:col;return board.getPenAt(row-1,c);
			case E : return board.getPenAt(row,col+1);
			case SE : c = row<board.rows/2?col+1:col;return board.getPenAt(row+1,c);
			case SW : c = row<board.rows/2?col:col-1;return board.getPenAt(row+1,c);
			case W : return board.getPenAt(row,col-1);
			case NW : c = row>board.rows/2?col:col-1;return board.getPenAt(row-1,c);
			default : return board.get(-1);
		}
	}
	
	/**
	 * Returns a four element array in the form [TOP,RIGHT,BOTTOM,LEFT] (Square Mode)
	 * or [NE,E,SE,SW,W,NW] (Hexagon Mode)
	 * containing the ID's of the players who have filled in the fences.
	 * If no player has filled in a fence, it's value will be 0.
	 */
	public int[] fences() {
		return fences.clone();
	}
	
	/**
	 * Returns the ID of the Pen. The top left Pen has ID 1.
	 */
	public int id() {
		return id;
	}
	
	/**
	 * Returns the winner of the Pen, or zero if it is still open.
	 */
	public int winner() {
		return winner;
	}
	
	/** 
	 * Returns the number of empty fences on this Pen.
	 */
	public int remaining() {
		int r = 0;
		for(int f : fences) {
			if(f == 0) r++;
		}
		return r;
	}
	
	

	@Override
	public String toString() {
		return fences[0]+"^>"+fences[1]+"v"+fences[2]+"<"+fences[3];
	}
	
}