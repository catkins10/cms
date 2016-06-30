<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">补卡人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="personName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">补卡时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="mendTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap" valign="top">补卡原因</td>
		<td class="tdcontent"><ext:field writeonly="true" property="reason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">审核情况</td>
		<td class="tdcontent"><ext:field writeonly="true" property="pass"/></td>
	</tr>
</table>