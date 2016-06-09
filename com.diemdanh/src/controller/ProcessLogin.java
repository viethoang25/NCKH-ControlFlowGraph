package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.bean.Account;
import model.bean.Unit;
import model.bo.AccountBO;
import model.bo.UnitBO;

@WebServlet("/ProcessLogin")
public class ProcessLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProcessLogin() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Account account = AccountBO.checkAccount(username, password);
		if(account == null) {
			
		} else {
			List<Unit> listUnits = UnitBO.getUnitsOfStudent(account.getId());
			request.setAttribute("studentid", account.getId());
			request.setAttribute("listunits", listUnits);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/studentUnits.jsp");
			rd.forward(request, response);
		}
	}

}
