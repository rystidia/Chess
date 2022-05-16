package cz.cvut.fel.pjv.chess.server;

import cz.cvut.fel.pjv.chess.MyColor;

public class GameResult {
    private final long totalTime;
    private final String whiteName;
    private final String blackName;
    private final MyColor winnerColor;

    public GameResult(long totalTime, String whiteName, String blackName, MyColor winnerColor) {
        this.totalTime = totalTime;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.winnerColor = winnerColor;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public MyColor getWinner() {
        return winnerColor;
    }
}
