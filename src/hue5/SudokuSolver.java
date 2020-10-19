package hue5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/* Please enter here an answer to task four between the tags:
 * <answerTask4>
 *    Hier sollte die Antwort auf die Aufgabe 4 stehen.
 * </answerTask4>
 */
public class SudokuSolver implements ISodukoSolver {

    private int[][] inputSudoku;
    private Cell[][] wrappedSudoku;
    private List<Unit> rows;
    private List<Unit> columns;
    private List<Unit> blocks;

    public SudokuSolver() {
        //initialize if necessary
    }

    @Override
    public final int[][] readSudoku(File file) {
        try {
            inputSudoku = Files.lines(file.toPath())
                    .map(s -> s.split(";"))
                    .map(a -> new int[]{
                Integer.parseInt(a[0]),
                Integer.parseInt(a[1]),
                Integer.parseInt(a[2]),
                Integer.parseInt(a[3]),
                Integer.parseInt(a[4]),
                Integer.parseInt(a[5]),
                Integer.parseInt(a[6]),
                Integer.parseInt(a[7]),
                Integer.parseInt(a[8])})
                    .toArray(int[][]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputSudoku;
    }

    @Override
    public boolean checkSudoku(int[][] rawSudoku) {
        // row checker
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 8; col++) {
                for (int col2 = col + 1; col2 < 9; col2++) {
                    if (rawSudoku[row][col] == rawSudoku[row][col2]) {
                        return false;
                    }
                }
            }
        }

// column checker
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 8; row++) {
                for (int row2 = row + 1; row2 < 9; row2++) {
                    if (rawSudoku[row][col] == rawSudoku[row2][col]) {
                        return false;
                    }
                }
            }
        }

// grid checker
        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) // row, col is start of the 3 by 3 grid
            {
                for (int pos = 0; pos < 8; pos++) {
                    for (int pos2 = pos + 1; pos2 < 9; pos2++) {
                        if (rawSudoku[row + pos % 3][col + pos / 3] == rawSudoku[row + pos2 % 3][col + pos2 / 3]) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public int[][] solveSudoku(int[][] rawSudoku) {
        // implement this method
        return new int[0][0]; // delete this line!
    }

    @Override
    public int[][] solveSudokuParallel(int[][] rawSudoku) {
        // implement this method
        return new int[0][0]; // delete this line!
    }

    public long benchmark() {
    }

    // add helper methods here if necessary
    @Override
    public String toString() {
        return "SudokuSolver{" + "inputSudoku=" + inputSudoku + ", wrappedSudoku=" + wrappedSudoku + ", rows=" + rows + ", columns=" + columns + ", blocks=" + blocks + '}';
    }

    private Cell[][] wrapRawSudoku(int[][] rawSudoku) {
    }

    private void initializeUnits(Cell[][] wrappedSudoku) {
    }
}
