package de.accso.graalvm.pythonconsole.inspector;

import org.graalvm.polyglot.*;

import de.accso.graalvm.pythonconsole.printer.LevelPrinter;

import java.util.stream.Collectors;

public class ValueInspector {
    private static final int MAX_DEPTH = 4;
    LevelPrinter printer;

    public ValueInspector(LevelPrinter printer) {
        this.printer = printer;
    }

    public void inspectValue(Value value) {
        inspectValue(0, value);
    }

    void inspectValue(int level, Value value) {
        inspectValueBasic(level, value); // null, boolean, string, exception
        inspectValueNumber(level, value); // numbers
        inspectValueTime(level, value); // date, time, timezone, ...
        inspectValueStructure(level, value); // arrays, lists and maps
        inspectValueObject(level, value); // host, proxy, meta object, native pointer
        inspectValueExec(level, value); // executable, instantiatable
    }

    void inspectValueBasic(int level, Value value) {
        if (value.isNull()) {
            printer.printLine(level, String.format("Value is Null."));
        }
        if (value.isBoolean()) {
            printer.printLine(level, String.format("Value is Boolean: %s", value.asBoolean()));
        }
        if (value.isString()) {
            printer.printLine(level, String.format("Value is String: %s", value.asString()));
        }
        if (value.isException()) {
            printer.printLine(level, String.format("Value is Exception: %s", value.throwException()));
        }
    }

    void inspectValueNumber(int level, Value value) {
        if (value.isNumber()) {
            printer.printLine(level, String.format("Value is Number."));
            if (value.fitsInByte()) {
                printer.printLine(level, String.format("o  Number %d fits in Byte.", value.asByte()));
            } else if (value.fitsInShort()) {
                printer.printLine(level, String.format("o  Number %d fits in Short.", value.asShort()));
            } else if (value.fitsInInt()) {
                printer.printLine(level, String.format("o  Number %d fits in Int.", value.asInt()));
            } else if (value.fitsInLong()) {
                printer.printLine(level, String.format("o  Number %d fits in Long.", value.asLong()));
            }

            if (value.fitsInFloat()) {
                printer.printLine(level, String.format("o  Number %f fits in Float.", value.asFloat()));
            } else if (value.fitsInDouble()) {
                printer.printLine(level, String.format("o  Number %f fits in Double.", value.asDouble()));
            }
        }
    }

    void inspectValueTime(int level, Value value) {
        if (value.isDate()) {
            printer.printLine(level, String.format("Value is Date: %s", value.asDate()));
        }
        if (value.isTime()) {
            printer.printLine(level, String.format("Value is Time: %s", value.asTime()));
        }
        if (value.isTimeZone()) {
            printer.printLine(level, String.format("Value is TimeZone: %s", value.asTimeZone()));
        }
        if (value.isInstant()) {
            printer.printLine(level, String.format("Value is Instant: %s", value.asInstant()));
        }
        if (value.isDuration()) {
            printer.printLine(level, String.format("Value is Duration: %s", value.asDuration()));
        }
    }

    void inspectValueStructure(int level, Value value) {
        if (value.hasArrayElements()) {
            printer.printLine(level, String.format("Value has %d array elements.", value.getArraySize()));
            if (value.getArraySize() > 0 && level < MAX_DEPTH) {
                printer.printLine(level, "o  Inspecting first Element...");
                Value element = value.getArrayElement(0);
                inspectValue(level + 1, element);
            }

        }
        if (value.hasMembers()) {
            printer.printLine(level, String.format("Value has %d member keys.", value.getMemberKeys().size()));
            printer.printLine(level,
                    String.format("o  Keys: %s", value.getMemberKeys().stream().collect(Collectors.joining(","))));
        }
    }

    void inspectValueObject(int level, Value value) {
        if (value.isHostObject()) {
            printer.printLine(level, String.format("Value is host object."));
        }
        if (value.isMetaObject()) {
            printer.printLine(level, String.format("Value is meta object. Qualified name: %s", value.getMetaQualifiedName()));
        }
        if (value.isProxyObject()) {
            printer.printLine(level, String.format("Value is proxy object: %s", value.asProxyObject()));
        }
        if (value.isNativePointer()) {
            printer.printLine(level, String.format("Value is native pointer: %d", value.asNativePointer()));
        }
    }

    void inspectValueExec(int level, Value value) {
        if (value.canExecute()) {
            printer.printLine(level, String.format("Value can be executed."));
            if (level < MAX_DEPTH) {
                printer.printLine(level, String.format("o  Trying execution with no arguments..."));
                Value result = value.execute();
                printer.printLine(level, "o  Inspecting result...");
                inspectValue(level + 1, result);
            }
        }
        if (value.canInstantiate()) {
            printer.printLine(level, String.format("Value can be instanciated."));
            if (level < MAX_DEPTH) {
                printer.printLine(level, String.format("o  Try creating instance with no arguments..."));
                Value inst = value.newInstance();
                printer.printLine(level, "o  Inspecting instance...");
                inspectValue(level + 1, inst);
            }
        }
    }
}