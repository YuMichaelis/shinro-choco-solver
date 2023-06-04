/* Author : Ambre Journot */

import org.chocosolver.solver.search.strategy.Search;
import java.util.AbstractMap;
import java.util.Map;
public class ShinroHelper extends ShinroSolver {

    /**
     * Constructor
     *
     * @param shinroGridState initial state of the grid
     * @param rowBalls   number of balls per row
     * @param columnBalls number of balls per column
     */
    public ShinroHelper(int[][] shinroGridState, int[] rowBalls, int[] columnBalls) {
        super(shinroGridState, rowBalls, columnBalls);
    }

    @Override
    public void configureSearch() {
        model.getSolver().setSearch(Search.activityBasedSearch(flatten(grid)));
    }
    
    @Override
    public void solve() {
        model.getSolver().solve();
        int misplacedBalls = countMisplacedBalls();
        
        if (misplacedBalls > 0) {
            System.out.println("There are " + misplacedBalls + " misplaced balls.");
        } else {
            Map.Entry<Integer, Integer> cell = findMostSatisfyingCell();
            if (cell != null) {
                System.out.println("The helper suggests placing a ball at cell (" + cell.getKey() + ", " + cell.getValue() + ")");
            } else {
                System.out.println("The helper could not find a suitable cell to suggest.");
            }
        }
    }

    /**
     * @return number of misplaced balls
     */
    private int countMisplacedBalls() {
        int misplacedBalls = 0;
        int gridSize = grid.length;
    
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (shinroGridState[i][j] == 1 && grid[i][j].getValue() != 1) {
                    misplacedBalls++;
                }
            }
        }
    
        return misplacedBalls;
    }

    /**
     * @return the cell that satisfies the most constraints
     */
    public Map.Entry<Integer, Integer> findMostSatisfyingCell() {
        int maxConstraintsSatisfied = -1;
        Map.Entry<Integer, Integer> mostSatisfyingCell = null;

        int gridSize = grid.length;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j].getValue() == 1 && shinroGridState[i][j] != 1) {
                    int constraintsSatisfied = countConstraintsSatisfied(i, j);
                    if (constraintsSatisfied > maxConstraintsSatisfied) {
                        maxConstraintsSatisfied = constraintsSatisfied;
                        mostSatisfyingCell = new AbstractMap.SimpleEntry<>(i, j);
                    }
                }
            }
        }

        return mostSatisfyingCell;
    }

    /**
     * @param row row index of the cell
     * @param col column index of the cell
     * @return number of constraints satisfied by the cell
     */
    private int countConstraintsSatisfied(int row, int col) {
        int gridSize = grid.length;
        int constraintsSatisfied = 0;
    
        // Count balls in same row and column
        for (int i = 0; i < gridSize; i++) {
            if (grid[row][i].getValue() == 1) constraintsSatisfied++;
            if (grid[i][col].getValue() == 1) constraintsSatisfied++;
        }

        // Subtract count of balls exceeding rowBalls and columnBalls
        if (constraintsSatisfied > rowBalls[row]) constraintsSatisfied -= (constraintsSatisfied - rowBalls[row]);
        if (constraintsSatisfied > columnBalls[col]) constraintsSatisfied -= (constraintsSatisfied - columnBalls[col]);

        // Check arrows constraints
        constraintsSatisfied += countArrowSatisfied(row, col);
        
        System.out.println("Cell (" + row + ", " + col + ") has " + constraintsSatisfied + " constraints satisfied.");
        return constraintsSatisfied;
    }

    /**
     * @param row row index of the cell
     * @param col column index of the cell
     * @return number of arrow constraints satisfied by the cell
     */
    private int countArrowSatisfied(int row, int col) {
        int arrowSatisfied = 0;
        int gridSize = grid.length;
    
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                switch (shinroGridState[i][j]) {
                    case 2: // Arrow points right
                        if (i == row && j < col) {
                            arrowSatisfied++;
                        }
                        break;
                    case 3: // Arrow points left
                        if (i == row && j > col) {
                            arrowSatisfied++;
                        }
                        break;
                    case 4: // Arrow points up
                        if (j == col && i > row) {
                            arrowSatisfied++;
                        }
                        break;
                    case 5: // Arrow points down
                        if (j == col && i < row) {
                            arrowSatisfied++;
                        }
                        break;
                    case 6: // Arrow points top-right
                        if ((col - j) == (i - row) && i > row && j < col) {
                            arrowSatisfied++;
                        }
                        break;
                    case 7: // Arrow points top-left
                        if ((col - j) == -(i - row) && i > row && j > col) {
                            arrowSatisfied++;
                        }
                        break;
                    case 8: // Arrow points bottom-right
                        if ((col - j) == -(i - row) && i < row && j < col) {
                            arrowSatisfied++;
                        }
                        break;
                    case 9: // Arrow points bottom-left
                        if ((col - j) == (i - row) && i < row && j > col) {
                            arrowSatisfied++;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    
        return arrowSatisfied;
    }
    
    /*
     * Main method.
     */
    public static void main(String[] args) {

        int[][] shinroGridState = {
            {0, 0, 0, 0, 0, 2, 0, 0},
            {0, 0, 5, 2, 0, 0, 0, 0},
            {0, 0, 5, 0, 0, 3, 0, 0},
            {5, 5, 0, 0, 3, 0, 0, 0},
            {0, 2, 0, 3, 0, 0, 9, 4},
            {0, 0, 6, 0, 4, 0, 0, 0},
            {0, 6, 4, 0, 0, 0, 0, 0},
            {4, 0, 0, 0, 0, 3, 7, 0}
        };
        int[] rowBalls = {3, 1, 1, 3, 1, 2, 0, 1};
        int[] columnBalls = {1, 2, 1, 3, 1, 2, 1, 1};

        ShinroHelper helper = new ShinroHelper(shinroGridState, rowBalls, columnBalls);
        helper.execute(args);
    }
}