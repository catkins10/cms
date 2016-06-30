<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<table valign="middle" width="100%" border="1" cellpadding="0" cellspacing="0" class="table">
	<col>
	<col width="100%">
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目名称</td>
		<td class="tdcontent"><ext:field property="projectName"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">勘察、设计阶段</td>
		<td class="tdcontent"><ext:field property="stage"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目规模</td>
		<td class="tdcontent"><ext:field property="scale"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">项目负责人</td>
		<td class="tdcontent"><ext:field property="projectLeader"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建人</td>
		<td class="tdcontent"><ext:field property="creator"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">创建时间</td>
		<td class="tdcontent"><ext:field property="created"/></td>
	</tr>
	<tr>
		<td class="tdtitle" nowrap="nowrap">备注</td>
		<td class="tdcontent"><ext:field property="remark"/></td>
	</tr>
</table>