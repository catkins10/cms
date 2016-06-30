<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form siteFormServiceName="bbsFormService" applicationName="bbs" action="/displayBbs" pageName="bbs">
<script>
	document.title = "<ext:write name="bbs" property="bbs.name"/>";
</script>
<table cellspacing="0" cellpadding="3" width="100%" class="forumLogin">
	<tr>
		<td align="left" valign="middle" nowrap="nowrap">
			<jsp:include page="../login/embedLogin.jsp"/>
		</td>
		<td align="right" valign="middle" nowrap="nowrap" >
			今日：<ext:writeNumber property="bbs.articleToday" propertyPlus="bbs.replyToday"/>&nbsp;昨日：<ext:writeNumber property="bbs.articleYesterday" propertyPlus="bbs.replyYesterday"/>&nbsp;<span style="display:none">精华区</span><br>
			主题：<ext:write property="bbs.articleTotal"/>&nbsp;回帖：<ext:writeNumber property="bbs.replyTotal"/>&nbsp;会员：<ext:write property="bbs.memberTotal"/>&nbsp;
			欢迎新会员：<a href="../usermanage/member.shtml?act=open&id=<ext:write property="bbs.lastMember.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank"><ext:write property="bbs.lastMember.loginName"/></a>
		</td>
	</tr>
</table>
<br>
<ext:iterate id="category" property="bbs.categories">
	<table cellspacing="0" cellpadding="3" width="100%" class="forumTable">
		<col width="50px">
		<col>
		<col width="60px">
		<col width="60px">
		<col width="200px">
		<tr>
			<td colspan="2" class="categoryHeader" nowrap="nowrap">&nbsp;» <ext:write name="category" property="name"/></td>
			<td colspan="3" class="categoryHeader" align="right" nowrap="nowrap">
				<ext:notEmpty name="category" property="managers">
					分区版主：
					<ext:iterate id="manager" name="category" property="managers">
						<a href="../usermanage/member.shtml?act=open&id=<ext:write name="manager" property="userId"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank"><ext:write name="manager" property="userName"/></a>
					</ext:iterate>
					&nbsp;
				</ext:notEmpty>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="forumHeader">&nbsp;版块</td>
			<td align="center" class="forumHeader">主题数</td>
			<td align="center" class="forumHeader">回帖数</td>
			<td align="center" class="forumHeader">最后发表</td>
		</tr>
		<ext:iterate id="forum" name="category" property="forums">
			<tr>
				<td align="center" class="forum" nowrap="nowrap">
					<a href="forum.shtml?id=<ext:write name="forum" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>">
						<img src="image/forum<ext:notEqual value="0" name="forum" property="articleToday">_new</ext:notEqual>.gif" border="0">
					</a>
				</td>
				<td class="forum" nowrap="nowrap">
					<a href="forum.shtml?id=<ext:write name="forum" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" class="forumName">『<ext:write name="forum" property="name"/>』</a><br>
					<ext:notEmpty name="forum" property="childForums">
						[<ext:iterate id="childForum" name="forum" property="childForums">
							<a href="forum.shtml?id=<ext:write name="childForum" property="id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>"><ext:write name="childForum" property="name"/></a>
						</ext:iterate>]<br>
					</ext:notEmpty>
					<ext:empty name="forum" property="childForums">
						<ext:notEmpty name="forum" property="description">
							[<ext:write name="forum" property="description" maxCharCount="50" ellipsis="..."/>]<br>
						</ext:notEmpty>
					</ext:empty>
					版主：
					<ext:iterate id="manager" name="forum" property="managers">
						<a href="../usermanage/member.shtml?act=open&id=<ext:write name="manager" property="userId"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank"><ext:write name="manager" property="userName"/></a>
					</ext:iterate>
				</td>
				<td align="center" class="forum" nowrap="nowrap"><ext:write name="forum" property="articleTotal"/></td>
				<td align="center" class="forum" nowrap="nowrap"><ext:writeNumber name="forum" property="replyTotal"/></td>
				<td class="forum" nowrap="nowrap">
					<ext:notEmpty name="forum" property="lastArticle">
						<a target="_blank" href="../article/viewArticle.shtml?id=<ext:write name="forum" property="lastArticle.id"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>"><ext:write name="forum" property="lastArticle.subject" maxCharCount="30" ellipsis="..."/></a><br>
						<a href="../usermanage/member.shtml?act=open&id=<ext:write name="forum" property="lastArticle.creatorId"/><%=(request.getParameter("siteId")!=null ? "&siteId=" + com.yuanluesoft.jeaf.util.RequestUtils.getParameterLongValue(request, "siteId") : "")%>" target="_blank"><ext:write name="forum" property="lastArticle.creatorNickname"/></a> <ext:write name="forum" property="lastArticle.created" format="yyyy-M-d H:mm"/>
					</ext:notEmpty>&nbsp;
				</td>
			</tr>
		</ext:iterate>
	</table>
	<br>
</ext:iterate>
</ext:form>