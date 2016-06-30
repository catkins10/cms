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
		<td class="tdtitle" nowrap="nowrap">有无简讯</td>
		<td class="tdcontent"><ext:field property="hasBrief"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">当前期号</td>
		<td class="tdcontent"><ext:field property="sn"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">当前总期号</td>
		<td class="tdcontent"><ext:field property="snTotal"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">每年重排期号</td>
		<td class="tdcontent"><ext:field property="resetSnByYear"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">栏目列表</td>
		<td class="tdcontent"><ext:field property="columns"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">分发范围</td>
		<td class="tdcontent"><ext:field property="issueRanges.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">编辑</td>
		<td class="tdcontent"><ext:field property="editors.visitorNames"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">主编</td>
		<td class="tdcontent"><ext:field property="chiefEditors.visitorNames"/></td>
	</tr>
</table>