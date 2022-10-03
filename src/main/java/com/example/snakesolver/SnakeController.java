package com.example.snakesolver;

public class SnakeController {

    private final SnakeView snakeView;

    private final Map map = new Map();

    public SnakeController(SnakeView snakeView) {
        this.snakeView = snakeView;
    }

    public Map getMap() {
        return map;
    }

    public void move() {
        map.move();
        if (map.isOver()) {
            snakeView.getTimeline().stop();
        }
    }

    public void setDirection(int direction) {
        if (map.getDirection() != (direction + 2) % 4) {
            map.setDirection(direction);
        }
    }

}
