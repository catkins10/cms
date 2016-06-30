<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext"%>

<%
String url = com.yuanluesoft.jeaf.util.RequestUtils.getRequestURL(request, false);
int endIndex = url.lastIndexOf('?');
int beginIndex = endIndex==-1 ? url.lastIndexOf('.') : url.lastIndexOf('.', endIndex);
if(",jsp,html,htm,shtml,do,".indexOf("," + (endIndex==-1 ? url.substring(beginIndex + 1) : url.substring(beginIndex + 1, endIndex)) + ",")==-1) {
	response.getWriter().write("您访问的页面不存在或者已经被删除。");
}
else if(com.yuanluesoft.jeaf.util.RequestUtils.getRequestInfo(request).isClientRequest()) {
	response.setStatus(404);
	throw new Exception();
}
else { %>
	<ext:page applicationName="cms/sitemanage" pageName="pageNotFoundPrompt" pageServiceName="siteIndexPageService"/>
<%
} %>