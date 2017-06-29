package gui_elements;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

/**
 * Thanks to https://stackoverflow.com/questions/8381374/how-to-implement-a-numberfield-in-javafx-2-0#16554408
 */
public class IntegerField extends TextField {
    public IntegerField() {
        this.addEventFilter(KeyEvent.KEY_TYPED, t -> {
            char ar[] = t.getCharacter().toCharArray();
            char ch = ar[t.getCharacter().toCharArray().length - 1];
            if (!(ch >= '0' && ch <= '9')) {
                t.consume();
            }
        });
    }

    public int getValue() {
        return Integer.parseInt(getText());
    }
}
