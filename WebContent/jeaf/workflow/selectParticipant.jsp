<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>
<%@page import="org.apache.commons.beanutils.PropertyUtils"%>
<%@page import="com.yuanluesoft.jeaf.util.CnToSpell"%>

<%request.setAttribute("writeFormPrompt", "true");%>
<script>
	function doOk() {
		var workflowExitJon;
		eval('workflowExitJon=' + document.getElementsByName("workflowExitText")[0].value + ';');
		var workflowExit = ListUtils.findObjectByProperty(workflowExitJon.objects, 'uuid', workflowExitJon.uuid);
		var exit;
		for(var i = 0; i < workflowExit.exits.length && !(exit=ListUtils.findObjectByProperty(workflowExitJon.objects, 'uuid', workflowExit.exits[i].uuid)).selected; i++);
		var selectedExits = [];
		if(exit.uuid.indexOf("SplitExit")==-1) {
			selectedExits.push(exit);
		}
		else {
			var split = ListUtils.findObjectByProperty(workflowExitJon.objects, 'uuid', exit.splitWorkflowExit.uuid);
			for(var i = 0; i < split.exits.length; i++) {
				var splitExit = ListUtils.findObjectByProperty(workflowExitJon.objects, 'uuid', split.exits[i].uuid);
				if(splitExit.selected) {
					selectedExits.push(splitExit);
				}
			}
		}
		for(var i = 0; i < selectedExits.length; i++) {
			var tableParticipant = DomUtils.getElement(document.getElementById("tab" + selectedExits[i].id), 'table', 'tableParticipant');
			var transactMode = DomUtils.getElement(tableParticipant, 'input', 'transactMode').value;
			for(var j = 0; j < selectedExits[i].transactModes.length; j++) {
				var transactModeModel = ListUtils.findObjectByProperty(workflowExitJon.objects, 'uuid', selectedExits[i].transactModes[j].uuid);
				if(transactModeModel.name==transactMode) {
					transactMode = transactModeModel;
					selectedExits[i].transactModes = [transactMode];
				}
			}
			if(DomUtils.getElement(tableParticipant, 'div', 'divSource')) {
				var resultSet = DomUtils.getElement(tableParticipant, 'div', 'divResult').getElementsByTagName('table')[0].rows;
				if(transactMode.name=="vote") {
					if(resultSet.length < transactMode.voteNumber + 1) {
						alert("环节[" + selectedExits[i].name + "]的办理人不能少于" + transactMode.voteNumber);
						return false;
					}
				}
				var participants = [];
				for(var p=1; p<resultSet.length; p++) {
					for(var j=0; j<selectedExits[i].participants.length; j++) {
						if(ListUtils.findObjectByProperty(workflowExitJon.objects, 'uuid', selectedExits[i].participants[j].uuid).id==resultSet[p].id) {
							participants.push(selectedExits[i].participants[j]);
						}
					}
				}
				if(participants.length==0) {
					alert("环节[" + selectedExits[i].name + "]的办理人不能为空！");
					return false;
				}
				selectedExits[i].participants = participants;
			}
			var urgeHours = DomUtils.getElement(tableParticipant, 'input', 'urgeHours');
			if(urgeHours) {
				selectedExits[i].urgeHours = urgeHours.value;
			}
		}
		document.getElementsByName("workflowExitText")[0].value = JsonUtils.stringify(workflowExitJon);
		FormUtils.doAction("<ext:write property="currentAction"/>", "workflowAction=doSend");
	}
	function getParticipantTable() {
		var tables = document.getElementsByTagName("table");
		for(var i=0; i<tables.length; i++) {
			if(tables[i].id=="tableParticipant" && tables[i].offsetWidth > 0) {
				return tables[i];
			}
		}
		return null;
	}
	function getSourceTable() { //获得源表格
		return DomUtils.getElement(getParticipantTable(), 'div', 'divSource').getElementsByTagName('table')[0];
	}
	function getResultTable() { //获得结果表格
		return DomUtils.getElement(getParticipantTable(), 'div', 'divResult').getElementsByTagName('table')[0];
	}
	function onClickSource(row) {
		var sourceTable = getSourceTable();
		setSelected(sourceTable, row);
		row.cells[0].focus();
	}
	function setSelected(table, selectRow) {
		for(var i=1; i<table.rows.length; i++) {
			var row = table.rows[i];
			for(var j=0; j<row.cells.length; j++) {
				var index = row.cells[j].className.indexOf(" selected");
				if(row!=selectRow) {
					if(index!=-1) {
						row.cells[j].className = row.cells[j].className.substring(0, index);
					}
				}
				else {
					if(index==-1) {
						row.cells[j].className = row.cells[j].className + " selected";
					}
				}
			}
		}
	}
	function onDblClickSource(row) {
		onClickResult(appendResult(row));
	}
	function onClickResult(row) {
		if(!row) {
			row = event.srcElement.parentElement;
		}
		var resultTable = getResultTable();
		setSelected(resultTable, row);
		row.cells[0].focus();
	}
	function appendResult(row) {
		var resultTable = getResultTable();
		if(DomUtils.getElement(getParticipantTable(), "input", "transactMode").value=="single") {
			for(var i=resultTable.rows.length - 1; i>0; i--) {
				resultTable.deleteRow(i);
			}
		}
		else {
			for(var i=1; i<resultTable.rows.length; i++) {
				if(resultTable.rows[i].id==row.id) {
					return resultTable.rows[i];
				}
			}
		}
		var newResult = resultTable.insertRow(-1);
		newResult.id = row.id;
		newResult.ondblclick = function() {
			resultTable.deleteRow(this.rowIndex);
		};
		newResult.onclick = onClickResult;
		newResult.style.cursor = "hand";
		var td = newResult.insertCell(-1);
		td.innerHTML = row.cells[1].innerHTML;
		td.className = "viewData";
		return newResult;
	}
	function selectOne() {
		var sourceTable = getSourceTable();
		var selects = DomUtils.getElements(sourceTable, "input", "select");
		var flag = true;
		var newResult;
		if(selects && selects.length>0) {
			for(var i=0; i<selects.length; i++) {
				if(selects[i].checked) {
					selects[i].checked = false;
					newResult = appendResult(selects[i].parentElement.parentElement);
					flag = false;
				}
			}
		}
		if(flag) {
			for(var i=1; i<sourceTable.rows.length; i++) {
				if(sourceTable.rows[i].cells[0].className.indexOf(" selected")!=-1) {
					newResult = appendResult(sourceTable.rows[i]);
					if(i<sourceTable.rows.length-1) {
						onClickSource(sourceTable.rows[i+1]);
					}
					break;
				}
			}
		}
		if(newResult) {
			onClickResult(newResult);
		}
	}
	function selectAll() {
		var sourceTable = getSourceTable();
		var newResult;
		for(var i=1; i<sourceTable.rows.length; i++) {
			newResult = appendResult(sourceTable.rows[i]);
			DomUtils.getElement(sourceTable.rows[i], "input", "select").checked = false;
		}
		if(newResult) {
			onClickResult(newResult);
		}
	}
	function cleanOne() {
		var resultTable = getResultTable();
		for(var i=0; i<resultTable.rows.length; i++) {
			if(resultTable.rows[i].cells[0].className.indexOf(" selected")!=-1) {
				resultTable.deleteRow(i);
				if(resultTable.rows.length>1) {
					onClickResult(resultTable.rows[i>resultTable.rows.length-1 ? resultTable.rows.length-1:i]);
				}
				break;
			}
		}
	}
	function cleanAll() {
		var resultTable = getResultTable();
		for(var i=resultTable.rows.length-1; i>0; i--) {
			resultTable.deleteRow(i);
		}
	}
	function onTransactModeChanged(mode) {
		if(mode=="single") {
			var resultTable = getResultTable();
			for(var i=resultTable.rows.length - 1; i>1; i--) {
				resultTable.deleteRow(i);
			}
		}
	}
	function onUrgeEnabledChanged(urgeEnabled) {
		var cells = DomUtils.getElement(getParticipantTable(), 'table', 'tableUrge').rows[0].cells;
		for(var i = 2; i < cells.length; i++) {
			cells[i].style.display = urgeEnabled ? '' : 'none';
		}
		if(!urgeEnabled) {
			DomUtils.getElement(getParticipantTable(), 'input', 'urgeHours').value = "0";
		}
	}
	document.body.onkeydown = function(event) {
		if(!event) {
			event = window.event;
		}
		var chr = String.fromCharCode(event.keyCode).toLowerCase();
		var sourceTable = getSourceTable();
		var beginRow = 1;
		for(var i=1; i<sourceTable.rows.length; i++) {
			if(sourceTable.rows[i].cells[0].className.indexOf(" selected")!=-1) { //选中记录
				if(sourceTable.rows[i].getAttribute("sort").indexOf(chr)==0 && i+1<sourceTable.rows.length) {
					beginRow = i+1;
				}
				break;
			}
		}
		for(var i=beginRow; i<sourceTable.rows.length; i++) {
			if(sourceTable.rows[i].getAttribute("sort").indexOf(chr)==0) {
				setSelected(sourceTable, sourceTable.rows[i]);
				sourceTable.rows[i].cells[0].focus();
				return;
			}
		}
		for(var i=1; i<sourceTable.rows.length; i++) {
			if(sourceTable.rows[i].getAttribute("sort").indexOf(chr)==0) {
				setSelected(sourceTable, sourceTable.rows[i]);
				sourceTable.rows[i].cells[0].focus();
				return;
			}
		}
	};
