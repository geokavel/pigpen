package pigpen;

import pigpen.players.*;

import java.util.*;


public class PigPen {
    static PigPen p;
    static Random r = new Random(74836282);
	Board board;
	int rows, cols,sides;
	ArrayList<Player> players;
	
	
	PigPen(String[] args) {
		sides = Integer.parseInt(args[0]);
		if(sides == 4) {
	    	rows = Integer.parseInt(args[1]);
	    	cols = Integer.parseInt(args[2]);
	    }
	    else if(sides == 6) {
	    	int length = Integer.parseInt(args[1]);
	    	rows = 2*length-1;
	    	cols = length;	
	    }
	    board = new Board(sides,rows,cols);
		players = new ArrayList<Player>();
	}
	
	public static void main(String[] args) {
        p = new PigPen(args);
		String[] names = new java.io.File("pigpen/players/").list();
		for(String n : names) {
			String c = "pigpen.players."+n.substring(0,n.indexOf("."));
			try {
			Player player = (Player)Class.forName(c).newInstance();
			p.players.add(player);
			}
			catch(Exception e) {}
		}
		p.board.setPlayers(p.players.size());
		/*Scanner s = new Scanner(System.in);
		while(true) {
			System.out.println(p.board.get(s.nextInt()).n(s.nextInt()).id());
		}*/
		
		boolean over = false;
			while(!over) {
		for(int i = 0;i<p.players.size();i++) {
			Player pl = p.players.get(i);
			int[] pick = pl.pick(p.board);
			if(pick == null || pick.length < 2) 
				pick = new int[]{1,0};
			//Scanner s = new Scanner(System.in);
			//int[] pick = new int[]{s.nextInt(),s.nextInt()};
			System.out.println(pick[0] + " " + pick[1]);
			if(!p.board.set(pick[0],pick[1],i+1)) {
				over = true;
				break;
				}
			p.drawBoard();
			}
			}
		for(int i : p.board.scores) {
			System.out.println(i);
		}	

	}
	
	public void drawBoard() {
	for(int i =0;i<p.rows;i++) {
		int cols = p.board.colsAt(i);
			for(int s = 0;s<p.rows-cols;s++) System.out.print(" ");
			for(int j = 0;j<cols;j++) {
				System.out.print(p.board.getPenAt(i,j).winner() + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * @return a random int from 0 (inclusive) to n (exclusive).
	 */
	public static int random(int n) {
		return r.nextInt(n);
	}
	
	/**
	 * @return a random double from 0 (inclusive) to 1 (exclusive).
	 */
	public static double random() {
		return r.nextDouble();
	}
}