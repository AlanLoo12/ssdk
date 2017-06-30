package ui;

import gui_elements.FloatField;
import gui_elements.IntegerField;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Player;
import model.World;
import model.WorldGenerator;

import static model.Entity.COIN;
import static model.Entity.EXIT;

/**
 * A controller class for the main menu
 */
public class MainMenu {
    private static final float COIN_DENSITY = 0.01f;

    @FXML public CheckBox generateOnTheGo;
    @FXML public IntegerField initialMapSize;
    @FXML public CheckBox breedRandomWalkers;
    @FXML public Spinner activeRandomWalkers;
    @FXML public CheckBox generateExit;
    @FXML public CheckBox generateDust;
    @FXML public CheckBox generateWalls;
    @FXML public FloatField randomWalkersBirthChance;
    @FXML public FloatField randomWalkersDeathChance;
    @FXML public CheckBox generateCoins;

    @FXML public void handleStartButtonAction(ActionEvent actionEvent) {
        World world = World.getInstance();
        WorldGenerator worldGenerator = new WorldGenerator(world);
        // Configure the generator
        if (generateCoins.isSelected()) {
            worldGenerator.generateRandomly(COIN, COIN_DENSITY);
        }
        worldGenerator.setGenerateDust(generateDust.isSelected());
        worldGenerator.setGenerateWalls(generateWalls.isSelected());
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
        WorldRenderer worldRenderer = new WorldRenderer(world, scene.getWidth(), scene.getHeight(), player);
        root.getChildren().add(worldRenderer.getGroup());

        KeyHandler keyHandler = new KeyHandler(player, worldRenderer);
        keyHandler.registerScene(scene);

        stage.show();
    }
}
