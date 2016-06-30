<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ page contentType="text/html; charset=UTF-8"%>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col valign="middle">
	<col valign="middle" width="33%">
	<col valign="middle">
	<col valign="middle" width="33%">
	<col valign="middle">
	<col valign="middle" width="33%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">申请人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="proposerName"/></td>
		<td class="tdtitle" nowrap="nowrap">申请部门</td>
		<td class="tdcontent"><ext:field writeonly="true" property="departmentName"/></td>
		<td class="tdtitle" nowrap="nowrap">出差地点</td>
		<td class="tdcontent"><ext:field writeonly="true" property="address"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" style="padding-top:6px">出差目的</td>
		<td colspan="5" class="tdcontent"><ext:field writeonly="true" property="reason"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">出差时间</td>
		<td colspan="3" class="tdcontent"><ext:field writeonly="true" property="beginTime"/>&nbsp;至&nbsp;<ext:field writeonly="true" property="endTime"/></td>
		<td class="tdtitle" nowrap="nowrap">交通工具</td>
		<td class="tdcontent"><ext:field writeonly="true" property="vehicle"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top" style="padding-top:6px">出差人</td>
		<td colspan="5" class="tdcontent"><ext:field writeonly="true" property="tripPerson.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td colspan="5" class="tdcontent"><ext:field writeonly="true" property="remark"/></td>
	</tr>
</table>