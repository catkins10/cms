<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Input ID</title>


  </head>
  
  <body>
  <form action="tool.jsp" method="post">
    <table><tr><td>ID��</td><td><input type="text" name="atr" />(��,�ָ���ID)</td></tr>
    	<tr><td><input type="submit" value="�ύ" /></td><td><input type="reset" value="����" /></td></tr>
    </table>
  </form>
  </body>
</html>
