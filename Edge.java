import java.awt.Point;

public class Edge {

    private boolean hasBeenCaptured;
    private char capturedBy;
    private Point pointOnBoard;

    public Edge(Point p) {
        this.pointOnBoard = p;
    }

}