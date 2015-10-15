Creating the N-gram ARPA files:
	The N-gram ARPA files are already created and located in the root folder of the project. In order to create new ARPA files, the Makefile located in the folder 'raw' can be run using commands:

	Create 4-grams for case sensitive corpus with ML, AD and KN smoothing:
		make all

	Create 4-grams for lowercase corpus with ML, AD and KN smoothing:
		make alllowercase

Compiling and running the word predictor:
	The easiest way to run the word predictor is to run the Makefile in the root folder of the project using "make all".

	The commands run are:
		javac -classpath lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar src/*.java -d bin/
		java -classpath bin:lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar Main

		(Commands for Linux. Change commands if necessary when running Windows)

