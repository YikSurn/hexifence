public class Edge {

    private boolean hasBeenCaptured;
    private char capturedBy;
    private Point pointOnBoard;

    public Edge(Point p, char capturedBy) {
        this.capturedBy = capturedBy;
        if (capturedBy == 'R' || capturedBy == 'B') {
            this.hasBeenCaptured = true;
        }
        this.pointOnBoard = p;
    }

}
