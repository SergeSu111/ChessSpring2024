package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMovesCalculator {
    public default Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {

        return null;
    }
}
