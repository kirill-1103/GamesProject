package ru.krey.games.logic.tetris;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class TetrisLogic {
    private final int MAX_SPEED = 13;

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
            System.out.println("LOSE");
            this.lose = true;
            lastRemovedRowsNumbers.removeIf(TetrisField.RESULT_LOSE::equals);
        }
        addPoints();
        addSpeed();
        this.lastIterationTime = LocalDateTime.now();
    }

    public void move(int moveCode){
        if(moveCode == TetrisField.MOVE_DOWN && field.moveDownIsPossible()){
            this.lastIterationTime = LocalDateTime.now();
            System.out.println("MOVE DOWN");
        }
        field.move(moveCode);
    }

    public void addTime(int time){
        this.timeInMillis += time;
    }

    public boolean isEnd(){
        return lose;
    }

    public boolean needFigures(){
        return this.field.needFigures();
    }

    private void addPoints(){
        switch (lastRemovedRowsNumbers.size()){
            case(1):
                this.points += 100;
                break;
            case(2):
                this.points += 300;
                break;
            case(3):
                this.points += 600;
                break;
            case(4):
                this.points += 1000;
        }
    }

    private void addSpeed(){
        if(++countNext == 6+speed*2.4 && speed < MAX_SPEED){
            speed+=1;
            countNext = 0;
        }
    }

    private void setLastIterationTime(LocalDateTime time){
        this.lastIterationTime = time;
    }

}
