package ui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Player;
import model.Item;

import java.util.Observable;
import java.util.Observer;

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

        VBox inventory = new VBox();
        items = new VBox();
        inventory.getChildren().add(new Text("Inventory:"));
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
            if (player.getInventory().getPosition(item) == player.getSelectedItem()) {
                items.getChildren().add(new Text("> " + item.toString() + " - " + quantity));
            } else {
                items.getChildren().add(new Text(item.toString() + " - " + quantity));
            }
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
