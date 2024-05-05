package chess;

import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator
{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        ChessPiece thePiece = board.getPiece(myPosition);
        return pawnMoves(board, thePiece, myPosition);
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPiece thePiece, ChessPosition startPosition)
    {
        
    }
}
