import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import position.Position;
import node.BaseNode;
import node.BreakNode;
import node.CallNode;
import node.DeclarationNode;
import node.DoNode;
import node.ExpressionNode;
import node.ForNode;
import node.FunctionNode;
import node.IfNode;
import node.ReturnNode;
import node.WhileNode;
import statement.BaseStatement;
import statement.IterationStatement;
import statement.SelectionStatement;
import edge.BaseEdge;
import function.FunctionInfor;

public class CfgTree {

	protected String sourceCode;
	protected List<FunctionInfor> functionList;
	protected List<BaseStatement> statementList;
	protected List<BaseNode> nodeList;
	protected List<BaseEdge> edgeList;
	protected List<BaseEdge> mainTree;

	public CfgTree(String sourceCode) {
		this.sourceCode = sourceCode;
		init();
		createNode();
		setFuctionForNode();
		setParentForNode();
		// sortAscNode();
		createEdge();
		createMainTree();
	}

	private void init() {
		SyntaxManager syntaxManager = new SyntaxManager(sourceCode);
		this.functionList = syntaxManager.getFunctionList();
		this.statementList = syntaxManager.getStatementList();
		nodeList = new ArrayList<>();
		edgeList = new ArrayList<>();
		mainTree = new ArrayList<>();
	}

	public void createNode() {
		int index = 0;
		for (FunctionInfor f : functionList) {
			nodeList.add(new FunctionNode(index, f.getName(), f.getContent()));
			index++;
		}
		for (BaseStatement s : statementList) {
			if (s instanceof SelectionStatement) {
				Position exp = ((SelectionStatement) s).getExpression();
				if (s.getType() == Constants.STRUCTURE_IF) {
					IfNode node = new IfNode(index, getSourceAt(exp),
							s.getContent());
					List<Position> state = ((SelectionStatement) s)
							.getStatement();
					if (state.size() == 1) {
						node.setTruePosition(state.get(0));
					} else if (state.size() == 2) {
						node.setTruePosition(state.get(0));
						node.setFalsePosition(state.get(1));
					}
					nodeList.add(node);
				}
			} else if (s instanceof IterationStatement) {
				StringBuilder expContent = new StringBuilder();
				List<Position> expList = ((IterationStatement) s)
						.getExpression();
				Position state = ((IterationStatement) s).getStatement();
				for (Position exp : expList) {
					expContent.append(getSourceAt(exp));
					expContent.append(" ");
				}
				if (s.getType() == Constants.STRUCTURE_DO) {
					DoNode node = new DoNode(index, expContent.toString(),
							s.getContent());
					// Do-while have one expression
					node.setExpressionPosition(expList.get(0));
					node.setStatementPosition(state);
					nodeList.add(node);
				} else if (s.getType() == Constants.STRUCTURE_WHILE) {
					WhileNode node = new WhileNode(index,
							expContent.toString(), s.getContent());
					node.setExpressionPosition(expList.get(0));
					node.setStatementPosition(state);
					nodeList.add(node);
				} else if (s.getType() == Constants.STRUCTURE_FOR) {
					// Just check the expression
					ForNode node = new ForNode(index, getSourceAt(expList.get(1)),
							s.getContent());
					/*ForNode node = new ForNode(index, expContent.toString(),
							s.getContent());*/
					node.setExpressionPosition(expList);
					node.setStatementPosition(state);
					nodeList.add(node);
				}
			} else {
				String content = getSourceAt(s.getContent());
				int callIndex = -1;
				for (int i = 0; i < functionList.size(); i++) {
					BaseNode n = nodeList.get(i);
					if (content.contains(n.getContent())) {
						callIndex = i;
						break;
					}
				}
				if (callIndex != -1) {
					// Create function call node
					CallNode callNode = new CallNode(index, content,
							s.getContent());
					callNode.setCallId(callIndex);
					nodeList.add(callNode);
				} else {
					if (s.getType() == Constants.STRUCTURE_DECLARATION) {
						nodeList.add(new DeclarationNode(index, getSourceAt(s
								.getContent()), s.getContent()));
					} else if (s.getType() == Constants.STRUCTURE_EXPRESSION) {
						nodeList.add(new ExpressionNode(index, getSourceAt(s
								.getContent()), s.getContent()));
					} else if (s.getType() == Constants.STRUCTURE_RETURN) {
						nodeList.add(new ReturnNode(index, getSourceAt(s
								.getContent()), s.getContent()));
					} else if (s.getType() == Constants.STRUCTURE_BREAK) {
						nodeList.add(new BreakNode(index, getSourceAt(s
								.getContent()), s.getContent()));
					} else {
						// Create normal node
						nodeList.add(new BaseNode(index, getSourceAt(s
								.getContent()), s.getContent()));
					}
				}
			}
			index++;
		}
	}

