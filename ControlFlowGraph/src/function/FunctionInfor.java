package function;

import position.Position;

public class FunctionInfor {

	protected String name;
	protected Position content;
	
	public FunctionInfor(String name, Position content) {
		this.name = name;
		this.content = content;
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
	
}
