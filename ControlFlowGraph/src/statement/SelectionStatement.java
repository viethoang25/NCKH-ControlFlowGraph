package statement;

import java.util.List;
import position.Position;

public class SelectionStatement extends BaseStatement {

	protected Position expression;
	protected List<Position> statement;

	public SelectionStatement(Position content, Position expression,
			List<Position> statement) {
		super(content);
		this.expression = expression;
		this.statement = statement;
	}

	public void addStatement(int index, Position element) {
		this.statement.add(index, element);
	}

	public void removeStatement(int index) {
		this.statement.remove(index);
	}

	public Position getExpression() {
		return expression;
	}

	public void setExpression(Position expression) {
		this.expression = expression;
	}

	public List<Position> getStatement() {
		return statement;
	}

	public void setStatement(List<Position> statement) {
		this.statement = statement;
	}

}