	public void createEdge() {
		// Create edge for Function node
		for (int i = 0; i < functionList.size(); i++) {
			BaseNode node = nodeList.get(i);
			int j = 0;
			for (j = 0; i < nodeList.size(); j++) {
				if (nodeList.get(j) != node
						&& nodeList.get(j).getFunctionId() == node.getIndex())
					break;
			}
			BaseEdge edge = new BaseEdge();
			edge.setNode(node, nodeList.get(j));
			edgeList.add(edge);
		}

		// Create edge for another node
		for (int i = 0; i < nodeList.size(); i++) {

			/*
			 * if (i + 1 == nodeList.size() || nodeList.get(i).getFunctionId()
			 * != nodeList.get(i + 1) .getFunctionId()) continue;
			 */

			BaseNode node = nodeList.get(i);
			if (node instanceof FunctionNode) {
				continue;
			}
			if (node instanceof IfNode) {
				BaseEdge edge;
				// Create edge true
				int trueId = -1;
				for (int j = 0; j < nodeList.size(); j++) {
					int startPos = nodeList.get(j).getPosition().start;
					if (((IfNode) node).getTruePosition().cover(startPos)) {
						trueId = j;
						break;
					}
				}
				if (trueId != -1) {
					edge = new BaseEdge();
					edge.setNode(node, nodeList.get(trueId));
					edge.setLabel(Constants.LABEL_TRUE);
					edgeList.add(edge);
				}

				// Create edge false
				Position fp = ((IfNode) node).getFalsePosition();
				if (fp == null && node.isEnd()) {
					edge = getEdgeAtEndNode(node);
					edge.setLabel(Constants.LABEL_FALSE);
					edgeList.add(edge);
					continue;
				}
				
				int falseId = -1;
				if (fp != null)
					for (int j = 0; j < nodeList.size(); j++) {
						int startPos = nodeList.get(j).getPosition().start;
						if (fp.cover(startPos)) {
							falseId = j;
							break;
						}
					}
				else {
					int minPos = 10000;
					for (int j = 0; j < nodeList.size(); j++) {
						int startPos = nodeList.get(j).getPosition().start;
						Position pos = node.getPosition();
						if (pos.end < startPos && startPos < minPos) {
							falseId = j;
							minPos = startPos;
						}
					}
				}
				if (falseId != -1) {
					edge = new BaseEdge();
					edge.setNode(node, nodeList.get(falseId));
					edge.setLabel(Constants.LABEL_FALSE);
					edgeList.add(edge);
				}

			} else if (node instanceof DoNode) {
				BaseEdge edge;
				// Create edge true
				int trueId = -1;
				for (int j = 0; j < nodeList.size(); j++) {
					int startPos = nodeList.get(j).getPosition().start;
					if (((DoNode) node).getStatementPosition().cover(startPos)) {
						trueId = j;
						break;
					}
				}
				if (trueId != -1) {
					edge = new BaseEdge();
					edge.setNode(node, nodeList.get(trueId));
					edge.setLabel(Constants.LABEL_TRUE);
					edgeList.add(edge);
				}

				// Create edge false
				if (node.isEnd()) {
					edge = getEdgeAtEndNode(node);
					edge.setLabel(Constants.LABEL_FALSE);
					edgeList.add(edge);
					continue;
				}
				int falseId = -1;
				int minPos = 10000;
				for (int j = 0; j < nodeList.size(); j++) {
					int startPos = nodeList.get(j).getPosition().start;
					Position pos = ((DoNode) node).getStatementPosition();
					if (pos.end < startPos && startPos < minPos) {
						falseId = j;
						minPos = startPos;
					}
				}
				if (falseId != -1) {
					edge = new BaseEdge();
					edge.setNode(node, nodeList.get(falseId));
					edge.setLabel(Constants.LABEL_FALSE);
					edgeList.add(edge);
				}
			} else if (node instanceof WhileNode) {
				BaseEdge edge;
				// Create edge true
				int trueId = -1;
				for (int j = 0; j < nodeList.size(); j++) {
					int startPos = nodeList.get(j).getPosition().start;
					if (((WhileNode) node).getStatementPosition().cover(
							startPos)) {
						trueId = j;
						break;
					}
				}
				if (trueId != -1) {
					edge = new BaseEdge();
					edge.setNode(node, nodeList.get(trueId));
					edge.setLabel(Constants.LABEL_TRUE);
					edgeList.add(edge);
				}

				// Create edge false
				if (node.isEnd()) {
					edge = getEdgeAtEndNode(node);
					edge.setLabel(Constants.LABEL_FALSE);
					edgeList.add(edge);
					continue;
				}
				int falseId = -1;
				int minPos = 10000;
				for (int j = 0; j < nodeList.size(); j++) {
					int startPos = nodeList.get(j).getPosition().start;
					Position pos = ((WhileNode) node).getStatementPosition();
					if (pos.end < startPos && startPos < minPos) {
						falseId = j;
						minPos = startPos;
					}
				}
				if (falseId != -1) {
					edge = new BaseEdge();
					edge.setNode(node, nodeList.get(falseId));
					edge.setLabel(Constants.LABEL_FALSE);
					edgeList.add(edge);
				}

			} else if (node instanceof ForNode) {
				BaseEdge edge;
				// Create edge true
				int trueId = -1;
				for (int j = 0; j < nodeList.size(); j++) {
					int startPos = nodeList.get(j).getPosition().start;
					if (((ForNode) node).getStatementPosition().cover(startPos)) {
						trueId = j;
						break;
					}
				}
				if (trueId != -1) {
					edge = new BaseEdge();
					edge.setNode(node, nodeList.get(trueId));
					edge.setLabel(Constants.LABEL_TRUE);
					edgeList.add(edge);
				}

				// Create edge false
				if (node.isEnd()) {
					edge = getEdgeAtEndNode(node);
					edge.setLabel(Constants.LABEL_FALSE);
					edgeList.add(edge);
					continue;
				}
				int falseId = -1;
				int minPos = 10000;
				for (int j = 0; j < nodeList.size(); j++) {
					int startPos = nodeList.get(j).getPosition().start;
					Position pos = ((ForNode) node).getStatementPosition();
					if (pos.end < startPos && startPos < minPos) {
						falseId = j;
						minPos = startPos;
					}
				}
				if (falseId != -1) {
					edge = new BaseEdge();
					edge.setNode(node, nodeList.get(falseId));
					edge.setLabel(Constants.LABEL_FALSE);
					edgeList.add(edge);
				}
			} else if (node instanceof ReturnNode) {
				BaseEdge edge = new BaseEdge();
				BaseNode functionNode = nodeList.get(node.getFunctionId());
				edge.setNode(node, functionNode);
				edgeList.add(edge);
			} else {
				if (node.isEnd()) {
					BaseEdge edge = getEdgeAtEndNode(node);
					edgeList.add(edge);
					/*
					 * BaseNode parentNode = nodeList.get(node.getParentId());
					 * if (parentNode instanceof IfNode) { int nodeId = -1; int
					 * minPos = 10000; for (int j = 0; j < nodeList.size(); j++)
					 * { int startPos = nodeList.get(j).getPosition().start;
					 * Position pos = parentNode.getPosition(); if (pos.end <
					 * startPos && startPos < minPos) { nodeId = j; minPos =
					 * startPos; } } if (nodeId != -1) { BaseEdge edge = new
					 * BaseEdge(); edge.setNode(node, nodeList.get(nodeId));
					 * edgeList.add(edge); } } else if (parentNode instanceof
					 * WhileNode) { BaseEdge edge = new BaseEdge();
					 * edge.setNode(node, parentNode); edgeList.add(edge); }
					 * else if (parentNode instanceof ForNode) { BaseEdge edge =
					 * new BaseEdge(); edge.setNode(node, parentNode);
					 * edgeList.add(edge); }
					 */
				} else if (nodeList.get(i + 1) instanceof DoNode) {
					BaseEdge edge = new BaseEdge();
					edge.setNode(node, nodeList.get(i + 2));
					edgeList.add(edge);
				} else {
					BaseEdge edge = new BaseEdge();
					edge.setNode(node, nodeList.get(i + 1));
					edgeList.add(edge);
				}
			}
		}
	}
	
