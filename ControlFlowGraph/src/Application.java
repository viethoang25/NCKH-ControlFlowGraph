import java.io.File;
import java.util.ArrayList;
import java.util.List;

import node.BaseNode;
import coverage.DFS;
import file.FileManager;

public class Application {

	private FileManager fileManager;
	private CfgTree cfgTree;
	private InitGraph initGraph;
	//private DFS dfs;
	private List<List<BaseNode>> allTestPath;

	public Application(File file, int depth) {
		fileManager = FileManager.getInstance();
		fileManager.readFile(file);

		cfgTree = new CfgTree(fileManager.getData());

		initGraph = new InitGraph();
		DFS dfs = new DFS(initGraph.getSizeArray(), initGraph.getGraph(),
				cfgTree.getNodeList());
		if (cfgTree.getStartLoop() > 0) {
			DFS loopDfs = new DFS(initGraph.getSizeArray(),
					initGraph.getGraph(), cfgTree.getNodeList());
			loopDfs.doDFS(cfgTree.getStartLoop(), cfgTree.getStartLoop(),
					cfgTree.getEndLoop().get(0));
			dfs.setForTestPath(loopDfs.getTestPath(), depth,
					cfgTree.getStartLoop());
		}
		
		allTestPath = new ArrayList<>();
		for(BaseNode n : cfgTree.getEndNode()){
			dfs.doDFS(0, 0, n.getIndex());
			for(List<BaseNode> list : dfs.getTestPath()){
				allTestPath.add(list);
			}
			dfs.setFreePathToFree();
		}
	}

	public Application(String file, int depth) {
		fileManager = FileManager.getInstance();
		fileManager.init(".\\example\\");
		fileManager.readFile(file);

		cfgTree = new CfgTree(fileManager.getData());

		initGraph = new InitGraph();
		DFS dfs = new DFS(initGraph.getSizeArray(), initGraph.getGraph(),
				cfgTree.getNodeList());
		if (cfgTree.getStartLoop() > 0) {
			DFS loopDfs = new DFS(initGraph.getSizeArray(),
					initGraph.getGraph(), cfgTree.getNodeList());
			loopDfs.doDFS(cfgTree.getStartLoop(), cfgTree.getStartLoop(),
					cfgTree.getEndLoop().get(0));
			dfs.setForTestPath(loopDfs.getTestPath(), depth,
					cfgTree.getStartLoop());
		}
		
		for(BaseNode n : cfgTree.getEndNode()){
			dfs.doDFS(0, 0, n.getIndex());
			for(List<BaseNode> list : dfs.getTestPath()){
				allTestPath.add(list);
			}
			dfs.setFreePathToFree();
		}
	}

	public String getResult() {
		StringBuilder result = new StringBuilder();
		result.append("Number of test path : " + allTestPath.size()
				+ "\n");
		int i = 1;
		for (List<BaseNode> list : allTestPath) {
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
