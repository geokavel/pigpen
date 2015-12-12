package pigpen;

import java.util.*;


public class NPlayer {
	
	
	public static void main(String[] args) throws Exception {
		ArrayList<Player> players = PigPen.allPlayers();
		HashMap<Player,Integer> scores = new HashMap<Player,Integer>();
		for(Player p : players) {
			scores.put(p,0);
		}
		
		PigPen p = new PigPen(args,players.toArray(new Player[0]));
		for(int r = 0;r<players.size();r++) { 
			HashMap<Player,Integer> s = p.play(""+r+1);
			System.out.println();
		for(Map.Entry<Player,Integer> score : s.entrySet()) {
			Player player = score.getKey();
			int oldScore = scores.get(player);
			int newScore = oldScore + score.getValue();
			scores.put(player, newScore);
			System.out.println(player.getClass().getSimpleName()+": "+oldScore+"+"+score.getValue()+"="+newScore);	
		} 
		
		}
		
		PigPen.printRankings(PigPen.rank(scores));
		
		
	}
	
	
}