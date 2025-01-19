package BlockPuzzle;

import java.util.List;
import java.util.ArrayList;

public class ShapeSet {

    public List<Shape> getShapes() {
        List<Shape> shapes = new ArrayList<>();
        shapes.add(createLShape());
        shapes.add(createHorizontalLine());
        shapes.add(createTShape());
        shapes.add(createSquare());
        shapes.add(createVerticalLine());
        return shapes;
    }

    public Shape createHorizontalLine() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(0, 0));
        cells.add(new Cell(1, 0));
        cells.add(new Cell(2, 0));
        return new Shape(RegionType.PIECE, cells);
    }

    public Shape createVerticalLine() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(0, 0));
        cells.add(new Cell(0, 1));
        cells.add(new Cell(0, 2));
        cells.add(new Cell(0, 3));
        return new Shape(RegionType.PIECE, cells);
    }

    public Shape createSquare() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(0, 0));
        cells.add(new Cell(0, 1));
        cells.add(new Cell(1, 0));
        cells.add(new Cell(1, 1));
        return new Shape(RegionType.PIECE, cells);
    }

    public Shape createTShape() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(0, 0));
        cells.add(new Cell(1, 0));
        cells.add(new Cell(2, 0));
        cells.add(new Cell(1, 1));
        return new Shape(RegionType.PIECE, cells);
    }

    public Shape createLShape() {
        List<Cell> cells = new ArrayList<>();
        cells.add(new Cell(0, 0));
        cells.add(new Cell(0, 1));
        cells.add(new Cell(0, 2));
        cells.add(new Cell(1, 2));
        return new Shape(RegionType.PIECE, cells);
    }
//
}
