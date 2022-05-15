package cz.cvut.fel.pjv.chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class MainFrame extends Application {

    private final Styles style = new Styles();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        final MenuScene ms = new MenuScene();
        Scene scene = new Scene(ms.createMenuScene());

        primaryStage.getIcons().add(new Image("/logo.png"));
        primaryStage.setMinWidth(style.minWidth);
        primaryStage.setMinHeight(style.minHeight + 40);
        primaryStage.setTitle("Chess");
        primaryStage.setOnCloseRequest(evt -> System.exit(1));
        primaryStage.setScene(scene); // Place in scene in the stage
        primaryStage.show();
    }
}
