import java.util.Scanner;

public class GameTest {

    public static void main(String[] args) {
        char[][] boardState;
        int entries; // store max board state entries per row or col
        try {
            Scanner input = new Scanner(System.in);
            int boardSize = input.nextInt();
            entries = 4*boardSize - 1;
            boardState = new char[entries][entries];
            input.nextLine(); // consume <enter> from prev int input
            int col;
            for (int row=0; row < entries; row++) {
                col = 0;
                String tempStr = input.nextLine();
                System.out.println(tempStr);
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
            Board gameBoard = new Board(boardSize, boardState);
            System.out.println(gamBoard.getPossibleMoves());
            
        }
        catch (IllegalStateException e) {
            System.out.println(e);
            System.exit(0);
        }
        catch (Exception e) {
            // Temporary handle errors
            System.out.println("Invalid input. One of the following may have occured:");
            System.out.println("Input chars that are not R B + or -");
            System.exit(0);
        }
    }
}
