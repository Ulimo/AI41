
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.uima.postag.*;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Path p = FileSystems.getDefault().getPath("big_test.arpa");
		
		NGrams grams = new NGrams(p);
		
		NGram[] result = grams.GetPrediction("height of itscvcvcv", 10, 5);
		
		InputStream modelIn = null;
		POSTaggerME tagger = null;
		try {
		  modelIn = new FileInputStream("en-pos-maxent.bin");
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
		
		String[] sent = new String[]{"Most", "large", "cities", "in", "the", "US", "had",
                "morning", "and", "afternoon", "newspapers", "."};
		
		String[] tags = tagger.tag(sent);
		double[] probs = tagger.probs();
		/*ArpaRead reader = new ArpaRead(p);
		
		HashMap<Integer, NGramHandler> grams = reader.GetNGrams();
		
		
		Iterator<Entry<Integer, NGramHandler>> it = grams.entrySet().iterator();
		
		while(it.hasNext())
		{
			Entry<Integer, NGramHandler> entry = it.next();
			
			NGramHandler handler = entry.getValue();
			if(handler != null)
			{
				handler.Sort();
			}
		}*/
		//grams.get(0).
		//grams.get(2).
		//System.err.println("hej");
	}

}
