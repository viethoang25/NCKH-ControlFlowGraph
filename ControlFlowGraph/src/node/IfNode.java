package node;

import position.Position;

public class IfNode extends BaseNode {

	protected Position truePosition;
	protected Position falsePosition;
	
	public IfNode(int index, String content, Position position) {
		super(index, content, position);
		this.truePosition = null;
		this.falsePosition = null;
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
