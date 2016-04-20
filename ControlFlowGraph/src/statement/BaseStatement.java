package statement;

import position.Position;

public class BaseStatement {

	protected int level;
	protected Position content;
	
	public BaseStatement(Position content){
		this.content = content;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Position getContent() {
		return content;
	}

	public void setContent(Position content) {
		this.content = content;
	}
	
}
