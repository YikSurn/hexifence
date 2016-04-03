import Point;

public class Cell {

    private static int numSidesCaptured;
    private static int layerNum;
    private Point pointOnBoard;
    private Point actualPoint;
    private ArrayList<Edge> edges;
    private HashMap<Edge, Cell> = new HashMap<Edge, Cell>();

    public Cell(Point pointOnBoard, Point actualPoint) {
        this.pointOnBoard = pointOnBoard;
        this.actualPoint = actualPoint;
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

    /* Return true if this cell can be captured by one move, false otherwise
    */
    public boolean canCaptureByOneMove() {
        return (numSidesCaptured == 5);
    }

    public Point getPointOnBoard() {
        return this.pointOnBoard;
    }

}
