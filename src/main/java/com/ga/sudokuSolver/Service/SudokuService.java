package com.ga.sudokuSolver.Service;

import com.ga.sudokuSolver.Sudoku;
import com.ga.sudokuSolver.SudokuSolver;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Service
public class SudokuService {

    private final SudokuSolver solver = new SudokuSolver();

    public Path solve(MultipartFile file) throws Exception {
        List<String> lines = new String(file.getBytes()).lines().toList();

        Sudoku sudoku = new Sudoku();
        sudoku.loadLines(lines, file.getOriginalFilename());
        solver.solve(sudoku);

        return sudoku.save();
    }
}