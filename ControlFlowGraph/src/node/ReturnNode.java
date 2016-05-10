package node;

import position.Position;

public class ReturnNode extends BaseNode{

	public ReturnNode() {
		super();
	}
	
	public ReturnNode(int index, String content, Position position) {
		super(index, content, position);
	}
	
	public ReturnNode clone() {
		ReturnNode temp = new ReturnNode();
		temp.index = index;
		temp.functionId = functionId;
		temp.content = new String(content);
		temp.position = new Position(position);
		temp.parentId = parentId;
		temp.isEnd = isEnd;
		return temp;
	}
	
}
