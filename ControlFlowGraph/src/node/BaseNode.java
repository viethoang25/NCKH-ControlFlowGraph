package node;

import position.Position;

public class BaseNode {

	protected int index;
	protected int functionId;
	protected String content;
	// Position of Content
	protected Position position;
	
	public BaseNode() {
		
	}

	public BaseNode(int index, String content, Position position) {
		this.index = index;
		this.content = content;
		this.position = position;
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getFunctionId() {
		return functionId;
	}

	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
}
