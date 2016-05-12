import java.io.File;
import java.util.List;

import node.BaseNode;
import coverage.DFS;
import file.FileManager;

public class Application {

	private FileManager fileManager;
	private CfgTree cfgTree;
	private InitGraph initGraph;
	private DFS dfs;

	public Application(File file, int depth) {
		fileManager = FileManager.getInstance();
		fileManager.readFile(file);

		cfgTree = new CfgTree(fileManager.getData());

		initGraph = new InitGraph();
		dfs = new DFS(initGraph.getSizeArray(), initGraph.getGraph(),
				cfgTree.getNodeList());
		dfs.doDFS(0, 0, initGraph.getSizeArray() - 1);
		if (cfgTree.getStartLoop() > 0) {
			DFS loopDfs = new DFS(initGraph.getSizeArray(),
					initGraph.getGraph(), cfgTree.getNodeList());
			loopDfs.doDFS(cfgTree.getStartLoop(), cfgTree.getStartLoop(),
					cfgTree.getEndLoop().get(0));
			dfs.setForTestPath(loopDfs.getTestPath(), depth,
					cfgTree.getStartLoop());
		}
	}

	public Application(String file, int depth) {
		fileManager = FileManager.getInstance();
		fileManager.init(".\\example\\");
		fileManager.readFile(file);

		cfgTree = new CfgTree(fileManager.getData());

		initGraph = new InitGraph();
		dfs = new DFS(initGraph.getSizeArray(), initGraph.getGraph(),
				cfgTree.getNodeList());
		dfs.doDFS(0, 0, initGraph.getSizeArray() - 1);
		if (cfgTree.getStartLoop() > 0) {
			DFS loopDfs = new DFS(initGraph.getSizeArray(),
					initGraph.getGraph(), cfgTree.getNodeList());
			loopDfs.doDFS(cfgTree.getStartLoop(), cfgTree.getStartLoop(),
					cfgTree.getEndLoop().get(0));
			dfs.setForTestPath(loopDfs.getTestPath(), depth,
					cfgTree.getStartLoop());
		}
	}

	public String getResult() {
		StringBuilder result = new StringBuilder();
		result.append("Number of test path : " + dfs.getTestPath().size()
				+ "\n");
		int i = 1;
		for (List<BaseNode> list : dfs.getTestPath()) {
			result.append("\n------< " + i + " >------\n");
			PathConstraint path = new PathConstraint(list,
					cfgTree.getEdgeList());
			Z3Handle z3 = new Z3Handle(path.getZ3Input());
			result.append(z3.toString());
			result.append("\n");
			i++;
		}

		return result.toString();
	}
}
