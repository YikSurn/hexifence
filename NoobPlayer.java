/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

import aiproj.hexifence.*;

import java.io.PrintStream;
import java.util.Random;
import java.util.ArrayList;

/*
 *  Noob player
 *  Makes random moves but does not choose an edge where number of uncaptured edges in the cell is 2
 *  Captures a cell if it could be captured
 */
public class NoobPlayer implements Player, Piece {

    private Board board;
    private int player;
    private int boardDimension;

    /* This function is called by the referee to initialize the player.
     *  Return 0 for successful initialization and -1 for failed one.
     */
    @Override
    public int init(int n, int p) {
        if (p != BLUE && p != RED) {
            return INVALID;
        }

        this.player = p;
        this.boardDimension = n;
        this.board = new Board(this.boardDimension);

        return 0;
    }

    /* Function called by referee to request a move by the player.
     *  Return object of class Move
     */
    @Override
    public Move makeMove() {
        Point chosenPoint;

        // Captures a cell if it could be captured
        Point capturePoint = this.board.pointToCaptureCell();
        if (capturePoint != null) {
            chosenPoint = capturePoint;
        }
        else {
            ArrayList<Edge> edgesToRandomFrom;

            ArrayList<Edge> safeEdges = this.board.getSafeEdges();
            if (safeEdges.size() == 0) {
                edgesToRandomFrom = this.board.getAllUncapturedEdges();
            } else {
                edgesToRandomFrom = safeEdges;
            }

            int numEdges = edgesToRandomFrom.size();

            Random random = new Random();
            int indexEdgeChosen = random.nextInt(numEdges);
            chosenPoint = edgesToRandomFrom.get(indexEdgeChosen).getPoint();
        }

        Move m = new Move();
        m.P = this.player;
        m.Row = chosenPoint.getX();
        m.Col = chosenPoint.getY();

        this.board.update(m);
        return m;
    }

    /* Function called by referee to inform the player about the opponent's move
     * Return -1 if the move is illegal otherwise return 0 if no cell has been
     * captured by the opponent and return 1 if one or more cells are captured
     * by the opponent
     */
    @Override
    public int opponentMove(Move m) {
        Point point = new Point(m.Row, m.Col);

        // Check for invalidity
        if (m.P == this.player) {
            // Check if opponent incorrectly labelled the move as player's own move
            return INVALID;
        } else if (!this.board.validPoint(point)) {
            return INVALID;
        }

        // Opponent's move is valid, check if the move captures a cell
        int value;
        if (this.board.isCapturingPoint(point)) {
            value = 1;
        } else {
            // No cell has been captured
            value = 0;
        }

        // Record the move and update board
        this.board.setLastOpponentPoint(point);
        this.board.update(m);
        return value;
    }

    /* This function when called by referee should return the winner
     *  Return -1, 0, 1, 2, 3 for INVALID, EMPTY, BLUE, RED, DEAD respectively
     */
    @Override
    public int getWinner() {
        return this.board.getWinner();
    }

    /* Function called by referee to get the board configuration in String format
     *
     */
    @Override
    public void printBoard(PrintStream output) {
        this.board.printBoard();
    }
}
