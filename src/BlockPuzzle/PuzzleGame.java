//package BlockPuzzle;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class PuzzleGame extends JPanel {
//
//    private int cellSize;
//    private List<Piece> shapes;
//    private Shape ghostShape;
//    private List<Shape> poppableRegions;
//    private int gridSize;
//
//    public PuzzleGame(int cellSize, List<Piece> shapes, Shape ghostShape, List<Shape> poppableRegions) {
//        this.cellSize = cellSize;
//        this.shapes = shapes;
//        this.ghostShape = ghostShape;
//        this.poppableRegions = poppableRegions;
//        this.gridSize = 3;
//    }
//
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        paintGrid(g);
//        paintMiniGrids((Graphics2D) g); // cosmetic
//        paintGhostShape(g, cellSize);
//        paintPoppableRegions(g, cellSize);
//        paintShapePalette(g, cellSize);
//    }
//
//    private void paintGrid(Graphics g) {
//        // Assume gridSize and other relevant data are available
//        for (int i = 0; i < gridSize; i++) {
//            for (int j = 0; j < gridSize; j++) {
//                g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);
//                // Additional logic for coloring or filling cells if needed
//            }
//        }
//    }
//
//
//
//
//
//
//
//
//    private void paintMiniGrids(Graphics g, int startX, int startY) {
//        g.setColor(Color.LIGHT_GRAY); // Set color for small squares
//
//        for (int k = 0; k < 3; k++) {
//            for (int l = 0; l < 3; l++) {
//                int miniGridX = startX + k * cellSize;
//                int miniGridY = startY + l * cellSize;
//                g.drawRect(miniGridX, miniGridY, cellSize, cellSize);
//            }
//        }
//    }
//
//
//    private void paintGhostShape(Graphics g, int cellSize) {
//        if (ghostShape != null) {
//            g.setColor(new Color(0, 0, 255, 100)); // Semi-transparent blue color
//            for (Cell cell : ghostShape) {
//                int x = cell.x() * cellSize;
//                int y = cell.y() * cellSize;
//                g.fillRect(x, y, cellSize, cellSize);
//            }
//        }
//    }
//
//    private void paintPoppableRegions(Graphics g, int cellSize) {
//        g.setColor(new Color(255, 0, 0, 100)); // Semi-transparent red color
//        for (Shape poppableRegion : poppableRegions) {
//            for (Cell cell : poppableRegion) {
//                int x = cell.x() * cellSize;
//                int y = cell.y() * cellSize;
//                g.fillRect(x, y, cellSize, cellSize);
//            }
//        }
//    }
//
//    private void paintShapePalette(Graphics g, int cellSize) {
//        int xOffset = 10;
//        int yOffset = 10;
//
//        for (int i = 0; i < shapes.size(); i++) {
//            Piece piece = shapes.get(i);
//            g.setColor(Color.BLACK);
//            for (Cell cell : piece.shape) {
//                int x = piece.x + cell.x() * cellSize;
//                int y = piece.y + cell.y() * cellSize;
//                g.drawRect(x, y, cellSize, cellSize);
//            }
//        }
//    }
//
//
//
//
//
//    public void repaint() {
//        super.repaint();
//    }
//
//    public static void main(String[] args) {
//        // Create instances of Palette, GridModel, and GameController
//        Palette palette = new Palette();
//        GridModel gridModel = new GridModel(8, 8);
//        GameController controller = new GameController(palette, gridModel, 10, 40, 30);
//
//        // Use the palette to get a list of pieces
//        List<Piece> pieces = palette.getPieces();
//
//        // Create an instance of PuzzleGame and set it up
//        PuzzleGame puzzleGame = new PuzzleGame(40, pieces, null, new ArrayList<>());
//        puzzleGame.addMouseListener(controller);
//        puzzleGame.addMouseMotionListener(controller);
//
//        controller.setPuzzleGame(puzzleGame);
//
//        // Set up the frame and add the PuzzleGame panel
//        JFrame frame = new JFrame("Block Puzzle Game");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(380, 600);
//        frame.setResizable(true);
//        frame.getContentPane().add(puzzleGame);
//        frame.setVisible(true);
//    }
//
//}

package BlockPuzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class PuzzleGame extends JPanel {
    private final GridModel gridModel;
    private final Palette palette;
    private final int margin = 5;
    private JLabel scoreLabel;
    private JLabel gameOverLabel;
    private final GameController gameController;
    private final int cellSize = 40;

    public PuzzleGame() {
        gridModel = new GridModel(9, 9);
        palette = new Palette();
        gameController = new GameController(palette, gridModel, margin);

        setPreferredSize(new Dimension(361, 500));
        setBackground(Color.WHITE);

        // Score label
        scoreLabel = new JLabel("Score "+ gridModel.getScore());
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        scoreLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        Font labelFont = scoreLabel.getFont();
        scoreLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 16));

        // game over label
        gameOverLabel = new JLabel("Game Over: " + gridModel.getGameOver());
        gameOverLabel.setForeground(Color.BLACK);
        gameOverLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        gameOverLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        gameOverLabel.setFont(new Font(labelFont.getName(), Font.BOLD, 16));

        // layout for score and gameover labels
        int bottomLabelMargin = 10;
        setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        scoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 90, bottomLabelMargin, 0));
        gameOverLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, bottomLabelMargin, 0));
        bottomPanel.add(gameOverLabel);
        bottomPanel.add(scoreLabel);


        add(bottomPanel, BorderLayout.PAGE_END);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gameController.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                gameController.mouseReleased(e);
                updateScoreLabel();
                updateGameOverLabel();
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                gameController.mouseDragged(e);
            }
        });

        gameController.setPuzzleGame(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGrid(g);
        paintMiniGrids((Graphics2D) g); // Assuming this is cosmetic as per screenshot
        paintGhostShape(g);
        paintPoppableRegions(g);
        paintShapePalette(g);
    }

    public void updateScoreLabel() {
        scoreLabel.setText("Score: " + gridModel.getScore());
    }

    public void updateGameOverLabel(){
        gameOverLabel.setText("Game over: " + gridModel.getGameOver());
    }

    private void paintGrid(Graphics g) {
        // paint the grid
        for (int i = 0; i < gridModel.getHEIGHT(); i++) {
            for (int j = 0; j < gridModel.getHEIGHT(); j++) {
                int x = i * cellSize;
                int y = j * cellSize;
                g.setColor(Color.GRAY);
                g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);

                // Fill placed shapes in yellow
                if (gridModel.getValue(i, j) == 1) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x, y, cellSize, cellSize);
                }
            }
        }
    }


    private void paintMiniGrids(Graphics2D g) {
        Stroke originalStroke = g.getStroke();
        g.setStroke(new BasicStroke(2.0f));

        for (int i = 0; i < gridModel.getHEIGHT(); i+= 3) {
            for (int j = 0; j < gridModel.getHEIGHT(); j += 3) {
                g.setColor(Color.BLACK);
                g.drawRect(i * cellSize, j * cellSize, cellSize * 3 , cellSize * 3);
            }
        }
        g.setStroke(originalStroke);
    }

    private void paintGhostShape(Graphics g) {
        Shape ghostShape = gameController.getGhostShape();
        if (ghostShape != null) {
            g.setColor(new Color(0, 0, 255, 100)); // Semi-transparent blue color
            for (Cell cell : ghostShape) {
                int x = cell.x() * cellSize;
                int y = cell.y() * cellSize;
                g.fillRect(x, y, cellSize, cellSize);
            }
        }
    }


    private void paintPoppableRegions(Graphics g) {
        g.setColor(new Color(255, 0, 0, 100)); // Red for poppable regions
        if (gameController.getGhostShape() != null) {
            for (Shape region : gridModel.getWouldBeFullRegions(gameController.getGhostShape())) {
                for (Cell cell : region) {
                    g.fillRect(cell.x() * cellSize, cell.y() * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    private void paintShapePalette(Graphics g) {

        for (Piece piece : palette.getPieces()) {
            if (piece.state == PieceState.IN_PALETTE) {
                // paint pieces in palette
                g.setColor(Color.GREEN);
                for (Cell cell : piece.shape) {
                    int x = piece.x + cell.x() * cellSize /2;
                    int y = piece.y + cell.y() * cellSize /2;
                    g.fillRect(x, y, cellSize / 2, cellSize / 2);
                }
            } else if (piece.state == PieceState.IN_PLAY) {
                // paint piece in play.
                g.setColor(Color.BLUE);
                for (Cell cell : piece.shape) {
                    int x = piece.x + cell.x() * cellSize /2;
                    int y = piece.y + cell.y() * cellSize /2;
                    g.fillRect(x, y, cellSize / 2, cellSize / 2);
                }
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Block Puzzle Game");
            PuzzleGame puzzleGame = new PuzzleGame();

            frame.setResizable(false);
            frame.setContentPane(puzzleGame);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}

