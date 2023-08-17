package ru.krey.games.logic.tetris;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.data.relational.core.sql.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

@Getter
public class TetrisField {
    public static final Integer EMPTY_CELL = 0;

    public static final Integer BUSY_CELL = 1;

    public static final Integer ACTIVE_CELL = 2;

    public final static int MOVE_LEFT = -1;

    public final static int MOVE_RIGHT = 1;

    public final static int MOVE_DOWN = 0;

    public final static int MOVE_ROTATE = 10;

    public final static Integer RESULT_LOSE = -1;

    private static final Integer LEFT_OFFSET = 2;

    private static final Integer RIGHT_OFFSET = 2;
    private static final Integer WIDTH_OFFSET = LEFT_OFFSET + RIGHT_OFFSET;

    private static final Integer STD_HEIGHT = 20;

    private static final Integer STD_WIDTH = 10;

    private static final Integer BOTTOM_OFFSET = 2;

    private static final Integer TOP_OFFSET = 4;

    private static final Integer HEIGHT_OFFSET = BOTTOM_OFFSET + TOP_OFFSET;

    private static final Integer START_X = 5;

    private static final Integer START_Y = 0;

    private List<List<Integer>> field;

    private final int height;

    private final int width;

    private int activeX;
    private int activeY;

    private int[][] activeFigure;

    private int figureIndex;

    private List<int[][]> nextFigures;

    private List<Integer> filledRow;

    TetrisField(int height, int width, List<int[][]> figures) {
        this.height = height;
        this.width = width;
        this.field = createTable();
        this.nextFigures = figures;
        this.figureIndex = 0;
        this.activeFigure = nextFigures.get(figureIndex++);
        addFigure(activeFigure);
        setFilledRow();
    }

    public TetrisField(List<int[][]> figures) {
        this(STD_HEIGHT + HEIGHT_OFFSET, STD_WIDTH + WIDTH_OFFSET, figures);
    }

    void move(int move) {
        switch (move) {
            case (MOVE_DOWN):
                moveDown();
                break;
            case (MOVE_LEFT):
                moveLeft();
                break;
            case (MOVE_RIGHT):
                moveRight();
                break;
            case (MOVE_ROTATE):
                rotate();
                break;
        }
    }

    List<Integer> nextIteration() {
        if (stopFigure()) {
            List<Integer> rowsNumbers = clearRows();
            if (endGame()) {
                rowsNumbers.add(RESULT_LOSE);
                return rowsNumbers;
            }
            activeFigure = TetrisFigureUtils.getFigureCopy(nextFigures.get(++figureIndex));
            addFigure(activeFigure);
//            clearFieldDebug();
            return rowsNumbers;
        } else {
            moveDown();
            return Collections.emptyList();
        }
    }

    public int getFigureIndex() {
        return figureIndex;
    }

    public void addFigures(List<int[][]> figures) {
        for (int[][] figure : figures) {
            this.nextFigures.add(figure);
        }
    }

    public int[][] nextFigure() {
        return this.nextFigures.get(figureIndex + 1);
    }

    public boolean moveDownIsPossible(){
        var fieldCopy = createTable();
        if (!setFigure(fieldCopy, activeX, activeY+1, activeFigure)) {
            return false;
        }
        return true;
    }

    public int[][] getFieldArray(){
        int[][] field = new int[height][width];
        for(int y=0;y<height;y++){
            for(int x =0;x<width;x++){
                field[y][x] = this.field.get(y).get(x);
            }
        }
        return field;
    }

    private void moveDown() {
        moveIfPossible(activeX, activeY + 1, activeFigure);
    }

    private void moveLeft() {
        moveIfPossible(activeX - 1, activeY, activeFigure);
    }

    private void moveRight() {
        moveIfPossible(activeX + 1, activeY, activeFigure);
    }

    private void rotate() {
        int[][] figureCopy = TetrisFigureUtils.getFigureCopy(activeFigure);
        figureCopy = TetrisFigureUtils.rotateFigure(figureCopy);
        moveIfPossible(activeX, activeY, figureCopy);
    }

    private void moveIfPossible(int x, int y, int[][] figure) {
        var fieldCopy = createTable();
        if (!setFigure(fieldCopy, x, y, figure)) {
            return;
        }
        if (checkNewFigure(fieldCopy)) {
            changeActive(field, EMPTY_CELL);
            activeX = x;
            activeY = y;
            setFigure(this.field, activeX, activeY, figure);
            this.activeFigure = figure;
        }
    }

    private boolean setFigure(List<List<Integer>> field, int xСoord, int yCoord, int[][] activeFigure) {
        for (int y = 0; y < TetrisFigureUtils.FIG_SIZE; y++) {
            for (int x = 0; x < TetrisFigureUtils.FIG_SIZE; x++) {
                if (activeFigure[y][x] == ACTIVE_CELL) {
                    if (y + yCoord >= height - BOTTOM_OFFSET || x + xСoord >= width - RIGHT_OFFSET || x + xСoord < LEFT_OFFSET) {
                        return false;
                    }
                    field.get(y + yCoord).set(x + xСoord, ACTIVE_CELL);
                }
            }
        }
        return true;
    }

