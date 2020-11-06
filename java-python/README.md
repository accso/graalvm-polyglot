# Calling Python from Java

## Install python and numpy

`gu install python`
`graalpython -m ginstall install numpy`

## compile (needs maven 3.x)

`mvn package`

## run (needs GraalVM, tested on 20.2 for java11)

Hello:

`java -cp target/numpy-1.0-SNAPSHOT.jar de.accso.graalvm.numpy.Hello`

NumPy:

`java -cp target/numpy-1.0-SNAPSHOT.jar de.accso.graalvm.numpy.JavaNumPyFuncReturn`
