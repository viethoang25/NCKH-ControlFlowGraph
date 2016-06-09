import java.util.ArrayList;
import java.util.List;

import node.BaseNode;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import prefix.InfixToPrefix;
import coverage.DFS;
import file.FileManager;

public class Main {
	public static void main(String[] args) {
		FileManager fileManager = FileManager.getInstance();
		fileManager.init("./example/");
		fileManager.readFile("sumOdd");
		SyntaxManager syntaxManager = new SyntaxManager(fileManager.getData());
		syntaxManager.printStatementList();
		CfgTree tree = new CfgTree(fileManager.getData());
		tree.printNodeList();
		tree.printEdgeList();
		for (BaseNode i : tree.getEndNode()) {
			System.out.println("END : " + i.getIndex());
		}
		/*
		 * System.out.println("-------------------------------------");
		 * tree.printMainTreeList();
		 * System.out.println("-------------------------------------");
		 */
		InitGraph graph = new InitGraph();
		DFS dfs = new DFS(graph.getSizeArray(), graph.getGraph(),
				tree.getNodeList());
		dfs.doDFS(0, 0, graph.getSizeArray() - 1);

		if (tree.getStartLoop() > 0) {
			DFS loopDfs = new DFS(graph.getSizeArray(), graph.getGraph(),
					tree.getNodeList());
			loopDfs.doDFS(tree.getStartLoop(), tree.getStartLoop(), tree
					.getEndLoop().get(0));
			dfs.setForTestPath(loopDfs.getTestPath(), 3, tree.getStartLoop());
		}
		
		System.out.println(dfs.getTestPath().size());
		for (List<BaseNode> list : dfs.getTestPath()) {
			System.out.println("LIST");
			for (BaseNode n : list) {
				System.out.print(n.getIndex() + "->");
			}
			System.out.println();
		}
		for (List<BaseNode> list : dfs.getTestPath()) {
			System.out.println("---------------------------");
			PathConstraint path = new PathConstraint(list, tree.getEdgeList());
			System.out.println("VARIABLE");
			path.printVariableList();
			/*
			 * System.out.println("EXPRESSION"); path.printExpressionList();
			 */
			System.out.println(path.getZ3Input());
			Z3Handle z3 = new Z3Handle(path.getZ3Input());
			z3.printResult();
		}
	}
}
