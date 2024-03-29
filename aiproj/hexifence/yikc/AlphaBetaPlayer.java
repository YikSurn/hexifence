/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

package aiproj.hexifence.yikc;

import aiproj.hexifence.*;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.lang.Math;

/*
 *  Alpha-beta player
 *  Implements alpha-beta pruning logic
 */
public class AlphaBetaPlayer implements Player, Piece {

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
        // Takes 6 seconds for depth of 12 and 25 seconds for depth of 13
        int THRESHOLD = 12;

        // Declare constants for alpha beta pruning
        int ALPHA = Integer.MIN_VALUE;
        int BETA = Integer.MAX_VALUE;

        Move m = new Move();

        Point capturePoint = this.board.getCapturableCellPoint();
        if (capturePoint != null) {
            m.P = this.player;
            m.Row = capturePoint.getX();
            m.Col = capturePoint.getY();
        } else if (this.board.getPossibleMoves() <= THRESHOLD) {
            // Start invoking alpha beta pruning when no safe edges are left
            char[][] boardState = this.board.getBoardIn2DArray();
            HashMap<Integer, Move> myBestMove;

            // Print out time taken
            if (this.board.getPossibleMoves() >= 12) {
                System.out.println("Possible moves : " + this.board.getPossibleMoves());
                double startTime = System.currentTimeMillis();
                myBestMove = alphaBeta(boardState, this.board.getPossibleMoves(), ALPHA, BETA, true);
                double endTime = System.currentTimeMillis();

                double totalTime = endTime - startTime;
                System.out.println("Total time taken (seconds) : " + (totalTime/1000));
            } else {
                myBestMove = alphaBeta(boardState, this.board.getPossibleMoves(), ALPHA, BETA, true);
            }

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
    private HashMap<Integer, Move> alphaBeta(char[][] boardState, int possibleMoves, int alpha, int beta, boolean maxPlayer) {
        int bestValue;
        int player = maxPlayer ? this.player : this.oppPlayer;
        Move bestMove = null;

        ArrayList<Move> moves = generateMoves(boardState, player);

        // If next move is empty or depth reaches the end
        if (possibleMoves == 0) {
            bestValue = evaluateBoardState(boardState);
        }
        else {
            bestValue = maxPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            // For each valid move, generate child node and recurse minimax
            for (Move move: moves) {
                // Try this move on the player
            	Point capturedCellPoint = checkIfCaptureCell(move, boardState);
                char[][] childBoard = generateChildBoardState(move, boardState, capturedCellPoint);
            	HashMap<Integer, Move> result;
                if (maxPlayer) {
                    // if player captured a cell, it's the player's turn again
                	if (capturedCellPoint != null) {
                		result = alphaBeta(childBoard, possibleMoves-1, alpha, beta, true);
                	}
                	else {
                		result = alphaBeta(childBoard, possibleMoves-1, alpha, beta, false);
                	}
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
                else {
                	if (capturedCellPoint != null) {
                		result = alphaBeta(childBoard, possibleMoves-1, alpha, beta, false);
                	}
                	else {
                		result = alphaBeta(childBoard, possibleMoves-1, alpha, beta, true);
                	}
                    int resultValue = (int) result.keySet().toArray()[0];
                    if (resultValue < bestValue) {
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
        int boardSize = boardState.length;
        int boardDimension = (boardSize + 1)/4;

        int numToWin = Board.numberOfCells(boardDimension)/2 + 1;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (boardState[row][col] == this.cellIdentity) {
                    numCapturedCells++;
                }
            }
        }

        return numCapturedCells - numToWin;
    }

    /* Generate a list of all legal moves of existing boardState
     * */
    private ArrayList<Move> generateMoves(char[][] boardState, int player) {
        ArrayList<Move> allLegalMoves = new ArrayList<Move>();
        int boardSize = boardState.length;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                // Represents a possible move that's available for capture
                if (boardState[row][col] == Board.EMPTY_EDGE) {
                    Move legalMove = new Move();
                    legalMove.P = player;
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
    private char[][] generateChildBoardState(Move move, char[][] boardState, Point capturedCellPoint) {
        // Copy board state over and make a move
        char[][] newBoardState = this.copyBoard(boardState);
        // If maxPlayer, means it is this player's turn, so move was made by opponent
        newBoardState[move.Row][move.Col] = move.P == this.player ? this.edgeIdentity : this.oppEdgeIdentity;
        if (capturedCellPoint != null) {
        	newBoardState[capturedCellPoint.getX()][capturedCellPoint.getY()] = move.P == this.player ? this.cellIdentity : this.oppCellIdentity;
        }

        return newBoardState;
    }

    /* Check if move is a capturing cell move
     * */
    Point checkIfCaptureCell(Move move, char[][] boardState) {
    	char[][] newBoardState = this.copyBoard(boardState);
    	newBoardState[move.Row][move.Col] = move.P == this.player ? this.edgeIdentity : this.oppEdgeIdentity;

        // Get the cell point associated with the captured edge
        ArrayList<Point> cellPointsOfEdge = new ArrayList<Point>();
        for (Point edgePoint : edgeAssociatedCells.keySet()) {
            if (edgePoint.getX() == move.Row && edgePoint.getY() == move.Col) {
                cellPointsOfEdge = edgeAssociatedCells.get(edgePoint);
            }
        }

        // Loop through the associated cell, and get all the edges that belong to the cell
        for (Point cellPoint: cellPointsOfEdge) {
            ArrayList<Point> allAssociatedEdgePoints = Cell.getPointOfCellEdges(cellPoint);
            int numEdgeCaptured = 0;

            // Increment counter if edges are captured
            for (Point edgePoint: allAssociatedEdgePoints) {
                if (newBoardState[edgePoint.getX()][edgePoint.getY()] != Board.EMPTY_EDGE) {
                    numEdgeCaptured++;
                }
            }

            // If fully captured and cell point is not labeled, this suggests that
            // the move made has captured the cell
            char cellValue = newBoardState[cellPoint.getX()][cellPoint.getY()];
            if (numEdgeCaptured == Board.HEXAGON && cellValue == Board.NA_POINT) {
            	// If maxPlayer, means it is this player's turn, so move was made by opponent
            	return new Point(cellPoint.getX(), cellPoint.getY());
            }
        }
        return null;
    }

    private char[][] copyBoard(char[][] boardState) {
        int size = boardDimension*4 - 1;
        char[][] dupBoard = new char[size][size];
        for (int i=0; i < size; i++) {
            dupBoard[i] = Arrays.copyOf(boardState[i], size);
        }
        return dupBoard;
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
