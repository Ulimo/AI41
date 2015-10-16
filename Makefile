all:
	javac -classpath lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar src/*.java -d bin/
	java -classpath bin:lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar Main
build:
	javac -classpath lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar src/*.java -d bin/
