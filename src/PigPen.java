package pigpen;

import pigpen.players.*;

import java.util.*;


public class PigPen {
    static PigPen p;
    static Random r = new Random(74836282);
	Board board;
	int rows, cols;
	ArrayList<Player> players;
	
	
	PigPen(int rows, int cols) {
	    this.rows = rows;
	    this.cols = cols;
		players = new ArrayList<Player>();
	}
	
	public static void main(String[] args) {
        p = new PigPen(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		String[] names = new java.io.File("pigpen/players/").list();
		for(String n : names) {
			String c = "pigpen.players."+n.substring(0,n.indexOf("."));
			try {
			Player player = (Player)Class.forName(c).newInstance();
			p.players.add(player);
			}
			catch(Exception e) {}
		}
		p.board = new Board(p.rows,p.cols, p.players.size());
		
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
			for(int j = 0;j<p.cols;j++) {
				System.out.print(p.board.getPenAt(i,j) + " ");
			}
			System.out.println();
		}
	}
	
	public static int random(int n) {
		return r.nextInt(n);
	}
	
	public static double random() {
		return r.nextDouble();
	}
}