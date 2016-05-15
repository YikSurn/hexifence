import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import aiproj.hexifence.Move;
import aiproj.hexifence.Piece;
import aiproj.hexifence.Player;

public class MiniMaxPlayer implements Player, Piece {

    private Board board;
    private int player;
    private int boardDimension;
    private char cellIdentity;


	@Override
	public int init(int n, int p) {
        if (p != BLUE && p != RED) {
            return INVALID;
        }
        this.cellIdentity = p == BLUE ? 'b' : 'r';
        this.player = p;
        this.boardDimension = n;
        this.board = new Board(this.boardDimension);

        return 0;
	}

	/* Randomly generate a set of moves until a certain threshold 
	 * of max moves left in the game
	 * */
	@Override
	public Move makeMove() {
		int threshold = 10;
		// Start invoking minimax 
		if (board.getPossibleMoves() <= threshold) {

		}

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
	
	/* A simple minimax algorithm */
	private int minimax(Board board, int depth, int maximizingPlayer) {
		ArrayList<Move> moves = generateMoves(board);
		// Base case: if leaf node
		if (depth == 0 || moves.size() == 0) {
			return evaluateBoardState(board);
		}
		Double negativeInfinity = Double.NEGATIVE_INFINITY;
		int bestValue = negativeInfinity.intValue();
		// For each valid move, generate child node and recurse minimax
		for (Move move: moves) {
			Board newBoard = generateChildBoard(move,board);
			int result = minimax(newBoard, depth-1, maximizingPlayer);
			bestValue = Math.max(bestValue, result);
		}
		
		return 0;
	}
	
	/* Evaluate a boardState and return an integer that represents
	 * the utility of this player
	 * */
	private int evaluateBoardState(Board boardState) {
		int numCapturedCells = 0;
		HashMap<Edge, ArrayList<Cell>> edgeToCells =  boardState.getEdgeToCells();
		for(Edge edge: edgeToCells.keySet()) {
			for (Cell cell: edgeToCells.get(edge)) {
				if (cell.getCapturedBy() == cellIdentity) {
					numCapturedCells += 1;
				}
			}
		}
		return numCapturedCells/2 + 1;
	}
	
	/* Generate a list of legal moves based on an existing boardState
	 * */
	private ArrayList<Move> generateMoves(Board boardState) {
		
	}
	
	/* Generate child node based on a new move applied by player */
	private Board generateChildBoard(Move move, Board boardState) {
		
	}

	@Override
	public int opponentMove(Move m) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWinner() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void printBoard(PrintStream output) {
		// TODO Auto-generated method stub
		
	}

}
