import java.util.List;

import edge.BaseEdge;
import file.FileManager;

public class InitGraph {
	private CfgTree mCfgTree = new CfgTree(FileManager.getInstance().getData());
	private List<BaseEdge> mainTree = mCfgTree.getMainTree();
	private int graph[][] = new int[10000][10000];
	public InitGraph(){
		for (BaseEdge e : mainTree){
			graph[e.getSource().getIndex()][e.getDestination().getIndex()] = 1;
		}
	}
	public int[][] getGraph(){
		return graph;
	}
}
