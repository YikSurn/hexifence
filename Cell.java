/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

import java.io.Serializable;
import java.util.ArrayList;

public class Cell implements Serializable {

    public static final int MAX_EDGES = 6;

    private int capturedBy;
    private boolean captured;
    private int numSidesUncaptured;
    private Point pointOnBoard;
    private Point actualPoint;
    private ArrayList<Edge> edges = new ArrayList<Edge>();

    public Cell(Point pointOnBoard, Point actualPoint) {
        this.pointOnBoard = pointOnBoard;
        this.actualPoint = actualPoint;
        this.numSidesUncaptured = MAX_EDGES;
        this.captured = false;
    }

    /* Returns the points of the edges of a cell
    */
    public static ArrayList<Point> getPointOfCellEdges(Point cellPointOnBoard) {
        int cellX = cellPointOnBoard.getX();
        int cellY = cellPointOnBoard.getY();

        ArrayList<Point> edgePointsOfCell = new ArrayList<Point>(6);

        edgePointsOfCell.add(new Point(cellX - 1, cellY - 1));
        edgePointsOfCell.add(new Point(cellX - 1, cellY));
        edgePointsOfCell.add(new Point(cellX, cellY - 1));
        edgePointsOfCell.add(new Point(cellX, cellY + 1));
        edgePointsOfCell.add(new Point(cellX + 1, cellY));
        edgePointsOfCell.add(new Point(cellX + 1, cellY + 1));

        return edgePointsOfCell;
    }

    public int getNumSidesUncaptured() {
        return this.numSidesUncaptured;
    }

    public void edgeCapturedUpdate(int player) {
        this.numSidesUncaptured = this.getUncapturedEdges().size();
        if (this.numSidesUncaptured == 0) {
            this.captured = true;
            this.capturedBy = player;
        }
    }

    public void addEdge(Edge e) {
        this.edges.add(e);
    }

    public boolean isCaptured() {
        return this.captured;
    }

    public int getCapturedBy() {
        return this.capturedBy;
    }

    /* Returns a list of actual points of this cell's adjacent cells
    */
    public ArrayList<Point> adjacentCells(int boardSize) {
        ArrayList<Point> cellPoints = new ArrayList<Point>();
        int max = 2*boardSize - 2; // maximum x or y point
        int x = actualPoint.getX();
        int y = actualPoint.getY();
        if (x-1 >= 0) {
            cellPoints.add(new Point(x-1, y));
        }
        if (y-1 >= 0) {
            cellPoints.add(new Point(x, y-1));
        }
        if (x-1 >=0 && y-1 >=0) {
            cellPoints.add(new Point(x-1, y-1));
        }
        if (x+1 <= max) {
            cellPoints.add(new Point(x+1, y));
        }
        if (y+1 <= max) {
            cellPoints.add(new Point(x, y+1));
        }
        if (x+1 <= max && y+1 <= max) {
            cellPoints.add(new Point(x+1, y+1));
        }
        return cellPoints;
    }

    /* Return a list of edges that belongs to this cell
    */
    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    /* Return a list of uncaptured edges that belongs to this cell
    */
    public ArrayList<Edge> getUncapturedEdges() {
        ArrayList<Edge> uncapturedEdges = new ArrayList<Edge>(this.edges.size());
        for (Edge edge: this.edges) {
            if (!edge.getHasBeenCaptured()) {
                uncapturedEdges.add(edge);
            }
        }
        return uncapturedEdges;
    }

    /* Return true if this cell can be captured by one move, false otherwise
    */
    public boolean canCaptureByOneMove() {
        return (this.numSidesUncaptured == 1);
    }

    public Point getPointOnBoard() {
        return this.pointOnBoard;
    }

}
