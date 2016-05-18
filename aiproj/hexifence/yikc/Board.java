/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

package aiproj.hexifence.yikc;

import aiproj.hexifence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* Board class
 */
public class Board implements Piece {

    public static final char NA_POINT = '-';
    public static final char EMPTY_EDGE = '+';
    public static final char RED_EDGE = 'R';
    public static final char BLUE_EDGE = 'B';
    public static final char RED_CELL = 'r';
    public static final char BLUE_CELL = 'b';
    public static final int HEXAGON = 6;

    private Point lastOpponentPoint;

    private int boardDimension;
    private int boardSize;
    private int possibleMoves;

    private ArrayList<Cell> cells;
    private HashMap<Edge, ArrayList<Cell>> EdgeToCells;

    public Board(int boardDimension) {
        int numCells = Board.numberOfCells(boardDimension);

        this.boardDimension = boardDimension;
        this.boardSize = (boardDimension * 4) - 1;

        this.cells = new ArrayList<Cell>(numCells);
        this.EdgeToCells = new HashMap<Edge, ArrayList<Cell>>();

        this.newState();
        this.possibleMoves = this.countPossibleMoves();
    }

    /* Recursive function to return number of cells in a board given the board dimension
     */
    public static int numberOfCells(int boardDimension) {
        int outerCells;

        // Base case
        if (boardDimension == 1) {
            return 1;
        }

        // number of cells in the outer later is (boardDimension-1)*6
        outerCells = (boardDimension - 1) * HEXAGON;
        return numberOfCells(boardDimension - 1) + outerCells;
    }

    public HashMap<Edge, ArrayList<Cell>> getEdgeToCells() {
        return this.EdgeToCells;
    }

    public int getBoardDimension() {
        return this.boardDimension;
    }

