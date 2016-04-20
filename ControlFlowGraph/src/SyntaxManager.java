import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import node.BaseNode;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import function.FunctionInfor;
import position.Position;
import statement.BaseStatement;
import statement.IterationStatement;
import statement.SelectionStatement;

public class SyntaxManager {

	protected String source;
	protected List<FunctionInfor> functionList;
	protected List<BaseStatement> statementList;

	public SyntaxManager(String source) {
		this.source = source;
		init();
		splitStatement();
		distinguishStatementType();
		sortAscStatement();
	}

	public void init() {
		CLexer lexer = new CLexer(new ANTLRInputStream(this.source));
		CParser parser = new CParser(new CommonTokenStream(lexer));
		ParseTree tree = parser.compilationUnit();
		CPrimaryListener listener = new CPrimaryListener();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(listener, tree);

		this.functionList = listener.getFunctionList();
		this.statementList = listener.getStatementList();
	}

	public void splitStatement() {
		// Filter elements of the class SelectionStatement
		List<SelectionStatement> selectionList = new ArrayList<>();
		for (BaseStatement state : statementList)
			if (state instanceof SelectionStatement)
				selectionList.add((SelectionStatement) state);
		// Check for SelectionStatment class. Include: switch, if
		for (SelectionStatement state : selectionList) {
			// Get statement. If statement and Else statement
			List<Position> statement = ((SelectionStatement) state)
					.getStatement();
			// With each statement
			for (Position e : statement) {
				if (!isBlock(e))
					this.statementList.add(new BaseStatement(e));
			}
		}

		// Filter elements of the class IterationStatement
		List<IterationStatement> iterationList = new ArrayList<>();
		for (BaseStatement state : statementList)
			if (state instanceof IterationStatement)
				iterationList.add((IterationStatement) state);
		// Check for IterationStatement class. Include: while, do, for
		for (IterationStatement state : iterationList) {
			// Get statement. Just one.
			Position statement = state.getStatement();
			if (!isBlock(statement))
				this.statementList.add(new BaseStatement(statement));
		}
	}

	// Check if "Position" is block. It means "Position" content 1 or more
	// elements in statementList
	private boolean isBlock(Position element) {
		boolean check = false;
		for (BaseStatement s : statementList) {
			if (element.cover(s.getContent().start)) {
				check = true;
				break;
			}
		}
		return check;
	}

	private void sortAscStatement() {
		Comparator<BaseStatement> comparator = new Comparator<BaseStatement>() {
			@Override
			public int compare(BaseStatement s1, BaseStatement s2) {
				Integer left = s1.getContent().start;
				Integer right = s2.getContent().start;
				return left.compareTo(right);
			}
		};
		Collections.sort(this.statementList, comparator);
	}

	// Distinguish statement type
	public void distinguishStatementType() {
		for (BaseStatement s : statementList) {
			String content = getSourceAt(s.getContent());
			if (content.contains("if"))
				s.setType(Constants.STRUCTURE_IF);
			else if (content.contains("switch"))
				s.setType(Constants.STRUCTURE_SWITCH);
			else if (content.contains("do"))
				s.setType(Constants.STRUCTURE_DO);
			else if (content.contains("while"))
				s.setType(Constants.STRUCTURE_WHILE);
			else if (content.contains("for"))
				s.setType(Constants.STRUCTURE_FOR);
		}
	}

	public void printStatementList() {
		for (BaseStatement state : statementList) {
			System.out.println("-------------");
			System.out.println("Type : " + state.getType());
			System.out.println("Content : " + state.getContent() + "\n"
					+ getSourceAt(state.getContent()));
			if (state instanceof SelectionStatement) {
				System.out.println("Expresstion : "
						+ ((SelectionStatement) state).getExpression()
								.toString());
				List<Position> statement = ((SelectionStatement) state)
						.getStatement();
				System.out.println("List Statement");
				for (int i = 0; i < statement.size(); i++) {
					System.out.println("Statement[" + i + "] : "
							+ statement.get(i).toString());
				}
			}
			if (state instanceof IterationStatement) {
				List<Position> expression = ((IterationStatement) state)
						.getExpression();
				System.out.println("List Expresstion");
				for (int i = 0; i < expression.size(); i++) {
					System.out.println("Expression[" + i + "] :"
							+ expression.get(i).toString());
				}
				System.out.println("Statement : "
						+ ((IterationStatement) state).getStatement()
								.toString());
			}
		}
	}

	private String getSourceAt(Position position) {
		return this.source.substring(position.start, position.end + 1);
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<FunctionInfor> getFunctionList() {
		return functionList;
	}

	public void setFunctionList(List<FunctionInfor> functionList) {
		this.functionList = functionList;
	}

	public List<BaseStatement> getStatementList() {
		return statementList;
	}

	public void setStatementList(List<BaseStatement> statementList) {
		this.statementList = statementList;
	}

}
