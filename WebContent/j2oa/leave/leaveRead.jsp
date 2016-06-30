<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="33%">
	<col valign="middle">
	<col valign="middle" width="33%">
	<col valign="middle">
	<col valign="middle" width="33%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="personName"/></td>
		<td class="tdtitle" nowrap="nowrap">申请部门</td>
		<td class="tdcontent"><ext:field writeonly="true" property="departmentName"/></td>
		<td class="tdtitle" nowrap="nowrap">请假类别</td>
		<td class="tdcontent"><ext:field writeonly="true" property="type"/></td>
	</tr>
	<tr>
		<td valign="top" style="padding-top:6px" class="tdtitle" nowrap="nowrap">请假原因</td>
		<td colspan="5" class="tdcontent"><ext:field writeonly="true" property="reason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">请假时间</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="beginTime"/>&nbsp;至&nbsp;<ext:field writeonly="true" property="endTime"/>	&nbsp;&nbsp;共&nbsp;<ext:field writeonly="true" property="dayCount"/>&nbsp;天</td>
		<td class="tdtitle" nowrap="nowrap">销假时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="terminateTime"/></td>
	</tr>
	<tr>
		<td valign="top" style="padding-top:6px" class="tdtitle" nowrap="nowrap">工作代理人</td>
		<td colspan="5" class="tdcontent"><ext:field writeonly="true" property="agents.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="5" class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>