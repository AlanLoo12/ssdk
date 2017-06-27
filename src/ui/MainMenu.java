package ui;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Player;
import model.World;
import model.WorldGenerator;

import java.io.IOException;

/**
 * A controller class for the main menu
 */
public class MainMenu {
    @FXML public CheckBox generate;
    @FXML public Spinner mapSize;
    @FXML public ListView gameMode;

    @FXML public void handleStartButtonAction(ActionEvent actionEvent) {
        Label selectedItem = (Label) gameMode.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem.getId());

        int initialMapSize = (int) mapSize.getValue();

        World world = new World();

        WorldGenerator worldGenerator = new WorldGenerator(world);

        for (int i = 0; i < initialMapSize; i++) {
            worldGenerator.tick();
        }

        if (generate.isSelected()) {
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    worldGenerator.tick();
                }
            };
            timer.start();
        }

        Group root = new Group();
        Player player = new Player(world);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setTitle("Super Simple Dungeon K");
        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        Point2D center = new Point2D(scene.getWidth() / 2, scene.getHeight() / 2);
        WorldRenderer worldRenderer = new WorldRenderer(world, center, player);
        root.getChildren().add(worldRenderer.getGroup());

        KeyHandler keyHandler = new KeyHandler(player, worldRenderer);
        keyHandler.registerScene(scene);

        stage.show();
    }
}
