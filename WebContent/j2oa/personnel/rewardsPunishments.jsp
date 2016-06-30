<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newRewardsPunishment() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/rewardsPunishment.shtml?id=<ext:write property="id"/>', 500, 290);
	}
	function openRewardsPunishment(rewardsPunishmentId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/rewardsPunishment.shtml?id=<ext:write property="id"/>&rewardsPunishment.id=' + rewardsPunishmentId, 500, 290);
	}
</script>
<%if(!"true".equals(request.getAttribute("readonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="登记" style="width:80px" onclick="newRewardsPunishment()">
	</div>
<%}%>
<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr height="20px" align="center">
		<td width="80px" nowrap="nowrap" class="tdtitle">时间</td>
		<td width="160px" nowrap="nowrap" class="tdtitle">奖惩内容</td>
		<td nowrap="nowrap" class="tdtitle">奖惩原因</td>
		<td nowrap="nowrap" class="tdtitle">备注</td>
	</tr>
	<ext:iterate id="rewardsPunishment" indexId="rewardsPunishmentIndex" property="rewardsPunishments">
		<tr onclick="openRewardsPunishment('<ext:write name="rewardsPunishment" property="id"/>')" align="center">
			<td class="tdcontent"><ext:write name="rewardsPunishment" property="happenDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="rewardsPunishment" property="content"/></td>
			<td class="tdcontent" align="left"><ext:write name="rewardsPunishment" property="reason"/></td>
			<td class="tdcontent" align="left"><ext:write name="rewardsPunishment" property="remark"/></td>
		</tr>
	</ext:iterate>
</table>