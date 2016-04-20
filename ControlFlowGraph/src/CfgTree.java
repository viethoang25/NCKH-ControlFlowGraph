import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import position.Position;
import node.BaseNode;
import statement.BaseStatement;
import statement.IterationStatement;
import statement.SelectionStatement;
import function.FunctionInfor;

public class CfgTree {

	protected String sourceCode;
	protected List<FunctionInfor> functionList;
	protected List<BaseStatement> statementList;
	protected List<BaseNode> nodeList;

	public CfgTree(String sourceCode) {
		this.sourceCode = sourceCode;
		init();
		createNode();
		sortAscNode();
	}

	private void init() {
		SyntaxManager syntaxManager = new SyntaxManager(sourceCode);
		this.functionList = syntaxManager.getFunctionList();
		this.statementList = syntaxManager.getStatementList();
		nodeList = new ArrayList<>();
	}

	public void createNode() {
		int index = 0;
		for (FunctionInfor f : functionList) {
			nodeList.add(new BaseNode(index, f.getName(), f.getContent()));
			// f.getContent cho nay chua hop ly can sua lai
			index++;
		}
		for (BaseStatement s : statementList) {
			if (s instanceof SelectionStatement) {
				Position exp = ((SelectionStatement) s).getExpression();
				nodeList.add(new BaseNode(index, getSourceAt(exp), exp));
			} else if (s instanceof IterationStatement) {
				StringBuilder expContent = new StringBuilder();
				Position pos = new Position(-1, -1);
				for (Position exp : ((IterationStatement) s).getExpression()) {
					expContent.append(getSourceAt(exp));
					expContent.append(" ");
					if (pos.start == -1)
						pos.start = exp.start;
					pos.end = exp.end;
				}
				nodeList.add(new BaseNode(index, expContent.toString(), pos));
			} else {
				nodeList.add(new BaseNode(index, getSourceAt(s.getContent()), s.getContent()));
			}
			index++;
		}
	}

	private String getSourceAt(Position position) {
		return this.sourceCode.substring(position.start, position.end + 1);
	}

	private void sortAscNode(){
		Comparator<BaseNode> comparator = new Comparator<BaseNode>() {
			@Override
			public int compare(BaseNode o1, BaseNode o2) {
				Integer left = o1.getPosition().start;
				Integer right = o2.getPosition().start;
				return left.compareTo(right);
			}
		};
		Collections.sort(this.nodeList, comparator);
	}

	public void printNodeList() {
		for (BaseNode n : nodeList) {
			System.out.println(n.getIndex() + "\n" + n.getContent());
		}
	}
}
