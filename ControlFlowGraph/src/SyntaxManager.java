import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import node.BaseNode;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import function.FunctionInfor;
import position.Position;
import statement.BaseStatement;
import statement.IterationStatement;
import statement.SelectionStatement;
import variable.VariableInfor;

public class SyntaxManager {

	protected String source;
	protected List<FunctionInfor> functionList;
	protected List<BaseStatement> statementList;
	protected List<VariableInfor> variableList;

	public SyntaxManager(String source) {
		this.source = source;
		init();
		splitStatement();
		distinguishStatementType();
		sortAscStatement();
		distinguishVariableType();
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
		this.variableList = listener.getVariableList();
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
			if (s instanceof SelectionStatement) {
				if (content.contains("if"))
					s.setType(Constants.STRUCTURE_IF);
				else if (content.contains("switch"))
					s.setType(Constants.STRUCTURE_SWITCH);
			} else if (s instanceof IterationStatement) {
				if (content.contains("do"))
					s.setType(Constants.STRUCTURE_DO);
				else if (content.contains("while"))
					s.setType(Constants.STRUCTURE_WHILE);
				else if (content.contains("for"))
					s.setType(Constants.STRUCTURE_FOR);
			} else {
				if (getDeclarationType(content) != null)
					s.setType(Constants.STRUCTURE_DECLARATION);
				else if (checkExpressionType(content))
					s.setType(Constants.STRUCTURE_EXPRESSION);
				else if (content.contains("return"))
					s.setType(Constants.STRUCTURE_RETURN);
				else if (content.contains("break"))
					s.setType(Constants.STRUCTURE_BREAK);
			}
		}
	}

	public void distinguishVariableType() {
		for (VariableInfor var : variableList) {
			for (FunctionInfor f : functionList) {
				if (f.getParameter().cover(var.getPosition().start)) {
					// Set is parameter
					var.setParameter(true);
					// Set type
					String str = getSourceAt(f.getParameter());
					String[] paras = str.split(",");
					for (int i = 0; i < paras.length; i++)
						paras[i] = paras[i] + ";";
					for (int i = 0; i < paras.length; i++) {
						String type = getDeclarationType(paras[i]);

						if (type == null)
							continue;
						else if (type.equals("int"))
							var.setType(Constants.TYPE_INT);
						else
							var.setType(Constants.TYPE_REAL);
					}
				}
			}

			for (BaseStatement s : statementList) {
				if (s.getContent().cover(var.getPosition().start)) {
					String type = getDeclarationType(getSourceAt(s.getContent()));

					if (type == null)
						continue;
					else if (type.equals("int"))
						var.setType(Constants.TYPE_INT);
					else
						var.setType(Constants.TYPE_REAL);
				}
			}
		}
	}

	public String getDeclarationType(String str) {
		CLexer lexer = new CLexer(new ANTLRInputStream(str));
		CLexer temp = new CLexer(new ANTLRInputStream(str));
		CParser parser = new CParser(new CommonTokenStream(lexer));
		// parser.removeErrorListeners();
		parser.reset();
		parser.declaration();
		if (parser.getNumberOfSyntaxErrors() > 0)
			return null;
		else {
			Token t = temp.nextToken();
			return t.getText();
		}
	}

	public boolean checkExpressionType(String str) {
		String temp = new String(str);
		if (temp.contains(";")) {
			temp = temp.substring(0, temp.lastIndexOf(';'));
		}
		CLexer lexer = new CLexer(new ANTLRInputStream(temp));
		CParser parser = new CParser(new CommonTokenStream(lexer));
		// parser.removeErrorListeners();
		parser.reset();
		parser.expression();
		if (parser.getNumberOfSyntaxErrors() > 0)
			return false;
		else {
			return true;
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

	public void printVariableList() {
		for (VariableInfor var : variableList) {
			System.out.println("-------------");
			System.out.println(var.toString());
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

	public List<VariableInfor> getVariableList() {
		return variableList;
	}

	public void setVariableList(List<VariableInfor> variableList) {
		this.variableList = variableList;
	}

}
