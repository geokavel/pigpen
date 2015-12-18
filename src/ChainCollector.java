package pigpen.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import pigpen.Board;
import pigpen.Pen;
import pigpen.Player;

public class ChainCollector extends Player {
    private enum Direction {
        TOP, RIGHT, BOTTOM, LEFT;

        public Direction opposite() {
            return values()[(ordinal() + 2) % 4];
        }
    }

    private enum ChainEndType {
        OPEN, CLOSED, LOOP
    }

    private static class PenEx {
        private final int id;
        private final List<Fence> openFences = new ArrayList<>();
        private boolean used = false;

        public PenEx(int id) {
            super();
            this.id = id;
        }

        public void addOpenFence(Direction direction, PenEx child) {
            openFences.add(new Fence(this, direction, child));
            if (child != null) {
                child.openFences.add(new Fence(child, direction.opposite(), this));
            }
        }
    }

    private static class Fence {
        public final PenEx parent;
        public final Direction direction;
        public final PenEx child;

        public Fence(PenEx parent, Direction direction, PenEx child) {
            super();
            this.parent = parent;
            this.direction = direction;
            this.child = child;
        }

        public int[] getMove() {
            if (parent == null) {
                return new int[] { child.id, direction.opposite().ordinal() };
            } else {
                return new int[] { parent.id, direction.ordinal() };
            }
        }
    }

    private static class Moves {
        private final TreeMap<Integer, List<Fence>> map = new TreeMap<>();

        public void add(int score, Fence move) {
            List<Fence> list = map.get(score);
            if (list == null) {
                list = new ArrayList<>();
                map.put(score, list);
            }
            list.add(move);
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

        public boolean hasExactlyOne() {
            return map.size() == 1 && map.firstEntry().getValue().size() == 1;
        }

        public int getLowestScore() {
            return map.firstKey();
        }

        public int[] getLowMove() {
            return map.firstEntry().getValue().get(0).getMove();
        }

        public int[] getHighMove() {
            return map.lastEntry().getValue().get(0).getMove();
        }
    }

    private static class BoardEx {
        private final List<PenEx> pens = new ArrayList<>();
        private final Moves neutralMoves = new Moves();
        private final Moves finisherMoves = new Moves();
        private final Moves safeFinisherMoves = new Moves();
        private final Moves sacrificeMoves = new Moves();
        private final Moves badMoves = new Moves();

        public BoardEx(Board board) {
            super();
            PenEx[][] tmp = new PenEx[board.rows][board.cols];
            for (int row = 0; row < board.rows; ++row) {
                for (int col = 0; col < board.cols; ++col) {
                    Pen pen = board.getPenAt(row, col);
                    int[] fences = pen.fences();
                    PenEx penEx = new PenEx(pen.id());
                    tmp[row][col] = penEx;
                    pens.add(penEx);
                    if (fences[Pen.TOP] == 0) {
                        penEx.addOpenFence(Direction.TOP, row == 0 ? null : tmp[row - 1][col]);
                    }
                    if (row == board.rows - 1 && fences[Pen.BOTTOM] == 0) {
                        penEx.addOpenFence(Direction.BOTTOM, null);
                    }
                    if (fences[Pen.LEFT] == 0) {
                        penEx.addOpenFence(Direction.LEFT, col == 0 ? null : tmp[row][col - 1]);
                    }
                    if (col == board.cols - 1 && fences[Pen.RIGHT] == 0) {
                        penEx.addOpenFence(Direction.RIGHT, null);
                    }
                }
            }
        }

        private ChainEndType followChain(Fence begin, List<Fence> result) {
            Fence current = begin;
            for (;;) {
                current.parent.used = true;
                result.add(current);
                if (current.child == null) {
                    return ChainEndType.OPEN;
                }
                List<Fence> childFences = current.child.openFences;
                switch (childFences.size()) {
                    case 1:
                        current.child.used = true;
                        return ChainEndType.CLOSED;
                    case 2:
                        if (current.child == begin.parent) {
                            return ChainEndType.LOOP;
                        } else {
                            current = current.direction.opposite() == childFences.get(0).direction ?
                                    childFences.get(1) : childFences.get(0);
                        }
                        break;
                    case 3:
                    case 4:
                        return ChainEndType.OPEN;
                    default:
                        throw new IllegalStateException();
                }
            }
        }

        public void findChains() {
            for (PenEx pen : pens) {
                if (!pen.used && pen.openFences.size() > 0) {
                    if (pen.openFences.size() < 3) {
                        List<Fence> fences = new ArrayList<>();
                        ChainEndType type1 = pen.openFences.size() == 1 ?
                                ChainEndType.CLOSED : followChain(pen.openFences.get(1), fences);
                        if (type1 == ChainEndType.LOOP) {
                            badMoves.add(fences.size(), fences.get(0));
                        } else {
                            Collections.reverse(fences);
                            ChainEndType type2 = followChain(pen.openFences.get(0), fences);
                            if (type1 == ChainEndType.OPEN && type2 == ChainEndType.CLOSED) {
                                type1 = ChainEndType.CLOSED;
                                type2 = ChainEndType.OPEN;
                                Collections.reverse(fences);
                            }
                            if (type1 == ChainEndType.OPEN) {
                                badMoves.add(fences.size() - 1, fences.get(fences.size() / 2));
                            } else if (type2 == ChainEndType.CLOSED) {
                                finisherMoves.add(fences.size() + 1, fences.get(0));
                                if (fences.size() == 3) {
                                    sacrificeMoves.add(fences.size() + 1, fences.get(1));
                                } else {
                                    safeFinisherMoves.add(fences.size() + 1, fences.get(0));
                                }

                            } else {
                                finisherMoves.add(fences.size(), fences.get(0));
                                if (fences.size() == 2) {
                                    sacrificeMoves.add(fences.size(), fences.get(1));
                                } else {
                                    safeFinisherMoves.add(fences.size(), fences.get(0));
                                }
                            }
                        }
                    } else {
                        pen.used = true;
                        for (Fence fence : pen.openFences) {
                            if (fence.child == null || fence.child.openFences.size() > 2) {
                                neutralMoves.add(fence.child == null ? 0 : fence.child.openFences.size(), fence);
                            }
                        }
                    }
                }
            }
        }

        public int[] bestMove() {
            if (!neutralMoves.isEmpty()) {
                if (!finisherMoves.isEmpty()) {
                    return finisherMoves.getHighMove();
                }
                return neutralMoves.getHighMove();
            }
            if (!safeFinisherMoves.isEmpty()) {
                return safeFinisherMoves.getHighMove();
            }
            if (badMoves.isEmpty() && !finisherMoves.isEmpty()) {
                return finisherMoves.getHighMove();
            }
            if (!sacrificeMoves.isEmpty()) {
                if (sacrificeMoves.hasExactlyOne()) {
                    if (badMoves.getLowestScore() - sacrificeMoves.getLowestScore() >= 2) {
                        return sacrificeMoves.getLowMove();
                    } else {
                        return finisherMoves.getHighMove();
                    }
                } else {
                    return finisherMoves.getHighMove();
                }
            }
            if (!badMoves.isEmpty()) {
                return badMoves.getLowMove();
            }
            return null;
        }
    }

    @Override
    public int[] pick(Board board, int id, int round) {
        BoardEx boardEx = new BoardEx(board);
        boardEx.findChains();
        return boardEx.bestMove();
    }
}