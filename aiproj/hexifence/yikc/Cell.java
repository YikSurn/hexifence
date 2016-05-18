/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

package aiproj.hexifence.yikc;

import java.util.ArrayList;

/* Hexagon class
 */
public class Cell {

    public static final int MAX_EDGES = 6;

    private boolean captured;
    private int capturedBy;
    private int numSidesUncaptured;
    private Point pointOnBoard;
    private Point actualPoint; // Identifier for the cells

    private ArrayList<Edge> edges;

    public Cell(Point pointOnBoard, Point actualPoint) {
        this.pointOnBoard = pointOnBoard;
        this.actualPoint = actualPoint;
        this.numSidesUncaptured = MAX_EDGES;
        this.captured = false;
        this.edges = new ArrayList<Edge>();
    }

    /* Returns the points of the edges of a cell
     */
    public static ArrayList<Point> getPointOfCellEdges(Point cellPointOnBoard) {
        int cellX = cellPointOnBoard.getX();
        int cellY = cellPointOnBoard.getY();

        ArrayList<Point> edgePointsOfCell = new ArrayList<Point>(MAX_EDGES);

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

    public void addEdge(Edge e) {
        this.edges.add(e);
    }

    public boolean isCaptured() {
        return this.captured;
    }

    public int getCapturedBy() {
        return this.capturedBy;
    }

    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    public Point getPointOnBoard() {
        return this.pointOnBoard;
    }

    /* Updates the cell on capture of one of its edges
     */
    public void edgeCapturedUpdate(int player) {
        this.numSidesUncaptured = this.getUncapturedEdges().size();
        if (this.numSidesUncaptured == 0) {
            this.captured = true;
            this.capturedBy = player;
        }
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

    /* Return true if this cell can be captured in one move, false otherwise
     */
    public boolean canCaptureInOneMove() {
        return (this.numSidesUncaptured == 1);
    }

}
