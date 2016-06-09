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
	<h3>Danh sách hình ảnh</h3>
	<%
		List<String> list = (List<String>) request.getAttribute("listimagepath");
		if (list != null) {
				for (String path : list) {
	%>
					<li><a href="<%=request.getContextPath()%>" target="maincontent">
					<img src="file://<%=path%>" alt="image" height="50" width="50"><%=path%></a></li>
	<%
				}
		}
	%>
</body>
</html>