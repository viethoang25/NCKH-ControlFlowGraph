package statement;

import java.util.List;

import position.Position;

public class IterationStatement extends BaseStatement {

	protected List<Position> expression;
	protected Position statement;

	public IterationStatement(Position content, List<Position> expression,
			Position statement) {
		super(content);
		this.expression = expression;
		this.statement = statement;
	}

	public void addExpression(int index, Position element) {
		this.expression.add(index, element);
	}

	public void removeExpression(int index) {
		this.expression.remove(index);
	}

	public List<Position> getExpression() {
		return expression;
	}

	public void setExpression(List<Position> expression) {
		this.expression = expression;
	}

	public Position getStatement() {
		return statement;
	}

	public void setStatement(Position statement) {
		this.statement = statement;
	}
}
