
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Path p = FileSystems.getDefault().getPath("big_test.arpa");
		ArpaRead reader = new ArpaRead(p);
		
		HashMap<Integer, NGramHandler> grams = reader.GetNGrams();
		
		//grams.get(2).
		//System.err.println("hej");
	}

}
