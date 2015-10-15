Creating the N-gram ARPA files:
	The N-gram ARPA files are already created and located in the root folder of the project. In order to create new ARPA files, the Makefile located in the folder 'raw' can be run using commands:

	Create 4-grams for case sensitive corpus with ML, AD and KN smoothing:
		make all

	Create 4-grams for lowercase corpus with ML, AD and KN smoothing:
		make alllowercase

Compiling and running the word predictor:
	For compiling the program, running the "make build" command in the project root is the easiest way. This corresponds to the following command:

	javac -classpath lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar src/*.java -d bin/

	(Commands for Linux. Change command if necessary when running Windows, I think ':' is supposed to be ';')

	The easiest way to run the word predictor is to run the Makefile in the root folder of the project using "make all". This compiles and runs the program using default values (Kneser-Ney Smoothing with case sensitive corpus, grammar constraints and context recognition switched on).

	The commands for "make alL" are:
		javac -classpath lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar src/*.java -d bin/
		java -classpath bin:lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar Main

		(Commands for Linux. Change commands if necessary when running Windows)

	To specify which ARPA-file to use (smoothing technique and whether to use lowercase corpus), enter the file as the first command line argument to the program [kn,knlc,abs,abslc,ml,mllc].

	Grammar constraints can be turned off using the second command line argument [grammar, nogrammar].

	Context recognition can be turned off using the third command line argument [context, nocontext].

	Examples:

	*Everything on, kneser-ney smoothing case sensitive*:
		java -classpath bin:lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar Main kn grammar context

	*Everything off, maximum likelihood smoothing lowercase*:
		java -classpath bin:lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar Main mllc nogrammar nocontext
