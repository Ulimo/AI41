import java.util.Arrays;
import java.util.LinkedList;

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
	
	public LinkedList<NGram> getMostProbableGrams(String[] words, int PredictionCount)
	{
		NGram toFind = new NGram(words, 0, 0);
		
		int[] interval = Search(toFind);
		
		
		return null;
	}
	
	public void AddNGram(NGram gram)
	{
		this.grams[Number] = gram;
		Number++;
	}
}
