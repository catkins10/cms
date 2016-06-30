<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<script>
	function newOption() {
		DialogUtils.openDialog('<%=request.getContextPath()%>/cms/evaluation/admin/evaluationOption.shtml?id=<ext:write property="id"/>', 500, 300);
	}
	function openOption(evaluationOptionId) {
		DialogUtils.openDialog('<%=request.getContextPath()%>/cms/evaluation/admin/evaluationOption.shtml?id=<ext:write property="id"/>&option.id=' + evaluationOptionId, 500, 300);
	}
</script>
<ext:equal value="true" name="editabled">
	<div style="padding-bottom:5px">
		<input type="button" class="button" value="添加选择项"  onclick="newOption()">
	</div>
</ext:equal>
<table width="100%" class="table" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td class="tdtitle" nowrap="nowrap" width="36px">序号</td>
		<td class="tdtitle" nowrap="nowrap">选项名称</td>
		<td class="tdtitle" nowrap="nowrap" width="150px">选中时对应的分数</td>
	</tr>
	<ext:iterate id="evaluationOption" indexId="evaluationOptionIndex" property="options">
		<tr style="cursor:pointer" align="center" onclick="openOption('<ext:write name="evaluationOption" property="id"/>')">
			<td class="tdcontent"><ext:writeNumber name="evaluationOptionIndex" plus="1"/></td>
			<td class="tdcontent" align="left"><ext:write name="evaluationOption" property="name"/></td>
			<td class="tdcontent"><ext:write name="evaluationOption" property="score" format="###"/></td>
		</tr>
	</ext:iterate>
</table>