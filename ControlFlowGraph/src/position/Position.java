package position;

public class Position {

	public int start;
	public int end;

	public Position(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public Position(Position pos) {
		if (pos != null) {
			this.start = pos.start;
			this.end = pos.end;
		}
	}

	public String toString() {
		return start + ", " + end;
	}

	public boolean cover(int pos) {
		return start <= pos && pos <= end;
	}
}
