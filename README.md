# HexiFence (AI Project)

## Authors

* Yik Surn Chong (yikc)
* Angeline Lim (angelinel)

## Code structure

The implementation of our HexiFence player is made up of several components:
  1. Board.java , Cell.java , Edge.java that represents our game board throughout the simulated game

yikc.java
=============


============================================================================================
## Approach

The implementation of our Player's move is strategized based on several states of the games:
  1. Randomly make moves : at the beginning of the game where first few moves are less important
  2. Run alpha-beta pruning : at a threshold set for number of possible moves
  3. Make a move that results in lowest number of cells capturable by the oppoonent : at the critical stage of the game where all uncaptured cells' edges are of 2 sides
  4. Capture a cell whenever a cell can be captured

### Search strategy

A history heuristic alpha-beta pruning strategy is used to come up with the best move for our Player at a given board state, once a predetermined number of possible moves left is reached. The aim of this strategy is to allow us to prune our search tree more efficiently by moves reordering, which would allow us to use backward-induction strategy with higher depth without losing efficiency. Our alpha-beta pruning strategy follows the classic alpha-beta pruning, whereas implementation of the history heuristic is based on two parameters:

  1. Mapping of moves to history
     Create a hashmap of moves (stored as board points) that maps to its respective score, where a higher score suggests that it is a sufficient move.
     Rationale: Significantly reduce the amount of nodes we expand, by expanding the node that is made by a sufficient move. With history heuristic, we maintain success of all moves at all depths such that the "killer" move will earn a high score.

  2. Weight of a sufficient move
     Weight of 2^depth is used, where depth is the depth of the sub-tree used.
     Rationale: A good move near the root of the tree have more potential of being useful throughout the game tree than a good move near the leaf node.

Further, within our alpha beta pruning algorithm, we made a slight modification on generating the possible moves (operators) to expand. Instead of expanding all valid moves based on each board state, our Player would only consider valid moves that would not lead to opponent capturing a cell, known as safe moves.

### Evaluation function

Since the player only cares about winning, not the number of edges he/she captures. Utility's of a player from a final board state is evaluated based on the number of cell the Player captures in comparison to the number of cells to capture in order to win the game. We do not compare the number of cells captured with that of the opponent because we only care about winning, not how many cells we can captured. Hence, our evaluation function is as follows:
    f(n) = n - (totalNumberOfCellsOnBoard/2 + 1); when f(n) is positive
    f(n) = -1; when n - (totalNumberOfCellsOnBoard/2 + 1) is negative

, where n = number of cells captured by our Player

### Creative techniques

Within our alpha-beta pruning recursive strategy, the base case is modified such that it is terminated when the board reaches a state where any uncaptured edge will lead to the opponent's capturing the cell. Instead of using the alpha-beta pruning recursive call, we opt for making a move such that it will minimize the number of cells that can be captured by our opponent.
