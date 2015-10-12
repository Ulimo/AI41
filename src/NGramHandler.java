import java.util.Arrays;
import java.util.LinkedList;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.uima.postag.*;

public class NGramHandler {

	private int Number = 0;
	
	private int NGramSize;
	
	private NGram[] grams;
	
	public int GetNumberOfGrams()
	{
		return grams.length;
	}
	
	public int GetGramSize()
	{
		return NGramSize;
	}
	
	public NGramHandler(int NgramSize, int NumberOfGrams)
	{
		this.NGramSize = NgramSize;
		this.grams = new NGram[NumberOfGrams];
	}
	
	public void Sort()
	{
		Arrays.sort(grams, 0, Number);
	}
	
	//https://en.wikipedia.org/wiki/Suffix_array
	int[] Search(NGram toFind)
	{
		int l = 0, r = grams.length;
		
		while(l < r)
		{
			int mid = (l+r) / 2;
			if(toFind.compareTo(grams[mid]) > 0)
			{
				l = mid + 1;
			}
			else
			{
				r = mid;
			}
		}
		int s = l; r = grams.length;
		while(l < r)
		{
			int mid = (l+r) / 2;
			if(toFind.compareTo(grams[mid]) < 0)
			{
				r = mid;
			}
			else
			{
				l = mid + 1;
			}
		}
		return new int[] {s, r};
	}
	
	private boolean SendToGrammarCheck(String[] wordsForGrammar, NGram nextgram, POSTaggerME posModel)
	{
		wordsForGrammar[wordsForGrammar.length - 1] = nextgram.GetLastWord();
		
		String[] tags = posModel.tag(wordsForGrammar);
		double[] probs = posModel.probs();
		
		return true;
	}

	private NGram[] MostProbable(int start, final int end, final int size, String[] wordsForGrammar, POSTaggerME posModel) {
		assert start >= 0;
		assert end > start;
		assert end-start >= size;

		NGram[] res = new NGram[size];

		for ( ; start < end ; ++start ) {
			
			//Grammar stuff here
			if(!SendToGrammarCheck(wordsForGrammar, grams[start], posModel))
			{
				continue;
			}
			
			for ( int i=0 ; i<size ; ++i ) {
				if(res[i]==null) {
					res[i]=grams[start];
					break;
				}
				else {
					if(res[i].GetProbability() < grams[start].GetProbability()) {
						for(int j=size-1; j>i; --j) {
							res[j] = res[j-1];
						}
						res[i] = grams[start];
						break;
					}
				}
			}
		}
		for(int i=0; i<size; ++i) {
			assert res[i] != null;
			if (i< size-1) {
				assert res[i].GetProbability() >= res[i+1].GetProbability();
			}
		}
		return res;
	}
	
	public NGram[] getMostProbableGrams(String[] wordsForGrammar, String[] words, int PredictionCount, POSTaggerME posModel)
	{
		NGram toFind = new NGram(words, 0, 0);
		
		int[] interval = Search(toFind);
		int Predict = Math.min(PredictionCount, interval[1] - interval[0]);
		return MostProbable(interval[0], interval[1], Predict, wordsForGrammar, posModel);
	}
	
	public void AddNGram(NGram gram)
	{
		this.grams[Number] = gram;
		Number++;
	}
}
