package de.accso.graalvm.pythonconsole.printer;

import java.util.Map;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.jline.terminal.Terminal;

public class TerminalLevelPrinter implements LevelPrinter {
    private Terminal terminal;

    private Map<Integer, Color> colorMap = Map.of(0, Ansi.Color.GREEN, 1, Ansi.Color.BLUE, 2, Ansi.Color.MAGENTA, 3,
            Ansi.Color.CYAN, 4, Ansi.Color.RED);

    public TerminalLevelPrinter(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void printLine(int level, String line) {
        Color color = colorMap.getOrDefault(level, Ansi.Color.BLACK);
        terminal.writer().println(Ansi.ansi().fg(color) + "  ".repeat(level) + line + Ansi.ansi().reset());
        terminal.flush();
    }
    
}