import java.util.HashMap;

import com.microsoft.z3.*;

public class Z3Handle {

	private String input;
	private Model model;
	private Status sat;

	public Z3Handle(String input) {
		this.input = input;
		solver();
	}

	private void solver() {
		HashMap<String, String> cfg = new HashMap<String, String>();
		cfg.put("model", "true");
		Context ctx = new Context(cfg);

		BoolExpr a = ctx.parseSMTLIB2String(input, null, null, null, null);
		/*
		 * Goal goal = ctx.mkGoal(true, false, false); goal.add(a);
		 * System.out.println(goal);
		 */

		Solver solver = ctx.mkSolver();
		solver.add(a);
		sat = solver.check();
		if (solver.check() == Status.SATISFIABLE) {
			model = solver.getModel();
		}
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(sat);
		str.append("\n");
		str.append(model);
		return str.toString();
	}
	
	public void printResult(){
		System.out.println(sat);
		System.out.println(model);
	}
}