	private BaseEdge getEdgeAtEndNode(BaseNode node) {
		BaseEdge edge = null;
		BaseNode parentNode = nodeList.get(node.getParentId());
		while(parentNode.isEnd()){
			parentNode = nodeList.get(parentNode.getParentId());
		}
		if (parentNode instanceof IfNode) {
			// check algorithm ?
			/*while(parentNode.isEnd() && nodeList.get(parentNode.getParentId()) instanceof IfNode){
				parentNode = nodeList.get(parentNode.getParentId());
			}*/
			
			int nodeId = -1;
			int minPos = 10000;
			for (int j = 0; j < nodeList.size(); j++) {
				int startPos = nodeList.get(j).getPosition().start;
				Position pos = parentNode.getPosition();
				if (pos.end < startPos && startPos < minPos) {
					nodeId = j;
					minPos = startPos;
				}
			}
			if (nodeId != -1) {
				edge = new BaseEdge();
				edge.setNode(node, nodeList.get(nodeId));
				//edgeList.add(edge);
			}
		} else if (parentNode instanceof WhileNode) {
			
			edge = new BaseEdge();
			edge.setNode(node, parentNode);
			// edgeList.add(edge);
		} else if (parentNode instanceof ForNode) {
			edge = new BaseEdge();
			edge.setNode(node, parentNode);
			// edgeList.add(edge);
		} else {
			// Edge for return node
			edge = new BaseEdge();
			edge.setNode(node, parentNode);
			// Edge node->node+1 : case {}
			/*
			 * edge = new BaseEdge(); edge.setNode(node,
			 * nodeList.get(nodeList.indexOf(edge) + 1));
			 */
		}
		return edge;
	}

