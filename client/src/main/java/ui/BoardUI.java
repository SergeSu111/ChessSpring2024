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
    }

    private static void drawHeader(PrintStream out, String headerText)
    {

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
