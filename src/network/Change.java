package network;

import model.Item;
import model.Position;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a single change to the world (add or remove)
 */
public final class Change {
    public Type getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public Item getItem() {
        return item;
    }

    public enum Type {
        ADD, REMOVE
    }
    private Type type;
    private Position position;
    private Item item;

    private Change(Type type, Position position, Item item) {
        this.type = type;
        this.position = position;
        this.item = item;
    }

    /**
     * Produce a change that adds item to the position
     * @param position position to which to add item
     * @param item item to add
     * @return change that adds item to the position
     */
    public static Change getAddChange(Position position, Item item) {
        return new Change(Type.ADD, position, item);
    }

    /**
     * Produce a change that removes item from the position
     * @param position position from which to remove item
     * @param item item to remove
     * @return change that removes item from the position
     */
    public static Change getRemoveChange(Position position, Item item) {
        return new Change(Type.REMOVE, position, item);
    }

    /**
     * Produce the change from the string serialization
     * @param string serialized version of the change
     * @return change corresponding to the string serialization
     * @throws IllegalArgumentException if given string does not correspond to
     * any valid change
     */
    static Change valueOf(@NotNull String string) throws IllegalArgumentException {
        if (!string.matches("(ADD|REMOVE) -?\\d+ -?\\d+ " + Item.getValidNamesRegex())) {
            throw new IllegalArgumentException(string);
        }

        String[] words = string.split(" ");

        Type type;
        if (words[0].equals("ADD")) {
            type = Type.ADD;
        } else {
            type = Type.REMOVE;
        }

        int x = Integer.parseInt(words[1]);
        int y = Integer.parseInt(words[2]);

        Item item = Item.valueOf(words[3]);

        return new Change(type, new Position(x, y), item);
    }

    /**
     * Produce the string encoding this change
     *
     * <p>
     *     String is of the form:
     *     (ADD|REMOVE) position item
     * </p>
     *
     * @return string encoding this change
     */
    @Override
    public String toString() {
        return type + " " + position + " " + item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Change change = (Change) o;

        return type == change.type && position.equals(change.position) && item == change.item;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + item.hashCode();
        return result;
    }
}
