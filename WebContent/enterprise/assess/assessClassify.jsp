<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveAssessClassify" onsubmit="return formOnSubmit();">
   	<ext:tab>
		<ext:tabBody tabId="basic">
		   	<table valign="middle" width="100%"border="1" cellpadding="0" cellspacing="0" class="table">
				<col>
				<col width="100%">
				<tr>
					<td class="tdtitle" nowrap="nowrap">类型</td>
					<td class="tdcontent"><ext:field property="classify"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">自评</td>
					<td class="tdcontent"><ext:field property="selfAssess"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">项目组考核</td>
					<td class="tdcontent"><ext:field property="projectTeamAccess"/></td>
				</tr>
				<tr>
					<td class="tdtitle" nowrap="nowrap">适用的用户</td>
					<td class="tdcontent"><ext:field property="assessUser.visitorNames"/></td>
				</tr>
			</table>
		</ext:tabBody>
	
		<ext:tabBody tabId="standards">
			<script>
			function newStandard() {
				DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/assess/assessStandard.shtml?id=<ext:write property="id"/>', 550, 300);
			}
			function openStandard(standardId) {
				DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/assess/assessStandard.shtml?id=<ext:write property="id"/>&standard.id=' + standardId, 550, 300);
			}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加考核标准" style="width:120px" onclick="newStandard()">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center" nowrap="nowrap" class="tdtitle" width="32px">序号</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="40%">考核内容</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="60%">考核说明及评分标准</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="60px">最大分值</td>
				</tr>
				<ext:iterate id="standard" property="standards">
					<tr style="cursor:pointer" valign="top" onclick="openStandard('<ext:write name="standard" property="id"/>')">
						<td class="tdcontent" align="center"><ext:write name="standard" property="priority" format="#.##"/></td>
						<td class="tdcontent"><ext:write name="standard" property="content"/></td>
						<td class="tdcontent"><ext:write name="standard" property="standard"/></td>
						<td class="tdcontent" align="center"><ext:write name="standard" property="maxValue" format="#.##"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
		
		<ext:tabBody tabId="activities">
			<script>
			function newActivity() {
				DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/assess/assessActivity.shtml?id=<ext:write property="id"/>', 400, 180);
			}
			function openActivity(activityId) {
				DialogUtils.openDialog('<%=request.getContextPath()%>/enterprise/assess/assessActivity.shtml?id=<ext:write property="id"/>&activity.id=' + activityId, 400, 180);
			}
			</script>
			<div style="padding-bottom:5px">
				<input type="button" class="button" value="添加考核步骤" style="width:120px" onclick="newActivity()">
			</div>
			<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td align="center" nowrap="nowrap" class="tdtitle" width="100%">考核步骤</td>
					<td align="center" nowrap="nowrap" class="tdtitle" width="100px">权重</td>
				</tr>
				<ext:iterate id="activity" property="activities">
					<tr style="cursor:pointer" valign="top" onclick="openActivity('<ext:write name="activity" property="id"/>')">
						<td class="tdcontent"><ext:write name="activity" property="activity"/></td>
						<td class="tdcontent" align="center"><ext:write name="activity" property="weight"/></td>
					</tr>
				</ext:iterate>
			</table>
		</ext:tabBody>
	</ext:tab>
</ext:form>