</script>
<div style="width:580px;">
	<ext:tab name="selectedParticipantTabs">
		<ext:iterate id="exit" indexId="exitIndex" name="selectedExits">
			<ext:tabBody nameTabId="exit" propertyTabId="id">
				<table id="tableParticipant" border="0" height="100%" width="100%" cellspacing="0" cellpadding="0px">
					<col width="50%">
					<col>
					<col width="50%">
					<tr valign="top">
						<td>
							<table border="0" cellpadding="0" cellspacing="0" style="width:100%">
								<tr>
									<td align="left" style="padding:2px;" nowrap="nowrap">办理方式：</td>
									<ext:sizeEqual name="exit" property="transactModes" value="1">
										<ext:iterate id="transactMode" name="exit" property="transactModes">
											<td style="padding-top:3px" width="100%">
												<ext:write name="transactMode" property="title"/>
												<input id="transactMode" type="hidden" value="<ext:write name="transactMode" property="name"/>">
											</td>
										</ext:iterate>
									</ext:sizeEqual>
									<ext:sizeNotEqual name="exit" property="transactModes" value="1">
										<td width="100%">
											<ext:iterate id="transactMode" name="exit" property="transactModes" length="1">
												<input id="transactMode<ext:write name="exitIndex"/>" name="transactMode" onchange="onTransactModeChanged(this.value)" type="hidden" value="<ext:write name="transactMode" property="name"/>">
												<script>
													new DropdownField('<input name="transactModeTitle<ext:write name="exitIndex"/>" onchange="afterSelectTransactMode()" value="<ext:write name="transactMode" property="title"/>" type="text" readonly>', '<ext:iterate id="transactMode" indexId="transactModeIndex" name="exit" property="transactModes"><ext:notEqual name="transactModeIndex" value="0">\0</ext:notEqual><ext:write name="transactMode" property="title"/>|<ext:write name="transactMode" property="name"/></ext:iterate>', 'transactMode<ext:write name="exitIndex"/>', 'transactModeTitle<ext:write name="exitIndex"/>', 'field', '', '', '');
												</script>
											</ext:iterate>
										</td>
									</ext:sizeNotEqual>
								</tr>
							</table>
						</td>
						<td></td>
						<td>
							<ext:equal value="true" name="exit" property="urgeHoursAdjustable">
								<table border="0" cellpadding="0" cellspacing="0" id="tableUrge">
									<tr>
										<td align="left" style="padding:2px;" nowrap="nowrap"><input name="urgeEnabled<ext:write name="exitIndex"/>" <ext:notEqual value="0" name="exit" property="urgeHours">checked</ext:notEqual> type="checkbox" onclick="onUrgeEnabledChanged(this.checked)"></td>
										<td align="left" style="padding:2px 2px 2px 0px;" nowrap="nowrap">催办<td>
										<td align="left" style="padding:2px; <ext:equal value="0" name="exit" property="urgeHours">display:none</ext:equal>" nowrap="nowrap">催办周期：</td>
										<td width="50px" style="<ext:equal value="0" name="exit" property="urgeHours">display:none</ext:equal>">
											<script>
												new DropdownField('<input id="urgeHours<ext:write name="exitIndex"/>" name="urgeHours" value="<ext:write name="exit" property="urgeHours" format="#.#"/>" type="text">', '24\0 48\0 72\0 96\0 120\0 12\0 6\0 3\0 2\0 1\0 0.5', 'urgeHours<ext:write name="exitIndex"/>', '', 'field', '', '', '');
											</script>
										</td>
										<td style="padding-left:3px; <ext:equal value="0" name="exit" property="urgeHours">display:none</ext:equal>" nowrap="nowrap">小时</td>
									</tr>
								</table>	
							</ext:equal>
						</td>
					</tr>
					<tr height="5px" valign="top">
						<td colspan="3"></td>
					</tr>
					<ext:equal value="false" name="exit" property="autoSend">
		    		 	<tr valign="bottom">
							<td style="padding:2px">选择办理人：</td>
							<td></td>
							<td style="padding:2px">选择结果：</td>
						</tr>
						<tr height="100%">
							<td>
								<div onselectstart="return false;" id="divSource" class="divData" style="height:260px;">
									<table class="view" cellspacing="1" cellpadding="0" border="0" style="table-layout:fixed" width="100%">
										<col align="center" valign="middle" width="32px" class="viewColOdd">
										<col align="left" valign="left" width="100%" class="viewColEven">
										<tr valign="middle">
											<td class="viewHeader" align="center"></td>
											<td class="viewHeader" align="center" id="title">办理人</td>
										</tr>
										<ext:iterate id="participant" indexId="participantIndex" name="exit" property="participants">
											<tr id="<ext:write name="participant" property="id"/>" sort="<%=CnToSpell.getShortSpell((String)PropertyUtils.getProperty(pageContext.getAttribute("participant"), "name"))%>" ondblclick="onDblClickSource(this)" onclick="onClickSource(this)" style="cursor:pointer">
												<td class="viewData<ext:equal value="0" name="participantIndex"> selected</ext:equal>"><input type="checkbox" class="checkbox" id="select" value="0"></td>
												<td class="viewData<ext:equal value="0" name="participantIndex"> selected</ext:equal>"><ext:write name="participant" property="name" filter="false"/></td>
											</tr>
										</ext:iterate>
									</table>
								</div>
							</td>
							<td valign="middle" align="center" nowrap="nowrap" style="padding:5px">
								<div class="selectOneButton" title="选择" onclick="selectOne()">&gt;</div><br>
				       			<div class="deleteOneButton" title="清除" onclick="cleanOne()">&lt;</div><br>
				       			<div class="selectAllButton" title="全选" onclick="selectAll()">&gt;&gt;</div><br>
				       			<div class="deleteAllButton" title="全清" onclick="cleanAll()">&lt;&lt;</div><br>
				       		</td>
							<td>
								<div id="divResult" class="divData" style="height:260px;">
									<table class="view" cellspacing="1" cellpadding="0" border="0" style="table-layout:fixed" width="100%">
										<col class="viewColOdd">
										<tr valign="middle">
											<td class="viewHeader" align="center">办理人</td>
										</tr>
									</table>
								</div>
							</td>
						</tr>
					</ext:equal>
					<!-- 自动发送,不允许用户选择办理人 -->
					<ext:equal value="true" name="exit" property="autoSend">
		    		 	<tr valign="bottom">
							<td colspan="3" style="padding:2px">办理人列表：</td>
						</tr>
						<tr height="100%">
							<td colspan="3">
								<div id="divResult" class="divData" style="height:260px;">
									<table class="view" cellspacing="1" cellpadding="0" border="0" style="table-layout:fixed" width="100%">
										<col align="left" valign="middle" width="100%" class="viewColOdd">
										<tr valign="middle">
											<td align="center" class="viewHeader">办理人</td>
										</tr>
										<ext:iterate id="participant" name="exit" property="participants">
											<tr>
												<td class="viewData"><ext:write name="participant" property="name"/></td>
											</tr>
										</ext:iterate>
									</table>
								</div>
							</td>
						</tr>
					</ext:equal>
				</table>
			</ext:tabBody>
		</ext:iterate>
	</ext:tab>
</div>