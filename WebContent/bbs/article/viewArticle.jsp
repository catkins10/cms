<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<ext:form siteFormServiceName="bbsFormService" applicationName="bbs" action="/displayViewArticle" pageName="bbsArticle">
	<script>
	   	document.title = "<ext:write name="viewArticle" property="article.subject"/>" + (document.title=="" ? "" : " - " + document.title);
		function createArticle() {
			location.href = '../article/article.shtml?forumId=<ext:write property="forum.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>';
		}
		function reply() {
			location.href = '../article/reply.shtml?articleId=<ext:write property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>';
		}
	</script>
	<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/view/js/view.js"></script>
	<%request.setAttribute("recordTitle", "个回复");%>
	<table cellspacing="0" cellpadding="3" width="100%" class="loginTable">
		<tr>
			<td align="left" valign="middle" nowrap="nowrap">
				<jsp:include page="../login/embedLogin.jsp"/>
			</td>
			<td align="right" valign="middle" nowrap="nowrap" style="padding:5px">
				精华区
			</td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="3" width="100%">
		<tr>
			<td nowrap="nowrap">
				<ext:iterate id="parentDirectory" property="parentDirectories">
					<ext:instanceof name="parentDirectory" className="Bbs">
						<a href="../forum/bbs.shtml?id=<ext:write name="parentDirectory" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>"><ext:write name="parentDirectory" property="directoryName"/></a>
					</ext:instanceof>
					<ext:instanceof name="parentDirectory" className="ForumCategory">
						<ext:write name="parentDirectory" property="directoryName"/>
					</ext:instanceof>
					<ext:instanceof name="parentDirectory" className="Forum">
						<a href="../forum/forum.shtml?id=<ext:write name="parentDirectory" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>"><ext:write name="parentDirectory" property="directoryName"/></a>
					</ext:instanceof>
					»
				</ext:iterate>
				<ext:write property="article.subject"/>
			</td>
		</tr>
	</table>
	<jsp:include page="/jeaf/view/viewCommon.jsp" />
	<div class="viewPackage">
		<div class="viewActionAndSearchBar">
			<jsp:include page="/jeaf/view/viewActionBar.jsp" />
		</div>
		<div class="viewPageAndCategoryBar">
			<div class="articleSwitchBar" style="float:left">
				<ext:notEmpty property="previousArticle">
					« <a href="viewArticle.shtml?id=<ext:write property="previousArticle.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" title="<ext:write property="previousArticle.subject"/>">上一主题：<ext:write property="previousArticle.subject" maxCharCount="30" ellipsis="..."/></a>
					<ext:notEmpty property="nextArticle">|</ext:notEmpty>
				</ext:notEmpty>
				<ext:notEmpty property="nextArticle">
					<a href="viewArticle.shtml?id=<ext:write property="nextArticle.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" title="<ext:write property="nextArticle.subject"/>">下一主题：<ext:write property="nextArticle.subject" maxCharCount="30" ellipsis="..."/></a> »
				</ext:notEmpty>
			</div>
			<jsp:include page="/jeaf/view/viewPageBar.jsp" />
		</div>
		<jsp:include page="replyView.jsp" />
		<div class="viewPageAndCategoryBottomBar">
			<jsp:include page="/jeaf/view/viewPageBar.jsp" />
		</div>
	</div>
</ext:form>