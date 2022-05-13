package cz.cvut.fel.pjv.chess.server;

public enum Protocol {
    // server origin:
    GAME_START,
    REJECTED,
    // client origin:
    MATCHMAKE_REQUEST,
    MOVE,
    GAME_END,
    DRAW_OFFER,
    RESPONSE_TO_OFFER,
    SURRENDER
}
