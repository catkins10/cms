<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<script>
	function doOK() {
		var radios = document.getElementsByName("selectWorkflowUndo");
		var i=0;
		for(; i<radios.length && !radios[i].checked; i++);
		document.getElementsByName("selectedWorkflowUndoId")[0].value = radios[i].id;
		FormUtils.doAction("<ext:write property="currentAction"/>", "workflowAction=doUndo");
	}
</script>
<div style="width:470px">
	允许取回的环节列表：
	<div id="divResult"  class="divData" style="height:230px;">
		<table onselectstart="return false;" cellspacing="1" cellpadding="0" border="0" style="table-layout:fixed" class="view">
			<col align="center" valign="middle" width="30" class="viewColOdd">
			<col align="left" valign="left" width="100%" class="viewColEven">
			<tr valign="middle">
				<td class="viewHeader" align="center"></td>
				<td class="viewHeader" align="center" id="title">允许取回的环节列表</td>
			</tr>
			<ext:iterate id="workflowUndo" indexId="index" property="workflowUndos">
				<tr style="cursor:pointer" id="0">
					<td class="viewData"><input type="radio" class="radio" <ext:equal value="0" name="index">checked</ext:equal> name="selectWorkflowUndo" id="<ext:write name="workflowUndo" property="id"/>"></td>
					<td class="viewData"><label for="<ext:write name="workflowUndo" property="id"/>"><ext:write name="workflowUndo" property="title"/></label></td>
				</tr>
			</ext:iterate>
		</table>
	</div>
	<html:hidden property="selectedWorkflowUndoId"/>
	<html:hidden property="opinionPackage.opinion"/>
</div>