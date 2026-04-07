# Google Java Format - Custom Formatter Makefile

JAR = core/target/google-java-format-HEAD-SNAPSHOT-all-deps.jar
CLI = com.google.googlejavaformat.java.CustomFormatterCLI
JVM_FLAGS = --add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED \
            --add-exports jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED \
            --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED \
            --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED \
            --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED \
            --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED

.PHONY: build test run clean

# Build the fat JAR
build:
	mvn clean package -pl core -DskipTests

# Run tests
test:
	mvn test -pl core -DfailIfNoTests=false

# Run the CLI
# Usage: cat Test.java | make run
run:
	java $(JVM_FLAGS) -cp $(JAR) $(CLI)

# Clean build artifacts
clean:
	mvn clean
