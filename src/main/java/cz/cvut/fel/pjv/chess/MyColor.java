package cz.cvut.fel.pjv.chess;

public enum MyColor {
    WHITE, BLACK;

    public static MyColor getOppositeColor(MyColor color){
        return color == MyColor.WHITE ? MyColor.BLACK : MyColor.WHITE;
    }
}
