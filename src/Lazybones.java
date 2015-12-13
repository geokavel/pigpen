package pigpen.players;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pigpen.Board;
import pigpen.Pen;
import pigpen.PigPen;
import pigpen.Player;

public class Lazybones extends Player {
    private static class Fence {
        private static boolean isOk(Board board, boolean vertical, int row, int col) {
            if (vertical) {
                Pen left = board.getPenAt(row, col - 1);
                Pen right = board.getPenAt(row, col);
                if (left.id() < 0 && right.id() < 0 ||
                        left.fences()[Pen.RIGHT] > 0 ||
                        right.fences()[Pen.LEFT] > 0 ||
                        left.remaining() == 2 ||
                        right.remaining() == 2) {
                    return false;
                }
            } else {
                Pen top = board.getPenAt(row - 1, col);
                Pen bottom = board.getPenAt(row, col);
                if (top.id() < 0 && bottom.id() < 0 ||
                        top.fences()[Pen.BOTTOM] > 0 ||
                        bottom.fences()[Pen.TOP] > 0 ||
                        top.remaining() == 2 ||
                        bottom.remaining() == 2) {
                    return false;
                }
            }
            return true;
        }

        private static Fence pickRandom(Board board) {
            List<Fence> ok = new ArrayList<>();
            List<Fence> notOk = new ArrayList<>();
            for (int row = 0; row < board.rows; row ++) {
                for (int col = 0; col < board.cols; col ++) {
                    (isOk(board, false, row, col) ? ok : notOk)
                            .add(new Fence(false, row, col));
                    (isOk(board, true, row, col) ? ok : notOk)
                            .add(new Fence(true, row, col));
                }
                (isOk(board, true, row, board.cols) ? ok : notOk)
                        .add(new Fence(true, row, board.cols));
            }
            for (int col = 0; col < board.cols; col ++) {
                (isOk(board, false, board.rows, col) ? ok : notOk)
                        .add(new Fence(false, board.rows, col));
            }
            if (ok.isEmpty()) {
                return notOk.get(PigPen.random(notOk.size()));
            } else {
                return ok.get(PigPen.random(ok.size()));
            }
        }

        private final boolean vertical;
        private final int row;
        private final int col;

        public Fence(boolean vertical, int row, int col) {
            super();
            this.vertical = vertical;
            this.row = row;
            this.col = col;
        }

        private Fence next(Board board, boolean negative) {
            int newRow = vertical ? (negative ? row - 1 : row + 1) : row;
            int newCol = vertical ? col : (negative ? col - 1 : col + 1);
            if (isOk(board, vertical, newRow, newCol)) {
                return new Fence(vertical, newRow, newCol);
            } else {
                return null;
            }
        }

        private int[] getResult(Board board) {
            if (vertical) {
                if (col < board.cols) {
                    return board.getPenAt(row, col).pick(Pen.LEFT);
                } else {
                    return board.getPenAt(row, col - 1).pick(Pen.RIGHT);
                }
            } else {
                if (row < board.rows) {
                    return board.getPenAt(row, col).pick(Pen.TOP);
                } else {
                    return board.getPenAt(row - 1, col).pick(Pen.BOTTOM);
                }
            }
        }
    }

    private Fence lastFence = null;
    private boolean negative = false;

    @Override
    public int[] pick(Board board, int id, int round) {
        List<Pen> money = board.getList().stream()
                .filter(p -> p.remaining() == 1).collect(Collectors.toList());
        if (!money.isEmpty()) {
            return money.get(PigPen.random(money.size())).pick(Pen.TOP);
        }
        if (lastFence != null) {
            lastFence = lastFence.next(board, negative);
        }
        if (lastFence == null) {
            lastFence = Fence.pickRandom(board);
            negative = PigPen.random(2) == 0;
        }
        return lastFence.getResult(board);
    }
}