package node;

import position.Position;

public class WhileNode extends BaseNode {

	protected Position statementPosition;
	protected Position expressionPosition;
	
	public WhileNode() {
		super();
	}
	
	public WhileNode(int index, String content, Position position) {
		super(index, content, position);
	}
	
	public WhileNode clone() {
		WhileNode temp = new WhileNode();
		temp.index = index;
		temp.functionId = functionId;
		temp.content = new String(content);
		temp.position = new Position(position);
		temp.parentId = parentId;
		temp.isEnd = isEnd;
		temp.statementPosition = new Position(statementPosition);
		temp.expressionPosition = new Position(expressionPosition);
		return temp;
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
