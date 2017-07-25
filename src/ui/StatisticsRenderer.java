package ui;

import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.item.Player;

import java.util.Observable;
import java.util.Observer;

import static javafx.scene.paint.Color.WHITE;

/**
 *
 */
public class StatisticsRenderer implements Observer {
    private Player player;
    private Group group;
    private VBox data;

    StatisticsRenderer(Player player) {
        this.player = player;

        group = new Group();

        VBox statistics = new VBox();
        data = new VBox();
        Text title = new Text("Statistics:");
        title.setFill(WHITE);
        statistics.getChildren().add(title);
        statistics.getChildren().add(data);

        player.addObserver(this);
        updateStatistics();

        group.getChildren().add(statistics);
    }

    private void updateStatistics() {
        data.getChildren().clear();

        Text time = new Text("Time: " + player.getTime());
        time.setFill(WHITE);
        data.getChildren().add(time);

        Text moves = new Text("Moves: " + player.getMoves());
        moves.setFill(WHITE);
        data.getChildren().add(moves);
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
        updateStatistics();
    }

    Group getGroup() {
        return group;
    }
}
