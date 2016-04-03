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
    public ArrayList<Point> adjacentCells(int boardSize) {
        ArrayList<Point> cellPoints = new ArrayList<Point>();
        int max = 2*boardSize - 2; // maximum x or y point
        int x = actualPoint.getX();
        int y = actualPoint.getY();
        boolean checkPoint = false;
        if (x-1 >= 0) {
            Point left = new Point(x-1, y);
            cellPoints.add(left);
        }
        if (y-1 >= 0) {
            Point top = new Point(x, y-1);
            cellPoints.add(top);
        }
        if (x-1 >=0 && y-1 >=0) {
            Point topleft = new Point(x-1, y-1);
            cellPoints.add(topLeft);
        }
        if (x+1 <= max) {
            Point right = new Point(x+1, y);
            cellPoints.add(right);
        }
        if (y+1 <= max) {
            Point bottom = new Point(x, y+1);
            cellPoints.add(bottom);
        }
        if (x+1 <= max && y+1 <= max) {
            Point bottomRight = new Point(x+1, y+1);
            cellPoints.add(bottomRight);
        }
        return cellPoints;
    }

    /* Return a list of edges that belongs to this cell
    */
    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    /* Return true if this cell can be captured by one move, false otherwise
    */
    public boolean canCaptureByOneMove() {
        return (numSidesCaptured == 5);
    }



}