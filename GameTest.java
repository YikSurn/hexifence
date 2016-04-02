import java.util.Scanner;

public class GameTest {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int[][] boardState;
        int entries; // store max board state entries per row or col
        try {
            int boardSize = input.nextInt();
            entries = 4*boardSize - 1;
            boardState = new int[entries][entries];
            int row= 0, col = 0;
            while (input.hasNext()) {
                String tempStr = input.nextLine();
                Scanner innerInput = new Scanner(tempStr);
                while (innerInput.hasNextInt()) {
                    boardState[row][col] = innerInput.nextInt();
                    if (col > entries) {
                        System.out.println("Exceeded max col input for board state");
                        System.exit(0);
                    }
                    col++;
                }
                if (row > entries) {
                    System.out.println("Exceeded max row input for board state");
                    System.exit(0);
                }
                row++;
            }
        }
        catch (Exception e) {
            // Temporary handle errors
            System.out.println("Invalid input");
            System.exit(0);
        }
    }
}
