/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

import aiproj.hexifence.*;

import java.io.PrintStream;
import java.util.Random;

/*
 *  Random player
 */
public class RandomPlayer implements Player, Piece {

    private Board board;
    private int player;
    private int boardSize;

    /* This funstion is called by the referee to initialise the player.
     *  Return 0 for successful initialization and -1 for failed one.
     */
    @Override
    public int init(int n, int p) {
        if (p != BLUE && p != RED) {
            return INVALID;
        }

        this.player = p;
        this.boardSize = n;
        this.board = new Board(boardSize);

        return 0;
    }

    /* Function called by referee to request a move by the player.
     *  Return object of class Move
     */
    @Override
    public Move makeMove() {
        Random random = new Random();
        int max = this.board.getBoardDimension()*4 - 1;
        int row = random.nextInt(max);
        int col = random.nextInt(max);
        Point point = new Point(row, col);

        while (!this.board.validPoint(point)) {
            row = random.nextInt(max);
            col = random.nextInt(max);
            point.setPoint(row, col);
        }

        Move m = new Move();
        m.P = this.player;
        m.Row = row;
        m.Col = col;

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

        // Opponent's move is valid
        this.board.setLastOpponentPoint(point);
        this.board.update(m);
        if (this.board.isCapturingPoint(point)) {
            return 1;
        } else {
            // No cell has been captured
            return 0;
        }
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
