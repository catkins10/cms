<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">期数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sn"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">分发范围</td>
		<td class="tdcontent"><ext:field property="issueRanges.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">总期数</td>
		<td class="tdcontent"><ext:field writeonly="true" property="snTotal"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">排版人</td>
		<td class="tdcontent"><ext:field property="typesetPerson"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">排版时间</td>
		<td class="tdcontent"><ext:field property="typesetTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">定版人</td>
		<td class="tdcontent"><ext:field property="issuePerson"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">定版时间</td>
		<td class="tdcontent"><ext:field property="issueTime"/></td>
	</tr>
</table>