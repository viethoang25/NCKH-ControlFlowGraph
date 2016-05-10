package node;

import position.Position;

public class ExpressionNode extends BaseNode {

	public ExpressionNode() {

	}

	public ExpressionNode(int index, String content, Position position) {
		super(index, content, position);
	}

	public ExpressionNode clone() {
		ExpressionNode temp = new ExpressionNode();
		temp.index = index;
		temp.functionId = functionId;
		temp.content = new String(content);
		temp.position = new Position(position);
		temp.parentId = parentId;
		temp.isEnd = isEnd;
		return temp;
	}

}
