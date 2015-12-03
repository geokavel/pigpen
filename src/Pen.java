package pigpen;

public class Pen  {
    public static final int TOP = 0;
    public static final int RIGHT = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    
	Board board;
	int[] fences = new int[4];
	int winner;
	int id; 
	
	Pen(Board board,int id) {
		this.board = board;
	    this.id = id;
	}
	
	public int[] pick(int fence) {
		if(fence >= 0 && fence <4) 
			return new int[]{id,fence};
		return new int[]{id,0};
	}
	
	public boolean closed() {
		return winner>0;
	}
	
	public Pen left() {
			return board.get(id-1);
	}
	
	public Pen right() {
		return board.get(id+1);
	}
	public Pen up() {
		return board.get(id - board.cols);
	}
	public Pen down() {
		return board.get(id + board.cols);
	}
	
	public int[] fences() {
		return fences.clone();
	}
	
	public int id() {
		return id;
	}
	public int winner() {
		return winner;
	}
	
	

	@Override
	public String toString() {
		return fences[0]+"^>"+fences[1]+"v"+fences[2]+"<"+fences[3];
	}
	
}