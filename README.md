# Print all possible human-readable Knight’s moves on a chessboard

### Problem Statement: 
Given a chess board of dimensions X by Y and a starting position of A and B, 
output the first P sequences of moves of a Knight piece that cover every field 
and return to the starting position.

A Knight piece can only move in an L-like motion, going two fields in one 
direction and then one field 90 degrees to that, or one field in one
direction and then two fields 90 degrees to that.

Create a function that takes four integers X, Y, A, B, P as inputs.
Print out a human-readable representation of a completed sequence, 
as soon as one has been found, to the console until P have been found
or the solution space is exhausted.

Use all cores available on a machine. Do not use "parallel" data structures
or libraries that contain those, i.e. data structures that have functions
built in to do parallel traversal.

Describe the tradeoffs between a single-core solution and a multi-core solution.


## Code

Eight posible moves from a point.
* int[] row = { 2, 1, -1, -2, -2, -1, 1, 2, 2 };
* int[] col = { 1, 2, 2, 1, -1, -2, -2, -1, 1 };

<img src="https://github.com/manojknit/KnightTour/blob/master/img/knight.png" width="350" height="400">

```
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
```

## Output
<img src="https://github.com/manojknit/KnightTour/blob/master/img/output.png" >


## Multi core usage test Code 
Following code is to test performance of multi core processors. It is based on thread spawning, however it is not at all good idea to do. As it is not limiting threads. 

```
package manoj;

import java.util.Arrays;

public class KnightTour implements Runnable {
    Thread t;
    int visited[][] = null;
    int pos = 1;
    int X, Y; //board size X*Y
    int sX, sY; // Starting position
    int printCount = 1; // Counter for print
    int maxPrintCount = 0; // first n prints 

    // Following arrays shows all eight possible movements for a knight.
    public static final int[] row = { 2, 1, -1, -2, -2, -1, 1, 2, 2 };
    public static final int[] col = { 1, 2, 2, 1, -1, -2, -2, -1, 1 };

    KnightTour(int visited[][], int X, int Y, int sX, int sY, int pos, int maxPrintCount)
    {
        this.X = X;
        this.Y = Y;
        this.sX = sX;
        this.sY = sY;
        this.pos = pos;
        this.maxPrintCount = maxPrintCount;
        if(visited == null)
            this.visited = new int[X][Y];
        else
            this.visited = visited;

        t = new Thread(this);
        t.start();
    }

    // Check if `(x, y)` is valid chessboard coordinates.
    // Note that a knight cannot go out of the chessboard
    private boolean isValid(int x, int y) {
        if (x < 0 || y < 0 || x >= this.X || y >= this.Y) {
            return false;
        }

        return true;
    }

    private static void print(int[][] visited) {
        for (int[] array : visited) {
            System.out.println(Arrays.toString(array));
        }
        System.out.println("------");
    }

    // Recursive function to perform the knight's tour using backtracking
    synchronized void knightTour(){//(int visited[][], int x, int y, int pos) {
        // mark the current square as visited
        int x = this.sX;
        int y = this.sY;
        int pos = this.pos;
        visited[x][y] = this.pos;

        // if all squares are visited, print the solution
        if (pos >= this.X * this.Y) {
            if (this.printCount > this.maxPrintCount)
                return;
            print(visited);
            this.printCount++;
            // backtrack before returning
            visited[x][y] = 0;
            return;
        }

        // check for all eight possible movements for a knight
        // and recur for each valid movement
        for (int k = 0; k < 8; k++) {
            // get the new position of the knight from the current
            // position on the chessboard
            int newX = x + row[k];
            int newY = y + col[k];

            // if the new position is valid and not visited yet
            if (this.isValid(newX, newY) && visited[newX][newY] == 0) {
                //knightTour(visited, newX, newY, pos + 1);
                new KnightTour(visited, this.X, this.Y, newX, newY, this.pos + 1, this.maxPrintCount);
            }
        }

        // backtrack from the current square and remove it from the current path
        visited[x][y] = 0;
    }

    @Override
    synchronized public void run() {
        knightTour();
    }
    
}
//Call from main
//KnightTour k = new KnightTour(null, X, Y, sX, sY, pos, maxPrintCount);
```

## Tradeoffs between a single-core execution and a multi-core execution
If I compile and run one thread in Sun's Java 1.1.7 on a 4-CPU Sun system. To compare performance let's turn off CPUs one at a time and run. I see two CPUs are much better than one, but beyond two, additional CPUs don't do not help much. This might be due to high context switching so we need to be very careful while choosing number of cores on the server. 

Java automatically scales to use all available processors. 

#### Multi-core 
Advantages: A multi-core processor works faster for certain programs. The server may not get as hot when it is turned on. The server needs less power because it can turn off sections that are not needed. 

Drawback: After certain limit context switching degrads performance. Adding more cores adds up cost.

#### Single-core
Cost will be less but will take more run time.

## Thank You
Manoj Kumar

