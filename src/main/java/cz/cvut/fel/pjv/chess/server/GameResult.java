package cz.cvut.fel.pjv.chess.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import cz.cvut.fel.pjv.chess.MyColor;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class GameResult {
    private String totalTime;
    private String whiteName;
    private String blackName;
    private String winnerName;

    public GameResult() {
    }

    public GameResult(String totalTime, String whiteName, String blackName, String winnerName) {
        this.totalTime = totalTime;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.winnerName = winnerName;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public void setWhiteName(String whiteName) {
        this.whiteName = whiteName;
    }

    public void setBlackName(String blackName) {
        this.blackName = blackName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }
}
