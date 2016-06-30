<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/dislpayMember" applicationName="bbs" pageName="bbsMember">
<table class="contentTable" width="100%" border="0" cellpadding="0" cellspacing="1">
	<col valign="middle" align="left">
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">昵称</td>
		<td class="tdcontent"><ext:write property="nickname"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">用户头像</td>
		<td class="tdcontent"><ext:personPortrait propertyPersonId="id"/></td>
	</tr>	<tr>
		<td class="tdtitle" nowrap="nowrap">积分</td>
		<td class="tdcontent"><ext:write property="point"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发帖数</td>
		<td class="tdcontent"><ext:write property="postCount"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">回帖数</td>
		<td class="tdcontent"><ext:write property="replyCount"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">性别</td>
		<td class="tdcontent">
			<ext:notEqual value="F" property="sex">男</ext:notEqual>
			<ext:equal value="F" property="sex">女</ext:equal>
		</td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">注册时间</td>
		<td class="tdcontent"><ext:write property="created" format="yyyy-MM-dd HH:mm"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">最后登录时间</td>
		<td class="tdcontent"><ext:write property="lastLoginTime" format="yyyy-MM-dd HH:mm"/></td>
	</tr>
	<ext:equal value="0" property="hideDetail">
		<tr>
			<td class="tdtitle" nowrap="nowrap">所属省份</td>
			<td class="tdcontent"><ext:write property="area"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">工作单位/所在院校</td>
			<td class="tdcontent"><ext:write property="company"/></td>
		</tr>
		<tr>
			<td class="tdtitle" nowrap="nowrap">邮箱</td>
			<td class="tdcontent"><ext:write property="email"/></td>
		</tr>
	</ext:equal>
</table>
</ext:form>
