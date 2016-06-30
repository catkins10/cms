<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col valign="middle" width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">名称</td>
		<td class="tdcontent"><ext:field property="name"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议任务名称</td>
		<td class="tdcontent"><ext:field property="taskName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">地区名称</td>
		<td class="tdcontent"><ext:field property="area"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议员类型</td>
		<td class="tdcontent"><ext:field writeonly="true" property="appraiserType"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议年度</td>
		<td class="tdcontent"><ext:field property="appraiseYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">评议月份</td>
		<td class="tdcontent"><ext:field property="appraiseMonth"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">截止时间</td>
		<td class="tdcontent"><ext:field property="endTime"/></td>
	</tr>
	<ext:equal value="create" property="act">
		<tr>
			<td class="tdtitle" nowrap="nowrap">没有评议员的单位</td>
			<td class="tdcontent"><ext:field property="cancelNoAppraisersUnit"/></td>
		</tr>
	</ext:equal>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发起人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">发起时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
</table>