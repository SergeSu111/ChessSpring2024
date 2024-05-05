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

        // if it is in bound for nextRow
        if (rowInBound(nextRow))
        {
            ChessPiece nextPiece = board.getPiece(new ChessPosition(nextRow, startColumn)); // get the nextPiece
            if (nextPiece == null) // if nextPiece is null
            {
                empty = true; // means no pieces in front of me
                if (nextRow == 1 || nextRow == 8)  // means need to promote
                {
                    ChessMove smallMove1 = new ChessMove(startPosition, new ChessPosition(nextRow, startColumn), ChessPiece.PieceType.ROOK);
                    pawnMoves.add(smallMove1);
                    ChessMove smallMove2 = new ChessMove(startPosition, new ChessPosition(nextRow, startColumn), ChessPiece.PieceType.BISHOP);
                    pawnMoves.add(smallMove2);
                    ChessMove smallMove3 = new ChessMove(startPosition, new ChessPosition(nextRow, startColumn), ChessPiece.PieceType.KNIGHT);
                    pawnMoves.add(smallMove3);
                    ChessMove smallMove4 = new ChessMove(startPosition, new ChessPosition(nextRow, startColumn), ChessPiece.PieceType.QUEEN);
                    pawnMoves.add(smallMove4);
                }
                // else just regular moving without promotion
                ChessMove smallMove = new ChessMove(startPosition, new ChessPosition(nextRow, startColumn), null);
                pawnMoves.add(smallMove);
            }
        }

        // if the startRow is just the setupRow
        if (startRow == setUpRow)
        {
            // we can go ahead 2
            if (thePiece.getTeamColor() == ChessGame.TeamColor.WHITE)
            {
                nextRow += 1;
                ChessPosition nextPosition = new ChessPosition(nextRow, startColumn);
                ChessPiece nextPiece = board.getPiece(nextPosition);
                if (nextPiece == null)
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                    pawnMoves.add(smallMove);
                }

            }
            nextRow -= 1; // black -= 1 based on nextRow
            ChessPosition nextPosition = new ChessPosition(nextRow, startColumn);
            ChessPiece nextPiece = board.getPiece(nextPosition);
            if (nextPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                pawnMoves.add(smallMove);
            }
        }





    }


    // create rowInBound
    public boolean rowInBound(int row)
    {
        return row >= 1 && row <= 8;
    }
}
