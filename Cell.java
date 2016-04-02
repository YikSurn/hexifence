import java.awt.Point;

public class Cell {

    private static int numSidesCaptured;
    private static int layerNum;
    private Point pointOnBoard;
    private Point actualPoint;
    private ArrayList<Edge> edges;
    private HashMap<Edge, Cell> = new HashMap<Edge, Cell>();

    public Cell(Point p) {
        this.pointOnBoard = p;
    }

    public int getNumSidesCaptured() {
        return this.numSidesCaptured;
    }

    /* Returns a list of actual points of this cell's adjacent cells
    */
    public ArrayList<Point> adjacentCells() {

    }

    /* Return a list of edge points that belongs to this cell
    */
    public ArrayList<Point> getEdges() {

    }

    /* Return true if this cell can be captured by one move, false otherwise
    */
    public boolean canCaptureByOneMove() {

    }



}