package prefix;

import java.util.List;
import java.util.ArrayList;

public class InfixToPrefix {
	private List<Character> infix;
	private List<Character> prefix;
	private List<Boolean> check = new ArrayList<>();
	private List<Boolean> checkPrefix = new ArrayList<>();
	private String theRealPrefix;
	public InfixToPrefix(){
		this.infix = new ArrayList<>();
		prefix = new ArrayList<>();
		theRealPrefix = new String();
	}
	
	public void setInfix(String infix){
		for (Character c: infix.toCharArray()){
			if (c != ' '){
				this.infix.add(c);
			}
		}
		for (int i=0; i<this.infix.size(); i++) this.check.add(false);
		this.infix = convertStandard(this.infix);
		infixToPrefix();
	}
	
	private void infixToPrefix(){
		Character symbol;
		List<Character> stack = new ArrayList<>();
		List<Boolean> checkStack = new ArrayList<>();
		this.infix = reserve(infix);
		this.check = reserveCheck(this.check);
		for (int i=0; i<infix.size(); i++){
			symbol = infix.get(i);
			if (isOperator(symbol) == false){				
				prefix.add(symbol);	 
				this.checkPrefix.add(this.check.get(i));
			}else{
				if (symbol == ')'){
					stack.add(symbol); //push
					checkStack.add(this.check.get(i));
				}else if (symbol == '('){
					while (stack.get(stack.size() - 1) != ')'){	
						prefix.add(stack.get(stack.size() - 1)); // pop
						this.checkPrefix.add(checkStack.get(checkStack.size() - 1));
						stack.remove(stack.size() - 1);
						checkStack.remove(checkStack.size() - 1);
					}
					if (stack.get(stack.size() - 1) == ')') {
						stack.remove(stack.size() - 1);
						checkStack.remove(checkStack.size() - 1);
					}
				}else{
					if (stack.isEmpty() == false){
						if (precedenceOfSymbol(symbol) <= precedenceOfSymbol(stack.get(stack.size() - 1))){
							while (precedenceOfSymbol(symbol) <= precedenceOfSymbol(stack.get(stack.size() - 1))){
								prefix.add(stack.get(stack.size() - 1)); //pop
								this.checkPrefix.add(checkStack.get(checkStack.size() - 1));
								stack.remove(stack.size() - 1);	
								checkStack.remove(checkStack.size() - 1);
								if (stack.isEmpty() == true) break;
							}							
							stack.add(symbol); //push
							checkStack.add(this.check.get(i));
						}else{						
							stack.add(symbol); //push			
							checkStack.add(this.check.get(i));
						}
					}else{
						stack.add(symbol); //push
						checkStack.add(this.check.get(i));
					}
				}
			}
		}
		while (stack.isEmpty() == false){
			prefix.add(stack.get(stack.size() - 1)); //pop
			this.checkPrefix.add(checkStack.get(checkStack.size() - 1));
			stack.remove(stack.size() -1);
			checkStack.remove(checkStack.size() - 1);
		}
		this.prefix = setParentheses(prefix);
		this.prefix = reserve(prefix);
		this.checkPrefix = reserveCheck(this.checkPrefix);
		for (int i=0; i<this.checkPrefix.size()-1; i++){
			if (this.checkPrefix.get(i) == true){
				this.prefix.add(i+1, '=');
				this.checkPrefix.add(i+1, false);
			}
		}
		for (int i=0; i<this.prefix.size(); i++){
			if (this.prefix.get(i) == '|'){
				this.theRealPrefix += "or ";
			}else if (this.prefix.get(i) == '&'){
				this.theRealPrefix += "and ";
			}else{
				this.theRealPrefix += Character.toString(this.prefix.get(i)) + " ";
			}
		}
		System.out.println(this.theRealPrefix);
	}
	
	private List<Character> setParentheses(List<Character> arr){
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
					if (arr.get(j) == '(') countParentheses++;
					else if (arr.get(j) == ')') {
						countParentheses--;
						if (countParentheses == 0) countOperand++;
					}
					if (isOperator(arr.get(j)) == false && countParentheses == 0) countOperand++;
					if (countOperand == 2){
						if (arr.get(i) != '!'){
							arr.add(i+1, '(');
							arr.add(j, ')');
							this.checkPrefix.add(i+1, false);
							this.checkPrefix.add(j, false);
							i+=3;
							break;
						}else{
							arr.add(i+1, '(');
							arr.add(i, '(');
							arr.add(i, '=');
							arr.add(j,')');
							arr.add(j,')');
							this.checkPrefix.add(i+1, false);
							this.checkPrefix.add(i, false);
							this.checkPrefix.add(i, false);
							this.checkPrefix.add(j, false);
							this.checkPrefix.add(j, false);
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
	
	private int precedenceOfSymbol(Character symbol){
		switch(symbol){
		case '+':
		case '-':
			return 6;
		case '*':
		case '/':
		case '%':
			return 8;
		case '^':
		case '$':
			return 10;
		case '=':
		case '!':
		case '>':
		case '<':
			return 4;
		case '&':
		case '|':
			return 1;
		case '#':
		case '(':
		case ')':
			return 0;
		default:
			return 0;
		}
	}
	
	private boolean isOperator(Character symbol){
		switch (symbol){
		case '+':
		case '-':
		case '*':
		case '/':
		case '^':
		case '$':
		case '(':
		case ')':
		case '=':
		case '>':
		case '<':
		case '!':
		case '&':
		case '|':
		case '%':
			return true;
		default:
			return false;
		}
	}
	
	private List<Character> reserve(List<Character> infix){
		List<Character> temp = new ArrayList<>();
		for (int i=infix.size()-1; i>=0; i--){
			temp.add(infix.get(i));
		}
		return temp;
	}
	
	private List<Boolean> reserveCheck(List<Boolean> checkPrefix){
		List<Boolean> check = new ArrayList<>();
		for (int i=checkPrefix.size()-1; i>=0; i--){
			check.add(checkPrefix.get(i));
		}
		return check;
	}
	
	public String getPrefix(){
		return this.theRealPrefix;
	}
	
	private List<Character> convertStandard(List<Character> arr){
		for (int i=0; i<arr.size()-1; i++){
			if (arr.get(i+1) == '='){
				if (arr.get(i) == '=' || arr.get(i) == '!'){
					arr.remove(i+1);
					this.check.remove(i+1);
				}else if (arr.get(i) == '>' || arr.get(i) == '<'){
					arr.remove(i+1);
					this.check.remove(i+1);
					this.check.set(i, true);
				}
			}else if (arr.get(i) == '&' && arr.get(i+1) == '&'){
				arr.remove(i);
				this.check.remove(i);
			}else if (arr.get(i) == '|' && arr.get(i+1) == '|'){
				arr.remove(i);
				this.check.remove(i);
			}
		}
		return arr;
	}
	
	
//	public static void main(String[] args) {
//		String str = "(A+B) < 5";
//		InfixToPrefix a = new InfixToPrefix();
//		a.setInfix(str);
//	}

}
