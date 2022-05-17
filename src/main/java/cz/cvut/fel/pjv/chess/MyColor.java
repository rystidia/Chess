package cz.cvut.fel.pjv.chess;

public enum MyColor {
    WHITE, BLACK;

    /**
     * Returns color opposite to the given color.
     */
    public static MyColor getOppositeColor(MyColor color) {
        return color == MyColor.WHITE ? MyColor.BLACK : MyColor.WHITE;
    }
}
