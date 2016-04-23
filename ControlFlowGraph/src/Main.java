import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import file.FileManager;

public class Main {
	public static void main(String[] args) {
		FileManager fileManager = FileManager.getInstance();
		fileManager.init("/Users/LisM/Desktop/Nghien cuu khoa hoc/app/NCKH/NCKH-ControlFlowGraph/ControlFlowGraph/example/");
		fileManager.readFile("hello.txt");
		
		/*SyntaxManager syntaxManager = new SyntaxManager(fileManager.getData());
		syntaxManager.printStatementList();*/
		CfgTree tree = new CfgTree(fileManager.getData());
		tree.printNodeList();
		tree.printEdgeList();
		System.out.println("-------------------------------------");
		tree.printMainTreeList();
	}
}
