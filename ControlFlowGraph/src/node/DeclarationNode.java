package node;

import position.Position;

public class DeclarationNode extends BaseNode {
	
	public DeclarationNode() {
		super();
	}
	
	public DeclarationNode(int index, String content, Position position) {
		super(index, content, position);
	}
	
	public DeclarationNode clone(){
		DeclarationNode temp = new DeclarationNode();
		temp.index = index;
		temp.functionId = functionId;
		temp.content = new String(content);
		temp.position = new Position(position);
		temp.parentId = parentId;
		temp.isEnd = isEnd;
		return temp;
	}

}
