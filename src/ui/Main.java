package ui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Player;
import model.World;
import model.WorldGenerator;

import static com.sun.javafx.scene.traversal.Direction.*;

public class Main extends Application {
    private static final World WORLD = new World();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Player player = new Player(WORLD);

        primaryStage.setTitle("Super Simple Dungeon K");
        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        Point2D center = new Point2D(primaryStage.getWidth() / 2, primaryStage.getHeight() / 2);
        WorldRenderer worldRenderer = new WorldRenderer(WORLD, center, player);
        root.getChildren().add(worldRenderer.getGroup());

        KeyHandler keyHandler = new KeyHandler(player, worldRenderer);
        keyHandler.registerScene(scene);

        WorldGenerator worldGenerator = new WorldGenerator(WORLD);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                worldGenerator.tick();
            }
        };
        timer.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
