/* Author : Ambre Journot */

import org.chocosolver.solver.Model;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.List;

public class ShinroSolver extends AbstractProblem {

    protected int[][] shinroGridState;
    protected int[] rowBalls;
    protected int[] columnBalls;
    protected BoolVar[][] grid;

    /**
     * Constructor
     *
     * @param shinroGridState initial state of the grid
     * @param rowBalls   number of balls per row
     * @param columnBalls number of balls per column
     */
    public ShinroSolver(int[][] shinroGridState, int[] rowBalls, int[] columnBalls) {
        this.shinroGridState = shinroGridState;
        this.rowBalls = rowBalls;
        this.columnBalls = columnBalls;
    }

    @Override
    public void buildModel() {
        int gridSize = shinroGridState.length;
        model = new Model("Shinro puzzle");
        grid = new BoolVar[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = model.boolVar("cell_" + i + "_" + j);
            }
        }

        for (int i = 0; i < gridSize; i++) {
            BoolVar[] rowCells = new BoolVar[gridSize];
            BoolVar[] columnCells = new BoolVar[gridSize];
            for (int j = 0; j < gridSize; j++) {
                rowCells[j] = grid[i][j];
                columnCells[j] = grid[j][i];
                if (shinroGridState[i][j] > 1) {
                    model.arithm(grid[i][j], "=", 0).post();
                }
            }
            model.sum(rowCells, "=", rowBalls[i]).post();
            model.sum(columnCells, "=", columnBalls[i]).post();
        }

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                List<BoolVar> cells = new ArrayList<>();
                switch(shinroGridState[i][j]) {
                    case 2: // Arrow points right
                        for (int k = 1; k < gridSize; k++) {
                            if (j+k < gridSize) {
                                cells.add(grid[i][j+k]);
                            }
                        }
                        break;
                    case 3: // Arrow points left
                        for (int k = 1; k < gridSize; k++) {
                            if (j-k >= 0) {
                                cells.add(grid[i][j-k]);
                            }
                        }
                        break;
                    case 4: // Arrow points up
                        for (int k = 1; k < gridSize; k++) {
                            if (i-k >= 0) {
                                cells.add(grid[i-k][j]);
                            }
                        }
                        break;
                    case 5: // Arrow points down
                        for (int k = 1; k < gridSize; k++) {
                            if (i+k < gridSize) {
                                cells.add(grid[i+k][j]);
                            }
                        }
                        break;
                    case 6: // Arrow points top-right
                        for (int k = 1; k < gridSize; k++) {
                            if (i-k >= 0 && j+k < gridSize) {
                                cells.add(grid[i-k][j+k]);
                            }
                        }
                        break;
                    case 7: // Arrow points top-left
                        for (int k = 1; k < gridSize; k++) {
                            if (i-k >= 0 && j-k >= 0) {
                                cells.add(grid[i-k][j-k]);
                            }
                        }
                        break;
                    case 8: // Arrow points bottom-right
                        for (int k = 1; k < gridSize; k++) {
                            if (i+k < gridSize && j+k < gridSize) {
                                cells.add(grid[i+k][j+k]);
                            }
                        }
                        break;
                    case 9: // Arrow points bottom-left
                        for (int k = 1; k < gridSize; k++) {
                            if (i+k < gridSize && j-k >= 0) {
                                cells.add(grid[i+k][j-k]);
                            }
                        }  
                        break;
                    default:
                        break;
                }
                if (!cells.isEmpty()) {
                    model.sum(cells.toArray(new BoolVar[cells.size()]), ">=", 1).post();
                }
            }
        }
    }

    @Override
    public void configureSearch() {
        //model.getSolver().setSearch(Search.inputOrderLBSearch(flatten(grid)));
        model.getSolver().setSearch(Search.minDomLBSearch(flatten(grid)));
        //model.getSolver().setSearch(Search.defaultSearch(model));
    }

    @Override
    public void solve() {
        model.getSolver().showStatistics();
        model.getSolver().showSolutions();
        System.out.println();
        while (model.getSolver().solve()) {
            System.out.println("---SOLUTION---");
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    System.out.print(grid[i][j].getValue() + " ");
                }
                System.out.println();
            }
            System.out.println("---------------");
        }

    }

    /**
     * @param vars 2D array of variables
     * @return 1D array of variables
     */
    protected IntVar[] flatten(BoolVar[][] vars) {
        IntVar[] flat = new IntVar[vars.length * vars[0].length];
        for (int i = 0; i < vars.length; i++) {
            for (int j = 0; j < vars[i].length; j++) {
                flat[i * vars[i].length + j] = vars[i][j];
            }
        }
        return flat;
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
            {0, 2, 1, 3, 0, 0, 9, 4},
            {0, 0, 6, 0, 4, 0, 0, 0},
            {0, 6, 4, 0, 0, 0, 0, 0},
            {4, 0, 0, 0, 0, 3, 7, 0}
        };
        int[] rowBalls = {3, 1, 1, 3, 1, 2, 0, 1};
        int[] columnBalls = {1, 2, 1, 3, 1, 2, 1, 1};
        
        ShinroSolver solver = new ShinroSolver(shinroGridState, rowBalls, columnBalls);
        solver.execute(args);
    }
}