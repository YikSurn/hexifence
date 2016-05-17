import java.io.PrintStream;

import java.util.ArrayList;
import java.util.HashMap;

import aiproj.hexifence.Move;
import aiproj.hexifence.Piece;
import aiproj.hexifence.Player;

/* A simple player that uses a heuristic to evaluate/estimate 
existing board state until a certain threshold; 
After a certain threshold is reached, an alpha-beta pruning
strategy is used to come up with player's best move
 * */
public class HeuristicEvalPlayer implements Player, Piece {

    private static int THRESHOLD = 20;
    private Board board;
    private int boardDimension;
    private int player;
    private char cellIdentity;
    private char edgeIdentity;
    private int oppPlayer;
    private char oppCellIdentity;
    private char oppEdgeIdentity;
    private HashMap<Point, ArrayList<Point>> edgeAssociatedCells;

    /* TEMP COMMENT: extra functions required */

    /* If heuristic returns 0, this suggests that 
    the boardState has reaches its intended "goal state"
    */
    private int calcHeuristic(char[][] boardState) {
    	return 0;
    }

    /* Return a list of possible optimal move based on a given
    condition (so as to only consider these moves)
    */
    private ArrayList<Move> conditionalBestMove() {
    	return new ArrayList<Move>();
    }

    /* IDA* algorithm to find the best set of strategies from a given
    start node to any goal node in the problem graph 
    Provided that heuristic is admissible! 
    */
    private HashMap<Integer, Move> iterativeDeepeningStar() {
    	return null;
    }


    /* The actual recursive call/search of IDA* algorithm
    */
    private HashMap<Integer, Move> iterativeDeepningSearch(char[][] boardState, int initBound, int newBound) {
    	return null;
    }

    /* Alpha beta pruning algorithm -- the most efficient algorithm we can come up with
    */
    private HashMap<Integer, Move> improvedAlphaBeta() {
    	return null;
    }

    @Override
    public int init(int n, int p) {
        if (p != BLUE && p != RED) {
            return INVALID;
        }
        this.boardDimension = n;
        this.board = new Board(this.boardDimension);
        this.edgeAssociatedCells = this.board.generateEdgeToCellsPoints();

        this.player = p;
        if (p == BLUE) {
            this.cellIdentity = Board.BLUE_CELL;
            this.edgeIdentity = Board.BLUE_EDGE;
            this.oppPlayer = RED;
            this.oppCellIdentity = Board.RED_CELL;
            this.oppEdgeIdentity = Board.RED_EDGE;
        } else {
            this.cellIdentity = Board.RED_CELL;
            this.edgeIdentity = Board.RED_EDGE;
            this.oppPlayer = BLUE;
            this.oppCellIdentity = Board.BLUE_CELL;
            this.oppEdgeIdentity = Board.BLUE_EDGE;
        }

        return 0;
    }

	@Override
	public Move makeMove() {
		// TODO Auto-generated method stub
		return null;
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
