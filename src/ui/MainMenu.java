package ui;

import gui_elements.FloatField;
import gui_elements.IntegerField;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Player;
import model.WorldManager;
import model.WorldGenerator;

import java.io.IOException;
import java.net.InetAddress;

import static model.Item.*;

/**
 * A controller class for the main menu
 */
public class MainMenu {
    @FXML public CheckBox generateOnTheGo;
    @FXML public IntegerField initialMapSize;
    @FXML public CheckBox breedRandomWalkers;
    @FXML public Spinner activeRandomWalkers;
    @FXML public CheckBox generateExit;
    @FXML public FloatField randomWalkersBirthChance;
    @FXML public FloatField randomWalkersDeathChance;

    @FXML public void handleStartButtonAction(ActionEvent actionEvent) {
        WorldManager world = WorldManager.getInstance();
        WorldGenerator worldGenerator = world.getGenerator();

        // Configure the generator
        worldGenerator.generateRandomly(PLANT, 0.001f);
        worldGenerator.setBreedRandomWalkers(breedRandomWalkers.isSelected());
        worldGenerator.setRandomWalkersBirthChance(randomWalkersBirthChance.getValue());
        worldGenerator.setRandomWalkersDeathChance(randomWalkersDeathChance.getValue());
        worldGenerator.setRandomWalkersToTick((Integer) activeRandomWalkers.getValue());

        // Generate
        worldGenerator.tick(initialMapSize.getValue());
        if (generateExit.isSelected()) {
            worldGenerator.putAtTheWalkerTip(EXIT);
        }

        if (generateOnTheGo.isSelected()) {
            AnimationTimer timer = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        worldGenerator.tick();
                    }
                };
            timer.start();
        }

        Player player = new Player();
        setUpUI(player);
    }

    public void handleConnectButtonAction(ActionEvent actionEvent) {
        try {
            WorldManager.getInstance().connect(InetAddress.getLocalHost());
        } catch (IOException e) {
            System.out.println("Connection failed");
            e.printStackTrace();
            System.exit(1);
        }

        Player player = new Player();
        setUpUI(player);
    }

    private void setUpUI(Player player) {
        Group root = new Group();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Super Simple Dungeon K");
        stage.setMaximized(true);
        stage.show();

        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());
        scene.setFill(Color.BLACK);
        stage.setScene(scene);

        StackPane background = new StackPane();

        WorldRenderer worldRenderer = new WorldRenderer(scene.getWidth(), scene.getHeight(), player);
        background.getChildren().add(worldRenderer.getGroup());

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
