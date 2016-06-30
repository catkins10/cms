<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.jinrun.filetransfer.JinRunFileFilter"%>

<%
	//测试参数配置
	String jinrunIp = "10.0.0.3"; //评标服务器IP
	String jinrunBiddingDocumentsPath = "e:\\招标文件\\"; //招标文件在评标服务器上的目录
	String webPath = "e:/fzztb"; //web主目录
	System.out.println(this);
	synchronized(this) {
		String file = webPath + "/cms/pages/12/attachments/" + request.getParameter("file");
		out.print("文件:" + file + "<br>");
		if(!JinRunFileFilter.VerifyGEF(file)) {
			out.print("不是GEF文件");
		}
		else if(JinRunFileFilter.TranFile(file, jinrunBiddingDocumentsPath, jinrunIp)) {
			out.print("发送到评标服务器成功");
		}
		else {
			out.print("发送到评标服务器失败");
		}
	}
%>