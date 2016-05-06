import aiproj.hexifence.*;

import java.io.PrintStream;

/*
 *  Random player
 */
public class RandomPlayer implements Player {

    /* This funstion is called by the referee to initialise the player.
     *  Return 0 for successful initialization and -1 for failed one.
     */
    @Override
    public int init(int n, int p) {

    }

    /* Function called by referee to request a move by the player.
     *  Return object of class Move
     */
    @Override
    public Move makeMove() {

    }

    /* Function called by referee to inform the player about the opponent's move
     * Return -1 if the move is illegal otherwise return 0 if no cell has been
     * captured by the opponent and return 1 if if one or more cells are captured
     * by the opponent
     */
    @Override
    public int opponentMove(Move m) {

    }

    /* This function when called by referee should return the winner
     *  Return -1, 0, 1, 2, 3 for INVALID, EMPTY, BLUE, RED, DEAD respectively
     */
    @Override
    public int getWinner() {

    }

    /* Function called by referee to get the board configuration in String format
     *
     */
    @Override
    public void printBoard(PrintStream output) {

    }
}
