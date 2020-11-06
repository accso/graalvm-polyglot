package de.accso.graalvm.pythonconsole.eval;

import java.util.Date;
import java.util.List;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import de.accso.graalvm.pythonconsole.inspector.ValueInspector;

public class ContextEvaluator implements Evaluator {
    private Context context;
    private ValueInspector inspector;

    private String INSPECT_COMMAND = "/inspect";
    private String CREATE_COMMAND = "/create";
    private String INVOKE_COMMAND = "/invoke";

    public ContextEvaluator(Context context, ValueInspector inspector) {
        this.context = context;
        this.inspector = inspector;
    }

    @Override
    public List<String> keywords() {
        return List.of(INSPECT_COMMAND, CREATE_COMMAND, INVOKE_COMMAND);
    }

    @Override
    public boolean evaluate(String line, String command, String[] args) {
        try {
            Value bindings = context.getBindings("python");
            if (CREATE_COMMAND.equals(command)) {
                bindings.putMember(args[0], new Date());
            } else if (INSPECT_COMMAND.equals(command)) {
                if (args.length > 0) {
                    Value member = bindings.getMember(args[0]);
                    inspector.inspectValue(member);
                } else {
                    inspector.inspectValue(bindings);
                }
            } else if (INVOKE_COMMAND.equals(command)) {
                Value member = bindings.getMember(args[0]);
                if (member.canInvokeMember(args[1])) {
                    Value result = member.invokeMember(args[1]);
                    inspector.inspectValue(result);
                } else {
                    inspector.inspectValue(member);
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }
}