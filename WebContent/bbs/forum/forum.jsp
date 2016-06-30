<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<ext:form siteFormServiceName="bbsFormService" applicationName="bbs" action="/displayForum" pageName="bbs">
<script>
   	document.title = "<ext:write name="forum" property="forum.name"/>" + (document.title=="" ? "" : " - " + document.title);
	function createArticle() {
		var siteId = '<%=com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId")%>';
		window.open('../article/article.shtml?forumId=' + document.getElementsByName("id")[0].value + (siteId=='null' || siteId=='0' ? "" : "&siteId=" + siteId));
	}
</script>
<%request.setAttribute("recordTitle", "个主题");%>
<html:hidden property="id"/>
<script language="JavaScript" charset="utf-8" src="<%=request.getContextPath()%>/jeaf/view/js/view.js"></script>

<table cellspacing="0" cellpadding="3" width="100%" class="loginTable">
	<tr>
		<td align="left" valign="middle" nowrap="nowrap">
			<jsp:include page="../login/embedLogin.jsp"/>
		</td>
		<td align="right" valign="middle" nowrap="nowrap" style="padding:5px;display:none">
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
					<a href="bbs.shtml?id=<ext:write name="parentDirectory" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>"><ext:write name="parentDirectory" property="directoryName"/></a>
				</ext:instanceof>
				<ext:instanceof name="parentDirectory" className="ForumCategory">
					<ext:write name="parentDirectory" property="directoryName"/>
				</ext:instanceof>
				<ext:instanceof name="parentDirectory" className="Forum">
					<a href="forum.shtml?id=<ext:write name="parentDirectory" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>"><ext:write name="parentDirectory" property="directoryName"/></a>
				</ext:instanceof>
				»
			</ext:iterate>
			<ext:write property="forum.name"/>
		</td>
	</tr>
	<tr>
		<td nowrap="nowrap" align="right">
			<ext:notEmpty property="forum.managers">
				版主：
				<ext:iterate id="manager" property="forum.managers">
					<a href="../usermanage/member.shtml?act=open&id=<ext:write name="manager" property="userId"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank"><ext:write name="manager" property="userName"/></a>
				</ext:iterate>
				&nbsp;
			</ext:notEmpty>
		</td>
	</tr>
</table>
<ext:notEmpty property="forum.childForums">
	<table cellspacing="0" cellpadding="3" width="100%" class="forumTable">
		<col width="50px">
		<col>
		<col width="60px">
		<col width="60px">
		<col width="200px">
		<tr>
			<td colspan="5" class="categoryHeader" nowrap="nowrap">&nbsp;» 子版块</td>
		</tr>
		<tr>
			<td colspan="2" class="forumHeader">&nbsp;版块</td>
			<td align="center" class="forumHeader">主题数</td>
			<td align="center" class="forumHeader">帖数</td>
			<td align="center" class="forumHeader">最后发表</td>
		</tr>
		<ext:iterate id="childForum" property="forum.childForums">
			<tr>
				<td align="center" class="forum" nowrap="nowrap">
					<a href="forum.shtml?id=<ext:write name="childForum" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>">
						<img src="image/forum<ext:notEqual value="0" name="childForum" property="articleToday">_new</ext:notEqual>.gif" border="0">
					</a>
				</td>
				<td class="forum" nowrap="nowrap">
					<a href="forum.shtml?id=<ext:write name="childForum" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" class="forumName">『<ext:write name="childForum" property="name"/>』</a><br>
					版主：
					<ext:iterate id="manager" name="childForum" property="managers">
						<a href="../usermanage/member.shtml?act=open&id=<ext:write name="manager" property="managerId"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank"><ext:write name="manager" property="userName"/></a>
					</ext:iterate>
				</td>
				<td align="center" class="forum" nowrap="nowrap"><ext:write name="childForum" property="articleTotal"/></td>
				<td align="center" class="forum" nowrap="nowrap"><ext:writeNumber name="childForum" property="articleTotal" namePlus="childForum" propertyPlus="replyTotal"/></td>
				<td class="forum" nowrap="nowrap">
					<ext:notEmpty name="childForum" property="lastArticle">
						<a href="../article/article.shtml?id=<ext:write name="childForum" property="lastArticle.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>"><ext:write name="childForum" property="lastArticle.subject" maxCharCount="30" ellipsis="..."/></a> 
						by <a href="../usermanage/member.shtml?act=open&id=<ext:write name="childForum" property="lastArticle.creatorId"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank"><ext:write name="childForum" property="lastArticle.creatorNickname"/></a> - <ext:write name="childForum" property="lastArticle.created" format="yyyy-M-d H:m"/>
					</ext:notEmpty>&nbsp;
				</td>
			</tr>
		</ext:iterate>
	</table>
	<br>
</ext:notEmpty>
<jsp:include page="/jeaf/view/viewCommon.jsp" />
<div class="viewPackage">
	<div class="viewActionAndSearchBar">
		<jsp:include page="/jeaf/view/viewActionBar.jsp" />
		<jsp:include page="/jeaf/view/viewSearchBar.jsp" />
	</div>
	<ext:notEqual value="condition" property="viewPackage.viewMode">
		<div class="viewPageAndCategoryBar">
			<jsp:include page="/jeaf/view/viewPageBar.jsp" />
		</div>
		<jsp:include page="/bbs/article/articleView.jsp" />
		<div class="viewPageAndCategoryBottomBar">
			<jsp:include page="/jeaf/view/viewPageBar.jsp" />
		</div>
	</ext:notEqual>
</div>
<ext:equal value="condition" property="viewPackage.viewMode">
	<jsp:include page="/jeaf/view/viewSearch.jsp" />
</ext:equal>
</ext:form>