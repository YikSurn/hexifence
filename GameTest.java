/* Authors:
 * Yik Surn Chong (yikc)
 * Angeline Lim (angelinel)
 */

import java.util.Scanner;

public class GameTest {

    public static void main(String[] args) {
        char[][] boardState;
        int entries; // store max board state entries per row or col
        try {
            Scanner input = new Scanner(System.in);
            int boardDimension = input.nextInt();
            entries = 4*boardDimension - 1;
            boardState = new char[entries][entries];
            input.nextLine(); // consume <enter> from prev int input
            int col;
            for (int row=0; row < entries; row++) {
                col = 0;
                String tempStr = input.nextLine();
                Scanner innerInput = new Scanner(tempStr);
                String validStr = innerInput.next("[RB+-]");
                while (validStr != null) {
                    boardState[row][col] = validStr.charAt(0);
                    if (col == entries-1) {
                        break;
                    }
                    else {
                        validStr = innerInput.next("[RB+-]");
                    }
                    col++;
                }
                innerInput.close();
            }
            input.close();

            Board gameBoard = new Board(boardDimension, boardState);
            System.out.println(gameBoard.getPossibleMoves());
            System.out.println(gameBoard.maxCellCaptureByOneMove());
            System.out.println(gameBoard.numCellsAvailableForCapture());
        }
        catch (IllegalStateException e) {
            System.out.println(e);
            System.exit(0);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
