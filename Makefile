#--- kommandon ---
#make //kompilerar programmet
#make run //kompilerar om (om nödvändigt) och kör sedan programmet
#make clean //rensar alla object files samt det länkade programmet


#Compiler
CC = javac

#Enter additional classpath folders (colon separated)
PROJECTPATH = /home/joho/Documents/ai/proj/
CLASSPATH = bin/
SOURCEPATH = src/
CFLAGS = -g -classpath "$(addprefix $(PROJECTPATH),$(SOURCEPATH))" -d $(addprefix $(PROJECTPATH), $(CLASSPATH))

#source and class files
MAIN_FILE = Main.java
SOURCE_FILES = ArpaRead.java NGramHandler.java NGram.java
CLASSFILES = $(MAIN_FILE:.java=.class) $(SOURCE_FILES:.java=.class)

#Parameters to send to the programme on 'make run'
PARAMS =

#'-ea' to enable assertions during execution. O.w. blank.
ASSERT = -ea

all: $(CLASSFILES)

%.class: $(addprefix $(SOURCEPATH),%.java)
	$(CC) $(CFLAGS) $<

#--- makefile dependency ---
#$(SOURCE_FILES): Makefile
#	touch $@

.PHONY: clean

clean:
	rm -f $(addprefix $(CLASSPATH),$(CLASSFILES))
run: $(addprefix $(CLASSPATH),$(CLASSFILES))
	java -classpath "$(addprefix $(PROJECTPATH),$(CLASSPATH))" $(ASSERT) $(MAIN_FILE:.java=) $(PARAMS)
debug: $(addprefix $(CLASSPATH),$(CLASSFILES))
	jdb -classpath $(CLASSPATH) -sourcepath $(SOURCEPATH) $(MAIN_FILE:.java=)
