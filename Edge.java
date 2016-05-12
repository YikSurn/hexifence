/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

public class Edge {

    private boolean hasBeenCaptured;
    private char capturedBy;
    private Point point;

    public Edge(Point p, char capturedBy) {
        this.setCapturedBy(capturedBy);
        if (capturedBy == 'R' || capturedBy == 'B') {
            this.hasBeenCaptured = true;
        }
        this.point = p;
    }

    public boolean getHasBeenCaptured() {
        return this.hasBeenCaptured;
    }

    public Point getPoint() {
        return this.point;
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
        if (another.getPoint().equals(this.point)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

	public char getCapturedBy() {
		return capturedBy;
	}

	public void setCapturedBy(char capturedBy) {
		this.capturedBy = capturedBy;
	}
}
