package variable;

import position.Position;

public class VariableInfor {

	protected String name;
	// Name position
	protected Position position;
	protected String initializer;
	protected int type;
	protected boolean isArray;
	protected boolean isParameter;

	public VariableInfor() {

	}

	public VariableInfor(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInitializer() {
		return initializer;
	}

	public void setInitializer(String initializer) {
		this.initializer = initializer;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String toString() {
		String str;
		str = "Name : " + name 
				+ "\nType : " + type 
				+ "\nInitializer : " + initializer
				+ "\nArray : " + isArray
				+ "\nParameter : " + isParameter
				+ "\n";
		return str;
	}

	public boolean isParameter() {
		return isParameter;
	}

	public void setParameter(boolean isParameter) {
		this.isParameter = isParameter;
	}

}
