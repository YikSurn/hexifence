import java.io.PrintStream;

import aiproj.hexifence.Move;
import aiproj.hexifence.Piece;
import aiproj.hexifence.Player;

public class MiniMaxPlayer implements Player, Piece {

	@Override
	public int init(int n, int p) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Move makeMove() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* Evaluate a boardState and return an integer that represents
	 * the utility of this player
	 * */
	private int evaluateBoardState(Board boardState) {
		return 0;
	}
	
	/* Generate a list of legal moves based on an existing boardState
	 * */
	private ArrayList<Move> generateMoves(Board boardState) {
		
	}
	
	/* Generate child node based on a new move applied by player */
	private Board generateChildBoard(Move move) {
		
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
