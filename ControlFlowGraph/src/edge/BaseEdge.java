package edge;

import node.BaseNode;

public class BaseEdge {

	protected BaseNode source;
	protected BaseNode destination;
	protected int label; 
	
	public BaseEdge() {
	}
	
	public void setNode(BaseNode source, BaseNode destination) {
		this.source = source;
		this.destination = destination;
	}

	public BaseNode getSource() {
		return source;
	}

	public void setSource(BaseNode source) {
		this.source = source;
	}

	public BaseNode getDestination() {
		return destination;
	}

	public void setDestination(BaseNode destination) {
		this.destination = destination;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}
	
}
