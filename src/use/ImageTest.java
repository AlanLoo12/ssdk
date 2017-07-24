package use;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.Paths;

public class ImageTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(500, 500);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        //Image image = new Image("https://opengameart.org/sites/default/files/styles/medium/public/potion_all_2.png");
        //Image image = new Image("assets/tiles/dc-dngn/wall/brick_brown0.png");
        //System.out.println(Paths.get("assets", "tiles", "dc-dngn", "wall", "brick_brown0.png").toUri().toString());
        Image image = new Image(Paths.get("assets", "tiles", "dc-dngn", "wall", "brick_brown0.png").toUri().toString());
        gc.drawImage(image,0,0);

        primaryStage.show();
        }
        }
