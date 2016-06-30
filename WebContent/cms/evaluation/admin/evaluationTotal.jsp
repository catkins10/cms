<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%
	com.yuanluesoft.cms.evaluation.forms.admin.EvaluationTopic topicForm = (com.yuanluesoft.cms.evaluation.forms.admin.EvaluationTopic)request.getAttribute("adminEvaluationTopic");
	request.setAttribute("totals", "true".equals(request.getAttribute("totalByOrg")) ? topicForm.getTotalsSortByOrg() : topicForm.getTotals());
%>

<script>
	function listEvaluations(targetPersonId, optionId) {
		DialogUtils.openSelectDialog('cms/evaluation', 'admin/evaluation', 550, 350, false, '', 'DialogUtils.openDialog("<%=request.getContextPath()%>/cms/evaluation/admin/evaluation.shtml?topicId=<ext:write property="id"/>&id={id}", "80%", "80%")', '', '', '', true, 'topicId=<ext:write property="id"/>&targetPersonId=' + targetPersonId + (optionId ? '&optionId=' + optionId : ''));
	}
</script>

<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center" style="font-weight: bold">
		<td class="tdtitle" width="50px" nowrap="nowrap">序号</td>
		<td class="tdtitle" nowrap="nowrap">姓名</td>
		<td class="tdtitle" nowrap="nowrap" width="100%">所在部门</td>
		<ext:iterate id="option" property="options">
			<td class="tdtitle" nowrap="nowrap" style="padding: 0 10px 0 10px"><ext:write name="option" property="name"/></td>
		</ext:iterate>
		<td class="tdtitle" width="60px" nowrap="nowrap">得分</td>
	</tr>
	<ext:iterate id="total" indexId="totalIndex" name="totals">
		<tr align="center">
			<td class="tdcontent" nowrap="nowrap">
				<a href="javascript:listEvaluations('<ext:write name="total" property="targetPersonId"/>')"><ext:writeNumber name="totalIndex" plus="1"/></a>
			</td>
			<td class="tdcontent" nowrap="nowrap" align="left">
				<a href="javascript:listEvaluations('<ext:write name="total" property="targetPersonId"/>')"><ext:write name="total" property="targetPersonName"/></a>
			</td>
			<td class="tdcontent" nowrap="nowrap" align="left">
				<a href="javascript:listEvaluations('<ext:write name="total" property="targetPersonId"/>')"><ext:write name="total" property="targetPersonOrg"/></a>
			</td>
			<ext:iterate id="optionTotal" name="total" property="optionTotals">
				<td class="tdcontent" nowrap="nowrap">
					<a href="javascript:listEvaluations('<ext:write name="total" property="targetPersonId"/>', '<ext:write name="optionTotal" property="optionId"/>')"><ext:write name="optionTotal" property="count"/></a>
				</td>
			</ext:iterate>
			<td class="tdcontent" nowrap="nowrap">
				<a href="javascript:listEvaluations('<ext:write name="total" property="targetPersonId"/>')"><ext:write name="total" property="score" format="###.#"/></a>
			</td>
		</tr>
	</ext:iterate>
</table>