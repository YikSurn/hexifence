import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

import aiproj.hexifence.Move;
import aiproj.hexifence.Piece;
import aiproj.hexifence.Player;

/* Alpha beta player
 * Makes a move based on alpha-beta pruning
 * */
public class AlphaPlayer implements Player, Piece {

    private Board board;
    private int boardDimension;
    private int player;
    private char cellIdentity;
    private char edgeIdentity;
    private int oppPlayer;
    private char oppCellIdentity;
    private char oppEdgeIdentity;
    private HashMap<Point, ArrayList<Point>> edgeAssociatedCells;
    private HashMap<Move, Integer> bestMoveHistoryScore = new HashMap<Move, Integer>();


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

	/* Randomly generate a set of moves until a certain threshold
	 * of max moves left in the game
	 * */
	@Override
	public Move makeMove() {
		int THRESHOLD = 10;
		Move m = new Move();

        Point capturePoint = this.board.pointToCaptureCell();
        if (capturePoint != null) {
            m.P = this.player;
            m.Row = capturePoint.getX();
            m.Col = capturePoint.getY();
        } else if (board.getPossibleMoves() <= THRESHOLD) {
            // Start invoking minimax
        	this.edgeAssociatedCells = this.board.generateEdgeToCellsPoints();
        	char[][] boardState = this.board.getBoardIn2DArray();
			HashMap<Integer, Move> myBestMove = alphaBeta(boardState, Integer.MIN_VALUE, Integer.MAX_VALUE, THRESHOLD, true);
			// Return Move generated by minimax algorithm
			m = myBestMove.get(myBestMove.keySet().toArray()[0]);
		} else {
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
            Point chosenPoint = edgesToRandomFrom.get(indexEdgeChosen).getPoint();

	        m.P = this.player;
	        m.Row = chosenPoint.getX();
	        m.Col = chosenPoint.getY();
		}

        this.board.update(m);
        return m;
	}


	/* Recursive minimax at level of depth for either maximizing or minimizing player.
	 * Return HashMap<Int, Move> of {bestScore -> bestMove}
	 *  */
	private HashMap<Integer, Move> alphaBeta(char[][] boardState, int alpha, int beta, int depth, boolean maximizingPlayer) {
		int bestValue;
		Move bestMove = new Move();
		ArrayList<Move> moves = generateMoves(boardState);

		// If next move is empty or depth reaches the end
		if (depth == 0 || moves.isEmpty()) {
			bestValue = evaluateBoardState(boardState);
		}
		else {
			// Sort moves such that highest rated move is at the end
			sortMoveBasedOnHistory(moves);
			if (maximizingPlayer) {
				bestValue = Integer.MIN_VALUE;
				for (Move move: moves) {
					// Try this move on the player
					char[][] newBoard = generateChildBoardState(move, boardState, maximizingPlayer);
					HashMap<Integer, Move> result = alphaBeta(newBoard, alpha, beta, depth-1, false);
					int resultValue = (int) result.keySet().toArray()[0];
					if (resultValue > bestValue) {
						bestValue = resultValue;
						bestMove = move;
					}
					alpha = Math.max(alpha, bestValue);
					if (beta <= alpha) {
						break; // cut-off
					}
				}
				// Update history score for best move
				int historyScore = 0;
				if (bestMoveHistoryScore.get(bestMove) != null) {
					bestMoveHistoryScore.get(bestMove); 
				}
				bestMoveHistoryScore.put(bestMove, historyScore + 2);
			}
			else {
				bestValue = Integer.MAX_VALUE;
				for (Move move: moves) {
					// Try this move on the player
					char[][] newBoard = generateChildBoardState(move, boardState, maximizingPlayer);
					HashMap<Integer, Move> result = alphaBeta(newBoard, alpha, beta, depth-1, true);
					int resultValue = (int) result.keySet().toArray()[0];
					if (resultValue > bestValue) {
						bestValue = resultValue;
						bestMove = move;
					}
					beta = Math.min(beta, bestValue);
					if (beta <= alpha) {
						break; // cut-off
					}
				}
			}
		}

		HashMap<Integer, Move> bestStrategy = new HashMap<Integer, Move>();
		bestStrategy.put(bestValue, bestMove);
		return bestStrategy;
	}

