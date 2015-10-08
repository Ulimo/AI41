
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Path p = FileSystems.getDefault().getPath("big_test.arpa");
		
		NGrams grams = new NGrams(p);
		
		NGram[] result = grams.GetPrediction("national", 3, 5);
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
