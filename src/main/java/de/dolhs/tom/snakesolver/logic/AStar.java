package de.dolhs.tom.snakesolver.logic;

import de.dolhs.tom.snakesolver.model.Map;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class AStar extends Solver {

    LinkedList<Integer> directions = new LinkedList<>();

    public AStar(Map map) {
        super(map);
    }

    private void aStar() {
        int[][] cost = new int[map.getRows()][map.getCols()];
        int[][][] from = new int[map.getRows()][map.getCols()][2];
        Queue<int[]> open = new PriorityQueue<>(Comparator.comparing(n -> cost[n[0]][n[1]] + Math.abs(n[0] - map.getFood()[0]) + Math.abs(n[1] - map.getFood()[1])));
        LinkedList<int[]> closed = new LinkedList<>();
        open.add(new int[]{map.getSnake().getFirst()[0], map.getSnake().getFirst()[1]});
        while (!open.isEmpty()) {
            int[] field = open.poll();
            if (field[0] == map.getFood()[0] && field[1] == map.getFood()[1]) {
                LinkedList<int[]> backtracking = new LinkedList<>();
                LinkedList<int[]> path = new LinkedList<>();
                backtracking.add(field);
                while (field[0] != map.getSnake().getFirst()[0] || field[1] != map.getSnake().getFirst()[1]) {
                    field = from[field[0]][field[1]];
                    backtracking.add(field);
                }
                for (int i = backtracking.size() - 1; i >= 0; i--) {
                    path.add(backtracking.get(i));
                }
                directions.addAll(directions(path));
                break;
            }
            for (int[] neighbor : adjacent(field)) {
                boolean explored = false;
                for (int[] c : closed) {
                    explored |= c[0] == neighbor[0] && c[1] == neighbor[1];
                }
                for (int[] part : map.getSnake()) {
                    explored |= part[0] == neighbor[0] && part[1] == neighbor[1];
                }
                if (!explored) {
                    closed.add(neighbor);
                    open.add(neighbor);
                    cost[neighbor[0]][neighbor[1]] = cost[field[0]][field[1]];
                    from[neighbor[0]][neighbor[1]] = field;
                }
            }
        }
    }

    @Override
    public void next() {
        if (directions.isEmpty())
            aStar();
        if (directions.isEmpty())
            map.setOver(true);
        else
            map.setDirection(directions.poll());
    }

}
