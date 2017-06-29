package gui_elements;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


/**
 *
 */
public class DoubleField extends TextField {
    public DoubleField() {
        this.addEventFilter(KeyEvent.KEY_TYPED, t -> {
            char ar[] = t.getCharacter().toCharArray();
            char ch = ar[t.getCharacter().toCharArray().length - 1];
            if (!((ch >= '0' && ch <= '9') || ch == '.')) {
                t.consume();
            }
        });
    }
}
