package Minesweeper.model;

import Minesweeper.view.messaging.GameMediator;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {

    private final Timer timer;
    private GameMediator mediator;
    private Duration timerCount;

    public GameTimer() {
        this.timer = new Timer();
        this.timerCount = Duration.ZERO;
    }

    public void setMediator(GameMediator mediator) {
        this.mediator = mediator;
        mediator.registerTimer(this);
    }

    public void start(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onUpdate();
            }
        }, 1000, 1000);

    }

    public void stop(){
        timer.cancel();
    }

    private void onUpdate(){
        timerCount = timerCount.plusSeconds(1);
        mediator.setTime(timerCount);
    }

    public Duration getTime() {
        return this.timerCount;
    }
}