	public void createMainTree() {
		// Find main function node id
		int mainId = -1;
		for (BaseNode n : nodeList) {
			if (n.getContent().equals("main")) {
				mainId = n.getIndex();
				break;
			}
		}
		// If don't have main function -> stop
		if (mainId == -1) {
			System.out.println("Don't have main function");
			return;
		}
		// Create basic main tree
		List<BaseEdge> callNodePointList = new ArrayList<>();
		for (BaseEdge e : edgeList) {
			if (e.getSource().getFunctionId() == mainId) {
				if (e.getSource() instanceof CallNode)
					callNodePointList.add(e);
				else
					mainTree.add(e);
			}
		}

		// Update if have function call
		// CHU Y: 1. CAN DOC LAI | 2. Moi chi ap dung cho goi ham bi goi o ham
		// chinh => Can them while de duyet
		List<BaseEdge> callTree = new ArrayList<>();
		for (int i = 0; i < mainTree.size(); i++) {
			BaseEdge e = mainTree.get(i);
			if (e.getDestination() instanceof CallNode) {
				// Get Id of function is called
				int callId = ((CallNode) e.getDestination()).getCallId();
				for (BaseEdge b : edgeList) {
					if (b.getSource().getFunctionId() == callId) {
						callTree.add(b);
					}

					// Another way
					/*
					 * if (b.getSource().getFunctionId() == callId) { if
					 * (b.getSource().getIndex() == callId) { // Replace
					 * e.getDestination() with b.getSource() BaseEdge newEdge =
					 * new BaseEdge(); newEdge.setNode(e.getDestination(),
					 * b.getDestination()); callTree.add(newEdge); } else {
					 * callTree.add(b); } }
					 */
				}
				// Create edge at callNode->function and function-> callNode
				// pointer
				// CallNode->Function
				BaseEdge newEdge = new BaseEdge();
				newEdge.setNode(e.getDestination(), nodeList.get(callId));
				callTree.add(newEdge);
				// Function -> CallNode Pointer
				for (BaseEdge b : edgeList) {
					if (b.getSource() == e.getDestination()) {
						newEdge = new BaseEdge();
						newEdge.setNode(nodeList.get(callId),
								b.getDestination());
						callTree.add(newEdge);
					}
				}
			}
		}
		mainTree.addAll(callTree);

	}

