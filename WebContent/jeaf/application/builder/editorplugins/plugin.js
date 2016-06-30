//插入表格
FormCustomTableCommand = function() {
	this.name = "formCustomTable";
	this.title = "插入表格";
	this.iconIndex = -1;
	this.iconUrl = RequestUtils.getContextPath() + "/jeaf/application/builder/editorplugins/icons/insertFormTable.gif";
	this.showTitle = true;
};
FormCustomTableCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
FormCustomTableCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	var fieldCount = this.getFields(1).length;
	var cols = Math.min(3, Math.max(1, Math.ceil(fieldCount / 8))); //按8行计算
	var inputs = [{name:'cols', title:'每行字段数', inputMode:'text', defaultValue:cols},
				  {name:'insertFields', title:'自动插入字段', inputMode:'radio', defaultValue:'yes', itemsText:'插入|yes\\0不插入|no'}];
	var command = this;
	DialogUtils.openInputDialog('插入表格', inputs, 430, 200, '', function(values) {
		editor.saveUndoStep();
		var cols = Number(values.cols);
		if(isNaN(cols)) {
			cols = 2;
		}
		else if(cols<1) {
			cols = 1;
		}
		var fields = command.getFields(cols); //获取字段列表
		var table = DomUtils.createElement(editorWindow, range, "table");
		if(!table) {
			alert("表格创建失败,请重新选择插入位置。");
			return;
		}
		table.width = "100%";
		table.border = "1";
		table.cellPadding ="0";
		table.cellSpacing = "0";
		table.className = "table";
		if(values.insertFields!='yes') { //不插入字段
			for(var i=0; i<fields.length; i+=cols) {
				var tr = table.insertRow(-1);
				for(var j=0; j<cols; j++) {
					var td = tr.insertCell(-1);
					td.className = "tdtitle";
					td.noWrap = true;
					td = tr.insertCell(-1);
					td.className = "tdcontent";
					td.width = Math.floor(100/cols) + "%";
				}
			}
		}
		else { //插入字段
			var tr, cellIndex = 0, previousCell;
			for(var i=0; i<fields.length; i++) {
				if(cellIndex%cols==0) {
					tr = table.insertRow(-1);
				}
				cellIndex++;
				if(!fields[i] || !fields[i].id) { //空字段
					previousCell.colSpan = (!previousCell.colSpan ? 3 : Number(previousCell.colSpan) + 2);
					previousCell.removeAttribute("width");
					continue;
				}
				//创建标签单元格
				var tdLabel = tr.insertCell(-1);
				tdLabel.className = "tdtitle";
				tdLabel.noWrap = true;
				tdLabel.innerHTML = fields[i].label;
				//创建内容单元格
				var tdContent = tr.insertCell(-1);
				tdContent.width = Math.floor(100/cols) + "%";
				tdContent.className = "tdcontent";
				tdContent.innerHTML = '<a id="formField" urn="' + fields[i].id + '">&lt;' + fields[i].label + "&gt;</a>";
				previousCell = tdContent;
			}
		}
	});
};
FormCustomTableCommand.prototype.getFields = function(cols) {
	var fields = [];
	var rows = document.getElementById("tableFields").rows;
	for(var i=1; i<rows.length; i++) {
		var values = rows[i].id.split("_");
		if(values[1]=="" || ",hidden,none,".indexOf("," + values[1] + ",")!=-1 || (values[2]!="1" && values[1]=="readonly")) {
			continue;
		}
		var field = {id:values[0], inputMode:values[1], label:rows[i].cells[1].innerHTML};
		if(",textarea,htmleditor,attachment,image,video".indexOf("," + field.inputMode + ",")!=-1) {  //是否占据一个整行
			for(var j=0; ; j+=cols) {
				if(!fields[j-j%cols]) {
					var index = j-j%cols;
					fields[index] = field;
					for(var k=1; k<cols; k++) {
						fields[index+k] = {};
					}
					break;
				}
			}
		}
		else {
			for(var j=0; ; j++) {
				if(!fields[j]) {
					fields[j] = field;
					break;
				}
			}
		}
	}
	fields.length = Math.ceil(fields.length/cols) * cols;
	return fields;
};
HtmlEditor.registerCommand(new FormCustomTableCommand());

//插入字段
FormCustomFieldCommand = function(name, title, iconUrl) {
	EditorMenuCommand.call(this, name, title, -1, iconUrl);
	this.showDropButton = false;
	this.showTitle = true;
};
FormCustomFieldCommand.prototype = new EditorMenuCommand;
FormCustomFieldCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
FormCustomFieldCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	var items = [];
	var rows = document.getElementById("tableFields").rows;
	for(var i=1; i<rows.length; i++) {
		items.push({id:i, title:rows[i].cells[1].innerHTML, iconIndex:-1});
	}
	return items;
};
FormCustomFieldCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	editor.saveUndoStep();
	var field = DomUtils.getParentNode(selectedElement, 'a') ;
	if(!field) {
		field = DomUtils.createLink(editorWindow, range);
	}
	var row = document.getElementById("tableFields").rows[Number(itemId)];
	if("formCustomLabel"==this.name) { //插入标签
		field.parentNode.replaceChild(editorDocument.createTextNode(row.cells[1].innerHTML), field);
	}
	else { //插入字段
		field.id = "formField";
		field.setAttribute("urn", row.id.split('_')[0]);
		field.innerHTML = "&lt;" + row.cells[1].innerHTML + "&gt;";
	}
};
HtmlEditor.registerCommand(new FormCustomFieldCommand('formCustomLabel', '插入标签', RequestUtils.getContextPath() + "/jeaf/application/builder/editorplugins/icons/insertFieldLabel.gif"));
HtmlEditor.registerCommand(new FormCustomFieldCommand('formCustomField', '插入字段', RequestUtils.getContextPath() + "/jeaf/application/builder/editorplugins/icons/insertField.gif"));