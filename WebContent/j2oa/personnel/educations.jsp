<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newEducation() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/education.shtml?id=<ext:write property="id"/>', 500, 290);
	}
	function openEducation(educationId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/j2oa/personnel/education.shtml?id=<ext:write property="id"/>&personnelEducation.id=' + educationId, 500, 290);
	}
</script>
<%if(!"true".equals(request.getAttribute("readonly"))) { %>
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="登记" style="width:80px" onclick="newEducation()">
	</div>
<%}%>

<table id="tableBill" width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td width="100px" nowrap="nowrap" class="tdtitle">开始时间</td>
		<td width="100px" nowrap="nowrap" class="tdtitle">结束时间</td>
		<td width="150px" nowrap="nowrap" class="tdtitle">所在学校</td>
		<td nowrap="nowrap" class="tdtitle">学习内容</td>
		<td nowrap="nowrap" class="tdtitle">备注</td>
	</tr>
	<ext:iterate id="education" indexId="educationIndex" property="educations">
		<tr onclick="openEducation('<ext:write name="education" property="id"/>')" align="center">
			<td class="tdcontent"><ext:write name="education" property="beginDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent"><ext:write name="education" property="endDate" format="yyyy-MM-dd"/></td>
			<td class="tdcontent" align="left"><ext:write name="education" property="school"/></td>
			<td class="tdcontent" align="left"><ext:write name="education" property="description"/></td>
			<td class="tdcontent" align="left"><ext:write name="education" property="remark"/></td>
		</tr>
	</ext:iterate>
</table>