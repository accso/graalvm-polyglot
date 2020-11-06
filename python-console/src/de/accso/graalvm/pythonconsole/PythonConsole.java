package de.accso.graalvm.pythonconsole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.graalvm.polyglot.Context;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.ParsedLine;
import org.jline.reader.Parser;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import de.accso.graalvm.pythonconsole.eval.ContextEvaluator;
import de.accso.graalvm.pythonconsole.eval.Evaluator;
import de.accso.graalvm.pythonconsole.eval.PythonEvaluator;
import de.accso.graalvm.pythonconsole.inspector.ValueInspector;
import de.accso.graalvm.pythonconsole.printer.TerminalLevelPrinter;

public class PythonConsole {

	public static void main(String[] args) {
		TerminalBuilder builder = TerminalBuilder.builder().jansi(true);
		try (Terminal terminal = builder.build()) {
			Context context = Context.newBuilder().allowAllAccess(true).build();
			ValueInspector inspector = new ValueInspector(new TerminalLevelPrinter(terminal));

			List<Evaluator> evaluators = new ArrayList<>();
			evaluators.add(new ContextEvaluator(context, inspector));
			evaluators.add(new PythonEvaluator(context, inspector));

			Parser parser = new DefaultParser();
			LineReader reader = LineReaderBuilder.builder().terminal(terminal)
					.completer(new StringsCompleter(
							evaluators.stream().flatMap(eval -> eval.keywords().stream()).collect(Collectors.toList())))
					.parser(parser).build();

			while (true) {
				String line = null;
				try {
					line = reader.readLine("> ", null, (MaskingCallback) null, null);
				} catch (UserInterruptException e) {
					// ignore
				} catch (EndOfFileException e) {
					break;
				}
				if (line == null) {
					continue;
				}

				line = line.trim();
				ParsedLine pl = reader.getParser().parse(line, 0);
				String[] argv = pl.words().subList(1, pl.words().size()).toArray(new String[0]);
				String command = pl.word();
				for (Evaluator eval : evaluators) {
					if (eval.evaluate(line, command, argv)) {
						break;
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
