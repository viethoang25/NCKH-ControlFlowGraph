package node;

import java.util.ArrayList;

import position.Position;

public class IfNode extends BaseNode {

	protected Position truePosition;
	protected Position falsePosition;
	
	public IfNode() {
		super();
	}
	
	public IfNode(int index, String content, Position position) {
		super(index, content, position);
		this.truePosition = null;
		this.falsePosition = null;
	}
	
	public IfNode clone() {
		IfNode temp = new IfNode();
		temp.index = index;
		temp.functionId = functionId;
		temp.content = new String(content);
		temp.position = new Position(position);
		temp.parentId = parentId;
		temp.isEnd = isEnd;
		temp.truePosition = new Position(truePosition);
		temp.falsePosition = new Position(falsePosition);
		return temp;
	}

	public Position getTruePosition() {
		return truePosition;
	}

	public void setTruePosition(Position truePosition) {
		this.truePosition = truePosition;
	}

	public Position getFalsePosition() {
		return falsePosition;
	}

	public void setFalsePosition(Position falsePosition) {
		this.falsePosition = falsePosition;
	}
	
}
