import generation.JasminGeneratorVisitor;
import parser.ASTDocument;
import parser.MyGrammar;
import parser.ParseException;
import semantics.SemanticVisitor;
import symbolTable.SymbolTable;
import symbolTable.exception.SemanticException;

import java.io.*;

public class Main{
	private static boolean debug = false;
	private static String filename;
	private static boolean useLoopTemplates;
	private static boolean optimizeBooleans;

	public static boolean parseArgs(String[] args) {
		if(args.length < 1 || args.length > 4){
			System.err.println("Usage: java jmm [-v] [-r=<num>] [-o] <input_file.jmm>");
			return false;
		}

		int i;

		for(i = 0; i < args.length - 1; i++) {
			switch (args[i]) {
				case "-v":
					debug = true;
					SymbolTable.setDebug(true);
					break;

				case "-werror":
					SemanticVisitor.werror = true;
					break;

				case "-r":
					break;

				case "-o":
					useLoopTemplates = true;
					optimizeBooleans = true;
					break;

				default:
					break;
			}
		}

		if (!args[i].matches("(.*).jmm")) {
			System.err.println("Invalid file type: only .jmm files are accepted");
			return false;
		}

		filename = args[i];

		return true;
	}

	public static void main(String[] args) throws ParseException, SemanticException {
		if(!parseArgs(args)) System.exit(1);

		ASTDocument root = syntacticAnalysis(filename);

		if(debug) {
			root.dump(">");
			System.out.print("\n");
		}

		String filename = semanticAnalysis(root);

		try {
			File generatedCodeFile = new File(filename + ".j");
			if (!generatedCodeFile.exists()) {
				generatedCodeFile.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(generatedCodeFile);
			PrintWriter writer = new PrintWriter(out);
			JasminGeneratorVisitor generator = new JasminGeneratorVisitor(writer, optimizeBooleans, useLoopTemplates);
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

	private static String semanticAnalysis(ASTDocument root) throws SemanticException {
		SemanticVisitor.numErrors = 0;
		String className = (String) root.jjtAccept(new SemanticVisitor(), null);

		if(SemanticVisitor.numErrors > 0) {
			throw new SemanticException(SemanticVisitor.numErrors + " error(s) were found during semantic analysis. Fix them and try again.");
		}

		return className;
	}
}