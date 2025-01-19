package BlockPuzzle;

import java.util.ArrayList;
import java.util.List;

public class Palette {

    // in the game we hold a palette of shapes as a list
    // this class is also responsible for replenishing them as they are used

    private List<Shape> shapes = new ArrayList<>();
    private List<Piece> pieces = new ArrayList<>();
    private final int nShapes = 1;

    public Palette() {
        shapes.addAll(new ShapeSet().getShapes());
        replenish();
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    private int nReadyPieces() {
        int readyPieces = 0;

        for (Piece piece : pieces) {
            if (piece.state == PieceState.IN_PALETTE) {
                readyPieces++;
            }
        }

        return readyPieces;
    }

    public void doLayout(int x, int y) {

        for (Piece piece : pieces) {
            if (piece.state == PieceState.IN_PALETTE) {
                piece.x = x;
                piece.y = y;
            }
        }
    }


    public void replenish() {
        int readyPieces = nReadyPieces();

        // Replenish the palette if there are not enough ready pieces.
        while (readyPieces < nShapes) {
            Shape shape = getRandomShape();

            int pieceCellSize = 40;

            Piece piece = new Piece(shape, 160, 380, pieceCellSize);

            pieces.add(piece);
            readyPieces++;
        }
    }


    private Shape getRandomShape() {
        int randomIndex = (int) (Math.random() * shapes.size());
        return shapes.get(randomIndex);
    }
}
