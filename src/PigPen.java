package pigpen;

import pigpen.players.*;

import java.util.*;
import java.io.*;

public class PigPen implements Comparator<Map.Entry<Player,Integer>> {
    static PigPen p;
    static Random r = new Random(74836282);
    
    static PrintWriter out;
    
	Board board;
	int rows, cols,sides;
	static ArrayList<Player> players;
	static HashMap<Player,Integer> scores;
	
	static boolean output;
	
	
	PigPen(int players, String[] args) {
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
	    board = new Board(sides,rows,cols,players);
	}
	
	public static void main(String[] args) throws Exception {
		players = new ArrayList<Player>();
		scores = new HashMap<Player,Integer>();
		String[] names = new java.io.File("pigpen/players/").list();
		for(String n : names) {
			String c = "pigpen.players."+n.substring(0,n.indexOf("."));
			Player player = (Player)Class.forName(c).newInstance();
			players.add(player);
			scores.put(player,0);
		}
		Collections.shuffle(players,r);
		output = Boolean.parseBoolean(args[args.length-1]);
		for(int r = 0;r<players.size();r++) { 
		Collections.rotate(players,1);
		p = new PigPen(players.size(),args);
		if(output) {
			out = new PrintWriter("out"+(r+1)+".txt");
			out.print("P: "); 
			for(Player player : players) {
				out.print(player.getClass().getSimpleName() + " ");
			}
			out.println();
			out.println("B: " + p.rows + " " + p.cols + " " + players.size());
		}
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
			if(output) {
				System.out.println(pick[0] + " " + pick[1]);
			}
			if(!p.board.set(pick[0],pick[1],i+1)) {
				over = true;
				break;
				}
			if(output) {
			p.drawBoard();
			}
			}
			}
			int[] s = p.board.scores;
			System.out.println();
		for(int i = 1;i<s.length;i++) {
			Player player = players.get(i-1);
			int oldScore = scores.get(player);
			int newScore = oldScore + s[i];
			scores.put(player, newScore);
			System.out.println(player.getClass().getSimpleName()+" ("+i+") : "+oldScore+"+"+s[i]+"="+newScore);
			
		}
		if(output) {
		out.flush();
		out.close();
		}
		}
		ArrayList<Map.Entry<Player,Integer>> rankings = new ArrayList<Map.Entry<Player,Integer>>(scores.entrySet());
		Collections.sort(rankings,p);
		System.out.println();
		for(Map.Entry<Player,Integer> rank : rankings) {
			System.out.println(rank.getKey().getClass().getSimpleName()+": "+rank.getValue());
		}
		
	}
	
	@Override
	public int compare(Map.Entry<Player,Integer> a, Map.Entry<Player,Integer> b) {
		return -a.getValue().compareTo(b.getValue());
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