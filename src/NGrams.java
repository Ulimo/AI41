import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class NGrams {
	
	HashMap<Integer, NGramHandler> handlers;
	
	private int LargestNGramSize;
	
	private POSTaggerME tagger = null;

	public NGrams(Path pathToCorpus, String PathToPOS)
	{
		ReadTrainingData(pathToCorpus);
		CreatePosModel(PathToPOS);
	}
	
	private void CreatePosModel(String PathToPOS)
	{
		InputStream modelIn = null;
		try {
		  modelIn = new FileInputStream(PathToPOS);
		  POSModel model = new POSModel(modelIn);
		  tagger = new POSTaggerME(model);
		}
		catch (IOException e) {
		  // Model loading failed, handle the error
		  e.printStackTrace();
		}
		finally {
		  if (modelIn != null) {
		    try {
		    	
		      modelIn.close();
		    }
		    catch (IOException e) {
		    }
		  }
		}
	}
	
	private void ReadTrainingData(Path p)
	{
		ArpaRead reader = new ArpaRead(p);
		handlers = reader.GetNGrams();
		
		
		SortHandlers();
	}
	
	public NGram[] GetPrediction(String sentance, int PredictionCount, int MaxNgramSize)
	{
		sentance = sentance.toLowerCase();
		String[] words = sentance.split("\\s+");

		
		String[] wordsForGrammar = new String[words.length + 1];
		
		for(int i = 0; i < words.length; i++)
		{
			wordsForGrammar[i] = words[i];
		}
		
		int HighestGram = Math.min(MaxNgramSize, Math.min(words.length + 1, LargestNGramSize));
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
			
			NGram[] grams = handler.getMostProbableGrams(wordsForGrammar, wordsToSend, PredictionCount, tagger);
			if(grams != null && grams.length > 0)
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
