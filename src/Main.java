
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Path p = FileSystems.getDefault().getPath("big_test.arpa");
		ArpaRead reader = new ArpaRead(p);
		//System.err.println("hej");
	}

}
