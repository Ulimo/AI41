File explanation:
	./
		This README file.
		Makefile for compiling and running word predictor.
		ARPA-files containing N-grams are stored here.
	src/
		Java source files:
			Main.java - Contains the main function where the path to files are described and the command line user interface is defined.
			ArpaRead.java - Represents an ARPA file reader, able to parse an ARPA formatted file and create a NGram Java object for every N-gram.
			NGram.java - Represents a single N-gram.
			NGrams.java - Represents all the N-grams of a given size(?). The context recognition is placed here for convenience.
			NGramHandler.java - ?
			Grammar.java - Handles checking for various grammar constraints.
	raw/
		Raw data, includes case sensitive corpus and lowercase corpus along with the books used to create them. Makefile to run KYLM to calculate N-grams and create ARPA-files.
	lib/
		Library files, OpenNLP and KYLM jar files.
	bin/
		Contains .class files.
	report/
		LaTex report.


Creating the N-gram ARPA files:
	The N-gram ARPA files are already created and located in the root folder of the project. In order to create new ARPA files, the Makefile located in the folder 'raw' can be run using commands:

	//Create 4-grams for case sensitive corpus with ML, AD and KN smoothing:
		make all
	//which corresponds to the following commands for KYLM:
		java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams CombinedCorpus.txt ../kn.model.arpa -kn -n 4
		java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams CombinedCorpus.txt ../abs.model.arpa -abs -n 4
		java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams CombinedCorpus.txt ../ml.model.arpa -ml -n 4

	//Create 4-grams for lowercase corpus with ML, AD and KN smoothing:
		make alllowercase
	//which corresponds to the following commands for KYLM:
		java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams CombinedCorpusLowercase.txt ../kn.model.lowercase.arpa -kn -n 4
		java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams CombinedCorpusLowercase.txt ../abs.model.lowercase.arpa -abs -n 4
		java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams CombinedCorpusLowercase.txt ../ml.model.lowercase.arpa -ml -n 4

Compiling and running the word predictor:
	For compiling the program, running the "make build" command in the project root is the easiest way. This corresponds to the following command:

	javac -classpath lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar src/*.java -d bin/

	(Commands for Linux. Change command if necessary when running Windows, I think ':' is supposed to be ';')

	The easiest way to run the word predictor is to run the Makefile in the root folder of the project using "make all". This compiles and runs the program using default values (Kneser-Ney Smoothing with case sensitive corpus, grammar constraints and context recognition switched on).

	The commands for executed for "make all" are:
		javac -classpath lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar src/*.java -d bin/
		java -classpath bin:lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar Main

		(Commands for Linux. Change commands if necessary when running Windows)

	To specify which ARPA-file to use (smoothing technique and whether to use lowercase corpus), enter the file as the first command line argument to the program [kn,knlc,abs,abslc,ml,mllc].

	Grammar constraints can be turned off using the second command line argument [grammar, nogrammar].

	Context recognition can be turned off using the third command line argument [context, nocontext].

	Examples:
	//Everything on, kneser-ney smoothing case sensitive:
		java -classpath bin:lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar Main kn grammar context

	//Everything off, maximum likelihood smoothing lowercase:
		java -classpath bin:lib/opennlp-tools-1.6.0.jar:lib/opennlp-uima-1.6.0.jar Main mllc nogrammar nocontext

User interface when running program:
	When starting the program the user is first prompted to choose number of predictions to be displayed every time the word predictor predicts words. Recommended: 5.

	After that the user is prompted to choose the maximum N-gram size to use. Max/Recommended: 4.

	The user is then prompted to enter a sentence for which the next word should be predicted. This sentence can be:
		1. Blank
		2. Contain one or several words separated by spaces, e.g. I am a
		3. Same as above but instead of typing out the entire word, the wildcard character '*' can be used to predict a word beginning with some letter(s). E.g. I am a gentlem*

	The word predictor then shows its predictions to the user, to which the user can choose to select one of the predicted words (typing 0 to 4 if five predictions) or telling the word predictor that none of the words were correct (-1).

	If -1 is entered, the user can continue the sentence in the same way as described above, by entering full words or by using a wildcard in the end.

	It's also possible to restart the word predictor or exit by entering -2 or -3 respectively.
