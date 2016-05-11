package coverage;

import java.util.ArrayList;
import java.util.List;
import node.BaseNode;

public class DFS {
	private List<List<Boolean>> graph = new ArrayList<>();
	private List<Boolean> free;
	private int n;
	private List<Integer> path;
	private List<List<Integer>> testPath;
	private List<List<BaseNode>> nodeTestPath;
	private List<BaseNode> listBaseNode;
	private List<List<BaseNode>> listForBaseNode = new ArrayList<>();
	private Integer amountLoop = 0;
	private Integer startLoop = 0;
	private boolean haveLoop = false;
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
	
	private List<List<BaseNode>> nodeTestPath(){
		List<List<BaseNode>> nodeTestPath = new ArrayList<>();
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
		return nodeTestPath;
	}
	
	public List<List<BaseNode>> getTestPath(){
		this.nodeTestPath = nodeTestPath();
		int count = 0;
		int countLoop = 0;
		List<BaseNode> temp;
		int i = 0;
		if (this.haveLoop == true){
			while (i < this.nodeTestPath.size()){
				for (int j=0; j<this.nodeTestPath.get(i).size(); j++) {
					if (this.nodeTestPath.get(i).get(j).getIndex() == this.startLoop){
						while (count < this.listForBaseNode.size()) {
							
							temp = this.nodeTestPath.get(i);
							while (countLoop < this.amountLoop){
								for (int w=this.listForBaseNode.get(count).size()-1; w>=0; w--)
									temp.add(j+1, this.listForBaseNode.get(count).get(w));
								countLoop++;
							}
							countLoop = 0;
							this.nodeTestPath.add(i+1, temp);
							count++;
						}
						this.nodeTestPath.remove(i);
						i += count - 1;
						count = 0;
						break;
					}
				}
				i++;
			}
		}
		return this.nodeTestPath;
	}
	
	public void setForTestPath(List<List<BaseNode>> forNodeTestPath, Integer amountLoop, Integer startLoop) {
		this.listForBaseNode = forNodeTestPath;
		this.amountLoop = amountLoop;
		this.startLoop = startLoop;
		this.haveLoop = true;
	}
	
}
