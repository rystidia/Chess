package cz.cvut.fel.pjv.chess;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.chess.players.Player;
import cz.cvut.fel.pjv.chess.players.RemotePlayer;
import cz.cvut.fel.pjv.chess.server.GameResult;
import cz.cvut.fel.pjv.chess.server.Packet;
import cz.cvut.fel.pjv.chess.server.Protocol;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class StatsScene {
    private final SceneController sceneController = new SceneController();

    private PrintWriter out;

    private final Styles style = new Styles();

    private final List<GameResult> allGameResults = new ArrayList<>();

    private final ObservableList<GameResult> gameResults = FXCollections.observableArrayList();

    public StatsScene() {
    }
    public GridPane createStatsScene() {
        GridPane root = new GridPane();

        TableView<GameResult> tableView = new TableView<>();

        TableColumn<GameResult, String> whiteCol = new TableColumn<>("White");
        whiteCol.setCellValueFactory(new PropertyValueFactory<>("whiteName"));
        TableColumn<GameResult, String> blackCol = new TableColumn<>("Black");
        blackCol.setCellValueFactory(new PropertyValueFactory<>("blackName"));
        TableColumn<GameResult, String> winnerCol = new TableColumn<>("Winner");
        winnerCol.setCellValueFactory(new PropertyValueFactory<>("winnerName"));
        TableColumn<GameResult, String> totalTimeCol = new TableColumn<>("Total time");
        totalTimeCol.setCellValueFactory(new PropertyValueFactory<>("totalTime"));

        tableView.getColumns().addAll(whiteCol, blackCol, winnerCol, totalTimeCol);

        try (
            Socket socket = new Socket(RemotePlayer.host, RemotePlayer.port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out = new PrintWriter(socket.getOutputStream(), true);
            ObjectMapper objectMapper = new ObjectMapper();
            out.println(objectMapper.writeValueAsString(new Packet(Protocol.STATS_REQUEST.name())));
            while (true) {
                String msg = in.readLine();
                if (msg == null) {
                    break;
                }
                GameResult gameResult = objectMapper.readValue(msg, GameResult.class);
                allGameResults.add(gameResult);
            }
        } catch (ConnectException ex) {
            Alert a = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
            a.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        gameResults.addAll(allGameResults);
        tableView.setItems(gameResults);

        root.add(tableView, 0, 0);

        Label nameLabel = style.newLabel("Filter by name:", 15);
        TextField nameField = new TextField();
        nameField.setOnAction(evt -> filterByName(nameField.getText().strip()));
        Button searchNameBtn = style.newButtonSmall("Search");
        searchNameBtn.setOnAction(evt -> filterByName(nameField.getText().strip()));
        Button longest10Btn = style.newButtonSmall("TOP 10 games");
        longest10Btn.setOnAction(evt -> filterLongest10());

        nameField.setMinWidth(200);
        HBox hbox = new HBox(2, nameField, searchNameBtn);
        hbox.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(3, nameLabel, hbox, longest10Btn);
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.setPadding(new Insets(20));
        root.add(vBox, 1, 0);

        Button menu = style.newButton("Menu");
        menu.setOnAction(sceneController::switchToMenu);
        root.add(menu, 0, 1);

        root.setVgap(20);
        root.setPadding(new Insets(20));
        root.setMinSize(style.minWidth, style.minHeight);

        return root;
    }

    private void filterByName(String name) {
        gameResults.clear();
        if (Objects.equals(name, "")) {
            gameResults.addAll(allGameResults);
            return;
        }
        for (GameResult gameResult : allGameResults) {
            if (Objects.equals(gameResult.getWhiteName(), name) || Objects.equals(gameResult.getBlackName(), name)) {
                gameResults.add(gameResult);
            }
        }
    }

    private void filterLongest10() {
        List<GameResult> temp = new ArrayList<>(allGameResults);
        temp.sort((o1, o2) -> (int) (Player.stringToTime(o2.getTotalTime(), false) - Player.stringToTime(o1.getTotalTime(), false)));
        gameResults.clear();
        gameResults.addAll(temp.subList(0, Math.min(temp.size(), 10)));
    }
}
