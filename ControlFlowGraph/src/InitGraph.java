import java.util.ArrayList;
import java.util.List;

import edge.BaseEdge;
import file.FileManager;

public class InitGraph {
	private CfgTree mCfgTree = new CfgTree(FileManager.getInstance().getData());
	private List<BaseEdge> edgeList = mCfgTree.getEdgeList();
	private List<List<Boolean>> graph = new ArrayList<>();
	private List<Boolean> temp;
	public InitGraph(){
		for (int i=0; i<=edgeList.get(edgeList.size()-1).getSource().getIndex(); i++){
			temp = new ArrayList<>();
			graph.add(temp);
		}
		for (int i=0; i<=edgeList.get(edgeList.size()-1).getSource().getIndex(); i++){
			for (int j=0; j<=edgeList.get(edgeList.size()-1).getSource().getIndex(); j++){
				graph.get(i).add(false);
			}
		}
		for (BaseEdge e : edgeList){
			graph.get(e.getSource().getIndex()).set(e.getDestination().getIndex(), true);
		}
	}
	public List<List<Boolean>> getGraph(){
		return graph;
	}
	
	public int getSizeArray(){
		return edgeList.get(edgeList.size()-1).getSource().getIndex()+1;
	}
}
