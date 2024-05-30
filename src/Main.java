//Name: Charuka Crishani Fernando
//IIT ID: 20222005
//UoW ID: W1985683

// Importing necessary Java libraries
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

// Defining the main class
class Main {
    // Defining arrays for moving directions
    static final int[] DX = {-1, 0, 1, 0};
    static final int[] DY = {0, 1, 0, -1};

    // Function to check if a cell is valid within the maze
    static boolean isValid(char[][] maze, int x, int y) {
        int n = maze.length, m = maze[0].length;
        return x >= 0 && x < n && y >= 0 && y < m && maze[x][y] != '0';
    }

    // Dijkstra's algorithm to find the shortest path
    static List<Cell> dijkstra(char[][] maze, int newStartX, int newStartY, int newFinishX, int newFinishY) {
        int n = maze.length, m = maze[0].length;
        // Initializing arrays for storing distances and previous cells
        Cell[][] prev = new Cell[n][m];
        int[][] dist = new int[n][m];
        // Setting initial distances to infinity
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[newStartX][newStartY] = 0;

        // Using PriorityQueue for Dijkstra's algorithm
        PriorityQueue<Cell> pq = new PriorityQueue<>(Comparator.comparingInt(cell -> cell.newDist));
        pq.offer(new Cell(newStartX, newStartY, 0, null));

        // Dijkstra's algorithm loop
        while (!pq.isEmpty()) {
            Cell cell = pq.poll();

            // Exploring adjacent cells
            for (int i = 0; i < 4; ++i) {
                int nx = cell.newX, ny = cell.newY;
                while (isValid(maze, nx + DX[i], ny + DY[i])) {
                    nx += DX[i];
                    ny += DY[i];
                    // If the finish cell is reached, reconstruct and return the path
                    if (nx == newFinishX && ny == newFinishY) {
                        Cell finishCell = new Cell(nx, ny, cell.newDist + 1, cell);
                        List<Cell> path = new ArrayList<>();
                        for (Cell c = finishCell; c != null; c = c.newPrev) path.add(c);
                        Collections.reverse(path);
                        return path;
                    }
                }
                // Updating distances and adding cells to PriorityQueue
                if (dist[nx][ny] > cell.newDist + 1) {
                    dist[nx][ny] = cell.newDist + 1;
                    prev[nx][ny] = cell;
                    pq.offer(new Cell(nx, ny, dist[nx][ny], cell));
                }
            }
        }

        return null; // No path found
    }

    // Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompting user for puzzle selection
        System.out.println("Puzzle numbers - 10, 20, 40, 80, 160, 320, 640, 1280, 2560");
        System.out.print("Enter 0 to exit \n");
        System.out.print("Enter which puzzle to solve: ");
        String puzzleNumber = scanner.nextLine();

        // Storing maze and start/finish coordinates
        List<char[]> mazeList = new ArrayList<>();
        int newStartX = -1, newStartY = -1, newFinishX = -1, newFinishY = -1;

        // Reading puzzle file
        try (BufferedReader br = new BufferedReader(new FileReader("benchmark_series/puzzle_" + puzzleNumber + ".txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll(" ", "");
                char[] row = line.toCharArray();
                mazeList.add(row);

                // Finding start and finish coordinates
                for (int i = 0; i < row.length; ++i) {
                    if (row[i] == 'S') {
                        newStartX = mazeList.size() - 1;
                        newStartY = i;
                    } else if (row[i] == 'F') {
                        newFinishX = mazeList.size() - 1;
                        newFinishY = i;
                    }
                }
            }
        } catch (IOException e) {
            // Error handling for file reading
            System.out.println("Error reading the puzzle file. Please try again.\n");
            return;
        }

        // Converting mazeList to 2D array
        char[][] maze = mazeList.toArray(new char[0][]);

        long start = System.currentTimeMillis(); // Starting timer

        // Finding shortest path using Dijkstra's algorithm
        List<Cell> path = dijkstra(maze, newStartX, newStartY, newFinishX, newFinishY);

        long now = System.currentTimeMillis(); // Ending timer
        double elapsed = (now - start); // Calculating elapsed time

        // Displaying the path or indicating no path found
        if (path != null) {
            System.out.println("1. Start at (" + (newStartX + 1) + "," + (newStartY + 1) + ")");
            for (int i = 0; i < path.size() - 1; ++i) {
                Cell cell = path.get(i);
                Cell nextCell = path.get(i + 1);
                String move;
                // Determining movement direction
                if (nextCell.newX < cell.newX) {
                    move = "Move up to";
                } else if (nextCell.newX > cell.newX) {
                    move = "Move down to";
                } else if (nextCell.newY < cell.newY) {
                    move = "Move left to";
                } else {
                    move = "Move right to";
                }
                System.out.println((i + 2) + ". " + move + " (" + (nextCell.newX + 1) + "," + (nextCell.newY + 1) + ")");
            }
            System.out.println("Done!");
        } else {
            System.out.println("No path found");
        }
        // Displaying elapsed time
        System.out.println("Elapsed time = " + elapsed + " milliseconds\n");
    }
}
