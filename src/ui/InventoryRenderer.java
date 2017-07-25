package ui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.item.Item;
import model.item.Player;

import java.util.Observable;
import java.util.Observer;

import static javafx.scene.paint.Color.WHITE;

/**
 *
 */
class InventoryRenderer implements Observer {
    private Player player;
    private Group group;
    private VBox items;

    InventoryRenderer(Player player) {
        this.player = player;

        group = new Group();

        //group.getStylesheets().add("ui/inventory.css");

        VBox inventory = new VBox();
        items = new VBox();
        Text title = new Text("Inventory:");
        title.setFill(WHITE);
        inventory.getChildren().add(title);
        inventory.getChildren().add(items);

        player.getInventory().addObserver(this);
        player.addObserver(this);
        updateItems(player);

        group.getChildren().add(inventory);
    }

    private void updateItems(Player player) {
        items.getChildren().clear();

        for (Item item : player.getInventory().keySet()) {
            int quantity = player.getInventory().get(item);

            String string;
            if (player.getInventory().getPosition(item) == player.getSelectedItem()) {
                string = "> " + item.toString() + " - " + quantity;
            } else {
                string = item.toString() + " - " + quantity;
            }

            Text text = new Text(string);
            text.setFill(WHITE);

            items.getChildren().add(text);
        }
    }

    Node getGroup() {
        return group;
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
        updateItems(player);
    }
}
