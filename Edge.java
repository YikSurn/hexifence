import java.io.Serializable;

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
        this.hasBeenCaptured = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof Edge) {
            Edge another = (Edge) obj;
            return another.getPoint().equals(this.point);
        } else {
            return false;
        }
    }

	public int getCapturedBy() {
		return capturedBy;
	}

}
