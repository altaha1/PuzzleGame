package BlockPuzzle;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class GameController implements MouseListener, MouseMotionListener {

    private PuzzleGame puzzleGame;
    private Palette palette;
    private GridModel gridModel;
    private final int margin;
    private Piece selectedPiece;
    private Shape ghostShape;
    private List<Shape> poppableRegions;
    private Point lastMousePoint;


    public GameController(Palette palette, GridModel gridModel, int margin) {
        this.palette = palette;
        this.gridModel = gridModel;
        this.margin = margin;
        this.poppableRegions = new ArrayList<>();
    }

    public void setPuzzleGame(PuzzleGame puzzleGame){
        this.puzzleGame = puzzleGame;
    }

    public Shape getGhostShape() {
        return ghostShape;
    }

    public void getIsGameOver(){
        gridModel.isGameOver(palette.getPieces());
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Piece piece : palette.getPieces()) {
            if (piece.contains(new Point(e.getX(), e.getY()))) {
                selectedPiece = piece;
                selectedPiece.state = PieceState.IN_PLAY;
                lastMousePoint = e.getPoint();
                break;
            }
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedPiece != null) {
            // Snap the piece to the grid before finalizing its position
            Shape shape = selectedPiece.snapToGrid(margin);

            // Check if the piece can be placed on the grid
            if (gridModel.canPlace(shape)) {
                gridModel.placeShape(shape);
                gridModel.clearFullRegions();
                selectedPiece.state = PieceState.PLACED;
                palette.replenish();
                getIsGameOver();
            } else {
                // Reset the piece state if it cannot be placed
                selectedPiece.state = PieceState.IN_PALETTE;
                palette.doLayout(160, 380);
            }
            poppableRegions.clear();

            selectedPiece = null;
            ghostShape = null;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedPiece != null && selectedPiece.state == PieceState.PLACED){
            return;
        }
        if (selectedPiece != null && lastMousePoint != null) {
            int dx = e.getX() - lastMousePoint.x;
            int dy = e.getY() - lastMousePoint.y;
            selectedPiece.x += dx;
            selectedPiece.y += dy;
            setGhostShape();
            if (ghostShape != null){
                poppableRegions = gridModel.getWouldBeFullRegions(ghostShape);
            } else {
                poppableRegions.clear();
            }
            lastMousePoint = e.getPoint();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private void setGhostShape() {
        if (selectedPiece != null) {
            // Get the snapped shape based on the current position of the selected piece
            Shape snappedShape = selectedPiece.snapToGrid(margin);

            // Check if the snapped shape is within the bounds of the grid
            boolean isInsideGrid = isShapeInsideGrid(snappedShape);


            if (isInsideGrid){
                if (gridModel.canPlace(snappedShape)){
                ghostShape = snappedShape;}
            } else {
                ghostShape = null;
            }
        }

    }

    private boolean isShapeInsideGrid(Shape shape) {
        Rectangle boundingBox = getBoundingBox(shape);

        // Check if the bounding box is within the bounds of the 9x9 grid
        return boundingBox.x >= 0 && boundingBox.y >= 0 &&
                boundingBox.x + boundingBox.width <= 9 &&
                boundingBox.y + boundingBox.height <= 9;
    }

    private Rectangle getBoundingBox(Shape shape) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Cell cell : shape) {
            int cellX = cell.x();
            int cellY = cell.y();

            // Update the bounding box coordinates based on the cell
            minX = Math.min(minX, cellX);
            minY = Math.min(minY, cellY);
            maxX = Math.max(maxX, cellX);
            maxY = Math.max(maxY, cellY);
        }

        // Calculate the width and height of the bounding box
        int width = maxX - minX + 1;
        int height = maxY - minY + 1;

        // Create and return the bounding box rectangle.
        return new Rectangle(minX, minY, width, height);
    }


    private void repaint() {
        if (puzzleGame != null){
            puzzleGame.repaint();
        }
    }
}
