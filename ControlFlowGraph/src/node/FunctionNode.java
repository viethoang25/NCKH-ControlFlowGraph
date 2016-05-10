package node;

import position.Position;

public class FunctionNode extends BaseNode {

	public FunctionNode() {

	}

	public FunctionNode(int index, String content, Position position) {
		super(index, content, position);
	}

	public FunctionNode clone() {
		FunctionNode temp = new FunctionNode();
		temp.index = index;
		temp.functionId = functionId;
		temp.content = new String(content);
		temp.position = new Position(position);
		temp.parentId = parentId;
		temp.isEnd = isEnd;
		return temp;
	}
}
