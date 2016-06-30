<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<ext:equal value="anonymous" name="SessionInfo" property="loginName" scope="session">
	<ext:button name="登录" width="36px" onclick="location='../usermanage/login.shtml?siteId=' + document.getElementsByName('siteId')[0].value + '&forceLogin=true&redirect=' + StringUtils.utf8Encode(location.href);"/>
	<ext:button name="注册" width="36px" onclick="location='../../jeaf/usermanage/member/regist.shtml?siteId=' + document.getElementsByName('siteId')[0].value;"/>
</ext:equal>
<ext:notEqual value="anonymous" name="SessionInfo" property="loginName" scope="session">
	<script>
	function doLogout() {
	    var logoutUrl = "<%=request.getContextPath()%>/jeaf/sso/logout.shtml";
	    var redirect = '<ext:write property="requestUrl"/>';
	    var anonymousEnable = '<ext:write property="anonymousEnable"/>';
	    if(redirect=='' || redirect=='null') {
	    	redirect = top.location.href;
	    }
		top.location = logoutUrl + '?' + (anonymousEnable=='true' ? 'anonymousEnable=true&' : '') + 'redirect=' + StringUtils.utf8Encode(redirect);
	}
	</script>
	您好，<ext:write name="SessionInfo" property="nickname" scope="session"/>！<a href="javascript:doLogout();">我要退出</a> <a href="<%=request.getContextPath()%>/bbs/usermanage/personalPanel.shtml<%=(request.getParameter("siteId")!=null ? "?siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>">修改个人信息</a>
</ext:notEqual>