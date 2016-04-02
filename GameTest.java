import java.util.Scanner;

public class GameTest {

    public static char[] validChars = new char[] {'R', 'B', '+', '-'};

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        char[][] boardState;
        int entries; // store max board state entries per row or col
        try {
            int boardSize = input.nextInt();
            entries = 4*boardSize - 1;
            boardState = new char[entries][entries];
            int row= 0, col = 0;
            while (input.hasNext()) {
                String tempStr = input.nextLine();
                Scanner innerInput = new Scanner(tempStr);
                while (innerInput.hasNext()) {
                    // Check for improper character inputs
                    char val = innerInput.next();
                    if (existIn(val, validChars)) {
                        boardState[row][col] = innerInput.next();
                    }
                    else {
                        System.out.println("Invalid syntax for board state. Only these characters are valid: R, B, +, -");
                        System.exit(0);
                    }
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

    public boolean existIn(char a, char[] charArray) {
        for (char c: charArray) {
            if (c == a) {
                return true;
            }
        }
        return false;
    }
}
