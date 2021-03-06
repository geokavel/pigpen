package pigpen;

import java.util.*;

/**
 * The game board. It is composed of Pens in a grid.
 */

public class Board {
	
	ArrayList<Pen> list;
	
	public final int rows;
	/**
	 * (In Hexagon Mode, this is the number of columns in the shortest row
	 * [equal to the side length of the hexagon].)
	 */
	public final int cols;
	
	/**
	 * 4 in Square Mode, 6 in Hexagon Mode
	 */
	public final int sides;
	/** 
	 * The number of Pens
	 */
	public final int size;
	/**
	 * The number of players
	 */
	public final int players;
	int[] scores;
	PigPen pp;
	
	Board(int sides, int rows, int cols,int players, PigPen pp) {
		this.rows = rows;
		this.cols = cols;
		this.sides = sides;
		this.players = players;
		this.pp = pp;
		size = sides==4 ? rows*cols : cols*(3*(cols-1))+1;
		list = new ArrayList<Pen>(size);
		for(int i = 0;i<size;i++) {
			list.add(new Pen(this,i+1,sides));
		}
		scores = new int[players+1];
	}
	
	/**
	 * Get a list of the given player's moves
	 */
	public LinkedList<int[]> moves(int player) {
		if(player < 1 || player > players) return null;
		LinkedList<int[]> m =  pp.players.get(player-1).moves;
		LinkedList<int[]> copy = new LinkedList<int[]>();
		for(int[] move : m) {
			copy.add(move.clone());
		}
		return copy;
	}
	
	
	
	/**
	 * In Hexagon Mode, use this to see how many columns a given row has
	 */
	public int colsAt(int r) {
		return sides==4 ? cols : rows-Math.abs(r-rows/2);
	}
	
	/**
	 * Returns the Pen at the specified row and column (0-indexed).
	 * If there is no Pen at the specified location, a Pen with ID -1 is returned.
	 */
	public Pen getPenAt(int r, int c) {
		//System.out.println(r +", "+c);
	    if(r < 0 || c < 0 || r >= rows || c >= colsAt(r)) return get(-1);
		if(sides == 4)
			return get(r*rows+c+1);
		
		int sum = 0;
		for(int i = 0;i<r;i++)
			sum += colsAt(i);
		return get(sum + c + 1);
	}
	
	/**
	 * Returns the Pen at the specified ID. Pen ID's start at 1. 
	 * If there is no pen with the given ID, a Pen with ID -1 is returned.
	 */
	public Pen get(int id) {
		if(id > 0 && id <= size) 
			return list.get(id-1);
		return new Pen(this,-1,sides);
	}
	
	/**
	 * Returns a copy of the underlying List of all the Pens.
	 */
	public ArrayList<Pen> getList() {
		return new ArrayList<Pen>(list);
	}
	
	/**
	 * Returns the players' scores for the round. 
	 * Each player's scores is at the index of the player's ID.
	 * The first element is always 0.
	 */
	public int[] scores() {
		return scores.clone();
	}
	
	
	int set(int pen, int fence, int player) {
		if(pen < 1 || pen > size) pen = 1;
		Pen p = get(pen);
		if(p.closed()) {
			fence = 0;
			boolean success = false;
			for(int i = pen+1; i != pen; i++) {
				if(i > size) i = 1;
				if(i == pen) break;
				if(!get(i).closed()) {
					p = get(i);
					success = true;
					break;
				}
			} 
			if(!success) return -1;
		}
		if(fence < 0 || fence >= sides) fence = 0;
		int[] f = p.fences;
		if(f[fence] != 0) {
			for(int i = fence+1;i != fence;i++) {
				if(i >= sides) i = 0;
				if(fence==i) break;
				if(f[i] == 0) {
					fence = i;
					break;
				}
			}
		}
		int score = 0; 
		pp.players.get(player-1).moves.add(new int[] {p.id(),fence});
    	Pen[] both = new Pen[]{p, p.n(fence)};
    	int[] fs = new int[]{fence, (fence + sides/2) % sides};
    	for(int i = 0;i<2;i++) {
    		int[] f2 = both[i].fences;
    		f2[fs[i]] = player;
    		if(PigPen.output && both[i].id != -1) PigPen.out.println("F: " + both[i].id + " " + fs[i] + " " + player);
			if(both[i].remaining() == 0) {
				score = 1;
				both[i].winner = player;
				scores[player]++;
				if(PigPen.output) PigPen.out.println("W: " + both[i].id + " " + player);
			}
		}
		return score;
	}
}