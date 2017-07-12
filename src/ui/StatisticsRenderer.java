package ui;

import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.WorldManager;

import java.util.Observable;
import java.util.Observer;

import static javafx.scene.paint.Color.WHITE;

/**
 *
 */
public class StatisticsRenderer implements Observer {
    private WorldManager world;
    private Group group;
    private VBox data;

    StatisticsRenderer(WorldManager world) {
        this.world = world;

        group = new Group();

        VBox statistics = new VBox();
        data = new VBox();
        Text title = new Text("Statistics:");
        title.setFill(WHITE);
        statistics.getChildren().add(title);
        statistics.getChildren().add(data);

        world.addObserver(this);
        updateStatistics(world);

        group.getChildren().add(statistics);
    }

    private void updateStatistics(WorldManager world) {
        data.getChildren().clear();

        Text worldSize = new Text("WorldManager size: " + world.size());
        worldSize.setFill(WHITE);
        data.getChildren().add(worldSize);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        updateStatistics(world);
    }

    Group getGroup() {
        return group;
    }
}
