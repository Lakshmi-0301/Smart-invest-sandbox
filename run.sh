if [ "$1" == "--clean" ]; then
  echo "Cleaning old class files..."
  rm -rf bin
fi

mkdir -p bin

echo "Compiling Java files..."
javac -d bin -cp "lib/sqlite-jdbc-3.50.3.0.jar" $(find src -name "*.java")

echo "Running program..."
java -cp "bin:lib/sqlite-jdbc-3.50.3.0.jar" test.TestMain
