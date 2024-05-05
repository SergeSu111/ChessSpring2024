package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator
{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece thePiece = board.getPiece(myPosition); // get the current piece

        return straight(board, thePiece, myPosition);
    }

    public Collection<ChessMove> straight(ChessBoard board, ChessPiece thePiece, ChessPosition startPosition)
    {
        ArrayList<ChessMove> rookMoves = new ArrayList<>();
        int startRow = startPosition.getRow();
        int startColumn = startPosition.getColumn();
        // up
        for (int nextRow = startRow + 1; nextRow <= 8; nextRow++)
        {
            ChessPosition nextPosition = new ChessPosition(nextRow, startColumn); // get the nextPosition
            ChessPiece nextPiece = board.getPiece(nextPosition);
            if (nextPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                rookMoves.add(smallMove);
            }
            else
            {
                if (nextPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null); // if Color is different, enemy's piece get it.
                    rookMoves.add(smallMove);
                }
                break;
            }
            if(thePiece.getPieceType() == ChessPiece.PieceType.KING)
            {
                break; // because King can only go one step
            }
        }
        // down
        for (int nextRow = startRow - 1; nextRow >= 1; nextRow--)
        {
            ChessPosition nextPosition = new ChessPosition(nextRow, startColumn); // get the nextPosition
            ChessPiece nextPiece = board.getPiece(nextPosition);
            if (nextPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                rookMoves.add(smallMove);
            }
            else
            {
                if (nextPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null); // if Color is different, enemy's piece get it.
                    rookMoves.add(smallMove);
                }
                break;
            }
            if(thePiece.getPieceType() == ChessPiece.PieceType.KING)
            {
                break; // because King can only go one step
            }
        }
        // right
        for (int nextColumn = startColumn + 1; nextColumn <= 8; nextColumn++)
        {
            ChessPosition nextPosition = new ChessPosition(startRow, nextColumn); // get the nextPosition
            ChessPiece nextPiece = board.getPiece(nextPosition);
            if (nextPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                rookMoves.add(smallMove);

            }
            else
            {
                if (nextPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null); // if Color is different, enemy's piece get it.
                    rookMoves.add(smallMove);
                }
                break;
            }
            if(thePiece.getPieceType() == ChessPiece.PieceType.KING)
            {
                break; // because King can only go one step
            }
        }
        // left
        for (int nextColumn = startColumn - 1; nextColumn >= 1; nextColumn--)
        {
            ChessPosition nextPosition = new ChessPosition(startRow, nextColumn); // get the nextPosition
            ChessPiece nextPiece = board.getPiece(nextPosition);
            if (nextPiece == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                rookMoves.add(smallMove);
            }
            else
            {
                if (nextPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null); // if Color is different, enemy's piece get it.
                    rookMoves.add(smallMove);
                }
                break;
            }
            if(thePiece.getPieceType() == ChessPiece.PieceType.KING)
            {
                break; // because King can only go one step
            }
        }
        return rookMoves;
    }


}
