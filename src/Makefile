#--- kommandon ---
#make //kompilerar programmet
#make run //kompilerar om (om nödvändigt) och kör sedan programmet
#make clean //rensar alla object files samt det länkade programmet

null :=
SPACE := $(null) $(null)
#Compiler
CC = javac

#Enter additional classpath folders (colon separated)
CLASSPATH = ../bin
LIBPATH = ../lib

LIBS = \
opennlp-tools-1.6.0.jar \
opennlp-uima-1.6.0.jar \

#source and class files
MAIN_FILE = Main

SOURCE_FILES = \
Main.java \
ArpaRead.java \
NGramHandler.java \
NGrams.java \
NGram.java \

CLASSFILES = $(SOURCE_FILES:.java=.class)

#Compilation
CFLAGS = -g -cp $(subst $(SPACE),:,$(addprefix $(LIBPATH)/,$(LIBS))):. -d $(CLASSPATH)
#Parameters to send to the programme on 'make run'
PARAMS =

#'-ea' to enable assertions during execution. O.w. blank.
ASSERT = -ea

all: $(addprefix $(CLASSPATH)/,$(CLASSFILES))

$(CLASSPATH)/%.class: %.java
	$(CC) $(CFLAGS) $<

#--- makefile dependency ---
#$(SOURCE_FILES): Makefile
#	touch $@

.PHONY: clean

clean:
	rm -f $(addprefix $(CLASSPATH)/,$(CLASSFILES))
run: $(addprefix $(CLASSPATH)/,$(CLASSFILES))
	java -classpath "$(CLASSPATH)" $(ASSERT) $(MAIN_FILE) $(PARAMS)
debug: $(addprefix $(CLASSPATH)/,$(CLASSFILES))
	jdb -classpath $(CLASSPATH) $(MAIN_FILE)
