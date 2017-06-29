package ui;

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
    @FXML public TextField initialMapSize;
    public CheckBox breedRandomWalkers;
    public Spinner activeRandomWalkers;
    public CheckBox generateExit;
    public CheckBox generateDust;
    public CheckBox generateWalls;
    public IntegerField randomWalkersBirthChance;
    public IntegerField randomWalkersDeathChance;

    @FXML public void handleStartButtonAction(ActionEvent actionEvent) {
        World world = new World();
        WorldGenerator worldGenerator = new WorldGenerator(world);
        int initialMapSize = Integer.parseInt(this.initialMapSize.getText());

        // Configure the generator
        worldGenerator.generateRandomly(COIN, COIN_DENSITY);
        worldGenerator.setGenerateDust(generateDust.isSelected());
        worldGenerator.setGenerateWalls(generateWalls.isSelected());
        worldGenerator.setBreedRandomWalkers(breedRandomWalkers.isSelected());
        worldGenerator.setRandomWalkersBirthChance(Float.parseFloat(randomWalkersBirthChance.getText()));
        worldGenerator.setRandomWalkersDeathChance(Float.parseFloat(randomWalkersDeathChance.getText()));
        worldGenerator.setRandomWalkersToTick((Integer) activeRandomWalkers.getValue());

        // Generate
        worldGenerator.tick(initialMapSize);
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
