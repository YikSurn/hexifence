/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

import java.util.ArrayList;

public class Cell {

    public static final int MAX_EDGES = 6;

    private int numSidesCaptured;
    private int layerNum;
    private Point pointOnBoard;
    private Point actualPoint;
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    // private HashMap<Edge, Cell> = new HashMap<Edge, Cell>();

    public Cell(Point pointOnBoard, Point actualPoint) {
        this.pointOnBoard = pointOnBoard;
        this.actualPoint = actualPoint;
        this.numSidesCaptured = 0;
    }

    public int getNumSidesCaptured() {
        return this.numSidesCaptured;
    }

    public void addEdge(Edge e) {
        this.edges.add(e);
        if (e.getHasBeenCaptured()) {
            this.numSidesCaptured++;
        }
    }

    /* Returns a list of actual points of this cell's adjacent cells
    */
    public ArrayList<Point> adjacentCells(int boardSize) {
        ArrayList<Point> cellPoints = new ArrayList<Point>();
        int max = 2*boardSize - 2; // maximum x or y point
        int x = actualPoint.getX();
        int y = actualPoint.getY();
        boolean checkPoint = false;
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
        ArrayList<Edge> edges = new ArrayList<Edge>(this.edges.size());
        for (Edge edge: this.edges) {
            if (!edge.getHasBeenCaptured()) {
                edges.add(edge);
            }
        }
        return edges;
    }

    /* Return true if this cell can be captured by one move, false otherwise
    */
    public boolean canCaptureByOneMove() {
        return (numSidesCaptured == Cell.MAX_EDGES - 1);
    }

    public Point getPointOnBoard() {
        return this.pointOnBoard;
    }

}
