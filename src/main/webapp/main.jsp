<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here :: dynamic :: accessinfo</title>
</head>
<body>
	<h1>${accessInfo[0].srName}(${accessInfo[0].srCode})</h1>
	<h1>${accessInfo[0].emName}(${accessInfo[0].emCode})</h1>
	<h1>최근 접속 일시 : ${accessInfo[0].date}</h1>
	
	<form action="AccessOut" method="post">

		<input type="submit" value="logout" />
		<input type="hidden" name= "srCode" value="${accessInfo[0].srCode}" />
		<input type="hidden" name= "emCode" value="${accessInfo[0].emCode}" />
	</form>
</body>
</html>

