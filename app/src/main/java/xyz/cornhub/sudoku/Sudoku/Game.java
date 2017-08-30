package xyz.cornhub.sudoku.Sudoku;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

/*
@author Eric Beijer
*/

public class Game extends Observable {
    private boolean[][] check = ((boolean[][]) Array.newInstance(Boolean.TYPE, new int[]{9, 9}));
    private int[][] game;
    private boolean help = true;
    private int selectedNumber;
    private int[][] solution;

    public Game() {
        newGame();
    }

    public void newGame() {
        this.solution = generateSolution((int[][]) Array.newInstance(Integer.TYPE, new int[]{9, 9}), 0);
        this.game = generateGame(copy(this.solution));
        setChanged();
        notifyObservers(UpdateAction.NEW_GAME);
    }

    public void checkGame() {
        this.selectedNumber = 0;
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                boolean z;
                boolean[] zArr = this.check[y];
                if (this.game[y][x] == this.solution[y][x]) {
                    z = true;
                } else {
                    z = false;
                }
                zArr[x] = z;
            }
        }
        setChanged();
        notifyObservers(UpdateAction.CHECK);
    }

    public void setHelp(boolean help) {
        this.help = help;
        setChanged();
        notifyObservers(UpdateAction.HELP);
    }

    public void setSelectedNumber(int selectedNumber) {
        this.selectedNumber = selectedNumber;
        setChanged();
        notifyObservers(UpdateAction.SELECTED_NUMBER);
    }

    public int getSelectedNumber() {
        return this.selectedNumber;
    }

    public boolean isHelp() {
        return this.help;
    }

    public boolean isSelectedNumberCandidate(int x, int y) {
        return this.game[y][x] == 0 && isPossibleX(this.game, y, this.selectedNumber) && isPossibleY(this.game, x, this.selectedNumber) && isPossibleBlock(this.game, x, y, this.selectedNumber);
    }

    public void setNumber(int x, int y, int number) {
        this.game[y][x] = number;
    }

    public int getNumber(int x, int y) {
        return this.game[y][x];
    }

    public boolean isCheckValid(int x, int y) {
        return this.check[y][x];
    }

    private boolean isPossibleX(int[][] game, int y, int number) {
        for (int x = 0; x < 9; x++) {
            if (game[y][x] == number) {
                return false;
            }
        }
        return true;
    }

    private boolean isPossibleY(int[][] game, int x, int number) {
        for (int y = 0; y < 9; y++) {
            if (game[y][x] == number) {
                return false;
            }
        }
        return true;
    }

    private boolean isPossibleBlock(int[][] game, int x, int y, int number) {
        int y1 = 3;
        int x1 = x < 3 ? 0 : x < 6 ? 3 : 6;
        if (y < 3) {
            y1 = 0;
        } else if (y >= 6) {
            y1 = 6;
        }
        for (int yy = y1; yy < y1 + 3; yy++) {
            for (int xx = x1; xx < x1 + 3; xx++) {
                if (game[yy][xx] == number) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getNextPossibleNumber(int[][] game, int x, int y, List<Integer> numbers) {
        while (numbers.size() > 0) {
            int number = ((Integer) numbers.remove(0)).intValue();
            if (isPossibleX(game, y, number) && isPossibleY(game, x, number) && isPossibleBlock(game, x, y, number)) {
                return number;
            }
        }
        return -1;
    }

    private int[][] generateSolution(int[][] game, int index) {
        if (index > 80) {
            return game;
        }
        int x = index % 9;
        int y = index / 9;
        List<Integer> numbers = new ArrayList();
        for (int i = 1; i <= 9; i++) {
            numbers.add(Integer.valueOf(i));
        }
        Collections.shuffle(numbers);
        while (numbers.size() > 0) {
            int number = getNextPossibleNumber(game, x, y, numbers);
            if (number == -1) {
                return (int[][]) null;
            }
            game[y][x] = number;
            int[][] tmpGame = generateSolution(game, index + 1);
            if (tmpGame != null) {
                return tmpGame;
            }
            game[y][x] = 0;
        }
        return (int[][]) null;
    }

    private int[][] generateGame(int[][] game) {
        List<Integer> positions = new ArrayList();
        for (int i = 0; i < 81; i++) {
            positions.add(Integer.valueOf(i));
        }
        Collections.shuffle(positions);
        return generateGame(game, positions);
    }

    private int[][] generateGame(int[][] game, List<Integer> positions) {
        while (positions.size() > 0) {
            int position = ((Integer) positions.remove(0)).intValue();
            int x = position % 9;
            int y = position / 9;
            int temp = game[y][x];
            game[y][x] = 0;
            if (!isValid(game)) {
                game[y][x] = temp;
            }
        }
        return game;
    }

    private boolean isValid(int[][] game) {
        return isValid(game, 0, new int[]{0});
    }

    private boolean isValid(int[][] game, int index, int[] numberOfSolutions) {
        if (index > 80) {
            int i = numberOfSolutions[0] + 1;
            numberOfSolutions[0] = i;
            if (i == 1) {
                return true;
            }
            return false;
        }
        int x = index % 9;
        int y = index / 9;
        if (game[y][x] == 0) {
            List<Integer> numbers = new ArrayList();
            for (int i2 = 1; i2 <= 9; i2++) {
                numbers.add(Integer.valueOf(i2));
            }
            while (numbers.size() > 0) {
                int number = getNextPossibleNumber(game, x, y, numbers);
                if (number == -1) {
                    return true;
                }
                game[y][x] = number;
                if (isValid(game, index + 1, numberOfSolutions)) {
                    game[y][x] = 0;
                } else {
                    game[y][x] = 0;
                    return false;
                }
            }
            return true;
        } else if (isValid(game, index + 1, numberOfSolutions)) {
            return true;
        } else {
            return false;
        }
    }

    private int[][] copy(int[][] game) {
        int[][] copy = (int[][]) Array.newInstance(Integer.TYPE, new int[]{9, 9});
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                copy[y][x] = game[y][x];
            }
        }
        return copy;
    }

    public String toString() {
        String print = "";
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                print = print + String.valueOf(this.game[x][y]);
            }
            print = print + "\n";
        }
        return print;
    }

    public void newEmptyGame() {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                this.game[x][y] = 0;
            }
        }
    }
}
