package com.ga.sudokuSolver;

public class SudokuSolver {

    private final int START_INDEX = 0;
    private final int END_INDEX = 8;

    private final int NO_VALUE = 0;
    private final int MIN_VAL = 1;
    private final int MAX_VAL = 9;

    public boolean solve(Sudoku sudoku){
        for(int r = START_INDEX ; r <= END_INDEX ; r++){
            for(int c = START_INDEX ; c <= END_INDEX ; c++){
                if(sudoku.getCell(r,c).getValue() == NO_VALUE){
                    for(int d = MIN_VAL ; d <= MAX_VAL ; d++){
                        if(isValid(sudoku , r , c , d)){
                            sudoku.getCell(r,c).setValue(d);
                            if(solve(sudoku)){
                                return true;
                            }else {
                                sudoku.getCell(r,c).setValue(NO_VALUE);
                            }
                        }
                    }
                    return false;
                }
            }
        }return true;
    }

    public boolean isValid(Sudoku sudoku , int row , int column , int value){
        //check columns
        if(!checkColumns(sudoku, row, column, value)){
            return false;
        };

        //check rows
        if(!checkRows(sudoku, row, column, value)){
            return false;
        };

        //check in 3x3 grid
        int startRow = (row / 3) * 3;
        int startCol = (column / 3) * 3;
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (sudoku.getCell(r, c).getValue() == value) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkRows(Sudoku sudoku , int row , int column , int value){
        for (int c = 0; c < 9; c++) {
            if (sudoku.getCell(row, c).getValue() == value) {
                return false;
            }
        }
        return true;
    }

    public boolean checkColumns(Sudoku sudoku , int row , int column , int value){
        for (int r = 0; r < 9; r++) {
            if (sudoku.getCell(r , column).getValue() == value) {
                return false;
            }
        }
        return true;
    }

}
