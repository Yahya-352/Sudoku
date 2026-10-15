package com.ga.sudokuSolver;

import com.ga.sudokuSolver.Exceptions.InvalidCharacterException;
import com.ga.sudokuSolver.Exceptions.SudokuFileNotFoundException;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Sudoku {
    String puzzleName;
    private SudokuCell[][] grid = new SudokuCell[9][9];

    public Sudoku() {
    }

    public Sudoku(int[][] values) {
        for (int r = 0; r < 9; r++)
            for (int c = 0; c < 9; c++)
                grid[r][c] = new SudokuCell(r, c, values[r][c]);
    }

    public SudokuCell getCell(int row, int col) {
        return grid[row][col];
    }

    public void load(String filename) throws SudokuFileNotFoundException, IOException, InvalidCharacterException {
        Path path = FileSystems.getDefault().getPath(filename);
        if (!path.toFile().exists()) {
            throw new SudokuFileNotFoundException("File Not Found: " + filename);
        }
        loadLines(Files.readAllLines(path), filename);
    }

    public void loadLines(List<String> lines, String filename) throws InvalidCharacterException {
        lines = new ArrayList<>(lines);
        lines.removeIf(line -> line.isBlank());

        if (lines.size() != 9) {
            throw new InvalidCharacterException(filename + " must have 9 rows");
        }
        for (int row = 0; row < 9; row++) {
            if (lines.get(row).length() != 9) {
                throw new InvalidCharacterException("Row " + (row + 1) + " must have 9 digits");
            }
            for (int col = 0; col < 9; col++) {
                char ch = lines.get(row).charAt(col);
                if (ch < '0' || ch > '9') {
                    throw new InvalidCharacterException(
                            "Invalid character '" + ch + "' at row " + (row + 1) + ", column " + (col + 1));
                }
                grid[row][col] = new SudokuCell(row, col, ch - '0');
            }
        }
        puzzleName = filename.replace(".txt", "");
    }

    public Path save() throws IOException {
        return Files.writeString(Path.of(puzzleName + ".solution.txt"), format());
    }

    public String format() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                sb.append(grid[r][c].getValue());
                if (c == 2 || c == 5) sb.append("  |  ");
                else if (c < 8) sb.append("  ");
            }
            sb.append("\n");
            if (r == 2 || r == 5) sb.append("----------------------------------\n");
        }
        return sb.toString();
    }
}