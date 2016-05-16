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
    private int opposingPlayer;
    private int boardDimension;
    private char cellIdentity;
    private char edgeIdentity;


	@Override
	public int init(int n, int p) {
        if (p != BLUE && p != RED) {
            return INVALID;
        }
        this.player = p;
        this.opposingPlayer = p == BLUE ? RED: BLUE;
        this.boardDimension = n;
        this.board = new Board(this.boardDimension);
        this.cellIdentity = p == BLUE ? 'b' : 'r';
        this.edgeIdentity = p == BLUE? 'B': 'R';

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
			HashMap<Integer, Move> myBestMove = minimax(this.board, 10, this.player);
			// Return Move generated by minimax algorithm
			return myBestMove.get(myBestMove.keySet().toArray()[0]);
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
	
	/* Recursive minimax at level of depth for either maximizing or minimizing player.
	 * Return HashMap<Int, Move> of {bestScore -> bestMove}
	 *  */
	private HashMap<Integer, Move> minimax(Board board, int depth, int maximizingPlayer) {
		int bestValue = Integer.MIN_VALUE;
		Move bestMove = new Move();
		ArrayList<Move> moves = generateMoves(board);

		// If next move is empty or depth reaches the end 
		if (depth == 0 || moves.isEmpty()) {
			bestValue = evaluateBoardState(board);
		}

		// For each valid move, generate child node and recurse minimax
		for (Move move: moves) {
			// Try this move on the player
			Board newBoard = generateChildBoard(move,board);
			if (maximizingPlayer == this.player) {
				HashMap<Integer, Move> result = minimax(newBoard, depth-1, this.opposingPlayer);
				int resultValue = (int) result.keySet().toArray()[0];
				if (resultValue > bestValue) {
					bestValue = resultValue;
					bestMove = move;
				}	
			}
			else {
				HashMap<Integer, Move> result = minimax(newBoard, depth-1, this.player);
				int resultValue = (int) result.keySet().toArray()[0];
				if (resultValue < bestValue) {
					bestValue = resultValue;
					bestMove = move;
				}				
			}
			// Undo move
			newBoard = null;
		}
		
		HashMap<Integer, Move> bestStrategy = new HashMap<Integer, Move>();
		bestStrategy.put(bestValue, bestMove);
		return bestStrategy;
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
	
	/* Generate a list of all legal moves of existing boardState
	 * */
	private ArrayList<Move> generateMoves(Board board) {
		ArrayList<Move> allLegalMoves = new ArrayList<Move>();
        char[][] boardState = board.getBoardIn2DArray();
        int boardSize = board.calcBoardSize();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
            	// Represents a possible move that's available for capture
                if (boardState[row][col] == '+') {
                    Move legalMove = new Move();
                    legalMove.P = this.player;
                    legalMove.Row = row;
                    legalMove.Col = col;  
                    allLegalMoves.add(legalMove);
                }
            }
        }
        return allLegalMoves;
	}
	
	/* Generate board child node based on a new move applied by player 
	 * */
//	private char[][] generateChildBoardState(Move move, char[][] boardState, int boardSize) {
//		char[][] newBoardState = boardState.clone();
//		newBoardState[move.Row][move.Col] = this.edgeIdentity;
//		return newBoardState;
//	}
	
	/* Generate child node of a board by deep copying existing board
	 * and apply new Move to board
	 * */
	private Board generateChildBoard(Move move, Board board) {
		Board newBoard = (Board) DeepCopy.copy(board);
		newBoard.update(move);
		return newBoard;
	}

	@Override
	public int opponentMove(Move m) {
		// TODO Auto-generated method stub
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

	@Override
	public int getWinner() {
		// TODO Auto-generated method stub
		return this.board.getWinner();
	}

	@Override
	public void printBoard(PrintStream output) {
		// TODO Auto-generated method stub
		this.board.printBoard();
	}

}
