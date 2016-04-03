import Cell;
import Point;

public class Board {

    private static int boardSize;
    private char[][] boardState;
    private int possibleMoves;
    private ArrayList<Cell> cells;

    public Board(int boardSize) {
        this.boardSize = boardSize;
    }

    public getPossibleMoves() {
        return possibleMoves;
    }

    /* Returns the points of the edges of a cell
    */
    private ArrayList<Point> getPointOfEdges(Cell cell) {
        Point cellPoint = cell.getPointOnBoard();
        int cellX = cellPoint.getX();
        int cellY = cellPoint.getY();

        ArrayList<Point> edgesPoints = new ArrayList<Point>(6);

        edgesPoints.add(new Point(cellX - 1, cellY - 1));
        edgesPoints.add(new Point(cellX - 1, cellY));
        edgesPoints.add(new Point(cellX, cellY - 1));
        edgesPoints.add(new Point(cellX, cellY + 1));
        edgesPoints.add(new Point(cellX + 1, cellY));
        edgesPoints.add(new Point(cellX + 1, cellY + 1));

        return edgesPoints;
    }

    /* Return the coordinates of the common edge between two cells
    */
    private Point getCommonEdgePoint(Cell c1, Cell c2) {
        Point c1Point = c1.getPointOnBoard();
        int c1X = c1Point.getX();
        int c1Y = c1Point.getY();
        Point c2Point = c2.getPointOnBoard();
        int c2X = c2Point.getX();
        int c2Y = c2Point.getY();

        // The common edge between two cells would have coordinates
        // that are directly in the middle of the two cells
        commonEdgePointX = (c1X + c2X)/2;
        commonEdgePointY = (c1Y + c2Y)/2;
        Point commonEdgePoint = new Point(commonEdgePointX, commonEdgePointY);

        return commonEdgePoint;
    }
}
