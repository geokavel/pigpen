package pigpen;

/**
 * A Pen composed of four fences. Every Pen shares some or all fences with other Pens.
 */

public class Pen  {
    /**
     * Constant value representing the top fence in a Pen
     */
    public static final int TOP = 0;
     /**
      * Constant value representing the right fence in a Pen
      */
    public static final int RIGHT = 1;
     /**
      * Constant value representing the bottom fence in a Pen
      */
    public static final int BOTTOM = 2;
     /**
      * Constant value representing the left fence in a Pen
      */
    public static final int LEFT = 3;
    
	Board board;
	int[] fences = new int[4];
	int winner;
	int id; 
	
	Pen(Board board,int id) {
		this.board = board;
	    this.id = id;
	}
	
	/**
	 * Generates a return value for Player.pick() 
	 * @param fence the ID of the fence: TOP, RIGHT, BOTTOM, or LEFT
	 * @return      a two element array in the form [PenID, FenceID]
	 */
	public int[] pick(int fence) {
		if(fence >= 0 && fence <4) 
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
	 * Gets the Pen to the left of this one on the Board. 
	 * If there is no Pen to the left, one with ID -1 will be returned.
	 */
	public Pen left() {
			return board.get(id-1);
	}
	
	/**
	 * Gets the Pen to the right of this one on the Board. 
	 * If there is no Pen to the right, one with ID -1 will be returned.
	 */
	public Pen right() {
		return board.get(id+1);
	}
	
	/**
	 * Gets the Pen above this one on the Board. 
	 * If there is no Pen above, one with ID -1 will be returned.
	 */
	public Pen up() {
		return board.get(id - board.cols);
	}
	
	/**
	 * Gets the Pen below this one on the Board. 
	 * If there is no Pen below, one with ID -1 will be returned.
	 */
	public Pen down() {
		return board.get(id + board.cols);
	}
	
	/**
	 * Returns a four element array in the form [TOP,RIGHT,BOTTOM,LEFT]
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