package BlockPuzzle;

import java.awt.*;
import java.util.ArrayList;

enum PieceState {
    IN_PALETTE, IN_PLAY, PLACED
}

public class Piece {
    // models a Shape located at a place in pixel coords with
    Shape shape;
    // top left of piece
    int x, y, cellSize;
    // boolean selected = false;
    PieceState state = PieceState.IN_PALETTE;

    public Piece(Shape shape, int x, int y, int cellSize){
        this.shape = shape;
        this.x = x;
        this.y = y;
        this.cellSize = cellSize;
    }

    public boolean contains(Point point) {
        for (Cell cell : shape) {
            int cellScreenX = x + cell.x() * cellSize / 2;
            int cellScreenY = y + cell.y() * cellSize / 2;

            Rectangle cellRect = new Rectangle(cellScreenX, cellScreenY, cellSize / 2, cellSize / 2);
            if (cellRect.contains(point)) {
                return true;
            }
        }
        return false;
    }


    public Shape snapToGrid(int margin) {
        // Calculate the snapped (aligned) coordinates for the top-left corner of the piece
        int snappedX = ((x - margin) / cellSize) * cellSize + margin;
        int snappedY = ((y - margin) / cellSize) * cellSize + margin;

        Shape snappedShape = new Shape(shape.getType(), new ArrayList<>());

        // Iterate through the cells of the original piece and adjust their coordinates
        for (Cell cell : shape) {
            // Calculate the new cell coordinates based on the snapped position and cell size
            // and add the adjusted cell to the new snapped shape.
            snappedShape.add(new Cell(cell.x() + snappedX / cellSize, cell.y() + snappedY / cellSize));
        }
        return snappedShape;
    }



    public int getCellSize() {
        return cellSize;
    }

    public String toString() {return "Piece: " + shape + " at " + x + ", " + y;}
}
