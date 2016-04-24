package coverage;

public class DFS {
	private int graph[][] = new int[10000][10000];
	private boolean[] free = new boolean[10000];
	private int n;
	private int path[] = new int[10000];
	public DFS(int sizeArray){
		this.n = sizeArray;
		for (int i=1; i<=this.n; i++){
			free[i] = true;
			for (int j=1; j<=this.n; j++){
				graph[i][j] = 0;
			}
		}
	}
	public void setGraph(int i, int j){
		graph[i][j] = 1;
	}
	public void doDFS(int v){
		if (free[v] == true){
			free[v] = false;
			for (int u=1; u<=n; u++){
				if (free[u] == true && graph[v][u] == 1){
					path[u] = v;
					doDFS(u);
				}
			}
		}
	}
	public void doStart(int startVertex, int endVertex){
		int v = endVertex;
		doDFS(startVertex);
		while (v != startVertex){
			System.out.print(v+" <- ");
			v = path[v];
		}
		System.out.println();
	}
}
