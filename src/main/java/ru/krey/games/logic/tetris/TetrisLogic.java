package ru.krey.games.logic.tetris;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class TetrisLogic {
    private final TetrisField field;
    private int points;
    private int speed;
    private int timeInMillis;
    private List<Integer> lastRemovedRowsNumbers;

    private boolean lose = false;

    private int countNext;

    private LocalDateTime lastIterationTime = LocalDateTime.now();


    public TetrisLogic(TetrisField field){
        this.field = field;
        this.points = 0;
        this.speed = 1;
        this.timeInMillis = 0;
        countNext = 0;
    }

    public void next(){
        lastRemovedRowsNumbers = field.nextIteration();
        if(lastRemovedRowsNumbers.contains(TetrisField.RESULT_LOSE)){
            this.lose = true;
            lastRemovedRowsNumbers.removeIf(TetrisField.RESULT_LOSE::equals);
        }
        addPoints();
        addSpeed();
        this.lastIterationTime = LocalDateTime.now();
    }

    public void move(int moveCode){
        field.move(moveCode);
    }

    public void addTime(int time){
        this.timeInMillis += time;
    }

    public boolean isEnd(){
        return lose;
    }

    private void addPoints(){
        switch (lastRemovedRowsNumbers.size()){
            case(1):
                this.points += 100;
                break;
            case(2):
                this.points += 300;
            case(3):
                this.points += 600;
                break;
            case(4):
                this.points += 1000;
        }
    }

    private void addSpeed(){
        if(++countNext%10==0){
            speed+=1;
        }
    }

    private void setLastIterationTime(LocalDateTime time){
        this.lastIterationTime = time;
    }
}
