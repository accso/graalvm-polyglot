package de.accso.graalvm.numpy;

import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import org.graalvm.polyglot.*;

public class JavaNumPyFuncParam {
    public static void main(String[] args) {
        Stopwatch evalWatch = Stopwatch.createUnstarted();
        Stopwatch execWatch = Stopwatch.createUnstarted();

        Context context = Context.newBuilder().allowAllAccess(true).build();

        String pythonSource = "import polyglot\n" +
                              "import site\n" +
                              "import numpy as np\n" +
                              // 
                              "@polyglot.export_value\n" +
                              "def printarray(n):\n" +
                              "    arr = np.arange(15).reshape(3, 5)\n" +
                              "    arr = arr * n\n" +
                              "    print(arr)";
        evalWatch.start();
        
        context.eval("python", pythonSource);
        
        evalWatch.stop();

        Value func = context.getPolyglotBindings().getMember("printarray");

        execWatch.start();
        func.execute(5);
        execWatch.stop();

        System.out.println("Elapsed Eval: " + evalWatch.elapsed(TimeUnit.MILLISECONDS) + "ms");
        System.out.println("Elapsed Exec: " + execWatch.elapsed(TimeUnit.MILLISECONDS) + "ms");

    }
}