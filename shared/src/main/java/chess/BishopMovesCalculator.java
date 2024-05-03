package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator
{

    @Override
    public  Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition)
    {
        ChessPiece thePiece = board.getPiece(myPosition); // get the current piece

        return diagonal(board, thePiece, myPosition);
    }

    public Collection<ChessMove> diagonal(ChessBoard board, ChessPiece thePiece, ChessPosition startPosition)
    {
        // get the start row and column
        int startColumn = startPosition.getColumn();
        int startRow = startPosition.getRow();

        ArrayList<ChessMove> allBishopMoves = new ArrayList<>();
        // up right
        for (int nextRow = startRow +1 , nextColumn = startColumn + 1;  nextRow <= 8 && nextColumn <= 8; nextRow++ , nextRow++)
        {
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn); // after moving once.
            if (bound(nextPosition)) // if it is inbound
            {
                ChessPiece nextPiece = board.getPiece(nextPosition); // get the current piece based on the moving position
                if (nextPiece.getPieceType() == null)
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                    allBishopMoves.add(smallMove); // put into big arrayList
                }
                else
                {
                    if (nextPiece.getTeamColor() != thePiece.getTeamColor()) // if there is a piece for the next
                    {
                        if (nextPiece.getPieceType() == ChessPiece.PieceType.KING)  // If the next piece is King
                        {
                            ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                            allBishopMoves.add(smallMove);
                            break;
                        }
                        ChessMove smallMove = new ChessMove(startPosition, nextPosition, null); // put enemy piece into my list
                        allBishopMoves.add(smallMove);
                    }

                }
            }
            break; // if out of bound
        }

        // up left
        for (int nextRow = startRow + 1, nextColumn = startRow - 1; nextRow <= 8 && nextColumn >= 1; nextRow++, nextColumn--)
        {
            ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn);
            if (bound(nextPosition)) // if is still in bound
            {
                ChessPiece nextPiece = board.getPiece(nextPosition); // get the nextPiece
                if (nextPiece.getPieceType() == null)
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                    allBishopMoves.add(smallMove);
                }
                else
                {
                    if (nextPiece.getTeamColor() != thePiece.getTeamColor()) // if color is different
                    {

                    }
                }
            }
        }

        // down right

        // down left
    }

    private Boolean bound(ChessPosition currentPosition)
    {
        return currentPosition.getRow() <= 8 && currentPosition.getColumn() <= 8;
    }
}
