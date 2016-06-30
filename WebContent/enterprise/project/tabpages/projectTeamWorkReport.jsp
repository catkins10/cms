<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newTeamWorkReport(teamId) {
		PageUtils.openurl('<%=request.getContextPath()%>/enterprise/workreport/workReport.shtml?teamId=' + teamId, 'mode=fullscreen', 'workReport');
	}
	function openTeamWorkReport(workReportId) {
		PageUtils.openurl('<%=request.getContextPath()%>/enterprise/workreport/workReport.shtml?id=' + workReportId, 'mode=fullscreen', workReportId);
	}
</script>
<ext:equal value="true" name="projectTeam" property="member">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="汇报" style="width:80px" onclick="newTeamWorkReport('<ext:write name="projectTeam" property="id"/>')">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" nowrap="nowrap" class="tdtitle" width="80px">汇报人</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="40%">前一阶段完成情况</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="30%">存在的问题</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="40%">下一阶段计划</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="110px">汇报时间</td>
	</tr>
	<ext:iterate id="workReport" name="projectTeam" property="workReports">
		<tr style="cursor:pointer" align="left" valign="top" onclick="openTeamWorkReport('<ext:write name="workReport" property="id"/>', '<ext:write name="teamWorkReport" property="teamId"/>')">
			<td class="tdcontent"><ext:write name="workReport" property="reporterName"/></td>
			<td class="tdcontent"><ext:write name="workReport" property="workDescription"/></td>
			<td class="tdcontent"><ext:write name="workReport" property="problem"/></td>
			<td class="tdcontent"><ext:write name="workReport" property="plan"/></td>
			<td class="tdcontent" align="center"><ext:write name="workReport" property="reportTime" format="yyyy-MM-dd HH:mm"/></td>
		</tr>
	</ext:iterate>
</table>