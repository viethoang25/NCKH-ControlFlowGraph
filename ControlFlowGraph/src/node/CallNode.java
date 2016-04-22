package node;

import position.Position;

public class CallNode extends BaseNode {

	protected int callId;
	
	public CallNode(int index, String content, Position position) {
		super(index, content, position);
	}

	public int getCallId() {
		return callId;
	}

	public void setCallId(int callId) {
		this.callId = callId;
	}
	
}
