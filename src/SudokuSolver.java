import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class SudokuSolver {
    // add ansi colors
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_WHITE = "\u001B[37m";
    // add ansi background colors
    private static final String ANSI_BACKGROUND_RED = "\u001B[41m";
    private static final String ANSI_BACKGROUND_GREEN = "\u001B[42m";
    private static final String ANSI_BACKGROUND_BLUE = "\u001B[44m";
    private static final String ANSI_BACKGROUND_PURPLE = "\u001B[45m";
    private static final String ANSI_BACKGROUND_CYAN = "\u001B[46m";
    private static final String ANSI_BACKGROUND_YELLOW = "\u001B[43m";
    private static final String ANSI_BACKGROUND_WHITE = "\u001B[47m";

    private static final int GRID_SIZE = 9;

    public static void main(String[] args) {
        int[][] board = {
                {6, 0, 0, 0, 0, 1, 0, 2, 0},
                {0, 8, 0, 0, 2, 6, 0, 7, 1},
                {2, 0, 0, 0, 3, 4, 6, 0, 0},
                {0, 0, 6, 0, 0, 0, 2, 4, 0},
                {4, 0, 0, 0, 6, 0, 7, 0, 0},
                {7, 0, 9, 4, 5, 0, 0, 1, 6},
                {0, 7, 0, 2, 0, 3, 1, 6, 0},
                {0, 0, 5, 6, 0, 7, 9, 8, 2},
                {0, 6, 2, 0, 0, 5, 0, 0, 7}
        };
        //int[][] board = generateUnsolvedSudoku(30);

        printBoard(board);
        // new line
        System.out.println();

        if (solveBoard(board)) {
            System.out.println("Solved successfully:");
            printBoard(board, true);
        } else {
            System.out.println("Unable to solve:");
            printBoard(board, false);
        }
    }



    private static void printBoard(int[][] board, boolean solvedCorrectlyColor) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                if (solvedCorrectlyColor) {
                    System.out.println(ANSI_GREEN + "------------------------" + ANSI_RESET);
                } else {
                    System.out.println(ANSI_RED + "------------------------" + ANSI_RESET);
                }
            }
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col % 3 == 0 && col != 0) {
                    if (solvedCorrectlyColor) {
                        System.out.print(ANSI_GREEN + " | " + ANSI_RESET);
                    } else {
                        System.out.print(ANSI_RED + " | " + ANSI_RESET);
                    }
                }
                if (solvedCorrectlyColor) {
                    System.out.print(ANSI_GREEN + board[row][col] + ANSI_RESET + " ");
                } else {
                    System.out.print(ANSI_RED + board[row][col] + ANSI_RESET + " ");
                }
            }
            System.out.println();
        }
    }

    private static void printBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            if (row % 3 == 0 && row != 0) {
                System.out.println("------------------------");
            }
            for (int col = 0; col < GRID_SIZE; col++) {
                if (col % 3 == 0 && col != 0) {
                    System.out.print(" | ");
                }
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }

    
    private static boolean isNumberInRow(int[][] board, int number, int row) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[row][i] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInCol(int[][] board, int number, int col) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board[i][col] == number) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNumberInBox(int[][] board, int number, int row, int col) {
        int localBoxRow = row - row % 3;
        int localBoxCol = col - col % 3;

        for (int i = localBoxRow; i < localBoxRow + 3; i++) {
            for (int j = localBoxCol; j < localBoxCol + 3; j++) {
                if (board[i][j] == number) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isValidPlacement(int[][] board, int number, int row, int col) {
        return !isNumberInRow(board, number, row) && !isNumberInCol(board, number, col) && !isNumberInBox(board, number, row, col);
    }

    private static boolean solveBoard(int[][] board) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int numberToTry = 1; numberToTry <= GRID_SIZE; numberToTry++) {
                        if (isValidPlacement(board, numberToTry, row, col)) {
                            board[row][col] = numberToTry;

                            if (solveBoard(board)) {
                                return true;
                            } else {
                                board[row][col] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private static int[][] generateUnsolvedSudoku(int numberOfCellsToFill) {
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        int[][] boardCopy = new int[GRID_SIZE][GRID_SIZE];
        board = addAnAmoutOfNumbersToBoard(board, numberOfCellsToFill);
        boardCopy = copyBoard(board);
        while (!solveBoard(board)) {
            // clear board
            board = clearBoard();
            boardCopy = clearBoard();
            // add new numbers
            board = addAnAmoutOfNumbersToBoard(board, numberOfCellsToFill);
            // copy board
            boardCopy = copyBoard(board);
        }
        return boardCopy;
    }

    private static int[][] addAnAmoutOfNumbersToBoard(int[][] board, int amount) {
        for (int i = 0; i < amount; i++) {
            // get random row and col that is not already filled
            int row = (int) (Math.random() * GRID_SIZE);
            int col = (int) (Math.random() * GRID_SIZE);
            while (board[row][col] != 0) {
                row = (int) (Math.random() * GRID_SIZE);
                col = (int) (Math.random() * GRID_SIZE);
            }

            // get random number between 1 and 9
            int number = (int) (Math.random() * GRID_SIZE) + 1;
            // check if number is valid
            while (!isValidPlacement(board, number, row, col)) {
                number = (int) (Math.random() * GRID_SIZE) + 1;
            }
            board[row][col] = number;
        }
        return board;
    }

    private static int numberOfEmptyCells(int[][] board) {
        int emptyCells = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (board[row][col] == 0) {
                    emptyCells++;
                }
            }
        }
        return emptyCells;
    }

    private static int[][] deleteAmountNumberOfBoard(int[][] board, int amount) {
        for (int i = 0; i < amount; i++) {
            //remove a number from the board that is not 0
            int randomRow = new Random().nextInt(GRID_SIZE);
            int randomCol = new Random().nextInt(GRID_SIZE);
            while (board[randomRow][randomCol] == 0) {
                randomRow = new Random().nextInt(GRID_SIZE);
                randomCol = new Random().nextInt(GRID_SIZE);
            }
            board[randomRow][randomCol] = 0;
        }
        return board;
    }

    private static int[][] swapTwoRows(int[][] board, int row1, int row2) {
        int[] temp = board[row1];
        board[row1] = board[row2];
        board[row2] = temp;
        return board;
    }

    private static int[][] swapTwoCols(int[][] board, int col1, int col2) {
        for (int row = 0; row < GRID_SIZE; row++) {
            int temp = board[row][col1];
            board[row][col1] = board[row][col2];
            board[row][col2] = temp;
        }
        return board;
    }

    private static int[][] swapAmountOfTimes(int[][] board, int amount) {
        for (int i = 0; i < amount; i++) {
            int randomRow1 = new Random().nextInt(GRID_SIZE);
            int randomRow2 = new Random().nextInt(GRID_SIZE);
            int randomCol1 = new Random().nextInt(GRID_SIZE);
            int randomCol2 = new Random().nextInt(GRID_SIZE);
            swapTwoRows(board, randomRow1, randomRow2);
            swapTwoCols(board, randomCol1, randomCol2);
        }
        return board;
    }

    private static int[][] copyBoard(int[][] board) {
        int[][] boardCopy = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                boardCopy[row][col] = board[row][col];
            }
        }
        return boardCopy;
    }

    private static int[][] clearBoard() {
        int[][] board = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                board[row][col] = 0;
            }
        }
        return board;
    }
}
