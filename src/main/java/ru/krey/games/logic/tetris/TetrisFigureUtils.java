package ru.krey.games.logic.tetris;

import java.util.ArrayList;
import java.util.List;

public class TetrisFigureUtils {
    final static int FIG_SIZE = 4;
    private final static int E = TetrisField.EMPTY_CELL;
    private final static int A = TetrisField.ACTIVE_CELL;

    private final static int FIG_X_COUNT = 4;

    static final int[][][] FIGS_I = {
            {
                    {E, A, E, E},
                    {E, A, E, E},
                    {E, A, E, E},
                    {E, A, E, E}
            },

            {
                    {E, E, E, E},
                    {A, A, A, A},
                    {E, E, E, E},
                    {E, E, E, E}
            },

            {
                    {E, A, E, E},
                    {E, A, E, E},
                    {E, A, E, E},
                    {E, A, E, E}
            },

            {
                    {E, E, E, E},
                    {A, A, A, A},
                    {E, E, E, E},
                    {E, E, E, E}
            },
    };

    static final int[][][] FIGS_J = {
            {
                    {E, E, A, E},
                    {E, E, A, E},
                    {E, A, A, E},
                    {E, E, E, E}
            },
            {
                    {E, A, E, E},
                    {E, A, A, A},
                    {E, E, E, E},
                    {E, E, E, E}
            },
            {
                    {E, E, A, A},
                    {E, E, A, E},
                    {E, E, A, E},
                    {E, E, E, E}
            },
            {
                    {E, E, E, E},
                    {E, A, A, A},
                    {E, A, E, E},
                    {E, E, E, E}
            }
    };

    static final int[][][] FIGS_L = {
            {
                    {E, A, E, E},
                    {E, A, E, E},
                    {E, A, A, E},
                    {E, E, E, E}
            },
            {
                    {E, E, E, E},
                    {A, A, A, E},
                    {A, E, E, E},
                    {E, E, E, E}
            },
            {
                    {A, A, E, E},
                    {E, A, E, E},
                    {E, A, E, E},
                    {E, E, E, E}
            },
            {
                    {E, E, A, E},
                    {A, A, A, E},
                    {E, E, E, E},
                    {E, E, E, E}
            }
    };

    static final int[][][] FIGS_O = {
            {
                    {E, E, E, E},
                    {E, A, A, E},
                    {E, A, A, E},
                    {E, E, E, E}
            },
            {
                    {E, E, E, E},
                    {E, A, A, E},
                    {E, A, A, E},
                    {E, E, E, E}
            },
            {
                    {E, E, E, E},
                    {E, A, A, E},
                    {E, A, A, E},
                    {E, E, E, E}
            },
            {
                    {E, E, E, E},
                    {E, A, A, E},
                    {E, A, A, E},
                    {E, E, E, E}
            },
    };

    static final int[][][] FIGS_Z = {
            {
                    {E, E, E, E},
                    {E, A, A, E},
                    {E, E, A, A},
                    {E, E, E, E}
            },
            {
                    {E, E, A, E},
                    {E, A, A, E},
                    {E, A, E, E},
                    {E, E, E, E}
            },
            {
                    {E, E, E, E},
                    {E, A, A, E},
                    {E, E, A, A},
                    {E, E, E, E}
            },
            {
                    {E, E, A, E},
                    {E, A, A, E},
                    {E, A, E, E},
                    {E, E, E, E}
            },
    };

    static final int[][][] FIGS_S = {
            {
                    {E, E, E, E},
                    {E, E, A, A},
                    {E, A, A, E},
                    {E, E, E, E}
            },
            {
                    {E, A, E, E},
                    {E, A, A, E},
                    {E, E, A, E},
                    {E, E, E, E}
            },
            {
                    {E, E, E, E},
                    {E, E, A, A},
                    {E, A, A, E},
                    {E, E, E, E}
            },
            {
                    {E, A, E, E},
                    {E, A, A, E},
                    {E, E, A, E},
                    {E, E, E, E}
            }
    };

    static final int[][][] FIGS_T = {
            {
                    {E, E, E, E},
                    {E, A, A, A},
                    {E, E, A, E},
                    {E, E, E, E}
            },
            {
                    {E, E, A, E},
                    {E, A, A, E},
                    {E, E, A, E},
                    {E, E, E, E}
            },
            {
                    {E, E, A, E},
                    {E, A, A, A},
                    {E, E, E, E},
                    {E, E, E, E}
            },
            {
                    {E, E, A, E},
                    {E, E, A, A},
                    {E, E, A, E},
                    {E, E, E, E}
            },
    };
    private static final int[][][][] figs = {FIGS_L, FIGS_I, FIGS_T, FIGS_S, FIGS_Z,
            FIGS_J, FIGS_O};

    public static List<int[][]> generateFigures(int count){
        var figures = new ArrayList<int[][]>();
        for(int i = 0;i<count;i++){
            figures.add(getRandomFigure());
        }
        return figures;
    }

    private static int[][] getRandomFigure() {
        return getFigureCopy(FIGS_I[0]);
        // return getFigureCopy(figs[(int) (Math.random() * 7)][0]);
    }

    static int[][] rotateFigure(int[][] figure) {
        for (int[][][] figs_x : figs) {
            int index = getIndex(figs_x, figure);
            if (index != -1) {
                return figs_x[(index + 1) % FIG_X_COUNT];
            }
        }
        return null;
    }

    static int[][] getFigureCopy(int[][] figure){
        int[][] figureCopy = new int[FIG_SIZE][FIG_SIZE];
        for (int y = 0; y < TetrisFigureUtils.FIG_SIZE; y++) {
            figureCopy[y] = new int[TetrisFigureUtils.FIG_SIZE];
            System.arraycopy(figure[y], 0, figureCopy[y], 0, FIG_SIZE);
        }
        return figureCopy;
    }
    static int getIndex(int[][][] figs_x, int[][] figure) {
        int i = 0;
        for (int[][] fig : figs_x) {
            if (figsEquals(fig, figure)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    static boolean figsEquals(int[][] fig1, int[][] fig2) {
        for (int y = 0; y < FIG_SIZE; y++) {
            for (int x = 0; x < FIG_SIZE; x++) {
                if (fig1[y][x] != fig2[y][x]) {
                    return false;
                }
            }
        }
        return true;
    }
}
