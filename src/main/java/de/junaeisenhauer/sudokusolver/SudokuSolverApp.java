package de.junaeisenhauer.sudokusolver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main application window of the program.
 */
public class SudokuSolverApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        Font.loadFont(getClass().getClassLoader().getResourceAsStream("font/Roboto-Regular.ttf"), 10);
        Font.loadFont(getClass().getClassLoader().getResourceAsStream("font/RobotoCondensed-Regular.ttf"), 10);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // load localization
        ResourceBundle resourceBundle = ResourceBundle.getBundle("localization/SudokuSolver");

        // load fxml file
        URL sudokuSolverResource = getClass().getClassLoader().getResource("view/SudokuSolver.fxml");
        FXMLLoader loader = new FXMLLoader(sudokuSolverResource, resourceBundle);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();

        // setup stage
        stage.getIcons().add(new Image("image/app_icon.png"));
        stage.setTitle(resourceBundle.getString("app.title"));
        stage.show();
    }

}
