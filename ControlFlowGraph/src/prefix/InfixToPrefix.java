package prefix;

import java.util.List;
import java.util.ArrayList;

public class InfixToPrefix {
	private List<String> infix;
	private List<String> prefix;
	private String theRealPrefix;
	public InfixToPrefix(){
		this.infix = new ArrayList<>();
		prefix = new ArrayList<>();
		theRealPrefix = new String();
	}
	
	public void setInfix(String infix){
		String temp = new String();
		Boolean kt = new Boolean(false);
		this.infix = new ArrayList<>();
		this.prefix = new ArrayList<>();
		this.theRealPrefix = new String();
		for (Character c: infix.toCharArray()) if (c != ' ') temp += c;
		infix = new String(temp);
		temp = new String();
		for (Character c: infix.toCharArray()){
			if (c == '(' || c == ')') {
				if (temp.equals("") == true) this.infix.add(c.toString());
				else{
					this.infix.add(temp);
					this.infix.add(c.toString());
					kt = !kt;
					temp = new String();
				}
			}
			else{
				if (isOperator(c.toString()) == kt) {
					temp += c;
				}else {
					if (temp.equals("") == false) {
						this.infix.add(temp);
						temp = new String(c.toString());
						kt = !kt;
					}
				}
			}
			
		}
		if (temp.equals("") == false) this.infix.add(temp);
		temp = new String();
		this.infix = convertStandard(this.infix);
		infixToPrefix();
	}
	
	private void infixToPrefix(){
		String symbol;
		List<String> stack = new ArrayList<>();
		this.infix = reserve(infix);
		for (int i=0; i<infix.size(); i++){
			symbol = infix.get(i);
			if (isOperator(symbol) == false){				
				prefix.add(symbol);	 
			}else{
				if (symbol.equals(")") == true){
					stack.add(symbol); //push
				}else if (symbol.equals("(") == true){
					while (stack.get(stack.size() - 1).equals(")") == false){	
						prefix.add(stack.get(stack.size() - 1)); // pop
						stack.remove(stack.size() - 1);
					}
					if (stack.get(stack.size() - 1).equals(")") == true) {
						stack.remove(stack.size() - 1);
					}
				}else{
					if (stack.isEmpty() == false){
						if (precedenceOfSymbol(symbol) <= precedenceOfSymbol(stack.get(stack.size() - 1))){
							while (precedenceOfSymbol(symbol) <= precedenceOfSymbol(stack.get(stack.size() - 1))){
								prefix.add(stack.get(stack.size() - 1)); //pop
								stack.remove(stack.size() - 1);	
								if (stack.isEmpty() == true) break;
							}							
							stack.add(symbol); //push
						}else{						
							stack.add(symbol); //push			
						}
					}else{
						stack.add(symbol); //push
					}
				}
			}
		}
		while (stack.isEmpty() == false){
			prefix.add(stack.get(stack.size() - 1)); //pop
			stack.remove(stack.size() -1);
		}
		this.prefix = setParentheses(prefix);
		this.prefix = reserve(prefix);
		this.prefix = checkArrayInPrefix(this.prefix);
		for (int i=0; i<this.prefix.size(); i++){
			if (this.prefix.get(i).equals("|") == true){
				this.theRealPrefix += "or ";
			}else if (this.prefix.get(i).equals("&") == true){
				this.theRealPrefix += "and ";
			}else if (this.prefix.get(i).equals("/")){
				this.theRealPrefix += "div ";
			}else if (this.prefix.get(i).equals("%")){
				this.theRealPrefix += "mod ";
			}else if (this.prefix.get(i).equals("!")){
				this.theRealPrefix += "not ";
			}else{
				this.theRealPrefix += this.prefix.get(i) + " ";
			}
		}
		System.out.println(this.theRealPrefix);
	}
	
	private List<String> checkArrayInPrefix(List<String> prefix) {
		Boolean checkArr = new Boolean(false);
		for (int i=0; i<prefix.size(); i++) {
			for (Character j : prefix.get(i).toCharArray()) {
				if (j == '_') {
					checkArr = true;
				}
			}
			if (checkArr == true) {
				prefix.set(i, new String("("+prefix.get(i).replaceAll("_", " ")+")"));
				checkArr = false;
			}
		}
		return prefix;
	}
	
	private List<String> setParentheses(List<String> arr){
		int j = 0;
		int i = 0;
		int countOperand;
		int countParentheses;
		while (i < arr.size()){
			if (isOperator(arr.get(i)) == true){
				
				countOperand = 0;
				j = i - 1;
				countParentheses = 0;
				while (true){
					if (arr.get(j).equals("(") == true) countParentheses++;
					else if (arr.get(j).equals(")") == true) {
						countParentheses--;
						if (countParentheses == 0) countOperand++;
					}
					if (isOperator(arr.get(j)) == false && countParentheses == 0) countOperand++;
					if (countOperand == 2){
						if (arr.get(i).equals("!") == false){
							arr.add(i+1, "(");
							arr.add(j, ")");
							i+=3;
							break;
						}else{
							arr.add(i+1, "(");
							arr.add(i, "(");
							arr.add(i, "=");
							arr.add(j,")");
							arr.add(j,")");
							i+=6;
							break;
						}
					}else{
						j--;
					}
					
				}
			}else{
				i++;
			}
		}
		return arr;
	}
	
	private int precedenceOfSymbol(String symbol){
		switch(symbol){
		case "+":
		case "-":
			return 6;
		case "*":
		case "/":
		case "%":
			return 8;
		case "^":
		case "$":
			return 10;
		case "=":
		case "!":
		case ">":
		case "<":
		case ">=":
		case "<=":
			return 4;
		case "&":
		case "|":
			return 1;
		case "#":
		case "(":
		case ")":
			return 0;
		default:
			return 0;
		}
	}
	
	private boolean isOperator(String symbol){
		switch (symbol){
		case "+":
		case "-":
		case "*":
		case "/":
		case "^":
		case "$":
		case "(":
		case ")":
		case "=":
		case ">":
		case "<":
		case "!":
		case "&":
		case "|":
		case "%":
		case ">=":
		case "<=":
			return true;
		default:
			return false;
		}
	}
	
	private List<String> reserve(List<String> infix){
		List<String> temp = new ArrayList<>();
		for (int i=infix.size()-1; i>=0; i--){
			temp.add(infix.get(i));
		}
		return temp;
	}
	
	public String getPrefix(){
		return this.theRealPrefix;
	}
	
	private List<String> convertStandard(List<String> arr){
		for (int i=0; i<arr.size()-1; i++){
			if (arr.get(i).equals("==")) arr.set(i, "=");
			else if (arr.get(i).equals("!=")) arr.set(i, "!");
			else if (arr.get(i).equals("&&")) arr.set(i, "&");
			else if (arr.get(i).equals("||")) arr.set(i, "|");
		}
		
		return arr;
	}
	
	
//	public static void main(String[] args) {
//		String str = "(a_0 + 20)%2 != 0";
//		InfixToPrefix a = new InfixToPrefix();
//		a.setInfix(str);
//	}

}
