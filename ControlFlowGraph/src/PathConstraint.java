import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import prefix.InfixToPrefix;
import edge.BaseEdge;
import file.FileManager;
import variable.VariableInfor;
import node.BaseNode;
import node.DeclarationNode;
import node.DoNode;
import node.ExpressionNode;
import node.ForNode;
import node.FunctionNode;
import node.IfNode;
import node.WhileNode;

public class PathConstraint {

	protected String source;
	protected List<BaseNode> nodeList;
	protected List<VariableInfor> allVariableList;
	protected HashMap<String, String> variableList;
	protected List<String> expressionList;
	protected StringBuilder z3Input;

	public PathConstraint(List<BaseNode> nodeList, List<BaseEdge> edgeList) {
		this.nodeList = new ArrayList<>();
		for (int i = nodeList.size() - 1; i >= 0; i--) {
			this.nodeList.add(nodeList.get(i).clone());
		}

		for (BaseEdge e : edgeList) {
			if (e.getLabel() == Constants.LABEL_FALSE) {
				for (int i = 0; i < this.nodeList.size() - 1; i++) {
					BaseNode node = this.nodeList.get(i);
					BaseNode des = this.nodeList.get(i + 1);
					if (node.getIndex() == e.getSource().getIndex()
							&& des.getIndex() == e.getDestination().getIndex()) {
						// Update expression content
						node.setContent("! (" + node.getContent() + ")");
					}
				}
			}
		}
		init();
		createVariable();
		createInput();
	}

	private void init() {
		this.source = FileManager.getInstance().getData();
		SyntaxManager syntaxManager = new SyntaxManager(source);
		allVariableList = syntaxManager.getVariableList();
		variableList = new HashMap<>();
		expressionList = new ArrayList<>();
		z3Input = new StringBuilder();
	}

	private void createVariable() {
		// char c = 'a';
		for (VariableInfor v : allVariableList) {
			if (v.isParameter()) {
				// if(!v.isArray())
				// variableList.put(v.getName(), c + "");
				if (!v.isArray())
					variableList.put(v.getName(), v.getName());
			}
			// c++;
		}
		for (BaseNode node : nodeList) {
			if (node instanceof DeclarationNode) {
				for (VariableInfor v : allVariableList) {
					if (node.getPosition().cover(v.getPosition().start)) {
						String value = v.getInitializer();
						variableList.put(v.getName(), v.getInitializer());
					}
				}
			} else if (node instanceof ExpressionNode) {
				String exp = node.getContent();
				exp = exp.replaceAll(";", "");
				exp = exp.replaceAll("\\s+", "");
				if (!exp.contains("="))
					continue;
				String[] element = exp.split("=");
				for (String name : variableList.keySet()) {
					if (contain(element[1], name)) {
						element[1] = element[1].replaceAll(name,
								variableList.get(name));
					}
				}

				if (element[0].contains("[")) {
					// Array
					String arrElement = element[0].replaceAll("\\[", "_")
							.replaceAll("\\]", "");
					variableList.put(arrElement, "(" + element[1] + ")");
				} else {
					// Normal
					variableList.put(element[0], "(" + element[1] + ")");
				}
			} else {
				if (node instanceof IfNode || node instanceof DoNode
						|| node instanceof WhileNode || node instanceof ForNode) {
					String exp = node.getContent();
					for (String name : variableList.keySet()) {
						exp = exp.replaceAll(name, variableList.get(name));
						exp = exp.replaceAll("\\[", "_").replaceAll("\\]", "");
					}
					expressionList.add(exp);
				}
			}
		}
	}

	private void createInput() {
		// char c = 'a';
		for (VariableInfor v : allVariableList) {
			if (v.isParameter()) {
				String type = v.getType() == Constants.TYPE_INT ? "Int"
						: "Real";
				if (!v.isArray()) {
					z3Input.append("(declare-fun " + v.getName() + " () "
							+ type + ")\n");
				} else {
					z3Input.append("(declare-fun " + v.getName() + " (Int) "
							+ type + ")\n");
				}
			}
			// c++;
		}

		for (String exp : expressionList) {
			// System.out.println(exp);
			InfixToPrefix infix = new InfixToPrefix();
			infix.setInfix(exp);
			String temp = infix.getPrefix();
			z3Input.append("(assert " + temp + ")\n");
		}
	}

	private boolean contain(String str, String name) {
		String regex = "[^a-z0-9]*" + name + "[^a-z0-9]*";
		return Pattern.compile(regex).matcher(str).find();
	}

	public void printVariableList() {
		for (String str : variableList.keySet()) {
			System.out.println(str + " --- " + variableList.get(str));
		}
	}

	public void printExpressionList() {

		for (String str : expressionList) {
			InfixToPrefix infix = new InfixToPrefix();
			System.out.println(str);
			infix.setInfix(str);
			System.out.println(infix.getPrefix());
		}
	}

	public String getZ3Input() {
		return z3Input.toString();
	}

	public static void main(String[] args) {
		String a = " a = b + 10;";
		a = a.trim();
		a = a.replaceAll(";", "");
		a = a.replaceAll("\\s+", "");
		String[] s = a.split("=");
		for (String t : s) {
			t.trim();
			System.out.println(t);
		}
	}
}
