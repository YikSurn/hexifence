/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

public class Edge {

    private boolean hasBeenCaptured;
    private int capturedBy;
    private Point point;

    public Edge(Point p) {
        this.hasBeenCaptured = false;
        this.point = p;
    }

    public boolean getHasBeenCaptured() {
        return this.hasBeenCaptured;
    }

    public Point getPoint() {
        return this.point;
    }

    public void setCapturedBy(int player) {
        this.capturedBy = player;
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

	public int getCapturedBy() {
		return capturedBy;
	}

}
