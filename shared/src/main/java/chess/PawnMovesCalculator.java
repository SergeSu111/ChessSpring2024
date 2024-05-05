package chess;

import java.util.ArrayList;
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
        ArrayList<ChessMove> pawnMoves = new ArrayList<>();
        // make sure color and make sure going down or going up
        int startRow = startPosition.getRow();
        int startColumn = startPosition.getColumn();
        int nextRow;

        // make sure if its empty in front of the pawn
        boolean empty = false;

        // set setup row and column
        int setUpRow;

        // means going up.
        if (thePiece.getTeamColor() == ChessGame.TeamColor.WHITE)
        {
            setUpRow = 2;
            nextRow = startRow + 1;
        }
        // else is Black
        setUpRow = 7;
        nextRow = startRow - 1;



    }


    // create rowInBound
    public boolean rowInBound(int row)
    {
        return row >= 1 && row <= 8;
    }
}
