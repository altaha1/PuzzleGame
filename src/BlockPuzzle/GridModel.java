package BlockPuzzle;

import java.util.List;
import java.util.ArrayList;

public class GridModel {
    private int[][] grid;
    private final int HEIGHT;
    private int score = 0;
    private boolean gameOver = false;
    public static final int GRID_ROWS = 9;
    public static final int GRID_COLS = 9;

    // constructor
    public GridModel(int rows, int cols){
        this.grid = new int[rows][cols];
        this.HEIGHT = rows;
    }

    public int getHEIGHT(){
        return HEIGHT;
    }

    public int getScore() {
        return score;
    }
    public boolean getGameOver(){
        return gameOver;
    }

    boolean canPlace(Shape shape){
        return canPlaceGrid(shape, grid);
    }

    boolean canPlaceGrid(Shape shape, int[][] grid) {
        if (shape != null){
            for (Cell cell : shape) {
                int cellX = cell.x();
                int cellY = cell.y();
                if (cellX < 0 || cellX >= grid.length || cellY < 0 || cellY >= grid[0].length || grid[cellX][cellY] != 0) {
                    return false;
                }
            }
        }
        return true;

    }


    boolean fullRegion(Shape shape) {
        return fullRegionGrid(shape, grid);
    }
    boolean fullRegionGrid(Shape shape, int[][] grid){
        for (Cell cell : shape) {
            int cellX = cell.x();
            int cellY = cell.y();

            if (cellX < 0 || cellX >= grid.length || cellY < 0 || cellY >= grid[0].length || grid[cellX][cellY] == 0) {
                return false;
            }
        }
        return true;
    }

    List<Shape> getFullRegions(int[][] grid) {
        return getFullRegionsGrid(grid);
    }
    private List<Shape> getFullRegionsGrid(int[][] grid) {
        List<Shape> fullRegions = new ArrayList<>();

        // Check rows
        for (int i = 0; i < grid.length; i++) {
            List<Cell> cells = new ArrayList<>();
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    cells.add(new Cell(i, j));
                }
            }
            if (cells.size() == grid[i].length) {
                fullRegions.add(new Shape(RegionType.ROW, cells));
            }
        }

        // Check columns
        for (int j = 0; j < grid[0].length; j++) {
            List<Cell> cells = new ArrayList<>();
            for (int i = 0; i < grid.length; i++) {
                if (grid[i][j] == 1) {
                    cells.add(new Cell(i, j));
                }
            }
            if (cells.size() == grid.length) {
                fullRegions.add(new Shape(RegionType.COL, cells));
            }
        }

        // Check 3x3 squares.
        for (int i = 0; i < grid.length; i += 3) {
            for (int j = 0; j < grid[i].length; j += 3) {
                List<Cell> cells = new ArrayList<>();
                for (int x = i; x < i + 3; x++) {
                    for (int y = j; y < j + 3; y++) {
                        if (grid[x][y] == 1) {
                            cells.add(new Cell(x, y));
                        }
                    }
                }
                if (cells.size() == 9) {
                    fullRegions.add(new Shape(RegionType.SUBSQUARE, cells));
                }
            }
        }

        return fullRegions;
    }

    public int getValue(int x, int y) {
        if (x >= 0 && x < grid.length && y >= 0 && y < grid[0].length) {
            return grid[x][y];
        } else {
            return 0;
        }
    }


    List<Shape> getWouldBeFullRegions(Shape toPlace) {
        List<Shape> wouldBeFullRegions;
        // Simulates placing a shape on the grid and determines regions that would become full

        int[][] simulatedGrid = new int[grid.length][grid[0].length];
        // Copy the current grid to the simulated grid
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, simulatedGrid[i], 0, grid[i].length);
        }
        placeShapeGrid(toPlace, simulatedGrid);

        wouldBeFullRegions = getFullRegionsGrid(simulatedGrid);

        return wouldBeFullRegions;
    }

    public void clearFullRegions() {

        List<Shape> fullRegions = getFullRegionsGrid(grid);
        for (Shape shape : fullRegions) {
            for (Cell cell : shape) {
                grid[cell.x()][cell.y()] = 0;
            }
        }
        score += fullRegions.size() * 50;
    }
    public void placeShape(Shape shape) {
        placeShapeGrid(shape, grid);
    }

    public void placeShapeGrid(Shape shape, int[][] grid) {
        for (Cell cell : shape) {
            grid[cell.x()][cell.y()] = 1; // 1 represents an occupied cell
        }
    }

    public void isGameOver(List<Piece> palettePieces) {
        for (Piece piece : palettePieces) {
            if (piece.state == PieceState.IN_PALETTE && canPlacePieceAnywhere(piece)) {
                // At least one piece can be placed, so the game is not over
                return;
            }
        }
        // No piece can be placed, indicating the game is over
        gameOver = true;
    }

    private boolean canPlacePieceAnywhere(Piece piece) {
        for (int i = 0; i < GRID_ROWS; i++) {
            for (int j = 0; j < GRID_COLS; j++) {
                for (Cell cell : piece.shape) {
                    int cellX = i + cell.x();
                    int cellY = j + cell.y();

                    if (cellX < 0 || cellX >= GRID_ROWS || cellY < 0 || cellY >= GRID_COLS || grid[cellX][cellY] != 0) {
                        break; // Break if any cell of the piece cannot be placed
                    } else if (cell == piece.shape.get(piece.shape.size() - 1)) {
                        return true; // All cells of the piece can be placed
                    }
                }
            }
        }
        return false;
    }
}
