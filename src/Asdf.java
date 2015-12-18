package pigpen.players;

import java.util.*;
import pigpen.*;

public class Asdf extends Player {
    private final List<Score> scores = new ArrayList<>();

    @Override
    public int[] pick(Board board, int id, int round) {
        scores.clear();
        List<Pen> pens = board.getList();

        pens.stream().filter(x -> !x.closed()).forEach((Pen p) -> evaluate(p));
        Optional<Score> best = scores.stream().max(Comparator.comparingInt(p -> p.points));

        if (best.isPresent()) {
            Score score = best.get();
            return score.pen.pick(score.fence);
        }
        return null;
    }

    private void evaluate(Pen pen) {
        int[] fences = pen.fences();
        for (int i = 0; i < fences.length; i++) {
            if (fences[i] == 0) {
                int points = getPoints(pen);
                Pen neighbour = pen.n(i);
                if (neighbour.id() != -1) {
                    points += getPoints(neighbour);
                }
                scores.add(new Score(pen, i, points));
            }
        }
    }

    private int getPoints(Pen pen) {
        switch (pen.remaining()) {
            case 1: return 10;
            case 2: return -1;
            case 3: return 1;
        }
        return 0;
    }

    class Score {
        private Pen pen;
        private int fence;
        private int points;

        Score(Pen pen, int fence, int points) {
            this.pen = pen;
            this.fence = fence;
            this.points = points;
        }
    }
}