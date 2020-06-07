package utils;

import parser.ASTAnd;
import parser.ASTLessThan;
import parser.ASTNegation;
import parser.Node;

public class Utils {
    public static boolean isArithmeticBoolean(Node node) {
        return node instanceof ASTAnd || node instanceof ASTNegation || node instanceof ASTLessThan;
    }
}
