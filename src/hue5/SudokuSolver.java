package hue5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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
        initializeUnits(wrapRawSudoku(rawSudoku));

        ExecutorService exec;
        exec = Executors.newFixedThreadPool(3);

        Callable<Boolean> rowChecker = () -> {
            // row checker
            for (Unit row : rows) {
                if (!row.isCorrect()) {
                    return false;
                }
            }
            return true;
        };

        Callable<Boolean> columnChecker = () -> {
            // column checker
            for (Unit column : columns) {
                if (!column.isCorrect()) {
                    return false;
                }
            }
            return true;
        };

        Callable<Boolean> gridChecker = () -> {
            // grid checker
            for (Unit block : blocks) {
                if (!block.isCorrect()) {
                    return false;
                }
            }
            return true;
        };

        Future<Boolean> rowCheckerResult = exec.submit(rowChecker);
        Future<Boolean> columnCheckerResult = exec.submit(columnChecker);
        Future<Boolean> gridCheckerResult = exec.submit(gridChecker);
        exec.shutdown();
        try {
            exec.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(SudokuSolver.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            return rowCheckerResult.get() && columnCheckerResult.get() && gridCheckerResult.get();
        } catch (InterruptedException | ExecutionException ex) {
            return false;
        }

    }

    @Override
    public int[][] solveSudoku(int[][] rawSudoku) {
        initializeUnits(wrapRawSudoku(rawSudoku));

        boolean fixed = true;
        while (fixed) {
            fixed = false;

            //entfernt die values die aufgrund der Regeln nicht möglich sind
            // Reduce
            for (Unit row : rows) {
                row.reducePossibleValues();
            }
            for (Unit column : columns) {
                column.reducePossibleValues();
            }
            for (Unit block : blocks) {
                block.reducePossibleValues();
            }

            //fixiert zahl die übrig bleibt
            // Select
            for (Unit block : blocks) {
                if (block.tryToSelectValue()) {
                    fixed = true;
                }
            }
        }

        int[][] solvedSudoku = new int[wrappedSudoku.length][wrappedSudoku[0].length];
        //speichert alle Zahlen ind das finale Integer Arr
        for (int row = 0; row < solvedSudoku.length; row++) {
            for (int col = 0; col < solvedSudoku[row].length; col++) {
                solvedSudoku[row][col] = wrappedSudoku[row][col].getSelectedValue();
            }
        }
        return solvedSudoku;
    }

    @Override
    public int[][] solveSudokuParallel(int[][] rawSudoku) {
        initializeUnits(wrapRawSudoku(rawSudoku));

        ExecutorService exec;
        exec = Executors.newFixedThreadPool(9);

        boolean fixed = true;
        while (fixed) {
            fixed = false;

            List<Callable<Boolean>> tasks = new ArrayList<>();

            // Reduce
            for (Unit row : rows) {
                tasks.add(() -> {
                    row.reducePossibleValues();
                    return true;
                });
            }
            try {
                exec.invokeAll(tasks);
            } catch (InterruptedException ex) {
                return new int[0][0];
            }

            tasks.clear();
            for (Unit column : columns) {
                tasks.add(() -> {
                    column.reducePossibleValues();
                    return true;
                });
            }
            try {
                exec.invokeAll(tasks);
            } catch (InterruptedException ex) {
                return new int[0][0];
            }

            tasks.clear();
            for (Unit block : blocks) {
                tasks.add(() -> {
                    block.reducePossibleValues();
                    return true;
                });
            }
            try {
                exec.invokeAll(tasks);
            } catch (InterruptedException ex) {
                return new int[0][0];
            }

            // Select
            tasks.clear();
            for (Unit block : blocks) {
                tasks.add(() -> {
                    return block.tryToSelectValue();
                });
            }
            try {
                List<Future<Boolean>> selectResults = exec.invokeAll(tasks);
                for (Future<Boolean> result : selectResults) {
                    if (result.get()) {
                        fixed = true;
                    }
                }
            } catch (InterruptedException | ExecutionException ex) {
                return new int[0][0];
            }
        }

        int[][] solvedSudoku = new int[wrappedSudoku.length][wrappedSudoku[0].length];
        for (int row = 0; row < solvedSudoku.length; row++) {
            for (int col = 0; col < solvedSudoku[row].length; col++) {
                solvedSudoku[row][col] = wrappedSudoku[row][col].getSelectedValue();
            }
        }
        return solvedSudoku;
    }

    public long benchmark(int[][] rawSudoku) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            checkSudoku(rawSudoku);
            solveSudokuParallel(rawSudoku);
        }
        return (System.currentTimeMillis() - time) / 10;
    }

    // add helper methods here if necessary
    @Override
    public String toString() {
        return "SudokuSolver{" + "inputSudoku=" + inputSudoku + ", wrappedSudoku=" + wrappedSudoku + ", rows=" + rows + ", columns=" + columns + ", blocks=" + blocks + '}';
    }

    private Cell[][] wrapRawSudoku(int[][] rawSudoku) {
        this.wrappedSudoku = new Cell[9][9];
        this.inputSudoku = rawSudoku;
        for (int row = 0; row < inputSudoku.length; row++) {
            for (int column = 0; column < inputSudoku[row].length; column++) {
                Cell cell;
                if (inputSudoku[row][column] != 0) {
                    //speichert fixen wert
                    cell = new Cell(inputSudoku[row][column]);
                } else {
                    cell = new Cell();
                }
                wrappedSudoku[row][column] = cell;
            }
        }
        return wrappedSudoku;
    }

    private void initializeUnits(Cell[][] wrappedSudoku) {
        this.rows = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.blocks = new ArrayList<>();

        for (int row = 0; row < 9; row++) {
            List<Cell> collectionOfCells = new ArrayList<>();
            for (int col = 0; col < 9; col++) {
                collectionOfCells.add(wrappedSudoku[row][col]);
            }
            rows.add(new Unit(collectionOfCells));
        }

        for (int col = 0; col < 9; col++) {
            List<Cell> collectionOfCells = new ArrayList<>();
            for (int row = 0; row < 9; row++) {
                collectionOfCells.add(wrappedSudoku[row][col]);
            }
            columns.add(new Unit(collectionOfCells));
        }

        for (int row = 0; row < 9; row += 3) {
            for (int col = 0; col < 9; col += 3) // row, col is start of the 3 by 3 grid
            {
                List<Cell> collectionOfCells = new ArrayList<>();
                for (int row2 = row; row2 < row + 3; row2++) {
                    for (int col2 = col; col2 < col + 3; col2++) {
                        collectionOfCells.add(wrappedSudoku[row2][col2]);
                    }
                }
                blocks.add(new Unit(collectionOfCells));
            }
        }
    }
}
