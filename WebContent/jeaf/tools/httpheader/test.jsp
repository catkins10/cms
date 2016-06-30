<%@ page contentType="text/html; charset=UTF-8"%>

<html>
<body>
accept:<%=request.getHeader("accept")==null ? "" : request.getHeader("accept").replaceAll("", "")%>
<br/><br/>
user-agent:<%=request.getHeader("user-agent")%>
</body>
</html>