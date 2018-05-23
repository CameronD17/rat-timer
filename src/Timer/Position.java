package Timer;

import javafx.scene.input.KeyCode;

public enum Position {
    MIDDLE(KeyCode.M, "Middle"),
    TOP_LIGHT(KeyCode.W, "Top"),
    LEFT_DARK(KeyCode.A, "Left"),
    BOTTOM_LIGHT(KeyCode.S, "Bottom"),
    RIGHT_DARK(KeyCode.D, "Right");

    KeyCode pos;
    String name;

    Position (KeyCode pos, String name) {
        this.pos = pos;
        this.name = name;
    }

    public static Position getPosition (KeyCode input) {
        for (Position type : Position.values()) {
            if (type.pos == input) {
                return type;
            }
        }
        return null;
    }

    public String getName () {
        return name;
    }
}
