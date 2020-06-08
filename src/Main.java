import ConstantPropagation.ConstantPropagationAnalysisVisitor;
import controlFlowAnalysis.*;
import generation.JasminGeneratorVisitor;
import parser.ASTDocument;
import parser.MyGrammar;
import parser.ParseException;
import semantics.SemanticVisitor;
import symbolTable.SymbolTable;
import symbolTable.exception.SemanticException;

import java.io.*;
import java.util.List;

public class Main{
	private static boolean debug = false;
	private static boolean registerAllocation = false;
	private static int numRegisters = 0;
	private static String filename;
	private static boolean useLoopTemplates = false;
	private static boolean optimizeBooleans = false;
	private static boolean propagateAndFoldConstants = false;

	public static boolean parseArgs(String[] args) {
		if(args.length < 1){
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

				case "-o":
					useLoopTemplates = true;
					optimizeBooleans = true;
					propagateAndFoldConstants = true;
					break;
				case "-lp":
					useLoopTemplates = true;
					break;
				case "-ob":
					optimizeBooleans = true;
					break;
				case "-pfc":
				    propagateAndFoldConstants = true;
					break;
				default:
					if(args[i].matches("-r=\\d+")){
						registerAllocation = true;
						numRegisters = Integer.parseInt(args[i].substring(3));
						if(numRegisters < 1) {
							System.err.println("Invalid number of registers");
							return false;
						}
					}
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

	public static void main(String[] args) throws ParseException, SemanticException, AllocationException {
		if(!parseArgs(args)) System.exit(1);

		ASTDocument root = syntacticAnalysis(filename);

		if(debug) {
			root.dump(">");
			System.out.print("\n");
		}

		String filename = semanticAnalysis(root);

		if(registerAllocation)
			controlFlowAnalysis(root);

		if (propagateAndFoldConstants) {
			ConstantPropagationAnalysisVisitor constProp = new ConstantPropagationAnalysisVisitor();
			root.jjtAccept(constProp, null);
		}

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

	private static void controlFlowAnalysis(ASTDocument root) throws AllocationException {

		@SuppressWarnings("unchecked")
		List<ControlFlowGraph> graphList = (List<ControlFlowGraph>) root.jjtAccept(new ControlFlowVisitor(), null);


		for(ControlFlowGraph graph: graphList){
			ControlFlowAnalysis.liveness(graph);

			if(debug) {
				System.out.println(graph.toString());
			}

			InterferenceGraph interferenceGraph = ControlFlowAnalysis.interferenceGraph(graph);

			if(debug) {
				System.out.println(interferenceGraph.print(graph.getMethodName()));
			}

			int methodLocalsCount = graph.getInitialStackOffset();

			if(!interferenceGraph.isEmpty()) {
				methodLocalsCount = ControlFlowAnalysis.coloring(interferenceGraph, numRegisters, graph.getInitialStackOffset());

				System.out.println(interferenceGraph.printAllocation(graph.getMethodName()));
			}

			graph.getMethodDescriptor().setLocalsCount(methodLocalsCount);
		}

	}

}