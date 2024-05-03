package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator
{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece thePiece = board.getPiece(myPosition); // get the current piece

        return diagonal(board, thePiece, myPosition);
    }

    public static Collection<ChessMove>  diagonal(ChessBoard board, ChessPiece thePiece, ChessPosition startPosition)
    {
        // get the start row and column
        int startColumn = startPosition.getColumn();
        int startRow = startPosition.getRow();

        ArrayList<ChessMove> allBishopMoves = new ArrayList<>();
        // up right
        for (int nextRow = startRow + 1, nextColumn = startColumn + 1; nextRow <= 8 && nextColumn <= 8; nextRow++, nextRow++) {
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn); // after moving once.
            ChessPiece nextPiece = board.getPiece(nextPosition); // get the current piece based on the moving position
            if (nextPiece.getPieceType() == null) {
                ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                allBishopMoves.add(smallMove); // put into big arrayList
            } else {
                if (nextPiece.getTeamColor() != thePiece.getTeamColor()) // if there is a piece for the next
                {

                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null); // put enemy piece into my list
                    allBishopMoves.add(smallMove);
                }
                break; // if the color is the same, just also break;
            }

        }

        // up left
        for (int nextRow = startRow + 1, nextColumn = startRow - 1; nextRow <= 8 && nextColumn >= 1; nextRow++, nextColumn--) {
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
            ChessPiece nextPiece = board.getPiece(nextPosition); // get the nextPiece
            if (nextPiece.getPieceType() == null) {
                ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                allBishopMoves.add(smallMove);
            } else {
                if (nextPiece.getTeamColor() != thePiece.getTeamColor()) // if color is different
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                    allBishopMoves.add(smallMove);
                }
                break;
            }
        }

        // down right
        for (int nextRow = startRow - 1, nextColumn = startColumn + 1; nextRow >= 1 && nextColumn <= 8; nextRow--, nextColumn++)
        {
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
            ChessPiece nextPiece = board.getPiece(nextPosition);
            if (nextPiece.getPieceType() == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                allBishopMoves.add(smallMove);
            }
            else {
                if (nextPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                    allBishopMoves.add(smallMove);
                }
                break;
            }

        }

        // down left
        for (int nextRow = startRow - 1, nextColumn = startColumn - 1; nextRow >= 1 && nextColumn >= 1; nextRow--, nextColumn--)
        {
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
            ChessPiece nextPiece = board.getPiece(nextPosition);
            if (nextPiece.getTeamColor() == null)
            {
                ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                allBishopMoves.add(smallMove);
            }
            else {
                if (nextPiece.getTeamColor() != thePiece.getTeamColor())
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                    allBishopMoves.add(smallMove);
                }
                break;
            }
        }
        return allBishopMoves;
    }
}




