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

    public boolean getHasBeenCaptured() {
        return hasBeenCaptured;
    }

    public Point getPointOnBoard() {
        return pointOnBoard;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Edge another = (Edge) obj;
        if (another.getPointOnBoard().equals(pointOnBoard)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
