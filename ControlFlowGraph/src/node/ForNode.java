package node;

import java.util.ArrayList;
import java.util.List;

import position.Position;

public class ForNode extends BaseNode {

	protected Position statementPosition;
	protected List<Position> expressionPosition;
	
	public ForNode(int index, String content, Position position) {
		super(index, content, position);
		expressionPosition = new ArrayList<Position>();
	}

	public Position getStatementPosition() {
		return statementPosition;
	}

	public void setStatementPosition(Position statementPosition) {
		this.statementPosition = statementPosition;
	}

	public List<Position> getExpressionPosition() {
		return expressionPosition;
	}

	public void setExpressionPosition(List<Position> expressionPosition) {
		this.expressionPosition = expressionPosition;
	}
	
}