    public int getPossibleMoves() {
        return this.possibleMoves;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public void setLastOpponentPoint(Point point) {
        this.lastOpponentPoint = point;
    }

    /* A class method that generates a dictionary of each edge point on board to its
     * respective cell(s) point(s)
     */
    public HashMap<Point, ArrayList<Point>> generateEdgeToCellsPoints() {
        int MAX_CELL = 2;
        Edge edge;
        Point edgePoint;
        ArrayList<Cell> edgeCells;
        ArrayList<Point> cellPoints;
        HashMap<Point, ArrayList<Point>> edgeToCellPoints = new HashMap<Point, ArrayList<Point>>();

        for (Map.Entry<Edge,ArrayList<Cell>> entry : this.EdgeToCells.entrySet()) {
            edge = entry.getKey();
            edgeCells = entry.getValue();

            edgePoint = edge.getPoint();
            cellPoints = new ArrayList<Point>(MAX_CELL);
            for (Cell c: edgeCells) {
                cellPoints.add(c.getPointOnBoard());
            }

            edgeToCellPoints.put(edgePoint, cellPoints);
        }

        return edgeToCellPoints;
    }

    /* Returns maximum number of cells that can be captured with one move
     */
    public int maxCellCaptureByOneMove() {
        Edge uncapturedEdge;
        ArrayList<Cell> cells;
        boolean atLeastOneCellCapturable = false;

        for (Cell c: this.cells) {
            if (c.canCaptureInOneMove()) {
                uncapturedEdge = c.getUncapturedEdges().get(0);
                cells = this.EdgeToCells.get(uncapturedEdge);

                // Check if uncaptured edge has an another adjacent cell
                if (cells.size() == 2) {
                    for (Cell adjacentCell: cells) {
                        // Check if the adjacent cell can be captured in one move
                        // If yes, max cells that can be captured is these two cells
                        if (adjacentCell != c && adjacentCell.canCaptureInOneMove()) {
                            return 2;
                        }
                    }
                }

                // The current edge can only capture one cell
                if (!atLeastOneCellCapturable) {
                    atLeastOneCellCapturable = true;
                }
            }
        }

        if (atLeastOneCellCapturable) {
            return 1;
        } else {
            return 0;
        }
    }

    /* Return total number of cells available for capture
     */
    public int numCellsAvailableForCapture() {
        int counter = 0;

        for (Cell cell: this.cells) {
            if (cell.canCaptureInOneMove()) {
                counter++;
            }
        }
        return counter;
    }

    /* Return the edge in the board given a point
     */
    public Edge getEdge(Point p) {
        for (Edge e: this.EdgeToCells.keySet()) {
            if (e.getPoint().equals(p)) {
                return e;
            }
        }

        // No such point
        return null;
    }

    /* Return true if point is a valid empty edge point on the board
     */
    public boolean validPoint(Point point) {
        Edge edge = this.getEdge(point);

        if (!isEdgePoint(point) || edge.getHasBeenCaptured()) {
            return false;
        }

        return true;
    }

    /* Return true if point is an edge point, false otherwise
     */
    public boolean isEdgePoint(Point point) {
        Edge edge = this.getEdge(point);

        if (edge == null) {
            return false;
        }

        return true;
    }

    /* Return true if the point captures at least one cell
     */
    public boolean isCapturingPoint(Point point) {
        Edge edge = this.getEdge(point);

        for (Cell c: this.EdgeToCells.get(edge)) {
            if (c.canCaptureInOneMove()) {
                return true;
            }
        }

        return false;
    }

    /* Returns number of cells captured by a player
     */
    public int getPlayerCells(int player) {
        int count = 0;

        for (Cell c: this.cells) {
            if (c.getCapturedBy() == player) {
                count++;
            }
        }

        return count;
    }

    /* Returns the winner of the game
     */
    public int getWinner() {
        if (this.getPossibleMoves() > 0) {
            // Game has not ended yet
            return EMPTY;
        } else if (!this.isEdgePoint(this.lastOpponentPoint)) {
            // Previous move of opponent is invalid
            return INVALID;
        }

        int redCells = this.getPlayerCells(RED);
        int blueCells = this.getPlayerCells(BLUE);

        if (redCells == blueCells) {
            // Game is a draw, which shouldn't occur as there are an odd number of cells
            return DEAD;
        } else if (redCells > blueCells) {
            return RED;
        } else {
            return BLUE;
        }
    }

    /* Updates the state of the game with the move made
     */
    public void update(Move m) {
        Point point = new Point(m.Row, m.Col);

        Edge edge = this.getEdge(point);
        edge.setCapturedBy(m.P);
        this.possibleMoves--;

        // Update the cells where the edge belongs to
        for (Cell c: this.EdgeToCells.get(edge)) {
            c.edgeCapturedUpdate(m.P);
        }
    }

    /* Return 2D char array that represents the current
     * board state of the game
     * */
    public char[][] getBoardIn2DArray() {
        char value;
        Point p;
        int x, y;

        char[][] board = new char[this.boardSize][this.boardSize];

        // Fill the board with value of edges
        for (Edge e: this.EdgeToCells.keySet()) {
            p = e.getPoint();
            x = p.getX();
            y = p.getY();

            if (e.getHasBeenCaptured()) {
                if (e.getCapturedBy() == RED) {
                    value = RED_EDGE;
                } else {
                    value = BLUE_EDGE;
                }
            } else {
                value = EMPTY_EDGE;
            }

            board[x][y] = value;
        }

        // Fill the board with value of cells
        for (Cell c: this.cells) {
            p = c.getPointOnBoard();
            x = p.getX();
            y = p.getY();

            if (c.isCaptured()) {

                if (c.getCapturedBy() == RED) {
                    value = RED_CELL;
                } else {
                    value = BLUE_CELL;
                }
            } else {
                value = NA_POINT;
            }

            board[x][y] = value;
        }

        // Fills up the other parts of the board with a '-'
        for (int i = 0; i < this.boardSize; i++) {
            for (int j = 0; j < this.boardSize; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = NA_POINT;
                }
            }
        }

        return board;
    }

