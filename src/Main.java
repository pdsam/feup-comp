import java.io.InputStream;
import java.io.FileInputStream;

// Java code invoking the parser
public class Main{
	public static int MAX_ERRORS = 10;
	public static int numErrors = 0;
	public static boolean foundError = false;

	public static void main(String[] args) throws ParseException {
		if(args.length < 1) {
			System.out.println("Usage: java jmm [-r=<num>] [-o] <input_file.jmm>");
			return;
		} else if (!args[0].matches("(.*).jmm")) {
			System.out.println("Invalid file type: only .jmm files are accepted");
			return;
		}
		InputStream file = null;

		try {
			file = new FileInputStream(args[0]);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return;
		}

		MyGrammar.numErrors = 0;
		MyGrammar.foundError = false;
		MyGrammar parser = new MyGrammar(file);
		SimpleNode root = parser.Program();
		if(MyGrammar.foundError) {
			throw new ParseException(Integer.toString(numErrors) + " error(s) were found during parsing. Fix them and try again.");
		}
		root.dump(">");
	}
}