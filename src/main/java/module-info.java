module cz.cvut.fel.pjv.chess {
    requires javafx.controls;
    requires javafx.fxml;

    opens cz.cvut.fel.pjv.chess to javafx.fxml;
    exports cz.cvut.fel.pjv.chess;
}
