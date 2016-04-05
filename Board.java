import java.util.ArrayList;

public class Board {

    private static int boardSize;
    private char[][] boardState;
    private int possibleMoves;
    private ArrayList<Cell> cells;
    private HashMap<Edge, ArrayList<Cell>> EdgeToCells = new HashMap<Edge, ArrayList<Cell>>();

    public Board(int boardSize, char[][] boardState) {
        this.boardSize = boardSize;
        this.boardState = boardState;
        this.storeState();
    }

    private void storeState() {
        int entries = this.boardSize*4 - 1;

        // Retrieve the cells
        ArrayList<Point> cellActualPoints = new ArrayList<Point>();
        ArrayList<Point> cellPoints = new ArrayList<Point>();
        int countY;
        int countX = 0;
        Point p;
        for (int i = 1; i < entries; i += 2) {
            countY = 0;
            int leftIndent = Math.max(0, i - entries/2);
            int rightIndent = Math.max(0, entries/2 - i);
            for (int j = 1 + leftIndent; j < entries - rightIndent; j += 2) {
                cellPoints.add(new Point(i, j));
                cellActualPoints.add(new Point(countX, countY));
                countY++;
            }
            countX++;
        }

        // Loop through cell points, instantiate cells and add their edges to the cells
        for (int i = 0; i < cellPoints.size(); i++) {
            Cell cell = new Cell(cellPoints.get(i), cellActualPoints.get(i));
            int cellX = cellPoints.get(i).getX();
            int cellY = cellPoints.get(i).getY();

            if (this.boardState[cellX][cellY] != '-') {
                System.out.format("Error. The coordinate (%d, %d) should be a '-' to denote the location of a cell\n", cellX, cellY);
            }

            ArrayList<Point> edgesPoints = getPointOfEdges(cell);
            for (Point edgeP: edgesPoints) {
                int pointX = edgeP.getX();
                int pointY = edgeP.getY();

                char value = this.boardState[pointX][pointY];

                if (value == '-') {
                    System.out.format("Error. The coordinate (%d, %d) should be a valid edge value: B, R or +\n", pointX, pointY);
                } else {
                    Edge edge = new Edge(new Point(pointX, pointY), value);
                    if (EdgeToCells.containsKey(edge)) {
                        // Append to dictionary's values
                        ArrayList<Cell> edgeCells = EdgeToCells.get(edge);
                        edgeCells.add(cell);
                        EdgeToCells.put(edge, cell);
                    }
                    else {
                        // Create new key, values
                        ArrayList<Cell> edgeCells = new ArrayList<Cell>();
                        edgeCells.add(cell);
                        EdgeToCells.put(edge, cell);
                    }
                    cell.addEdge(edge);
                }
            }
            this.cells.add(cell);
        }
    }

    public int getPossibleMoves() {
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
        int commonEdgePointX = (c1X + c2X)/2;
        int commonEdgePointY = (c1Y + c2Y)/2;
        Point commonEdgePoint = new Point(commonEdgePointX, commonEdgePointY);

        return commonEdgePoint;
    }

    /* Return max cell available for captured
    */
    public int maxCellAvailableCapture() {
        int counter = 0;
        for(Cell hex: this.cells) {
            if (hex.canCaptureByOneMove()) {
                counter++;
            }
        }
        return counter;
    }

    public int maxCellCaptureByOneMove() {

    }
}
