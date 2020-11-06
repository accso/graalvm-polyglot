package de.accso.graalvm.numpy;

import org.graalvm.polyglot.*;

public class JavaNumPy {
    public static void main(String[] args) {
        Context context = Context.newBuilder().allowAllAccess(true).build();

        String pythonSource = "import site                          \n" +
                              "import numpy as np                   \n" +
                              "arr = np.arange(15).reshape(3, 5)    \n" +
                              "arr = arr * 3                        \n" +
                              "print(arr)";
       
        context.eval("python", pythonSource);
    }
}