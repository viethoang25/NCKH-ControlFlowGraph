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
	<form action="<%=request.getContextPath()%>/ProcessLogin" method="POST">
		Tên đăng nhập : <input type="text" class="form-control" id="inputtext" name="username">
		Mật khẩu : <input type="password" class="form-control" id="inputtext" name="password">
		<input type="submit" class="btn btn-primary" value="Đăng nhập">
	</form>
</body>
</html>