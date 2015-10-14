FILENAME = report
pdf:
	pdflatex $(FILENAME).tex
	pdflatex $(FILENAME).tex
	pdflatex $(FILENAME).tex
bib:
	latex $(FILENAME).tex
	bibtex $(FILENAME).aux
	latex $(FILENAME).tex
	pdflatex $(FILENAME).tex