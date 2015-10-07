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
	
	public LinkedList<NGram> getMostProbableGrams(String[] words)
	{
		
		return null;
	}
	
	public void AddNGram(NGram gram)
	{
		this.grams[Number] = gram;
		Number++;
	}
}
