package node;

import java.util.ArrayList;
import java.util.List;

import position.Position;

public class ForNode extends BaseNode {

	protected Position statementPosition;
	protected List<Position> expressionPosition;

	public ForNode() {
		super();
	}

	public ForNode(int index, String content, Position position) {
		super(index, content, position);
		expressionPosition = new ArrayList<Position>();
	}

	public ForNode clone() {
		ForNode temp = new ForNode();
		temp.index = index;
		temp.functionId = functionId;
		temp.content = new String(content);
		temp.position = new Position(position);
		temp.parentId = parentId;
		temp.isEnd = isEnd;
		temp.statementPosition = new Position(statementPosition);
		temp.expressionPosition = new ArrayList<>();
		for (Position p : expressionPosition)
			temp.expressionPosition.add(new Position(p));
		return temp;
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
