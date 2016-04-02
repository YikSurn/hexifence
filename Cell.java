import java.awt.Point;

public class Cell {

    private static int numSidesCaptured;
    private static int layerNum;
    private Point pointOnBoard;
    private HashMap<Edge, Cell> = new HashMap<Edge, Cell>();

    public Cell(Point p) {
        this.pointOnBoard = p;
    }

    public int getNumSidesCaptured() {
        return this.numSidesCaptured;
    }

}