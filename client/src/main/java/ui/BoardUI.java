package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PipedReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;

public class BoardUI
{
    private static final int COLUMNS = 8;
    private static final int ROWS = 8;
    private static ChessBoard board = new ChessBoard();
    public static ChessGame.TeamColor color;
    public static void main(String[] args)
    {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
    }


    public static void callWhiteBoard(PrintStream out)
    {
        color = WHITE;
        int startRowNumberWhite = 1;
        String[] lettersInHeaderWhite = {"h", "g", "f", "e", "d", "c", "b", "a"};
        drawHeaders(out, lettersInHeaderWhite);
        drawBoard(out, startRowNumberWhite);
    }

    public static void callBlackBoard(PrintStream out)
    {
        color = BLACK;
        int startRowNumberBlack = 8;
        String[] lettersInHeaderBlack = {"a", "b", "c", "d", "e", "f", "g", "h"};
        drawHeaders(out, lettersInHeaderBlack);
        drawBoard(out, startRowNumberBlack);
    }
    private static void drawHeaders(PrintStream out, String[] lettersInHeader)
    {
        setGray(out);
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
//        int prefixLength = (COLUMNS / 8);
        // suffixLength IS the space after the letter and before the next prefixLength
        // print the length
//        int halfLength =  EMPTY.length() / 2;
        //String subEmpty = EMPTY.substring(0,halfLength);
        out.print("\u2003 ");
        // print the letter
        printHeaderText(out, headerText);
        // print the suffixLength
        //out.print(EMPTY.repeat(realLength));
    }

    private static void printHeaderText(PrintStream out, String letter)
    {
        //out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(letter);
        setGray(out);
    }

    private static void drawBoard(PrintStream out, int startRowNumber)
    {
        if (color == WHITE)
        {
            for (int boardRow = 0; boardRow < ROWS; boardRow++)
            {
                drawEachRow(out, boardRow, startRowNumber);
            }
        }
        else
        {
            for (int boardRow = 7; boardRow > -1; boardRow--)
            {
                drawEachRow(out, boardRow, startRowNumber);
            }
        }

//        out.println( RESET_BG_COLOR);
//        out.println(RESET_TEXT_COLOR);
    }

    private static void putPieceOnWhiteSpot(int squareRow, int boardCol, int prefixLength, PrintStream out)
    {
        boardCol--;
        setWhite(out);
        out.print(SET_TEXT_COLOR_RED);
        out.print(EMPTY.repeat(prefixLength)); // make the small piece into spot have the same prefix;
        board.resetBoard(); // reset so I can get the reset pieces.
        ChessPiece targetPiece = board.getPiece(new ChessPosition(squareRow + 1, boardCol + 1));
        // how can I turned the piece I got onto the board?
        if (targetPiece != null)
        {
            String returnedPiece = pickPiece(targetPiece, null, out);
            out.print(returnedPiece);
            out.print(EMPTY.repeat(prefixLength));
        }
        else
        {
            out.print(EMPTY);
            out.print(EMPTY.repeat(prefixLength));
        }
    }

    private static void putPieceOnBlackSpot(int squareRow, int boardCol, int prefixLength, PrintStream out)
    {
        boardCol--;
        setBlack(out);
        out.print(EMPTY.repeat(prefixLength));
        board.resetBoard();
        ChessPiece targetPiece = board.getPiece(new ChessPosition(squareRow + 1, boardCol + 1));
        if (targetPiece != null)
        {
            String returnedPiece = pickPiece(targetPiece, null, out);
            out.print(returnedPiece);
            out.print(EMPTY.repeat(prefixLength));
        }
        else
        {
            out.print(EMPTY);
            out.print(EMPTY.repeat(prefixLength));
        }


    }

