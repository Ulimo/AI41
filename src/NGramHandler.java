import java.util.Arrays;
import java.util.LinkedList;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.uima.postag.*;

public class NGramHandler {

	private int Number = 0; //Contains the number of n-grams added to the handler
	
	private int NGramSize; //The size of the n-grams in this handler
	
	private NGram[] grams; //Array containing the n-grams
	
	/**
	 * Returns the number of n-grams inside this handler
	 * @return
	 */
	public int GetNumberOfGrams()
	{
		return grams.length;
	}
	
	/**
	 * Returns the size of the N in the n-grams in the handler
	 * @return
	 */
	public int GetGramSize()
	{
		return NGramSize;
	}
	
	public NGramHandler(int NgramSize, int NumberOfGrams)
	{
		this.NGramSize = NgramSize;
		this.grams = new NGram[NumberOfGrams];
	}
	
	/**
	 * Sort all n-grams in this handler for binary search
	 */
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
	
	/**
	 * Sends a n-gram to the grammar check and will return true if the word is ok, false if it is not
	 * @param wordsForGrammar
	 * @param nextgram
	 * @param posModel
	 * @return
	 */
	private boolean SendToGrammarCheck(String[] wordsForGrammar, NGram nextgram, POSTaggerME posModel)
	{
		wordsForGrammar[wordsForGrammar.length - 1] = nextgram.GetLastWord();
		
		String[] tags = posModel.tag(wordsForGrammar);
		double[] probs = posModel.probs();
		
		return Grammar.getGrammar(wordsForGrammar, tags, probs);
	}

	
	/**
	 * Returns the most probable n-grams up to a maximum of size.
	 * @param start
	 * @param end
	 * @param size
	 * @param wordsForGrammar
	 * @param posModel
	 * @return
	 */
	private NGram[] MostProbable(int start, final int end, final int size, String[] wordsForGrammar, POSTaggerME posModel) {
		assert start >= 0;
		assert end > start;
		assert end-start >= size;

		NGram[] res = new NGram[size];

		for ( ; start < end ; ++start ) {
			
			//Call grammar check only if grammar checking is turned on
			if(Main.useGrammar && !SendToGrammarCheck(wordsForGrammar, grams[start], posModel))
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
	
	/**
	 * Returns the most probable n-grams up to prediction count
	 * @param wordsForGrammar
	 * @param words
	 * @param PredictionCount
	 * @param posModel
	 * @return
	 */
	public NGram[] getMostProbableGrams(String[] wordsForGrammar, String[] words, int PredictionCount, POSTaggerME posModel)
	{
		NGram toFind = new NGram(words, 0, 0);
		
		int[] interval = Search(toFind);
		int Predict = Math.min(PredictionCount, interval[1] - interval[0]);
		return MostProbable(interval[0], interval[1], Predict, wordsForGrammar, posModel);
	}
	
	/**
	 * Add a n-gram to the handler
	 * @param gram
	 */
	public void AddNGram(NGram gram)
	{
		this.grams[Number] = gram;
		Number++;
	}
}
