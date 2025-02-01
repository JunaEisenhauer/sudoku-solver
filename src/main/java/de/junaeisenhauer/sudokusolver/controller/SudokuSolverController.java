package de.junaeisenhauer.sudokusolver.controller;

import de.junaeisenhauer.sudokusolver.algorithm.SudokuSolver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for the sudoku solver which handles user inputs, accesses the sudoku solve algorithm and updates
 * the view.
 */
public class SudokuSolverController {

    @FXML
    private Button solve;
    @FXML
    private Button reset;
    @FXML
    private ProgressIndicator progress;

    @FXML
    private GridPane grid;

    private int width;
    private int sqrtWidth;
    private int height;
    private int sqrtHeight;

    private GridPane[][] subGrids;

    private FieldController[][] fieldController;

    private SudokuSolver solver;

    @FXML
    private void initialize() {
        solver = new SudokuSolver();

        width = 9;
        sqrtWidth = (int) Math.sqrt(width);
        height = 9;
        sqrtHeight = (int) Math.sqrt(height);

        subGrids = new GridPane[sqrtWidth][sqrtHeight];
        fieldController = new FieldController[width][height];
        initializeGrid();
    }

    @FXML
    public void onSolve(ActionEvent event) {
        lockGrid();
        solve.setDisable(true);
        reset.setDisable(true);
        new Thread(() -> {
            int[][] grid = getValueGrid();

            boolean hasError = false;
            checkForErrors:
            for (int x = 0; x < grid.length; x++) {
                for (int y = 0; y < grid[x].length; y++) {
                    if (grid[x][y] != 0 && !solver.checkField(x, y, grid)) {
                        hasError = true;
                        break checkForErrors;
                    }
                }
            }

            if (!hasError && solver.solve(grid)) {
                setValueGrid(grid);
            } else {
                openUnsolvableAlert();
            }

            unlockGrid();
            solve.setDisable(false);
            reset.setDisable(false);
        }).start();
    }

    @FXML
    public void onReset(ActionEvent event) {
        for (int x = 0; x < fieldController.length; x++) {
            for (int y = 0; y < fieldController[x].length; y++) {
                fieldController[x][y].setValue(0);
            }
        }
    }

    private void initializeGrid() {
        for (int x = 0; x < subGrids.length; x++) {
            for (int y = 0; y < subGrids[x].length; y++) {
                GridPane subGrid = new GridPane();
                subGrid.getStyleClass().add("subGrid");
                grid.add(subGrid, x, y);
                subGrids[x][y] = subGrid;
            }
        }

        for (int x = 0; x < fieldController.length; x++) {
            for (int y = 0; y < fieldController[x].length; y++) {
                initializeField(x, y);
            }
        }
    }

    private void initializeField(int x, int y) {
        // load localization
        ResourceBundle resourceBundle = ResourceBundle.getBundle("localization/SudokuSolver");

        // load fxml file
        URL tileResource = getClass().getClassLoader().getResource("view/Field.fxml");
        FXMLLoader loader = new FXMLLoader(tileResource, resourceBundle);
        try {
            Parent tile = loader.load();
            FieldController controller = loader.getController();
            fieldController[x][y] = controller;
            controller.setValue(0);
            subGrids[x / sqrtWidth][y / sqrtHeight].add(tile, x % sqrtWidth, y % sqrtHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[][] getValueGrid() {
        int[][] grid = new int[width][height];
        for (int x = 0; x < fieldController.length; x++) {
            for (int y = 0; y < fieldController[x].length; y++) {
                grid[x][y] = fieldController[x][y].getValue();
            }
        }
        return grid;
    }

    private void setValueGrid(int[][] grid) {
        for (int x = 0; x < fieldController.length; x++) {
            for (int y = 0; y < fieldController[x].length; y++) {
                fieldController[x][y].setValue(grid[x][y]);
            }
        }
    }

    private void lockGrid() {
        for (int x = 0; x < fieldController.length; x++) {
            for (int y = 0; y < fieldController[x].length; y++) {
                fieldController[x][y].lock();
            }
        }
    }

    private void unlockGrid() {
        for (int x = 0; x < fieldController.length; x++) {
            for (int y = 0; y < fieldController[x].length; y++) {
                fieldController[x][y].unlock();
            }
        }
    }

    private void openUnsolvableAlert() {
        Platform.runLater(() -> {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("localization/SudokuSolver");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("sudoku.unsolvable.title"));
            alert.setHeaderText(resourceBundle.getString("sudoku.unsolvable.header"));
            alert.setContentText(resourceBundle.getString("sudoku.unsolvable.content"));
            alert.showAndWait();
        });
    }

}
