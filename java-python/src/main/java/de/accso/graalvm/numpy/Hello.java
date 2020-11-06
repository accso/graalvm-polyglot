package de.accso.graalvm.numpy;

import org.graalvm.polyglot.*;

public class Hello {
    public static void main(String[] args) {
        Context context = Context.newBuilder().allowAllAccess(true).build();

        String pythonSource = "import polyglot           \n" + 
                              "                          \n" +
                              "@polyglot.export_value    \n" +
                              "def hello(name):          \n" + 
                              "  print(\"Hello \" + name)  ";

        context.eval("python", pythonSource);

        Value helloFunc = context.getPolyglotBindings().getMember("hello");
        helloFunc.execute("GraalVM!");
    }
}