<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newTeamAssess(teamId) {
		PageUtils.openurl('<%=request.getContextPath()%>/enterprise/assess/assess.shtml?teamId=' + teamId, 'mode=fullscreen', 'assess');
	}
	function openTeamAssess(assessId) {
		PageUtils.openurl('<%=request.getContextPath()%>/enterprise/assess/assess.shtml?id=' + assessId, 'mode=fullscreen', assessId);
	}
</script>
<ext:equal value="true" name="projectTeam" property="manager">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="新建考核" style="width:80px" onclick="newTeamAssess('<ext:write name="projectTeam" property="id"/>')">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" nowrap="nowrap" class="tdtitle" width="180px">项目组成员</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="100%">考核成绩</td>
		<td align="center" nowrap="nowrap" class="tdtitle" width="200px">考核时间</td>
	</tr>
	<ext:iterate id="assess" name="projectTeam" property="assesses">
		<ext:iterate id="assessResult" name="assess" property="results">
			<tr style="cursor:pointer" align="center" valign="top" onclick="openTeamAssess('<ext:write name="assess" property="id"/>', '<ext:write name="teamAssess" property="teamId"/>')">
				<td class="tdcontent" align="left"><ext:write name="assessResult" property="personName"/></td>
				<td class="tdcontent" align="left">
					<ext:notEqual value="0.0" name="assessResult" property="result">
						<ext:write name="assessResult" property="result" format="#.##"/>
					</ext:notEqual>
				</td>
				<td class="tdcontent"><ext:write name="assess" property="created" format="yyyy-MM-dd"/></td>
			</tr>
		</ext:iterate>
	</ext:iterate>
</table>