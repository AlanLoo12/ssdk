package ui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.InventoryItem;
import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
class InventoryRenderer implements Observer {
    private static final int MAX_VISIBLE_ITEMS_AT_A_TIME = 5;

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

        int position = 0;
        for (InventoryItem inventoryItem : player.getInventory().keySet()) {
            int quantity = player.getInventory().get(inventoryItem);
            if (position == player.getSelectedInventoryItem()) {
                items.getChildren().add(new Text("> " + inventoryItem.toString() + " - " + quantity));
            } else {
                items.getChildren().add(new Text(inventoryItem.toString() + " - " + quantity));
            }
            position++;
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
