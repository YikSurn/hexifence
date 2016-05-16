/* Authors:

 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

import aiproj.hexifence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Board implements Piece, Serializable {

    private static final char NA_POINT = '-';
    private static final char EMPTY_EDGE = '+';
    private static final char RED_EDGE = 'R';
    private static final char BLUE_EDGE = 'B';
    private static final char RED_CELL = 'r';
    private static final char BLUE_CELL = 'b';
    private static final int HEXAGON = 6;

    private Point lastOpponentPoint;
    private int boardDimension;
    private int possibleMoves;
    private ArrayList<Cell> cells;
    private HashMap<Edge, ArrayList<Cell>> EdgeToCells = new HashMap<Edge, ArrayList<Cell>>();

    public Board(int boardDimension) {
        this.boardDimension = boardDimension;
        int cells = this.numberOfCells(boardDimension);
        this.cells = new ArrayList<Cell>(cells);

        this.newState();
        this.possibleMoves = this.countPossibleMoves();
    }

    /* A class method that generates a dictionary of edge point on board to its
     * respective cell(s) point(s)
     * */
    public static HashMap<Point, ArrayList<Point>> generateEdgeToCellsPoints() {
    	HashMap<Point, ArrayList<Point>> edgeToCellPoints = new HashMap<Point, ArrayList<Point>>();
        Edge edge;
        ArrayList<Cell> edgeCells;
        Point edgePoint;
        ArrayList<Point> cellPoints;

        for (Map.Entry<Edge,ArrayList<Cell>> entry : this.EdgeToCells.entrySet()) {
            edge = entry.getKey();
            edgeCells = entry.getValue();

            edgePoint = edge.getPoint();
            cellPoints = new ArrayList<Point>(2);
            for (Cell c: edgeCells) {
                cellPoints.add(c.getPointOnBoard());
            }

            edgeToCellPoints.put(edgePoint, cellPoints);
        }

    	return edgeToCellPoints;
    }

    public HashMap<Edge, ArrayList<Cell>> getEdgeToCells() {
		return EdgeToCells;
	}

	private int numberOfCells(int boardDimension) {
        if (boardDimension == 1) {
            return 1;
        }

        // with minimum board dimension of 2, number of cells in the outer later is (boardDimension-1)*6
        int outerCells = (boardDimension - 1) * HEXAGON;
        return numberOfCells(boardDimension - 1) + outerCells;
    }

    public int getBoardDimension() {
        return this.boardDimension;
    }

    public int getPossibleMoves() {
        return this.possibleMoves;
    }

    /* Returns maximum number of cells that can be captured with one move
     */
    public int maxCellCaptureByOneMove() {
        boolean atLeastOneCellCapturable = false;

        for (Cell c: this.cells) {
            if (c.canCaptureByOneMove()) {
                // Check if the uncaptured edge is the common edge to another capturable cell
                Edge uncapturedEdge = c.getUncapturedEdges().get(0);
                ArrayList<Cell> cells = this.EdgeToCells.get(uncapturedEdge);
                // Check if uncaptured edge has an adjacent cell
                if (cells.size() == 2) {
                    for (Cell adjacentCell: cells) {
                        // Check if the adjacent cell can be captured in one move
                        // If yes, max cells that can be captured is these two cells
                        if (adjacentCell != c && adjacentCell.canCaptureByOneMove()) {
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
        for (Cell hex: this.cells) {
            if (hex.canCaptureByOneMove()) {
                counter++;
            }
        }
        return counter;
    }

    private void newState() {
        int entries = this.boardDimension*4 - 1;

        // Retrieve the cells
        ArrayList<Point> cellActualPoints = new ArrayList<Point>();
        ArrayList<Point> cellPoints = new ArrayList<Point>();
        int countY;
        int countX = 0;
        for (int i = 1; i < entries; i += 2) {
            countY = 0;
            int leftIndent = Math.max(0, i - entries/2);
            int rightIndent = Math.max(0, entries/2 - i);
            for (int j = 1 + leftIndent; j < entries - rightIndent; j += 2) {
                cellPoints.add(new Point(i, j));
                cellActualPoints.add(new Point(countX, countY));
                countY++;
            }
            countX++;
        }

        // Loop through cell points, instantiate cells and add their edges to the cells
        HashMap<Point, Edge> allEdgePoints = new HashMap<Point, Edge>(); // (points of edge: edge)
        Edge edge;
        ArrayList<Cell> edgeCells;
        for (int i = 0; i < cellPoints.size(); i++) {
            Cell cell = new Cell(cellPoints.get(i), cellActualPoints.get(i));

            ArrayList<Point> edgePointsOfCell = Cell.getPointOfCellEdges(cellPoints.get(i));
            for (Point edgeP: edgePointsOfCell) {
                edge = null;

                // find if the point has already been retrieved
                for (Point point: allEdgePoints.keySet()) {
                    if (point.equals(edgeP)) {
                        edge = allEdgePoints.get(point);
                    }
                }

                if (edge != null) {
                    // edge already exist/created
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

    /* Return the points of the common edge between two cells
    */
//    private Point getCommonEdgePoint(Cell c1, Cell c2) {
//        Point c1Point = c1.getPointOnBoard();
//        int c1X = c1Point.getX();
//        int c1Y = c1Point.getY();
//        Point c2Point = c2.getPointOnBoard();
//        int c2X = c2Point.getX();
//        int c2Y = c2Point.getY();
//
//        // The common edge between two cells would have points
//        // that are directly in the middle of the two cells
//        int commonEdgePointX = (c1X + c2X)/2;
//        int commonEdgePointY = (c1Y + c2Y)/2;
//        Point commonEdgePoint = new Point(commonEdgePointX, commonEdgePointY);
//
//        return commonEdgePoint;
//    }

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

    private Edge getEdge(Point p) {
        for (Edge e: this.EdgeToCells.keySet()) {
            if (e.getPoint().equals(p)) {
                return e;
            }
        }

        // No such point
        return null;
    }

    /* Return true if point is a valid empty edge point of a hexifence */
    public boolean validPoint(Point point) {
    	Edge edge = this.getEdge(point);
    	if (!isEdgePoint(point) || edge.getHasBeenCaptured()) {
    		return false;
    	}
    	return true;
    }

    /* Return true if point is an edge point, false otherwise */
    public boolean isEdgePoint(Point point) {
        Edge edge = this.getEdge(point);
        if (edge == null) {
            return false;
        }
        return true;
    }

    public boolean isCapturingPoint(Point point) {
        Edge edge = this.getEdge(point);
        for (Cell c: this.EdgeToCells.get(edge)) {
            if (c.canCaptureByOneMove()) {
                return true;
            }
        }

        return false;
    }

    public int getPlayerCells(int player) {
        int count = 0;

        for (Cell c: this.cells) {
            if (c.getCapturedBy() == player) {
                count++;
            }
        }

        return count;
    }

    public int getWinner() {
        if (this.getPossibleMoves() > 0) {
            return EMPTY;
        } else if (!this.isEdgePoint(this.lastOpponentPoint)) {
            return INVALID;
        }

        int redCells = this.getPlayerCells(RED);
        int blueCells = this.getPlayerCells(BLUE);

        if (redCells == blueCells) {
            return DEAD;
        } else if (redCells > blueCells) {
            return RED;
        } else {
            return BLUE;
        }
    }

    public void setLastOpponentPoint(Point point) {
        this.lastOpponentPoint = point;
    }

    public void update(Move m) {
        Point point = new Point(m.Row, m.Col);

        Edge edge = this.getEdge(point);
        edge.setCapturedBy(m.P);
        this.possibleMoves--;

        // Update the cell's edges where the captured edge belongs to
        for (Cell c: this.EdgeToCells.get(edge)) {
            c.edgeCapturedUpdate(m.P);
        }
    }


    /* Return two-dimension char array that represents the current
     * board state of the game
     * */
    public char[][] getBoardIn2DArray() {
    	char value;
    	Point p;
    	int x, y;
    	int boardSize = calcBoardSize();
        char[][] board = new char[boardSize][boardSize];

        for (Edge e: this.EdgeToCells.keySet()) {
            if (e.getHasBeenCaptured()) {
                if (e.getCapturedBy() == RED) {
                    value = RED_EDGE;
                } else {
                    value = BLUE_EDGE;
                }
            } else {
                value = EMPTY_EDGE;
            }

            p = e.getPoint();
            x = p.getX();
            y = p.getY();
            board[x][y] = value;
        }

        for (Cell c: this.cells) {
            if (c.isCaptured()) {
                if (c.getCapturedBy() == RED) {
                    value = RED_CELL;
                } else {
                    value = BLUE_CELL;
                }

                p = c.getPointOnBoard();
                x = p.getX();
                y = p.getY();
                board[x][y] = value;
            }
        }

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = NA_POINT;
                }
            }
        }
        return board;

    }

    /* Print the board in rows x cols square format */
    public void printBoard() {
        // To be removed, print now for extra stats on the game results
        this.printOngoingStats();

        char[][] board = getBoardIn2DArray();
        int boardSize = calcBoardSize();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (col != 0) {
                    System.out.print(' ');
                }
                System.out.print(board[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public Point pointToCaptureCell() {
        for (Cell c: this.cells) {
            if (c.canCaptureByOneMove()) {
                Edge edge = c.getUncapturedEdges().get(0);
                return edge.getPoint();
            }
        }

        return null;
    }

    public ArrayList<Edge> getAllUncapturedEdges() {
        ArrayList<Edge> uncapturedEdges = new ArrayList<Edge>();
        for (Edge e: this.EdgeToCells.keySet()) {
            if (!e.getHasBeenCaptured()) {
                uncapturedEdges.add(e);
            }
        }

        return uncapturedEdges;
    }

    // Edges where it would not lead to an opponent capturing a cell in the next turn
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

    private void printOngoingStats() {
        int blueCells = this.getPlayerCells(BLUE);
        int redCells = this.getPlayerCells(RED);

        System.out.println("Number of cells (BLUE): " + blueCells);
        System.out.println("Number of cells (RED) : " + redCells);
    }

    /* Return size of board in terms of max rows/cols it can have
     * */
    public int calcBoardSize() {
    	return  this.boardDimension*4 - 1;
    }
}
