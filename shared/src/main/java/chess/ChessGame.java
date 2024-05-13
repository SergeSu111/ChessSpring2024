package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;

    private TeamColor turn;

    public ChessGame() {
        this.board = new ChessBoard(); // get the ChessBoard by creating a Chessboard object by constructor
        this.board.resetBoard(); // reset The board;
        this.turn = TeamColor.WHITE; // the default turn is white
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team; // set the turn color to the team
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece currentPiece= this.board.getPiece(startPosition);
        Collection<ChessMove> potentialMoves = currentPiece.pieceMoves(this.board, startPosition); // get all potential moves but need to plus isinCheck
        HashSet<ChessMove> resultValid =  new HashSet<>();
        for (ChessMove smallMove : potentialMoves) {
            if (!isInCheck(currentPiece.getTeamColor()))
            {
                resultValid.add(smallMove);
            }
        }
        return resultValid;

    }

    @Override
    public String toString() {
        return "ChessGame{}";
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException
    {
        // call validMove

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard currentBoard = this.getBoard(); // get the currentBoard
        ChessPosition kingPosition = null;

        // to get the King's position
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row + 1, col + 1);
                ChessPiece currentPiece = currentBoard.getPiece(currentPosition); // get the currentPiece

                if (currentPiece.getPieceType() == ChessPiece.PieceType.KING && currentPiece.getTeamColor() == teamColor) {
                    kingPosition = currentPosition; // get the King's position
                    break;
                }
            }
        }

        // to make sure the current spot's end position is the King's position
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row + 1, col + 1);
                ChessPiece currentPiece = currentBoard.getPiece(currentPosition); // get the currentPiece
                if (currentPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> allMoves = currentPiece.pieceMoves(currentBoard, currentPosition);
                    for (ChessMove smallMove : allMoves) {
                        if (smallMove.endPosition == kingPosition)
                        {
                            return true;
                        }
                    }
                }

            }
        }
        return false;

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor)
    {
        Collection<ChessMove> validMoves;
        // call isInCheck. IS TRUE
        if (isInCheck(teamColor))
        {
            // call valid moves, which is empty
            for (int row = 0; row < 8; row++)
            {
                for (int col = 0; col < 8; col++)
                {
                    ChessPosition currentPosition = new ChessPosition(row + 1, col + 1);
                    ChessPiece currentPiece = this.board.getPiece(currentPosition);
                    validMoves = validMoves(currentPosition);
                    if (!validMoves.isEmpty())
                    {
                        return false;
                    }

                }
            }
            return true;
        }
        return false;
        // is true
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor)
    {
        // it is only called
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
