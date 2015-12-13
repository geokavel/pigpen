package pigpen;

import java.util.*;


public class NPlayer {
	
	
	public static void main(String[] args) throws Exception {
		ArrayList<Class> players = PigPen.allPlayers();
		HashMap<Class,Integer> scores = new HashMap<Class,Integer>();
		for(Class p : players) {
			scores.put(p,0);
		}
		
		PigPen p = new PigPen(args,players.toArray(new Class[0]));
		for(int r = 0;r<players.size();r++) { 
			HashMap<Class,Integer> s = p.play(""+r+1);
			System.out.println();
		for(Map.Entry<Class,Integer> score : s.entrySet()) {
			Class player = score.getKey();
			int oldScore = scores.get(player);
			int newScore = oldScore + score.getValue();
			scores.put(player, newScore);
			System.out.println(player.getSimpleName()+": "+oldScore+"+"+score.getValue()+"="+newScore);	
		} 
		
		}
		
		PigPen.printRankings(PigPen.rank(scores));
		
		
	}
	
	
}