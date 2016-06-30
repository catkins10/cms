<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<script>
	function doOK() {
		var radios = document.getElementsByName("selectReverseInstance");
		if(radios.length==0) {
			alert("允许回退的环节为空,请联系管理员检查流程配置。");
			return;
		}
		var i=0;
		for(; i<radios.length && !radios[i].checked; i++);
		document.getElementsByName("selectedReverseInstanceId")[0].value = radios[i].id;
		FormUtils.doAction("<ext:write property="currentAction"/>", "workflowAction=doReverse");
	}
</script>
<div style="width:500px">
	允许回退的环节列表：
	<div id="divResult" class="divData" style="height:230px;">
		<table onselectstart="return false;" cellspacing="1" cellpadding="0" border="0" style="table-layout:fixed" class="view">
			<col align="center" valign="middle" width="30" class="viewColOdd">
			<col align="left" valign="left" width="100%" class="viewColEven">
			<tr valign="middle">
				<td class="viewHeader" align="center"></td>
				<td class="viewHeader" align="center" id="title">环节名称</td>
			</tr>
			<ext:iterate id="reverseActivityInstance" indexId="index" property="reverseActivityInstances">
				<tr style="cursor:pointer" height="20" id="0">
					<td class="viewData"><input type="radio" class="radio" <ext:equal value="0" name="index">checked</ext:equal> name="selectReverseInstance" id="<ext:write name="reverseActivityInstance" property="id"/>"></td>
					<td class="viewData"><label for="<ext:write name="reverseActivityInstance" property="id"/>"><ext:write name="reverseActivityInstance" property="name"/></label></td>
				</tr>
			</ext:iterate>
		</table>
	</div>
	<html:hidden property="selectedReverseInstanceId"/>
</div>