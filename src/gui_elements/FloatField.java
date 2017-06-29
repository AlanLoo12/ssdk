package gui_elements;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


/**
 *
 */
public class FloatField extends TextField {
    public FloatField() {
        this.addEventFilter(KeyEvent.KEY_TYPED, t -> {
            char ar[] = t.getCharacter().toCharArray();
            char ch = ar[t.getCharacter().toCharArray().length - 1];
            if (!((ch >= '0' && ch <= '9') || (ch == '.' & !getText().contains(".")))) {
                t.consume();
            }
        });
    }

    public float getValue() {
        return Float.parseFloat(getText());
    }
}
