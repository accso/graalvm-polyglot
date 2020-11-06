package de.accso.graalvm.pythonconsole.eval;

import java.util.List;

public interface Evaluator {
    List<String> keywords();
    boolean evaluate(String line, String command, String[] args);
}