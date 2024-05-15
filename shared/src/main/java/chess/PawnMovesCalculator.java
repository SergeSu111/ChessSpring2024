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

    @Override
    public String toString() {
        return "PawnMovesCalculator{}";
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
        else
        {
            setUpRow = 7;
            nextRow = startRow - 1;
        }


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
                else
                {
                    ChessMove smallMove = new ChessMove(startPosition, new ChessPosition(nextRow, startColumn), null);
                    pawnMoves.add(smallMove);
                }
            }
        }

        // if the startRow is just the setupRow
        if (startRow == setUpRow)
        {
            // we can go ahead 2
            if (thePiece.getTeamColor() == ChessGame.TeamColor.WHITE)
            {
                int nextNextRow = nextRow + 1;  // just in case you have to make sure the nextRow has not same color pieces
                ChessPosition nextNextPosition = new ChessPosition(nextNextRow, startColumn);
                ChessPiece nextNextPiece = board.getPiece(nextNextPosition);

                // also has to test if the nextPiece is the same color
                ChessPosition nextPosition = new ChessPosition(nextRow, startColumn);
                ChessPiece nextPiece = board.getPiece(nextPosition);

                if (nextNextPiece == null && nextPiece == null)
                {
                    ChessMove smallMove1 = new ChessMove(startPosition, nextNextPosition, null); // for step 2
                    //ChessMove smallMove2 = new ChessMove(startPosition, nextPosition, null); // for step1
                    pawnMoves.add(smallMove1);
                    //pawnMoves.add(smallMove2);
                }
                if (nextPiece == null && nextNextPiece != null)  // if nextPiece is null but nextNext is not . I can go 1 step even if I am in the setup Line
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                    pawnMoves.add(smallMove);
                }

            }
            else
            {
                int nextNextRow = nextRow - 1;  // just in case you have to make sure the nextRow has not same color pieces
                ChessPosition nextNextPosition = new ChessPosition(nextNextRow, startColumn);
                ChessPiece nextNextPiece = board.getPiece(nextNextPosition);

                // also has to test if the nextPiece is the same color
                ChessPosition nextPosition = new ChessPosition(nextRow, startColumn);
                ChessPiece nextPiece = board.getPiece(nextPosition);

                if (nextNextPiece == null && nextPiece == null)
                {
                    ChessMove smallMove1 = new ChessMove(startPosition, nextNextPosition, null);
//                    ChessMove smallMove2 = new ChessMove(startPosition, nextPosition, null);
                    pawnMoves.add(smallMove1);
//                    pawnMoves.add(smallMove2);
                }
                if (nextPiece == null && nextNextPiece != null)  // if nextPiece is null but nextNext is not . I can go 1 step even if I am in the setup Line
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null);
                    pawnMoves.add(smallMove);
                }

            }

        }

        // right
        int nextColumn = startColumn + 1;
        ChessPosition nextPositionRight = new ChessPosition(nextRow, nextColumn); // 右斜方的位置
        if (KnightMovesCalculator.isInBound(nextPositionRight)) // if the nextPosition is in bound
        {

            ChessPiece nextPiece = board.getPiece(nextPositionRight);
            if (nextPiece != null && nextPiece.getTeamColor() != thePiece.getTeamColor())  // I can eat
            {
                if (nextPositionRight.getRow() == 1 || nextPositionRight.getRow() == 8) // needs promote
                {
                    ChessMove smallMove1 = new ChessMove(startPosition, nextPositionRight, ChessPiece.PieceType.ROOK);
                    pawnMoves.add(smallMove1);
                    ChessMove smallMove2 = new ChessMove(startPosition, nextPositionRight, ChessPiece.PieceType.QUEEN);
                    pawnMoves.add(smallMove2);
                    ChessMove smallMove3 = new ChessMove(startPosition, nextPositionRight, ChessPiece.PieceType.KNIGHT);
                    pawnMoves.add(smallMove3);
                    ChessMove smallMove4 = new ChessMove(startPosition, nextPositionRight, ChessPiece.PieceType.BISHOP);
                    pawnMoves.add(smallMove4);
                }
                else
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPositionRight, null); // else not in 1 or 8 line
                    pawnMoves.add(smallMove);
                }


            }
        }

        // left
        nextColumn -= 2; // because we just got the right. we want to turned it left. -2
        ChessPosition nextPosition = new ChessPosition(nextRow, nextColumn); // 右斜方的位置
        if (KnightMovesCalculator.isInBound(nextPosition)) // if the nextPosition is in bound
        {

            ChessPiece nextPieceLeft = board.getPiece(nextPosition);
            if (nextPieceLeft != null && nextPieceLeft.getTeamColor() != thePiece.getTeamColor())  // I can eat
            {
                if (nextPosition.getRow() == 1 || nextPosition.getRow() == 8) // needs promote
                {
                    ChessMove smallMove1 = new ChessMove(startPosition, nextPosition, ChessPiece.PieceType.ROOK);
                    pawnMoves.add(smallMove1);
                    ChessMove smallMove2 = new ChessMove(startPosition, nextPosition, ChessPiece.PieceType.QUEEN);
                    pawnMoves.add(smallMove2);
                    ChessMove smallMove3 = new ChessMove(startPosition, nextPosition, ChessPiece.PieceType.KNIGHT);
                    pawnMoves.add(smallMove3);
                    ChessMove smallMove4 = new ChessMove(startPosition, nextPosition, ChessPiece.PieceType.BISHOP);
                    pawnMoves.add(smallMove4);
                }
                else
                {
                    ChessMove smallMove = new ChessMove(startPosition, nextPosition, null); // else not in 1 or 8 line
                    pawnMoves.add(smallMove);

                }

            }
        }

        return pawnMoves;

    }


    // create rowInBound
    public boolean rowInBound(int row)
    {
        return row >= 1 && row <= 8;
    }
}
