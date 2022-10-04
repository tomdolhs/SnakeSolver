package de.dolhs.tom.snakesolver;

import java.util.LinkedList;

public class Solver {

    private Map map;

    private LinkedList<Integer> queue = new LinkedList<>();

    public Solver(Map map) {
        this.map = map;
        hamiltonCycle();
    }

    public void next() {
        map.setDirection(queue.getFirst());
        queue.addLast(queue.removeFirst());
    }

    private void hamiltonCycle() {
        LinkedList<int[]> path = new LinkedList<>();
        path.add(map.getSnake().getFirst());
        boolean found = hamiltonCycle(path);
        for (int i = 0; i < path.size() - 1; i++) {
            if (path.get(i)[1] == path.get(i + 1)[1] && path.get(i)[0] == path.get(i + 1)[0] - 1) {
                queue.add(0);
            } else if (path.get(i)[1] == path.get(i + 1)[1] + 1 && path.get(i)[0] == path.get(i + 1)[0]) {
                queue.add(1);
            } else if (path.get(i)[1] == path.get(i + 1)[1] && path.get(i)[0] == path.get(i + 1)[0] + 1) {
                queue.add(2);
            } else if (path.get(i)[1] == path.get(i + 1)[1] - 1 && path.get(i)[0] == path.get(i + 1)[0]) {
                queue.add(3);
            }
        }
    }

    private boolean hamiltonCycle(LinkedList<int[]> path) {
        if (path.size() == map.getCols() * map.getRows())  {
            if (adjacent(path.getFirst(), path.getLast())) {
                path.addLast(path.getFirst());
                return true;
            } else
                return false;
        }
        for (int i = 0; i < 4; i++) {
            int[] neighbor = null;
            switch (i) {
                case 0:
                    neighbor = new int[]{path.getLast()[0] + 1, path.getLast()[1]};
                    break;
                case 1:
                    neighbor = new int[]{path.getLast()[0], path.getLast()[1] - 1};
                    break;
                case 2:
                    neighbor = new int[]{path.getLast()[0] - 1, path.getLast()[1]};
                    break;
                case 3:
                    neighbor = new int[]{path.getLast()[0], path.getLast()[1] + 1};
                    break;
            }
            boolean contained = false;
            for (int[] node : path) {
                contained |= node[0] == neighbor[0] && node[1] == neighbor[1];
            }
            if (!contained && neighbor[0] >= 0 && neighbor[0] < map.getCols() && neighbor[1] >= 0 && neighbor[1] < map.getRows()) {
                path.addLast(neighbor);
                if (hamiltonCycle(path))
                    return true;
                path.removeLast();
            }
        }
        return false;
    }

    private boolean adjacent(int[] a, int[] b) {
        return a[1] == b[1] && a[0] == b[0] - 1 || a[1] == b[1] + 1 && a[0] == b[0] || a[1] == b[1] && a[0] == b[0] + 1 || a[1] == b[1] - 1 && a[0] == b[0];
    }

}
