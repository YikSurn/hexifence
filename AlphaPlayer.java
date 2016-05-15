import java.io.PrintStream;
import java.util.ArrayList;

import aiproj.hexifence.Move;
import aiproj.hexifence.Piece;
import aiproj.hexifence.Player;

public class AlphaPlayer implements Player, Piece {

    private Board board;
    private int player;
    private int boardSize;

	@Override
	public int init(int n, int p) {

		return 0;
	}

	@Override
	public Move makeMove() {

		return null;
	}
	
	/* A simple alpha beta pruning algorithm */
	public int alphaBetaPruning(char[][] boardState,int alpha, int beta, int depth) {
		int bestScore = alpha;
		Move bestMove;
		// Base case: at leaf node
		if (depth == 0) {
			return evaluateBoardState(boardState);
		}
		ArrayList<Move> nextMoves = generateMoves(boardState);
		// No move for this boardState
		if (nextMoves.size() == 0) {
			return evaluateBoardState(boardState);
		}
		
		for(Move move: nextMoves) {
			char[][] newBoardState = generateState(move);
			// Recursive call
			int result = alphaBetaPruning(newBoardState, alpha, beta, depth-1);
			if (result > bestScore) {
				bestScore = result;
			}
			
			// Check for cut-off 
			if (bestScore >= beta) {
				bestMove = move;
				break;
			}
			
			alpha = Math.max(alpha, bestScore);
		}
		
		// Update history score for best move
		
		// Return best score
		return bestScore;
	}

	/* Evaluate an existing board state of the game, and 
	 * return an integer to represents the "utility" 
	 * the state gives to this player
	 * */
	private int evaluateBoardState(char[][] boardState) {
		
		return 0;
	}
	
	/* Generate a list of all possible moves for a current board state
	 * */
	private ArrayList<Move> generateMoves(char[][] boardState) {
	   ArrayList<Move> allPossibleMoves = new ArrayList<Move>();
	   
	   return allPossibleMoves;
	}
	
	/* Generate a new boardState based on a new move made by player */
	private char[][] generateState(Move move) {
		
	}

	@Override
	public int opponentMove(Move m) {

		return 0;
	}

	@Override
	public int getWinner() {

		return 0;
	}

	@Override
	public void printBoard(PrintStream output) {
		
	}
	
	

}
