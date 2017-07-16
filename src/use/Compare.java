package use;

import model.Position;

/**
 *
 */
public class Compare {

    public static void main(String[] args) {
        //System.out.println(new Position(0,0).compareTo(new Position(0, 1)));
        Position positionA = new Position(3,0);
        Position positionB = new Position(1,2);

        System.out.println(positionA.compareTo(positionB));
        System.out.println(positionB.compareTo(positionA));
    }
}
