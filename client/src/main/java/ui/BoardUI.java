package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

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
    }

    private static void drawHeaders(PrintStream out)
    {
        setGray(out);
        String[] lettersInHeader = {"h", "e", "f", "g", "d", "c", "b", "a"};
        for(int column = 0; column < COLUMNS; column++)
        {
            drawHeader(out, lettersInHeader[column]);
        }
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

    }

    private static void drawBoard(PrintStream out)
    {

    }

    private static void setGray(PrintStream out)
    {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }


}
