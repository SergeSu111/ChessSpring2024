package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator
{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        ChessPiece thePiece = board.getPiece(myPosition);
        return KnightMove(board, thePiece, myPosition);
    }

    private Collection<ChessMove> KnightMove(ChessBoard board, ChessPiece thePiece, ChessPosition startPosition) {
        int startColumn = startPosition.getColumn();
        int startRow = startPosition.getRow();
        int nextRow;
        int nextColumn;

        ArrayList<ChessMove> KnightMoves = new ArrayList<>();
        // right 1 up 2
        nextRow = startRow + 2;
        nextColumn = startColumn + 1;
        if (isInBound(new ChessPosition(nextRow, nextColumn)))  // if the endPosition is inBound
        {
            ChessPosition endPosition = new ChessPosition(nextRow, nextColumn); // get the end Position
            ChessPiece endPiece = board.getPiece(endPosition); // get the piece
            if (endPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                KnightMoves.add(smallMove);
            }
            else
            {
                if (endPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                    KnightMoves.add(smallMove);
                }
            }
        }


        // left 1 up 2
        nextColumn = startColumn - 1;
        nextRow = startRow + 2;
        if (isInBound(new ChessPosition(nextRow, nextColumn)))  // if the endPosition is inBound
        {
            ChessPosition endPosition = new ChessPosition(nextRow, nextColumn); // get the end Position
            ChessPiece endPiece = board.getPiece(endPosition); // get the piece
            if (endPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                KnightMoves.add(smallMove);
            }
            else
            {
                if (endPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                    KnightMoves.add(smallMove);
                }
            }
        }

        // right 2 up 1
        nextColumn = startColumn + 2;
        nextRow = startRow + 1;
        if (isInBound(new ChessPosition(nextRow, nextColumn)))  // if the endPosition is inBound
        {
            ChessPosition endPosition = new ChessPosition(nextRow, nextColumn); // get the end Position
            ChessPiece endPiece = board.getPiece(endPosition); // get the piece
            if (endPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                KnightMoves.add(smallMove);
            }
            else
            {
                if (endPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                    KnightMoves.add(smallMove);
                }
            }
        }

        // left 2 up 1
        nextColumn = startColumn - 2;
        nextRow = startRow + 1;
        if (isInBound(new ChessPosition(nextRow, nextColumn)))  // if the endPosition is inBound
        {
            ChessPosition endPosition = new ChessPosition(nextRow, nextColumn); // get the end Position
            ChessPiece endPiece = board.getPiece(endPosition); // get the piece
            if (endPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                KnightMoves.add(smallMove);
            }
            else
            {
                if (endPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                    KnightMoves.add(smallMove);
                }
            }
        }
        // right 1 down 2
        nextColumn += startColumn + 1;
        nextRow -= startRow - 2;
        if (isInBound(new ChessPosition(nextRow, nextColumn)))  // if the endPosition is inBound
        {
            ChessPosition endPosition = new ChessPosition(nextRow, nextColumn); // get the end Position
            ChessPiece endPiece = board.getPiece(endPosition); // get the piece
            if (endPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                KnightMoves.add(smallMove);
            }
            else
            {
                if (endPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                    KnightMoves.add(smallMove);
                }
            }
        }
        // left 1 down 2
        nextColumn -= startColumn - 1;
        nextRow -= startRow - 2;
        if (isInBound(new ChessPosition(nextRow, nextColumn)))  // if the endPosition is inBound
        {
            ChessPosition endPosition = new ChessPosition(nextRow, nextColumn); // get the end Position
            ChessPiece endPiece = board.getPiece(endPosition); // get the piece
            if (endPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                KnightMoves.add(smallMove);
            }
            else
            {
                if (endPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                    KnightMoves.add(smallMove);
                }
            }
        }
        // right 2 down 1
        nextColumn = startColumn + 2;
        nextRow -= startRow - 1;
        if (isInBound(new ChessPosition(nextRow, nextColumn)))  // if the endPosition is inBound
        {
            ChessPosition endPosition = new ChessPosition(nextRow, nextColumn); // get the end Position
            ChessPiece endPiece = board.getPiece(endPosition); // get the piece
            if (endPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                KnightMoves.add(smallMove);
            }
            else
            {
                if (endPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                    KnightMoves.add(smallMove);
                }
            }
        }
        // left 2 down 1
        nextColumn -= startColumn - 2;
        nextRow -= startRow - 1;
        if (isInBound(new ChessPosition(nextRow, nextColumn)))  // if the endPosition is inBound
        {
            ChessPosition endPosition = new ChessPosition(nextRow, nextColumn); // get the end Position
            ChessPiece endPiece = board.getPiece(endPosition); // get the piece
            if (endPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                KnightMoves.add(smallMove);
            }
            else
            {
                if (endPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, endPosition, null);
                    KnightMoves.add(smallMove);
                }
            }
        }
        return KnightMoves;

    }

    boolean isInBound(ChessPosition currentPosition)
    {
        return currentPosition.getColumn() >= 1 && currentPosition.getColumn() <= 8 && currentPosition.getRow() >= 1 && currentPosition.getRow() <= 8;
    }

}