    /* Print the board in rows x cols square format
     */
    public void printBoard() {
        char[][] board = getBoardIn2DArray();

        for (int row = 0; row < this.boardSize; row++) {
            for (int col = 0; col < this.boardSize; col++) {

                // Print whitespace between the values
                // by printing before each value except the first
                if (col != 0) {
                    System.out.print(' ');
                }

                System.out.print(board[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /* Return a point that captures at least one cell
     */
    public Point getCapturableCellPoint() {
        for (Cell c: this.cells) {
            if (c.canCaptureInOneMove()) {
                Edge edge = c.getUncapturedEdges().get(0);
                return edge.getPoint();
            }
        }

        // No cells capturable with one point
        return null;
    }

    /* Return all the uncaptured edges of the board state
     */
    public ArrayList<Edge> getAllUncapturedEdges() {
        ArrayList<Edge> uncapturedEdges = new ArrayList<Edge>();

        for (Edge e: this.EdgeToCells.keySet()) {
            if (!e.getHasBeenCaptured()) {
                uncapturedEdges.add(e);
            }
        }

        return uncapturedEdges;
    }

    /* Return edges where it would not lead to an opponent capturing a cell in the next turn
     */
    public ArrayList<Edge> getSafeEdges() {
        int MIN_SAFE_UNCAPTURED_EDGES = 3;
        ArrayList<Edge> uncapturedEdges = this.getAllUncapturedEdges();
        ArrayList<Edge> safeEdges = new ArrayList<Edge>();

        boolean safeEdge;
        for (Edge e: uncapturedEdges) {
            safeEdge = true;
            for (Cell c: this.EdgeToCells.get(e)) {
                if (c.getNumSidesUncaptured() < MIN_SAFE_UNCAPTURED_EDGES) {
                    safeEdge = false;
                    break;
                }
            }

            if (safeEdge == true) {
                safeEdges.add(e);
            }
        }

        return safeEdges;
    }


    /* Initialize a new state of the board
     */
    private void newState() {
        ArrayList<Point> cellActualPoints = new ArrayList<Point>();
        ArrayList<Point> cellPoints = new ArrayList<Point>();

        // Retrieve the cells
        int countX = 0;
        int countY;
        int leftIndent;
        int rightIndent;
        int boardSize = this.boardDimension*4 - 1;
        for (int i = 1; i < boardSize; i += 2) {
            countY = 0;
            leftIndent = Math.max(0, i - boardSize/2);
            rightIndent = Math.max(0, boardSize/2 - i);

            for (int j = 1 + leftIndent; j < (boardSize - rightIndent); j += 2) {
                cellPoints.add(new Point(i, j));
                cellActualPoints.add(new Point(countX, countY));
                countY++;
            }
            countX++;
        }

        // Loop through cell points, instantiate cells and add their edges to the cells
        Cell cell;
        Edge edge;
        ArrayList<Cell> edgeCells;
        ArrayList<Point> edgePointsOfCell;
        HashMap<Point, Edge> allEdgePoints = new HashMap<Point, Edge>(); // {points of edge: edge}
        for (int i = 0; i < cellPoints.size(); i++) {
            cell = new Cell(cellPoints.get(i), cellActualPoints.get(i));
            edgePointsOfCell = Cell.getPointOfCellEdges(cellPoints.get(i));

            for (Point edgeP: edgePointsOfCell) {
                edge = null;

                // find if the point has already been retrieved
                for (Point point: allEdgePoints.keySet()) {
                    if (point.equals(edgeP)) {
                        edge = allEdgePoints.get(point);
                    }
                }

                if (edge != null) {
                    // edge already exist/created, update the edge with list of cells its
                    // associated with
                    edgeCells = EdgeToCells.get(edge);

                    edgeCells.add(cell);
                    EdgeToCells.put(edge, edgeCells);
                } else {
                    // create new edge
                    edgeCells = new ArrayList<Cell>(2);
                    edge = new Edge(edgeP);

                    edgeCells.add(cell);
                    allEdgePoints.put(edgeP, edge);
                    EdgeToCells.put(edge, edgeCells);
                }

                cell.addEdge(edge);
            }

            this.cells.add(cell);
        }
    }

    /* Count all possible moves in a board state */
    private int countPossibleMoves() {
        int possibleMoves = 0;

        for (Edge edge: EdgeToCells.keySet()) {
            if (!edge.getHasBeenCaptured()) {
                possibleMoves++;
            }
        }
        return possibleMoves;
    }

}
