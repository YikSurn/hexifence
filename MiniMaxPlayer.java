/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

import aiproj.hexifence.*;

import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;
import java.lang.Math;
import java.util.Map;

/*
 *  MiniMax player
 *  Implements minimax logic
 */
public class MiniMaxPlayer implements Player, Piece {

    private Board board;
    private int boardDimension;
    private int player;
    private char cellIdentity;
    private char edgeIdentity;
    private int oppPlayer;
    private char oppCellIdentity;
    private char oppEdgeIdentity;
    private HashMap<Point, ArrayList<Point>> edgeAssociatedCells;
    private Map<Integer, ArrayList<Point>> numCellsToPointsMade = null;

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
        // Takes 6 seconds for depth of 9 and 60 seconds for depth of 10
        int THRESHOLD = 22;
        Move m = new Move();

        // Capture a cell when possible
        Point capturePoint = this.board.pointToCaptureCell();
        if (capturePoint != null) {
            m.P = this.player;
            m.Row = capturePoint.getX();
            m.Col = capturePoint.getY();
        } else if (this.board.getSafeEdges().size() == 0) {
            // Choose edge that gives opponent least number of cells when there are no more safe edges
            if (this.numCellsToPointsMade == null) {
                this.numCellsToPointsMade = this.generateNumCellsToPointsMade();
                // for (Map.Entry<Integer ,ArrayList<Point>> entry : this.numCellsToPointsMade.entrySet()) {
                //     int numCells = entry.getKey();
                //     ArrayList<Point> edgePoints = entry.getValue();
                //     System.out.println("Num cells :" + numCells);
                //     System.out.println("Number of the points :" + edgePoints.size());
                // }
            }

            // TreeMap is already sorted
            outerloop:
            for (ArrayList<Point> edgePointBatch: this.numCellsToPointsMade.values()) {
            	for (Point edgePoint: edgePointBatch) {
            		if (!this.board.getEdge(edgePoint).getHasBeenCaptured()) {
            			m.P = this.player;
            			m.Row = edgePoint.getX();
            			m.Col = edgePoint.getY();
            			break outerloop;
            		}
            	}
            }
        } else if (this.board.getPossibleMoves() <= THRESHOLD) {
            // Start invoking minimax when below threshold
            char[][] boardState = this.board.getBoardIn2DArray();

            System.out.println("Possible moves : " + this.board.getPossibleMoves());
            double startTime = System.currentTimeMillis();
            HashMap<Integer, Move> myBestMove = minimax(boardState, this.board.getPossibleMoves(), true);
            double endTime = System.currentTimeMillis();
            double totalTime = endTime - startTime;
            System.out.println("Total time taken (seconds) : " + (totalTime/1000));

            // Return Move generated by minimax algorithm
            m = myBestMove.get(myBestMove.keySet().toArray()[0]);
        } else {
            // Randomly choose edge when there still are safe edges and possible moves are above threshold
            ArrayList<Edge> edgesToRandomFrom = this.board.getSafeEdges();
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

    /* Generate a dictionary where the key is the number of cells that can
     * be captured in one go if the opponent makes a move in the array of points
     * (the value of dictionary)
     *
     * Call this function when there are no more safe edges left in the board
     */
    public Map<Integer, ArrayList<Point>> generateNumCellsToPointsMade() {
        if (this.board.getSafeEdges().size() != 0) {
            return null;
        }

        char[][] boardState = this.board.getBoardIn2DArray();
        // Store all points of edges that has yet to be captured
        ArrayList<Point> uncapturedEdgePoints = new ArrayList<Point>();
        for (Point edgePoint: this.edgeAssociatedCells.keySet()) {
            if (boardState[edgePoint.getX()][edgePoint.getY()] == Board.EMPTY_EDGE) {
                uncapturedEdgePoints.add(edgePoint);
            }
        }

        Map<Integer, ArrayList<Point>> numCellsToPointsMade = new TreeMap<Integer, ArrayList<Point>>();
        for (Point edgePoint: uncapturedEdgePoints) {
            char[][] dupBoard = copyBoard(boardState);
            int edgeX = edgePoint.getX();
            int edgeY = edgePoint.getY();
            dupBoard[edgeX][edgeY] = this.edgeIdentity;

            // Start recursion function
            // Find number of cells that can be captured if player were to make this move
            ArrayList<Point> affectedCells = this.edgeAssociatedCells.get(edgePoint);
            int numCells = calculateCellsCapturable(dupBoard, affectedCells);

            ArrayList<Point> edgePoints;
            if (numCellsToPointsMade.get(numCells) == null) {
                // New number of cells capturable
                edgePoints = new ArrayList<Point>();
            } else {
                edgePoints = numCellsToPointsMade.get(numCells);
            }
            edgePoints.add(edgePoint);
            numCellsToPointsMade.put(numCells, edgePoints);
        }

        return numCellsToPointsMade;
    }

    /* Recursive function to return the number of cells that could be captured
     * based on a move made by opposing player
     */
    private int calculateCellsCapturable(char[][] boardState, ArrayList<Point> affectedCells) {
        int numCells = 0;

        if (affectedCells != null) {
            // Loop through points of associated cell, and get all the edges that belong to the cell
            for (Point cellPoint: affectedCells) {
                ArrayList<Point> allAssociatedEdgePoints = Cell.getPointOfCellEdges(cellPoint);
                Point nextEdgePoint = null;
                boolean firstEdgeUncaptured = true;

                for (Point edgePoint: allAssociatedEdgePoints) {
                    if (boardState[edgePoint.getX()][edgePoint.getY()] == Board.EMPTY_EDGE) {
                        if (firstEdgeUncaptured) {
                            firstEdgeUncaptured = false;
                            nextEdgePoint = edgePoint;
                        } else {
                            nextEdgePoint = null;
                            break;
                        }
                    }
                }

                if (nextEdgePoint != null) {
                    boardState[nextEdgePoint.getX()][nextEdgePoint.getY()] = this.edgeIdentity;
                    numCells++;

                    // There is either none or one nextAffectedCell
                    Point nextAffectedCell = null;
                    for (Point nextCellPoint: getAssociatedCellPoints(nextEdgePoint)) {
                        if (!nextCellPoint.equals(cellPoint)) {
                            nextAffectedCell = nextCellPoint;
                            break;
                        }
                    }

                    ArrayList<Point> nextAffectedCells = null;
                    if (nextAffectedCell != null) {
                        nextAffectedCells = new ArrayList<Point>();
                        nextAffectedCells.add(nextAffectedCell);
                    }
                    numCells += calculateCellsCapturable(boardState, nextAffectedCells);
                }
            }
        }

        return numCells;
    }

    /* Return Point in board state that would result in capturing of cell by one move
    */
    private ArrayList<Point> getAssociatedCellPoints(Point edgePoint) {
        for (Point p: this.edgeAssociatedCells.keySet()) {
            if (p.equals(edgePoint)) {
                return this.edgeAssociatedCells.get(p);
            }
        }

        return null;
    }

    /* Recursive minimax at level of depth for either maximizing or minimizing player.
     * Return HashMap<Int, Move> of {bestScore -> bestMove}
     *  */
    private HashMap<Integer, Move> minimax(char[][] boardState, int possibleMoves, boolean maxPlayer) {
        int bestValue;
        int player = maxPlayer ? this.player : this.oppPlayer;
        Move bestMove = null;

        ArrayList<Move> moves = generateMoves(boardState, player);

        // If game state has reaches the point where all cells only has at most 2 uncaptured cells
        ArrayList<Edge> safeEdges = this.board.getSafeEdges();
        if (safeEdges.size() == 0) {
            bestValue = predictWinningBoardState(boardState, possibleMoves, maxPlayer);
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
                	if (capturedCellPoint != null) {
                		result = minimax(childBoard, possibleMoves-1, true);
                	}
                	else {
                		result = minimax(childBoard, possibleMoves-1, false);
                	}
                    int resultValue = (int) result.keySet().toArray()[0];
                    if (resultValue > bestValue) {
                        bestValue = resultValue;
                        bestMove = move;
                    }
                }
                else {
                	if (capturedCellPoint != null) {
                		result = minimax(childBoard, possibleMoves-1, false);
                	}
                	else {
                		result = minimax(childBoard, possibleMoves-1, true);
                	}
                    int resultValue = (int) result.keySet().toArray()[0];
                    if (resultValue < bestValue) {
                        bestValue = resultValue;
                        bestMove = move;
                    }
                }
            }
        }

