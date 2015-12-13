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
					for(int i = 0;i<2;i++) {
					HashMap<Class,Integer> s = p.play((a+1)+","+(b+1)+"-"+(i+1));
					Class winner = PigPen.rank(s).get(0).getKey();
					scores.put(winner,scores.get(winner)+1);
					}
					
				}
		}
		PigPen.printRankings(PigPen.rank(scores));
		
	}
	
	
}