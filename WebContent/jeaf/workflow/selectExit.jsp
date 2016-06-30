<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<%request.setAttribute("writeFormPrompt", "true");%>
<script>
	function doOK() { //完成选择出口进入办理人选择
		var workflowExitJon;
		eval('workflowExitJon=' + document.getElementsByName("workflowExitText")[0].value + ';');
		var workflowExit = ListUtils.findObjectByProperty(workflowExitJon.objects, 'uuid', workflowExitJon.uuid);
		var radios = document.getElementsByName("radioExitSelect");
		if(!radios[0]) {
			radios = new Array(radios);
		}
		for(var i=0; i<radios.length; i++) {
			if(!radios[i].checked) {
				continue;
			}
			var exit;
			for(var j = 0; j < workflowExit.exits.length && (exit=ListUtils.findObjectByProperty(workflowExitJon.objects, 'uuid', workflowExit.exits[j].uuid)).id!=radios[i].value; j++);
			exit.selected = true;
			if(radios[i].value.indexOf(".split")==-1) {
				break;
			}
			var seleced = false;
			var tr = DomUtils.nextElement(radios[i].parentNode.parentNode);
			while(tr) {
				var splitSelect;
				if((splitSelect=DomUtils.getElement(tr, null, 'splitSelect')) && splitSelect.checked) {
					var split = ListUtils.findObjectByProperty(workflowExitJon.objects, 'uuid', exit.splitWorkflowExit.uuid);
					var splitExit;
					for(var k = 0; k < split.exits.length && (splitExit=ListUtils.findObjectByProperty(workflowExitJon.objects, 'uuid', split.exits[k].uuid)).id!=splitSelect.value; k++);
					splitExit.selected = true;
					seleced = true;
				}
				tr = DomUtils.nextElement(tr);
			}
			if(!seleced) {
				alert("尚未选择并行出口！");
				return false;
			}
			break;
		}
		document.getElementsByName("workflowExitText")[0].value = JsonUtils.stringify(workflowExitJon);
		FormUtils.doAction("<ext:write property="currentAction"/>", "workflowAction=afterSelectExit");
	}
	function onClickWorkflowExit(src) {
		DomUtils.getElement(src.parentNode, null, 'radioExitSelect').checked = true;
	}
	function onClickSplitWorkflowExit(src) {
		var tr = src.parentElement.previousSibling;
		while(!DomUtils.getElement(tr, null, 'radioExitSelect')) {
			tr = tr.previousSibling;
		}
		DomUtils.getElement(tr, null, 'radioExitSelect').checked = true;
		if(event.srcElement!=DomUtils.getElement(src, null, 'splitSelect')) {
			DomUtils.getElement(src, null, 'splitSelect').checked = !DomUtils.getElement(src, null, 'splitSelect').checked;
		}
	}
</script>
<div style="width:500px">
	流程环节列表：
	<div class="divData" style="height:230px;">
		<table onselectstart="return false;" class="view" cellspacing="1" cellpadding="0" border="0" style="table-layout:fixed" width="100%">
			<col align="center" valign="middle" width="30px" class="viewColOdd">
			<col align="left" valign="left" width="100%" class="viewColEven">
			<tr valign="middle" height="18">
				<td class="viewHeader" align="center"></td>
				<td class="viewHeader" align="center" id="title">环节名称</td>
			</tr>
			<ext:iterate id="exit" indexId="exitIndex" property="workflowExit.exits">
				<tr id="0">
					<td class="viewData"><input class="radio" type="radio" <ext:equal value="0" name="exitIndex">checked</ext:equal> name="radioExitSelect" value="<ext:write name="exit" property="id"/>"></td>
					<td class="viewData" onclick="onClickWorkflowExit(this)"><ext:write name="exit" property="name"/></td>
				</tr>
				<ext:instanceof name="exit" className="SplitExit">
					<ext:iterate id="split" name="exit" property="splitWorkflowExit.exits">
						<tr style="cursor:pointer" id="0">
							<td class="viewData"></td>
							<td class="viewData" onclick="onClickSplitWorkflowExit(this)"><input type="checkbox" class="checkbox" checked id="splitSelect" value="<ext:write name="split" property="id"/>"><ext:write name="split" property="name"/></td>
						</tr>
					</ext:iterate>
				</ext:instanceof>
			</ext:iterate>
		</table>
	</div>
</div>