    private static String switchTypeToGetPieceBLACK(ChessPiece targetPiece, String pieceOnUIBoard, PrintStream out)
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
        out.print(SET_TEXT_COLOR_RED);
        return pieceOnUIBoard;
    }

    private static String switchTypeToGetPieceWHITE(ChessPiece targetPiece, String pieceOnUIBoard, PrintStream out)
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
        out.print(SET_TEXT_COLOR_BLUE);
        return pieceOnUIBoard;
    }
    private static String pickPiece(ChessPiece targetPiece, String pieceOnUIBoard, PrintStream out)
    {
        if (targetPiece.getTeamColor() == BLACK)
        {
            return  switchTypeToGetPieceBLACK(targetPiece, pieceOnUIBoard, out);
        }
        else
        {
            return switchTypeToGetPieceWHITE(targetPiece, pieceOnUIBoard, out);
        }
    }
    private static void drawEachRow(PrintStream out, int boardRow, int StartRowNumber)
    {
        int prefixLength = (COLUMNS /16);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(EMPTY.repeat(prefixLength));
        out.print(String.valueOf(StartRowNumber));
        out.print(EMPTY.repeat(prefixLength));

        if (color == WHITE)
        {
            // means start from white spot
            if (boardRow % 2 == 0)
            {
                for (int boardCol = 1; boardCol <= COLUMNS; boardCol++)
                {
                    if (boardCol % 2 == 0)
                    {
                        putPieceOnWhiteSpot(boardRow, boardCol, prefixLength, out); // the null is current pieceOnUIBoard, it will be updated
                    }
                    else
                    {
                        putPieceOnBlackSpot(boardRow, boardCol, prefixLength, out);
                    }
                }
            }
            else
            {
                for (int boardCol = 1; boardCol <= COLUMNS; boardCol++)
                {
                    // white spot
                    if (boardCol % 2 == 0)
                    {
                        putPieceOnBlackSpot(boardRow, boardCol, prefixLength, out); // the null is current pieceOnUIBoard, it will be updated
                    }
                    else // black spot
                    {
                        putPieceOnWhiteSpot(boardRow, boardCol, prefixLength, out);
                    }
                }
                // the last row must be in the 7 so should write in here
            }
        }
        else
        {
            int copyRow = boardRow;
            copyRow++;
            // means start from white spot
            if (copyRow % 2 == 0)
            {
                for (int boardCol = 8; boardCol > 0; boardCol--)
                {
                    if (boardCol % 2 == 0)
                    {
                        putPieceOnWhiteSpot(boardRow, boardCol, prefixLength, out); // the null is current pieceOnUIBoard, it will be updated
                    }
                    else
                    {
                        putPieceOnBlackSpot(boardRow, boardCol, prefixLength, out);
                    }
                }
            }
            else
            {

                for (int boardCol = 1; boardCol <= COLUMNS; boardCol++)
                {
                    int copyCol = boardCol;
                    copyCol--;
                    // white spot
                    if (copyCol % 2 == 0)
                    {
                        putPieceOnBlackSpot(boardRow, boardCol, prefixLength, out); // the null is current pieceOnUIBoard, it will be updated
                    }
                    else // black spot
                    {
                        putPieceOnWhiteSpot(boardRow, boardCol, prefixLength, out);
                    }
                }
                // the last row must be in the 7 so should write in here
            }
        }


        setGray(out);
        out.print(EMPTY.repeat(prefixLength));
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(StartRowNumber);
        out.print(EMPTY.repeat(prefixLength));
        if (color == WHITE)
        {
            StartRowNumber = StartRowNumber + 1;
        }
        else
        {
            StartRowNumber = StartRowNumber - 1;
        }

        out.println();

        if (boardRow == 7)
        {
            setGray(out); // make the next line gray
            if (color == WHITE)
            {
                String[] lettersInHeaderWhite = {"h", "g", "f", "e", "d", "c", "b", "a"};
                drawHeaders(out,lettersInHeaderWhite);
            }
            else
            {
                String[] lettersInHeaderBlack = {"a", "b", "c", "d", "e", "f", "g", "h"};
                drawHeaders(out, lettersInHeaderBlack);
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
