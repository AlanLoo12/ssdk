package ui;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Player;
import model.World;

import static com.sun.javafx.scene.traversal.Direction.*;

public class Main extends Application {
    public static final World WORLD = new World();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Player player = new Player(WORLD);

        primaryStage.setTitle("Super Simple Dungeon K");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        Point2D center = new Point2D(primaryStage.getWidth() / 2, primaryStage.getHeight() / 2);
        WorldRenderer worldRenderer = new WorldRenderer(WORLD, center, player);
        root.getChildren().add(worldRenderer.getGroup());

        KeyHandler keyHandler = new KeyHandler(player);
        keyHandler.registerScene(scene);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
