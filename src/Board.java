package pigpen;

import java.util.*;

public class Board {
	
	ArrayList<Pen> list;
	public final int rows,cols;
	public final int size;
	final int[] scores;
	
	Board(int rows, int cols, int players) {
		this.rows = rows;
		this.cols = cols;
		list = new ArrayList<Pen>(rows*cols);
		for(int i = 0;i<rows*cols;i++) {
			list.add(new Pen(this,i+1));
		}
		size = list.size();
		scores = new int[players+1];
	}
	
	public Pen getPenAt(int x, int y) {
		return list.get(x*rows+y);
	}
	
	public Pen get(int id) {
		if(id > 0 && id <= size) 
			return list.get(id-1);
		return new Pen(this,-1);
	}
	
	public ArrayList<Pen> getList() {
		return new ArrayList<Pen>(list);
	}
	
	public int[] scores() {
		return scores.clone();
	}
	
	boolean set(int pen, int fence, int player) {
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
			if(!success) return success;
		}
		int[] f = p.fences;
		if(fence < 0 || fence > 3 || f[fence] != 0) {
			for(int i = fence+1;i != fence;i++) {
				if(i > 3) i = 0;
				if(fence==i) break;
				if(f[i] == 0) {
					fence = i;
					break;
				}
			}
		}
		Pen[] neighbors = new Pen[]{p.up(),p.right(),p.down(),p.left()};
    	Pen[] both = new Pen[]{p, neighbors[fence]};
    	int[] fs = new int[]{fence, (fence + 2) % 4};
    	for(int i = 0;i<2;i++) {
    		int[] f2 = both[i].fences;
    		f2[fs[i]] = player;
			if(f2[0]*f2[1]*f2[2]*f2[3] != 0) {
				both[i].winner = player;
				scores[player]++;
			}
		}
		return true;
	}
}