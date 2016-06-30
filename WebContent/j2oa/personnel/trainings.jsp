<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newTraining() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/training.shtml?id=<ext:write property="id"/>', 500, 290);
	}
	function openTraining(trainingId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/training.shtml?id=<ext:write property="id"/>&training.id=' + trainingId, 500, 290);
	}
</script>
<%if(!"true".equals(request.getAttribute("readonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="登记" style="width:80px" onclick="newTraining()">
	</div>
<%}%>
<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td width="80px" nowrap="nowrap" class="tdtitle">开始时间</td>
		<td width="80px" nowrap="nowrap" class="tdtitle">结束时间</td>
		<td nowrap="nowrap" class="tdtitle">培训内容</td>
		<td nowrap="nowrap" class="tdtitle">备注</td>
	</tr>
	<ext:iterate id="training" indexId="trainingIndex" property="trainings">
		<tr onclick="openTraining('<ext:write name="training" property="id"/>')" align="center">
			<td class="tdcontent"><ext:write name="training" property="beginDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="training" property="endDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent" align="left"><ext:write name="training" property="description"/></td>
			<td class="tdcontent" align="left"><ext:write name="training" property="remark"/></td>
		</tr>
	</ext:iterate>
</table>