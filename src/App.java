import java.util.Arrays;

/**
 * Print all possible human-readable Knight’s moves on a chessboard
 */
public class App {
    // Backtracking Approach---------------------------------------
    public static int X = 5, Y = 5; // X * Y chessboard
    public static int p = 1; // Counter for print

    // Following arrays show 8 possible movements for a knight.
    public static final int[] row = { 2, 1, -1, -2, -2, -1, 1, 2, 2 };
    public static final int[] col = { 1, 2, 2, 1, -1, -2, -2, -1, 1 };

    // Recursive function to perform the knight's tour using backtracking
    public static void knightTour(int visited[][], int A, int B, int position, int maxPrintCount) {
        // mark the current square as visited
        visited[A][B] = position;

        // if all squares are visited, print the solution
        if (position >= X * Y) {
            if (p > 7)
                return;
            print(visited);
            p++;
            // backtrack before returning
            visited[A][B] = 0;
            return;
        }

        // check for all eight possible movements for a knight
        // and recur for each valid movement
        for (int k = 0; k < 8; k++) {
            // get the new position of the knight from the current
            // position on the chessboard
            int newA = A + row[k];
            int newB = B + col[k];

            // if the new position is valid and not visited yet
            if (isValidMove(newA, newB) && visited[newA][newB] == 0) {
                knightTour(visited, newA, newB, position + 1, maxPrintCount);
            }
        }

        // backtrack and remove from the current path
        visited[A][B] = 0;
    }

    // Check for knight shouldn't go out of the chessboard and valid coordinate
    private static boolean isValidMove(int x, int y) {
        if (x < 0 || y < 0 || x >= X || y >= Y) {
            return false;
        }

        return true;
    }

    // Print
    private static void print(int[][] visited) {
        for (int[] array : visited) {
            System.out.println(Arrays.toString(array));
        }
        System.out.println("------");
    }
    
    public static void main(String[] args) throws Exception {
        int A = 0, B = 0; // Start position A * B
        // 'visited' to keeps track of squares involved in the knight's tour and stores
        // the order
        // in which the squares are visited
        int visited[][] = new int[X][Y];
        int pos = 1;
        int maxPrintCount = 7; // Print count

        // knight tour from A * B
        knightTour(visited, A, B, pos, maxPrintCount);
        System.out.println("Done!");
    }
}
