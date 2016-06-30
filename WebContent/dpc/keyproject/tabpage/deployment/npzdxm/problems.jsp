<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@ taglib uri="/WEB-INF/struts-logic" prefix="logic" %>

<script>
function newProblem() {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/problem.shtml?id=<ext:write property="id"/>', 600, 400);
}
function openProblem(problemId, completed) {
	DialogUtils.openDialog('<%=request.getContextPath()%>/dpc/keyproject/problem.shtml?id=<ext:write property="id"/>&problem.id=' + problemId + (completed ? '&problem.completed=1' : ''), 600, 400);
}
</script>
<ext:empty name="debrief"><ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="登记存在的问题" onclick="newProblem()">
	</div>
</ext:equal></ext:empty>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="80px">年份</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">月份</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">存在的问题及工作建议</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">待审核</td>
	</tr>
	<ext:iterate id="problem" property="problems">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("problem")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top">
				<td class="tdcontent" align="center" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="problemYear"/></td>
				<td class="tdcontent" align="center" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="problemMonth"/></td>
				<td class="tdcontent" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="problem"/></td>
				<td class="tdcontent" align="center" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="needApprovalTitle"/></td>
			</tr>
<%		} %>
	</ext:iterate>
</table>