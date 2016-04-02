# hexifence
Artificial Intelligence project

Algorithms for game handling: 
1. Given a 2D array of board state, coordinates of cells (depending if N=2 or N=3) are "plucked" and saved
2. 2 types of cells' coordinates are saved (one based on the 2D array index, one for the calculation of adjacent cells)
3. With the given list of cell coordinates, coordinates of their respective edges are calculated (Note: create a function to handle that), and saved
4. To link an edge to two adjacent cells, we grab the edge(s) that is in between the adjacent cell(s). This would be based on the coordinates on 2D array of given board state
