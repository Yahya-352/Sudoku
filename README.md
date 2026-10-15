# Sudoku Solver

A Spring Boot REST API that solves 9×9 Sudoku puzzles using **backtracking**.
Upload a puzzle as a `.txt` file and get the solved grid back as a downloadable file.

## Tech stack

- Java 21
- Spring Boot (Web MVC)
- Maven (wrapper included)

## Running the app

```bash
./mvnw spring-boot:run
```

On Windows use `mvnw.cmd spring-boot:run`. The server starts on `http://localhost:8080`.

## Puzzle file format

A plain `.txt` file with **9 lines of 9 digits**. Use `0` for empty cells. Blank lines are ignored.

```
000150070
106000820
300860040
900400567
004708300
732006004
040081009
017000208
050037000
```

Any other character, or a row that isn't exactly 9 digits long, is rejected.

## API

### `POST /solve`

| Part     | Value                                |
|----------|--------------------------------------|
| Body     | `multipart/form-data`                |
| Field    | `file`: the puzzle `.txt` file       |
| Response | The solution as a `.txt` attachment |

The server saves the solution as `<puzzle-name>.solution.txt` in its working directory and sends that file back:

```
4  2  8  |  1  5  9  |  6  7  3
1  9  6  |  3  7  4  |  8  2  5
3  7  5  |  8  6  2  |  9  4  1
----------------------------------
9  8  1  |  4  2  3  |  5  6  7
5  6  4  |  7  1  8  |  3  9  2
7  3  2  |  5  9  6  |  1  8  4
----------------------------------
2  4  3  |  6  8  1  |  7  5  9
6  1  7  |  9  4  5  |  2  3  8
8  5  9  |  2  3  7  |  4  1  6
```

**curl:**

```bash
curl -OJ -F "file=@puzzle1.txt" http://localhost:8080/solve
```

**Postman:** `POST` → Body → `form-data` → key `file` (type **File**) → choose your puzzle → **Send and Download**.

## The algorithm: backtracking

The solver (`SudokuSolver.solve`) fills the board one empty cell at a time. When it reaches a dead end, it undoes its last guess and tries something else.

1. **Find the next empty cell.** Scan the board row by row, left to right, for the first cell with value `0`. If there are none, every cell is filled with a valid digit, so the board is solved: **return `true`**.
2. **Try each digit 1 to 9** in that cell.
3. **Check the digit before placing it.** Skip it if the same digit already appears in the cell's **row**, **column**, or **3×3 box** (`isValid`).
4. **Place it.** Set the cell to that digit.
5. **Recurse.** Call `solve` again to fill the rest of the board. If that call returns `true`, the whole puzzle is solved: **return `true`** all the way up.
6. **Undo (backtrack).** If the recursive call returns `false`, this digit led to a dead end. Reset the cell to `0` and continue with the next digit.
7. **Give up on this cell.** If none of the 9 digits work, **return `false`**. This sends control back to the previous cell, which then undoes its digit and tries its next one.

Notes:

- **The original numbers are never changed.** The solver only ever writes to cells that were `0`, so the puzzle's given numbers stay as they are.
- **Finding the 3×3 box:** the top-left corner of a cell's box is `(row / 3) * 3, (col / 3) * 3`, using integer division.
- **If the very first call returns `false`, the puzzle has no solution.** Every possibility was tried and all of them hit a dead end.
- **Worst case is exponential**, but real puzzles have many filled cells, so most digits get ruled out quickly and solving takes milliseconds.

## Project structure

```
src/main/java/com/ga/sudokuSolver/
├── Controller/SudokuController.java    # POST /solve, returns the solution file
├── Service/SudokuService.java          # reads the upload, solves, saves
├── Exceptions/                         # InvalidCharacterException, SudokuFileNotFoundException
├── Sudoku.java                         # 9×9 grid: load, save, format, print
├── SudokuCell.java                     # one cell: row, col, value, fixed flag
├── SudokuSolver.java                   # backtracking algorithm
└── SolverTest.java                     # console demo with a sample puzzle
```

## Known limitations

- **Unsolvable puzzles aren't reported as errors.** If `solve` returns `false`, the API still returns HTTP 200 with the unsolved grid, zeros included.
- **Invalid files return HTTP 500.** For example, a bad character or a wrong number of rows. The response includes the error message, but a `400 Bad Request` would be more accurate.
- **Same-name uploads overwrite.** Solution files are named after the upload, so uploading the same file name again replaces the previous solution.
