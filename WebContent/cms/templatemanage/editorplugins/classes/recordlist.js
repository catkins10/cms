//记录列表命令
RecordListCommand = function() {
	EditorMenuCommand.call(this, 'recordList', '记录列表', 38);
	this.showDropButton = false;
	this.showTitle = true;
};
RecordListCommand.prototype = new EditorMenuCommand;
RecordListCommand.prototype.execute = function(toolbarButton, editor, editorWindow, editorDocument, range, selectedElement) { //执行命令
	if((document.getElementsByName('searchPage')[0] && document.getElementsByName('searchPage')[0].value=="true") ||
	   (selectedElement && selectedElement.id=="recordList")) {
		this.constructor.prototype.execute.call(this, toolbarButton, editor, editorWindow, editorDocument, range, selectedElement);
	}
	else {
		this.insertRecordList();
	}
};
RecordListCommand.prototype.getState = function(editor, sourceCodeMode, editorWindow, editorDocument, range, selectedElement) { //获取状态
	return range ? HtmlEditor.TOOLBAR_BUTTON_STATE_NORMAL : HtmlEditor.TOOLBAR_BUTTON_STATE_DISABLED;
};
RecordListCommand.prototype.createItems = function(editor, editorWindow, editorDocument, range, selectedElement) { //获取菜单项目列表,由继承者实现,返回{id:[ID], html:[HTML], title, iconIndex, iconUrl}列表, 指定html时, title、iconIndex、iconUrl无效
	var items = [{id:'insertRecordList', title:'插入记录列表', iconIndex:80}];
	if(document.getElementsByName('searchPage')[0] && document.getElementsByName('searchPage')[0].value=="true") { //搜索页面
		items.push({id:'insertSearchResult', title:'插入搜索结果', iconIndex:4});
	}
	if(selectedElement && selectedElement.id=="recordList") {
		items.push({id:'editRecordList', title:'编辑记录列表', iconIndex:38});
		items.push({id:'deleteRecordList', title:'删除记录列表', iconIndex:81});
	}
	return items;
};
RecordListCommand.prototype.onclick = function(itemId, editor, editorWindow, editorDocument, range, selectedElement) { //点击菜单项目,由继承者实现
	if(itemId=="insertRecordList") { //插入记录列表
		this.insertRecordList();
	}
	else if(itemId=="insertSearchResult") { //插入搜索结果
		RecordListCommand.doInsertRecordList(document.getElementsByName("applicationName")[0].value, document.getElementsByName("searchResultsName")[0].value, "搜索结果", true, false);
	}
	else if(itemId=="editRecordList") { //编辑记录列表
		this.editRecordList();
	}
	else if(itemId=="deleteRecordList") { //删除记录列表
		this.deleteRecordList();
	}
};
RecordListCommand.prototype.insertRecordList = function() { //插入记录列表
	var applicationName = document.getElementsByName("applicationName")[0];
	var pageName = document.getElementsByName("pageName")[0];
	var url = RequestUtils.getContextPath() + "/cms/sitemanage/selectView.shtml" +
			  "?currentApplicationName=" + (applicationName ?  applicationName.value : "") +
			  "&currentPageName=" + (pageName ?  pageName.value : "") +
			  "&script=" + StringUtils.utf8Encode("RecordListCommand.doInsertRecordList('{id}'.split('__')[0], '{id}'.split('__')[1], '{name}', false, '{private}', '{recordClassName}')");
	DialogUtils.openDialog(url, 550, 360);
};
RecordListCommand.prototype.editRecordList = function() { //编辑记录列表
	//获取父元素
	if(!HtmlEditor.selectedElement || HtmlEditor.selectedElement.id!='recordList') {
		alert("未选定记录列表");
		return;
	}
	var applicationName = '';
	var recordListName = '';
	var privateRecordList = '';
	var recordClassName = '';
	var urn = HtmlEditor.selectedElement.getAttribute("urn");
	var index = urn.indexOf('applicationName=');
	if(index!=-1) {
		index += 'applicationName='.length;
		var indexEnd = urn.indexOf("&", index);
		applicationName = indexEnd==-1 ?  urn.substring(index) : urn.substring(index, indexEnd);
	}
	index = urn.indexOf('recordListName=');
	if(index!=-1) {
		index += 'recordListName='.length;
		var indexEnd = urn.indexOf("&", index);
		recordListName = indexEnd==-1 ? urn.substring(index) : urn.substring(index, indexEnd);
	}
	index = urn.indexOf('privateRecordList=');
	if(index!=-1) {
		index += 'privateRecordList='.length;
		var indexEnd = urn.indexOf("&", index);
		privateRecordList = indexEnd==-1 ? urn.substring(index) : urn.substring(index, indexEnd);
	}
	index = urn.indexOf('recordClassName=');
	if(index!=-1) {
		index += 'recordClassName='.length;
		var indexEnd = urn.indexOf("&", index);
		recordClassName = indexEnd==-1 ?  urn.substring(index) : urn.substring(index, indexEnd);
	}
	if(recordListName=='') {
		alert("记录列表格式不正确");
		return;
	}
	RecordListCommand.doInsertRecordList(applicationName, recordListName, HtmlEditor.selectedElement.innerHTML.replace(/&lt;/gi, "").replace(/&gt;/gi, ""), urn.indexOf("searchResults=true")!=-1, privateRecordList, recordClassName);
};
RecordListCommand.prototype.deleteRecordList = function() { //删除记录列表
	if(!HtmlEditor.selectedElement || HtmlEditor.selectedElement.id!='recordList') {
		alert("未选定记录列表");
		return;
	}
	if(confirm("是否确定删除记录列表<" + HtmlEditor.selectedElement.innerHTML.replace(/&[^;]*;/gi, "") + ">")) {
		HtmlEditor.editor.saveUndoStep();
		HtmlEditor.selectedElement.parentNode.removeChild(HtmlEditor.selectedElement);
	}
};
RecordListCommand.doInsertRecordList = function(applicationName, recordListName, title, isSearchResults, privateRecordList, recordClassName) {
	var themeType = document.getElementsByName("themeType")[0];
	var dialogUrl = RequestUtils.getContextPath() + "/cms/templatemanage/dialog/insertRecordList.shtml" +
					"?recordListName=" + recordListName +
					(applicationName!="" ? "&applicationName=" + applicationName : "") +
					(isSearchResults ? "&searchResults=true" : "") +
					"&themeType=" + (themeType ?  themeType.value : "") +
					(privateRecordList=="true" ? "&privateRecordList=true&recordClassName=" + recordClassName : "");
	DialogUtils.openDialog(dialogUrl, 780, 400, '', HtmlEditor.getDialogArguments());
};
HtmlEditor.registerCommand(new RecordListCommand());