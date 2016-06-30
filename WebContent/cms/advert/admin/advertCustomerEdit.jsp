<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("editabled", "true");%>
<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col align="left">
	<col width="50%">
	<col align="left">
	<col width="50%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">客户名称</td>
		<td colspan="3" class="tdcontent"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">联系人</td>
		<td class="tdcontent"><ext:field property="linkman"/></td>
		<td class="tdtitle" nowrap="nowrap">联系电话</td>
		<td class="tdcontent"><ext:field property="tel"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">账户余额</td>
		<td class="tdcontent"><ext:field property="account"/></td>
		<td class="tdtitle" nowrap="nowrap">最后充值时间</td>
		<td class="tdcontent"><ext:field property="lastTopupTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent" class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>