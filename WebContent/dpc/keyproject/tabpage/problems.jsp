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
		<td class="tdtitle" nowrap="nowrap" width="60px">年份</td>
		<td class="tdtitle" nowrap="nowrap" width="60px">月份</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">存在的问题</td>
		<td class="tdtitle" nowrap="nowrap" width="160">责任单位或责任人</td>
		<td class="tdtitle" nowrap="nowrap" width="80px">解决时限</td>
		<td class="tdtitle" nowrap="nowrap" width="50%">解决情况</td>
		<td class="tdtitle" nowrap="nowrap" width="50px">待审核</td>
		<td class="tdtitle" nowrap="nowrap"></td>
	</tr>
	<ext:iterate id="problem" property="problems">
<%		if(request.getAttribute("debrief")==null || ((com.yuanluesoft.dpc.keyproject.pojo.KeyProjectComponent)pageContext.getAttribute("problem")).getNeedApprovalTitle().equals("√")) { %>
			<tr style="cursor:pointer" valign="top">
				<td class="tdcontent" align="center" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="problemYear"/></td>
				<td class="tdcontent" align="center" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="problemMonth"/></td>
				<td class="tdcontent" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="problem"/></td>
				<td class="tdcontent" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="responsible"/></td>
				<td class="tdcontent" align="center" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="timeLimit"/></td>
				<td class="tdcontent" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="result"/></td>
				<td class="tdcontent" align="center" onclick="openProblem('<ext:write name="problem" property="id"/>');"><ext:write name="problem" property="needApprovalTitle"/></td>
				<ext:equal value="1" name="problem" property="completed">
					<td class="tdcontent" onclick="openProblem('<ext:write name="problem" property="id"/>')" nowrap="nowrap" style="padding:5px"></a></td>
				</ext:equal>
				<ext:notEqual value="1" name="problem" property="completed">
					<ext:equal value="true" name="editabled">
						<td class="tdcontent" onclick="openProblem('<ext:write name="problem" property="id"/>', true);" nowrap="nowrap" style="padding:5px">提交解决情况</a></td>
					</ext:equal>
					<ext:notEqual value="true" name="editabled">
						<td class="tdcontent" onclick="openProblem('<ext:write name="problem" property="id"/>');" nowrap="nowrap" style="padding:5px"></a></td>
					</ext:notEqual>
				</ext:notEqual>
			</tr>
<%		} %>
	</ext:iterate>
</table>