        HashMap<Integer, Move> bestStrategy = new HashMap<Integer, Move>();
        bestStrategy.put(bestValue, bestMove);
        return bestStrategy;
    }

    /* Return the point on board that will capture the minimum number of cells
    */
    private Point getPointThatCaptureLowestNumCells() {
        if (numCellsToPointsMade == null) {
            return null;
        }
        int lowest = Integer.MAX_VALUE;
        for (Map.Entry<Integer ,ArrayList<Point>> entry : this.numCellsToPointsMade.entrySet()) {
        	int numCells = entry.getKey();
        	if (lowest > numCells) {
        		lowest = numCells;
        	}
        }
        // Return the first edge point that leads to the lowest num cells, and update hash map
        ArrayList<Point> edgePoints = this.numCellsToPointsMade.get(lowest);
        Point bestMove = edgePoints.get(0);
        edgePoints.remove(bestMove);
        if (edgePoints.isEmpty()) {
        	this.numCellsToPointsMade.remove(lowest);
        }
        else {
        	this.numCellsToPointsMade.put(lowest, edgePoints);
        }
        return bestMove;
    }

    /* Based on the number of possible moves, predict the winning board state
     * assuming both player(s) are rational and want to win
    */
    private int predictWinningBoardState(char[][] boardState, int possibleMoves, boolean maxPlayer) {
        // If next move is empty or depth reaches the end
        if (possibleMoves == 0) {
            return evaluateBoardState(boardState);
        }
        else {
            // Get the move where player capture lowest num of cell
        	Point bestPoint = getPointThatCaptureLowestNumCells();
        	// Update board state based on that move
        	if (maxPlayer) {
            	boardState[bestPoint.getX()][bestPoint.getY()] = this.edgeIdentity;
        	}
        	else {
        		boardState[bestPoint.getX()][bestPoint.getY()] = this.oppCellIdentity;
        	}
            // recursive call to predict final board state
        	return predictWinningBoardState(boardState, possibleMoves-1, !maxPlayer);
        }
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
