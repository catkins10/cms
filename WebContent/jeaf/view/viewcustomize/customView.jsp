<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

<ext:form action="/saveViewCustom">
	<script language="JavaScript">
		var sourceColumns = [
			<ext:iterate id="column" indexId="columnIndex" property="sourceColumns">
				<ext:notEqual value="0" name="columnIndex">,</ext:notEqual>
				{name: '<ext:write name="column" property="name"/>',
				 title: '<ext:write name="column" property="title"/>',
				 width: '<ext:write name="column" property="width"/>',
				 align: '<ext:write name="column" property="align"/>',
				 display: '<ext:write name="column" property="display"/>'}
			</ext:iterate>
		];
		var CLASS_PATH = "com.yuanluesoft.jeaf.view";
		var selectedSourceColumn;
		function onClickSourceColumn(src) {
			if(selectedSourceColumn) {
				setClassName(selectedSourceColumn, "viewData");
			}
			selectedSourceColumn = src;
			selectedSourceColumn.cells[0].focus();
			setClassName(src, "viewData selected");
		}
		var selectedDisplayColumn;
		function onClickDisplayColumn(src, setFocus) {
			if(!src) {
				src = event.srcElement.parentElement;
				if(src.tagName.toLowerCase()!="tr") {
					src = src.parentElement;
				}
			}
			if(selectedDisplayColumn) {
				setClassName(selectedDisplayColumn, "viewData");
			}
			selectedDisplayColumn = src;
			if(setFocus) {
				selectedDisplayColumn.cells[0].focus();
			}
			setClassName(src, "viewData selected");
		}
		var selectedSortColumn;
		function onClickSortColumn(src, setFocus) {
			if(!src) {
				src = event.srcElement.parentElement;
				if(src.tagName.toLowerCase()!="tr") {
					src = src.parentElement;
				}
			}
			if(selectedSortColumn) {
				setClassName(selectedSortColumn, "viewData");
			}
			selectedSortColumn = src;
			if(setFocus) {
				selectedSortColumn.cells[0].focus();
			}
			setClassName(src, "viewData selected");
		}
		function setClassName(tr, className) {
			var cells = tr.cells;
			for(var i = cells.length-1; i>=0; i--) {
				cells[i].className = className;
			}
		}
		function addDisplayColumn() {
			if(selectedSourceColumn) {
				var tableDisplayColumn = document.getElementById("tableDisplayColumn"), rows = tableDisplayColumn.rows;
				var sourceColumn = sourceColumns[selectedSourceColumn.rowIndex-1];
				var i=1;
				for(; i<rows.length && (DomUtils.getElement(rows[i], "input", "columnName").value!=sourceColumn.name || DomUtils.getElement(rows[i], "input", "columnTitle").value!=sourceColumn.title); i++);
				if(i<rows.length) {
					onClickDisplayColumn(rows[i], true);
				}
				else {
					var displayColumn = tableDisplayColumn.insertRow(-1);
					displayColumn.onclick = onClickDisplayColumn;
					displayColumn.style.cursor = "pointer";
					
					var td = displayColumn.insertCell(-1);
					td.className = "viewData";
					td.innerHTML = sourceColumn.title + 
								   '<input type="hidden" name="columnName" value="' + sourceColumn.name + '">' +
								   '<input type="hidden" name="columnTitle" value="' + sourceColumn.title + '">';
															
					td = displayColumn.insertCell(-1);
					td.className = "viewData";
					td.style.padding = "1px 3px 1px 3px";
					td.innerHTML = '<input name="columnWidth" style="width:100%;" type="text" value="' + sourceColumn.width + '">';
					
					td = displayColumn.insertCell(-1);
					td.className = "viewData";
					td.style.padding = "1px 3px 1px 3px";
					td.innerHTML = '<select name="columnAlign" style="width:100%;">' +
								   '	<option value="left">左对齐' +
								   '	<option value="center"' + (sourceColumn.align=='center' ? ' selected' : '') + '>居中' +
								   '	<option value="right"' + (sourceColumn.align=='right' ? ' selected' : '') + '>右对齐' +
								   '</select>';
					onClickDisplayColumn(displayColumn, true);
				}
				var tableSourceColumn = document.getElementById("tableSourceColumn");
				var nextRow = tableSourceColumn.rows[selectedSourceColumn.rowIndex==tableSourceColumn.rows.length - 1 ? 1 : selectedSourceColumn.rowIndex+1];
				onClickSourceColumn(nextRow);
			}
		}
		function deleteDisplayColumn() {
			if(selectedDisplayColumn) {
				var rowIndex = selectedDisplayColumn.rowIndex;
				var tableDisplayColumn = document.getElementById("tableDisplayColumn");
				tableDisplayColumn.deleteRow(rowIndex);
				if(tableDisplayColumn.rows.length==1) {
					selectedDisplayColumn = null;
				}
				else {
					onClickDisplayColumn(tableDisplayColumn.rows[rowIndex>=tableDisplayColumn.rows.length ? rowIndex-1 : rowIndex], true);
				}
			}
		}
		function moveDisplayColumn(down) {
			if(selectedDisplayColumn) {
				var rowIndex = selectedDisplayColumn.rowIndex;
				var tableDisplayColumn = document.getElementById("tableDisplayColumn");
				if(down) {
					if(rowIndex<tableDisplayColumn.rows.length-1) {
						DomUtils.moveTableRow(tableDisplayColumn, rowIndex, rowIndex + 1);
						tableDisplayColumn.rows[rowIndex + 1].cells[0].focus();
					}
				}
				else {
					if(rowIndex>1) {
						DomUtils.moveTableRow(tableDisplayColumn, rowIndex, rowIndex - 1);
						tableDisplayColumn.rows[rowIndex - 1].cells[0].focus();
					}
				}
			}
		}
		function addSortColumn() {
			if(selectedSourceColumn && sourceColumns[selectedSourceColumn.rowIndex-1].display=="sort") {
				var tableSortColumn = document.getElementById("tableSortColumn"), rows = tableSortColumn.rows;
				var sourceColumn = sourceColumns[selectedSourceColumn.rowIndex-1];
				var i=1;
				for(;i<rows.length && DomUtils.getElement(rows[i], "input", "sortColumnName").value != sourceColumn.name; i++);
				if(i<rows.length) {
					onClickSortColumn(rows[i], true);
				}
				else {
					var sortColumn = tableSortColumn.insertRow(-1);
					sortColumn.onclick = onClickSortColumn;
					sortColumn.style.cursor = "pointer";
				
					var td = sortColumn.insertCell(-1);
					td.className = "viewData";
					td.innerHTML = sourceColumn.title + 
								   '<input type="hidden" name="sortColumnName" value="' + sourceColumn.name + '">' +
								   '<input type="hidden" name="sortColumnTitle" value="' + sourceColumn.title + '">';
															
					td = sortColumn.insertCell(-1);
					td.className = "viewData";
					td.style.padding = "1px 3px 1px 3px";
					td.innerHTML = '<select name="sortColumnDirection" style="width:100%;">' +
								   '	<option value="asc">升序' +
								   '	<option value="desc">降序' +
								   '</select>';
					onClickSortColumn(sortColumn, true);
				}
				var tableSourceColumn = document.getElementById("tableSourceColumn");
				var nextRow = tableSourceColumn.rows[selectedSourceColumn.rowIndex==tableSourceColumn.rows.length - 1 ? 1 : selectedSourceColumn.rowIndex+1];
				onClickSourceColumn(nextRow);
			}
		}
		function deleteSortColumn() {
			if(selectedSortColumn) {
				var rowIndex = selectedSortColumn.rowIndex;
				var tableSortColumn = document.getElementById("tableSortColumn");
				tableSortColumn.deleteRow(rowIndex);
				if(tableSortColumn.rows.length==1) {
					selectedSortColumn = null;
				}
				else {
					onClickSortColumn(tableSortColumn.rows[rowIndex>=tableSortColumn.rows.length ? rowIndex-1 : rowIndex], true);
				}
			}
		}
		function saveViewCustom(apply) {
			var pageRows = FieldValidator.validateNumberField(document.getElementsByName("viewCustom.pageRows")[0], true, "每页显示行数");
			if(pageRows=="NaN") {
				return;
			}
			if(pageRows<1) {
				alert("每页显示行数必须大于零！");
				return;
			}
			if(pageRows>1000) {
				alert("每页显示行数不能超过1000！");
				return;
			}
			//显示列
			if(document.getElementById("tableDisplayColumn").rows.length<2) {
				alert("显示列数必须大于零！");
				return;
			}
			FormUtils.doAction('saveViewCustom', (apply ? 'apply=true' : ''));
		}
	</script>
	<table border="0" height="100%" width="100%" cellspacing="0" cellpadding="0px" style="table-layout:fixed">
		<col width="180px">
		<col width="36px">
		<col width="100%">
		<tr height="30px" valign="top">
			<td colspan="3">
   		   		<table border="0" cellpadding="0" cellspacing="0" width="100%">
       				<tr>
     					<td nowrap>每页显示行数：</td>
						<td width="100%"><ext:field property="viewCustom.pageRows"/></td>
					</tr>
				</table>
			</td>
		</tr>
 	    <tr valign="bottom">
   	     	<td style="padding:0 0 2px 0px">列选择：</td>
   		   	<td></td>
   		   	<td style="padding:0 0 2px 0px">显示列：</td>
   		</tr>
        <tr height="100%" valign="top">
         	<td rowspan="3">
				<div onselectstart="return false;" style="background:white; border:gray 1px solid; width:100%; height:100%; overflow:auto;">
					<table id="tableSourceColumn" class="view" width="100%" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed; display: none;">
						<col align="left" width="100%" class="viewColOdd">
						<tr valign="middle" align="center">
							<td class="viewHeader">列名称</td>
						</tr>
						<ext:iterate id="column" property="sourceColumns">
							<tr onclick="onClickSourceColumn(this)" style="cursor:pointer" bordercolor="E1E8F5" height="22" id="0">
								<td class="viewData"><ext:write name="column" property="title"/></td>
							</tr>
						</ext:iterate>
					</table>
		  		</div>
		  	</td>
       		<td valign="middle" align="center">
       			<div class="selectOneButton" title="增加" onclick="addDisplayColumn()">&gt;</div><br>
        		<div class="deleteOneButton" title="删除" onclick="deleteDisplayColumn()">&lt;</div><br>
        		<br>
        		<div class="moveUp" title="上移" onclick="moveDisplayColumn(false)">∧</div><br>
        		<div class="moveDown" title="下移" onclick="moveDisplayColumn(true)">∨</div>
        	</td>
			<td>
				<div onselectstart="return false;" style="background:white; border:gray 1px solid; width:100%; height:100%; overflow:auto;">
					<table id="tableDisplayColumn" class="view" width="100%" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed;  display: none;">
						<col align="left" width="100%" class="viewColOdd">
						<col align="left" width="64px" class="viewColEven">
						<col align="left" width="72px" class="viewColOdd">
						<tr valign="middle" align="center">
							<td class="viewHeader">列名称</td>
							<td class="viewHeader">列宽</td>
							<td class="viewHeader">对齐方式</td>
						</tr>
						<ext:iterate id="column" property="viewCustom.columns">
							<tr onclick="onClickDisplayColumn(this)" style="cursor:pointer">
								<td class="viewData">
									<ext:write name="column" property="columnTitle"/>
									<input type="hidden" name="columnName" value="<ext:write name="column" property="columnName"/>">
									<input type="hidden" name="columnTitle" value="<ext:write name="column" property="columnTitle"/>">
								</td>
								<td class="viewData" style="padding:1px 3px 1px 3px">
									<input name="columnWidth" style="width:100%;" type="text" value="<ext:write name="column" property="columnWidth"/>">
								</td>
								<td class="viewData" style="padding:1px 3px 1px 3px">
									<select name="columnAlign" style="width:100%;">
										<option value="left">左对齐
										<option value="center" <ext:equal value="center" name="column" property="columnAlign">selected</ext:equal>>居中
										<option value="right" <ext:equal value="right" name="column" property="columnAlign">selected</ext:equal>>右对齐
									</select>
								</td>
							</tr>
						</ext:iterate>
					</table>
		  		</div>
			</td>
        </tr>
        <tr>
        	<td width="36px"></td>
   		   	<td style="padding:6px 0px 2px 0px;">排序列：</td>
        </tr>
        <tr height="108px">
        	<td valign="middle" align="center">
	    		<div class="selectOneButton" title="增加" onclick="addSortColumn()">&gt;</div><br>
        		<div class="deleteOneButton" title="删除" onclick="deleteSortColumn()">&lt;</div>
        	</td>
			<td>
				<div onselectstart="return false;" id="divList" style="background:white; border:gray 1px solid; width:100%; height:100%; overflow:auto;">
					<table id="tableSortColumn" class="view" width="100%" border="0" cellpadding="0" cellspacing="1" style="table-layout:fixed; display: none;">
						<col align="left" width="100%" class="viewColOdd">
						<col align="left" width="80px" class="viewColEven">
						<tr valign="middle" align="center">
							<td class="viewHeader">列名称</td>
							<td class="viewHeader">排序方式</td>
						</tr>
						<ext:iterate id="sortColumn" property="viewCustom.sortColumns">
							<tr onclick="onClickSortColumn(this)" style="cursor:pointer">
								<td class="viewData">
									<ext:write name="sortColumn" property="columnTitle"/>
									<input type="hidden" name="sortColumnName" value="<ext:write name="sortColumn" property="columnName"/>">
									<input type="hidden" name="sortColumnTitle" value="<ext:write name="sortColumn" property="columnTitle"/>">
								</td>
								<td class="viewData" style="padding:1px 3px 1px 3px">
									<select name="sortColumnDirection" style="width:100%;">
										<option value="asc">升序
										<option value="desc" <ext:equal value="true" name="sortColumn" property="desc">selected</ext:equal>>降序
									</select>
								</td>
							</tr>
						</ext:iterate>
					</table>
		  		</div>
			</td>
        </tr>
    </table>
    <script type="text/javascript">
    	EventUtils.addEvent(window, 'load', function() {
    		var list = ['tableSourceColumn', 'tableDisplayColumn', 'tableSortColumn'];
    		for(var i=0; i<list.length; i++) {
    			var element = document.getElementById(list[i]);
				element.parentElement.style.height = element.parentElement.parentElement.offsetHeight + 'px';
				element.style.display = '';
			}
		});
	</script>
 </ext:form>