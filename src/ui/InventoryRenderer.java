package ui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Player;

/**
 *
 */
class InventoryRenderer {
    private Player player;
    private Group group;

    InventoryRenderer(Player player) {
        this.player = player;

        group = new Group();

        VBox items = new VBox();
        items.getChildren().add(new Text("Inventory:"));
        items.getChildren().add(new Text("STONE - 30"));
        items.getChildren().add(new Text("DIRT - 40"));
        items.getChildren().add(new Text("> PICK AXE - 6000"));

        group.getChildren().add(items);
    }

    Node getGroup() {
        return group;
    }
}
