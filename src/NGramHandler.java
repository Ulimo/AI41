
public class NGramHandler {

	private int Number = 0;
	
	private int NGramSize;
	
	private NGram[] grams;
	
	public int GetNumberOfGrams()
	{
		return grams.length;
	}
	
	public NGramHandler(int NgramSize, int NumberOfGrams)
	{
		this.NGramSize = NgramSize;
		this.grams = new NGram[NumberOfGrams];
	}
	
	public void AddNGram(NGram gram)
	{
		this.grams[Number] = gram;
		Number++;
	}
}
