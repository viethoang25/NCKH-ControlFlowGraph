package function;

import position.Position;

public class FunctionInfor {

	protected String name;
	protected Position content;
	protected Position parameter;

	public FunctionInfor(String name, Position content, Position parameter) {
		this.name = name;
		this.content = content;
		this.parameter = parameter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Position getContent() {
		return content;
	}

	public void setContent(Position content) {
		this.content = content;
	}

	public Position getParameter() {
		return parameter;
	}

	public void setParameter(Position parameter) {
		this.parameter = parameter;
	}

}
