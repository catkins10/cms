<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr title="如：公司作息时间">
		<td class="tdtitle" nowrap="nowrap">描述</td>
		<td class="tdcontent"><ext:field writeonly="true" property="description"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">生效时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="effectiveDate"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">适用的用户</td>
		<td class="tdcontent"><ext:field writeonly="true" property="timetableVisitors.visitorNames"/></td>
	</tr>
</table>