	public void setFuctionForNode() {
		// Function node have id from 0->functionList's size
		for (int i = 0; i < functionList.size(); i++) {
			Position funcNode = nodeList.get(i).getPosition();
			for (BaseNode node : nodeList) {
				int startPos = node.getPosition().start;
				if (funcNode.cover(startPos))
					node.setFunctionId(i);
			}
		}
	}

	public void setParentForNode() {
		for (BaseNode n : nodeList) {
			Position parentPos = n.getPosition();
			for (BaseNode b : nodeList) {
				Position childPos = b.getPosition();
				if (b != n && parentPos.cover(childPos.start)) {
					b.setParentId(n.getIndex());
				}
			}
		}
		for (int i = 0; i < functionList.size(); i++) {
			nodeList.get(i).setParentId(i);
		}
		for (BaseNode n : nodeList) {
			int start = n.getPosition().start;
			BaseNode parentNode = nodeList.get(n.getParentId());
			int end;
			if (parentNode instanceof WhileNode) {
				end = ((WhileNode) parentNode).getStatementPosition().end;
			} else if (parentNode instanceof DoNode) {
				end = ((DoNode) parentNode).getStatementPosition().end;
			} else if (parentNode instanceof ForNode) {
				end = ((ForNode) parentNode).getStatementPosition().end;
			} else if (parentNode instanceof IfNode) {
				end = ((IfNode) parentNode).getTruePosition().end;
				if (start > end)
					end = ((IfNode) parentNode).getFalsePosition().end;
			} else {
				end = parentNode.getPosition().end;
			}
			Position checkPos = new Position(start, end);
			boolean isEnd = true;
			for (BaseNode b : nodeList) {
				if (b != n && /*b.getParentId() != n.getIndex()*/ !n.getPosition().cover(b.getPosition().start)
						&& checkPos.cover(b.getPosition().start)) {
					isEnd = false;
					break;
				}
			}
			n.setEnd(isEnd);
		}
	}

	private String getSourceAt(Position position) {
		return this.sourceCode.substring(position.start, position.end + 1);
	}

	private void sortAscNode() {
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
			System.out.println(n.getIndex() + " ----- " + n.isEnd()
					+ " ----- " + n.getClass().getName() + "\n"
					+ n.getContent());
		}
	}

	public void printEdgeList() {
		for (BaseEdge e : edgeList) {
			System.out.println(e.getSource().getIndex() + " ---> "
					+ e.getDestination().getIndex() + " : " + e.getLabel());
		}
	}

	public void printMainTreeList() {
		for (BaseEdge e : mainTree) {
			System.out.println(e.getSource().getIndex() + " ---> "
					+ e.getDestination().getIndex() + " : " + e.getLabel());
		}
	}

	public List<BaseEdge> getMainTree() {
		return mainTree;
	}

	public List<BaseEdge> getEdgeList() {
		return edgeList;
	}

	public List<BaseNode> getNodeList() {
		return nodeList;
	}

}
