package node;

import position.Position;

public class WhileNode extends BaseNode {

	protected Position statementPosition;
	protected Position expressionPosition;
	
	public WhileNode(int index, String content, Position position) {
		super(index, content, position);
	}

	public Position getStatementPosition() {
		return statementPosition;
	}

	public void setStatementPosition(Position statementPosition) {
		this.statementPosition = statementPosition;
	}

	public Position getExpressionPosition() {
		return expressionPosition;
	}

	public void setExpressionPosition(Position expressionPosition) {
		this.expressionPosition = expressionPosition;
	}
	
}
