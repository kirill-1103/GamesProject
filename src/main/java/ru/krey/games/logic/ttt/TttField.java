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

    public int getWinner3InRow() {
        for (int i = 0; i < this.size; i++) {
            if (this.field[0][i] == this.field[1][i] && this.field[2][i] == this.field[1][i] &&
                this.field[0][i]!=NONE) {
                return this.field[0][i];
            }
            if (this.field[i][0] == this.field[i][1] && this.field[i][2] == this.field[i][1] &&
                    this.field[i][0]!=NONE){
                return this.field[i][0];
            }
        }
        if(this.field[0][0]==this.field[1][1] && this.field[2][2] == this.field[1][1] &&
                this.field[0][0]!=NONE){
            return this.field[0][0];
        }
        if(this.field[0][2]==this.field[1][1] && this.field[2][0] == this.field[1][1] &&
                this.field[0][2]!=NONE){
            return this.field[0][2];
        }

        for(int y =0 ;y<size;y++){
            for(int x =0 ;x<size;x++){
                if(field[y][x] == NONE){
                    return NONE;
                }
            }
        }

        return DRAW;
    }

    public int getWinner4InRow() {

    }

    public int[] randomMove(){
        class Pair{int x;int y;}
        List<Pair> coords = new ArrayList<>();
        for(int y=0;y<size;y++){
            for(int x = 0;x<size;x++){
                if(field[y][x]==NONE){
                    Pair p = new Pair();
                    p.x=x;
                    p.y=y;
                    coords.add(p);
                }
            }
        }
        int index = RandomUtils.nextInt()%coords.size();
        int x = coords.get(index).x;
        int y = coords.get(index).y;
        this.setMove(x,y,TttField.O);
        return new int[]{x, y};
    }
}
