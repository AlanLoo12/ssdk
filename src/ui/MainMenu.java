package ui;

import model.Position;
import model.item.Floor;
import model.item.Player;
import ui.gui_elements.FloatField;
import ui.gui_elements.IntegerField;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;

/**
 * A controller class for the main menu
 */
public class MainMenu {
    private static final long MAX_DELAY = 1000;
    @FXML public CheckBox generateOnTheGo;
    @FXML public IntegerField initialMapSize;
    @FXML public CheckBox breedRandomWalkers;
    @FXML public Spinner activeRandomWalkers;
    @FXML public FloatField randomWalkersBirthChance;
    @FXML public FloatField randomWalkersDeathChance;
    @FXML public TextField serverAddress;
    @FXML public IntegerField serverPort;

    @FXML public void handleStartButtonAction(ActionEvent actionEvent) {

        generateStubWorld();

        // Generate
        //worldGenerator.tick(initialMapSize.getValue());

        //WorldPhysics worldPhysics = new WorldPhysics();
        //worldPhysics.start();

        //if (generateOnTheGo.isSelected()) {
        //    worldGenerator.start();
        //}

        Player player = new Player(Position.ORIGIN);
        setUpUI(player);
    }

    private void generateStubWorld() {
        int RADIUS = 20;

        for (int x = -RADIUS; x < RADIUS; x++) {
            for (int y = -RADIUS; y < RADIUS; y++) {
                new Floor(new Position(x, y));
            }
        }
    }

    private void setUpUI(Player player) {
        Group root = new Group();


        Stage stage = new Stage();
        //stage.initModality(Modality.APPLICATION_MODAL);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Super Simple Dungeon K");
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.show();

        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        scene.setFill(Color.BLACK);
        stage.setScene(scene);

        StackPane background = new StackPane();

        WorldRenderer worldRenderer = new WorldRenderer(scene.getWidth(), scene.getHeight(), player);
        background.getChildren().add(worldRenderer.getGroup());
        AnimationTimer timer = new AnimationTimer() {
            private long prev = 0;

            @Override
            public void handle(long l) {
                if (l - prev > MAX_DELAY) {
                    prev = l;
                        worldRenderer.render();
                }
            }
        };
        timer.start();

        InventoryRenderer inventoryRenderer = new InventoryRenderer(player);
        GridPane inventoryGrid = new GridPane();
        inventoryGrid.setAlignment(Pos.TOP_RIGHT);
        inventoryGrid.add(inventoryRenderer.getGroup(), 0, 0);

        StatisticsRenderer statisticsRenderer = new StatisticsRenderer(player);
        GridPane statisticsGrid = new GridPane();
        statisticsGrid.setAlignment(Pos.TOP_LEFT);
        statisticsGrid.add(statisticsRenderer.getGroup(), 0, 0);

        background.getChildren().add(inventoryGrid);
        background.getChildren().add(statisticsGrid);

        scene.setRoot(background);

        KeyHandler keyHandler = new KeyHandler(player, worldRenderer);
        keyHandler.registerScene(scene);
    }
}
