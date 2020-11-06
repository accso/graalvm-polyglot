package de.accso.graalvm.numpy;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import org.graalvm.polyglot.*;

public class JavaNumPyFunc {
    public static void main(String[] args) {
        Stopwatch evalWatch = Stopwatch.createUnstarted();
        Stopwatch execWatch = Stopwatch.createUnstarted();

        Context context = Context.newBuilder().allowAllAccess(true).build();

        String pythonSource = "import polyglot\n" +
                              "import site\n" +
                              "import numpy as np\n" +
                              "@polyglot.export_value\n" +
                              "def printarray():\n" +
                              "    arr = np.arange(15).reshape(3, 5)\n" +
                              "    arr = arr * 3\n" +
                              "    print(arr)";
        evalWatch.start();
        
        context.eval("python", pythonSource);
        
        evalWatch.stop();

        Value func = context.getPolyglotBindings().getMember("printarray");

        execWatch.start();
        func.execute();
        execWatch.stop();

        System.out.println("Elapsed Eval: " + evalWatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        System.out.println("Elapsed Exec: " + execWatch.elapsed(TimeUnit.MILLISECONDS) + "ms");

        final int n = 500; 
        execWatch.reset();
        execWatch.start();
        for (int i = 0; i < n; i++) {
            func.execute();
        }
        execWatch.stop();
        
        long elapsed = execWatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("Elapsed Exec(x" + n + "x): " + elapsed + "ms - avg. " + elapsed / n + "ms");
    }
}