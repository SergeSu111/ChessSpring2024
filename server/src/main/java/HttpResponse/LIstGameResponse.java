package HttpResponse;

import Model.GameData;
import chess.ChessGame;

import java.util.ArrayList;

public record LIstGameResponse(ArrayList<GameData> games) {
}
