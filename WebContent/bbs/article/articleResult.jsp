<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<ext:form siteFormServiceName="bbsFormService" applicationName="bbs" pageName="bbsPrompt">
	<a href="viewArticle.shtml?id=<ext:write property="id"/>">提交完成，现在将返回主题页面，如果您的浏览器没有自动跳转，请点击这里。</a>
	<script>
		window.setTimeout("location.href='viewArticle.shtml?id=<ext:write property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>'", 3000);
	</script>
</ext:form>