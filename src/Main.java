import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
	
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
			//e.printStackTrace();
			return;
		}

		MyGrammar parser = new MyGrammar(file);
		SimpleNode root = parser.Program();
		root.dump(">");
	}

}