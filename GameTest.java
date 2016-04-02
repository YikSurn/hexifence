import java.util.Scanner;

public class GameTest {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int[][] 2DArray;
        int entries; // store board state entries per row and col
        try {
            int boardSize = input.nextInt();
            entries = 4*boardSize - 1;
            2DArray = new int[entries][entries];
            int row, col = 0;
            while (input.hasNextInt()) {
                // store board state into a 2D array
                int[row][col] = input.nextInt();
                if (col < entries) {
                    col++;
                }
                else if (col == entries) {
                    if (row >= entries) {
                        System.out.println("Exceeded max input for board state");
                        System.exit(0);
                    }
                    row++;
                    col = 0;
                }
            }
        }
        catch (Exception e) {
            // Temporary handle errors
            System.out.println("Invalid input");
            System.exit(0);
        }
    }
}
