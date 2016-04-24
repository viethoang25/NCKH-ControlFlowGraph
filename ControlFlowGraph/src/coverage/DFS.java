package coverage;

public class DFS {
	private int graph[][] = new int[10000][10000];
	private boolean[] free = new boolean[10000];
	private int n;
	private int path[] = new int[10000];
	public DFS(int sizeArray, int graph[][]){
		this.n = sizeArray;
		for (int i=0; i<this.n; i++){
			free[i] = true;
		}
		this.graph = graph;
	}
	public void doDFS(int v, int startVertex, int endVertex){
		if (v != endVertex){
			if (free[v] == true){
				free[v] = false;
				for (int u=0; u<n; u++){
					if (free[u] == true && graph[v][u] == 1){
						path[u] = v;
						doDFS(u, startVertex, endVertex);
					}
				}
			}
		}else{
			printPath(startVertex, endVertex);
		}
	}
	public void printPath(int startVertex, int endVertex){
		int v = endVertex;
		while (v != startVertex){
			System.out.print(v+" <- ");
			v = path[v];
		}
		System.out.println(startVertex);
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
