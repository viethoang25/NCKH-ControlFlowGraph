import java.util.ArrayList;
import java.util.List;

import function.FunctionInfor;
import position.Position;
import statement.BaseStatement;
import statement.IterationStatement;
import statement.SelectionStatement;

public class CPrimaryListener extends CBaseListener {

	private List<FunctionInfor> functionList;
	private List<BaseStatement> statementList;

	public CPrimaryListener() {
		functionList = new ArrayList<>();
		statementList = new ArrayList<>();
	}

	public List<FunctionInfor> getFunctionList() {
		return functionList;
	}

	public List<BaseStatement> getStatementList() {
		return statementList;
	}

	@Override
	public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
		CParser.DirectDeclaratorContext ddc = ctx.declarator()
				.directDeclarator().directDeclarator();
		if (ddc != null) {
			String name = ddc.getText();
			Position content = null;
			CParser.BlockItemListContext bilc = ctx.compoundStatement()
					.blockItemList();
			if (bilc != null) {
				int start = bilc.start.getStartIndex();
				int end = bilc.stop.getStopIndex();
				content = new Position(start, end);
			}
			functionList.add(new FunctionInfor(name, content));
		}

	}

	@Override
	public void enterBlockItem(CParser.BlockItemContext ctx) {
		if (ctx != null) {
			int start = ctx.start.getStartIndex();
			int end = ctx.stop.getStopIndex();
			Position content = new Position(start, end);

			if (ctx.statement() != null)
				if (ctx.statement().selectionStatement() != null
						|| ctx.statement().iterationStatement() != null) {
					return;
				}
			this.statementList.add(new BaseStatement(content));
		}
	}

	@Override
	public void enterSelectionStatement(CParser.SelectionStatementContext ctx) {
		if (ctx != null) {
			// Get content
			int start = ctx.start.getStartIndex();
			int end = ctx.stop.getStopIndex();
			Position content = new Position(start, end);

			// Get expression
			start = ctx.expression().start.getStartIndex();
			end = ctx.expression().stop.getStopIndex();
			Position expression = new Position(start, end);

			// Get list sub statement
			List<Position> statement = new ArrayList<>();
			List<CParser.StatementContext> scList = ctx.statement();
			for (CParser.StatementContext sc : scList) {
				start = sc.start.getStartIndex();
				end = sc.stop.getStopIndex();
				statement.add(new Position(start, end));
			}

			// Add list primary statement
			this.statementList.add(new SelectionStatement(content, expression,
					statement));
		}
	}

	@Override
	public void enterIterationStatement(CParser.IterationStatementContext ctx) {
		if (ctx != null) {
			// Get content
			int start = ctx.start.getStartIndex();
			int end = ctx.stop.getStopIndex();
			Position content = new Position(start, end);

			// Get list expression
			List<CParser.ExpressionContext> ecList = ctx.expression();
			List<Position> expression = new ArrayList<>();
			for (CParser.ExpressionContext ec : ecList) {
				start = ec.start.getStartIndex();
				end = ec.stop.getStopIndex();
				expression.add(new Position(start, end));
			}

			// Get sub statement
			start = ctx.statement().start.getStartIndex();
			end = ctx.statement().stop.getStopIndex();
			Position statement = new Position(start, end);

			// Add list primary statement
			this.statementList.add(new IterationStatement(content, expression,
					statement));
		}
	}
}
