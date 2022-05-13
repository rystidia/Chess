package cz.cvut.fel.pjv.chess.server;

import cz.cvut.fel.pjv.chess.MyColor;

public class Packet {
    private String type;
    private String name;
    private String opponentName;
    private MyColor color;
    private String from;
    private String to;
    private Character promotionFigure;
    private MyColor winnerColor;
    private Boolean drawAccepted;

    public Packet() {
    }

    public Packet(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public MyColor getColor() {
        return color;
    }

    public void setColor(MyColor color) {
        this.color = color;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Character getPromotionFigure() {
        return promotionFigure;
    }

    public void setPromotionFigure(Character promotionFigure) {
        this.promotionFigure = promotionFigure;
    }

    public MyColor getWinnerColor() {
        return winnerColor;
    }

    public void setWinnerColor(MyColor winnerColor) {
        this.winnerColor = winnerColor;
    }

    public boolean getDrawAccepted() {
        return drawAccepted;
    }

    public void setDrawAccepted(boolean drawAccepted) {
        this.drawAccepted = drawAccepted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
