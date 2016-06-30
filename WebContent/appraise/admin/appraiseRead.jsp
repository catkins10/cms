<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议任务名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="taskName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">地区名称</td>
		<td class="tdcontent"><ext:field writeonly="true" property="area"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议员类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="appraiserType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议年度</td>
		<td class="tdcontent"><ext:field writeonly="true" property="appraiseYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议月份</td>
		<td class="tdcontent"><ext:field writeonly="true" property="appraiseMonth"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">截止时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="endTime"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发起人</td>
		<td class="tdcontent"><ext:field writeonly="true" property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发起时间</td>
		<td class="tdcontent"><ext:field writeonly="true" property="created"/></td>
	</tr>
</table>