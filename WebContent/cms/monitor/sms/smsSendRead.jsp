<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" valign="top" nowrap="nowrap">内容</td>
		<td class="tdcontent"><ext:field writeonly="true" property="content"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">分类</td>
		<td class="tdcontent"><ext:field writeonly="true" property="category"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发送时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="sendTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
</table>