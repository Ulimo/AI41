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
	
	public LinkedList<NGram> GetPrediction(String sentance)
	{
		sentance = sentance.toLowerCase();
		String[] words = sentance.split("\\s+");

		//Not yet implemented, should look at the last words up to the largest n-gram size (WordCount + 1)
		for(int i = LargestNGramSize; i >= 1; i--)
		{
			NGramHandler handler = handlers.get(i);
			
			LinkedList<NGram> grams = handler.getMostProbableGrams(words);
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
