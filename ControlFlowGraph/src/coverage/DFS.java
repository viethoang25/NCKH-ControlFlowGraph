package coverage;

import java.util.ArrayList;
import java.util.List;

public class DFS {
	private List<List<Boolean>> graph = new ArrayList<>();
	private List<Boolean> free;
	private int n;
	private List<Integer> path;
	private List<List<Integer>> testPath = new ArrayList<>();
	public DFS(int sizeArray, List<List<Boolean>> graph){
		this.n = sizeArray;
		this.graph = graph;
		free = new ArrayList<>();
		path = new ArrayList<>();
		for (int i=0; i<this.n; i++){
			free.add(true);
			path.add(null);
		}
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
	
	public List<List<Integer>> getTestPath(){
		return this.testPath;
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