	/* Evaluate a boardState and return an integer that represents
	 * the utility of this player
	 * */
	private int evaluateBoardState(char[][] boardState) {
		int numCapturedCells = 0;

		char playerCellIdentity = this.cellIdentity;
		int boardSize = boardState.length;
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				if (boardState[row][col] == playerCellIdentity) {
					numCapturedCells += 1;
				}
			}
		}
		return numCapturedCells/2 + 1;
	}

	/* Sort an array list of Move based on the history heuristic score
	 * assigned to each move
	 * */
	private ArrayList<Move> sortMoveBasedOnHistory(ArrayList<Move> moves) {
		// Don't do anything if history score table is empty
		if (bestMoveHistoryScore.isEmpty()) {
			return moves;
		}
		ArrayList<Move> sortedMoves = new ArrayList<Move>();
		// Sort an array of history score
		ArrayList<Integer> sortedScore = new ArrayList<Integer>();
		for (Move move: moves) {
			if (bestMoveHistoryScore.get(move) == null) {
				continue;
			}
			int historyScore = bestMoveHistoryScore.get(move);
			sortedScore.add(historyScore);
		}
		Collections.sort(sortedScore);

		// Allocate moves to same index location as that of sorted score(s)
		for (int n = 0; n < moves.size(); n++) {
			if (bestMoveHistoryScore.get(moves.get(n)) == null) {
				continue;
			}
			int historyScore = bestMoveHistoryScore.get(moves.get(n));
			int index = sortedScore.indexOf(historyScore);
			sortedMoves.add(index, moves.get(n));
		}
<<<<<<< HEAD
		return sortedMoves;
=======

        return sortedMoves;
>>>>>>> c45bdde9f43b408b685950fa9c732fc3de20686a
	}

	/* Generate a list of all legal moves of existing boardState
	 * */
	private ArrayList<Move> generateMoves(char[][] boardState) {
		ArrayList<Move> allLegalMoves = new ArrayList<Move>();
        int boardSize = boardState.length;

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
	private char[][] generateChildBoardState(Move move, char[][] boardState, boolean maximizingPlayer) {
		// Copy board state over and make a move
		char[][] newBoardState = boardState.clone();
		newBoardState[move.Row][move.Col] = this.edgeIdentity;

		// Get the cell point associated with the captured edge
		ArrayList<Point> cellPointsOfEdge = new ArrayList<Point>();
		for (Point edge : edgeAssociatedCells.keySet()) {
			if (edge.getX() == move.Row && edge.getY() == move.Col) {
				cellPointsOfEdge = edgeAssociatedCells.get(edge);
			}
		}
		// Loop through the associated cell, and get all the edges that belong to the cell
		for (Point cellPoints: cellPointsOfEdge) {
			ArrayList<Point> allAssociatedEdgePoints = Cell.getPointOfCellEdges(cellPoints);
			int numEdgeCaptured = 0;
			// Increment counter if edges are captured
			for (Point edges: allAssociatedEdgePoints) {
				if (newBoardState[edges.getX()][edges.getY()] == Board.RED_EDGE ||
						newBoardState[edges.getX()][edges.getY()] == Board.BLUE_EDGE) {
					numEdgeCaptured += 1;
				}
			}
			// If fully captured and cell point is not labeled, this suggests
			// that maximizingPlayer's move has successfully captured cell
			char cellValue = newBoardState[cellPoints.getX()][cellPoints.getY()];
			if (numEdgeCaptured == Board.HEXAGON &&
					cellValue == Board.NA_POINT) {
<<<<<<< HEAD
				cellValue = maximizingPlayer ? this.cellIdentity : this.oppCellIdentity;
			}		
=======
				cellValue = maximizingPlayer ? this.cellIdentity : this.opponentCellIdentity;
			}
>>>>>>> c45bdde9f43b408b685950fa9c732fc3de20686a
		}
		return newBoardState;
	}


	/* Generate child node of a board by deep copying existing board
	 * and apply new Move to board
	 * */
//	private Board generateChildBoard(Move move, Board board) {
//		Board newBoard = (Board) DeepCopy.copy(board);
//		newBoard.update(move);
//		return newBoard;
//	}

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
