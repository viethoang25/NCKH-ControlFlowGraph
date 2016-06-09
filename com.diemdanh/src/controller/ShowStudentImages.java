package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import manager.Constants;

@WebServlet("/ShowStudentImages")
public class ShowStudentImages extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ShowStudentImages() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		String studentId = request.getParameter("studentid");
		String unitId = request.getParameter("unitid");
		String directory = Constants.DIRECTORY_UNITS + "/" + unitId;
		File[] files = new File(directory).listFiles();
		List<String> listImagePath = new ArrayList<>();

		listFiles(directory, listImagePath);

		request.setAttribute("listimagepath", listImagePath);
		RequestDispatcher rd = request
				.getRequestDispatcher("/jsp/studentImages.jsp");
		rd.forward(request, response);
	}

	private void listFiles(String directoryName, List<String> files) {
		File directory = new File(directoryName);

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				if (getFileExtension(file.getName()).equals("jpg"))
					files.add(file.getAbsolutePath());
			} else if (file.isDirectory()) {
				listFiles(file.getAbsolutePath(), files);
			}
		}
	}

	private String getFileExtension(String name) {
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}
}
