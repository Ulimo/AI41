CORPUS=CombinedCorpus.txt
LCCORPUS=CombinedCorpusLowercase.txt

all:
	make kn
	make abs
	make ml

alllowercase:
	make lowercasekn
	make lowercaseabs
	make lowercaseml
	
kn:
	java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams $(CORPUS) ../kn.model.arpa -kn -n 4
abs:
	java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams $(CORPUS) ../abs.model.arpa -abs -n 4
ml:
	java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams $(CORPUS) ../ml.model.arpa -ml -n 4
	
lowercasekn: 
	java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams $(LCCORPUS) ../kn.model.lowercase.arpa -kn -n 4
lowercaseabs: 
	java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams $(LCCORPUS) ../abs.model.lowercase.arpa -abs -n 4
lowercaseml: 
	java -cp ../lib/kylm-0.0.7.jar kylm.main.CountNgrams $(LCCORPUS) ../ml.model.lowercase.arpa -ml -n 4
