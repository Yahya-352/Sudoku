package com.ga.sudokuSolver;

public class SudokuCell {
    private int row;
    private int col;
    private int value;
    private boolean fixedNumber;

    public SudokuCell(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
        checkFixedValues();
    }

    private boolean checkFixedValues(){
        if (value != 0) {
            this.fixedNumber = true;
        } else {
            this.fixedNumber = false;
        }
        return fixedNumber;
    }

    public boolean isFixedNumber() {
        return fixedNumber;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
