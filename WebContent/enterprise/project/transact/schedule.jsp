<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function completeSchedule(designCompleted) {
		if(designCompleted) {
			if(!confirm("是否确定项目已经完成")) {
				return;
			}
		}
		else {
			if(document.getElementsByName("projectTeam.stage")[0].value=="") {
				alert("项目阶段未设置");
				return;
			}
			if(document.getElementsByName("projectTeam.workContent")[0].value=="") {
				alert("工作内容未设置");
				return;
			}
			if(document.getElementsByName("projectTeam.expectingDate")[0].value=="") {
				alert("完成时间未设置");
				return;
			}
			if(document.getElementsByName("projectTeam.projectTeamManagerNames")[0].value=="") {
				alert("项目负责人未设置");
				return;
			}
			if(document.getElementsByName("projectTeam.projectTeamMemberNames")[0].value=="") {
				alert("项目组成员未设置");
				return;
			}
		}
		FormUtils.doAction('completeSchedule', 'isDesignCompleted=' + (designCompleted ? "1" : "0"));
	}
</script>

<div style="width:500px">
	<table width="100%" border="0" cellpadding="3" cellspacing="0">
		<tr>
			<td nowrap width="80" align="right">项目阶段：</td>
			<td width="100%"><ext:field property="projectTeam.stage"/></td>
		</tr>
		<tr>
			<td nowrap align="right" valign="top">工作内容：</td>
			<td width="100%"><ext:field property="projectTeam.workContent"/></td>
		</tr>
		<tr>
			<td nowrap align="right">完成时间：</td>
			<td width="100%"><ext:field property="projectTeam.expectingDate"/></td>
		</tr>
		<tr>
			<td nowrap align="right">项目负责人：</td>
			<td width="100%"><ext:field property="projectTeam.projectTeamManagerNames"/></td>
		</tr>
		<tr>
			<td nowrap align="right">项目组成员：</td>
			<td width="100%"><ext:field property="projectTeam.projectTeamMemberNames"/></td>
		</tr>
		<tr align="right">
			<td >附件：</td>
			<td><ext:field property="projectTeam.attachments"/></td>
		</tr>
	</table>
	<html:hidden property="projectTeam.id"/>
</div>