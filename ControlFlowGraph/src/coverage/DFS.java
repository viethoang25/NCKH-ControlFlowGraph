package coverage;

import java.util.ArrayList;
import java.util.List;

import file.FileManager;
import node.BaseNode;

public class DFS {
	private List<List<Boolean>> graph = new ArrayList<>();
	private List<Boolean> free;
	private int n;
	private List<Integer> path;
	private List<List<Integer>> testPath;
	private List<List<BaseNode>> nodeTestPath;
	private List<BaseNode> listBaseNode;
	public DFS(int sizeArray, List<List<Boolean>> graph, List<BaseNode> listBaseNode){
		testPath = new ArrayList<>();
		this.n = sizeArray;
		this.graph = graph;
		free = new ArrayList<>();
		path = new ArrayList<>();
		for (int i=0; i<this.n; i++){
			free.add(true);
			path.add(null);
		}
		this.listBaseNode = listBaseNode;
	}
	public void doDFS(int v, int startVertex, int endVertex){
		if (v != endVertex){
			if (free.get(v) == true){
				free.set(v, false);
				for (int u=0; u<n; u++){
					if (free.get(u) == true && graph.get(v).get(u) == true){
						path.set(u, v);
						doDFS(u, startVertex, endVertex);
					}
				}
			}
		}else{
			printPath(startVertex, endVertex);
		}
	}
	private void printPath(int startVertex, int endVertex){
		int v = endVertex;
		List<Integer> temp = new ArrayList<>();
		while (v != startVertex){
			System.out.print(v+" <- ");
			temp.add(v);
			free.set(v, true);
			v = path.get(v);
		}
		System.out.println(startVertex);
		temp.add(startVertex);
		testPath.add(temp);
	}
	
	public List<List<BaseNode>> getTestPath(){
		nodeTestPath = new ArrayList<>();
		for (int i=0; i<testPath.size(); i++) {
			List<BaseNode> temp = new ArrayList<>();
			nodeTestPath.add(temp);
		}
		for (int i=0; i<testPath.size(); i++){
			for (int j=0; j<testPath.get(i).size(); j++){
				for (BaseNode w : listBaseNode){
					if (w.getIndex() == testPath.get(i).get(j)) {
						nodeTestPath.get(i).add(w);
						break;
					}
				}
			}
		}
		return this.nodeTestPath;
	}
	/*public void running(int startVertex, int endVertex){
		int v = endVertex;
		doDFS(startVertex);
		while (v != startVertex){
			System.out.print(v+" <- ");
			v = path[v];
		}
		System.out.println(startVertex);
	}*/
	
}
