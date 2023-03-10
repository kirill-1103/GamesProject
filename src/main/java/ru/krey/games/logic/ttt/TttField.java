package ru.krey.games.logic.ttt;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.security.core.parameters.P;
import ru.krey.games.error.BadRequestException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TttField {
    public final static int X = 1;
    public final static int O = -1;
    public final static int NONE = 0;
    public final static int DRAW = 2;

    private final int size;
    private final int[][] field;

    public TttField(int size) {
        this.size = size;
        this.field = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.field[i][j] = NONE;
            }
        }
    }

    public boolean setMove(int x, int y, int move) {
        if (x < 0 || y < 0 || x >= size || y >= size || this.field[y][x] != TttField.NONE) {
            throw new RuntimeException("Данное поле уже занято!");
        }
        this.field[y][x] = move;
        return true;
    }

    public int[][] getField() {
        return field;
    }

    public int getWinner() {
        if (this.size == 3) {
            return getWinner3InRow();
        } else {
            return getWinner4InRow();
        }
    }

    private int getWinner3InRow() {
        for (int i = 0; i < this.size; i++) {
            if (this.field[0][i] == this.field[1][i] && this.field[2][i] == this.field[1][i] &&
                    this.field[0][i] != NONE) {
                return this.field[0][i];
            }
            if (this.field[i][0] == this.field[i][1] && this.field[i][2] == this.field[i][1] &&
                    this.field[i][0] != NONE) {
                return this.field[i][0];
            }
        }
        if (this.field[0][0] == this.field[1][1] && this.field[2][2] == this.field[1][1] &&
                this.field[0][0] != NONE) {
            return this.field[0][0];
        }
        if (this.field[0][2] == this.field[1][1] && this.field[2][0] == this.field[1][1] &&
                this.field[0][2] != NONE) {
            return this.field[0][2];
        }

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (field[y][x] == NONE) {
                    return NONE;
                }
            }
        }

        return DRAW;
    }

    private int getWinner4InRow() {
        int horizontalVertical = getWinner4HorizontalVertical();
        if (horizontalVertical != TttField.NONE) {
            return horizontalVertical;
        }
        return getWinner4Diagonal();
    }

    private int getWinner4HorizontalVertical() {
        int win = 4;

        for (int y = 0; y < size; y++) {
            int xCountX = 0;
            int xCountY = 0;
            int oCountX = 0;
            int oCountY = 0;
            for (int x = 0; x < size; x++) {
                int value = field[y][x];
                if (value == X) {
                    xCountX++;
                    oCountX = 0;
                } else if (value == O) {
                    oCountX++;
                    xCountX = 0;
                } else {
                    oCountX = 0;
                    xCountX = 0;
                }

                value = field[x][y];
                if (value == X) {
                    xCountY++;
                    oCountY = 0;
                } else if (value == O) {
                    oCountY++;
                    xCountY = 0;
                } else {
                    xCountY = 0;
                    oCountY = 0;
                }

                if (xCountX == win || xCountY == win) {
                    return X;
                } else if (oCountX == win || oCountY == win) {
                    return O;
                }
            }
        }
        return NONE;
    }

    private int getWinner4Diagonal() {
        int win = 4;

        int d = size - win + 1;

        for (int i = 0; i < d; i++) {
            int xCount1 = 0;
            int oCount1 = 0;
            int xCount2 = 0;
            int oCount2 = 0;
            int xCount3 = 0;
            int oCount3 = 0;
            int xCount4 = 0;
            int oCount4 = 0;
            for (int j = 0; j < size - i; j++) {
                int value = field[i + j][j];
                if (value == X) {
                    xCount1 += 1;
                    oCount1 = 0;
                } else if (value == O) {
                    xCount1 = 0;
                    oCount1 += 1;
                } else {
                    xCount1 = 0;
                    oCount1 = 0;
                }

                value = field[j][size - 1 - (i + j)];
                if (value == X) {
                    xCount2 += 1;
                    oCount2 = 0;
                } else if (value == O) {
                    xCount2 = 0;
                    oCount2 += 1;
                } else {
                    xCount2 = 0;
                    oCount2 = 0;
                }

                value = field[j][i+j];
                if (value == X) {
                    xCount3 += 1;
                    oCount3 = 0;
                } else if (value == O) {
                    xCount3 = 0;
                    oCount3 += 1;
                } else {
                    xCount3 = 0;
                    oCount3 = 0;
                }

                value = field[size-1-j][j+i];
                if (value == X) {
                    xCount4 += 1;
                    oCount4 = 0;
                } else if (value == O) {
                    xCount4 = 0;
                    oCount4 += 1;
                } else {
                    xCount4 = 0;
                    oCount4 = 0;
                }

                if (xCount1 == win || xCount2 == win ||
                        xCount3 == win || xCount4 == win) {
                    return X;
                } else if (oCount1 == win || oCount2 == win ||
                        oCount3 == win || oCount4 == win) {
                    return O;
                }
            }
        }

        return NONE;
    }


    public int[] randomMove() {
        class Pair {
            int x;
            int y;
        }
        List<Pair> coords = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (field[y][x] == NONE) {
                    Pair p = new Pair();
                    p.x = x;
                    p.y = y;
                    coords.add(p);
                }
            }
        }
        int index = RandomUtils.nextInt() % coords.size();
        int x = coords.get(index).x;
        int y = coords.get(index).y;
        this.setMove(x, y, TttField.O);
        return new int[]{x, y};
    }
}
