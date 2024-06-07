package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class BoardUI
{
    private static final int COLUMNS = 8;
    private static final int ROWS = 8;

    public static void main(String[] args)
    {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawHeaders(out);
        drawBoard(out);
    }

    private static void drawHeaders(PrintStream out)
    {
        setGray(out);
        String[] lettersInHeader = {"h", "e", "f", "g", "d", "c", "b", "a"};
        for(int column = 0; column < COLUMNS; column++)
        {
            drawHeader(out, lettersInHeader[column]);
        }
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText)
    {
        // How do I know how much the length it is?
        // prefixLength is the space in front of the letter in header.
        int prefixLength = COLUMNS / 7;
        // suffixLength IS the space after the letter and before the next prefixLength
        // print the length
        out.print(EMPTY.repeat(prefixLength));
        // print the letter
        printHeaderText(out, headerText);
        // print the suffixLength
        out.print(EMPTY.repeat(prefixLength)); // because suffix is the same length with prefix
    }

    private static void printHeaderText(PrintStream out, String letter)
    {
        //out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(letter);
        setGray(out);
    }

    private static void drawBoard(PrintStream out)
    {
        for (int boardRow = 0; boardRow < ROWS; boardRow++)
        {
            drawEachRow(out);
        }
    }

    private static void drawEachRow(PrintStream out)
    {
        int prefixLength = COLUMNS / 14;
        String pieceOnUIBoard = null;
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(EMPTY.repeat(prefixLength));
        int numberRow = 1;
        out.print(EMPTY.repeat(prefixLength));

        out.print(EMPTY.repeat(prefixLength));
        out.print(String.valueOf(numberRow));
        out.print(EMPTY.repeat(prefixLength));

        for (int squareRow = 0; squareRow < ROWS; squareRow++)
        {
            for (int boardCol = 0; boardCol < COLUMNS; boardCol++)
            {
                if (boardCol % 2 == 0)
                {
                    setWhite(out);
                    out.print(SET_TEXT_COLOR_RED);
                    out.print(EMPTY.repeat(prefixLength)); // make the small piece into spot have the same prefix
                    ChessBoard board = new ChessBoard();
                    board.resetBoard(); // reset so I can get the reset pieces.
                    ChessPiece targetPiece = board.getPiece(new ChessPosition(squareRow + 1, boardCol + 1));
                    // how can I turned the piece I got onto the board?
                    if (targetPiece != null)
                    {
                        if (targetPiece.getTeamColor() == ChessGame.TeamColor.BLACK)
                        {
                            switch (targetPiece.getPieceType())
                            {
                                case PAWN -> pieceOnUIBoard = BLACK_PAWN;
                                case KNIGHT -> pieceOnUIBoard = BLACK_KNIGHT;
                                case ROOK -> pieceOnUIBoard = BLACK_ROOK;
                                case QUEEN -> pieceOnUIBoard = BLACK_QUEEN;
                                case KING -> pieceOnUIBoard = BLACK_KING;
                                case BISHOP -> pieceOnUIBoard = BLACK_BISHOP;
                            }
                        }
                        else
                        {
                            switch (targetPiece.getPieceType())
                            {
                                case PAWN -> pieceOnUIBoard = WHITE_PAWN;
                                case KNIGHT -> pieceOnUIBoard = WHITE_KNIGHT;
                                case ROOK -> pieceOnUIBoard = WHITE_ROOK;
                                case QUEEN -> pieceOnUIBoard = WHITE_QUEEN;
                                case KING -> pieceOnUIBoard = WHITE_KING;
                                case BISHOP -> pieceOnUIBoard = WHITE_BISHOP;
                            }
                        }
                        out.print(pieceOnUIBoard);
                        out.print(EMPTY.repeat(prefixLength));

                    }
                    else
                    {
                        out.print(EMPTY);
                    }

                }
                else // black spot
                {
                    setBlack(out);
                    out.print(SET_TEXT_COLOR_RED);
                }
            }
        }
    }

    private static void setGray(PrintStream out)
    {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setWhite(PrintStream out)
    {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setBlack(PrintStream out)
    {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }


}