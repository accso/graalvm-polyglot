package de.accso.graalvm.pythonconsole.eval;

import java.util.Collections;
import java.util.List;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import de.accso.graalvm.pythonconsole.inspector.ValueInspector;

public class PythonEvaluator implements Evaluator {

    private Context context;
    private ValueInspector inspector;

    public PythonEvaluator(Context context, ValueInspector inspector) {
        this.context = context;
        this.inspector = inspector;
    }

    @Override
    public List<String> keywords() {
        return Collections.emptyList();
    }

    @Override
    public boolean evaluate(String line, String command, String[] args) {
        try {
            Value value = context.eval("python", line);
            inspector.inspectValue(value);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

}