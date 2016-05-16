package coverage;

import java.util.ArrayList;
import java.util.List;

import node.BaseNode;

public class CoverageVertex {
	List<List<BaseNode>> testPath;
	int maxVertex;
	List<Boolean> checkVertex;

	public CoverageVertex(List<List<BaseNode>> testPath, int maxVertex) {
		this.testPath = new ArrayList<>();
		this.testPath = testPath;
		this.maxVertex = maxVertex;
		this.checkVertex = new ArrayList<>();
		for (int i = 0; i <= maxVertex; i++)
			this.checkVertex.add(false);
	}

	public List<List<BaseNode>> getTestPathCoverageVertex() {
		List<List<BaseNode>> testPath = new ArrayList<>();
		for (int i = 0; i < this.testPath.size(); i++) {
			for (BaseNode j : this.testPath.get(i)) {
				if (this.checkVertex.get(j.getIndex()) == false) {
					this.checkVertex.set(j.getIndex(), true);
					this.maxVertex--;
					if (this.maxVertex == -1) {
						return testPath;
					}
				}
			}
			testPath.add(this.testPath.get(i));
		}
		return null;
	}
}
