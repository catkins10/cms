<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">县别</td>
		<td class="tdcontent"><ext:field property="county"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">年度</td>
		<td class="tdcontent"><ext:field property="planYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">月份</td>
		<td class="tdcontent"><ext:field property="planMonth"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">合同外资计划(验资口径)</td>
		<td class="tdcontent"><ext:field property="contractCheckPlan"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">实际到资计划(验资口径)</td>
		<td class="tdcontent"><ext:field property="receiveCheckPlan"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">合同外资计划(可比口径)</td>
		<td class="tdcontent"><ext:field property="contractPlan"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">实际到资计划(可比口径)</td>
		<td class="tdcontent"><ext:field property="receivePlan"/></td>
	</tr>
</table>