    private void setFilledRow() {
        filledRow = new ArrayList<Integer>();
        filledRow.add(EMPTY_CELL);
        filledRow.add(EMPTY_CELL);
        for (int i = 0; i < width; i++) {
            filledRow.add(BUSY_CELL);
        }
        filledRow.add(EMPTY_CELL);
        filledRow.add(EMPTY_CELL);
    }

    private boolean checkNewFigure(List<List<Integer>> fieldWithFigure) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (fieldWithFigure.get(y).get(x).equals(ACTIVE_CELL) && this.field.get(y).get(x).equals(BUSY_CELL)) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<List<Integer>> getFieldCopy() {
        List<List<Integer>> copy = new ArrayList<>(Collections.nCopies(height, new ArrayList<>()));
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                copy.get(y).add(field.get(y).get(x));
            }
        }
        return copy;
    }

    private void changeActive(List<List<Integer>> field, int newState) {
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (field.get(y).get(x).equals(ACTIVE_CELL)) {
                    field.get(y).set(x, newState);
                }
            }
        }
    }

    private List<List<Integer>> createTable() {
        List<List<Integer>> table = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            table.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                table.get(i).add(EMPTY_CELL);
            }
        }
        return table;
    }

    private boolean stopFigure() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (field.get(y).get(x).equals(ACTIVE_CELL)) {
                    if (y == height - BOTTOM_OFFSET - 1 || field.get(y + 1).get(x).equals(BUSY_CELL)) {
                        changeActive(field, BUSY_CELL);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean endGame() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (field.get(y).get(x).equals(ACTIVE_CELL) && y < TOP_OFFSET) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Integer> clearRows() {
        List<Integer> rowsNumbers = new ArrayList<>();
        for (int row = TOP_OFFSET; row < height - BOTTOM_OFFSET; row++) {
            int count = 0;
            for (int i = LEFT_OFFSET; i < width - RIGHT_OFFSET; i++) {
                if (field.get(row).get(i).equals(BUSY_CELL)) {
                    count++;
                }
            }
            if (count == STD_WIDTH) {
                rowsNumbers.add(row);
                field.set(row, getEmptyRow());
            }
        }
        if(!rowsNumbers.isEmpty()){
            shiftRows();
        }
        return rowsNumbers;
    }

    private void shiftRows() {
        List<List<Integer>> newField = new ArrayList<>();
        for(int i = 0 ;i<height;i++){
            boolean emptyRow = true;
            for(int j = 0;j<width;j++){
                if(field.get(i).get(j).equals(BUSY_CELL)){
                    emptyRow = false;
                    break;
                }
            }
            if(!emptyRow){
                newField.add(field.get(i));
            }
        }
        int newFieldSize = newField.size();
        for(int i=0;i<height-BOTTOM_OFFSET-newFieldSize;i++){
            newField.add(0, getEmptyRow());
        }
        for(int i = 0;i<BOTTOM_OFFSET;i++){
            newField.add(getEmptyRow());
        }
        for(int i = 0;i<height;i++){
            for(int j=0;j<width;j++){
                if(field.get(i).get(j).equals(ACTIVE_CELL)){
                    newField.get(i).set(j,ACTIVE_CELL);
                }
            }
        }
        this.field = newField;
    }

    private List<Integer> getEmptyRow() {
        List<Integer> emptyList = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            emptyList.add(EMPTY_CELL);
        }
        return emptyList;
    }

    private void addFigure(int[][] figure) {
        this.activeX = START_X;
        this.activeY = START_Y;
//        for (int y = START_Y; y < TetrisFigureUtils.FIG_SIZE + START_Y; y++) {
//            for (int x = START_X; x < TetrisFigureUtils.FIG_SIZE + START_X; x++) {
//                field.get(y).set(x, figure[y - START_Y][x - START_X]);
//            }
//        }
        return;
    }

    public void clearFieldDebug() {
        boolean clear = false;
        for (int i = 0; i < 10 && !clear; i++) {
            for (int j = 0; j < STD_WIDTH; j++) {
                if (field.get(i).get(j).equals(BUSY_CELL)) {
                    clear = true;
                    break;
                }
            }
        }
        if (clear) {
            for (List<Integer> row : field) {
                ListIterator<Integer> iterator = row.listIterator();
                while (iterator.hasNext()) {
                    iterator.next();
                    iterator.set(EMPTY_CELL);
                }
            }
        }
    }

    public boolean needFigures() {
        return this.nextFigures.size() - this.figureIndex < 3;
    }

    public static String toJson(int[][] field) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(field);
    }

    public static int[][] fromJson(String jsonField) throws JsonProcessingException {
        if (jsonField == null) return null;
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonField, int[][].class);
    }
}