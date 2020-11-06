package de.accso.graalvm.numpy;

import org.graalvm.polyglot.*;
import static java.lang.System.out;

public class JavaNumPyFuncReturn {
	public static void main(String[] args) {
        Context context = Context.newBuilder().allowAllAccess(true).build();

        String pythonSource = "import polyglot                      \n" +
                              "import site                          \n" +
                              "import numpy as np                   \n" +
                              "                                     \n" +
                              "@polyglot.export_value               \n" +
                              "def createarray():                   \n" +
                              "    arr = np.arange(15).reshape(3, 5)\n" +
                              "    arr = arr * 3                    \n" +
                              "    return arr";
      
        context.eval("python", pythonSource);

        Value func = context.getPolyglotBindings().getMember("createarray");
        Value result = func.execute();

        // Ausgabe des results zeigt die Matrix an
        out.println(result);


        // Wird auch als Array erkannt, Zugriff auf Zeile 1 Spalte 2
        out.println("result has array elements: " + result.hasArrayElements());
        out.println("element at (0,1): " + result.getArrayElement(0).getArrayElement(1));
        // Ein asLong schlägt aber Fehl, da Java den Typ von '3' nicht kennt...
        Value value = result.getArrayElement(0).getArrayElement(1);
        // FIXED! hasArrayElements auf "3" gibt immer noch "true" zurück??
        out.println("element at (0,1) has array elements? " + value.hasArrayElements());
        // aber getArraySize schlägt fehl.
        // out.println("element at (0,1) array size: " + value.getArraySize());
        // selbst asString schlägt fehl
        // out.println(Long.valueOf(value.asString()));
        out.println("element at (0,1) has members? " + value.hasMembers());
        out.println("element at (0,1) member keys: " + value.getMemberKeys());
        // Ouch
        long javaValue = Long.valueOf("" + value);
        out.println("As Java Long: " + javaValue);
    }
}