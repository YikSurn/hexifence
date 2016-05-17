/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

package aiproj.hexifence.yikc;

import java.io.Serializable;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof Point) {
            Point another = (Point) obj;
            return (x == another.getX() && y == another.getY());
        } else {
            return false;
        }
    }

}
