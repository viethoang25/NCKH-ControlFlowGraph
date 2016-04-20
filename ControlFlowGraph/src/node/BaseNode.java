package node;

import position.Position;

public class BaseNode {

	protected int index;
	protected int functionId;
	protected String content;
	// Position of all Content
	protected Position position;

	protected int parentId;
	protected boolean isEnd;

	public BaseNode() {
		this.isEnd = false;
	}

	public BaseNode(int index, String content, Position position) {
		this.index = index;
		this.content = content;
		this.position = position;
		this.isEnd = false;
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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

}
