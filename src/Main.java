import generation.JasminGeneratorVisitor;
import parser.ASTDocument;
import parser.MyGrammar;
import parser.ParseException;
import semantics.SemanticVisitor;
import symbolTable.SymbolTable;
import symbolTable.exception.SemanticException;

import java.io.*;

// Java code invoking the parser
//TODO set type of function
//TODO set type of self reference
//TODO set type of var reference
public class Main{
	private static boolean debug = true;

	public static void main(String[] args) throws ParseException, SemanticException {
		if(args.length < 1) {
			System.out.println("Usage: java jmm [-r=<num>] [-o] <input_file.jmm>");
			return;
		} else if (!args[0].matches("(.*).jmm")) {
			System.out.println("Invalid file type: only .jmm files are accepted");
			return;
		}

		ASTDocument root = syntacticAnalysis(args[0]);

		if(debug)
			root.dump(">");

		semanticAnalysis(root);

		try {
			File generatedCodeFile = new File("prog.jsm");
			if (!generatedCodeFile.exists()) {
				generatedCodeFile.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(generatedCodeFile);
			PrintWriter writer = new PrintWriter(out);
			JasminGeneratorVisitor generator = new JasminGeneratorVisitor(writer);
			root.jjtAccept(generator, null);
			writer.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ASTDocument syntacticAnalysis(String filename) throws ParseException {
		InputStream file;

		try {
			file = new FileInputStream(filename);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			return null;
		}

		MyGrammar.numErrors = 0;
		MyGrammar parser = new MyGrammar(file);
		ASTDocument root = parser.Program();

		if(MyGrammar.numErrors > 0) {
			throw new ParseException(MyGrammar.numErrors  + " error(s) were found during syntactic analysis. Fix them and try again.");
		}

		return root;
	}

	private static void semanticAnalysis(ASTDocument root) throws SemanticException {
		SemanticVisitor.numErrors = 0;
		root.jjtAccept(new SemanticVisitor(), null);

		if(SemanticVisitor.numErrors > 0) {
			throw new SemanticException(SemanticVisitor.numErrors + " error(s) were found during semantic analysis. Fix them and try again.");
		}
	}
}