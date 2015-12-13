package pigpen;

import pigpen.players.*;

import java.util.*;
import java.io.*;

public class PigPen {
    static PigPen p;
    static Random r = new Random(74836282);
    
    static PrintWriter out;
    
	Board board;
	int rows, cols,sides;
	ArrayList<Player> players;
	
	static boolean output = false;;
	
	
	PigPen(String[] args, Player... players) {
		this.players = new ArrayList<Player>(Arrays.asList(players));
		Collections.shuffle(this.players,r);
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
	    	output = Boolean.parseBoolean(args[args.length-1]);
	}
	

	HashMap<Player,Integer> play(String round) {
	Collections.rotate(players,1);
  board = new Board(sides, rows, cols, players.size());
    if (output) {
    	try {
    	new File("output").mkdir();
      out = new PrintWriter("output/out_"+round+".txt");
      }
      catch(Exception e) {}
      out.print("P: "); 
      for (Player player : players) {
        out.print(player.getClass().getSimpleName() + " ");
      }
      out.println();
      out.println("B: " + rows + " " + cols + " " + players.size());
    }
    while (true) {
      for (int i = 0; i<players.size(); i++) {
        Player pl = players.get(i);
        int status = 1;
        while (status == 1) {
          int[] pick = pl.pick(board, i+1 );
          if (pick == null || pick.length < 2) 
            pick = new int[]{1, 0};
          //Scanner s = new Scanner(System.in);
          //int[] pick = new int[]{s.nextInt(),s.nextInt()};
          if (output) {
            System.out.println(pick[0] + " " + pick[1]);
          }
          status = board.set(pick[0], pick[1], i+1);
          if (output) {
          drawBoard();
        }
        }
        if (status == -1) {
          if (output) {
            out.flush();
            out.close();
          }
          HashMap<Player, Integer> scores = new HashMap<Player, Integer>();
          for (int s = 0; s<players.size(); s++) {
            scores.put(players.get(s), board.scores[s+1]);
          }
          return scores;
        }  
        
    }
    }
    }

	
	void drawBoard() {
	for(int i =0;i<rows;i++) {
		int cols = board.colsAt(i);
			for(int s = 0;s<rows-cols;s++) System.out.print(" ");
			for(int j = 0;j<cols;j++) {
				System.out.print(board.getPenAt(i,j).winner() + " ");
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
	
	static ArrayList<Player> allPlayers() throws Exception {
		ArrayList<Player> players = new ArrayList<Player>();
		String[] names = new java.io.File("pigpen/players/").list();
		for(String n : names) {
			if(n.contains("$")) continue;
			String c = "pigpen.players."+n.substring(0,n.indexOf("."));
			Player player = (Player)Class.forName(c).newInstance();
			players.add(player);
		}
		return players;
	}
	
	
	static ArrayList<Map.Entry<Player,Integer>> rank(HashMap<Player,Integer> scores) {
		ArrayList<Map.Entry<Player,Integer>> rankings = new ArrayList<Map.Entry<Player,Integer>>(scores.entrySet());
		Collections.sort(rankings,new Comparator<Map.Entry<Player,Integer>>() {
		
		@Override
	public int compare(Map.Entry<Player,Integer> a, Map.Entry<Player,Integer> b) {
		return -a.getValue().compareTo(b.getValue());
	}
		
		});
		return rankings;
	}
	
	static void printRankings(ArrayList<Map.Entry<Player,Integer>> rankings) {
		System.out.println();
		for(Map.Entry<Player,Integer> rank : rankings) {
			System.out.println(rank.getKey().getClass().getSimpleName()+": "+rank.getValue());
		}
	}
}