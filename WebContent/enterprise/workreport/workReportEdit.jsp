<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col width="120px">
	<col width="100%">
	<tr>
		<td class="tdtitle" valign="top">前一阶段完成情况</td>
		<td class="tdcontent" colspan="3"><ext:field property="workDescription"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">存在的问题</td>
		<td class="tdcontent" colspan="3"><ext:field property="problem"/></td>
	</tr>
	<tr>
		<td class="tdtitle" valign="top">下一阶段计划</td>
		<td class="tdcontent" colspan="3"><ext:field property="plan"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">汇报人</td>
		<td class="tdcontent" colspan="3"><ext:field property="reporterName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">汇报时间</td>
		<td class="tdcontent" colspan="3"><ext:field property="reportTime"/></td>
	</tr>
</table>