import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

public class NGrams {
	
	HashMap<Integer, NGramHandler> handlers;
	
	private int LargestNGramSize;

	public NGrams(Path pathToCorpus)
	{
		ReadTrainingData(pathToCorpus);
	}
	
	private void ReadTrainingData(Path p)
	{
		ArpaRead reader = new ArpaRead(p);
		handlers = reader.GetNGrams();
		
		
		SortHandlers();
	}
	
	public LinkedList<NGram> GetPrediction(String sentance, int PredictionCount)
	{
		sentance = sentance.toLowerCase();
		String[] words = sentance.split("\\s+");

		
		int HighestGram = Math.min(words.length + 1, LargestNGramSize);
		//Not yet implemented, should look at the last words up to the largest n-gram size (WordCount + 1)
		for(int i = HighestGram; i >= 1; i--)
		{
			NGramHandler handler = handlers.get(i);
			
			int numberOfWordsToSend = i - 1;
			
			String[] wordsToSend = new String[numberOfWordsToSend];
			
			int startInSentence = words.length - numberOfWordsToSend;
			for(int x = 0; x < numberOfWordsToSend; x++)
			{
				wordsToSend[x] = words[startInSentence + x];
			}
			
			LinkedList<NGram> grams = handler.getMostProbableGrams(wordsToSend, PredictionCount);
			if(grams != null && grams.size() > 0)
			{
				return grams;
			}
		}
		
		
		//Grammar checks?
		
		
		
		return null;	
	}
	
	
	
	private void SortHandlers()
	{
		int LargestGramSize = 0;
		
		Iterator<Entry<Integer, NGramHandler>> it = handlers.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Integer, NGramHandler> entry = it.next();
			
			NGramHandler handler = entry.getValue();
			if(handler != null)
			{
				int gramSize = handler.GetGramSize();
				if(gramSize > LargestGramSize)
				{
					LargestGramSize = gramSize;
				}
				
				handler.Sort();
			}
		}
		LargestNGramSize = LargestGramSize;
		
	}
}
