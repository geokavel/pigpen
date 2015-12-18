package pigpen;

import java.util.*;

public class Tournament {
	public static void main(String[] args) throws Exception {
		HashMap<Class,Integer> scores = new HashMap<Class,Integer>();
		ArrayList<Class> players = PigPen.allPlayers();
		for(Class p  : players) scores.put(p,0);
				for(int a = 0;a<players.size()-1;a++) {
					for(int b = a+1;b<players.size();b++) { 
					PigPen p = new PigPen(args,players.get(a),players.get(b));
					System.out.println();
					for(int i = 0;i<12;i++) {
					HashMap<Class,Integer> s = p.play((a+1)+","+(b+1)+"-"+(i+1));
					Class p1 = p.players.get(0).getClass();
					Class p2 = p.players.get(1).getClass();
					System.out.println(p1.getSimpleName() + " vs. " + p2.getSimpleName() 
					+ ": " + s.get(p1) + " - " + s.get(p2)); 
					p.drawBoard();
					Class winner = PigPen.rank(s).get(0).getKey();
					scores.put(winner,scores.get(winner)+1);
					}
					
				}
		}
		PigPen.printRankings(PigPen.rank(scores));
		
	}
	
	
}