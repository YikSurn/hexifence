/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Board {

    private int boardDimension;
    private char[][] boardState;
    private int possibleMoves;
    private ArrayList<Cell> cells = new ArrayList<Cell>();
    private HashMap<Edge, ArrayList<Cell>> EdgeToCells = new HashMap<Edge, ArrayList<Cell>>();

    public Board(int boardDimension, char[][] boardState) {
        this.boardDimension = boardDimension;
        this.boardState = boardState;
        this.storeState();
        this.possibleMoves = countPossibleMoves();
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
    
    public Edge getEdgeOfBoard(Point point) {
    	
    }
    
    /* Loop through the board, and get the point
     * 
     * */
    public int checkPoint(Point point) {
    	
    }

    private void storeState() {
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
        ArrayList<Point> edgePoints = new ArrayList<Point>(); // store points of edges
        for (int i = 0; i < cellPoints.size(); i++) {
            Cell cell = new Cell(cellPoints.get(i), cellActualPoints.get(i));
            int cellX = cellPoints.get(i).getX();
            int cellY = cellPoints.get(i).getY();

            if (this.boardState[cellX][cellY] != '-') {
                System.out.format("Error. The coordinate (%d, %d) should be a '-' to denote the location of a cell\n", cellX, cellY);
                System.exit(0);
            }

            ArrayList<Point> edgesPoints = getPointOfEdges(cell);
            for (Point edgeP: edgesPoints) {
                int pointX = edgeP.getX();
                int pointY = edgeP.getY();

                char value = this.boardState[pointX][pointY];

                if (value == '-') {
                    System.out.format("Error. The coordinate (%d, %d) should be a valid edge value: B, R or +\n", pointX, pointY);
                    System.exit(0);
                } else {
                    Edge edge = new Edge(new Point(pointX, pointY), value);
                    if (!edgePoints.contains(edge.getPointOnBoard())) {
                        // Create new key, values
                        ArrayList<Cell> edgeCells = new ArrayList<Cell>(2);
                        edgeCells.add(cell);
                        edgePoints.add(edge.getPointOnBoard());
                        EdgeToCells.put(edge, edgeCells);
                    }
                    else {
                        // Append to dictionary's values
                        ArrayList<Cell> edgeCells = EdgeToCells.get(edge);
                        edgeCells.add(cell);
                        EdgeToCells.put(edge, edgeCells);
                    }
                    cell.addEdge(edge);
                }
            }
            this.cells.add(cell);
        }
    }

    /* Returns the points of the edges of a cell
    */
    private ArrayList<Point> getPointOfEdges(Cell cell) {
        Point cellPoint = cell.getPointOnBoard();
        int cellX = cellPoint.getX();
        int cellY = cellPoint.getY();

        ArrayList<Point> edgesPoints = new ArrayList<Point>(6);

        edgesPoints.add(new Point(cellX - 1, cellY - 1));
        edgesPoints.add(new Point(cellX - 1, cellY));
        edgesPoints.add(new Point(cellX, cellY - 1));
        edgesPoints.add(new Point(cellX, cellY + 1));
        edgesPoints.add(new Point(cellX + 1, cellY));
        edgesPoints.add(new Point(cellX + 1, cellY + 1));

        return edgesPoints;
    }

    /* Return the points of the common edge between two cells
    */
    private Point getCommonEdgePoint(Cell c1, Cell c2) {
        Point c1Point = c1.getPointOnBoard();
        int c1X = c1Point.getX();
        int c1Y = c1Point.getY();
        Point c2Point = c2.getPointOnBoard();
        int c2X = c2Point.getX();
        int c2Y = c2Point.getY();

        // The common edge between two cells would have points
        // that are directly in the middle of the two cells
        int commonEdgePointX = (c1X + c2X)/2;
        int commonEdgePointY = (c1Y + c2Y)/2;
        Point commonEdgePoint = new Point(commonEdgePointX, commonEdgePointY);

        return commonEdgePoint;
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
