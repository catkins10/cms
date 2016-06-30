//表格命令
EditorTableCommand = function(name, title, iconIndex) {
	EditorMenuCommand.call(this, name, title, iconIndex);
	this.width = 120; //菜单宽度
	this.height = 200; //菜单高度
	this.autoHeight = true; //是否自动高度
	this.showDropButton = false;
};
EditorTableCommand.prototype = new EditorMenuCommand;
EditorTableCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	if(DomUtils.getParentNode(selectedElement, 'td')) {
		this.constructor.prototype.execute.call(this, toolbarButton, editor, editorWindow, editorDocument, range, selectedElement);
	}
	else {
		this.openTableDialog(true, editor, editorWindow, editorDocument, range, selectedElement);
	}
};
EditorTableCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	var items = [{id:'insertTable', title:'插入表格', iconIndex:80},
				 {id:'tableProperty', title:'表格属性', iconIndex:38},
				 {id:'tableCellProperty', title:'单元格属性', iconIndex:56},
				 {id:'tableInsertRowAfter', title:'下插入行', iconIndex:61},
				 {id:'tableInsertRowBefore', title:'上插入行', iconIndex:69},
				 {id:'tableDeleteRows', title:'删除行', iconIndex:62},
				 {id:'tableInsertColumnAfter', title:'右插入列', iconIndex:63},
				 {id:'tableInsertColumnBefore', title:'左插入列', iconIndex:70},
				 {id:'tableDeleteColumns', title:'删除列', iconIndex:64}];
	var tableCell = DomUtils.getParentNode(selectedElement, 'td');
	var table = tableCell ? DomUtils.getParentNode(tableCell, 'table') : null;
	if(tableCell.cellIndex!=tableCell.parentNode.cells.length-1) {
		items.push({id:'tableMergeRight', title:'右合并单元格', iconIndex:59});
	}
	if(tableCell.parentNode.rowIndex!=table.rows.length-1) {
		items.push({id:'tableMergeDown', title:'下合并单元格', iconIndex:59});
	}
	if(tableCell.colSpan>1 || tableCell.rowSpan>1) {
		items.push({id:'tableSplitCell', title:'拆分单元格', iconIndex:60});
	}
	items.push({id:'tableDelete', title:'删除表格', iconIndex:81});
	return items;
};
EditorTableCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
EditorTableCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	var tableCell = DomUtils.getParentNode(selectedElement, 'td');
	var table = tableCell ? DomUtils.getParentNode(tableCell, 'table') : null;
	if(itemId=='insertTable') { //插入表格
		this.openTableDialog(true, editor, editorWindow, editorDocument, range, selectedElement);
	}
	else if(itemId=='tableProperty') { //表格属性
		this.openTableDialog(false, editor, editorWindow, editorDocument, range, selectedElement);
	}
	else if(itemId=='tableCellProperty') { //单元格属性
		DialogUtils.openDialog(RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/tableCell.shtml', 430, 300, '', HtmlEditor.getDialogArguments());
	}
	else if(itemId=='tableInsertRowAfter') { //下插入行
		this.insertRow(editor, table, tableCell, false);
	}
	else if(itemId=='tableInsertRowBefore') { //上插入行
		this.insertRow(editor, table, tableCell, true);
	}
	else if(itemId=='tableDeleteRows') { //删除行
		editor.saveUndoStep();
		if(table.rows.length>1) {
			table.deleteRow(tableCell.parentNode.rowIndex);
		}
		else {
			table.parentNode.removeChild(table);
		}
	}
	else if(itemId=='tableInsertColumnAfter') { //右插入列
		this.insertColumn(editor, table, tableCell, false);
	}
	else if(itemId=='tableInsertColumnBefore') { //左插入列
		this.insertColumn(editor, table, tableCell, true);
	}
	else if(itemId=='tableDeleteColumns') { //删除列
		editor.saveUndoStep();
		if(tableCell.parentNode.cells.length==1) {
			table.parentNode.removeChild(table);
		}
		else {
			var cellIndex = tableCell.cellIndex;
			for(var i=0; i<table.rows.length; i++) {
				table.rows[i].deleteCell(cellIndex);
			}
		}
	}
	else if(itemId=='tableMergeRight') { //右合并单元格
		editor.saveUndoStep();
		try {
			for(var i=0; i<tableCell.rowSpan; i++) {
				var row = table.rows[tableCell.parentNode.rowIndex + i];
				var cell = row.cells[tableCell.cellIndex + (i==0 ? 1 : 0)];
				for(var j=cell.childNodes.length; j>0; j--) {
					tableCell.appendChild(cell.childNodes[0]);
				}
				i += cell.rowSpan - 1;
				row.deleteCell(cell.cellIndex);
			}
			tableCell.colSpan++;
		}
		catch(e) {
		
		}
	}
	else if(itemId=='tableMergeDown') { //下合并单元格
		editor.saveUndoStep();
		try {
			var row = table.rows[tableCell.parentNode.rowIndex + tableCell.rowSpan];
			for(var i=0; i<tableCell.colSpan; i++) {
				var cell = row.cells[tableCell.cellIndex];
				for(var j=cell.childNodes.length; j>0; j--) {
					tableCell.appendChild(cell.childNodes[0]);
				}
				i += cell.colSpan - 1;
				row.deleteCell(cell.cellIndex);
			}
			tableCell.rowSpan++;
		}
		catch(e) {
		
		}
	}
	else if(itemId=='tableSplitCell') { //拆分单元格
		editor.saveUndoStep();
		for(var i=0; i<tableCell.rowSpan; i++) {
			var row = table.rows[tableCell.parentNode.rowIndex + i];
			for(var j=(i==0 ? 1 : 0); j<tableCell.colSpan; j++) {
				row.insertCell(tableCell.cellIndex + j);
			}
		}
		tableCell.rowSpan = 1;
		tableCell.colSpan = 1;
	}
	else if(itemId=='tableDelete') { //删除表格
		editor.saveUndoStep();
		table.parentNode.removeChild(table);
	}	
};
EditorTableCommand.prototype.openTableDialog = function(isNew, editor, editorWindow, editorDocument, range, selectedElement) { //打开表格对话框
	DialogUtils.openDialog(RequestUtils.getContextPath() + '/jeaf/htmleditor/dialog/table.shtml?newTable=' + isNew, 430, 300, '', HtmlEditor.getDialogArguments());
};
EditorTableCommand.prototype.insertRow = function(editor, table, tableCell, insertBefore) { //插入行
	editor.saveUndoStep();
	var currentRow = tableCell.parentNode;
	var newRow = table.insertRow(currentRow.rowIndex + (insertBefore ? 0 : 1));
	for(var i=0; i<currentRow.cells.length; i++) {
		var newCell = newRow.insertCell(-1);
		this.cloneCell(newCell, currentRow.cells[i]);
	}
};
EditorTableCommand.prototype.insertColumn = function(editor, table, tableCell, insertBefore) { //插入列
	editor.saveUndoStep();
	var cellIndex = tableCell.cellIndex;
	for(var i=0; i<table.rows.length; i++) {
		var currentCell = table.rows[i].cells[cellIndex];
		var newCell = table.rows[i].insertCell(cellIndex + (insertBefore ? 0 : 1));
		this.cloneCell(newCell, currentCell);
	}
};
EditorTableCommand.prototype.cloneCell = function(newCell, currentCell) { //克隆单元格
	newCell.width = currentCell.width;
	newCell.height = currentCell.height;
	if(currentCell.noWrap) {
		newCell.noWrap = currentCell.noWrap;
	}
	if(currentCell.align) {
		newCell.align = currentCell.align;
	}
	if(currentCell.vAlign) {
		newCell.vAlign = currentCell.vAlign;
	}
	if(currentCell.borderColor) {
		newCell.borderColor = currentCell.borderColor;
	}
	if(currentCell.bgColor) {
		newCell.bgColor = currentCell.bgColor;
	}
	if(currentCell.style.cssText) {
		newCell.style.cssText = currentCell.style.cssText;
	}
};