import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;

import function.FunctionInfor;
import position.Position;
import statement.BaseStatement;
import statement.IterationStatement;
import statement.SelectionStatement;
import variable.VariableInfor;

public class CPrimaryListener extends CBaseListener {

	private List<FunctionInfor> functionList;
	private List<BaseStatement> statementList;
	private List<VariableInfor> variableList;

	public CPrimaryListener() {
		functionList = new ArrayList<>();
		statementList = new ArrayList<>();
		variableList = new ArrayList<>();
	}

	public List<FunctionInfor> getFunctionList() {
		return functionList;
	}

	public List<BaseStatement> getStatementList() {
		return statementList;
	}

	public List<VariableInfor> getVariableList() {
		return variableList;
	}

	@Override
	public void enterFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
		CParser.DirectDeclaratorContext ddc = ctx.declarator()
				.directDeclarator().directDeclarator();
		
		if (ddc != null) {
			String name = ddc.getText();
			// Get function content
			Position content = null;
			CParser.BlockItemListContext bilc = ctx.compoundStatement()
					.blockItemList();
			if (bilc != null) {
				int start = bilc.start.getStartIndex();
				int end = bilc.stop.getStopIndex();
				content = new Position(start, end);
			}
			// Get function parameter
			CParser.ParameterTypeListContext prlc = ctx.declarator().directDeclarator().parameterTypeList();
			Position parameter = null;
			if(prlc != null){
				int start = prlc.start.getStartIndex();
				int end = prlc.stop.getStopIndex();
				parameter = new Position(start, end);
			}
			
			functionList.add(new FunctionInfor(name, content, parameter));
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

	@Override
	public void enterDeclarator(CParser.DeclaratorContext ctx) {
		// variable list don't need Function Definition
		if (ctx.getParent() instanceof CParser.FunctionDefinitionContext)
			return;

		VariableInfor var = new VariableInfor();

		if (ctx.directDeclarator().directDeclarator() == null) {
			// Normal
			int start = ctx.directDeclarator().start.getStartIndex();
			int end = ctx.directDeclarator().stop.getStopIndex();
			var.setName(ctx.directDeclarator().getText());
			var.setPosition(new Position(start, end));
		} else {
			// Array
			int start = ctx.directDeclarator().directDeclarator().start
					.getStartIndex();
			int end = ctx.directDeclarator().directDeclarator().stop
					.getStopIndex();
			var.setArray(true);
			var.setName(ctx.directDeclarator().directDeclarator().getText());
			var.setPosition(new Position(start, end));
		}

		// If declarator have initializer
		if (ctx.getParent() instanceof CParser.InitDeclaratorContext) {
			CParser.InitializerContext initializerContext = ((CParser.InitDeclaratorContext) ctx
					.getParent()).initializer();
			if (initializerContext != null)
				var.setInitializer(initializerContext.getText());
		}

		variableList.add(var);
	}

	@Override
	public void enterTypeName(CParser.TypeNameContext ctx) {
		VariableInfor var = new VariableInfor(ctx.getText());
		int start = ctx.start.getStartIndex();
		int end = ctx.stop.getStopIndex();
		var.setPosition(new Position(start, end));
		variableList.add(var);
	}
}
