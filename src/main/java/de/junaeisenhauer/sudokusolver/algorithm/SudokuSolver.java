package de.junaeisenhauer.sudokusolver.algorithm;

/**
 * Provides the algorithm to solve the sudoku.
 */
public class SudokuSolver {

    /**
     * Solves the sudoku grid with the backtracking algorithm.
     *
     * @param grid the grid of the sudoku. The array will be modified to solve it (call by reference).
     * @return if a solution was found
     */
    public boolean solve(int[][] grid) {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[x].length; y++) {
                if (grid[x][y] == 0) {
                    for (int value = 1; value <= 9; value++) {
                        grid[x][y] = value;
                        if (checkField(x, y, grid) && solve(grid)) {
                            return true;
                        }
                        grid[x][y] = 0;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks for same number in the horizontal and vertical line and checks for same number in the same 3x3 box.
     *
     * @param posX the x position of the field to check
     * @param posY the y position of the field to check
     * @param grid the sudoku grid
     * @return if the field is valid
     */
    public boolean checkField(int posX, int posY, int[][] grid) {
        int value = grid[posX][posY];
        if (value == 0) {
            return true;
        }

        // horizontal
        for (int x = 0; x < 9; x++) {
            if (x == posX) {
                continue;
            }
            if (grid[x][posY] == value) {
                return false;
            }
        }

        // vertical
        for (int y = 0; y < 9; y++) {
            if (y == posY) {
                continue;
            }
            if (grid[posX][y] == value) {
                return false;
            }
        }

        // sub grid
        int subGridX = posX - posX % 3;
        int subGridY = posY - posY % 3;

        for (int x = subGridX; x < subGridX + 3; x++) {
            for (int y = subGridY; y < subGridY + 3; y++) {
                if (x == posX && y == posY) {
                    continue;
                }
                if (grid[x][y] == value) {
                    return false;
                }
            }
        }

        return true;
    }

}
