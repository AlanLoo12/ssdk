package ui;

import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.World;

import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class StatisticsRenderer implements Observer {
    private World world;
    private Group group;
    private VBox data;

    StatisticsRenderer(World world) {
        this.world = world;

        group = new Group();

        VBox statistics = new VBox();
        data = new VBox();
        statistics.getChildren().add(new Text("Statistics:"));
        statistics.getChildren().add(data);

        world.addObserver(this);
        updateStatistics(world);

        group.getChildren().add(statistics);
    }

    private void updateStatistics(World world) {
        data.getChildren().clear();

        data.getChildren().add(new Text("World size: " + world.size()));
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
