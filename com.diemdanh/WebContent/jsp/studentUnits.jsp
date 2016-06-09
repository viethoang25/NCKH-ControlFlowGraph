<%@page import="model.bean.Unit"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hệ thống điểm danh</title>
<link rel="stylesheet" href="css/bootstrap.min.css">
<script type="text/javascript" src="js/bootstrap.min.js"></script>
</head>
<body>
	<h3>Danh sách học phần</h3>
	<%
		List<Unit> list = (List<Unit>) request.getAttribute("listunits");
		String studentId = (String) request.getAttribute("studentid");
		if (list != null) {
				for (Unit unit : list) {
	%>
					<li><a href="<%=request.getContextPath()%>/ShowStudentImages?studentid=<%=studentId%>&unitid=<%=unit.getId()%>" 
					><%=unit.getId()%></a></li>
	<%
				}
		}
	%>
</body>
</html>