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
import model.Position;
import model.World;
import model.WorldGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static model.Entity.COIN;
import static model.Entity.EXIT;

/**
 * A controller class for the main menu
 */
public class MainMenu {
    private static final Random RANDOM = new Random();
    private static final float COIN_DENSITY = 0.01f;
    private static final int DEFAULT_MAP_SIZE = 30000;
    @FXML public CheckBox generate;
    @FXML public TextField mapSize;
    @FXML public ListView gameMode;

    @FXML public void handleStartButtonAction(ActionEvent actionEvent) {
        String selectedItem;
        try {
            selectedItem = ((Label) gameMode.getSelectionModel().getSelectedItem()).getId();
        } catch (Exception e) {
            selectedItem = "exit";
        }

        World world = new World();
        WorldGenerator worldGenerator = new WorldGenerator(world);
        int initialMapSize = DEFAULT_MAP_SIZE;
        try {
            initialMapSize = Integer.parseInt(mapSize.getText());
        } catch (NumberFormatException ignored) {}

        for (int i = 0; i < initialMapSize; i++) {
            worldGenerator.tick();
        }

        if (selectedItem.equals("exit")) {
            List<Position> positionList = new ArrayList<>();
            positionList.addAll(world.getWalkablePositions());

            // Generate the coins
            //worldGenerator.generateRandomly(COIN, COIN_DENSITY);

            for (int i = 0; i < COIN_DENSITY * positionList.size(); i++) {
                Position position = positionList.get(RANDOM.nextInt(positionList.size()));

                world.put(position, COIN);
            }

            // Generate the exit
            Position exit = positionList.get(RANDOM.nextInt(positionList.size()));

            world.put(exit, EXIT);

        } else {
            if (generate.isSelected()) {
                AnimationTimer timer = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        worldGenerator.tick();
                    }
                };
                timer.start();
            }
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
