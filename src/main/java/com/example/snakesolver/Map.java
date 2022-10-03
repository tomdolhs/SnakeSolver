package com.example.snakesolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Map {

    private final LinkedList<int[]> snake = new LinkedList<>();

    private int[] food = new int[2];

    private int direction = 0;

    private int score = 0;

    private boolean over = false;

    public Map() {
        snake.add(new int[]{17,24});
        snake.add(new int[]{16,24});
        snake.add(new int[]{15,24});
        food[0] = 24;
        food[1] = 24;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void move() {
        for (int i = snake.size() - 1; i >= 1; i--) {
            snake.get(i)[0] = snake.get(i-1)[0];
            snake.get(i)[1] = snake.get(i-1)[1];
        }
        switch (direction) {
            case 0: // Right.
                snake.get(0)[0] = snake.get(0)[0] + 1;
                break;
            case 1: // Up.
                snake.get(0)[1] = snake.get(0)[1] - 1;
                break;
            case 2: // Left.
                snake.get(0)[0] = snake.get(0)[0] - 1;
                break;
            case 3: // Down.
                snake.get(0)[1] = snake.get(0)[1] + 1;
                break;
        }
        collide();
        eat();
    }

    public void eat() {
        if (snake.getFirst()[0] == food[0] && snake.getFirst()[1] == food[1]) {
            snake.addFirst(new int[]{food[0], food[1]});
            generateFood();
        }
    }

    private void collide() {
        int[] head = snake.getFirst();
        over |= head[0] < 0 || head[0] >= 50 || head[1] < 0 || head[1] >= 50;
        for (int i = 1; i < snake.size(); i++) {
            int[] part = snake.get(i);
            over |= head[0] == part[0] && head[1] == part[1];
        }
    }

    private void generateFood() {
        Random random = new Random();
        ArrayList<int[]> valid = new ArrayList<>();
        for (int x = 0; x < 50; x++) {
            for (int y = 0; y < 50; y++) {
                valid.add(new int[]{x, y});
            }
        }
        for (int[] part : snake) {
            valid.remove(part[0]*50 + part[1]);
        }
        food = valid.get(random.nextInt(valid.size()));
    }

    public LinkedList<int[]> getSnake() {
        return snake;
    }

    public int[] getFood() {
        return food;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
