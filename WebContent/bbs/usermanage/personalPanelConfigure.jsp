<%@ page contentType="text/html; charset=UTF-8"%>

<%
	if("myArticles".equals(request.getParameter("target"))) {
    	response.sendRedirect("../article/myArticles.shtml");
    }
    else {
    	response.sendRedirect("bbsUser.shtml");
    }
%>